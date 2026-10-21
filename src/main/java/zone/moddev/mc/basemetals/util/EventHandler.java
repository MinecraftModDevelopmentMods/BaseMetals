package zone.moddev.mc.basemetals.util;

import java.util.List;

import zone.moddev.mc.basemetals.data.AchievementNames;
import zone.moddev.mc.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.item.ItemMMDBlend;
import com.mcmoddev.lib.item.ItemMMDIngot;
import com.mcmoddev.lib.item.ItemMMDShield;
import com.mcmoddev.lib.item.ItemMMDSmallBlend;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.recipe.ShieldUpgradeRecipe;
import com.mcmoddev.lib.util.ConfigBase.Options;

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
		if (activeItemStack == null) {
			return;
		}
		if ((damage > 0.0F) && (activeItemStack.getItem() instanceof ItemMMDShield)) {
			final int i = 1 + MathHelper.floor(damage);
			activeItemStack.damageItem(i, player);
			if (activeItemStack.stackSize <= 0) {
				final EnumHand enumhand = player.getActiveHand();
				ForgeEventFactory.onPlayerDestroyItem(player, activeItemStack, enumhand);
				if (enumhand == EnumHand.MAIN_HAND) {
					player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, (ItemStack) null);
				} else {
					player.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, (ItemStack) null);
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

		if ((left == null) || (right == null) || (left.stackSize != 1)
				|| (right.stackSize != 1)) {
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

}
