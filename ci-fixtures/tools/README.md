# Forge 1.20.1 Mavenizer compatibility fixture

`minecraft-mavenizer-0.5.21-orespawn-compat.jar` is a build-only derivative of
MinecraftForge MinecraftMavenizer `0.5.21`, source commit
`6968241ce7a0a902cdc1c534b976e8373a423091`.

Forge 47's decompiler emits eight duplicate record accessors in Holder.Direct,
OptionInstance and MemoryCondition. The target-specific rules in
`minecraft-source-compatibility.json` remove only those accessors; Java
generates the same public methods automatically. Missing or ambiguous source
matches stop the build. The derivative also honors
`ORESPAWN_MAVENIZER_OFFLINE=true` by adding Mavenizer's native `--offline`
argument. This closes ForgeGradle 7's failure to propagate Gradle offline mode
to the subprocess; it does not alter normal online builds.

`minecraft-mavenizer-0.5.21-orespawn-compat.patch` contains OreSpawn's source
changes. Apply it to the pinned upstream commit with `git am` to rebuild the
fixture using Temurin 25.0.3+9 and the upstream Gradle 9.1.0 wrapper.

The fixture is used only while ForgeGradle prepares the development dependency.
It is checksum-verified by the build and excluded from every Base Metals artifact.
MinecraftMavenizer remains licensed LGPL-2.1-only; see `LICENSE-MAVENIZER.txt`.

SHA-256:
`416DCA4797BC55826F0DDCF03FDC405F1990FC15C2133FA27A1F143BF7976177`
