# OreSpawn world generation

Base Metals requires OreSpawn `[4.1.0.117011,5.0.0)` and packages provider
schema 3, revision 1 at `data/basemetals/orespawn/provider.json`. Builds and
tests use the published OreSpawn `4.1.0.117011` release, CurseForge file `9088186`.

The provider declares ores only: it has no rocks, geomes, biome rules, terrain
dimensions, formations, fluid deposits, vanilla suppression, or strata
defaults. Every placement uses the `vein` pattern, quantity `4..11`, spread
`8`, vertical spread `4`, node size `4`, length `16`, air-exposure discard `0`,
and `retrogen:false`.

| Ore | Selector | Inclusive Y | Distribution | Attempts/chunk | Hosts |
| --- | --- | ---: | --- | ---: | --- |
| Cold Iron | Nether | 0..127 | uniform | 5 | Netherrack |
| Adamantine | Nether | 0..127 | uniform | 2 | Netherrack |
| Starsteel | End | 0..254 | uniform | 5 | End stone |
| Copper | all except Nether/End | 0..95 | uniform | 10 | Stone and all OreSpawn rock families |
| Silver | all except Nether/End | 0..31 | uniform | 4 | same |
| Tin | all except Nether/End | 0..127 | uniform | 10 | same |
| Lead | all except Nether/End | 0..63 | uniform | 5 | same |
| Zinc | all except Nether/End | 0..95 | uniform | 5 | same |
| Mercury | all except Nether/End | 0..31 | uniform | 3 | same |
| Nickel | all except Nether/End | 32..95 | uniform | 1 | same |
| Platinum | all except Nether/End | 1..31 | uniform | 0.125 | same |

Minecraft 1.17 added vanilla copper. Base Metals copper is disabled by default
in new profiles, leaving vanilla copper enabled. Existing OreSpawn profiles and
explicit overrides keep their chosen copper setting; this update does not change
them. The copper row above describes the retained rule if you enable it.
Antimony and Bismuth ores are available in creative mode and old
saves, but do not generate unless you add rules for them in OreSpawn.

The ordinary-dimension selector includes modded dimensions while excluding the
Nether and End. Mineralogy rock families are additional hosts, allowing the
same OreSpawn rules to operate in vanilla stone or Mineralogy strata without a
second generator.
