package com.mcmoddev.basemetals.items;

import javax.annotation.Nullable;

import com.mcmoddev.lib.item.ItemMMDBow;
import com.mcmoddev.lib.material.MMDMaterial;

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
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/** Base Metals bow whose material contributes to projectile damage. */
public final class ItemBaseMetalsBow extends ItemMMDBow {

	public ItemBaseMetalsBow(final MMDMaterial material) {
		super(material);
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack bow, final World world,
			final EntityLivingBase user, final int timeLeft) {
		if (!(user instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) user;
		final boolean hasInfiniteAmmo = player.capabilities.isCreativeMode
				|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow) > 0;
		ItemStack ammunition = findArrow(player);
		int charge = this.getMaxItemUseDuration(bow) - timeLeft;
		charge = ForgeEventFactory.onArrowLoose(bow, world, player, charge,
				!ammunition.isEmpty() || hasInfiniteAmmo);
		if (charge < 0 || ammunition.isEmpty() && !hasInfiniteAmmo) {
			return;
		}

		if (ammunition.isEmpty()) {
			ammunition = new ItemStack(Items.ARROW);
		}

		final float velocity = getArrowVelocity(charge);
		if (velocity < 0.1F) {
			return;
		}

		final ItemArrow arrowItem = ammunition.getItem() instanceof ItemArrow
				? (ItemArrow) ammunition.getItem() : (ItemArrow) Items.ARROW;
		final boolean infiniteShot = player.capabilities.isCreativeMode
				|| arrowItem.isInfinite(ammunition, bow, player);
		if (!world.isRemote) {
			final EntityArrow projectile = arrowItem.createArrow(world, ammunition, player);
			MaterialRangedDamage.applyComponent(projectile, getMMDMaterial());
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F,
					velocity * 3.0F, 1.0F);
			if (velocity == 1.0F) {
				projectile.setIsCritical(true);
			}

			final int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);
			if (power > 0) {
				projectile.setDamage(projectile.getDamage() + power * 0.5D + 0.5D);
			}
			final int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);
			if (punch > 0) {
				projectile.setKnockbackStrength(punch);
			}
			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
				projectile.setFire(100);
			}

			bow.damageItem(1, player);
			if (infiniteShot) {
				projectile.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
			}
			world.spawnEntity(projectile);
		}

		world.playSound(null, player.posX, player.posY, player.posZ,
				SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
				1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + velocity * 0.5F);
		if (!infiniteShot) {
			ammunition.shrink(1);
			if (ammunition.isEmpty()) {
				player.inventory.deleteStack(ammunition);
			}
		}
		player.addStat(StatList.getObjectUseStats(this));
	}

	private static ItemStack findArrow(final EntityPlayer player) {
		if (isArrowStack(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		}
		if (isArrowStack(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}
		for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
			final ItemStack stack = player.inventory.getStackInSlot(slot);
			if (isArrowStack(stack)) {
				return stack;
			}
		}
		return ItemStack.EMPTY;
	}

	private static boolean isArrowStack(@Nullable final ItemStack stack) {
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemArrow;
	}
}
