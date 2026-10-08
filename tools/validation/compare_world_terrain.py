"""Compare solid vanilla terrain in an original world and its disposable upgrade."""

import argparse
import gzip
import io
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


def compare(original, upgraded):
    before, after = chunks(original), chunks(upgraded)
    checked = 0
    for key, chunk in before.items():
        if key not in after:
            raise AssertionError(f"Original chunk is missing after upgrade: {key}")
        sections = {int(section["Y"]): section for section in after[key]["Sections"]}
        for section in chunk["Sections"]:
            height = int(section["Y"])
            actual = list(block_names(sections[height])) if height in sections else [None] * 4096
            for index, expected in enumerate(block_names(section)):
                if expected not in TERRAIN_NAMES:
                    continue
                checked += 1
                if actual[index] != expected:
                    raise AssertionError(f"Terrain changed at {key}, section {height}, index {index}: "
                                         f"{expected} became {actual[index]}")
    if not checked:
        raise AssertionError("The fixture contains no solid vanilla terrain to compare")

    print(f"TERRAIN_UPGRADE PASS chunks={len(before)} solid_blocks={checked}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("original", type=Path)
    parser.add_argument("upgraded", type=Path)
    arguments = parser.parse_args()
    compare(arguments.original, arguments.upgraded)
