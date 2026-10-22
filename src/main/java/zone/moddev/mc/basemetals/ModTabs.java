package zone.moddev.mc.basemetals;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import zone.moddev.mc.basemetals.content.BaseMetalAmmoItem;
import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.ScytheItem;

public final class ModTabs {
    private ModTabs() {}

    public static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, BaseMetals.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = REGISTER.register("blocks", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.basemetals.blocks"))
            .icon(() -> ModContent.item("starsteel_block").get().getDefaultInstance())
            .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                     // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category(entry.get()).equals("blocks")) {
                        output.accept(entry.get());
                    }
            })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ITEMS = REGISTER.register("items", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.basemetals.items"))
            .icon(() -> ModContent.item("starsteel_gear").get().getDefaultInstance())
            .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                     // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category(entry.get()).equals("items")) {
                        output.accept(entry.get());
                    }
            })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TOOLS = REGISTER.register("tools", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.basemetals.tools"))
            .icon(() -> ModContent.item("starsteel_pickaxe").get().getDefaultInstance())
            .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                     // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category(entry.get()).equals("tools")) {
                        output.accept(entry.get());
                    }
            })).build());
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COMBAT = REGISTER.register("combat", () ->
        CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.basemetals.combat"))
            .icon(() -> ModContent.item("starsteel_sword").get().getDefaultInstance())
            .displayItems((parameters, output) -> ModContent.itemsById().forEach((id, entry) -> {
                     // Molten block items are kept for old saves; filled buckets are shown instead.
                    if (!ModContent.fluids().containsKey(id) && category(entry.get()).equals("combat")) {
                        output.accept(entry.get());
                    }
            })).build());


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
