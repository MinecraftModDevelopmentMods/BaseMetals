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
- `ide/intellij.gradle` generates Gradle-backed client and server configurations.

Applied-script order is intentional. Artifact setup publishes an immutable
`baseMetalsRelease` map consumed by publishing and packaged-runtime checks.
Keep machine-specific paths in Gradle properties, never in these scripts.

Model and data providers live in the separate `dataGenerator` source set.
Use `generateResources` after changing them to update the committed resources.
Run it before starting a separate build, so resource processing sees the new files.
`check` generates a temporary copy and reports differences without changing
source files. Neither the providers nor test probes enter ordinary game
launches or public jars.

The scythe's harvestable-block tag lives in `src/main/resources`. Its explicit
block list preserves the older harvesting behaviour, while vanilla crop,
flower, sapling and leaf tags include suitable new plants.

Normal client and server launches load only Base Metals and OreSpawn. The
optional-mod profile has its own run folder, but no optional integration mods
are pinned for Forge 1.21.11. The Mekanism, Tinkers, Thermal and Ender IO Java
integrations are not enabled here; their dormant data remains available for
future integration work.

Filled buckets use three atlas aliases from the vanilla bucket and Forge masks.
They keep the bucket and molten fluid on the block atlas without copying those
textures, so resource packs can still replace them.

Run `prepareEclipse` after importing the project or refreshing Gradle. It
generates the launches, copies processed resources and checks that ordinary
runs cannot see tests or probes. Client launches explicitly select the client
side of ForgeGradle's launcher so it resolves assets and native libraries.

The two CI client smokes pass `-PvirtualDisplaySmoke=true`. This disables
Forge's early splash window only in their disposable run directories; the
game window and integrated-world assertions still run. Leave this option off
for ordinary launches. `verifyVirtualDisplaySmokeSetup` checks both paths as
part of `check`.
