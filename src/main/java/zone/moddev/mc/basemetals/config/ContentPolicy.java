package zone.moddev.mc.basemetals.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

/** Limits how items are obtained without removing them from existing saves. */
public final class ContentPolicy {
    private static final Set<String> MATERIALS = names("adamantine", "antimony", "aquarium", "bismuth",
            "brass", "bronze", "charcoal", "coal", "coldiron", "copper", "cupronickel", "diamond",
            "electrum", "emerald", "ender", "gold", "invar", "iron", "lapis", "lead", "mercury",
            "mithril", "nickel", "obsidian", "pewter", "platinum", "prismarine", "quartz", "redstone",
            "silver", "starsteel", "steel", "stone", "tin", "wood", "zinc");
    private static final Set<String> FULL_TOOLS = names("adamantine", "aquarium", "bronze", "coldiron",
            "copper", "cupronickel", "invar", "mithril", "nickel", "starsteel", "steel", "iron");
    private static final Set<String> PROTECTIVE = names("adamantine", "aquarium", "brass", "bronze",
            "coldiron", "copper", "cupronickel", "electrum", "invar", "mithril", "nickel", "platinum",
            "silver", "starsteel", "steel", "iron", "gold", "diamond");
    private static final Set<String> GEARS = names("adamantine", "aquarium", "brass", "bronze", "coldiron",
            "copper", "cupronickel", "invar", "mithril", "nickel", "starsteel", "steel", "iron", "gold", "wood");
    private static final Set<String> EXTRA_AMMUNITION = names("silver", "obsidian", "diamond");
    private static final Set<String> ANVILS = names("stone", "steel", "adamantine");
    private static final Set<MaterialForm> TOOLS = EnumSet.of(MaterialForm.AXE, MaterialForm.CRACKHAMMER,
            MaterialForm.HOE, MaterialForm.PICKAXE, MaterialForm.SCYTHE, MaterialForm.SHEARS,
            MaterialForm.SHOVEL, MaterialForm.SWORD);
    private static final Set<MaterialForm> ARMOR = EnumSet.of(MaterialForm.BOOTS, MaterialForm.CHESTPLATE,
            MaterialForm.HELMET, MaterialForm.LEGGINGS, MaterialForm.HORSE_ARMOR, MaterialForm.SHIELD);
    private static final ContentPolicy HIGH = new ContentPolicy(ContentMode.HIGH_FANTASY);
    private static final ContentPolicy LOW = new ContentPolicy(ContentMode.LOW_FANTASY);

    private final ContentMode mode;

    private ContentPolicy(ContentMode mode) {
        this.mode = mode;
    }

    public static ContentPolicy forMode(ContentMode mode) {
        return mode == ContentMode.LOW_FANTASY ? LOW : HIGH;
    }

    public static ContentPolicy active() {
        return forMode(BaseMetalsConfig.activeMode());
    }

    public boolean allows(String itemId) {
        if (mode == ContentMode.HIGH_FANTASY) return true;
        if (itemId == null || !itemId.startsWith("basemetals:")) return true;
        String path = itemId.substring("basemetals:".length());
        String ordinary = path.startsWith("double_") ? path.substring(7)
                : path.startsWith("flowing_") ? path.substring(8) : path;

        for (String material : MATERIALS) {
            if (ordinary.equals(material) || ordinary.startsWith(material + "_")) {
                return allows(material, ordinary.equals(material) ? MaterialForm.FLUID : MaterialForm.fromPath(path));
            }
        }

        return true;
    }

    public boolean allows(String material, MaterialForm form) {
        if (mode == ContentMode.HIGH_FANTASY) return true;
        if (form == MaterialForm.BOW || form == MaterialForm.CROSSBOW || form == MaterialForm.FISHING_ROD) return false;

        if (TOOLS.contains(form)) {
            if (FULL_TOOLS.contains(material)) return true;
            if ("silver".equals(material)) return form == MaterialForm.SWORD;
            if ("obsidian".equals(material)) return form == MaterialForm.AXE || form == MaterialForm.SWORD || form == MaterialForm.SCYTHE;
            if ("diamond".equals(material)) return form == MaterialForm.CRACKHAMMER || form == MaterialForm.SCYTHE || form == MaterialForm.SHEARS;
            if ("stone".equals(material)) return form == MaterialForm.CRACKHAMMER || form == MaterialForm.SCYTHE;
            return "wood".equals(material) && form == MaterialForm.CRACKHAMMER;
        }

        if (ARMOR.contains(form)) return PROTECTIVE.contains(material);
        if (form == MaterialForm.ARROW || form == MaterialForm.BOLT) {
            return FULL_TOOLS.contains(material) || EXTRA_AMMUNITION.contains(material);
        }
        if (form == MaterialForm.GEAR) return GEARS.contains(material);
        if (form == MaterialForm.ANVIL) return ANVILS.contains(material);
        if ("mercury".equals(material)) {
            return form == MaterialForm.ORE || form == MaterialForm.POWDER || form == MaterialForm.SMALLPOWDER
                    || form == MaterialForm.FLUID || form == MaterialForm.INGOT;
        }

        return true;
    }

    private static Set<String> names(String... values) {
        return Collections.unmodifiableSet(new HashSet<>(Arrays.asList(values)));
    }
}
