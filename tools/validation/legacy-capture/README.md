# Legacy world fixtures

This test mod creates a Minecraft 1.10.2 or 1.12.2 world using the original Base Metals
release being tested. It has no dependency on Base Metals or MMDLib classes,
so the same fixture generator works with both the Cyano and MMD releases.

The fixture contains every registered Base Metals and Vanilla Bits block
state in the Overworld, Nether and End. Chests contain every registered item,
including damaged and enchanted equipment, custom names, proof NBT and legacy
universal fluid buckets. Armour stands and player inventories provide separate
equipment and playerdata checks.

The first run saves a registry manifest and stops the server. A second run
checks that the original Minecraft version can reload the fixture. Only then
should a copy be opened in Minecraft 1.15.2.

## Build

From the Base Metals project directory, use its Gradle wrapper:

```sh
./gradlew -p tools/validation/legacy-capture build
./gradlew -p tools/validation/legacy-capture build -PlegacyMinecraft=1.12.2
```

The build uses Forge 1.10.2-12.18.3.2511 or 1.12.2-14.23.5.2847 and Java 8. The test JAR is
written to `tools/validation/legacy-capture/build/fixture/` and never belongs
in a public Base Metals release.

## Run the upgrade matrix

`tools/validation/test_legacy_worlds.js` takes a local JSON specification and
either `capture` or `upgrade`:

```sh
node tools/validation/test_legacy_worlds.js build/legacy-world-test-spec.json capture
node tools/validation/test_legacy_worlds.js build/legacy-world-test-spec.json upgrade
```

Keep that specification under ignored build output. It supplies these paths:

- `output`: a new, empty directory for disposable test servers and results;
- `java8`: the Java 8 executable;
- `runtime110` and `runtime115`: installed Forge server directories, including
  their launcher, Minecraft server JAR and `libraries` directory;
- `captureJar`, `modJar` and `probeJar`: the fixture generator, packaged Base
  Metals candidate and packaged runtime probe;
- `oreSpawn`: the release dependency, with `path` and `sha256`;
- `profiles`: entries with an `id`, expected `marker`, and either `mods110`
  for a newly captured 1.10 fixture or `world` for an existing source fixture.

Mod entries accept a path or an object containing `path` and `sha256`.
Pin historical release hashes when preparing the specification.

For a new 1.12 fixture, set `captureVersion` to `1.12.2`, `captureRuntime` to
its Forge server directory, and `captureLauncher` to its universal JAR's name.
Use `mods` for the profile's historical mod list. The runner passes the source
version to the capture mod so its manifest and completion markers agree.

The runner copies worlds before upgrading them, then checks the first load
and a save/reload. Source-world hashes must remain unchanged. It refuses to
overwrite a profile directory; use a fresh output directory for another run.
Logs and summary files stay alongside the disposable servers, outside Git.

A profile can also name an `advancementFixture` JSON file. The runner puts
that saved progress in the disposable copy, and the probe loads it through
Minecraft's player-advancement manager before checking it again on reload.
`src/integrationTest/resources/upgrade/advancement_progress_113.json` covers
an armour achievement and both ingot- and rod-based recipe discovery.

The 1.15 probe checks block identities and saved orientation/state, item
identity and count, durability, names, enchantments, proof NBT, worn armour,
playerdata and converted fluid buckets. Connected faces, powered states and
flowing-fluid levels can change normally when Minecraft ticks the world;
these are not treated as migration failures.

`compare_world_terrain.py` compares solid vanilla terrain across the same
world copies, including old numeric palettes and flattened palettes. It
requires Python and `nbtlib`; it never writes either world.
