# Gradle build layout

The top-level `build.gradle` contains the shared project identity, Java
toolchain, source sets, and Forge development runs. The applied scripts keep
the remaining build concerns small enough to review independently.

- `dependencies.gradle` owns repositories and dependency scopes.
- `release/artifacts.gradle` builds and audits deterministic release files.
- `release/publishing.gradle` describes guarded Maven publication.
- `verification/support.gradle` owns data, GameTest, and upgrade smoke gates.
- `verification/packaged-runtime.gradle` runs the built mod in official
  packaged client and dedicated-server installations.
- `ide/eclipse.gradle` normalizes ForgeGradle's Eclipse launches and checks
  that production runs cannot see validation-only code.

Applied-script order is intentional. Artifact setup publishes an immutable
`baseMetalsRelease` map consumed by publishing and packaged-runtime checks.
Keep machine-specific paths in Gradle properties, never in these scripts.

Model and data providers live in the separate `dataGenerator` source set.
Use `generateResources` after changing them to update the committed resources.
`check` generates a temporary copy and reports differences without changing
source files. Neither the providers nor test probes enter ordinary game
launches or public jars.

The scythe's harvestable-block tag lives in `src/main/resources`. Its explicit
block list preserves the older harvesting behaviour, while vanilla crop,
flower, sapling and leaf tags include suitable new plants.

Normal client and server launches use an explicit runtime classpath, not
Buildship's full dependency container. Compile-only integration APIs therefore
remain available in the editor without loading extra mods into the game.

`runClientOptionalMods` uses a separate folder and loads the pinned 1.20.1
integration mods marked `development` in `gradle/optional-mods.json`. Their downloaded jars
are checked by `verifyOptionalRuntimeDependencies`. Ordinary client and server
launches still load only essential mods.

Use `compatSmoke` for development-runtime integration tests or
`packagedCompatSmoke` for the packaged server tests. Neither switch is needed
for ordinary development.

Thermal and Ender IO are covered by packaged client/server tests. Their embedded
dependencies still use SRG names after development remapping, so they are not
included in the IDE profile.
