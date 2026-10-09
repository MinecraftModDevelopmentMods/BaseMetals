# Supported versions

Base Metals `3.1.0.117011` targets Minecraft `1.17.1`, Forge `37.1.1`, and
Java 16. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
16 requirement of the released mod. Release builds use Temurin `16.0.2+7`.
The ForgeGradle renamer also needs Java 8 as a build-time utility; it is not the
game runtime.

Builds and tests use the published
[OreSpawn `4.1.0.117011` release](https://www.curseforge.com/minecraft/mc-mods/mmd-orespawn/files/9088186) with
SHA-256 `1BDEDCBB179CB5A3B169E5C8A6B301E3888AE15B94108B0AEC627D705CFAD833`.
At runtime, Base Metals requires OreSpawn `[4.1.0.117011,5.0.0)` for Minecraft 1.17.1.
The configuration cog uses OreSpawn's public 4.1 client API.

The development coordinate is `curse.maven:mmd-orespawn-245586:9088186`:
CurseForge project `245586`, file `9088186`. CI downloads and checks the same
file before staging it in a temporary Maven repository.
The schema-3 provider supports the OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.0.117011`.
