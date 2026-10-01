package com.mcmoddev.basemetals.advancement;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.util.Config.Options;
import net.minecraft.advancements.Advancement;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/** Awards the original achievements for crafting, smelting, building, and equipment. */
@EventBusSubscriber(modid = BaseMetals.MODID)
public final class GameplayAdvancements {

	private static final Set<String> ALLOYS = new HashSet<>(Arrays.asList(
			"aquarium", "brass", "bronze", "cupronickel", "electrum", "invar",
			"mithril", "pewter", "steel"));
	private static final EntityEquipmentSlot[] ARMOR_SLOTS = {
			EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
			EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
	private static final String[] ARMOR_FORMS = {"helmet", "chestplate", "leggings", "boots"};

	private GameplayAdvancements() {
	}

	@SubscribeEvent
	public static void crafted(final ItemCraftedEvent event) {
		if (!enabled(event.player)) {
			return;
		}

		final String path = itemPath(event.crafting);

		if (path == null) {
			return;
		}

		if (path.endsWith("_crackhammer")) {
			award(event.player, "geologist");
		}

		if (path.endsWith("_blend") || path.endsWith("_smallblend")) {
			award(event.player, "metallurgy");
		}
	}

	@SubscribeEvent
	public static void smelted(final ItemSmeltedEvent event) {
		if (!enabled(event.player)) {
			return;
		}

		final String path = itemPath(event.smelting);

		if (path == null || !path.endsWith("_ingot")) {
			return;
		}

		award(event.player, "this_is_new");

		final String material = path.substring(0, path.length() - "_ingot".length());

		if (ALLOYS.contains(material)) {
			award(event.player, material + "_maker");
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void placed(final BlockEvent.EntityPlaceEvent event) {
		if (!(event.getEntity() instanceof EntityPlayerMP)) {
			return;
		}

		final EntityPlayerMP player = (EntityPlayerMP) event.getEntity();

		if (!enabled(player)) {
			return;
		}

		final ResourceLocation id = event.getPlacedBlock().getBlock().getRegistryName();

		if (id != null && BaseMetals.MODID.equals(id.getNamespace())
				&& id.getPath().endsWith("_block")
				&& !"iron_block".equals(id.getPath()) && !"gold_block".equals(id.getPath())) {
			award(player, "blocktastic");
		}
	}

	@SubscribeEvent
	public static void equipped(final TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END || !enabled(event.player)
				|| event.player.ticksExisted % 20 != 0) {
			return;
		}

		final EntityPlayer player = event.player;

		if (fullArmor(player, "adamantine")) {
			award(player, "juggernaut");
		}

		if (fullArmor(player, "coldiron") && "coldiron_sword".equals(itemPath(player.getHeldItemMainhand()))) {
			award(player, "demon_slayer");
		}

		if (fullArmor(player, "mithril") && "mithril_sword".equals(itemPath(player.getHeldItemMainhand()))) {
			award(player, "angel_of_death");
		}

		if (fullArmor(player, "aquarium") && player.isInsideOfMaterial(Material.WATER)) {
			award(player, "scuba_diver");
		}

		if ("starsteel_boots".equals(itemPath(player.getItemStackFromSlot(EntityEquipmentSlot.FEET)))) {
			award(player, "moon_boots");
		}
	}

	private static boolean enabled(final EntityPlayer player) {
		return player instanceof EntityPlayerMP && !player.world.isRemote && Options.enableAchievements();
	}

	private static boolean fullArmor(final EntityPlayer player, final String material) {
		for (int index = 0; index < ARMOR_SLOTS.length; index++) {
			final ItemStack stack = player.getItemStackFromSlot(ARMOR_SLOTS[index]);

			if (!(stack.getItem() instanceof ItemArmor)
					|| ((ItemArmor) stack.getItem()).armorType != ARMOR_SLOTS[index]
					|| !(material + "_" + ARMOR_FORMS[index]).equals(itemPath(stack))) {
				return false;
			}
		}

		return true;
	}

	private static String itemPath(final ItemStack stack) {
		final ResourceLocation id = stack.isEmpty() ? null : stack.getItem().getRegistryName();

		return id != null && BaseMetals.MODID.equals(id.getNamespace()) ? id.getPath() : null;
	}

	private static void award(final EntityPlayer player, final String id) {
		final EntityPlayerMP serverPlayer = (EntityPlayerMP) player;
		final Advancement advancement = player.getServer().getAdvancementManager()
				.getAdvancement(new ResourceLocation(BaseMetals.MODID, id));

		// Disabled achievements and server overrides may leave no definition to award.
		if (advancement != null && !serverPlayer.getAdvancements().getProgress(advancement).isDone()) {
			serverPlayer.getAdvancements().grantCriterion(advancement, "event");
		}
	}
}
