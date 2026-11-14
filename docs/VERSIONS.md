# Supported versions

Base Metals `3.0.1.113021` targets Minecraft `1.13.2`, Forge `25.0.223`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Builds and tests use the published OreSpawn
`4.1.7.113021` release (CurseForge project `245586`, file `9611746`, SHA-256
`E65DCCA0623A20111C40779CB0FE2078E98C618FAA68F79C9A28E1E53C63957E`).
At runtime, Base Metals requires OreSpawn `[4.1.7.113021,5.0.0)` for Minecraft 1.13.2.
The configuration cog uses OreSpawn's public 4.1 client API.

Builds download this file through CurseMaven. CI verifies the download and
stages it in a temporary Maven repository. The schema-3 provider supports the
OreSpawn versions in the runtime range above.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.0.1.113021`.
