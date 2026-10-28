# World upgrade checks

`test_legacy_worlds.js` starts the packaged Forge 26.1.2 server against a fresh
copy of each supplied world, then starts it again to check the saved result.
It refuses to overwrite an existing test directory and fingerprints every
source world before and after the test.
An optional `forgeVersion` selects another installed Forge 26.1.2 build for
compatibility checks; the default is the compilation target, `64.0.9`.

Pass a local JSON specification containing `java` (the Java 25 executable), `serverRuntime`, `output`,
`modJar`, `probeJar`, `oreSpawn` (a `path` and `sha256`), and a `profiles` list.
Each profile needs an `id`, `world` path and expected log `marker`. Captured
fixtures include a registry manifest so the probe can check each block state,
inventory stack, worn item and filled bucket rather than just successful startup.
Keep machine-specific specifications and test output outside the checkout.
For fresh-world compatibility checks, set `fresh:true` and `fixture:false`.
Optional `extraMods`, `contentMode`, `dataPacks` and JVM `properties` let the
same runner check installed integrations and explicit data-pack overrides.
Use `lateChunks:true` on captured fixtures to leave Nether and End blocks
unopened through the first load and reload, then check them on a third start.
`oreOverrides` edits named Base Metals rules in the disposable world's saved
OreSpawn profile and verifies that those values survive both starts.

The log check accepts known vanilla registry changes, including paintings moved
to data files. Unknown or modded entries still fail the test. Check that boundary
with `node --test tools/validation/test_painting_registry.js`.

`compare_world_terrain.py` checks fixed vanilla terrain before and after
conversion. It accepts Minecraft's new underground floor and accounts for
normal falling sand and gravel, but rejects ore replacing existing rock.
Exposed dirt may become grass only when grass was already growing beside it;
buried changes and new ore in saved terrain still fail the comparison.
It needs the Python `nbtlib` package. Run `test_world_terrain.py` to check the
palette readers and falling-block comparison.

`compare_current_world.py` compares Base Metals block states, stored items,
equipment and advancement progress in flattened saves. It reads both older
chunk entity lists and the separate entity files used by newer Minecraft.
Item comparisons account for the component conversion while still checking
damage, enchantments, names, lore and custom data.
Flying projectiles are excluded from the static item count because they can
hit targets during the test; their ammunition save/load and pickup behaviour
are covered by the runtime tests. Run all comparison-tool regressions with
`python -m unittest discover -s tools/validation -p 'test_*.py'`.

## Client/server content modes

`test_content_modes.js` launches a separate client and server for each case.
It checks unchanged older configs, missing configs, matching Low Fantasy modes,
and both kinds of mismatch. Matching clients disconnect and rejoin three times
to check repeated connections. Existing gameplay switches must keep their values.

Its local specification needs `java` (the Java 25 executable), `serverRuntime`, `clientRuntime`,
`output`, `modJar`, `serverProbe`, `clientProbe`, and the same checksum-pinned
`oreSpawn` object used by the world tests. The test uses localhost only and
chooses a free port; no public Minecraft server is involved.
