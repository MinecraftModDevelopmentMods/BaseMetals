package com.mcmoddev.basemetals.util;

import java.util.List;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.item.*;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.recipe.ShieldUpgradeRecipe;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
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
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {

	@SubscribeEvent
	public void attackEvent(LivingAttackEvent event) {
		float damage = event.getAmount();
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		if (player.getActiveItemStack() == null) {
			return;
		}
		ItemStack activeItemStack = player.getActiveItemStack();
		if ((damage > 0.0F) && (activeItemStack != null)
				&& (activeItemStack.getItem() instanceof ItemMMDShield)) {
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

	@SubscribeEvent
	void event(ItemCraftedEvent event) {
		if (!(Options.enableAchievements())) {
			return;
		}

		final Item item = event.crafting.getItem();
		if (!(item instanceof IMMDObject)) {
			return;
		}

		if ((item instanceof ItemMMDBlend) || (item instanceof ItemMMDSmallBlend)) {
			event.player.addStat(Achievements.getAchievementByName(AchievementNames.METALLURGY), 1);
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
			event.player.addStat(Achievements.getAchievementByName(AchievementNames.THIS_IS_NEW), 1);
			if (materialName.equals(MaterialNames.AQUARIUM)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.AQUARIUM_MAKER), 1);
			} else if (materialName.equals(MaterialNames.BRASS)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.BRASS_MAKER), 1);
			} else if (materialName.equals(MaterialNames.BRONZE)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.BRONZE_MAKER), 1);
			} else if (materialName.equals(MaterialNames.ELECTRUM)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.ELECTRUM_MAKER), 1);
			} else if (materialName.equals(MaterialNames.STEEL)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.STEEL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.INVAR)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.INVAR_MAKER), 1);
			} else if (materialName.equals(MaterialNames.MITHRIL)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.MITHRIL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.CUPRONICKEL)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.CUPRONICKEL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.PEWTER)) {
				event.player.addStat(Achievements.getAchievementByName(AchievementNames.PEWTER_MAKER), 1);
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

		if (left == null || right == null || left.stackSize != 1 || right.stackSize != 1) {
			return;
		}

		InventoryCrafting recipeInput = getDummyCraftingInv();
		recipeInput.setInventorySlotContents(0, left);
		recipeInput.setInventorySlotContents(1, right);
		List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (IRecipe recipe : recipes) {
			if ((recipe instanceof ShieldUpgradeRecipe) && (((ShieldUpgradeRecipe) recipe).matches(recipeInput, null))) {
				event.setOutput(recipe.getCraftingResult(recipeInput));
				event.setCost(((ShieldUpgradeRecipe) recipe).getCost(recipeInput));
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onUpdate(TickEvent.RenderTickEvent event) {
		if ((Options.requireMMDOreSpawn()) && (Loader.isModLoaded("orespawn"))) {
			return;
		}

		if (!Options.fallbackOrespawn()) {
			return;
		}

		if (!Options.requireMMDOreSpawn()) {
			return;
		}

		// FIXME: .fontRenderer doesn't exist on 1.10.2
		/*
        GuiScreen guiscreen = Minecraft.getMinecraft().currentScreen;
        if( guiscreen == null ) return;
    	FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
    	int y = (guiscreen.height / 100) * 2;
    	int x = (guiscreen.width/2);
        
        if (guiscreen instanceof GuiMainMenu) {
        	guiscreen.drawCenteredString(fontRender, "MMD OreSpawn not present, but requested in configuration, using fallback generator!", x, y, 0xffffff00);        	
        } else if(guiscreen instanceof GuiWorldSelection) {
        	x = 10;
        	int widest = fontRender.getStringWidth("This is likely not what you want - try turning off the 'using_orespawn' option");
        	int shortest = fontRender.getStringWidth("Fallback Ore Spawn Generator Enabled!");
        	int wrap = widest+50;

        	if( (guiscreen.width/2) <= wrap ) {
        		wrap = shortest + 50;
        	}
        	
        	fontRender.drawSplitString("Fallback Ore Spawn Generator Enabled!\nThis is likely not what you want - try turning off the 'using_orespawn' option\n(or install MMD OreSpawn)", x, y, wrap, 0xFFFFFF00);
        }
		*/
    }
}
