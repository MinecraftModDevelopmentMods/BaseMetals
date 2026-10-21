# Building Base Metals 1.10.2

Use the inner `BaseMetals` directory as the project root. The Gradle wrapper is
part of the project; there is no need to install Gradle separately.

## Java versions

- Java 17 runs Gradle 9.6.1 and ForgeGradle 7.0.34.
- Java 25 runs Forge's Minecraft Mavenizer during Minecraft setup.
- Java 8 compiles the mod and runs Minecraft 1.10.2.

Ordinary development accepts compatible Java 8 installations. Release checks use
Temurin 8u502-b07 and Gradle on Temurin 17.0.1+12. Do not change Eclipse's own
runtime to Java 8: its Gradle and Minecraft runtimes are separate settings.

The compile and runtime target is Forge 12.18.3.2511 with stable-29 mappings.
OreSpawn 4.1.2.110021 is pinned through Curse Maven and verified against its
published SHA-256. Saved registry IDs and recipes have not changed.

## Eclipse

Import the project as an existing Gradle project, select the wrapper and use
Java 17 for Gradle. Run the `prepareEclipse` task once, then refresh the project.
Choose the generated `runClient` or `runServer` launch, using JavaSE-8.
Gradle synchronization regenerates those launches when needed.

Eclipse copies resources from Gradle's processed output, so a clean Eclipse build
cannot replace the mod version with a `${version}` placeholder. After changing
textures or other resources, refresh Gradle or run `prepareEclipse` again.

The launch tasks merge production classes and processed resources, so Minecraft
sees one mod root. Ordinary launches include the published OreSpawn 4.1.2.110021
release. Other integration APIs, JUnit and the migration probes
are excluded. Keep personal worlds outside the development `run` directory.

Remapped copies of signed legacy dependencies can report a fingerprint warning
in development. Their original downloads are still checksum-verified; the
transformed copies cannot retain a valid signature for the original bytes.

## Build checks

```text
gradlew clean check build javadoc verifyReleaseArtifacts writeReleaseChecksums prepareEclipse
```

The installable jar is `build/libs/BaseMetals-2.5.1.110021.jar`. Sources,
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

The target branch is `master-1.10.2`, matching OreSpawn's naming. CI runs on that
branch and feature branches, with separate CodeQL and wrapper checks. It tests
a cold Forge setup, an offline rebuild, Java 8 bytecode, Eclipse isolation and
byte-identical release jars. Public Cyano and MMD releases are used to create
temporary upgrade worlds; personal saves are never needed by CI.

To run the packaged suite locally, prepare a disposable Forge server with
`gradle/prepare-packaged-forge-runtime.sh`, then run `packagedForgeRuntimeTest`
with `-PpackagedForgeRuntimeRoot=<server-directory>`. Python 3 needs `nbtlib`
2.0.4 and NumPy 2.2.6. On Windows, set `-PpythonExecutable=<python.exe>` if
`python3` is not available. The test destination must not already exist;
keep a failed run before cleaning the build directory.

The shared **Release Base Metals** workflow lives on the repository's default
branch. Version `2.5.1.110021` selects `master-1.10.2`; it requires successful
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

Build the separate test mod with `migrationProbeJar`. It is written to
`build/test-mods`, never the release directory. The checks and their phased
first-load, reload and later-chunk procedure are described in
[the legacy test guide](../tools/validation/legacy110/README.md).

Always test a disposable world copy. Base Metal Golems checks use the unchanged
6.02.1 add-on with Extra Golems 6.02, including golems saved by Cyano Base Metals.
Removing the add-ons from a live world can remove their entities; the no-add-on
test therefore uses a separate copy without those saved entities.

The OreSpawn upgrade runner is `tools/validation/legacy110/run_orespawn_checks.py`.
It first saves a control world with the previous Base Metals and OreSpawn 3 jars,
then tests copies with the new jar and OreSpawn 4.1. It checks stock, custom and
disabled rules, OS1 imports, saved item data, reloads and chunks opened later.
Use `--help` for its input paths; its output must be a disposable directory.
Build the packaged probes with `packagedLaunchProbeJar packagedOreSpawnProbeJar`.
The ordinary probe jars use development mappings and are not interchangeable
with the packaged versions.
