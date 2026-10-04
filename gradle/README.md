# Base Metals build scripts

The root `build.gradle` contains the parts needed to compile and package Base
Metals: project identity, Java and Forge setup, development runs, resource
processing, and archive creation.

The supporting scripts keep the less frequently changed release machinery out
of that main path:

- `dependencies.gradle` owns exact third-party coordinates and the matching
  development, fixture, and verification configurations.
- `data/recipe-advancements.gradle` derives recipe-book discovery advancements
  from the shipped recipe catalogue and preserves each recipe's conditions.
- `data/gameplay-advancements.gradle` restores the historical achievement tree.
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
- `verification/packaged-advancements.gradle` checks gameplay achievements,
  saved progress, disabled settings, and Low Fantasy in disposable worlds.
- `release/artifacts.gradle` audits release jars and writes checksums.
- `release/publishing.gradle` defines guarded Maven publication.
- `ide/eclipse.gradle` generates and verifies isolated Eclipse launches.
- `verification/workflows.gradle` checks the pinned GitHub Actions contracts.

The [release guide](../docs/releasing.md) explains how the default-branch
dispatcher selects and publishes a Minecraft target.

Scripts share settings through small immutable maps rather than relying on
variables from another script's scope.

When adding a task, put it beside the workflow it supports. Keep task names
stable because CI and the release dispatcher call them directly.
