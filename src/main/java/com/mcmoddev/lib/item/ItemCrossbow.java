package com.mcmoddev.lib.item;

import javax.annotation.Nullable;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.init.Materials;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
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
public class ItemCrossbow extends ItemBow {

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			final boolean flag = entityplayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, (itemstack != null) || flag);
			if (i < 0) {
				return;
			}

			if ((itemstack != null) || flag) {
				if (itemstack == null) {
					// FIXME - this is potentially unreliable
					itemstack = new ItemStack(Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.BOLT));
				}

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					// OLD
					//final boolean flag1 = flag && (itemstack.getItem() instanceof ItemBolt); // Forge: Fix consuming custom arrows.
					// NEW
					final boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemBolt ? ((ItemBolt) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer) : false);

					if (!worldIn.isRemote) {
						// TODO: FIXME Using Materials.vanilla_iron.arrow, MC uses Items.ARROW
//						final ItemArrow itemArrow = ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Materials.vanilla_iron.bolt));
						final ItemBolt itemBolt = (ItemBolt) ((ItemBolt) (itemstack.getItem() instanceof ItemBolt ? itemstack.getItem() : Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.BOLT)));
						final EntityCustomBolt entityBolt = itemBolt.createBolt(worldIn, itemstack, entityplayer);
						entityBolt.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

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

						stack.damageItem(1, entityplayer);

						if (flag1) {
							entityBolt.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityBolt);
					}

					worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, (1.0F / ((itemRand.nextFloat() * 0.4F) + 1.2F)) + (f * 0.5F));

					if (!flag1) {
						itemstack.setCount(itemstack.getCount() - 1);

						if (itemstack.getCount() == 0) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}

	// TODO: This may not be needed
	private ItemStack findAmmo(EntityPlayer player) {
		if (this.isBolt(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		} else if (this.isBolt(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		} else {
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
				final ItemStack itemstack = player.inventory.getStackInSlot(i);

				if (this.isBolt(itemstack)) {
					return itemstack;
				}
			}

			return null;
		}
	}

	@Override
	protected boolean isArrow(@Nullable ItemStack stack) {
		return false;
	}

	protected boolean isBolt(@Nullable ItemStack stack) {
		// Changed ItemArrow to ItemMetalArrow
		return (stack != null) && (stack.getItem() instanceof ItemBolt);
	}

	// TODO: This may not be needed
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
		final boolean flag = this.findAmmo(playerIn) != null;

		if (!playerIn.capabilities.isCreativeMode && !flag)
			return new ActionResult<>(EnumActionResult.FAIL, playerIn.getHeldItemMainhand());
		else {
			playerIn.setActiveHand(hand);
			return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItemMainhand());
		}
	}
}
