package zone.moddev.mc.basemetals.compat;

import java.util.Map;

/** Uses vanilla copper for new items while keeping the old Base Metals IDs valid. */
public final class VanillaCopper {
    private static final Map<String, String> EQUIVALENTS = Map.of(
            "basemetals:copper_ingot", "minecraft:copper_ingot",
            "basemetals:copper_block", "minecraft:copper_block",
            "basemetals:copper_door", "minecraft:copper_door",
            "basemetals:copper_trapdoor", "minecraft:copper_trapdoor",
            "basemetals:copper_slab", "minecraft:cut_copper_slab",
            "basemetals:copper_stairs", "minecraft:cut_copper_stairs");

    private static final Map<String, String> INGREDIENT_TAGS = Map.of(
            "basemetals:copper_ingot", "forge:ingots/copper",
            "basemetals:copper_block", "forge:storage_blocks/copper",
            "basemetals:copper_door", "forge:doors/copper",
            "basemetals:copper_trapdoor", "forge:trapdoors/copper",
            "basemetals:copper_slab", "forge:slabs/copper",
            "basemetals:copper_stairs", "forge:stairs/copper");

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
