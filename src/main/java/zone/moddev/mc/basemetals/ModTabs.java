package zone.moddev.mc.basemetals;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import zone.moddev.mc.basemetals.content.BaseMetalAmmoItem;
import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.ScytheItem;

public final class ModTabs {
    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BaseMetals.MOD_ID);

    public static final RegistryObject<CreativeModeTab> BLOCKS = tab("blocks", "starsteel_block", Items.IRON_BLOCK);
    public static final RegistryObject<CreativeModeTab> ITEMS = tab("items", "starsteel_gear", Items.IRON_INGOT);
    public static final RegistryObject<CreativeModeTab> TOOLS = tab("tools", "starsteel_pickaxe", Items.IRON_PICKAXE);
    public static final RegistryObject<CreativeModeTab> COMBAT = tab("combat", "starsteel_sword", Items.IRON_SWORD);

    private ModTabs() {}

    public static void register(BusGroup bus) {
        TABS.register(bus);
    }

    private static RegistryObject<CreativeModeTab> tab(String category,
            String iconId, Item fallback) {
        return TABS.register(category, () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.basemetals." + category))
                .icon(() -> ModContent.itemsById().containsKey(iconId)
                        ? ModContent.item(iconId).get().getDefaultInstance() : fallback.getDefaultInstance())
                .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                    // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category.equals(category(entry.get()))) {
                        output.accept(entry.get());
                    }
                })).build());
    }

    public static String category(Item item) {
        if (item instanceof BlockItem) return "blocks";
        if (item instanceof CrackhammerItem || item instanceof ScytheItem
                || item instanceof MaterialItems.Pickaxe || item instanceof MaterialItems.Axe
                || item instanceof MaterialItems.Shovel || item instanceof MaterialItems.Hoe
                || item instanceof MaterialItems.Shears || item instanceof MaterialItems.FishingRod) return "tools";
        if (item instanceof MaterialItems.Sword || item instanceof MaterialItems.Bow
                || item instanceof MaterialItems.Crossbow || item instanceof MaterialItems.Armor
                || item instanceof MaterialItems.HorseArmor || item instanceof MaterialItems.Shield
                || item instanceof BaseMetalAmmoItem) return "combat";
        return "items";
    }
}
