package com.mcmoddev.basemetals.util;

import java.util.List;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.item.ItemMMDBlend;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.item.ItemMMDShield;
import com.mcmoddev.lib.item.ItemMMDSmallBlend;
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

	/**
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void attackEvent(final LivingAttackEvent event) {
		final float damage = event.getAmount();
		if (!(event.getEntityLiving() instanceof EntityPlayer)) {
			return;
		}
		final EntityPlayer player = (EntityPlayer) event.getEntityLiving();
		final ItemStack activeItemStack = player.getActiveItemStack();
		if (activeItemStack.isEmpty()) {
			return;
		}
		if ((damage > 0.0F) && (activeItemStack.getItem() instanceof ItemMMDShield)) {
			final int i = 1 + MathHelper.floor(damage);
			activeItemStack.damageItem(i, player);
			if (activeItemStack.getCount() <= 0) {
				final EnumHand enumhand = player.getActiveHand();
				ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
				} else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);
				}
				if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
					player.playSound(SoundEvents.BLOCK_ANVIL_BREAK, 0.8F,
							0.8F + (player.world.rand.nextFloat() * 0.4F));
				}
			}
		}
	}

	@SubscribeEvent
	void event(final ItemCraftedEvent event) {
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
	void event(final ItemSmeltedEvent event) {
		if (!(Options.enableAchievements())) {
			return;
		}

		final Item item = event.smelting.getItem();
		if (!(item instanceof IMMDObject)) {
			return;
		}

		final String materialName = ((IMMDObject) item).getMMDMaterial().getName();
		if (item instanceof ItemMMDIngot) {
			event.player.addStat(Achievements.getAchievementByName(AchievementNames.THIS_IS_NEW),
					1);
			if (materialName.equals(MaterialNames.AQUARIUM)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.AQUARIUM_MAKER), 1);
			} else if (materialName.equals(MaterialNames.BRASS)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.BRASS_MAKER), 1);
			} else if (materialName.equals(MaterialNames.BRONZE)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.BRONZE_MAKER), 1);
			} else if (materialName.equals(MaterialNames.ELECTRUM)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.ELECTRUM_MAKER), 1);
			} else if (materialName.equals(MaterialNames.STEEL)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.STEEL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.INVAR)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.INVAR_MAKER), 1);
			} else if (materialName.equals(MaterialNames.MITHRIL)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.MITHRIL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.CUPRONICKEL)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.CUPRONICKEL_MAKER), 1);
			} else if (materialName.equals(MaterialNames.PEWTER)) {
				event.player.addStat(
						Achievements.getAchievementByName(AchievementNames.PEWTER_MAKER), 1);
			}
		}
	}

	/**
	 *
	 * @return
	 */
	public static InventoryCrafting getDummyCraftingInv() {
		final Container tempContainer = new Container() {

			@Override
			public boolean canInteractWith(final EntityPlayer player) {
				return false;
			}
		};

		return new InventoryCrafting(tempContainer, 2, 1);
	}

	/**
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void handleAnvilEvent(final AnvilUpdateEvent event) {
		final ItemStack left = event.getLeft();
		final ItemStack right = event.getRight();

		if (left.isEmpty() || right.isEmpty() || (left.getCount() != 1)
				|| (right.getCount() != 1)) {
			return;
		}

		final InventoryCrafting recipeInput = getDummyCraftingInv();
		recipeInput.setInventorySlotContents(0, left);
		recipeInput.setInventorySlotContents(1, right);
		final List<IRecipe> recipes = CraftingManager.getInstance().getRecipeList();
		for (final IRecipe recipe : recipes) {
			if ((recipe instanceof ShieldUpgradeRecipe)
					&& (((ShieldUpgradeRecipe) recipe).matches(recipeInput, null))) {
				event.setOutput(recipe.getCraftingResult(recipeInput));
				event.setCost(((ShieldUpgradeRecipe) recipe).getCost(recipeInput));
			}
		}
	}

	/**
	 *
	 * @param event
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onUpdate(final TickEvent.RenderTickEvent event) {
		if ((Options.requireMMDOreSpawn()) && (Loader.isModLoaded("orespawn"))) {
			return;
		}

		if (!Options.fallbackOrespawn()) {
			return;
		}

		if (!Options.requireMMDOreSpawn()) {
			return;
		}

		final Minecraft minecraft = Minecraft.getMinecraft();
		final GuiScreen guiscreen = minecraft.currentScreen;
		if (guiscreen == null) {
			return;
		}
		final FontRenderer fontRender = minecraft.fontRenderer;
		final int y = (guiscreen.height / 100) * 2;
		int x = (guiscreen.width / 2);

		if (guiscreen instanceof GuiMainMenu) {
			guiscreen.drawCenteredString(fontRender,
					"MMD OreSpawn not present, but requested in configuration, using fallback generator!",
					x, y, 0xffffff00);
		} else if (guiscreen instanceof GuiWorldSelection) {
			x = 10;
			final int widest = fontRender.getStringWidth(
					"This is likely not what you want - try turning off the 'using_orespawn' option");
			final int shortest = fontRender.getStringWidth("Fallback Ore Spawn Generator Enabled!");
			int wrap = widest + 50;

			if ((guiscreen.width / 2) <= wrap) {
				wrap = shortest + 50;
			}

			fontRender.drawSplitString(
					"Fallback Ore Spawn Generator Enabled!\nThis is likely not what you want - try turning off the 'using_orespawn' option\n(or install MMD OreSpawn)",
					x, y, wrap, 0xFFFFFF00);
		}
	}
}
