# Supported versions

Base Metals `3.1.0.116051` targets Minecraft `1.16.5`, Forge `36.2.34`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Builds and tests use the published
[OreSpawn `4.1.0.116051` release](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9080646) with
SHA-256 `CDF59D83F191C5FBCD8921228DB577066A63A8FEB25CA79DB4EAEEA32D32DB04`.
At runtime, Base Metals requires OreSpawn `[4.1.0.116051,5.0.0)` for Minecraft 1.16.5.
The configuration cog uses OreSpawn's public 4.1 client API.

The development coordinate is `curse.maven:mmd-orespawn-245586:9080646`:
CurseForge project `245586`, file `9080646`. CI downloads and checks the same
file before staging it in a temporary Maven repository.
The schema-3 provider supports the OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.0.116051`.
