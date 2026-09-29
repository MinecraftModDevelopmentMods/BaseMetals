# Base Metals build scripts

The root `build.gradle` contains the parts needed to compile and package Base
Metals: project identity, Java and Forge setup, development runs, resource
processing, and archive creation.

The supporting scripts keep the less frequently changed release machinery out
of that main path:

- `dependencies.gradle` owns exact third-party coordinates and the matching
  development, fixture, and verification configurations.
- `verification/support.gradle` defines shared toolchain, dependency, JSON,
  and OreSpawn provider checks.
- `verification/packaged-forge.gradle` prepares the common clean Forge runtime
  and its reusable probes.
- `verification/packaged-forge-dependencies.gradle` checks required OreSpawn
  behavior and preservation of an existing override.
- `verification/packaged-forge-migration.gradle` covers OreSpawn 3 migration.
- `verification/packaged-forge-cofh.gradle` covers the optional CoFH World
  coexistence profile.
- `verification/packaged-forge-tinkers.gradle` covers Tinkers, TAIGA,
  Construct's Armory, and content-mode combinations.
- `release/artifacts.gradle` audits release jars and writes checksums.
- `release/publishing.gradle` defines guarded Maven publication.
- `ide/eclipse.gradle` generates and verifies isolated Eclipse launches.
- `verification/workflows.gradle` checks the pinned GitHub Actions contracts.

Shared values are exposed through small immutable maps. This lets an applied
script state which part of the build contract it uses instead of depending on
variables that happen to exist in another script's scope.

When adding a task, put it beside the workflow it supports. Keep task names
stable because CI and the release dispatcher call them directly.
