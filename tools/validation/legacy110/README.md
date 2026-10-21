# 1.10 migration checks

These checks use disposable worlds. Never point a server launch at the only copy
of a player's world.

The main Gradle project compiles the complete mod with Forge 2511 and stable-29
mappings. Run the repository's wrapper from the project root, using Java 17 for
Gradle. The Java 8 compiler and runtime are selected separately:

```text
gradlew clean check build migrationProbeJar
```

The main jar contains production code and resources only. The separate
`build/test-mods/legacy110-probe.jar` is a server-side test mod; do not distribute it with releases
or leave it installed for normal play.

`audit_artifacts.py` compares the candidate with the pre-namespace production jar.
It checks the complete class and resource inventory, Java 8 bytecode, saved
resource names, version metadata and exclusion of test material and local paths.

## Runtime checks

For the captured Cyano and MMD fixtures, launch the packaged server with
`review.manifest` pointing to `legacy_registry_manifest_runtime.json` and
`review.phase` set to `first`, `reload`, `delayed`, then `stable` on separate boots.
The first two boots leave the remote fixture chunk unopened. The next two check
every saved item and block state, including names, enchantments and other item
data. Each boot also checks the Human Detector's item, player-only detection and
redstone activation/release. Read the JSON report and require `passed: true`;
Minecraft's exit code alone does not indicate success.

For a larger world, supply `review.chunks` instead: a UTF-8 file of
`dimension,chunkX,chunkZ` lines. The probe loads and saves those chunks, then stops
the server. Its completion message confirms only that the visits finished, not
that all content survived.

Use `audit_world.py` before and after these visits to compare the saved blocks and
inventories. It needs Python 3, NumPy and nbtlib. Pass the earlier report through
`--source` for comparisons. The audit fingerprints the input world, checks every
saved Base Metals and Advantage block state, and compares item counts, damage and
NBT. The `--delay` check inspects Sylvester's remote powder chest; its raw chunk
hash must stay unchanged until the delayed visit. After that visit it must contain
220 coal powder items under the current name, and retain them after another boot.

Keep the original world's fingerprint, the mod-jar hashes, each boot's logs and
the audit reports with the local test evidence. Existing Cyano integrations need
separate compatibility checks; registry migration cannot repair an add-on's Java
API references.

## Golems and old add-on APIs

Use the original Base Metal Golems 6.02.1 and Extra Golems 6.02 jars unchanged.
With Cyano Base Metals installed, `golem.phase=seed` saves all eighteen golem
types near spawn and another eighteen in the remote chest chunk. Keep that
seeded world as a read-only reference, and test a copy with the candidate jar.

Run separate boots with `golem.phase` set to `first`, `reload`, `delayed` and
`stable`. The first two must exclude the remote chunk from `review.chunks`;
the last two include it. Each boot checks the real add-on's build-event handler,
material blocks and ingot drops. It also checks the saved golems that should
have loaded by that point. `audit_golems.py` compares their saved identities,
health, equipment and persistent NBT. Use `--unopened` before the delayed visit
to require an identical raw chunk, not just equivalent entities.

For the old MMD API check, build the probe with
`-PlegacyBaselineJar=<pre-namespace Base Metals jar>`. This compiles a small
consumer against the old classes, not the new source. Set
`golem.checkMmdApi=true` at runtime to check its class literals, reflection,
inheritance, mod instance and material lookups after relinking.

Finally use a fresh, unseeded world copy with `golem.phase=absent`, first without
Base Metal Golems and then without Extra Golems too. Check the JSON reports for
`passed: true`; this also exercises the Human Detector and confirms the bridge
has no required add-on dependency. Never remove the add-ons from the only copy
of a world containing their entities.

## OreSpawn upgrades

`run_orespawn_checks.py` creates and saves a world with the previous Base Metals
and OreSpawn 3 jars, then upgrades disposable copies. It covers stock, edited
and disabled OS3 rules, optional OS1 input, existing OS4 overrides, reloads and
chunks opened later. The runner also samples fresh Overworld, Nether and End
chunks and checks every Base Metals ore against its configured height range.

Use the packaged versions of its probes: `packagedLaunchProbeJar` and
`packagedOreSpawnProbeJar`. The regular probe jars use development mappings for
IDE launches. All probes remain outside the public jar.

`run_vanilla_takeover_checks.py` checks the Manage Vanilla Ores switch against
an imported custom diamond rule. It needs the same packaged jars, Java and
Forge template as the upgrade runner, plus NumPy and nbtlib. It tests both switch
settings, reloads and later exploration, confirms Base Metals ores still generate,
and compares 49 existing chunks after switching off, on and back off. The source
configuration and fixture world must remain unchanged.

## Automated CI suite

`packagedForgeRuntimeTest` builds all probes separately and runs `run_ci_checks.py`.
It downloads checksum-pinned public releases, creates its own disposable Cyano
and MMD worlds, and checks first load, reload, delayed chunks and a further reload.
The fixture includes registered items with names, damage and NBT, plus block states
in the Overworld, Nether and End. Neither private worlds nor local mod builds are
needed. OS1/OS3 imports, override precedence and vanilla takeover are checked in
the same suite.

The eighteen-golem/Advantage-mod checks remain a separate local compatibility
profile. They are not part of the ordinary client launch or the hosted CI suite.
All generated worlds, downloaded jars, logs and reports stay under the ignored
build directory. Keep failed evidence before running `clean`.
