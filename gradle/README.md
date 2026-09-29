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
