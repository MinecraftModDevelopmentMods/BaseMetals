# Migrating from Base Metals 1.10 or 1.12

Always upgrade a copy and retain the original legacy world. Install Base Metals
3.1.0 and OreSpawn 4.1.0, but do not carry MMDLib, Additional Loot Tables, the
OreSpawn 3 plugin, or the old native fallback generator into the 1.20.6 instance.

Minecraft's data fixer cannot identify third-party numeric block IDs from
before the 1.13 flattening. Base Metals snapshots those positions, runs
Mojang's complete chunk fixes through the 1.13 palette transition, and then
overlays only the saved third-party blocks and their original block entities.
Any vanilla block entities which Mojang's fixer inferred from a reused mod
numeric ID are removed at those positions. Vanilla's context-sensitive block,
block-entity, entity, inventory, map, statistics, objective, jukebox, spawn
egg, banner, and villager fixes still run everywhere else. The migrator reads
the exact numeric map from an untouched legacy `level.dat`, `level.dat_old`,
or the `basemetals-legacy-registry.dat` snapshot
when available. If neither retains a legacy map, it uses the map captured from
the public Base Metals 2.5.0-rc2.332 and MMDLib 1.0.0-rc2.36 runtime.

Before replacing any region file, `level.dat`, or playerdata file, the
migrator copies the original to
`basemetals-1.12-region-backup` inside the world. Region replacements are
transactional. A completed scan writes
`BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt`, recording either the conversion
summary or that no legacy content was present; subsequent starts do nothing.

The packaged fallback map covers the exact public 1.12 runtime. With a saved
legacy registry map, blocks from the world's other 1.10/1.12 mods also retain
their registry name and original block-entity data, although block states
without a Base Metals-owned metadata decoder fall back to that mod's default
state. Base Metals and historical `mmdlib:*` states retain their old metadata
semantics, including complete paired-door state. If neither legacy level file
contains an ID used by a numeric chunk, migration stops instead of guessing or
deleting it. Restore the backed-up regions and recover the world's original
`level.dat_old` before retrying. Forge normally creates that file before Base
Metals' migration hook runs; keep it beside `level.dat` for the first upgrade.
This supports both the original Cyano Base Metals 2.4 line and the later MMD
Base Metals 2.5 line. Registry paths containing uppercase characters, which
Minecraft 1.10 still allowed, are normalized to the lowercase form required by
modern Minecraft.

Base Metals 3.1.0 preserves historical `basemetals:*` IDs, including hidden
`double_<material>_slab` blocks and molten-fluid block IDs such as
`basemetals:adamantine`. Dedicated bucket items and flowing-fluid registry
entries are new. Legacy `forge:bucketfilled` stacks containing Base Metals
fluids become the corresponding dedicated bucket, and damage on old Base
Metals/MMDLib tools and armor is preserved instead of being consumed as
pre-flattening item metadata. Dedicated-server playerdata and the embedded
single-player inventory in `level.dat` follow the same conversion on the first
upgrade start. Missing mappings also forward:

- `basemetals:liquid_mercury` to `basemetals:mercury`;
- `basemetals:carbon_powder` to `basemetals:coal_powder`;
- Cyano-era `basemetals:<material>_door_item` IDs to the corresponding
  `basemetals:<material>_door`;
- Cyano-era `basemetals:iron_nugget` to `minecraft:iron_nugget`, and its
  `basemetals:liquid_mercury` block item to `basemetals:mercury_bucket`;
- historical `mmdlib:*` Vanilla Bits blocks, items, and fluids to matching
  `basemetals:*` IDs.

## Moving from 1.13 through 1.19

These worlds already use named block states, so they do not need the numeric
ID conversion. Older wall connections are converted to the current wall
states. Item aliases, tool damage, names and enchantments are retained.

Older custom dimensions using Minecraft's removed `vanilla_layered` biome
source are converted to its 1.18 Overworld replacement. Their dimension IDs,
seeds and noise settings are retained, including the large-biome setting.
Modded biome sources are left alone. The original `level.dat` is backed up
before this conversion.

Older Forge versions also saved a numeric vanilla biome registry index. Minecraft
now loads biomes from world data, so the obsolete Forge index is removed when it
contains only known vanilla names. Chunk biomes are left to Minecraft's data fixer.
Snapshots with modded or unknown biomes, overrides or aliases are retained.

Minecraft 1.18 can generate new terrain below previously saved chunks.
Base Metals lets that conversion run, while preventing ore generation from
replacing blocks in the saved 0–255 height range. Newly generated chunks and
the new underground area can still receive ores. The save hooks stop with an
error if the expected Minecraft conversion methods change, rather than
silently skipping these protections.

Old inventories remain valid in either content mode. A configuration without
`contentMode` starts in High Fantasy; Low Fantasy only restricts new acquisition.
The mode is a server or modpack setting, not a setting stored in each world.

## Moving from 1.20.1

Minecraft 1.20.6 stores item properties as components rather than the older item
tag. Base Metals keeps names, enchantments, durability and custom data during
that conversion. Saved arrows and bolts also retain their ammunition.

Make the first upgrade on a copy, then check stored equipment and inventories
before using the upgraded world normally. Keep the original: a converted world
must not be opened in an older Minecraft version.

## OreSpawn 3 configuration

OreSpawn 4.1 migrates OS3 files before merging provider defaults. When an
installed provider has one rule whose output matches a legacy rule, the old
values are written under that provider rule ID. Explicit user values,
including enabled legacy Base Metals copper, therefore survive without a
duplicate rule. Ambiguous and unmatched outputs retain legacy IDs and are
listed in `config/orespawn-migration/migration-report.txt`.

Fresh profiles use the provider defaults in [WORLDGEN.md](WORLDGEN.md).
Migrated profiles retain explicit enabled flags, quantities, frequencies,
dimensions, and heights.

The generated manifests at `data/basemetals/registry_manifest_1_12.json` and
`data/basemetals/registry_manifest.json` document the runtime baseline,
restored content, and explicit 1.18 additions.

## Existing modern worlds

Modern worlds do not need the old numeric-ID conversion. Base Metals only runs
it after confirming an older source save. A leftover `level.dat_old` does not
replace the current player or turn on legacy terrain protection.

For a genuine upgrade, the original single-player record is captured before
Minecraft reads it. The world-local migration sidecar keeps the source version
and saved registry map for reloads and chunks opened later; it does not store
a player record. A saved registry map from an earlier upgrade takes precedence
over an older backup.

Chunk reading uses Minecraft's supported region formats. Empty region files
are left alone, and backups include external chunk files when their region
needs conversion.
