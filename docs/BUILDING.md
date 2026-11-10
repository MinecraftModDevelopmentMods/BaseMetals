# Building Base Metals 1.11.2

Use the inner `BaseMetals` directory as the project root. The Gradle wrapper is
part of the project; there is no need to install Gradle separately.

## Java versions

- Java 17 runs Gradle 9.6.1 and ForgeGradle 7.0.34.
- Java 25 runs Forge's Minecraft Mavenizer during Minecraft setup.
- Java 8 compiles the mod and runs Minecraft 1.11.2.

Ordinary development accepts compatible Java 8 installations. Release checks use
Temurin 8u502-b07 and Gradle on Temurin 17.0.1+12. Do not change Eclipse's own
runtime to Java 8: its Gradle and Minecraft runtimes are separate settings.

The compile and runtime target is Forge 13.20.1.2588 with MCP stable 32 for 1.11 mappings.
OreSpawn 4.1.6.111021 is pinned through Curse Maven and verified against its
published SHA-256. The branch starts from the updated Base Metals 1.10.2 release.

## Eclipse

Import the project as an existing Gradle project, select the wrapper and use
Java 17 for Gradle. Run the `prepareEclipse` task once, then refresh the project.
Choose the generated `runClient` or `runServer` launch, using JavaSE-8.
Gradle synchronization regenerates those launches when needed.

Eclipse copies resources from Gradle's processed output, so a clean Eclipse build
cannot replace the mod version with a `${version}` placeholder. After changing
textures or other resources, refresh Gradle or run `prepareEclipse` again.

The launch tasks merge production classes and processed resources, so Minecraft
sees one mod root. Ordinary launches include the published OreSpawn 4.1.6.111021
release. Other integration APIs, JUnit and the migration probes
are excluded. Keep personal worlds outside the development `run` directory.

Remapped copies of signed legacy dependencies can report a fingerprint warning
in development. Their original downloads are still checksum-verified; the
transformed copies cannot retain a valid signature for the original bytes.

## Build checks

```text
gradlew clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums prepareEclipse
```

The installable jar is `build/libs/BaseMetals-2.5.2.111021.jar`. Sources,
Javadocs are written alongside it; checksums are in `build/release/SHA256SUMS`. The mapped development jar
is kept separately under `build/libs-dev`; it is not the jar to install in a
normal Minecraft instance. Building never uploads anything.

The dependency list and SHA-256 values are in `gradle/dependencies.json`.
For an offline or mirrored setup, Python 3 can stage those exact files before
running Gradle:

```text
python gradle/stage-legacy-dependencies.py .gradle/legacy-dependencies
```

Use `--cache <Gradle-user-home>` to reuse verified downloads. A complete local
mirror is used instead of the old artifact hosts. Generated mirrors, IDE files,
worlds and reports are ignored by Git.

CI and the release dispatcher call `bash gradle/stage-legacy-dependencies.sh <mirror>`
and pass that directory as `-PlegacyDependencyVerificationRepository=<mirror>`.
The older `legacyDependencyRepository` property still works for local setups.

## CI and releases

The target branch is `master-1.11.2`, matching OreSpawn's naming. CI runs on that
branch and feature branches, with separate CodeQL and wrapper checks. It tests
a cold Forge setup, an offline rebuild, Java 8 bytecode, Eclipse isolation and
byte-identical release jars. Public 1.11.2 releases are used to create temporary upgrade worlds; personal
saves are never needed by CI.

To run the packaged suite locally, prepare a disposable Forge server with
`gradle/prepare-packaged-forge-runtime.sh`, then run `packagedForgeRuntimeTest`
with `-PpackagedForgeRuntimeRoot=<server-directory>`. On Windows, set
`-PpythonExecutable=<python.exe>` if
`python3` is not available. The test destination must not already exist;
keep a failed run before cleaning the build directory.

The packaged tests check that OreSpawn keeps existing overrides when importing
old configs, and that moving an OreSpawn 1 world without its converted config
does not duplicate its ore rules. They also exercise the vanilla-ore switch,
molten metal contact and the Modern Metals add-on.

The shared **Release Base Metals** workflow lives on the repository's default
branch. Version `2.5.2.111021` selects `master-1.11.2`; it requires successful
CI for the exact source commit and explicit publication confirmation. It builds
one candidate, then publishes those same files to MMD Maven, CurseForge and
GitHub, in that order. A tag alone only validates the candidate.

The four release secrets are `CURSEFORGE_TOKEN`, `MAVEN_UPLOAD_URL`,
`MAVEN_UPLOAD_USERNAME` and `MAVEN_UPLOAD_PASSWORD`. OreSpawn is a required
CurseForge relation; MMDLib is bundled in this branch, not a separate requirement.
Maven publication rejects missing credentials and non-HTTPS upload destinations.
`-PpreparedReleaseDir=<bundle>` checks the three jars, checksums and changelog
without rebuilding the release. Do not run a publish task just to test a build.

## Save and add-on compatibility

Use a disposable world copy when testing an upgrade. The packaged tests use the
previous public 1.11.2 Base Metals and OreSpawn 3 releases as their starting
point. The verification jars are under `build/test-mods`; don't install them in
a regular instance.

For add-on development, use
`zone.moddev.mc.basemetals:BaseMetals:2.5.2.111021`. The public material and
recipe types under `com.mcmoddev.lib` are still bundled in Base Metals.
