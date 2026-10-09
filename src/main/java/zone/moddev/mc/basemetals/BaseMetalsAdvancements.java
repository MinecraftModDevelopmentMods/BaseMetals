package zone.moddev.mc.basemetals;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.content.MaterialItems;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

final class BaseMetalsAdvancements {
    private static final String CRITERION = "event";
    private static final ResourceLocation STORAGE_BLOCKS = new ResourceLocation("forge", "storage_blocks");
    private static final Map<String, String> ALLOY_ADVANCEMENTS;

    static {
        Map<String, String> values = new LinkedHashMap<String, String>();
        values.put("aquarium", "aquarium_maker");
        values.put("brass", "brass_maker");
        values.put("bronze", "bronze_maker");
        values.put("cupronickel", "cupronickel_maker");
        values.put("electrum", "electrum_maker");
        values.put("invar", "invar_maker");
        values.put("mithril", "mithril_maker");
        values.put("pewter", "pewter_maker");
        values.put("steel", "steel_maker");
        ALLOY_ADVANCEMENTS = Collections.unmodifiableMap(values);
    }

    private BaseMetalsAdvancements() {}

    static void onCrafted(ServerPlayer player, ItemStack result) {
        if (result.getItem() instanceof CrackhammerItem) award(player, "geologist");
        ResourceLocation id = result.getItem().getRegistryName();
        if (baseMetals(id) && (id.getPath().endsWith("_blend") || id.getPath().endsWith("_smallblend"))) {
            award(player, "metallurgy");
        }
    }

    static void onSmelted(ServerPlayer player, ItemStack result) {
        ResourceLocation id = result.getItem().getRegistryName();
        if (!baseMetals(id) || !id.getPath().endsWith("_ingot")) return;
        award(player, "this_is_new");
        String material = id.getPath().substring(0, id.getPath().length() - "_ingot".length());
        String advancement = ALLOY_ADVANCEMENTS.get(material);
        if (advancement != null) award(player, advancement);
    }

    static void onPlaced(ServerPlayer player, BlockState state) {
        ResourceLocation id = state.getBlock().getRegistryName();
        if (baseMetals(id) && state.is(BlockTags.getAllTags().getTagOrEmpty(STORAGE_BLOCKS))) {
            award(player, "blocktastic");
        }
    }

    static void onEquipment(ServerPlayer player) {
        if (fullArmor(player, "coldiron") && mainHandSword(player, "coldiron")) award(player, "demon_slayer");
        if (fullArmor(player, "mithril") && mainHandSword(player, "mithril")) award(player, "angel_of_death");
        if (fullArmor(player, "aquarium") && player.isInWater()) award(player, "scuba_diver");
        if (fullArmor(player, "adamantine")) award(player, "juggernaut");
        if (material(player.getItemBySlot(EquipmentSlot.FEET), "starsteel")) award(player, "moon_boots");
    }

    private static boolean fullArmor(ServerPlayer player, String material) {
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : slots) {
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
        return stack.getItem() instanceof MaterialBacked
                && ((MaterialBacked) stack.getItem()).baseMetalsMaterial().name().equals(expected);
    }

    private static boolean baseMetals(ResourceLocation id) {
        return id != null && BaseMetals.MOD_ID.equals(id.getNamespace());
    }

    static boolean award(ServerPlayer player, String id) {
        Advancement advancement = player.getServer().getAdvancements().getAdvancement(
                new ResourceLocation(BaseMetals.MOD_ID, id));
        if (advancement == null) return false;
        AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
        return !progress.isDone() && player.getAdvancements().award(advancement, CRITERION);
    }
}
