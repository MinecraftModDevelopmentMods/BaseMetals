package com.mcmoddev.lib.item;

import javax.annotation.Nullable;

import com.mcmoddev.lib.entity.EntityCustomArrow;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
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
public class ItemBow extends net.minecraft.item.ItemBow {

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 */
	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving, final int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			final boolean flag = entityPlayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			// Check for specific Arrows
			ItemStack itemStack = this.myFindAmmo(entityPlayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityPlayer, i, (itemStack != null) || flag);
			if (i < 0) {
				return;
			}

			if ((itemStack != null) || flag) {
				if (itemStack == null) {
					itemStack = new ItemStack(Items.ARROW);
				}

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					// OLD
					//final boolean flag1 = flag && (itemstack.getItem() instanceof ItemArrow); // Forge: Fix consuming custom arrows.
					// NEW
					final boolean flag1 = entityPlayer.capabilities.isCreativeMode || (itemStack.getItem() instanceof ItemArrow ? ((ItemArrow) itemStack.getItem()).isInfinite(itemStack, stack, entityPlayer) : false);

					if (!worldIn.isRemote) {
						final ItemArrow itemArrow = ((ItemArrow) (itemStack.getItem() instanceof ItemArrow ? itemStack.getItem() : Items.ARROW));
						final EntityArrow entityArrow = itemArrow.createArrow(worldIn, itemStack, entityPlayer);
						entityArrow.setAim(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						if (f == 1.0F) {
							entityArrow.setIsCritical(true);
						}

						final int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							entityArrow.setDamage(entityArrow.getDamage() + ((double) j * 0.5D) + 0.5D);
						}

						final int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0) {
							entityArrow.setKnockbackStrength(k);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
							entityArrow.setFire(100);
						}

						stack.damageItem(1, entityPlayer);

						if (flag1) {
							entityArrow.pickupStatus = EntityCustomArrow.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityArrow);
					}

					worldIn.playSound((EntityPlayer) null, entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f * 0.5F));

					if (!flag1) {
						--itemStack.stackSize;

						if (itemStack.stackSize == 0) {
							entityPlayer.inventory.deleteStack(itemStack);
						}
					}

					entityPlayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	// TODO: This may not be needed
	private ItemStack myFindAmmo(final EntityPlayer player) {
		if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				final ItemStack itemStack = player.inventory.getStackInSlot(i);

				if (this.isArrow(itemStack)) {
					return itemStack;
				}
			}

			return null;
		}
	}

	@Override
	protected boolean isArrow(@Nullable final ItemStack stack) {
		// Changed ItemArrow to ItemMMDArrow
		return (stack != null) && (stack.getItem() instanceof ItemMMDArrow);
	}

	// TODO: This may not be needed
	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn, final EnumHand handIn) {
		final boolean flag = this.myFindAmmo(playerIn) != null;

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		} else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
}
