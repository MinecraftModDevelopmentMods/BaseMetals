package zone.moddev.mc.basemetals.compat;

import java.util.Map;

/** Uses vanilla copper for new items while keeping the old Base Metals IDs valid. */
public final class VanillaCopper {
    private static final Map<String, String> EQUIVALENTS = Map.ofEntries(
            Map.entry("basemetals:copper_ingot", "minecraft:copper_ingot"),
            Map.entry("basemetals:copper_nugget", "minecraft:copper_nugget"),
            Map.entry("basemetals:copper_block", "minecraft:copper_block"),
            Map.entry("basemetals:copper_door", "minecraft:copper_door"),
            Map.entry("basemetals:copper_trapdoor", "minecraft:copper_trapdoor"),
            Map.entry("basemetals:copper_slab", "minecraft:cut_copper_slab"),
            Map.entry("basemetals:copper_stairs", "minecraft:cut_copper_stairs"),
            Map.entry("basemetals:copper_bars", "minecraft:copper_bars"),
            Map.entry("basemetals:copper_pickaxe", "minecraft:copper_pickaxe"),
            Map.entry("basemetals:copper_axe", "minecraft:copper_axe"),
            Map.entry("basemetals:copper_shovel", "minecraft:copper_shovel"),
            Map.entry("basemetals:copper_hoe", "minecraft:copper_hoe"),
            Map.entry("basemetals:copper_sword", "minecraft:copper_sword"),
            Map.entry("basemetals:copper_helmet", "minecraft:copper_helmet"),
            Map.entry("basemetals:copper_chestplate", "minecraft:copper_chestplate"),
            Map.entry("basemetals:copper_leggings", "minecraft:copper_leggings"),
            Map.entry("basemetals:copper_boots", "minecraft:copper_boots"),
            Map.entry("basemetals:copper_horse_armor", "minecraft:copper_horse_armor"));

    private static final Map<String, String> INGREDIENT_TAGS = Map.ofEntries(
            Map.entry("basemetals:copper_ingot", "forge:ingots/copper"),
            Map.entry("basemetals:copper_nugget", "forge:nuggets/copper"),
            Map.entry("basemetals:copper_block", "forge:storage_blocks/copper"),
            Map.entry("basemetals:copper_door", "forge:doors/copper"),
            Map.entry("basemetals:copper_trapdoor", "forge:trapdoors/copper"),
            Map.entry("basemetals:copper_slab", "forge:slabs/copper"),
            Map.entry("basemetals:copper_stairs", "forge:stairs/copper"),
            Map.entry("basemetals:copper_bars", "forge:bars/copper"),
            Map.entry("basemetals:copper_pickaxe", "forge:tools/pickaxes/copper"),
            Map.entry("basemetals:copper_axe", "forge:tools/axes/copper"),
            Map.entry("basemetals:copper_shovel", "forge:tools/shovels/copper"),
            Map.entry("basemetals:copper_hoe", "forge:tools/hoes/copper"),
            Map.entry("basemetals:copper_sword", "forge:tools/swords/copper"),
            Map.entry("basemetals:copper_helmet", "forge:armors/helmets/copper"),
            Map.entry("basemetals:copper_chestplate", "forge:armors/chestplates/copper"),
            Map.entry("basemetals:copper_leggings", "forge:armors/leggings/copper"),
            Map.entry("basemetals:copper_boots", "forge:armors/boots/copper"),
            Map.entry("basemetals:copper_horse_armor", "forge:armors/horse/copper"));

    private VanillaCopper() {}

    public static String output(String itemId) {
        return EQUIVALENTS.getOrDefault(itemId, itemId);
    }

    public static String ingredientTag(String itemId) {
        return INGREDIENT_TAGS.get(itemId);
    }

    public static Map<String, String> equivalents() {
        return EQUIVALENTS;
    }
}
