# Supported versions

Base Metals `3.0.1.115021` targets Minecraft `1.15.2`, Forge `31.2.57`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Builds and tests use the published [OreSpawn `4.1.0.115021` release](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9073591),
CurseForge project `245586`, file `9073591`, with
SHA-256 `F7C9A110E834EC3F73D55C20E8F52EEA1CF48C4E3ACCFBE8FCEFB60E78ABE387`.
At runtime, Base Metals requires OreSpawn `[4.1.0.115021,5.0.0)` for Minecraft 1.15.2.
The configuration cog uses OreSpawn's public 4.1 client API.

The exact build dependency is `curse.maven:mmd-orespawn-245586:9073591`.
CI stages the same checksum-verified file in a temporary Maven repository.
The schema-3 provider supports the OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.0.1.115021`.
