# Build scripts

`build.gradle` holds the Minecraft target, toolchains, dependencies and resource
setup. Longer checks live in the scripts below, following OreSpawn's layout.
They share a small `baseMetalsBuild` map of artifact helpers and build paths.

| Script | Purpose |
| --- | --- |
| `release/artifacts.gradle` | Release identity, dependency checks, jar audits, checksums and Maven publication. |
| `verification/development.gradle` | Disposable development-server and legacy-upgrade profiles. |
| `verification/generated-data.gradle` | Catalogue resources and Kiri's ore model generation. |
| `verification/packaged-forge.gradle` | Finished-jar client, server and content-mode checks. |
| `ide/eclipse.gradle` | Buildship settings, launches and production-classpath isolation. |
| `ide/intellij.gradle` | Client and server shortcuts using IntelliJ's Gradle runner. |

Use Java 17 to run Gradle. ForgeGradle uses Java 25 for its Mavenizer; Minecraft
and the mod run on Java 8. Development accepts newer Java 8 updates, while
release auditing requires the pinned compiler in `gradle.properties`.

Run `check build` for the normal build, then `verifyReleaseArtifacts` and
`writeReleaseChecksums` when preparing a release candidate. Packaged checks
also need `packagedForgeServerRuntime` and `packagedForgeClientRuntime`, pointing
to official Forge installations. They copy runtime files into disposable build
directories and never open your normal saved worlds.

After changing launch settings, run `genEclipseRuns eclipse
verifyEclipseProductionClasspath`. IntelliJ users can use `genIntellijRuns` or
the Gradle `runClient` task. Tests and probes stay out of normal launches and
public jars.
