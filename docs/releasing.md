# Releasing Base Metals

Run **Release Base Metals** in the MMD repository's Actions tab. The workflow
lives on the default branch, but builds the selected Minecraft branch rather
than the default branch's mod.

Enter the full four-part Base Metals version, choose the CurseForge release
level, and confirm publication. For example, `3.0.1.113021` selects Minecraft
1.13.2 with Forge and releases `master-1.13.2`.

## Branches and version numbers

The final version component encodes Minecraft's major version, two-digit minor
version, two-digit patch version, and a loader digit: `1` for Forge or `2` for
NeoForge. The dispatcher looks for `master-<major>.<minor>.<patch>` first, then
`master-<major>.<minor>`. NeoForge branches have the suffix `-neo`.

This follows OreSpawn's branch convention, including newer `26.x` versions.
There is no list of allowed Minecraft versions to update when adding a port.
The selected branch must exist and its metadata must match the requested
release. Old branches without the modern build and release checks cannot be
released through this workflow.

## Preparing a target branch

Each port needs:

- Matching `mod_version`, `minecraft_version`, `loader_name`, and `loader_code`
  in `gradle.properties`, with `curseforge_project_id=240967`.
- `java_version` for Minecraft, `java_toolchain_version` for compilation, and
  `gradle_java_version` for Gradle. `java_setup_version` can specify the hosted
  JDK download selector. `gradle_java_setup_version` can pin a separate Gradle
  JDK; legacy Java 8 and 16 ports default to Temurin `17.0.1+12` for Gradle.
- An exact, checksum-verified OreSpawn dependency and either
  `gradle/stage-orespawn-release.sh` or the legacy dependency-staging script.
- `check`, `build`, `javadoc`, `verifyReleaseArtifacts`, `writeReleaseChecksums`,
  and the prepared-artifact Maven publication used by the existing ports.
- A successful **Build, test, and audit** check on the exact release commit.

Required CurseForge dependencies come from `cf_requirements`. MMDLib-free ports
may instead declare `orespawn_curse_project_id=245586`, which makes OreSpawn
required without adding MMDLib. The 1.12 branch still requires both mods.

## Publication

The workflow builds and tests once, then records checksums for the main,
sources, and Javadoc jars. It publishes those same files to MMD Maven,
CurseForge, and finally GitHub Releases. An existing tag must point to the
validated commit; the workflow never moves it to another commit.

Publication requires `MAVEN_UPLOAD_URL`, `MAVEN_UPLOAD_USERNAME`,
`MAVEN_UPLOAD_PASSWORD`, and `CURSEFORGE_TOKEN` in the MMD repository. Forks can
run CI but cannot publish through this dispatcher.
