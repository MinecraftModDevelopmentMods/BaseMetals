package zone.moddev.mc.basemetals;

import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.RegistryHandle;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/** Registry-name aliases used when loading older Minecraft and Base Metals saves. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MissingMappings {
    private MissingMappings() {}

    @SubscribeEvent
    public static void blocks(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
            ResourceLocation vanillaTarget = vanilla114Target(mapping.key);
            if (vanillaTarget != null) {
                mapping.remap(ForgeRegistries.BLOCKS.getValue(vanillaTarget));
                continue;
            }
            if (!isLegacyNamespace(mapping.key.getNamespace())) continue;
            RegistryHandle<Block> target = ModContent.blocksById().get(blockTargetPath(mapping.key.getPath()));
            if (target != null) mapping.remap(target.get());
        }
    }

    @SubscribeEvent
    public static void items(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
            ResourceLocation vanillaTarget = vanilla114Target(mapping.key);
            if (vanillaTarget != null) {
                mapping.remap(ForgeRegistries.ITEMS.getValue(vanillaTarget));
                continue;
            }
            if (!isLegacyNamespace(mapping.key.getNamespace())) continue;
            Item target = ForgeRegistries.ITEMS.getValue(itemTargetId(mapping.key.getPath()));
            if (target != null) mapping.remap(target);
        }
    }

    private static boolean isLegacyNamespace(String namespace) {
        return BaseMetals.MOD_ID.equals(namespace) || "mmdlib".equals(namespace);
    }

    public static ResourceLocation vanilla114Target(ResourceLocation oldName) {
        if (!"minecraft".equals(oldName.getNamespace())) return null;

        // Vanilla converts the saved blocks and items, but Forge's registry
        // snapshot still needs aliases for names retired in 1.14.
        switch (oldName.getPath()) {
            case "sign": return new ResourceLocation("minecraft", "oak_sign");
            case "wall_sign": return new ResourceLocation("minecraft", "oak_wall_sign");
            case "rose_red": return new ResourceLocation("minecraft", "red_dye");
            case "cactus_green": return new ResourceLocation("minecraft", "green_dye");
            case "dandelion_yellow": return new ResourceLocation("minecraft", "yellow_dye");
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
                ? new ResourceLocation("minecraft", "iron_nugget")
                : new ResourceLocation(BaseMetals.MOD_ID, itemTargetPath(path));
    }

    public static String fluidTargetPath(String name) {
        String path = name == null ? "" : name.toLowerCase(java.util.Locale.ROOT);
        int separator = Math.max(path.lastIndexOf(':'), path.lastIndexOf('.'));
        if (separator >= 0) path = path.substring(separator + 1);
        return "liquid_mercury".equals(path) ? "mercury" : path;
    }
}
