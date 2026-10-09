package zone.moddev.mc.basemetals.config;

import java.util.Locale;

/** The blocks, parts and equipment that can be made from a material. */
public enum MaterialForm {
    ARROW, AXE, BLEND, BOOTS, BOLT, BOW, CHESTPLATE, CRACKHAMMER, CROSSBOW,
    FISHING_ROD, GEAR, HELMET, HOE, HORSE_ARMOR, INGOT, LEGGINGS, NUGGET,
    PICKAXE, POWDER, ROD, SCYTHE, SHEARS, SHIELD, SHOVEL, SMALLBLEND,
    SMALLPOWDER, SWORD, CASING, CLUMP, CRUSHED, CRUSHED_PURIFIED, CRYSTAL,
    DENSE_PLATE, DIRTY_POWDER, SHARD, ANVIL, BARS, BLOCK, BOOKSHELF, BUTTON,
    DOOR, DOUBLE_SLAB, FENCE, FENCE_GATE, FLOWER_POT, LADDER, LEVER, PLATE,
    PRESSURE_PLATE, SLAB, STAIRS, TRAPDOOR, TRIPWIRE_HOOK, WALL, ORE, RAW, FLUID, OTHER;

    public static MaterialForm fromPath(String path) {
        MaterialForm result = OTHER;
        int longest = 0;

        for (MaterialForm form : values()) {
            String suffix = form == DIRTY_POWDER ? "powder_dirty" : form.name().toLowerCase(Locale.ROOT);
            if (path.endsWith("_" + suffix) && suffix.length() > longest) {
                result = form;
                longest = suffix.length();
            }
        }

        if (path.startsWith("double_") && result == SLAB) return DOUBLE_SLAB;
        if (path.endsWith("_bucket") || path.startsWith("flowing_")) return FLUID;
        return result;
    }
}
