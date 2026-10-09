"""Compare solid vanilla terrain in an original world and its disposable upgrade."""

import argparse
import gzip
import io
import math
import struct
import zlib
from pathlib import Path

import nbtlib


LEGACY_TERRAIN = {
    1: ["stone", "granite", "polished_granite", "diorite", "polished_diorite",
        "andesite", "polished_andesite"],
    3: ["dirt", "coarse_dirt", "podzol"],
    7: ["bedrock"],
    12: ["sand", "red_sand"],
    13: ["gravel"],
    24: ["sandstone", "chiseled_sandstone", "cut_sandstone"],
    49: ["obsidian"],
    87: ["netherrack"],
    88: ["soul_sand"],
    89: ["glowstone"],
    121: ["end_stone"],
}
TERRAIN_NAMES = {"minecraft:" + name for variants in LEGACY_TERRAIN.values() for name in variants}
FALLING_BLOCKS = {"minecraft:gravel", "minecraft:sand", "minecraft:red_sand"}
AIR = {None, "minecraft:air", "minecraft:cave_air", "minecraft:void_air"}
FALL_THROUGH = AIR | {"minecraft:water", "minecraft:lava", "minecraft:grass", "minecraft:tall_grass",
                      "minecraft:fern", "minecraft:large_fern", "minecraft:dead_bush",
                      "minecraft:seagrass", "minecraft:tall_seagrass"}


def chunks(world):
    result = {}
    for dimension in ["", "DIM-1", "DIM1"]:
        for region in (world / dimension / "region").glob("*.mca"):
            data = region.read_bytes()
            for slot in range(1024):
                offset = int.from_bytes(data[slot * 4:slot * 4 + 3], "big") * 4096
                if not offset:
                    continue
                length = struct.unpack_from(">I", data, offset)[0]
                compression = data[offset + 4]
                payload = data[offset + 5:offset + 4 + length]
                payload = gzip.decompress(payload) if compression == 1 else zlib.decompress(payload)
                level = nbtlib.File.parse(io.BytesIO(payload))["Level"]
                result[(dimension, int(level["xPos"]), int(level["zPos"]))] = level
    return result


def block_names(section):
    if "Blocks" in section:
        blocks = section["Blocks"]
        add = section.get("Add")
        metadata = section["Data"]
        for index in range(4096):
            block = int(blocks[index]) & 255
            if add is not None:
                block |= ((int(add[index // 2]) >> (4 * (index % 2))) & 15) << 8
            variants = LEGACY_TERRAIN.get(block)
            meta = (int(metadata[index // 2]) >> (4 * (index % 2))) & 15
            yield "minecraft:" + variants[min(meta, len(variants) - 1)] if variants else None
        return

    palette = [str(state["Name"]) for state in section.get("Palette", [])]
    if not palette:
        yield from [None] * 4096
        return
    states = [int(value) & ((1 << 64) - 1) for value in section.get("BlockStates", [])]
    if not states:
        yield from [palette[0]] * 4096
        return
    bits = max(4, (len(palette) - 1).bit_length())
    mask = (1 << bits) - 1
    entries_per_word = 64 // bits
    padded = len(states) == (4096 + entries_per_word - 1) // entries_per_word

    for index in range(4096):
        if padded:
            word, entry = divmod(index, entries_per_word)
            shift = entry * bits
        else:
            word, shift = divmod(index * bits, 64)

        value = states[word] >> shift
        if not padded and shift + bits > 64:
            value |= states[word + 1] << (64 - shift)
        yield palette[value & mask]


def falling_blocks(world, loaded_chunks, dropped_items=None):
    result = {}
    seen = set()

    def collect(dimension, entities):
        for entity in entities:
            identity = tuple(entity.get("UUID", []))
            if identity and identity in seen:
                continue
            if identity:
                seen.add(identity)
            if str(entity.get("id", "")) == "minecraft:item" and dropped_items is not None:
                item = entity.get("Item", {})
                name = str(item.get("id", ""))
                if name in FALLING_BLOCKS:
                    dropped_items.append({"dimension": dimension, "name": name,
                                          "count": int(item.get("Count", 0)),
                                          "position": tuple(float(value) for value in entity["Pos"]),
                                          "uuid": tuple(entity.get("UUID", []))})
                continue
            if str(entity.get("id", "")) != "minecraft:falling_block":
                continue
            name = str(entity.get("BlockState", {}).get("Name", ""))
            if name not in FALLING_BLOCKS:
                continue
            x, y, z = [math.floor(float(value)) for value in entity["Pos"]]
            key = (dimension, x >> 4, z >> 4, ((z & 15) << 4) | (x & 15))
            result.setdefault(key, []).append((y, name))

    for (dimension, _, _), chunk in loaded_chunks.items():
        collect(dimension, chunk.get("Entities", []))
    for dimension in ["", "DIM-1", "DIM1"]:
        for region in (world / dimension / "entities").glob("*.mca"):
            data = region.read_bytes()
            for slot in range(1024):
                offset = int.from_bytes(data[slot * 4:slot * 4 + 3], "big") * 4096
                if not offset:
                    continue
                length = struct.unpack_from(">I", data, offset)[0]
                payload = data[offset + 5:offset + 4 + length]
                payload = gzip.decompress(payload) if data[offset + 4] == 1 else zlib.decompress(payload)
                root = nbtlib.File.parse(io.BytesIO(payload))
                collect(dimension, root.get("Entities", []))
    return result


def match_fallen_blocks(missing, recovered):
    # A falling block may land, remain in flight, or become a dropped item.
    # Match each original grain once; nearby stacks cannot cover several losses.
    # Falling entities and merged item stacks can drift into neighbouring columns.
    neighbours = []
    for dimension, name, x, y, z in missing:
        candidates = []
        for index, (other_dimension, other_name, px, py, pz) in enumerate(recovered):
            if other_dimension != dimension or other_name != name:
                continue
            distance = math.hypot(px - x, pz - z)
            if distance <= 4 and 0 <= py <= y + 1:
                candidates.append((distance, -py, index))
        neighbours.append([index for _, _, index in sorted(candidates)])

    owners = {}

    def assign(source, visited):
        for target in neighbours[source]:
            if target in visited:
                continue
            visited.add(target)
            if target not in owners or assign(owners[target], visited):
                owners[target] = source
                return True
        return False

    for source in sorted(range(len(missing)), key=lambda index: len(neighbours[index])):
        if not assign(source, set()):
            raise AssertionError(f"Fallen terrain block was not recovered: {missing[source]}")


def compare(original, upgraded):
    before, after = chunks(original), chunks(upgraded)
    original_drops, current_drops = [], []
    falling_blocks(original, before, original_drops)
    current_falling = falling_blocks(upgraded, after, current_drops)
    old_drop_ids = {drop["uuid"] for drop in original_drops}
    current_drops = [drop for drop in current_drops if drop["uuid"] not in old_drop_ids]
    checked = 0
    missing, recovered = [], []

    for (dimension, chunk_x, chunk_z, column), entries in current_falling.items():
        for y, name in entries:
            recovered.append((dimension, name, chunk_x * 16 + (column & 15) + 0.5,
                              y, chunk_z * 16 + (column >> 4) + 0.5))
    for drop in current_drops:
        x, y, z = drop["position"]
        recovered.extend([(drop["dimension"], drop["name"], x, y, z)] * drop["count"])

    for key, chunk in before.items():
        if key not in after:
            raise AssertionError(f"Original chunk is missing after upgrade: {key}")
        sections = {int(section["Y"]): list(block_names(section)) for section in after[key]["Sections"]}
        original_sections = {int(section["Y"]): list(block_names(section)) for section in chunk["Sections"]}
        for height in original_sections.keys() | sections.keys():
            actual = sections.get(height, [None] * 4096)
            for index, expected in enumerate(original_sections.get(height, [None] * 4096)):
                x = key[1] * 16 + (index & 15) + 0.5
                y = height * 16 + (index >> 8)
                z = key[2] * 16 + ((index >> 4) & 15) + 0.5
                if actual[index] in FALLING_BLOCKS and actual[index] != expected:
                    recovered.append((key[0], actual[index], x, y, z))
                if expected not in TERRAIN_NAMES:
                    continue
                checked += 1
                if actual[index] != expected:
                    if expected in FALLING_BLOCKS and actual[index] in FALL_THROUGH | FALLING_BLOCKS:
                        missing.append((key[0], expected, x, y, z))
                        continue
                    raise AssertionError(f"Terrain changed at {key}, section {height}, index {index}: "
                                         f"{expected} became {actual[index]}")
    if not checked:
        raise AssertionError("The fixture contains no solid vanilla terrain to compare")

    match_fallen_blocks(missing, recovered)
    print(f"TERRAIN_UPGRADE PASS chunks={len(before)} solid_blocks={checked} "
          f"recovered_falling_blocks={len(missing)}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("original", type=Path)
    parser.add_argument("upgraded", type=Path)
    arguments = parser.parse_args()
    compare(arguments.original, arguments.upgraded)
