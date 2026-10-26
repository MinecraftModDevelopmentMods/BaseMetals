# Supported versions

Base Metals `3.1.1.121111` targets Minecraft 1.21.11, Forge 61.1.0 and Java 21.
Packaged client, server and save-upgrade checks also pass on Forge 61.2.4.
OreSpawn `4.1.0.121111` is the exact build and test dependency. Clients and
servers need OreSpawn `[4.1.0.121111,5.0.0)`.

The pinned CurseForge artifact is project 245586, file 9268464:
`curse.maven:mmd-orespawn-245586:9268464`.
Its SHA-256 is
`DE39929057288CEA110196C9E9D708E129B80BC360A2E53976337FFA6A84A67B`.

Release artifacts use Maven coordinate
`zone.moddev.mc.basemetals:BaseMetals:3.1.1.121111`.

The provider remains schema 3, revision 1. OreSpawn 4.1 is needed for the
configuration-screen API; the provider's eleven rule IDs and 1.18 distributions
have not changed.

The data pack uses format `94.1`. Forge wraps client mod resources with the
native resource-pack format `75.0`. The resource metadata retains the minor
version rather than rounding the data format down to `94`.
