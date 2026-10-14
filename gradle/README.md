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

`runClientOptionalMods` launches the `optionalMods` client with Tinkers and
Mantle. Extra dependencies selected with `compatSmoke` affect only this
separate profile. Use `packagedCompatSmoke` to check original mod distributions;
this avoids remapping problems in upstream JARs such as CoFH Core.
