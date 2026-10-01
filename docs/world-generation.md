# Base Metals world generation

Base Metals 2.6.0 requires OreSpawn 4.1.0.112021 or newer in the 4.x line.
OreSpawn is the only Base Metals ore generator. Base Metals does not register a
fallback generator.

Base Metals supplies eleven ore rules through OreSpawn's API (schema 5,
provider revision 2). Cold Iron and Adamantine generate
in the Nether, Starsteel generates in the End, and copper, silver, tin, lead,
zinc, mercury, nickel and platinum generate in ordinary dimensions. Existing
registry names and the historical distribution values are preserved.

High Fantasy and Low Fantasy both enable all eleven rules by default. Content
mode does not change OreSpawn world generation. Existing global overrides and
saved-world profiles remain authoritative and are never rewritten silently.

## Letting another mod generate the ores

For a new world, copy
[`examples/basemetals-orespawn-disabled.json`](examples/basemetals-orespawn-disabled.json)
to the instance's `config` directory and rename it to:

```text
basemetals-orespawn.json
```

The example is a complete OreSpawn provider override with every Base Metals
ore rule disabled. Configure CoFH World or another generator separately, then
create the world. OreSpawn will still be present because it is a required
dependency, but it will not place Base Metals ores.

OreSpawn stores a self-contained configuration snapshot for each world. To
change an existing world, disable the Base Metals rules through OreSpawn's
configuration screen or edit:

```text
<world>/serverconfig/orespawn-worldgen.json
```

Make a backup before editing a world's snapshot. Changing generation settings
does not remove ore from chunks that have already been generated. Retrogen is
disabled by default and should remain disabled when another generator owns the
same ores.

## Upgrading an older installation

OreSpawn 4 migrates `config/orespawn3/basemetals.json` into the same stable
rule IDs used by the native provider. Customized frequencies, heights and
disabled rules therefore remain associated with their Base Metals rules.
Previously generated `config/basemetals-orespawn.json` overrides also remain
valid and authoritative.

The legacy MMDLib settings named `using_orespawn` and `fallback_orespawn` are
not Base Metals OreSpawn 4 controls. Use the provider override or the per-world
OreSpawn configuration instead.
