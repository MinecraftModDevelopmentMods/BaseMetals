"""Check saved golem identities and the chunk deliberately opened on a later boot."""

import argparse
import hashlib
import io
import json
from pathlib import Path

import nbtlib

from audit_world import plain, raw_chunk


GROUPS = {"near": (14, 13), "later": (154, -504)}
MATERIALS = "adamantine aquarium brass bronze coldiron copper cupronickel electrum invar lead mithril nickel platinum silver starsteel steel tin zinc".split()
# These fields change during ordinary entity ticks, even with the AI disabled.
TRANSIENT = {"Motion", "Rotation", "FallDistance", "OnGround", "Air", "Fire",
             "HurtTime", "HurtByTimestamp", "PortalCooldown"}


def capture(world):
    result = {}

    for group, (x, z) in GROUPS.items():
        path = world / "region" / f"r.{x // 32}.{z // 32}.mca"
        data = raw_chunk(path, (x & 31) + (z & 31) * 32)
        chunk = nbtlib.File.parse(io.BytesIO(data))["Level"]
        entities = {}

        for entity in chunk.get("Entities", []):
            name = str(entity.get("CustomName", ""))
            if not name.startswith(f"BM compatibility {group} "):
                continue
            if name in entities:
                raise ValueError(f"Duplicate saved golem: {name}")
            entities[name] = {key: plain(value) for key, value in entity.items()
                              if key not in TRANSIENT}

        expected = {f"BM compatibility {group} {material}" for material in MATERIALS}
        if set(entities) != expected:
            raise ValueError(f"Missing or unexpected {group} golems: {set(entities) ^ expected}")

        result[group] = {"chunk_sha256": hashlib.sha256(data).hexdigest(), "entities": entities}

    return result


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("world", type=Path)
    parser.add_argument("output", type=Path)
    parser.add_argument("--source", type=Path)
    parser.add_argument("--unopened", action="store_true")
    args = parser.parse_args()
    groups = capture(args.world)
    errors = []

    if args.source:
        source = json.loads(args.source.read_text(encoding="utf-8"))["groups"]
        for group in GROUPS:
            if groups[group]["entities"] != source[group]["entities"]:
                errors.append(f"Saved {group} golem identity or NBT changed")
        if args.unopened and groups["later"]["chunk_sha256"] != source["later"]["chunk_sha256"]:
            errors.append("The later-opened chunk changed before its scheduled visit")

    report = {"groups": groups, "errors": errors, "passed": not errors}
    args.output.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"entities": {g: len(v["entities"]) for g, v in groups.items()},
                      "errors": errors, "passed": not errors}))
    if errors:
        raise SystemExit(1)


if __name__ == "__main__":
    main()
