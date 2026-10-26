package zone.moddev.mc.basemetals;

import zone.moddev.mc.basemetals.content.ModContent;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.world.level.material.Fluid;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.Identifier;
import net.minecraftforge.registries.MissingMappingsEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/** Registry-name aliases used when loading older Minecraft and Base Metals saves. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MissingMappings {
    private MissingMappings() {}

    @SubscribeEvent
    public static void blocks(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Block> mapping : event.getAllMappings(ForgeRegistries.Keys.BLOCKS)) {
            Identifier vanillaTarget = vanilla114Target(mapping.getKey());
            if (vanillaTarget != null) {
                Block target = ForgeRegistries.BLOCKS.getValue(vanillaTarget);
                if (target != null) mapping.remap(target);
                continue;
            }
            if (!isLegacyNamespace(mapping.getKey().getNamespace())) continue;
            RegistryObject<? extends Block> target = ModContent.blocksById().get(blockTargetPath(mapping.getKey().getPath()));
            if (target != null) mapping.remap(target.get());
        }
    }

    @SubscribeEvent
    public static void items(MissingMappingsEvent event) {
        for (MissingMappingsEvent.Mapping<Item> mapping : event.getAllMappings(ForgeRegistries.Keys.ITEMS)) {
            Identifier vanillaTarget = vanilla114Target(mapping.getKey());
            if (vanillaTarget != null) {
                Item target = ForgeRegistries.ITEMS.getValue(vanillaTarget);
                if (target != null) mapping.remap(target);
                continue;
            }
            if (!isLegacyNamespace(mapping.getKey().getNamespace())) continue;
            Item target = ForgeRegistries.ITEMS.getValue(itemTargetId(mapping.getKey().getPath()));
            if (target != null) mapping.remap(target);
        }
    }

    @SubscribeEvent
    public static void fluids(MissingMappingsEvent event) {
        for (var mapping : event.getAllMappings(ForgeRegistries.Keys.FLUIDS)) {
            if (!isLegacyNamespace(mapping.getKey().getNamespace())) continue;
            var target = ModContent.fluids().get(fluidTargetPath(mapping.getKey().getPath()));
            if (target != null) mapping.remap(target.source().get());
        }
    }

    private static boolean isLegacyNamespace(String namespace) {
        return BaseMetals.MOD_ID.equals(namespace) || "mmdlib".equals(namespace);
    }

    public static Identifier vanilla114Target(Identifier oldName) {
        if (!"minecraft".equals(oldName.getNamespace())) return null;

        // Vanilla converts the saved blocks and items, but Forge's registry
        // snapshot still needs aliases for names retired in 1.14.
        switch (oldName.getPath()) {
            case "sign": return Identifier.fromNamespaceAndPath("minecraft", "oak_sign");
            case "wall_sign": return Identifier.fromNamespaceAndPath("minecraft", "oak_wall_sign");
            case "rose_red": return Identifier.fromNamespaceAndPath("minecraft", "red_dye");
            case "cactus_green": return Identifier.fromNamespaceAndPath("minecraft", "green_dye");
            case "dandelion_yellow": return Identifier.fromNamespaceAndPath("minecraft", "yellow_dye");
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

    public static Identifier itemTargetId(String path) {
        return "iron_nugget".equals(path)
                ? Identifier.fromNamespaceAndPath("minecraft", "iron_nugget")
                : Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID, itemTargetPath(path));
    }

    public static String fluidTargetPath(String name) {
        String path = name == null ? "" : name.toLowerCase(java.util.Locale.ROOT);
        int separator = Math.max(path.lastIndexOf(':'), path.lastIndexOf('.'));
        if (separator >= 0) path = path.substring(separator + 1);
        return "liquid_mercury".equals(path) ? "mercury" : path;
    }
}
