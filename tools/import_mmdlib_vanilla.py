#!/usr/bin/env python3
"""Import MMDLib's vanilla material extensions into Base Metals.

MMDLib carried the vanilla extensions (iron crackhammers, diamond doors, emerald
armour and so on) while its 2.0 API was being dogfooded. They are Base Metals
content, so this copies them back:

* material definitions from ``src/main/resources/mmdlib/material`` (copper is
  skipped: Base Metals defines its own full copper material);
* the static blockstates, models and textures they use, from
  ``src/main/resources/assets/mmdlib``.

While copying, ``mmdlib:`` resource locations become ``basemetals:``, MMDLib's
``dust`` form becomes Base Metals' ``powder`` (the 1.12 IDs), and molten metal
moves to Base Metals' own fluids (``has_fluid`` false plus the
``basemetals:bucket`` item form) for the 11 materials 1.12 melted; lapis, stone
and wood have no molten form. Recipes, tags, loot and names are produced by
Base Metals' datagen, not copied.

Usage: python3 tools/import_mmdlib_vanilla.py [--mmdlib ../MMDLib] [--check]
"""
import argparse
import json
import re
import shutil
import sys
from pathlib import Path

SKIPPED_MATERIALS = {"copper"}
# 1.12 Base Metals never had these molten; MMDLib's example gave every material a fluid.
NOT_MOLTEN = {"lapis", "stone", "wood"}
BLOCK_STATE_KEYS = ("model",)


def main():
    repo = Path(__file__).resolve().parents[1]
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--mmdlib", type=Path, default=repo.parent / "MMDLib", help="MMDLib checkout")
    parser.add_argument("--check", action="store_true", help="report differences without writing")
    args = parser.parse_args()

    source = args.mmdlib / "src/main/resources"
    target = repo / "src/main/resources"
    materials = sorted(p.stem for p in (source / "mmdlib/material").glob("*.json")
                       if p.stem not in SKIPPED_MATERIALS)
    dust = re.compile(r"\b(" + "|".join(materials) + r")_dust\b")

    def rename_path(path):
        return dust.sub(r"\1_powder", path)

    def rename_location(value):
        if not isinstance(value, str) or not value.startswith("mmdlib:"):
            return value
        return "basemetals:" + rename_path(value[len("mmdlib:"):])

    def rewrite(value):
        if isinstance(value, dict):
            return {key: rewrite(item) for key, item in value.items()}
        if isinstance(value, list):
            return [rewrite(item) for item in value]
        return rename_location(value)

    writes = {}  # target path -> bytes
    problems = []

    def emit(relative, data):
        writes[target / relative] = data

    # Material definitions.
    for name in materials:
        definition = json.loads((source / f"mmdlib/material/{name}.json").read_text())
        items = definition.get("items", "").split()
        items = ["basemetals:powder" if item in ("dust", "powder") else item for item in items]
        if definition.get("has_fluid", True) and name not in NOT_MOLTEN:
            items.append("basemetals:bucket")
        definition["items"] = " ".join(items)
        definition["has_fluid"] = False
        special = definition.get("special", {})
        if name in NOT_MOLTEN:
            special = {key: value for key, value in special.items()
                       if key not in ("viscosity", "temperature", "density")}
            definition["special"] = special
        if "fuel_dust" in special:
            special = {("fuel_powder" if key == "fuel_dust" else key): value for key, value in special.items()}
            definition["special"] = special
        definition = rewrite(definition)
        emit(f"mmdlib/material/{name}.json", (json.dumps(definition, indent=2) + "\n").encode())

    # Client assets: walk blockstates and item models of the materials' content.
    assets = source / "assets/mmdlib"
    pending = []
    for state in sorted((assets / "blockstates").glob("*.json")):
        if owner(state.stem, materials) and not state.stem.endswith("_molten"):
            pending.append(("blockstates", state.stem))
    for model in sorted((assets / "models/item").glob("*.json")):
        stem = model.stem
        if owner(stem, materials) and "_molten" not in stem:
            pending.append(("models", "item/" + stem))
    for name in materials:
        for layer in (1, 2):
            if (assets / f"textures/models/armor/{name}_layer_{layer}.png").exists():
                pending.append(("textures", f"models/armor/{name}_layer_{layer}"))
        if (assets / f"textures/entity/horse/armor/horse_armor_{name}.png").exists():
            pending.append(("textures", f"entity/horse/armor/horse_armor_{name}"))

    seen = set()
    while pending:
        kind, path = pending.pop()
        if (kind, path) in seen:
            continue
        seen.add((kind, path))
        if kind == "textures":
            png = assets / f"textures/{path}.png"
            if not png.exists():
                problems.append(f"missing texture mmdlib:{path}")
                continue
            emit(f"assets/basemetals/textures/{rename_path(path)}.png", png.read_bytes())
            meta = png.with_name(png.name + ".mcmeta")
            if meta.exists():
                emit(f"assets/basemetals/textures/{rename_path(path)}.png.mcmeta", meta.read_bytes())
            continue
        file = assets / f"{kind}/{path}.json"
        if not file.exists():
            problems.append(f"missing {kind} mmdlib:{path}")
            continue
        data = json.loads(file.read_text())
        for reference_kind, reference in references(kind, data):
            if reference.startswith("mmdlib:"):
                pending.append((reference_kind, reference[len("mmdlib:"):]))
        emit(f"assets/basemetals/{kind}/{rename_path(path)}.json",
             (json.dumps(rewrite(data), indent=2) + "\n").encode())

    # Never overwrite Base Metals' own files with different content.
    for path, data in sorted(writes.items()):
        if path.exists() and not same(path, data) and not path.relative_to(target).parts[0] == "mmdlib":
            problems.append(f"would overwrite different file {path.relative_to(repo)}")

    if problems:
        print("\n".join(problems), file=sys.stderr)
        raise SystemExit(1)

    changed = [path for path, data in writes.items() if not path.exists() or not same(path, data)]
    if args.check:
        for path in sorted(changed):
            print(f"out of date: {path.relative_to(repo)}")
        raise SystemExit(1 if changed else 0)
    for path in changed:
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_bytes(writes[path])
    print(f"{len(materials)} materials, {len(writes)} files ({len(changed)} written)")


def same(path, data):
    """Equal content; JSON is compared by value so formatting alone is no change."""
    existing = path.read_bytes()
    if existing == data:
        return True
    if path.suffix == ".json":
        return json.loads(existing) == json.loads(data)
    return False


def owner(stem, materials):
    """The material a resource belongs to, by its leading name."""
    for name in materials:
        if stem.startswith(name + "_") or stem.startswith("half_slab_" + name) \
                or stem.startswith("upper_slab_" + name):
            return name
    return None


def references(kind, data):
    """Models and textures named by a blockstate or model."""
    found = []

    def walk(value):
        if isinstance(value, dict):
            for key, item in value.items():
                if key == "model" and isinstance(item, str):
                    found.append(("models", item))
                else:
                    walk(item)
        elif isinstance(value, list):
            for item in value:
                walk(item)

    if kind == "blockstates":
        walk(data)
        return found
    parent = data.get("parent")
    if isinstance(parent, str):
        found.append(("models", parent))
    for texture in data.get("textures", {}).values():
        if isinstance(texture, str) and ":" in texture and not texture.startswith("#"):
            found.append(("textures", texture))
    for override in data.get("overrides", []):
        if isinstance(override.get("model"), str):
            found.append(("models", override["model"]))
    return found


if __name__ == "__main__":
    main()
