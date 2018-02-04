package com.mcmoddev.lib.item;

import javax.annotation.Nullable;

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
public class ItemCrossbow extends ItemBow {

	/**
	 * Called when the player stops using an Item (stops holding the right mouse button).
	 */
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			final EntityPlayer entityPlayer = (EntityPlayer) entityLiving;
			final boolean flag = entityPlayer.capabilities.isCreativeMode || (EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0);
			// Check for specific Bolts
			ItemStack itemStack = this.myFindAmmo(entityPlayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityPlayer, i, (itemStack != null) || flag);
			if (i < 0) {
				return;
			}

			if ((itemStack != null) || flag) {
				if (itemStack == null) {
					itemStack = getBolt();
					if (itemStack == null)
						return; // if its still null at this point, there is something seriously wrong, just bug out
				}

				final float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					final boolean flag1 = entityPlayer.capabilities.isCreativeMode || (itemStack.getItem() instanceof ItemBolt ? ((ItemBolt) itemStack.getItem()).isInfinite(itemStack, stack, entityPlayer) : false);

					if (!worldIn.isRemote) {
						final ItemBolt itemBolt = ((ItemBolt) (itemStack.getItem() instanceof ItemBolt ? itemStack.getItem() : Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.BOLT)));
						final EntityCustomBolt entityBolt = itemBolt.createBolt(worldIn, itemStack, entityPlayer);
						entityBolt.setAim(entityPlayer, entityPlayer.rotationPitch, entityPlayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

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

						if (flag1) {
							entityBolt.pickupStatus = EntityCustomBolt.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityBolt);
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

	private ItemStack getBolt() {
		for (MMDMaterial material : Materials.getAllMaterials()) {
			if (material.hasItem(Names.BOLT))
				return new ItemStack(material.getItem(Names.BOLT));
		}
		return null;
	}

	private ItemStack myFindAmmo(EntityPlayer player) {
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

			return null;
		}
	}

	@Override
	protected boolean isArrow(@Nullable ItemStack stack) {
		return false;
	}

	protected boolean isBolt(@Nullable ItemStack stack) {
		return (stack != null) && (stack.getItem() instanceof ItemBolt);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final boolean flag = this.myFindAmmo(playerIn) != null;

		if (!playerIn.capabilities.isCreativeMode && !flag) {
			return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
		} else {
			playerIn.setActiveHand(handIn);
			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}
	}
}
