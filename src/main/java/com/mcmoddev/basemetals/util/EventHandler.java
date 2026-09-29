package com.mcmoddev.basemetals.util;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.Fluids;
import com.mcmoddev.basemetals.properties.AdamantineProperty;
import com.mcmoddev.basemetals.properties.AdamantineToolProperty;
import com.mcmoddev.basemetals.properties.AquariumProperty;
import com.mcmoddev.basemetals.properties.AquariumToolProperty;
import com.mcmoddev.basemetals.properties.ColdIronProperty;
import com.mcmoddev.basemetals.properties.ColdIronToolProperty;
import com.mcmoddev.basemetals.properties.LeadProperty;
import com.mcmoddev.basemetals.properties.MithrilProperty;
import com.mcmoddev.basemetals.properties.MithrilToolProperty;
import com.mcmoddev.basemetals.properties.StarSteelProperty;
import com.mcmoddev.lib.events.MMDLibRegisterFluids;
import com.mcmoddev.lib.events.MMDLibRegisterMaterialProperties;
import com.mcmoddev.lib.events.MMDLibRegisterMaterials;
import com.mcmoddev.lib.item.ItemMMDShield;
import com.mcmoddev.lib.recipe.ShieldUpgradeRecipe;
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
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid=BaseMetals.MODID)
public final class EventHandler {

	public EventHandler() {
	}

	/** Applies durability loss when a Base Metals shield blocks damage. */
	@SubscribeEvent
	public static void attackEvent(final LivingAttackEvent event) {
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

	/** Creates the two-slot crafting view used to evaluate shield upgrades at an anvil. */
	@Nonnull public static InventoryCrafting getDummyCraftingInv() {
		final Container tempContainer = new Container() {

			@Override
			public boolean canInteractWith(final EntityPlayer player) {
				return false;
			}
		};

		return new InventoryCrafting(tempContainer, 2, 1);
	}

	/** Converts a matching shield-upgrade recipe into an anvil result and level cost. */
	@SubscribeEvent
	public static void handleAnvilEvent(final AnvilUpdateEvent event) {
		final ItemStack left = event.getLeft();
		final ItemStack right = event.getRight();

		if (left.isEmpty() || right.isEmpty() || (left.getCount() != 1)
				|| (right.getCount() != 1)) {
			return;
		}

		final InventoryCrafting recipeInput = getDummyCraftingInv();
		recipeInput.setInventorySlotContents(0, left);
		recipeInput.setInventorySlotContents(1, right);
		final IRecipe recipe = CraftingManager.findMatchingRecipe(recipeInput, null);
		if ((recipe instanceof ShieldUpgradeRecipe)
				&& (((ShieldUpgradeRecipe) recipe).matches(recipeInput, null))) {
			event.setOutput(recipe.getCraftingResult(recipeInput));
			event.setCost(((ShieldUpgradeRecipe) recipe).getCost(recipeInput));
		}
	}

	@SubscribeEvent
	public static void mmdlibRegisterMaterials(final MMDLibRegisterMaterials event) {
		com.mcmoddev.basemetals.init.Materials.init(event);
		if(FMLCommonHandler.instance().getEffectiveSide() ==  Side.CLIENT) {
			com.mcmoddev.basemetals.init.Materials.initTooltips();			
		}
	}
	
	@SubscribeEvent
	public static void mmdlibRegisterMaterialProperies(final MMDLibRegisterMaterialProperties event) {
		event.getRegistry().registerAll(
				new StarSteelProperty().setRegistryName("basemetals:moon_jump"),
				new LeadProperty().setRegistryName("basemetals:slowdown"),
				new AdamantineProperty().setRegistryName("basemetals:resistance_is_futile"),
				new ColdIronProperty().setRegistryName("basemetals:swim_in_lava"),
				new MithrilProperty().setRegistryName("basemetals:clear_effects"),
				new AquariumProperty().setRegistryName("basemetals:the_abyss_was_better"),
				new AdamantineToolProperty().setRegistryName("basemetals:heavy_damage"),
				new ColdIronToolProperty().setRegistryName("basemetals:cold_as_ice"),
				new MithrilToolProperty().setRegistryName("basemetals:holy_roller"),
				new AquariumToolProperty().setRegistryName("basemetals:drown_bitca"));
	}	
}
