package zone.moddev.mc.basemetals;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.CreativeModeTabEvent;
import zone.moddev.mc.basemetals.content.BaseMetalAmmoItem;
import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.ScytheItem;

public final class ModTabs {
    public static CreativeModeTab BLOCKS;
    public static CreativeModeTab ITEMS;
    public static CreativeModeTab TOOLS;
    public static CreativeModeTab COMBAT;

    private ModTabs() {}

    public static void register(CreativeModeTabEvent.Register event) {
        BLOCKS = tab(event, "blocks", "starsteel_block", Items.IRON_BLOCK);
        ITEMS = tab(event, "items", "starsteel_gear", Items.IRON_INGOT);
        TOOLS = tab(event, "tools", "starsteel_pickaxe", Items.IRON_PICKAXE);
        COMBAT = tab(event, "combat", "starsteel_sword", Items.IRON_SWORD);
    }

    private static CreativeModeTab tab(CreativeModeTabEvent.Register event, String category,
            String iconId, Item fallback) {
        return event.registerCreativeModeTab(new ResourceLocation(BaseMetals.MOD_ID, category), builder -> builder
                .title(Component.translatable("itemGroup.basemetals." + category))
                .icon(() -> ModContent.itemsById().containsKey(iconId)
                        ? ModContent.item(iconId).get().getDefaultInstance() : fallback.getDefaultInstance())
                .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                    // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category.equals(category(entry.get()))) {
                        output.accept(entry.get());
                    }
                })));
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
