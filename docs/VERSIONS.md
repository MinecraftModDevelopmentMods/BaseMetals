# Supported versions

Base Metals `3.1.1.2601021` targets Minecraft 26.1.2, Forge 64.0.9 and Java 25.
Forge 64.1.5 is the additional compatibility target.
OreSpawn `4.1.0.2601021` is the exact build and test dependency. Clients and
servers need OreSpawn `[4.1.0.2601021,5.0.0)`.

The pinned CurseForge artifact is project 245586, file 9299079:
`curse.maven:mmd-orespawn-245586:9299079`.
Its SHA-256 is
`6D5CE2017C3314968083D140309E2F1A54BC239AC65C48944C995E9ECF937948`.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.1.2601021`.

The provider remains schema 3, revision 1. OreSpawn 4.1 is needed for the
configuration-screen API; the provider's eleven rule IDs and 1.18 distributions
have not changed.

The data pack uses format `101.1`. Forge wraps client mod resources with the
native resource-pack format `84.0`. The resource metadata retains the minor
version rather than rounding the data format down to `101`.
