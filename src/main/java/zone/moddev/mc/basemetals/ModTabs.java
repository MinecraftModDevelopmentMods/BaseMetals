package zone.moddev.mc.basemetals;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.lib.common.runtime.MaterialAmmoItem;
import zone.moddev.mc.lib.common.runtime.ScytheItem;
import zone.moddev.mc.lib.common.runtime.StandardItems;

public final class ModTabs {
    private ModTabs() {}

    public static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BaseMetals.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = tab("blocks", "starsteel_block");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = tab("items", "starsteel_gear");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS = tab("tools", "starsteel_pickaxe");
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMBAT = tab("combat", "starsteel_sword");

    private static DeferredHolder<CreativeModeTab, CreativeModeTab> tab(String category, String icon) {
        return REGISTER.register(category, () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.basemetals." + category))
            .icon(() -> ModContent.item(icon).get().getDefaultInstance())
            .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                // Molten block items are kept for old saves; filled buckets are shown instead.
                if (!ModContent.fluids().containsKey(id) && category(entry.get()).equals(category)) {
                    output.accept(entry.get());
                }
            })).build());
    }

    public static String category(Item item) {
        if (item instanceof BlockItem) return "blocks";
        if (item instanceof StandardItems.Tool || item instanceof ScytheItem
                || item instanceof StandardItems.Pickaxe || item instanceof StandardItems.Axe
                || item instanceof StandardItems.Shovel || item instanceof StandardItems.Hoe
                || item instanceof StandardItems.Shears || item instanceof StandardItems.FishingRod) return "tools";
        if (item instanceof StandardItems.Sword || item instanceof StandardItems.Bow
                || item instanceof StandardItems.Crossbow || item instanceof StandardItems.Armor
                || item instanceof StandardItems.HorseArmor || item instanceof StandardItems.Shield
                || item instanceof MaterialAmmoItem) return "combat";
        return "items";
    }
}
