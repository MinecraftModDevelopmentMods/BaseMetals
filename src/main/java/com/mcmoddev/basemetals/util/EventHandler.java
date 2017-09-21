package com.mcmoddev.basemetals.util;


import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.item.ItemMMDBlend;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.item.ItemMMDShield;
import com.mcmoddev.lib.item.ItemMMDSmallBlend;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.recipe.ShieldUpgradeRecipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventHandler {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		float damage = event.getAmount();
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.getActiveItemStack() == ItemStack.EMPTY) {
			return;
		}
		ItemStack activeItemStack = player.getActiveItemStack();
		if ((damage > 0.0F) && (activeItemStack != ItemStack.EMPTY) && (activeItemStack.getItem() instanceof ItemMMDShield)) {
			int i = 1 + MathHelper.floor(damage);
			activeItemStack.damageItem(i, player);
			if (activeItemStack.getCount() <= 0) {
				EnumHand enumhand = player.getActiveHand();
				ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				} else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
					player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
				}
			}
		}
	}

	@SubscribeEvent void event(ItemCraftedEvent event) {
		if (!(Options.enableAchievements())) {
			return;
		}

		final Item item = event.crafting.getItem();
		if (!(item instanceof IMMDObject)) {
			return;
		}
			
//		final String materialName = ((IMMDObject) item).getMMDMaterial().getName();
		if ((item instanceof ItemMMDBlend) || (item instanceof ItemMMDSmallBlend)) {
	//		event.player.addStat(Achievements.getAchievementByName("metallurgy"), 1);
		} else if (item instanceof ItemMMDIngot) {
	//		event.player.addStat(Achievements.getAchievementByName("this_is_new"), 1);
		}
	}

	@SubscribeEvent
	void event(ItemSmeltedEvent event) {
		if (!(Options.enableAchievements())) {
			return;
		}

		final Item item = event.smelting.getItem();
		if (!(item instanceof IMMDObject)) {
			return;
		}

		final String materialName = ((IMMDObject) item).getMMDMaterial().getName();
		if (item instanceof ItemMMDIngot) {
			// event.player.addStat(Achievements.getAchievementByName(AchievementNames.THIS_IS_NEW), 1);
			if (materialName.equals(MaterialNames.AQUARIUM)) {
		//		event.player.addStat(Achievements.getAchievementByName("aquarium_maker"), 1);
			} else if (materialName.equals(MaterialNames.BRASS)) {
	//			event.player.addStat(Achievements.getAchievementByName("brass_maker"), 1);
			} else if (materialName.equals(MaterialNames.BRONZE)) {
	//			event.player.addStat(Achievements.getAchievementByName("bronze_maker"), 1);
			} else if (materialName.equals(MaterialNames.ELECTRUM)) {
	//			event.player.addStat(Achievements.getAchievementByName("electrum_maker"), 1);
			} else if (materialName.equals(MaterialNames.STEEL)) {
	//			event.player.addStat(Achievements.getAchievementByName("steel_maker"), 1);
			} else if (materialName.equals(MaterialNames.INVAR)) {
	//			event.player.addStat(Achievements.getAchievementByName("invar_maker"), 1);
			} else if (materialName.equals(MaterialNames.MITHRIL)) {
	//			event.player.addStat(Achievements.getAchievementByName("mithril_maker"), 1);
			} else if (materialName.equals(MaterialNames.CUPRONICKEL)) {
	//			event.player.addStat(Achievements.getAchievementByName("cupronickel_maker"), 1);
			}
		}
	}

	public static InventoryCrafting getDummyCraftingInv() {
		Container tempContainer = new Container() {
			@Override
			public boolean canInteractWith(EntityPlayer player) {
				return false;
			}
		};

		return new InventoryCrafting(tempContainer, 2, 1);
	}

	@SubscribeEvent
	public void handleAnvilEvent(AnvilUpdateEvent event) {
		ItemStack left = event.getLeft();
		ItemStack right = event.getRight();

		if (left == ItemStack.EMPTY || right == ItemStack.EMPTY || left.getCount() != 1 || right.getCount() != 1) {
			return;
		}

		InventoryCrafting recipeInput = getDummyCraftingInv();
		recipeInput.setInventorySlotContents(0, left);
		recipeInput.setInventorySlotContents(1, right);
		IRecipe recipe = CraftingManager.findMatchingRecipe(recipeInput, null);
		if ((recipe instanceof ShieldUpgradeRecipe) && (((ShieldUpgradeRecipe) recipe).matches(recipeInput, null))) {
			event.setOutput(recipe.getCraftingResult(recipeInput));
			event.setCost(((ShieldUpgradeRecipe) recipe).getCost(recipeInput));
		}
	}
}
