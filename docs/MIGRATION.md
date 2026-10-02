# Migrating from Base Metals 1.10 or 1.12

Minecraft 1.13 introduced the flattening, which replaced numeric block and item
identifiers with names and palette-based block states. Third-party numeric
blocks cannot be reconstructed by Mojang's vanilla fixer alone, so Base Metals
installs a narrowly scoped migration hook before Forge reads an old world.

Always upgrade a copy and retain the original 1.10/1.12 world. Install Base
Metals `3.0.1.113021`, OreSpawn `4.1.0.113021`, and any other 1.13.2 mods needed
by that world. Do not carry MMDLib, Additional Loot Tables, the OreSpawn 3
plugin, or Base Metals' old fallback generator into the new instance.

For worlds whose `level.dat` or `level.dat_old` contains the old Forge registry
snapshot, Base Metals:

- restores the saved `basemetals:*` and `mmdlib:*` numeric block identities to
  Minecraft's flattening table before chunk conversion;
- translates legacy metadata for slabs, double slabs, stairs, doors,
  trapdoors, buttons, levers, pressure plates, anvils, plates, and fluids;
- protects existing legacy chunks from population writes while they are being
  converted;
- migrates Cyano and MMD registry aliases, including uppercase 1.10 names;
- converts old `forge:bucketfilled` Base Metals fluid stacks to dedicated
  buckets; and
- preserves tool and armour durability as flattened item NBT.

Before changing `level.dat`, `level.dat_old`, or a playerdata file, the hook
creates a sibling `*.basemetals-legacy-backup` copy. Minecraft converts region
chunks normally as they are loaded and saved, so the untouched source world is
the authoritative rollback. Never test an upgrade against the only copy.

Historical IDs retained or forwarded include hidden
`double_<material>_slab` blocks, legacy molten-fluid block IDs such as
`basemetals:adamantine`, `basemetals:liquid_mercury`,
`basemetals:carbon_powder`, Cyano `<material>_door_item` names, the old iron
nugget, and the MMDLib Vanilla Bits block/item/fluid names.

The migration supports the original Cyano Base Metals 2.4 line and the later
MMD Base Metals releases for Minecraft 1.10.2 and 1.12.2. It is not a downgrade
path: do not open a world already saved by Minecraft 1.14 or newer, including
the Base Metals 1.18.2 port, in Minecraft 1.13.2.

## OreSpawn configuration

The Base Metals provider uses the historical 1.13.2 defaults documented in
[WORLDGEN.md](WORLDGEN.md), including enabled copper. OreSpawn configuration
migration remains OreSpawn's responsibility. Keep a copy of the old OreSpawn
configuration alongside the untouched source world when validating an upgrade.

The generated manifests at `data/basemetals/registry_manifest_1_12.json` and
`data/basemetals/registry_manifest.json` document the historical registry
baseline and its 1.13.2 projection.
