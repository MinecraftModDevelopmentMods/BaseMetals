"""Exercise OreSpawn upgrades on disposable packaged Forge servers."""

import argparse
import copy
import hashlib
import json
import shutil
import subprocess
import time
from pathlib import Path


DIMENSIONS = {-1: "minecraft:the_nether", 0: "minecraft:overworld", 1: "minecraft:the_end"}
JAR_NAME = "forge-1.10.2-12.18.3.2511-universal.jar"


def write_json(path, value):
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, indent=2) + "\n", encoding="utf-8")


def digest(path):
    return hashlib.sha256(path.read_bytes()).hexdigest()


def prepare(directory, template, mods, world=None, flat=True):
    directory.mkdir(parents=True, exist_ok=False)
    for name in (JAR_NAME, "minecraft_server.1.10.2.jar"):
        shutil.copy2(template / name, directory / name)
    shutil.copytree(template / "libraries", directory / "libraries")
    (directory / "mods").mkdir()
    for mod in mods:
        shutil.copy2(mod, directory / "mods" / mod.name)
    (directory / "eula.txt").write_text("eula=true\n", encoding="utf-8")
    (directory / "server.properties").write_text(
        "level-name=world\nserver-port=0\nonline-mode=false\nlevel-seed=8675309\n"
        "view-distance=2\nmax-tick-time=-1\nspawn-protection=0\n"
        + ("level-type=FLAT\n" if flat else "level-type=DEFAULT\n"), encoding="utf-8")
    if world:
        if directory.resolve().is_relative_to(world.resolve()):
            raise ValueError("The disposable directory must not be inside its source world")
        shutil.copytree(world, directory / "world")


def run(java, directory, phase, properties, report_kind="worldgen", expected_failure=False):
    arguments = [str(java), "-Xms256M", "-Xmx2G"]
    arguments += [f"-D{key}={value}" for key, value in properties.items()]
    arguments += ["-jar", JAR_NAME, "nogui"]
    started = time.time_ns()
    log = directory / f"{phase}-console.txt"
    with log.open("wb") as output:
        result = subprocess.run(arguments, cwd=directory, stdout=output,
                                stderr=subprocess.STDOUT, timeout=240)
    report_path = directory / f"{phase}-{report_kind}.json"
    if not report_path.is_file() or report_path.stat().st_mtime_ns < started:
        raise AssertionError(f"No fresh runtime report; read {log}")
    report = json.loads(report_path.read_text(encoding="utf-8"))
    if expected_failure:
        if report.get("passed") or "Retired generation code" not in report.get("failure", ""):
            raise AssertionError(f"Did not reproduce the old generator: {report_path}")
    elif result.returncode or not report.get("passed"):
        raise AssertionError(f"Runtime check failed: {report_path}; read {log}")
    print(directory.name, phase, "expected failure" if expected_failure else "passed", flush=True)
    return report


def reject_dependency(java, directory, template, mods):
    prepare(directory, template, mods)
    log = directory / "rejection-console.txt"
    with log.open("wb") as output:
        subprocess.run([str(java), "-Xms256M", "-Xmx2G", "-jar", JAR_NAME, "nogui"],
                       cwd=directory, stdout=output, stderr=subprocess.STDOUT, timeout=120)

    text = log.read_text(encoding="utf-8", errors="replace")
    if ("MissingModsException" not in text or "orespawn" not in text
            or "4.1.2.110021" not in text or (directory / "world/level.dat").exists()):
        raise AssertionError(f"The unsupported dependency was not clearly rejected; read {log}")
    print(directory.name, "rejected before world load", flush=True)


def expected_os3(source):
    ores = {}
    for name, spawn in source["spawns"].items():
        parameters = spawn["parameters"]
        size = parameters["size"]
        variation = parameters.get("variation", 0)
        placement = {"enabled": spawn.get("enabled", True), "min_y": parameters["minHeight"],
                     "max_y": parameters["maxHeight"] - 1, "frequency": parameters["frequency"]}
        if variation:
            placement.update(min_quantity=max(1, size - variation), max_quantity=min(64, size + variation - 1))
        else:
            placement["quantity"] = size
        dimensions = spawn.get("dimensions", [])
        if dimensions:
            domain = {"dimensions": {DIMENSIONS[value]: placement for value in dimensions}}
        else:
            domain = {"dimension_selectors": {"orespawn:all_except_nether_end": placement}}
        ores[f"basemetals:legacy/{name}"] = {
            "block": spawn["blocks"][0]["name"], "enabled": spawn.get("enabled", True),
            "retrogen": spawn.get("retrogen", False), **domain}
    return {"ores": ores}


def expected_os1(source):
    ores = {}
    explicit = {int(value["dimension"]) for value in source["dimensions"] if value["dimension"] != "+"}
    ordinal = 0
    for dimension in source["dimensions"]:
        domains = sorted(set(DIMENSIONS) - explicit) if dimension["dimension"] == "+" else [int(dimension["dimension"])]
        for ore in dimension["ores"]:
            name = ore["blockID"].replace(":", "_") + "_" + str(ordinal)
            ordinal += 1
            placement = {"enabled": True, "min_y": ore.get("minHeight", 0),
                         "max_y": ore.get("maxHeight", 256) - 1, "frequency": ore.get("frequency", 0.5)}
            size, variation = ore.get("size", 8), ore.get("variation", 0)
            if variation:
                placement.update(min_quantity=max(1, size - variation), max_quantity=min(64, size + variation - 1))
            else:
                placement["quantity"] = size
            ores[f"basemetals:legacy/{name}"] = {"block": ore["blockID"], "enabled": True,
                    "dimensions": {DIMENSIONS[value]: placement for value in domains}}
    return {"ores": ores}


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    for name in ("java", "template", "mod", "orespawn", "probe", "output", "os3"):
        parser.add_argument("--" + name, type=Path, required=True)
    parser.add_argument("--os1", type=Path)
    parser.add_argument("--old-mod", type=Path)
    parser.add_argument("--old-orespawn", type=Path)
    parser.add_argument("--old-support-mod", type=Path, action="append", default=[])
    parser.add_argument("--old-version", default="2.5.1.110021")
    parser.add_argument("--launch-probe", type=Path)
    parser.add_argument("--red", action="store_true")
    args = parser.parse_args()
    args.output.mkdir(parents=True, exist_ok=True)
    mods = [args.mod, args.orespawn, args.probe]
    stock = json.loads(args.os3.read_text(encoding="utf-8"))

    if args.red:
        directory = args.output / "red"
        prepare(directory, args.template, mods)
        write_json(directory / "expected.json", expected_os3(stock))
        run(args.java, directory, "first", {"worldgen.expected": directory / "expected.json"}, expected_failure=True)
        return

    if not all((args.old_mod, args.old_orespawn, args.launch_probe)):
        parser.error("Full upgrade checks need --old-mod, --old-orespawn and --launch-probe")

    reject_dependency(args.java, args.output / "missing-orespawn", args.template, [args.mod])
    reject_dependency(args.java, args.output / "old-orespawn-rejected", args.template,
                      [args.mod, args.old_orespawn])

    mods.append(args.launch_probe)
    control = args.output / "old-os3-control"
    prepare(control, args.template, [args.old_mod, args.old_orespawn, args.launch_probe, *args.old_support_mod])
    write_json(control / "config/orespawn3/basemetals.json", stock)
    run(args.java, control, "fresh", {"launch.phase": "fresh", "launch.remoteMarker": "true",
                                    "launch.expectedVersion": args.old_version}, report_kind="launch")

    custom = copy.deepcopy(stock)
    custom["spawns"]["tin_ore"]["parameters"].update(size=5, variation=0, frequency=2.75, minHeight=10, maxHeight=50)
    custom["spawns"]["nickel_ore"]["dimensions"] = [0]
    custom["spawns"]["silver_ore"]["enabled"] = False
    custom["spawns"]["lead_ore"]["retrogen"] = True
    disabled = copy.deepcopy(stock)
    for spawn in disabled["spawns"].values():
        spawn["enabled"] = False

    scenarios = [("os3-stock", stock, "orespawn3", expected_os3(stock)),
                 ("os3-custom", custom, "orespawn3", expected_os3(custom)),
                 ("os3-disabled", disabled, "orespawn3", expected_os3(disabled))]
    if args.os1:
        source = json.loads(args.os1.read_text(encoding="utf-8"))
        scenarios.append(("os1", source, "orespawn", expected_os1(source)))

    for name, source, legacy_directory, expected in scenarios:
        directory = args.output / name
        prepare(directory, args.template, mods, control / "world")
        legacy = directory / "config" / legacy_directory / "basemetals.json"
        legacy.parent.mkdir(parents=True, exist_ok=True)
        if name == "os1":
            shutil.copy2(args.os1, legacy)
        elif name == "os3-stock":
            shutil.copy2(control / "config/orespawn3/basemetals.json", legacy)
        else:
            write_json(legacy, source)
        source_hash = digest(legacy)
        write_json(directory / "expected.json", expected)
        properties = {"worldgen.expected": directory / "expected.json", "worldgen.savedMarker": "true"}
        remote = directory / "world/region/r.2.-3.mca"
        remote_hash = digest(remote)
        run(args.java, directory, "first", properties)
        stable = [directory / "config/basemetals-orespawn.json",
                  directory / "config/orespawn-upgrade-report.txt",
                  directory / "world/serverconfig/orespawn-worldgen.json"]
        hashes = {path: digest(path) for path in stable}
        properties["worldgen.phase"] = "reload"
        run(args.java, directory, "reload", properties)
        if digest(remote) != remote_hash:
            raise AssertionError(f"An unopened region changed during upgrade: {directory}")
        if digest(legacy) != source_hash or any(digest(path) != value for path, value in hashes.items()):
            raise AssertionError(f"Migration rewrote a source or was not stable on reload: {directory}")
        properties["worldgen.openRemote"] = "true"
        for phase in ("later", "stable"):
            properties["worldgen.phase"] = phase
            run(args.java, directory, phase, properties)
        if digest(legacy) != source_hash or any(digest(path) != value for path, value in hashes.items()):
            raise AssertionError(f"Migration changed after opening later chunks: {directory}")
        write_json(directory / "migration-hashes.json", {str(path.relative_to(directory)): value for path, value in hashes.items()})

    fresh = args.output / "native-fresh"
    prepare(fresh, args.template, mods, flat=False)
    write_json(fresh / "expected.json", expected_os3(stock))
    run(args.java, fresh, "first", {"worldgen.expected": fresh / "expected.json", "worldgen.sample": "true"})
    run(args.java, fresh, "reload", {"worldgen.phase": "reload", "worldgen.expected": fresh / "expected.json"})

    existing = args.output / "existing-os4"
    prepare(existing, args.template, mods, fresh / "world")
    shutil.copytree(fresh / "config", existing / "config", dirs_exist_ok=True)
    profile_path = existing / "world/serverconfig/orespawn-worldgen.json"
    profile = json.loads(profile_path.read_text(encoding="utf-8"))
    tin = "basemetals:legacy/tin_ore"
    selector = "orespawn:all_except_nether_end"
    profile["ores"][tin]["dimension_selectors"][selector]["frequency"] = 6.25
    write_json(profile_path, profile)
    override = {"schema_version": 5, "provider_modid": "basemetals", "provider_revision": 1,
                "ores": {key: copy.deepcopy(value) for key, value in profile["ores"].items()
                         if key.startswith("basemetals:")}}
    override["ores"][tin]["dimension_selectors"][selector]["frequency"] = 9.0
    override_path = existing / "config/basemetals-orespawn.json"
    write_json(override_path, override)
    write_json(existing / "config/orespawn3/basemetals.json", custom)
    expected = expected_os3(stock)
    expected["ores"][tin]["dimension_selectors"][selector]["frequency"] = 6.25
    write_json(existing / "expected.json", expected)
    hashes = {path: digest(path) for path in (profile_path, override_path)}
    for phase in ("first", "reload"):
        run(args.java, existing, phase, {"worldgen.phase": phase, "worldgen.expected": existing / "expected.json"})
    if any(digest(path) != value for path, value in hashes.items()):
        raise AssertionError("An existing OS4 override or world profile was overwritten")

    future = args.output / "existing-os4-new-world"
    prepare(future, args.template, mods)
    write_json(future / "config/basemetals-orespawn.json", override)
    write_json(future / "config/orespawn3/basemetals.json", custom)
    write_json(future / "expected.json", {"ores": override["ores"]})
    override_hash = digest(future / "config/basemetals-orespawn.json")
    for phase in ("first", "reload"):
        run(args.java, future, phase, {"worldgen.phase": phase, "worldgen.expected": future / "expected.json"})
    if digest(future / "config/basemetals-orespawn.json") != override_hash:
        raise AssertionError("The existing override changed when creating a new world")
    print("BASE_METALS_OS41_MIGRATION_SUITE_PASS", flush=True)


if __name__ == "__main__":
    main()
