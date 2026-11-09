# Ore generation in Minecraft 1.11.2

Base Metals now requires MMD OreSpawn 4.1.6.111021 or a newer compatible 4.x
release. OreSpawn is the only ore-generation engine; Base Metals no longer
ships its old OS3 plugin or fallback generator. This also raises the minimum
Forge version to 13.20.1.2588.

## Existing worlds

Back up the world and its configuration folder before updating. Install the
new Base Metals and OreSpawn jars together, removing the older jars first.
Keep the old `config/orespawn/` or `config/orespawn3/` files and
`config/orespawn.cfg` in place. OreSpawn can import their rules without changing
the source files or replacing an existing Base Metals provider override. Check
`config/orespawn-upgrade-report.txt` before exploring new chunks.

The Base Metals provider override is `config/basemetals-orespawn.json`. Each
world keeps its effective settings in
`<world>/serverconfig/orespawn-worldgen.json`. Back up these files too if you
already use OreSpawn 4. Existing ores are not replaced by a change to the
defaults; review any imported retrogen setting separately.

Copy the instance configuration folder as well as the world when moving an old
save to another installation. A world alone cannot recover custom ore settings
that were stored outside it. If an OreSpawn 1 world already has an imported rule
saved in its profile, OreSpawn keeps that rule instead of adding a second copy.

OreSpawn's **Manage Vanilla Ores** switch also applies to imported vanilla ore
rules. With it off, Minecraft places its own ores and the imported settings stay
saved but inactive. With it on, the imported rules take over without a second
vanilla pass. Base Metals ores are unaffected by that switch.

## Changing ore placement

Use OreSpawn's editor, or edit its JSON files while Minecraft is stopped:

- `config/orespawn-worldgen.json` holds defaults for new worlds.
- `config/basemetals-orespawn.json` overrides the Base Metals provider.
- `<world>/serverconfig/orespawn-worldgen.json` holds an existing world's rules.

The old `using_orespawn` and `fallback_orespawn` entries in `BaseMetals.cfg`
are no longer read. They can remain in an older file, but they do not disable
OreSpawn 4. If CoFH World or another mod places your ores, disable the Base
Metals rules in OreSpawn to avoid generating them twice.

If you previously used the internal fallback generator without an OreSpawn
configuration, new chunks use the defaults below. The fallback's approximate
placement is not an OreSpawn config that can be imported. Existing chunks are
still kept as they are.

## Default rules

Every rule ID starts with `basemetals:legacy/` and ends in `<material>_ore`.
Those IDs are kept stable for imported configurations and saved world profiles.

| Material | Dimension | Height, inclusive | Attempts per chunk |
| --- | --- | --- | ---: |
| Cold Iron | Nether | 0–127 | 5 |
| Adamantine | Nether | 0–127 | 2 |
| Star Steel | End | 0–254 | 5 |
| Copper | Ordinary dimensions | 0–95 | 10 |
| Silver | Ordinary dimensions | 0–31 | 4 |
| Tin | Ordinary dimensions | 0–127 | 10 |
| Lead | Ordinary dimensions | 0–63 | 5 |
| Zinc | Ordinary dimensions | 0–95 | 5 |
| Mercury | Ordinary dimensions | 0–31 | 3 |
| Nickel | Ordinary dimensions | 32–95 | 1 |
| Platinum | Ordinary dimensions | 1–31 | 0.125 |

Ordinary dimensions means every dimension except the vanilla Nether and End.
Each attempt has a block budget of 4–11, matching the old size 8 / variation 4
settings. Actual yield also depends on the available host rock.

Antimony and Bismuth remain registered but do not gain new generation rules.
Materials disabled in `BaseMetals.cfg` are omitted from the native defaults.
Base Metals supplies no terrain, biome or strata settings. Mineralogy remains
optional and can supply ore-replaceable rocks through OreSpawn.
