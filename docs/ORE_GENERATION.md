# Ore generation in Minecraft 1.10.2

Base Metals now requires MMD OreSpawn 4.1.2.110021 or a newer compatible 4.x
release. OreSpawn is the only ore-generation engine; Base Metals no longer
ships its old OS3 plugin or fallback generator. This also raises the minimum
Forge version to 12.18.3.2511.

## Existing worlds

Back up the world and its configuration folder before updating. Install the
new Base Metals and OreSpawn jars together, removing the older jars first.
Keep the old configuration files in place:

- OreSpawn 1: `config/orespawn/*.json`
- OreSpawn 3: `config/orespawn3/*.json`, together with `config/orespawn.cfg`

OreSpawn imports those files without changing the originals. Custom ore
frequencies, sizes, heights, dimensions and disabled rules carry across. The
old exclusive maximum height becomes an inclusive maximum one block lower;
for example, `maxHeight: 96` becomes `max_y: 95`.

Read `config/orespawn-upgrade-report.txt` after the first start. It lists what
was imported and anything that needs attention. Missing blocks and unsupported
custom settings cannot be guessed safely, so keep the report with your backup.

An OS1 `+` dimension entry is imported for the dimensions installed at that
start. If you add another dimension mod later, review its ore rules yourself.

The resulting provider override is `config/basemetals-orespawn.json`. Each
world stores its effective settings in
`<world>/serverconfig/orespawn-worldgen.json`. Existing OS4 overrides and world
profiles take priority over the defaults supplied by Base Metals. Updating the
mod does not reset them or replace generated terrain. Retrogen stays off
unless the imported configuration explicitly enables it.

OreSpawn's **Manage Vanilla Ores** switch controls imported vanilla-ore rules
as well as its newer vanilla defaults. When it is off, Minecraft generates its
own ores; imported vanilla rules stay saved but inactive. When it is on, the
imported frequency, height and vein settings are used without adding a second
vanilla/default pass. This switch does not disable Base Metals' own ore rules.
Changing it affects new generation, not ore already placed in existing chunks.

Existing provider overrides are kept even when legacy files are imported for
the first time. OreSpawn records the retained file in its upgrade report. An
unfinished or invalid override is left for the normal loader to check, not
silently replaced by older settings.

Copy the instance configuration folder as well as the world when moving an old
save to another installation. A world alone cannot recover custom ore settings
that were stored outside it.

## Changing ore placement

Use OreSpawn's ore editor, or edit its JSON files while Minecraft is stopped:

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
