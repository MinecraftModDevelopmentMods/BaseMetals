package com.mcmoddev.lib.item;

import javax.annotation.Nonnull;

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
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			final boolean flag = entityPlayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			// Check for specific Arrows
			ItemStack itemStack = this.myFindAmmo(entityPlayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityPlayer, i, (!itemStack.isEmpty()) || flag);
			if (i < 0) {
				return;
			}

			if (!itemStack.isEmpty() || flag) {
				if (itemStack.isEmpty()) {
					itemStack = new ItemStack(Items.ARROW);
				}

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					final boolean flag1 = entityPlayer.capabilities.isCreativeMode || (itemStack.getItem() instanceof ItemArrow && ((ItemArrow) itemStack.getItem()).isInfinite(itemStack, stack, entityPlayer));

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

						if (flag1 || entityPlayer.capabilities.isCreativeMode && (itemStack.getItem() == Items.SPECTRAL_ARROW || itemStack.getItem() == Items.TIPPED_ARROW)) {
							entityArrow.pickupStatus = EntityCustomArrow.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityArrow);
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

	private ItemStack myFindAmmo(EntityPlayer player) {
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

			return ItemStack.EMPTY;
		}
	}

	@Override
	protected boolean isArrow(@Nonnull ItemStack stack) {
		// Changed ItemArrow to ItemMMDArrow
		// TODO: Do we really need this?
		return stack.getItem() instanceof ItemMMDArrow;
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final ItemStack itemStack = playerIn.getHeldItem(handIn);
		// Check for specific Arrows
		final boolean flag = !this.myFindAmmo(playerIn).isEmpty();

		final ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStack, worldIn, playerIn, handIn, flag);
		if (ret != null) {
			return ret;
		}

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return flag ? new ActionResult<>(EnumActionResult.PASS, itemStack) : new ActionResult<>(EnumActionResult.FAIL, itemStack);
		} else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
		}
	}
}
