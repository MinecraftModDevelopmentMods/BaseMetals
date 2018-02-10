package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemCrossbow extends net.minecraft.item.ItemBow {

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			final boolean flag = entityPlayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			// Check for specific Bolts
			ItemStack itemStack = this.myFindAmmo(entityPlayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityPlayer, i, (!itemStack.isEmpty()) || flag);
			if (i < 0) {
				return;
			}

			if (!itemStack.isEmpty() || flag) {
				if (itemStack.isEmpty()) {
					itemStack = getBolt();
					if (itemStack.isEmpty())
						return; // if its still null at this point, there is something seriously wrong, just bug out
				}

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					final boolean flag1 = entityPlayer.capabilities.isCreativeMode || (itemStack.getItem() instanceof ItemBolt && ((ItemBolt) itemStack.getItem()).isInfinite(itemStack, stack, entityPlayer));

					if (!worldIn.isRemote) {
						final ItemBolt itemBolt = ((ItemBolt) (itemStack.getItem() instanceof ItemBolt ? itemStack.getItem() : Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.BOLT)));
						final EntityCustomBolt entityBolt = itemBolt.createBolt(worldIn, itemStack, entityPlayer);
						entityBolt.shoot(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						if (f == 1.0F) {
							entityBolt.setIsCritical(true);
						}

						final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							entityBolt.setDamage(entityBolt.getDamage() + ((double) j * 0.5D) + 0.5D);
						}

						final int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0) {
							entityBolt.setKnockbackStrength(k);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
							entityBolt.setFire(100);
						}

						stack.damageItem(1, entityPlayer);

						if (flag1 || entityPlayer.capabilities.isCreativeMode) {
							entityBolt.pickupStatus = EntityCustomBolt.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityBolt);
					}

					worldIn.playSound((EntityPlayer) null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f * 0.5F));

					if (!flag1 && !entityPlayer.capabilities.isCreativeMode) {
						itemStack.shrink(1);

						if (itemStack.isEmpty()) {
							entityPlayer.inventory.deleteStack(itemStack);
						}
					}

					entityPlayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	private ItemStack getBolt() {
		for (MMDMaterial material : Materials.getAllMaterials()) {
			if (material.hasItem(Names.BOLT))
				return new ItemStack(material.getItem(Names.BOLT));
		}
		return ItemStack.EMPTY;
	}

	private ItemStack myFindAmmo(final EntityPlayer player) {
		if (this.isBolt(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.isBolt(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				final ItemStack itemStack = player.inventory.getStackInSlot(i);

				if (this.isBolt(itemStack)) {
					return itemStack;
				}
			}

			return ItemStack.EMPTY;
		}
	}

	@Override
	protected boolean isArrow(@Nonnull final ItemStack stack) {
		return false;
	}

	protected boolean isBolt(@Nonnull final ItemStack stack) {
		return stack.getItem() instanceof ItemBolt;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
		final ItemStack itemStack = playerIn.getHeldItem(handIn);
		// Check for specific Arrows
		final boolean flag = !this.myFindAmmo(playerIn).isEmpty();

//		final ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStack, worldIn, playerIn, handIn, flag);
//		if (ret != null) {
//			return ret;
//		}

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return flag ? new ActionResult<>(EnumActionResult.PASS, itemStack) : new ActionResult<>(EnumActionResult.FAIL, itemStack);
		} else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
		}
	}
}
