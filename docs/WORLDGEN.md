# OreSpawn world generation

Base Metals requires OreSpawn `[4.1.0.121011,5.0.0)` and packages provider schema 3,
revision 1 at `data/basemetals/orespawn/provider.json`. Builds and tests use the
published OreSpawn `4.1.0.121011` release. The provider is checked against the
included 4.1.0 schema.

The provider declares ores only: it has no rocks, geomes, biome rules, terrain
dimensions, formations, fluid deposits, vanilla suppression, or strata
defaults. Every enabled placement uses the `vein` pattern, quantity `4..11`,
spread `8`, vertical spread `4`, node size `4`, length `16`, air-exposure
discard `0`, and `retrogen:false`.

| Ore | Selector | Y | Distribution | Attempts/chunk | Hosts |
| --- | --- | ---: | --- | ---: | --- |
| Cold iron | Nether | 0..127 | uniform | 5 | `forge:netherrack` |
| Adamantine | Nether | 0..127 | uniform | 2 | `forge:netherrack` |
| Starsteel | End | 0..254 | uniform | 5 | `forge:end_stones` |
| Tin | all except Nether/End | -64..128 | triangle | 10 | vanilla ore-replaceable tags and all rock families |
| Lead | all except Nether/End | -64..64 | triangle | 5 | same |
| Zinc | all except Nether/End | -64..96 | triangle | 5 | same |
| Silver | all except Nether/End | -64..32 | bottom triangle | 4 | same |
| Mercury | all except Nether/End | -64..32 | bottom triangle | 3 | same |
| Nickel | all except Nether/End | -32..96 | triangle | 1 | same |
| Platinum | all except Nether/End | -64..16 | bottom triangle | 0.125 | same |
| Base Metals copper | disabled | -16..112 | triangle | 10 | same |

Vanilla copper remains under vanilla generation. Base Metals copper, antimony,
and bismuth ore blocks remain registered for old worlds and data packs, but do
not generate on a fresh installation. Packs may enable the copper rule or add
new rules through OreSpawn's normal provider overrides.

The ordinary-dimension selector deliberately includes modded dimensions while
excluding the Nether and End. Mineralogy rock families are listed as additional
hosts, so one OreSpawn rule set works with either vanilla stone or Mineralogy
strata without duplicate generation.
