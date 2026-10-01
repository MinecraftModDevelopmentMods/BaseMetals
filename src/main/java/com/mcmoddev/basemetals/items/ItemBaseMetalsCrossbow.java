package com.mcmoddev.basemetals.items;

import javax.annotation.Nullable;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemBolt;
import com.mcmoddev.lib.item.ItemMMDCrossbow;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Fires bolts on the server so their hits deal damage.
 * MMDLib's crossbow spawns them only on the client.
 */
public final class ItemBaseMetalsCrossbow extends ItemMMDCrossbow {

	public ItemBaseMetalsCrossbow(final MMDMaterial material) {
		super(material);
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack crossbow, final World world,
			final EntityLivingBase user, final int timeLeft) {
		if (!(user instanceof EntityPlayer)) {
			return;
		}

		final EntityPlayer player = (EntityPlayer) user;
		final boolean hasInfiniteAmmo = player.capabilities.isCreativeMode
				|| EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, crossbow) > 0;
		ItemStack ammunition = findBolt(player);
		int charge = this.getMaxItemUseDuration(crossbow) - timeLeft;

		charge = ForgeEventFactory.onArrowLoose(crossbow, world, player, charge,
				!ammunition.isEmpty() || hasInfiniteAmmo);

		if (charge < 0 || ammunition.isEmpty() && !hasInfiniteAmmo) {
			return;
		}

		if (ammunition.isEmpty()) {
			ammunition = defaultBolt();

			if (ammunition.isEmpty()) {
				return;
			}
		}

		final float velocity = getArrowVelocity(charge);

		if (velocity < 0.1F) {
			return;
		}

		final ItemBolt boltItem = (ItemBolt) ammunition.getItem();
		final boolean infiniteShot = player.capabilities.isCreativeMode
				|| boltItem.isInfinite(ammunition, crossbow, player);

		if (!world.isRemote) {
			final ItemStack projectileAmmunition = ammunition.copy();

			projectileAmmunition.setCount(1);

			final EntityCustomBolt projectile = boltItem.createBolt(
					world, projectileAmmunition, player);

			MaterialRangedDamage.applyComponent(projectile, getMMDMaterial());
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F,
					velocity * 3.0F, 1.0F);

			if (velocity == 1.0F) {
				projectile.setIsCritical(true);
			}

			final int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, crossbow);

			if (power > 0) {
				projectile.setDamage(projectile.getDamage() + power * 0.5D + 0.5D);
			}

			final int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, crossbow);

			if (punch > 0) {
				projectile.setKnockbackStrength(punch);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, crossbow) > 0) {
				projectile.setFire(100);
			}

			crossbow.damageItem(1, player);

			if (infiniteShot) {
				projectile.pickupStatus = EntityCustomBolt.PickupStatus.CREATIVE_ONLY;
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

	private static ItemStack defaultBolt() {
		final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);

		return iron.isEmpty() || !iron.hasItem(Names.BOLT)
				? ItemStack.EMPTY
				: new ItemStack(iron.getItem(Names.BOLT));
	}

	private static ItemStack findBolt(final EntityPlayer player) {
		if (isBoltStack(player.getHeldItem(EnumHand.OFF_HAND))) {
			return player.getHeldItem(EnumHand.OFF_HAND);
		}

		if (isBoltStack(player.getHeldItem(EnumHand.MAIN_HAND))) {
			return player.getHeldItem(EnumHand.MAIN_HAND);
		}

		for (int slot = 0; slot < player.inventory.getSizeInventory(); slot++) {
			final ItemStack stack = player.inventory.getStackInSlot(slot);

			if (isBoltStack(stack)) {
				return stack;
			}
		}

		return ItemStack.EMPTY;
	}

	private static boolean isBoltStack(@Nullable final ItemStack stack) {
		return stack != null && !stack.isEmpty() && stack.getItem() instanceof ItemBolt;
	}
}
