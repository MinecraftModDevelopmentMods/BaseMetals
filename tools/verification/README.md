# Port regression checks

These checks are optional. They leave the normal build and deployment setup
alone and do not add test classes to the published jar.

Use Java 21 and run this from the project directory:

```text
./gradlew -I tools/verification/port-checks.init.gradle portTest runPortGameTestServer
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

`portTest` checks the generated recipe unlocks, loot registrations and current
player-facing translation keys. Older, unused translation aliases may differ
between languages; those are not required to match.

`runPortGameTestServer` starts a disposable test server in `run-port-gametest`.
It checks shield upgrades, old and current projectile item data, recipe network
encoding, enchanted smith trades, village chest additions and loaded
advancements. It never opens a normal save.

After changing generated resources, run `runData` before these checks. Include
the resulting JSON files with the source changes, not the datagen `.cache`
files or the test server's logs and world.
