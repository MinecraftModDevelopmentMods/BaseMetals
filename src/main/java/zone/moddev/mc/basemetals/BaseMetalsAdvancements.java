package zone.moddev.mc.basemetals;

import java.util.Map;

import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.content.MaterialItems;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Event-backed equivalents of the legacy achievements. Impossible advancement
 * criteria keep the data tree visible while these checks retain behaviours
 * that vanilla inventory predicates cannot express.
 */
final class BaseMetalsAdvancements {
    private static final String CRITERION = "event";
    private static final TagKey<Block> STORAGE_BLOCKS = BlockTags.create(
            ResourceLocation.fromNamespaceAndPath("forge", "storage_blocks"));
    private static final Map<String, String> ALLOY_ADVANCEMENTS = Map.ofEntries(
            Map.entry("aquarium", "aquarium_maker"),
            Map.entry("brass", "brass_maker"),
            Map.entry("bronze", "bronze_maker"),
            Map.entry("cupronickel", "cupronickel_maker"),
            Map.entry("electrum", "electrum_maker"),
            Map.entry("invar", "invar_maker"),
            Map.entry("mithril", "mithril_maker"),
            Map.entry("pewter", "pewter_maker"),
            Map.entry("steel", "steel_maker"));

    private BaseMetalsAdvancements() {}

    static void onCrafted(ServerPlayer player, ItemStack result) {
        if (result.getItem() instanceof CrackhammerItem) award(player, "geologist");
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (id != null && id.getNamespace().equals(BaseMetals.MOD_ID)
                && (id.getPath().endsWith("_blend") || id.getPath().endsWith("_smallblend"))) {
            award(player, "metallurgy");
        }
    }

    static void onSmelted(ServerPlayer player, ItemStack result) {
        ResourceLocation id = BuiltInRegistries.ITEM.getKey(result.getItem());
        if (id == null || !id.getNamespace().equals(BaseMetals.MOD_ID)
                || !id.getPath().endsWith("_ingot")) {
            return;
        }
        award(player, "this_is_new");
        String material = id.getPath().substring(0, id.getPath().length() - "_ingot".length());
        String alloyAdvancement = ALLOY_ADVANCEMENTS.get(material);
        if (alloyAdvancement != null) award(player, alloyAdvancement);
    }

    static void onPlaced(ServerPlayer player, BlockState state) {
        ResourceLocation id = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        if (id != null && id.getNamespace().equals(BaseMetals.MOD_ID) && state.is(STORAGE_BLOCKS)) {
            award(player, "blocktastic");
        }
    }

    static void onEquipment(ServerPlayer player) {
        if (fullArmor(player, "coldiron") && mainHandSword(player, "coldiron")) {
            award(player, "demon_slayer");
        }
        if (fullArmor(player, "mithril") && mainHandSword(player, "mithril")) {
            award(player, "angel_of_death");
        }
        if (fullArmor(player, "aquarium") && player.isUnderWater()) {
            award(player, "scuba_diver");
        }
        if (fullArmor(player, "adamantine")) {
            award(player, "juggernaut");
        }
        if (material(player.getItemBySlot(EquipmentSlot.FEET), "starsteel")) {
            award(player, "moon_boots");
        }
    }

    private static boolean fullArmor(ServerPlayer player, String material) {
        for (EquipmentSlot slot : new EquipmentSlot[] {
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (!(stack.getItem() instanceof ArmorItem) || !material(stack, material)) return false;
        }
        return true;
    }

    private static boolean mainHandSword(ServerPlayer player, String material) {
        ItemStack stack = player.getMainHandItem();
        return stack.getItem() instanceof MaterialItems.Sword && material(stack, material);
    }

    private static boolean material(ItemStack stack, String expected) {
        return stack.getItem() instanceof MaterialBacked backed
                && backed.baseMetalsMaterial().name().equals(expected);
    }

    static boolean award(ServerPlayer player, String id) {
        AdvancementHolder advancement = player.getServer().getAdvancements().get(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, id));
        if (advancement == null) return false;
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        if (progress.isDone()) return false;
        return player.getAdvancements().award(advancement, CRITERION);
    }
}
