package com.mcmoddev.basemetals.util;


import com.mcmoddev.lib.item.*;
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

	@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onUpdate(TickEvent.RenderTickEvent event) {
		if ((Options.requireMMDOreSpawn()) && (Loader.isModLoaded("orespawn"))) {
			return;
		}
		
		if(!Options.fallbackOrespawn()) {
			return;
		}
		
		if( !Options.requireMMDOreSpawn() ) {
			return;
		}
		
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
    }
}
