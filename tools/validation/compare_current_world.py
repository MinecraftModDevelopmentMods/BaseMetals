"""Check Base Metals blocks and item data in an existing flattened save."""

import argparse
import copy
import gzip
import io
import json
import struct
import zlib
from collections import Counter
from pathlib import Path

import nbtlib

from compare_world_terrain import block_names, chunks


def saved_entities(world):
    # Newer Minecraft saves entities separately from the terrain chunks.
    for dimension in ('', 'DIM-1', 'DIM1'):
        for region in (world / dimension / 'entities').glob('*.mca'):
            data = region.read_bytes()
            for slot in range(1024):
                offset = int.from_bytes(data[slot * 4:slot * 4 + 3], 'big') * 4096
                if not offset:
                    continue
                length = struct.unpack_from('>I', data, offset)[0]
                payload = data[offset + 5:offset + 4 + length]
                payload = gzip.decompress(payload) if data[offset + 4] == 1 else zlib.decompress(payload)
                root = nbtlib.File.parse(io.BytesIO(payload))
                yield from root.get('Entities', [])


def ordered(tag):
    if isinstance(tag, nbtlib.Compound):
        return nbtlib.Compound({key: ordered(tag[key]) for key in sorted(tag)})
    if isinstance(tag, nbtlib.List):
        return type(tag)(ordered(value) for value in tag)
    return tag


def canonical_text(value):
    if isinstance(value, (nbtlib.Compound, nbtlib.List)):
        parsed = value.unpack(json=True)
    else:
        text = str(value)
        try:
            parsed = json.loads(text)
        except (ValueError, TypeError):
            parsed = text

    if isinstance(parsed, str):
        parsed = {"text": parsed}

    def styles(component):
        if isinstance(component, dict):
            result = {}
            boolean_styles = {"bold", "italic", "underlined", "strikethrough", "obfuscated"}
            for key, child in component.items():
                result[key] = bool(child) if key in boolean_styles and isinstance(child, int) else styles(child)
            return result
        if isinstance(component, list):
            return [styles(child) for child in component]
        return component

    return nbtlib.String(json.dumps(styles(parsed), sort_keys=True, separators=(",", ":")))


def canonical_stack(stack):
    """Compare saved item data across the 1.20.5 component conversion."""
    result = nbtlib.Compound(copy.deepcopy(stack))
    result["count"] = nbtlib.Int(int(result.pop("Count", result.get("count", 1))))
    components = result.pop("components", nbtlib.Compound())
    legacy = result.pop("tag", nbtlib.Compound())

    for old, new in (("Damage", "minecraft:damage"), ("RepairCost", "minecraft:repair_cost")):
        if old in legacy:
            components[new] = nbtlib.Int(int(legacy.pop(old)))
        if new in components and not int(components[new]):
            components.pop(new)

    display = legacy.pop("display", nbtlib.Compound())
    if "Name" in display:
        components["minecraft:custom_name"] = display.pop("Name")
    if "Lore" in display:
        components["minecraft:lore"] = display.pop("Lore")
    if display:
        legacy["display"] = display
    if "minecraft:custom_name" in components:
        components["minecraft:custom_name"] = canonical_text(components["minecraft:custom_name"])
    if "minecraft:lore" in components:
        components["minecraft:lore"] = nbtlib.List[nbtlib.String](
            canonical_text(line) for line in components["minecraft:lore"])

    for old, new in (("Enchantments", "minecraft:enchantments"),
                     ("StoredEnchantments", "minecraft:stored_enchantments")):
        if old in legacy:
            levels = nbtlib.Compound({str(entry["id"]): nbtlib.Int(int(entry["lvl"])) for entry in legacy.pop(old)})
            components[new] = nbtlib.Compound({"levels": levels})
        if new in components:
            enchantments = components[new]
            if "levels" not in enchantments:
                enchantments = nbtlib.Compound({"levels": enchantments})
                components[new] = enchantments
            if bool(enchantments.get("show_in_tooltip", False)):
                enchantments.pop("show_in_tooltip")
            if not enchantments.get("levels"):
                components.pop(new)

    if legacy:
        components["minecraft:custom_data"] = legacy
    if components:
        result["components"] = components
    return ordered(result).snbt()


def canonical_player(player):
    """Keep worn slots comparable after Minecraft moves them out of Inventory."""
    result = copy.deepcopy(player)
    slots = {"feet": 100, "legs": 101, "chest": 102, "head": 103, "offhand": -106}
    for name, stack in result.get("equipment", {}).items():
        if name in slots and isinstance(stack, nbtlib.Compound):
            stack["Slot"] = nbtlib.Byte(slots[name])
    return result


def snapshot(world):
    blocks = {}
    stacks = Counter()

    def items(value):
        if isinstance(value, (dict, nbtlib.Compound)):
            identifier = str(value.get('id', ''))
            # Flying shots can hit a target during the runtime test. Their NBT
            # round-trip is covered separately; saved inventories must stay intact.
            if identifier in ('basemetals:custom_arrow', 'basemetals:custom_bolt') and not value.get('inGround', False):
                return
            if str(value.get("id", "")).startswith("basemetals:") and ("Count" in value or "count" in value):
                stacks[canonical_stack(value)] += 1
                return
            for child in value.values():
                items(child)
        elif isinstance(value, (list, nbtlib.List)):
            for child in value:
                items(child)

    for key, chunk in chunks(world).items():
        for section in chunk.get("sections", chunk.get("Sections", [])):
            original_palette = section.get('block_states', {}).get('palette', section.get('Palette', []))
            if not any(str(entry.get('Name', '')).startswith('basemetals:') for entry in original_palette):
                continue
            source = copy.deepcopy(section)
            palette = source.get("block_states", {}).get("palette", source.get("Palette", []))
            for entry in palette:
                # Older wall connections used booleans; Minecraft now stores none, low or tall.
                if str(entry.get("Name", "")).startswith("basemetals:") and str(entry["Name"]).endswith("_wall"):
                    properties = entry.get("Properties", {})
                    for direction in ("north", "south", "east", "west"):
                        if str(properties.get(direction, "")) in ("false", "true"):
                            properties[direction] = nbtlib.String("low" if str(properties[direction]) == "true" else "none")
                state = ordered(entry).snbt()
                entry["Name"] = nbtlib.String(state)
            for index, state in enumerate(block_names(source)):
                if state and "basemetals:" in state:
                    blocks[(*key, int(section["Y"]), index)] = state
        items(chunk.get("block_entities", chunk.get("TileEntities", [])))
        items(chunk.get("entities", chunk.get("Entities", [])))

    for entity in saved_entities(world):
        items(entity)

    items(canonical_player(nbtlib.load(world / "level.dat")["Data"].get("Player", {})))
    for player in (world / "playerdata").glob("*.dat"):
        items(canonical_player(nbtlib.load(player)))

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
