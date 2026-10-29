# Supported versions

Base Metals `3.1.1.2602001` targets Minecraft 26.2, Forge 65.1.0 and Java 25.
Forge 65.1.5 is the additional compatibility target.
OreSpawn `4.1.0.2602001` is the exact build and test dependency. Clients and
servers need OreSpawn `[4.1.0.2602001,5.0.0)`.

The pinned CurseForge artifact is project 245586, file 9332183:
`curse.maven:mmd-orespawn-245586:9332183`.
Its SHA-256 is
`F74785025898DF059E0F00F6ADA53364E07ECBBC306807ED7CD55350609E2547`.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.1.2602001`.

The provider remains schema 3, revision 1. OreSpawn 4.1 is needed for the
configuration-screen API; the provider's eleven rule IDs and 1.18 distributions
have not changed.

The data pack uses format `107.1`. Forge wraps client mod resources with the
native resource-pack format `88.0`. The resource metadata retains the minor
version rather than rounding the data format down to `107`.
