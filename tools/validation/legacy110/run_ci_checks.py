"""Build disposable upgrade worlds from public releases, then test the packaged candidate."""

import argparse
import hashlib
import subprocess
import sys
import time
import urllib.error
import urllib.request
from pathlib import Path

from run_orespawn_checks import digest, prepare, run


ROOT = Path(__file__).resolve().parents[3]
FIXTURES = Path(__file__).with_name("fixtures")
RELEASES = {
    "cyano": ("base-metals-240967", "2323100", "F919CEF794C73F02758BBD967346DBA0B53F6383153C7E29BD79F211CDA8C319"),
    "mmd": ("base-metals-240967", "2533174", "51F04770C316E62D9ED3EF713144F02FA2EC899645813627091365E12F61C3EF"),
    "mmdlib": ("mmdlib-261744", "2400405", "286149F4E8132535135C27285A750916D35AB3ECDDB28AE4475FC604D1FDF178"),
    "os3": ("mmd-orespawn-245586", "2533153", "D964148C20175E16CCEFAB02AE43A181A50DC08A1D636A77E59CFE8C8EA8D41A"),
    "os1": ("mmd-orespawn-245586", "2323091", "91345E4B825AA06E05F470066F467AABA0C7A2804A8DBCA4D3CD1905ED53607C"),
    "orespawn": ("mmd-orespawn-245586", "9276430", "4D4975421FEEBF17505056306D06B0344D7A4553B6275885D7C5C5065B0348A6"),
}


def download(directory, name):
    artifact, version, expected = RELEASES[name]
    target = directory / f"{artifact}-{version}.jar"
    if not target.exists():
        url = f"https://www.cursemaven.com/curse/maven/{artifact}/{version}/{target.name}"
        request = urllib.request.Request(url, headers={"User-Agent": "Mozilla/5.0"})
        for attempt in range(3):
            try:
                with urllib.request.urlopen(request, timeout=30) as response:
                    data = response.read()
                break
            except (OSError, urllib.error.URLError):
                if attempt == 2:
                    raise
                print(f"Retrying public fixture download: {name}", flush=True)
                time.sleep(2)
        if hashlib.sha256(data).hexdigest().upper() != expected:
            raise AssertionError(f"Public fixture checksum mismatch: {name}")
        target.write_bytes(data)
    if digest(target).upper() != expected:
        raise AssertionError(f"Fixture checksum mismatch: {target}")
    print("Verified public fixture:", name, expected, flush=True)
    return target


def capture(java, directory):
    log = directory / "capture-console.txt"
    with log.open("wb") as output:
        result = subprocess.run([str(java), "-Xms256M", "-Xmx2G", "-jar",
                                 "forge-1.10.2-12.18.3.2511-universal.jar", "nogui"],
                                cwd=directory, stdout=output, stderr=subprocess.STDOUT, timeout=360)
    manifest = directory / "legacy_registry_manifest_runtime.json"
    if result.returncode or not manifest.is_file() or "BASEMETALS_LEGACY_CAPTURE" not in log.read_text(errors="replace"):
        raise AssertionError(f"Legacy fixture capture failed: {log}")


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("java", "template", "output"):
        parser.add_argument("--" + name, type=Path, required=True)
    args = parser.parse_args()
    args.output = args.output.resolve()
    args.output.mkdir(parents=True, exist_ok=False)
    downloads = args.output / "downloads"
    downloads.mkdir()
    jars = {name: download(downloads, name) for name in RELEASES}
    mod = ROOT / "build/libs/BaseMetals-2.5.1.110021.jar"
    probes = ROOT / "build/test-mods"

    def script(name, *arguments):
        subprocess.run([sys.executable, str(Path(__file__).with_name(name)),
                        "--java", str(args.java), "--template", str(args.template),
                        "--mod", str(mod), "--orespawn", str(jars["orespawn"]),
                        "--probe", str(probes / "orespawn-probe-packaged.jar"), *map(str, arguments)], check=True)

    script("run_orespawn_checks.py", "--output", args.output / "worldgen",
           "--os3", FIXTURES / "orespawn3-basemetals.json", "--os1", FIXTURES / "orespawn1-basemetals.json",
           "--old-mod", jars["mmd"], "--old-orespawn", jars["os3"],
           "--old-support-mod", jars["mmdlib"], "--old-version", "2.5.0-beta4",
           "--launch-probe", probes / "launch-probe-packaged.jar")
    script("run_vanilla_takeover_checks.py", "--output", args.output / "takeover")

    for family in ("cyano", "mmd"):
        source = args.output / (family + "-source")
        support = [jars["mmdlib"], jars["os3"]] if family == "mmd" else [jars["os1"]]
        prepare(source, args.template, [jars[family], *support, probes / "legacy-capture-packaged.jar"])
        # Villages can otherwise populate over the remote block-state test grid.
        properties = source / "server.properties"
        properties.write_text(properties.read_text(encoding="utf-8") + "generate-structures=false\n", encoding="utf-8")
        capture(args.java, source)
        capture(args.java, source)
        before = {p.relative_to(source / "world"): digest(p) for p in (source / "world").rglob("*") if p.is_file()}
        target = args.output / (family + "-upgraded")
        prepare(target, args.template, [mod, jars["orespawn"], probes / "legacy110-probe.jar"], source / "world")
        manifest = source / "world/legacy_registry_manifest_runtime.json"
        later = target / "world/region/r.-2.2.mca"
        unopened = digest(later)
        for phase in ("first", "reload", "delayed", "stable"):
            run(args.java, target, phase, {"review.phase": phase, "review.manifest": manifest}, report_kind="review")
            if phase in ("first", "reload") and digest(later) != unopened:
                raise AssertionError("An unopened fixture region changed during upgrade")
        after = {p.relative_to(source / "world"): digest(p) for p in (source / "world").rglob("*") if p.is_file()}
        if before != after:
            raise AssertionError("The source fixture was modified")
    print("BASE_METALS_CI_RUNTIME_PASS", flush=True)


if __name__ == "__main__":
    main()
