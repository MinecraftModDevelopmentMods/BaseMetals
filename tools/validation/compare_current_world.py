"""Check Base Metals blocks and item data in an existing flattened save."""

import argparse
import copy
import json
from collections import Counter
from pathlib import Path

import nbtlib

from compare_world_terrain import block_names, chunks


def ordered(tag):
    if isinstance(tag, nbtlib.Compound):
        return nbtlib.Compound({key: ordered(tag[key]) for key in sorted(tag)})
    if isinstance(tag, nbtlib.List):
        return type(tag)(ordered(value) for value in tag)
    return tag


def snapshot(world):
    blocks = {}
    stacks = Counter()

    def items(value):
        if isinstance(value, (dict, nbtlib.Compound)):
            if str(value.get("id", "")).startswith("basemetals:") and "Count" in value:
                stacks[ordered(value).snbt()] += 1
                return
            for child in value.values():
                items(child)
        elif isinstance(value, (list, nbtlib.List)):
            for child in value:
                items(child)

    for key, chunk in chunks(world).items():
        for section in chunk.get("sections", chunk.get("Sections", [])):
            source = copy.deepcopy(section)
            palette = source.get("block_states", {}).get("palette", source.get("Palette", []))
            for entry in palette:
                state = ordered(entry).snbt()
                entry["Name"] = nbtlib.String(state)
            for index, state in enumerate(block_names(source)):
                if state and "basemetals:" in state:
                    blocks[(*key, int(section["Y"]), index)] = state
        items(chunk.get("block_entities", chunk.get("TileEntities", [])))
        items(chunk.get("Entities", []))

    items(nbtlib.load(world / "level.dat")["Data"].get("Player", {}))
    for player in (world / "playerdata").glob("*.dat"):
        items(nbtlib.load(player))

    advancements = {}
    for file in (world / "advancements").glob("*.json"):
        progress = json.loads(file.read_text(encoding="utf8"))
        advancements[file.name] = {key: value for key, value in progress.items() if key.startswith("basemetals:")}
    return blocks, stacks, advancements


def compare(original, upgraded):
    before_blocks, before_stacks, before_advancements = snapshot(original)
    after_blocks, after_stacks, after_advancements = snapshot(upgraded)
    for position, state in before_blocks.items():
        if after_blocks.get(position) != state:
            raise AssertionError(f"Changed Base Metals block at {position}: {state}")
    if before_stacks - after_stacks:
        raise AssertionError(f"Changed or missing item data: {before_stacks - after_stacks}")
    for player, progress in before_advancements.items():
        for identifier, value in progress.items():
            if after_advancements.get(player, {}).get(identifier) != value:
                raise AssertionError(f"Changed advancement progress: {player}/{identifier}")
    if not before_blocks and not before_stacks:
        raise AssertionError("This save has no Base Metals content to check")
    print(f"CURRENT_WORLD_UPGRADE PASS blocks={len(before_blocks)} stacks={sum(before_stacks.values())} "
          f"advancement_players={len(before_advancements)}")


if __name__ == "__main__":
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("original", type=Path)
    parser.add_argument("upgraded", type=Path)
    arguments = parser.parse_args()
    compare(arguments.original, arguments.upgraded)
