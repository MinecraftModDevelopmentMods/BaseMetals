# Archived Ender IO and Tinkers' Construct compatibility

Base Metals carried this compatibility over from the 1.20 Forge version. It was archived
for 4.0.0 (NeoForge 1.21.1) because Base Metals has no working integration with either mod
on 1.21.1. Nothing here is compiled, generated or packaged: `archive/` is outside every
source set and resource directory.

| Path | What it was | Where it plugged in |
|---|---|---|
| `datagen/GeneratedData-enderio-tinkers.java.fragment` | `writeEnderIoRecipes`, `writeTinkersRecipes`, `writeTinkersMaterials` and their helpers (`enderSag`, `tinkerMelting`, `tinkerCasting`, `tinkerBlockCasting`, `tinkerMaterialIngredient`, `harvestTier`) | Methods of `GeneratedData`. `run()` called `writeTinkersMaterials(cache)` after `writeRawProcessing(cache)`; `writeCompatibilityRecipes` called `writeTinkersRecipes(cache)` and `writeEnderIoRecipes(cache)` after `writeThermalRecipes(cache)`. |
| `java/TinkersCompat.java`, `java/TinkersContentPolicy.java` | Content-mode filtering of Base Metals' Tinkers material stats. Already commented out, and written against pre-1.21 APIs. | `src/main/java/zone/moddev/mc/basemetals/compat/` |
| `templates/neoforge.mods.toml.fragment` | Optional dependencies on `tconstruct` `[3.7,4)` and `enderio_machines` `(5,7)` | End of `src/main/templates/META-INF/neoforge.mods.toml` |
| `generated/` | The last generated output: 450 Tinkers recipes, 42 Ender IO SAG mill recipes, and Tinkers material definitions, stats, traits and render info (572 files) | `src/generated/resources/` at the same relative paths |

## Restoring

1. Check each mod's 1.21.1 recipe and material formats. These were written for the 1.20
   versions, and the dependency version ranges are 1.20.1-era.
2. Paste the fragment's methods back into `GeneratedData` and restore the three calls above.
3. Move the Java classes back and port them, and restore the `mods.toml` blocks with
   updated version ranges. An optional dependency installed outside its range stops the
   game from loading.
4. Run `./gradlew runData`. `generated/` is for reference only; datagen recreates the files.

Copper's own Tinkers material and recipes are not in `generated/`: copper had already
become a vanilla extension, which Tinkers handles itself.
