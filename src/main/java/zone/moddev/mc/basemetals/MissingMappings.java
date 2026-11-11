package zone.moddev.mc.basemetals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.lib.common.runtime.RuntimeContentRegistrar;

/** Registry-name aliases used when loading older Minecraft and Base Metals saves. */
public final class MissingMappings {
    private MissingMappings() {}

    /** Called once Base Metals content has been queued, before registry events. */
    public static void registerAliases() {
        // MMDLib 2.0 registers its own mmdlib: content; never alias over a real entry.
        Set<ResourceLocation> registered = new HashSet<>();
        RuntimeContentRegistrar.instances().forEach(registrar -> {
            registered.addAll(registrar.items().keySet());
            registered.addAll(registrar.blocks().keySet());
        });
        for (String namespace : List.of(BaseMetals.MOD_ID, "mmdlib")) {
            ModContent.blocksById().keySet().forEach(path -> alias(ModContent.BLOCKS, registered,
                    namespace, path, id(BaseMetals.MOD_ID, path)));
            ModContent.hiddenBlocks().forEach(path -> alias(ModContent.BLOCKS, registered,
                    namespace, legacyDoubleSlab(path), id(BaseMetals.MOD_ID, path)));
            ModContent.itemsById().keySet().forEach(path -> alias(ModContent.ITEMS, registered,
                    namespace, path, itemTargetId(path)));
            ModContent.itemsById().keySet().stream().filter(path -> path.endsWith("_door"))
                    .forEach(path -> alias(ModContent.ITEMS, registered, namespace, path + "_item",
                            id(BaseMetals.MOD_ID, path)));
            ModContent.fluids().keySet().forEach(path -> alias(ModContent.FLUIDS, registered,
                    namespace, path, id(BaseMetals.MOD_ID, path)));

            alias(ModContent.BLOCKS, registered, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury"));
            alias(ModContent.ITEMS, registered, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury_bucket"));
            alias(ModContent.ITEMS, registered, namespace, "carbon_powder", itemTargetId("carbon_powder"));
            alias(ModContent.ITEMS, registered, namespace, "iron_nugget", id("minecraft", "iron_nugget"));
            alias(ModContent.FLUIDS, registered, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury"));
        }
        for (String path : List.of("sign", "wall_sign")) {
            alias(ModContent.BLOCKS, registered, "minecraft", path, vanilla114Target(id("minecraft", path)));
        }
        for (String path : List.of("sign", "rose_red", "cactus_green", "dandelion_yellow")) {
            alias(ModContent.ITEMS, registered, "minecraft", path, vanilla114Target(id("minecraft", path)));
        }
    }

    /** Base Metals named compatibility slabs double_X_slab; MMDLib names them X_double_slab. */
    private static String legacyDoubleSlab(String path) {
        return "double_" + path.substring(0, path.length() - "_double_slab".length()) + "_slab";
    }

    private static ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private static void alias(DeferredRegister<?> registry, Set<ResourceLocation> registered,
            String namespace, String oldPath, ResourceLocation target) {
        ResourceLocation oldId = id(namespace, oldPath);
        // Identity aliases would introduce a resolution loop.
        if (!oldId.equals(target) && !registered.contains(oldId)) registry.addAlias(oldId, target);
    }

    public static ResourceLocation vanilla114Target(ResourceLocation oldName) {
        if (!"minecraft".equals(oldName.getNamespace())) return null;

        // Preserve registry aliases for vanilla names retired in 1.14.
        switch (oldName.getPath()) {
            case "sign": return ResourceLocation.fromNamespaceAndPath("minecraft", "oak_sign");
            case "wall_sign": return ResourceLocation.fromNamespaceAndPath("minecraft", "oak_wall_sign");
            case "rose_red": return ResourceLocation.fromNamespaceAndPath("minecraft", "red_dye");
            case "cactus_green": return ResourceLocation.fromNamespaceAndPath("minecraft", "green_dye");
            case "dandelion_yellow": return ResourceLocation.fromNamespaceAndPath("minecraft", "yellow_dye");
            default: return null;
        }
    }

    public static String blockTargetPath(String path) {
        if (path.startsWith("double_") && path.endsWith("_slab")) {
            return path.substring("double_".length(), path.length() - "_slab".length()) + "_double_slab";
        }
        return "liquid_mercury".equals(path) ? "mercury" : path;
    }

    public static String itemTargetPath(String path) {
        if ("liquid_mercury".equals(path)) return "mercury_bucket";
        return path.endsWith("_door_item") ? path.substring(0, path.length() - "_item".length()) : path;
    }

    public static ResourceLocation itemTargetId(String path) {
        // 1.12's carbon powder is the coal vanilla extension's powder.
        if ("carbon_powder".equals(path)) return ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "coal_powder");
        return "iron_nugget".equals(path)
                ? ResourceLocation.fromNamespaceAndPath("minecraft", "iron_nugget")
                : ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, itemTargetPath(path));
    }

    public static String fluidTargetPath(String name) {
        String path = name == null ? "" : name.toLowerCase(java.util.Locale.ROOT);
        int separator = Math.max(path.lastIndexOf(':'), path.lastIndexOf('.'));
        if (separator >= 0) path = path.substring(separator + 1);
        return "liquid_mercury".equals(path) ? "mercury" : path;
    }
}
