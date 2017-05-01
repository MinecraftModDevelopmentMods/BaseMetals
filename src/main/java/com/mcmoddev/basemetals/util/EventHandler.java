package com.mcmoddev.basemetals.util;

import java.util.List;

import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.item.ItemMMDShield;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
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
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.relauncher.Side;

public class EventHandler {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent e) {
		float damage = e.getAmount();
		if (!(e.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) e.getEntityLiving();
		if (player.getActiveItemStack() == null) {
			return;
		}
		ItemStack activeItemStack = player.getActiveItemStack();
		if ((damage > 0.0F) && (activeItemStack != null) && (activeItemStack.getItem() instanceof ItemMMDShield)) {
			int i = 1 + MathHelper.floor(damage);
			activeItemStack.damageItem(i, player);
			if (activeItemStack.stackSize <= 0) {
				EnumHand enumhand = player.getActiveHand();
				ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
				} else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack) null);
				}
				activeItemStack = null;
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
					player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8F, 0.8F + player.world.rand.nextFloat() * 0.4F);
				}
			}
		}
	}

	/*
	@SubscribeEvent void event(ItemCraftedEvent event) {
		final Item item = event.crafting.getItem();
		if (item instanceof IMetalObject) {
			final MetalMaterial material = ((IMetalObject) item).getMaterial();
			if (Options.enableAchievements) {
				if (item instanceof ItemMetalBlend) {
					// event.player.addStat(Achievements.metallurgy, 1);
				}
			}
		}
	}
	*/

	@SubscribeEvent
	void event(ItemSmeltedEvent event) {
		final Item item = event.smelting.getItem();
		if (item instanceof IMMDObject) {
			final MMDMaterial material = ((IMMDObject) item).getMMDMaterial();
			if (Options.enableAchievements()) {
				if (item instanceof ItemMMDIngot) {
					// event.player.addStat(Achievements.this_is_new, 1);
					if ("aquarium".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("aquarium_maker"), 1);
					} else if ("brass".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("brass_maker"), 1);
					} else if ("bronze".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("bronze_maker"), 1);
					} else if ("electrum".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("electrum_maker"), 1);
					} else if ("steel".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("steel_maker"), 1);
					} else if ("invar".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("invar_maker"), 1);
					} else if ("mithril".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("mithril_maker"), 1);
					} else if ("cupronickel".equals(material.getName())) {
						event.player.addStat(Achievements.getAchievementByName("cupronickel_maker"), 1);
					}
				}
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
	public void handleAnvilEvent(AnvilUpdateEvent evt) {
		ItemStack left = evt.getLeft();
		ItemStack right = evt.getRight();

		if (left == null || right == null || left.stackSize != 1 || right.stackSize != 1) {
			return;
		}

		InventoryCrafting recipeInput = getDummyCraftingInv();
		recipeInput.setInventorySlotContents(0, left);
		recipeInput.setInventorySlotContents(1, right);
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe r : recipes) {
			if (r instanceof ShieldUpgradeRecipe) {
				if (((ShieldUpgradeRecipe) r).matches(recipeInput, null)) {
					evt.setOutput(r.getCraftingResult(recipeInput));
					evt.setCost(((ShieldUpgradeRecipe) r).getCost(recipeInput));
				}
			}
		}
	}
}
