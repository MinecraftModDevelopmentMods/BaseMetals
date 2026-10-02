# Supported versions

Base Metals `3.0.1.113021` targets Minecraft `1.13.2`, Forge `25.0.223`, and
Java 8. The Gradle build itself runs on Java `17.0.1+12` and uses ForgeGradle
`7.0.34` with Gradle `9.6.1`; those build-time runtimes do not change the Java
8 requirement of the released mod.

Development, CI, and release qualification use the exact public OreSpawn
`4.1.0.113021` release (CurseForge project `245586`, file `9035885`, SHA-256
`66C9CC5F8BE8F08F3DD5FA26EBEA79B52972B2FB86CB4DADBEEBC29AC01D3435`).
The runtime contract is OreSpawn `[4.0.16.113021,5.0.0)` for Minecraft 1.13.2.

The published dependency is resolved through CurseMaven. CI stages the same
checksum-verified file in a temporary Maven mirror. Local candidate repositories
are no longer used. The schema-3 provider remains compatible with the supported
OreSpawn 4 versions, so the runtime dependency range is unchanged.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.0.1.113021`.
