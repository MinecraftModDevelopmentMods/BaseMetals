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

Normal client and server launches use an explicit runtime classpath, not
Buildship's full dependency container. Compile-only integration APIs therefore
remain available in the editor without loading extra mods into the game.

`runClientOptionalMods` uses a separate folder for optional-mod testing. No
optional mods are added automatically: the inherited integration mods do not
publish builds for Minecraft 1.19.4. The `compatSmoke` and `packagedCompatSmoke`
switches reject those unsupported jars rather than loading another Minecraft
version's mods.
