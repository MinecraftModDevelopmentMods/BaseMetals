package zone.moddev.mc.basemetals.material;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.lib.MMDLib;
import zone.moddev.mc.lib.common.types.MMDMaterial;

/**
 * Base Metals' materials, read from the MMDLib definitions in {@code mmdlib/material/}.
 * Available once MMDLib has loaded definitions, before registry events.
 *
 * <p>The vanilla extensions (iron, diamond, wood, ...) are also Base Metals definitions,
 * but they extend existing vanilla materials and are not part of this catalogue; see
 * {@link #isVanillaExtension}.
 */
public final class MaterialCatalogue {
    private static volatile List<MaterialDefinition> all;
    private static volatile Map<String, MaterialDefinition> byName;

    private MaterialCatalogue() {}

    public static List<MaterialDefinition> all() {
        load();
        return all;
    }

    public static Map<String, MaterialDefinition> byName() {
        load();
        return byName;
    }

    public static MaterialDefinition get(String name) {
        MaterialDefinition material = byName().get(name);
        if (material == null) {
            throw new IllegalArgumentException("Unknown Base Metals material: " + name);
        }
        return material;
    }

    /** The Base Metals material behind an MMDLib material, if it is one of ours. */
    public static Optional<MaterialDefinition> of(MMDMaterial material) {
        return material.name()
                .filter(id -> id.getNamespace().equals(BaseMetals.MOD_ID))
                .map(id -> byName().get(id.getPath()));
    }

    /** A Base Metals extension of a vanilla material, whose type is not a Base Metals kind. */
    public static boolean isVanillaExtension(MMDMaterial material) {
        return material.name().map(id -> id.getNamespace().equals(BaseMetals.MOD_ID)).orElse(false)
                && MaterialDefinition.Kind.of(material.properties().type()).isEmpty();
    }

    private static synchronized void load() {
        if (all != null) return;
        Map<String, MaterialDefinition> materials = new LinkedHashMap<>();
        // loadedMaterials() is sorted by ID, which keeps the historical alphabetical order.
        MMDLib.loadedMaterials().forEach((id, result) -> {
            if (id.getNamespace().equals(BaseMetals.MOD_ID) && !isVanillaExtension(result.material())) {
                materials.put(id.getPath(), MaterialDefinition.of(result.material()));
            }
        });
        if (materials.isEmpty()) {
            throw new IllegalStateException("Base Metals materials were requested before MMDLib loaded them");
        }
        byName = Map.copyOf(materials);
        all = List.copyOf(materials.values());
    }
}
