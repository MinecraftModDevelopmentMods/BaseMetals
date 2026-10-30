# Supported versions

Base Metals `3.1.1.2603001` targets Minecraft 26.3, Forge 66.0.0 and Java 25.
Forge 66.1.0 is the additional compatibility target.
OreSpawn `4.1.0.2603001` is the exact build and test dependency. Clients and
servers need OreSpawn `[4.1.0.2603001,5.0.0)`.

The pinned CurseForge artifact is project 245586, file 9333781:
`curse.maven:mmd-orespawn-245586:9333781`.
Its SHA-256 is
`A7FCDD626B9044091BFDB7277DA3169A5307EB5F97FCF88741A878401DCF1AEA`.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.1.2603001`.

The provider remains schema 3, revision 1. OreSpawn 4.1 is needed for the
configuration-screen API; the provider's eleven rule IDs and 1.18 distributions
have not changed.

The data pack uses format `121.0`. Forge wraps client mod resources with the
native resource-pack format `97.1`. Pack metadata keeps the full major and
minor version numbers.
