# Supported versions

Base Metals `3.0.1.113021` targets Minecraft `1.13.2`, Forge `25.0.223`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Builds and tests use the published OreSpawn
`4.1.0.113021` release (CurseForge project `245586`, file `9035885`, SHA-256
`66C9CC5F8BE8F08F3DD5FA26EBEA79B52972B2FB86CB4DADBEEBC29AC01D3435`).
At runtime, Base Metals requires OreSpawn `[4.1.0.113021,5.0.0)` for Minecraft 1.13.2.
The configuration cog uses OreSpawn's public 4.1 client API.

Builds download this file through CurseMaven. CI verifies the download and
stages it in a temporary Maven repository. The schema-3 provider supports the
OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.0.1.113021`.
