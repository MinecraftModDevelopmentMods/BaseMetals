# Migrating from earlier Base Metals versions

Minecraft 1.13 introduced the flattening, which replaced numeric block and item
identifiers with names and palette-based block states. Third-party numeric
blocks cannot be reconstructed by Mojang's vanilla fixer alone, so Base Metals
installs a narrowly scoped migration hook before Forge reads an old world.

Always upgrade a copy and retain the original 1.10/1.12 world. Install Base
Metals `3.1.0.117011`, OreSpawn `4.1.0.117011`, and any other 1.17.1 mods needed
by that world. Do not carry MMDLib, Additional Loot Tables, the OreSpawn 3
plugin, or Base Metals' old fallback generator into the new instance.

For worlds whose `level.dat` or `level.dat_old` contains the old Forge registry
snapshot, Base Metals:

- restores the saved `basemetals:*` and `mmdlib:*` numeric block identities to
  Minecraft's flattening table before chunk conversion;
- translates legacy metadata for slabs, double slabs, stairs, doors,
  trapdoors, buttons, levers, pressure plates, anvils, plates, and fluids;
- protects saved chunks in the Overworld, Nether, End and named custom
  dimensions from neighbouring world-generation writes during conversion;
- migrates Cyano and MMD registry aliases, including uppercase 1.10 names;
- converts old `forge:bucketfilled` Base Metals fluid stacks to dedicated
  buckets; and
- preserves tool and armour durability as flattened item NBT.

Before changing `level.dat`, `level.dat_old`, or a playerdata file, the hook
creates a sibling `*.basemetals-legacy-backup` copy. Minecraft converts region
chunks normally as they are loaded and saved, so the untouched source world is
your backup if you need to go back. Never test an upgrade against the only copy.

Historical IDs retained or forwarded include hidden
`double_<material>_slab` blocks, legacy molten-fluid block IDs such as
`basemetals:adamantine`, `basemetals:liquid_mercury`,
`basemetals:carbon_powder`, Cyano `<material>_door_item` names, the old iron
nugget, and the MMDLib Vanilla Bits block/item/fluid names.

The migration supports the original Cyano Base Metals 2.4 line and the later
MMD Base Metals releases for Minecraft 1.10.2 and 1.12.2. It is not a downgrade
path: do not open a world already saved by Minecraft 1.18 or newer, including
the Base Metals 1.18.2 port, in Minecraft 1.17.1.

## Mod settings

This port uses `config/basemetals-common.toml`. It does not read the old
`BaseMetals.cfg` or `MMDLib.cfg` files. Configure the four gameplay switches
and content mode through Forge's Mods list or the Base Metals cog in OreSpawn.
High Fantasy is the default; if you used Low Fantasy in 1.12, select it again
in the new screen and restart Minecraft.

The old switches for disabling individual materials are not carried forward.
Blocks and items remain available so old saves still load. Use Low Fantasy to
limit what you can make instead. See [Content modes and configuration](CONTENT_MODES.md).

## OreSpawn configuration

The Base Metals provider keeps the historical defaults documented in
[WORLDGEN.md](WORLDGEN.md), except that new profiles leave Base Metals copper
disabled in favour of vanilla copper. Existing profiles and explicit overrides
keep their values. OreSpawn handles its own configuration migration.
Keep a copy of the old OreSpawn
configuration alongside the untouched source world when validating an upgrade.

The generated manifests at `data/basemetals/registry_manifest_1_12.json` and
`data/basemetals/registry_manifest.json` list the old registered names and their
1.17.1 counterparts.

During a pre-flattening upgrade, vanilla can mistake the lower bits of a modded
block ID for a bed and create an invalid bed entity there. Base Metals removes
those stray entities only where the converted block belongs to Base Metals.
It does not remove real beds or change their blocks.

## Moving from Minecraft 1.13.2, 1.14.4, 1.15.2 or 1.16.5

Use a copy of your older world, with the 1.17.1 builds of Base Metals and
OreSpawn. Keep `basemetals-common.toml` and your OreSpawn settings: this port
uses the same configuration keys, registered names and ore-rule IDs. Existing
items, recipe-book progress and terrain should remain unchanged. Once Minecraft
has saved the copy in 1.17.1, do not reopen it in the older game.

Base Metals converts the old boolean wall connections to 1.16's low and absent
connections. Existing tall connections and wall posts are left as they are.

Forge 1.13 saved a profession registry that no longer exists in 1.14. Base
Metals removes that obsolete index when it contains only vanilla professions;
Minecraft still converts the villagers and their saved trades normally. If
another mod added professions to that index, Forge keeps its warning so you
can check that mod's migration support. The original `level.dat` is backed up
before Base Metals changes it.

The old vanilla sign and dye names are mapped to oak signs and the matching
1.14 dyes. Other missing registry entries still need the relevant mod's
migration support; Base Metals does not silently discard them.

On the first upgrade, Forge may also warn about vanilla registry entries that
Minecraft 1.16 removed or renamed, such as zombie pigmen, their spawn egg and
sounds. These warnings are separate from Base Metals' migration. Keep the
backup and check any missing entries from other mods before continuing.
