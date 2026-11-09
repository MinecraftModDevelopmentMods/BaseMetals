"""Build disposable upgrade worlds from public releases, then test the packaged candidate."""

import argparse
import hashlib
import json
import subprocess
import sys
import time
import urllib.error
import urllib.request
from pathlib import Path

from run_orespawn_checks import JAR_NAME, digest, prepare, reject_dependency, run


ROOT = Path(__file__).resolve().parents[3]
FIXTURES = Path(__file__).with_name("fixtures")
RELEASES = {
    "mmd": ("base-metals-240967", "2533178", "ABDF704023100AD6BB70351CE72C31A07943AD5BCE8308155564194A25089CCC"),
    "os3": ("mmd-orespawn-245586", "2533159", "05CC48E59C7B20ED9B7F29AE014F5E2E5B2F16F289EF7D6F274F017B5E2A6F77"),
    "orespawn": ("mmd-orespawn-245586", "9524249", "4246D1EB83FFFC325F04E3D3FE331D1404714F4A8D773ADE278A5D0EBEE5EDBC"),
    "old_os4": ("mmd-orespawn-245586", "8840593", "51FF282D41E3C566DB2541CCAD53968030EA12E985A5CEE7307162BBF560C99A"),
    "modern_metals": ("modern-metals-244357", "2534044", "8F5011542650A03A7B25E4BCB410D676A5FB6AD705B91552E494A08A6C43087A"),
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


def capture(java, directory, phase, marker):
    log = directory / f"{phase}-console.txt"
    with log.open("wb") as output:
        result = subprocess.run([str(java), "-Xms256M", "-Xmx2G", "-jar",
                                 JAR_NAME, "nogui"],
                                cwd=directory, stdout=output, stderr=subprocess.STDOUT, timeout=360)
    manifest = directory / "world/legacy_registry_manifest_runtime.json"
    if result.returncode or not manifest.is_file() or marker not in log.read_text(errors="replace"):
        raise AssertionError(f"Legacy fixture capture failed: {log}")


def compare_saved_block_states(source, target):
    def states(directory):
        manifest = directory / "world/legacy_registry_manifest_runtime.json"
        return json.loads(manifest.read_text(encoding="utf-8"))["blocks"]

    before, after = states(source), states(target)
    for block in after:
        if block["id"].endswith("_slab"):
            # The 1.10-based model uses a single named variant; it uses no saved metadata bits.
            for state in block["states"]:
                props = state["saved_properties"]
                if props.get("variant") == "default":
                    del props["variant"]
    if before != after:
        raise AssertionError("Saved block metadata or properties changed")


def check_fantasy_modes(args, mods):
    modes = {}
    for name, setting in (("legacy", ""), ("high", "S:contentMode=high_fantasy"),
                          ("low", "S:contentMode=low_fantasy")):
        target = args.output / ("fantasy-" + name)
        prepare(target, args.template, mods)
        config = target / "config/BaseMetals.cfg"
        config.parent.mkdir()
        config.write_text(f"general {{\n    B:crackhammer_full_stacks=true\n    {setting}\n}}\n", encoding="utf-8")
        modes[name] = run(args.java, target, "first", {"fantasy.serverProbe": "true"}, report_kind="fantasy")
        if "B:crackhammer_full_stacks=true" not in config.read_text(encoding="utf-8"):
            raise AssertionError("Reading the Fantasy Level changed an existing setting")
        profile = json.loads((target / "world/serverconfig/orespawn-worldgen.json").read_text(encoding="utf-8"))
        modes[name]["ores"] = {key: value for key, value in profile["ores"].items() if key.startswith("basemetals:")}

    high, low = modes["high"], modes["low"]
    if modes["legacy"] != high:
        raise AssertionError("A config without Fantasy Level did not behave like High Fantasy")
    if high["registry"] != low["registry"] or high["ores"] != low["ores"] or len(low["ores"]) != 11:
        raise AssertionError("Low Fantasy changed the registry or ore rules")
    for key in ("basemetals:adamantine_bow", "basemetals:steel_crossbow",
                "basemetals:steel_fishing_rod", "basemetals:pewter_chestplate"):
        if key not in high["crafting"] or key in low["crafting"]:
            raise AssertionError(f"Incorrect crafting availability for {key}")
    for key in ("basemetals:adamantine_pickaxe", "basemetals:steel_sword", "minecraft:bow"):
        if key not in high["crafting"] or high["crafting"][key] != low["crafting"].get(key):
            raise AssertionError(f"Low Fantasy unexpectedly changed {key}")
    for key in ("basemetals:brass_pickaxe", "basemetals:electrum_sword"):
        if key not in high["trades"] or key in low["trades"]:
            raise AssertionError(f"Incorrect villager trade availability for {key}")
    print("BASE_METALS_FANTASY_MODES_PASS", flush=True)


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
    mod = ROOT / "build/libs/BaseMetals-2.5.1.111021.jar"
    probes = ROOT / "build/test-mods"

    def script(name, *arguments):
        subprocess.run([sys.executable, str(Path(__file__).with_name(name)),
                        "--java", str(args.java), "--template", str(args.template),
                        "--mod", str(mod), "--orespawn", str(jars["orespawn"]),
                        "--probe", str(probes / "orespawn-probe-packaged.jar"), *map(str, arguments)], check=True)

    launch = probes / "launch-probe-packaged.jar"
    reject_dependency(args.java, args.output / "old-os4-rejected", args.template, [mod, jars["old_os4"]])
    check_fantasy_modes(args, [mod, jars["orespawn"], probes / "fantasy-probe-packaged.jar"])
    for name, addons in (("base-only", []), ("modern-metals", [jars["modern_metals"]])):
        target = args.output / name
        prepare(target, args.template, [mod, jars["orespawn"], launch, *addons])
        for phase in ("fresh", "reload"):
            run(args.java, target, phase, {"launch.phase": phase, "launch.moltenContact": "true",
                    "launch.modernMetals": "true" if addons else "false"}, report_kind="launch")

    probe = probes / "legacy-capture-packaged.jar"
    source = args.output / "old-1.11.2"
    prepare(source, args.template, [jars["mmd"], jars["os3"], probe])
    properties = source / "server.properties"
    properties.write_text(properties.read_text(encoding="utf-8") + "generate-structures=false\n", encoding="utf-8")
    capture(args.java, source, "capture", "BASEMETALS_LEGACY_CAPTURE PASS")
    capture(args.java, source, "control-reload", "BASEMETALS_LEGACY_CAPTURE_RELOAD PASS")
    before = {p.relative_to(source / "world"): digest(p) for p in (source / "world").rglob("*") if p.is_file()}
    target = args.output / "upgraded-1.11.2"
    prepare(target, args.template, [mod, jars["orespawn"], probe], source / "world")
    for phase in ("first", "reload"):
        capture(args.java, target, phase, "BASEMETALS_LEGACY_CAPTURE_RELOAD PASS")
        compare_saved_block_states(source, target)
    after = {p.relative_to(source / "world"): digest(p) for p in (source / "world").rglob("*") if p.is_file()}
    if before != after:
        raise AssertionError("The source fixture was modified")

    script("run_orespawn_checks.py", "--output", args.output / "worldgen",
           "--os3", FIXTURES / "orespawn3-basemetals.json", "--os1", FIXTURES / "orespawn1-basemetals.json",
           "--old-mod", jars["mmd"], "--old-orespawn", jars["os3"], "--launch-probe", launch)
    script("run_vanilla_takeover_checks.py", "--output", args.output / "takeover")
    print("BASE_METALS_CI_RUNTIME_PASS", flush=True)


if __name__ == "__main__":
    main()
