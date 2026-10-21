"""Compare saved 1.10 blocks and inventories without opening Minecraft."""

import argparse
import gzip
import hashlib
import io
import json
import struct
import zlib
from collections import Counter
from pathlib import Path

import nbtlib
import numpy as np


MATERIALS = "adamantine aquarium brass bronze coldiron copper cupronickel electrum invar lead mithril nickel platinum silver starsteel steel tin".split()
ALIASES = {"basemetals:carbon_powder": "basemetals:coal_powder",
           "basemetals:liquid_mercury": "basemetals:mercury"}
ALIASES.update({f"basemetals:{name}_door_item": f"basemetals:{name}_door" for name in MATERIALS})
PREFIXES = ("basemetals:", "poweradvantage:", "steamadvantage:", "electricadvantage:")


def fingerprint(root):
    digest = hashlib.sha256()
    count = size = 0
    for path in sorted(root.rglob("*"), key=lambda p: p.relative_to(root).as_posix()):
        if not path.is_file():
            continue
        digest.update(path.relative_to(root).as_posix().encode("utf-8") + b"\n")
        with path.open("rb") as handle:
            while data := handle.read(1024 * 1024):
                digest.update(data)
                size += len(data)
        count += 1
    return {"files": count, "bytes": size, "sha256": digest.hexdigest().upper()}


def nbt(path):
    data = path.read_bytes()
    if data[:2] == b"\x1f\x8b":
        data = gzip.decompress(data)
    return nbtlib.File.parse(io.BytesIO(data))


def raw_chunk(path, index):
    with path.open("rb") as handle:
        handle.seek(index * 4)
        location = int.from_bytes(handle.read(4), "big")
        if not location:
            return None
        handle.seek((location >> 8) * 4096)
        length = struct.unpack(">I", handle.read(4))[0]
        compression = handle.read(1)[0]
        data = handle.read(length - 1)
    if compression == 1:
        return gzip.decompress(data)
    if compression == 2:
        return zlib.decompress(data)
    if compression == 3:
        return data
    raise ValueError(f"Unsupported chunk compression {compression}")


def plain(value):
    if isinstance(value, dict):
        return {str(k): plain(v) for k, v in value.items()}
    if isinstance(value, (list, nbtlib.List)):
        return [plain(v) for v in value]
    if hasattr(value, "tolist"):
        return value.tolist()
    if isinstance(value, str):
        return str(value)
    if isinstance(value, int):
        return int(value)
    if isinstance(value, float):
        return float(value)
    return value


def stacks(value, owner, result):
    if isinstance(value, dict):
        name = str(value.get("id", ""))
        if "Count" in value and name.startswith(PREFIXES):
            payload = plain(value)
            payload["id"] = ALIASES.get(name, name)
            result.append({"owner": owner, "original_id": name, "payload": payload})
        else:
            for key, child in value.items():
                if key != "Sections":
                    stacks(child, owner, result)
    elif isinstance(value, (list, nbtlib.List)):
        for child in value:
            stacks(child, owner, result)


def nibbles(values):
    packed = np.asarray(values, dtype=np.int16) & 255
    result = np.empty(4096, dtype=np.int16)
    result[0::2], result[1::2] = packed & 15, packed >> 4
    return result


def scan(world):
    level = nbt(world / "level.dat")
    registry = {int(v["V"]): str(v["K"]) for v in level["FML"]["Registries"]["minecraft:blocks"]["ids"]}
    wanted = {numeric: name for numeric, name in registry.items() if name.startswith(PREFIXES)}
    lookup = np.zeros(4096, dtype=np.bool_)
    for numeric in wanted:
        lookup[numeric] = True
    items, blocks, points = [], {}, set()
    block_totals = Counter()
    chunks = 0

    for path in sorted(world.rglob("r.*.*.mca")):
        if path.parent.name != "region":
            continue
        relative = path.relative_to(world).as_posix()
        dimension = -1 if relative.startswith("DIM-1/") else 1 if relative.startswith("DIM1/") else 0
        _, rx, rz, _ = path.name.split(".")
        with path.open("rb") as handle:
            locations = handle.read(4096)

        for index in range(1024):
            if not int.from_bytes(locations[index * 4:index * 4 + 4], "big"):
                continue
            data = raw_chunk(path, index)
            root = nbtlib.File.parse(io.BytesIO(data))
            chunk = root.get("Level", root)
            chunks += 1
            point = (dimension, int(rx) * 32 + (index & 31), int(rz) * 32 + (index >> 5))
            before = len(items)
            for tile in chunk.get("TileEntities", []):
                owner = [relative, index, "tile", *[int(tile.get(k, 0)) for k in ("x", "y", "z")]]
                stacks(tile, owner, items)
            for entity in chunk.get("Entities", []):
                owner = [relative, index, "entity", int(entity.get("UUIDMost", 0)), int(entity.get("UUIDLeast", 0))]
                stacks(entity, owner, items)
            if len(items) != before:
                points.add(point)

            states = []
            for section in chunk.get("Sections", []):
                ids = np.asarray(section["Blocks"], dtype=np.int16) & 255
                if "Add" in section:
                    ids |= nibbles(section["Add"]) << 8
                offsets = np.flatnonzero(lookup[ids])
                if not len(offsets):
                    continue
                metadata = nibbles(section["Data"])
                for offset in offsets:
                    name = ALIASES.get(wanted[int(ids[offset])], wanted[int(ids[offset])])
                    states.append([int(section["Y"]), int(offset), name, int(metadata[offset])])
                    block_totals[name] += 1
                    if name.endswith("_door") or name == "basemetals:mercury":
                        points.add(point)
            if states:
                key = f"{relative}:{index}"
                blocks[key] = hashlib.sha256(json.dumps(sorted(states)).encode()).hexdigest()

    for path in sorted((world / "playerdata").glob("*.dat")):
        stacks(nbt(path), ["player", path.name], items)
    stacks(level.get("Data", {}).get("Player", {}), ["level-player"], items)
    return {"chunks": chunks, "blocks": blocks, "block_totals": dict(block_totals),
            "items": items, "points": sorted(points)}


def item_key(record):
    return json.dumps([record["owner"], record["payload"]], sort_keys=True)


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("world", type=Path)
    parser.add_argument("output", type=Path)
    parser.add_argument("--source", type=Path, help="Previously captured source audit")
    parser.add_argument("--fingerprint-only", action="store_true")
    parser.add_argument("--delay", action="store_true", help="Only inspect Sylvester's unopened remote chest chunk")
    args = parser.parse_args()
    report = {"fingerprint": fingerprint(args.world)}

    if args.delay:
        data = raw_chunk(args.world / "region/r.4.-16.mca", 282)
        root = nbtlib.File.parse(io.BytesIO(data))
        chest = next(t for t in root["Level"]["TileEntities"] if tuple(int(t[k]) for k in ("x", "y", "z")) == (2470, 68, -8053))
        report["delayed_chunk"] = {"sha256": hashlib.sha256(data).hexdigest(),
            "legacy_units": sum(int(i["Count"]) for i in chest["Items"] if str(i["id"]) == "basemetals:carbon_powder"),
            "current_units": sum(int(i["Count"]) for i in chest["Items"] if str(i["id"]) == "basemetals:coal_powder")}
    elif not args.fingerprint_only:
        report["content"] = scan(args.world)

    if args.source and not args.delay:
        source = json.loads(args.source.read_text(encoding="utf-8"))["content"]
        target = report["content"]
        errors = []
        for chunk, digest in source["blocks"].items():
            if target["blocks"].get(chunk) != digest:
                errors.append({"type": "blocks", "chunk": chunk})
        available = Counter(item_key(v) for v in target["items"])
        for record in source["items"]:
            key = item_key(record)
            if available[key]:
                available[key] -= 1
            else:
                errors.append({"type": "item", "expected": record})
        report.update(errors=errors, passed=not errors)

    args.output.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({k: v for k, v in report.items() if k != "content"}, indent=2), flush=True)
    if "content" in report:
        content = report["content"]
        print("Chunks:", content["chunks"], "block chunks:", len(content["blocks"]),
              "item stacks:", len(content["items"]), "visit points:", len(content["points"]), flush=True)
    if report.get("passed") is False:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
