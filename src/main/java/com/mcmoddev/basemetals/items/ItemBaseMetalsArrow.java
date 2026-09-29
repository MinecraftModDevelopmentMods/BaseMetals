package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.entity.EntityCustomArrow;
import com.mcmoddev.lib.item.ItemMMDArrow;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Base Metals arrow whose material contributes to projectile damage. */
public final class ItemBaseMetalsArrow extends ItemMMDArrow {

	public ItemBaseMetalsArrow(final MMDMaterial material) {
		super(material);
	}

	@Override
	public EntityCustomArrow createArrow(final World world, final ItemStack ammunition,
			final EntityPlayer shooter) {
		final ItemStack projectileAmmunition = ammunition.copy();
		projectileAmmunition.setCount(1);
		final EntityCustomArrow projectile = super.createArrow(
				world, projectileAmmunition, shooter);
		MaterialRangedDamage.applyComponent(projectile, getMMDMaterial());
		return projectile;
	}

	@Override
	public EntityArrow createArrow(final World world, final ItemStack ammunition,
			final EntityLivingBase shooter) {
		if (shooter instanceof EntityPlayer) {
			return createArrow(world, ammunition, (EntityPlayer) shooter);
		}
		final EntityArrow projectile = super.createArrow(world, ammunition, shooter);
		MaterialRangedDamage.applyComponent(projectile, getMMDMaterial());
		return projectile;
	}
}
