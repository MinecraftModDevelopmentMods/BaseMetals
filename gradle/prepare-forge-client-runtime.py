#!/usr/bin/env python3
"""Prepare a checksum-verified official Forge client runtime for packaged smoke tests."""

from __future__ import annotations

import argparse
import concurrent.futures
import hashlib
import json
import os
import platform
import shutil
import subprocess
import sys
import time
import urllib.request
import zipfile
from pathlib import Path
from typing import Any

MINECRAFT_VERSION = "1.18.2"
FORGE_VERSION = "40.3.0"
FORGE_INSTALLER_SHA256 = "9434c29790504dcd11ce97cb30dd8891b4b18848982357bc1b5abfa79fb95103"
VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json"
ASSET_OBJECT_URL = "https://resources.download.minecraft.net/{prefix}/{digest}"
USER_AGENT = "BaseMetals-release-validation/3.1.1.118021"


def digest_file(path: Path, algorithm: str) -> str:
    digest = hashlib.new(algorithm)
    with path.open("rb") as source:
        for block in iter(lambda: source.read(1024 * 1024), b""):
            digest.update(block)
    return digest.hexdigest()


def download(url: str, target: Path, algorithm: str | None = None,
             expected: str | None = None) -> Path:
    if target.is_file() and (expected is None or digest_file(target, algorithm or "sha1") == expected):
        return target

    target.parent.mkdir(parents=True, exist_ok=True)
    temporary = target.with_name(target.name + ".part")
    for attempt in range(1, 4):
        try:
            request = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
            with urllib.request.urlopen(request, timeout=90) as response, temporary.open("wb") as output:
                shutil.copyfileobj(response, output)
            if expected is not None:
                actual = digest_file(temporary, algorithm or "sha1")
                if actual != expected:
                    raise RuntimeError(
                        f"Checksum mismatch for {url}: expected {expected}, found {actual}")
            os.replace(temporary, target)
            return target
        except Exception:
            temporary.unlink(missing_ok=True)
            if attempt == 3:
                raise
            time.sleep(attempt * 2)
    raise AssertionError("unreachable")


def load_json(path: Path) -> dict[str, Any]:
    with path.open("r", encoding="utf-8") as source:
        return json.load(source)


def current_os() -> tuple[str, str]:
    system = platform.system().lower()
    if system.startswith("win"):
        os_name = "windows"
    elif system == "darwin":
        os_name = "osx"
    else:
        os_name = "linux"

    machine = platform.machine().lower()
    arch = "x86" if machine in {"x86", "i386", "i686"} else machine
    return os_name, arch


def rule_matches(rule: dict[str, Any], os_name: str, arch: str) -> bool:
    if rule.get("features"):
        return False
    required_os = rule.get("os")
    if required_os is None:
        return True
    if required_os.get("name") not in {None, os_name}:
        return False
    if required_os.get("arch") not in {None, arch}:
        return False
    version_pattern = required_os.get("version")
    if version_pattern is not None:
        import re
        if re.search(version_pattern, platform.version()) is None:
            return False
    return True


def library_allowed(library: dict[str, Any], os_name: str, arch: str) -> bool:
    rules = library.get("rules")
    if not rules:
        return True
    allowed = False
    for rule in rules:
        if rule_matches(rule, os_name, arch):
            allowed = rule.get("action") == "allow"
    return allowed


def install_forge_client(java: Path, installer: Path, runtime: Path) -> None:
    profile = runtime / "launcher_profiles.json"
    if not profile.exists():
        profile.write_text(json.dumps({
            "profiles": {},
            "selectedProfile": None,
            "clientToken": "00000000-0000-0000-0000-000000000000",
            "authenticationDatabase": {},
            "launcherVersion": {"name": "basemetals-validation", "format": 21},
        }, separators=(",", ":")), encoding="utf-8")

    completed = subprocess.run(
        [str(java), "-jar", str(installer), "--installClient", str(runtime)],
        text=True,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        check=False,
    )
    if completed.returncode != 0:
        tail = "\n".join(completed.stdout.splitlines()[-200:])
        raise RuntimeError(f"Forge client installation failed:\n{tail}")
    if "Successfully installed client into launcher." not in completed.stdout:
        raise RuntimeError("Forge installer did not report successful client installation")
    print("Forge installer completed and validated its downloads.")


def prepare_minecraft_metadata(runtime: Path) -> tuple[dict[str, Any], dict[str, Any], str]:
    metadata_dir = runtime / ".validation-metadata"
    manifest_file = download(VERSION_MANIFEST_URL, metadata_dir / "version_manifest_v2.json")
    manifest = load_json(manifest_file)
    version = next((entry for entry in manifest["versions"]
                    if entry["id"] == MINECRAFT_VERSION), None)
    if version is None:
        raise RuntimeError(f"Minecraft {MINECRAFT_VERSION} is absent from the official manifest")

    base_json_file = download(
        version["url"],
        runtime / "versions" / MINECRAFT_VERSION / f"{MINECRAFT_VERSION}.json",
        "sha1",
        version["sha1"],
    )
    base_json = load_json(base_json_file)

    forge_version_id = next((
        candidate for candidate in (
            f"{MINECRAFT_VERSION}-forge-{FORGE_VERSION}",
            f"forge-{FORGE_VERSION}",
        )
        if (runtime / "versions" / candidate / f"{candidate}.json").is_file()
    ), None)
    if forge_version_id is None:
        raise RuntimeError(f"Forge {FORGE_VERSION} client profile was not installed")
    forge_json = load_json(
        runtime / "versions" / forge_version_id / f"{forge_version_id}.json")
    return base_json, forge_json, forge_version_id


def prepare_client_jar(runtime: Path, base_json: dict[str, Any]) -> None:
    client = base_json["downloads"]["client"]
    download(
        client["url"],
        runtime / "versions" / MINECRAFT_VERSION / f"{MINECRAFT_VERSION}.jar",
        "sha1",
        client["sha1"],
    )


def prepare_libraries(runtime: Path, metadata: list[dict[str, Any]],
                      forge_version_id: str) -> int:
    os_name, arch = current_os()
    arch_bits = "32" if arch == "x86" else "64"
    native_archives: list[tuple[Path, tuple[str, ...]]] = []
    artifact_count = 0

    for document in metadata:
        for library in document.get("libraries", []):
            if not library_allowed(library, os_name, arch):
                continue
            downloads = library.get("downloads", {})
            artifact = downloads.get("artifact")
            if artifact:
                target = runtime / "libraries" / artifact["path"]
                url = artifact.get("url")
                if url:
                    download(url, target, "sha1", artifact.get("sha1"))
                elif not target.is_file():
                    raise RuntimeError(f"Missing installed Forge library: {target}")
                elif artifact.get("sha1") and digest_file(target, "sha1") != artifact["sha1"]:
                    raise RuntimeError(f"Installed Forge library checksum mismatch: {target}")
                artifact_count += 1

            classifier_template = library.get("natives", {}).get(os_name)
            if classifier_template is None:
                continue
            classifier = classifier_template.replace("${arch}", arch_bits)
            native = downloads.get("classifiers", {}).get(classifier)
            if native is None:
                raise RuntimeError(
                    f"Missing native classifier {classifier} for {library.get('name')}")
            archive = runtime / "libraries" / native["path"]
            download(native["url"], archive, "sha1", native.get("sha1"))
            native_archives.append((archive, tuple(library.get("extract", {}).get("exclude", []))))

    natives = runtime / "natives" / forge_version_id
    if natives.exists():
        shutil.rmtree(natives)
    natives.mkdir(parents=True)

    natives_root = natives.resolve()
    for archive, excludes in native_archives:
        with zipfile.ZipFile(archive) as zipped:
            for member in zipped.infolist():
                name = member.filename.replace("\\", "/")
                if member.is_dir() or any(name.startswith(prefix) for prefix in excludes):
                    continue
                target = (natives / name).resolve()
                if natives_root not in target.parents:
                    raise RuntimeError(f"Unsafe native archive member: {name}")
                target.parent.mkdir(parents=True, exist_ok=True)
                with zipped.open(member) as source, target.open("wb") as output:
                    shutil.copyfileobj(source, output)
    return artifact_count


def prepare_assets(runtime: Path, base_json: dict[str, Any]) -> int:
    index = base_json["assetIndex"]
    index_file = download(
        index["url"],
        runtime / "assets" / "indexes" / f"{index['id']}.json",
        "sha1",
        index["sha1"],
    )
    objects = load_json(index_file)["objects"]
    unique_hashes = sorted({entry["hash"] for entry in objects.values()})

    def fetch(digest: str) -> None:
        download(
            ASSET_OBJECT_URL.format(prefix=digest[:2], digest=digest),
            runtime / "assets" / "objects" / digest[:2] / digest,
            "sha1",
            digest,
        )

    with concurrent.futures.ThreadPoolExecutor(max_workers=16) as pool:
        list(pool.map(fetch, unique_hashes))
    return len(unique_hashes)


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--installer", required=True, type=Path)
    parser.add_argument("--runtime", required=True, type=Path)
    parser.add_argument("--java", required=True, type=Path)
    args = parser.parse_args()

    installer = args.installer.resolve()
    runtime = args.runtime.resolve()
    java = args.java.resolve()
    if not installer.is_file():
        raise RuntimeError(f"Forge installer is missing: {installer}")
    if not java.is_file():
        raise RuntimeError(f"Java executable is missing: {java}")
    actual_installer = digest_file(installer, "sha256")
    if actual_installer != FORGE_INSTALLER_SHA256:
        raise RuntimeError(
            f"Forge installer checksum mismatch: expected {FORGE_INSTALLER_SHA256}, "
            f"found {actual_installer}")

    runtime.mkdir(parents=True, exist_ok=True)
    install_forge_client(java, installer, runtime)
    base_json, forge_json, forge_version_id = prepare_minecraft_metadata(runtime)
    prepare_client_jar(runtime, base_json)
    libraries = prepare_libraries(runtime, [base_json, forge_json], forge_version_id)
    assets = prepare_assets(runtime, base_json)
    print(
        f"Prepared official Minecraft {MINECRAFT_VERSION}/Forge {FORGE_VERSION} "
        f"client runtime with {libraries} libraries and {assets} asset objects.")
    return 0


if __name__ == "__main__":
    try:
        raise SystemExit(main())
    except Exception as exception:
        print(f"ERROR: {exception}", file=sys.stderr)
        raise SystemExit(1)
