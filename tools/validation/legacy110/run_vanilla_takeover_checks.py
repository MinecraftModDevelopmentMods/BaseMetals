"""Check the vanilla takeover switch with Base Metals and an imported OS3 rule."""

import argparse
import copy
import io
import json
import shutil
from pathlib import Path

import nbtlib

from audit_world import fingerprint, plain, raw_chunk
from run_orespawn_checks import digest, expected_os3, prepare, run, write_json


def terrain(world):
    result = {}
    for x in range(64, 71):
        for z in range(64, 71):
            path = world / "region" / f"r.{x >> 5}.{z >> 5}.mca"
            raw = raw_chunk(path, (x & 31) + (z & 31) * 32)
            if raw is None:
                raise AssertionError(f"Missing saved chunk: {x},{z}")
            level = nbtlib.File.parse(io.BytesIO(raw))["Level"]
            if not level["TerrainPopulated"]:
                raise AssertionError(f"The terrain fixture is not fully generated: {x},{z}")
            result[f"{x},{z}"] = {
                "sections": [{key: plain(section[key]) for key in ("Y", "Blocks", "Data", "Add") if key in section}
                             for section in level["Sections"]],
                "biomes": plain(level["Biomes"])}
    return result


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("java", "template", "mod", "orespawn", "probe", "output"):
        parser.add_argument("--" + name, type=Path, required=True)
    args = parser.parse_args()
    stock = json.loads((Path(__file__).parent / "fixtures/orespawn3-basemetals.json").read_text(encoding="utf-8"))
    mods = [args.mod, args.orespawn, args.probe]
    source = {"version": "2.0", "spawns": {"diamond_ore": {
        "enabled": True, "retrogen": False, "feature": "default", "replaces": "default",
        "dimensions": [], "biomes": {"excludes": []},
        "parameters": {"frequency": 0.375, "size": 4, "variation": 0, "minHeight": 7, "maxHeight": 12},
        "blocks": [{"name": "minecraft:diamond_ore", "chance": 100}]}}}

    initial = args.output / "management-off"
    prepare(initial, args.template, mods, flat=False)
    write_json(initial / "config/orespawn3/orespawn.json", source)
    (initial / "config/orespawn.cfg").write_text(
        'general {\n B:"Replace Vanilla Oregen"=false\n B:Retrogen=false\n B:"Force Retrogen"=false\n}\n',
        encoding="utf-8")
    write_json(initial / "expected.json", expected_os3(stock))
    properties = {"worldgen.expected": initial / "expected.json", "worldgen.vanillaManaged": "false"}
    first = run(args.java, initial, "first", properties)
    original_rule = copy.deepcopy(first["profile"]["ores"]["orespawn:legacy/diamond_ore"])
    original_terrain = terrain(initial / "world")
    properties.update({"worldgen.phase": "reload", "worldgen.vanillaReload": "true"})
    run(args.java, initial, "reload", properties)
    if terrain(initial / "world") != original_terrain:
        raise AssertionError("Reloading changed the original terrain")

    source_fingerprint = fingerprint(initial / "world")
    switched = args.output / "management-toggled"
    prepare(switched, args.template, mods, initial / "world", flat=False)
    shutil.copytree(initial / "config", switched / "config", dirs_exist_ok=True)
    write_json(switched / "expected.json", expected_os3(stock))
    legacy = switched / "config/orespawn3/orespawn.json"
    legacy_hash = digest(legacy)
    profile_path = switched / "world/serverconfig/orespawn-worldgen.json"
    results = []

    for phase, managed, base, reload_only in (
            ("on", True, 80, False), ("on-reload", True, 80, True),
            ("on-later", True, 96, False), ("off-again", False, 128, False),
            ("off-reload", False, 128, True)):
        profile = json.loads(profile_path.read_text(encoding="utf-8"))
        profile["manage_vanilla_ores"] = managed
        write_json(profile_path, profile)
        report = run(args.java, switched, phase, {
            "worldgen.phase": phase, "worldgen.expected": switched / "expected.json",
            "worldgen.vanillaManaged": str(managed).lower(), "worldgen.vanillaSampleBase": base,
            "worldgen.vanillaReload": str(reload_only).lower(), "worldgen.openVanillaOld": "true"})
        if report["profile"]["ores"]["orespawn:legacy/diamond_ore"] != original_rule:
            raise AssertionError("Changing vanilla management rewrote the imported rule")
        if terrain(switched / "world") != original_terrain:
            raise AssertionError("Opening old chunks or switching management changed existing terrain")
        if digest(legacy) != legacy_hash:
            raise AssertionError("The original OS3 configuration changed")
        results.append({key: report[key] for key in ("phase", "vanillaManaged", "diamonds",
                        "diamondsOutsideCustomHeight", "diamondEvents", "deniedDiamondEvents", "baseMetalsOres")})

    if fingerprint(initial / "world") != source_fingerprint:
        raise AssertionError("The source fixture changed")
    write_json(args.output / "takeover-results.json", {
        "passed": True, "protectedChunks": 49, "sourceUnchanged": True, "phases": results})
    print("BASE_METALS_VANILLA_TAKEOVER_PASS", flush=True)


if __name__ == "__main__":
    main()
