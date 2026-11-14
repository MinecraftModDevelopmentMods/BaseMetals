package zone.moddev.mc.basemetals.material;

import static zone.moddev.mc.basemetals.material.MaterialDefinition.Kind.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Ordered catalogue used by registration, data generation, and validation. */
public final class MaterialCatalogue {
    public static final List<MaterialDefinition> ALL;
    public static final Map<String, MaterialDefinition> BY_NAME;

    static {
        List<MaterialDefinition> materials = new ArrayList<MaterialDefinition>();
        materials.add(material("adamantine", RARE_ORE, 12, 100, 0, 0x53393F, true, 4, 3, 3));
        materials.add(material("antimony", ORE, 1, 1, 1, 0xD8E3DE, true, 0, 0, 1));
        materials.add(material("aquarium", RARE_ALLOY, 4, 10, 15, 0x0000FF, false, 1, 1, 1));
        materials.add(material("bismuth", ORE, 1, 1, 1, 0xDDD7CB, true, 0, 0, 1));
        materials.add(material("brass", ALLOY, 3.5, 3, 9, 0xFFE374, false, 1, 1, 1));
        materials.add(material("bronze", ALLOY, 8, 4, 4.5, 0xF7A54F, false, 2, 2, 1));
        materials.add(material("coldiron", RARE_ORE, 7, 7, 7, 0xC7CEF0, true, 2, 2, 2));
        materials.add(material("copper", ORE, 4, 4, 5, 0xFF9F78, false, 1, 1, 1));
        materials.add(material("cupronickel", ALLOY, 6, 6, 6, 0xC8AB6F, false, 1, 1, 1));
        materials.add(material("electrum", ALLOY, 5, 4, 10, 0xFFF2B3, false, 0, 0, 1));
        materials.add(material("invar", ALLOY, 9, 10, 3, 0xD2CDB8, false, 2, 2, 1));
        materials.add(material("lead", ORE, 1, 1, 1, 0x7B7B7B, false, 0, 0, 1));
        materials.add(material("mercury", MERCURY, 1, 1, 1, 0xE2E2E2, false, 0, 0, 1));
        materials.add(material("mithril", RARE_ALLOY, 9, 9, 9, 0xF4FFFF, false, 3, 2, 1));
        materials.add(material("nickel", ORE, 4, 4, 7, 0xEEFFEB, true, 1, 1, 1));
        materials.add(material("pewter", ALLOY, 1, 1, 1, 0x92969F, false, 0, 0, 1));
        materials.add(material("platinum", RARE_ORE, 3, 5, 15, 0xF2FFFF, true, 0, 0, 1));
        materials.add(material("silver", ORE, 5, 4, 6, 0xFFFFFF, false, 0, 0, 1));
        materials.add(material("starsteel", RARE_ORE, 10, 25, 12, 0x53393F, true, 3, 3, 3));
        materials.add(material("steel", ALLOY, 8, 15, 2, 0xD5E3E5, false, 2, 2, 1));
        materials.add(material("tin", ORE, 3, 1, 2, 0xFFF7EE, false, 0, 0, 1));
        materials.add(material("zinc", ORE, 1, 1, 1, 0xBCBCBC, true, 0, 0, 1));
        ALL = Collections.unmodifiableList(materials);

        Map<String, MaterialDefinition> byName = new LinkedHashMap<String, MaterialDefinition>();
        for (MaterialDefinition material : ALL) {
            if (byName.put(material.name(), material) != null) {
                throw new IllegalStateException("Duplicate material " + material.name());
            }
        }
        BY_NAME = Collections.unmodifiableMap(byName);
    }

    private MaterialCatalogue() {}

    public static MaterialDefinition get(String name) {
        MaterialDefinition material = BY_NAME.get(name);
        if (material == null) throw new IllegalArgumentException("Unknown Base Metals material: " + name);
        return material;
    }

    private static MaterialDefinition material(String name, MaterialDefinition.Kind kind,
            double hardness, double strength, double magic, int colour, boolean processing, int toolLevel, int blockLevel, int oreLevel) {
        return new MaterialDefinition(name, kind, hardness, strength, magic, colour, processing, toolLevel, blockLevel, oreLevel);
    }
}
