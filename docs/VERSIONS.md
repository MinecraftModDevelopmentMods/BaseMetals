# Supported versions

Base Metals `3.0.1.114041` targets Minecraft `1.14.4`, Forge `28.2.26`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Builds and tests use the published OreSpawn
`4.1.0.114041` release (CurseForge project `245586`, file `9041431`, SHA-256
`7691C1AE7C6AC890A2CC785F3EEF54A1255D61E4CFCF5661D67CB1609CFC41C6`).
At runtime, Base Metals requires OreSpawn `[4.1.0.114041,5.0.0)` for Minecraft 1.14.4.
The configuration cog uses OreSpawn's public 4.1 client API.

Builds download this file through CurseMaven. CI verifies the download and
stages it in a temporary Maven repository. The schema-3 provider supports the
OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.0.1.114041`.
