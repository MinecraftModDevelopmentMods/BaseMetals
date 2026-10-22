package zone.moddev.mc.basemetals;

import java.util.List;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.moddev.mc.basemetals.content.ModContent;

/** Registry-name aliases used when loading older Minecraft and Base Metals saves. */
public final class MissingMappings {
    private MissingMappings() {}

    public static void registerAliases() {
        for (String namespace : List.of(BaseMetals.MOD_ID, "mmdlib")) {
            ModContent.blocksById().keySet().forEach(path -> alias(ModContent.BLOCKS,
                    namespace, path, id(BaseMetals.MOD_ID, path)));
            ModContent.itemsById().keySet().forEach(path -> alias(ModContent.ITEMS,
                    namespace, path, itemTargetId(path)));
            ModContent.itemsById().keySet().stream().filter(path -> path.endsWith("_door"))
                    .forEach(path -> alias(ModContent.ITEMS, namespace, path + "_item",
                            id(BaseMetals.MOD_ID, path)));
            ModContent.fluids().keySet().forEach(path -> alias(ModContent.FLUIDS,
                    namespace, path, id(BaseMetals.MOD_ID, path)));

            alias(ModContent.BLOCKS, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury"));
            alias(ModContent.ITEMS, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury_bucket"));
            alias(ModContent.ITEMS, namespace, "carbon_powder", id(BaseMetals.MOD_ID, "coal_powder"));
            alias(ModContent.ITEMS, namespace, "iron_nugget", id("minecraft", "iron_nugget"));
            alias(ModContent.FLUIDS, namespace, "liquid_mercury", id(BaseMetals.MOD_ID, "mercury"));
        }
        for (String path : List.of("sign", "wall_sign")) {
            alias(ModContent.BLOCKS, "minecraft", path, vanilla114Target(id("minecraft", path)));
        }
        for (String path : List.of("sign", "rose_red", "cactus_green", "dandelion_yellow")) {
            alias(ModContent.ITEMS, "minecraft", path, vanilla114Target(id("minecraft", path)));
        }
    }

    private static ResourceLocation id(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    private static void alias(DeferredRegister<?> registry, String namespace,
            String oldPath, ResourceLocation target) {
        ResourceLocation oldId = id(namespace, oldPath);
        // Identity aliases would introduce a resolution loop.
        if (!oldId.equals(target)) registry.addAlias(oldId, target);
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
        return "liquid_mercury".equals(path) ? "mercury" : path;
    }

    public static String itemTargetPath(String path) {
        if ("carbon_powder".equals(path)) return "coal_powder";
        if ("liquid_mercury".equals(path)) return "mercury_bucket";
        return path.endsWith("_door_item") ? path.substring(0, path.length() - "_item".length()) : path;
    }

    public static ResourceLocation itemTargetId(String path) {
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
