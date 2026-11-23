# Releasing Base Metals for Minecraft 1.14.4

Use Java 17.0.1+12 for Gradle, Temurin 8u502-b07 for compilation and Java 25
for Mavenizer. Run `check build javadoc verifyReleaseArtifacts
writeReleaseChecksums`, then regenerate and verify the Eclipse launches.
Keep packaged gameplay tests local and finish manual testing before delivery.

Push the fork branch and open a PR to MMD's `master-1.14.4`. Wait for its checks
before merging. Compare all three jars from the merged CI bundle with the
locally qualified files, including their SHA-256 hashes.

Start the default-branch `deploy-release.yml` dispatcher with the four-part
release version and the CurseForge release level. It derives the target branch
from the version, checks the metadata and prior CI, builds a sealed bundle,
then publishes those same files to MMD Maven, CurseForge and GitHub. Live
publication requires `confirm_live_publication=true`.

Verify public downloads from all three services against the qualified hashes.
Check CurseForge's Minecraft version, loader and OreSpawn dependency as well.
Keep failed receipts, and retry only failed publishing jobs so an accepted
upload is not sent twice. Once Base Metals is available, update companion
mods to its released version and exact jar hash before publishing them.
