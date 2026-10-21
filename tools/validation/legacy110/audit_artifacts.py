"""Check the namespace move against the previous complete production jar."""

import argparse
import hashlib
import json
import struct
import zipfile
from pathlib import Path


OLD = "com/mcmoddev/basemetals/"
CURRENT = "zone/moddev/mc/basemetals/"
ADDITIONS = {CURRENT + "compat/" + name + ".class" for name in (
    "BaseMetalsLoadingPlugin", "CyanoMaterials", "LegacyNamespaceTransformer",
    "LegacyNamespaceTransformer$1")}
ADDITIONS.update("cyano/basemetals/init/" + name + ".class" for name in ("Blocks", "Items"))
ADDITIONS.add(CURRENT + "worldgen/BaseMetalsWorldgenProvider.class")
REMOVALS = {"com/mcmoddev/orespawn/BaseMetalsOreSpawn.class"}
REMOVALS.update("com/mcmoddev/lib/oregen/" + name + ".class" for name in (
    "FallbackGenerator", "FallbackGeneratorData", "FallbackGeneratorData$1", "FallbackGeneratorData$StonePredicate",
    "FallbackGeneratorData$NetherPredicate", "FallbackGeneratorData$EndPredicate"))


def check(baseline, candidate):
    errors = []
    with zipfile.ZipFile(baseline) as before, zipfile.ZipFile(candidate) as after:
        old_files = {name for name in before.namelist() if not name.endswith("/")}
        new_files = {name for name in after.namelist() if not name.endswith("/")}
        old_classes = {name for name in old_files if name.endswith(".class")}
        new_classes = {name for name in new_files if name.endswith(".class")}
        expected = {CURRENT + name[len(OLD):] if name.startswith(OLD) else name for name in old_classes}
        if new_classes != (expected - REMOVALS) | ADDITIONS:
            errors.append("Unexpected production class additions or removals")
        if any(name.startswith(OLD) for name in new_classes):
            errors.append("Old Base Metals implementation classes are still packaged")

        exports = {name[len(OLD):-6] for name in old_classes if name.startswith(OLD) and "$" not in name}
        declared = set(after.read("META-INF/basemetals-legacy-api.list").decode("utf-8").splitlines())
        if exports != declared:
            errors.append("The legacy API list does not cover exactly the former classes")

        metadata = {"META-INF/MANIFEST.MF", "mcmod.info", "META-INF/basemetals-legacy-api.list"}
        old_resources = old_files - old_classes - metadata
        new_resources = new_files - new_classes - metadata
        if old_resources - {"assets/basemetals/orespawn/basemetals.json"} != new_resources:
            errors.append("Unexpected resource additions or removals")
        for name in old_resources & new_resources:
            old = before.read(name)
            new = after.read(name)
            if name.endswith((".json", ".lang", ".cfg", ".txt", ".mcmeta", ".info")):
                old = old.replace(b"\r\n", b"\n")
                new = new.replace(b"\r\n", b"\n")
                if old and not old.endswith(b"\n"):
                    old += b"\n"
                if new and not new.endswith(b"\n"):
                    new += b"\n"
            if old != new:
                errors.append("Changed saved resource: " + name)

        for name in new_files:
            data = after.read(name)
            lowered = name.lower()
            if any(part in lowered for part in ("agent-notes", "agents.md", "codex", "review/", "probe", "fixtures/", "test/")):
                errors.append("Non-production entry: " + name)
            if name.endswith(".class") and struct.unpack(">H", data[6:8])[0] != 52:
                errors.append("Not Java 8 bytecode: " + name)
            if b"D:\\BaseMetals" in data or b"C:\\Users\\johnb" in data:
                errors.append("Local path in " + name)

        mod = json.loads(after.read("mcmod.info"))[0]
        if mod["modid"] != "basemetals" or mod["version"] != "2.5.1.110021":
            errors.append("Wrong mod identity or version")
        manifest = after.read("META-INF/MANIFEST.MF").decode("utf-8").replace("\r\n ", "")
        if "zone.moddev.mc.basemetals.compat.BaseMetalsLoadingPlugin" not in manifest:
            errors.append("Missing early namespace adapter")

    return {"candidate": str(candidate), "sha256": hashlib.sha256(candidate.read_bytes()).hexdigest().upper(),
            "productionClasses": len(new_classes), "legacyApiClasses": len(exports),
            "errors": errors, "passed": not errors}


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("baseline", type=Path)
    parser.add_argument("candidate", type=Path)
    parser.add_argument("output", type=Path)
    args = parser.parse_args()
    report = check(args.baseline, args.candidate)
    args.output.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps(report, indent=2))
    if not report["passed"]:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
