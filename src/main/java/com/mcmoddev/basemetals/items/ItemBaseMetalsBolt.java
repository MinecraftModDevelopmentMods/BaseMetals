package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.item.ItemMMDBolt;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/** Base Metals bolt whose material contributes to projectile damage. */
public final class ItemBaseMetalsBolt extends ItemMMDBolt {

	public ItemBaseMetalsBolt(final MMDMaterial material) {
		super(material);
	}

	@Override
	public EntityCustomBolt createBolt(final World world, final ItemStack ammunition,
			final EntityPlayer shooter) {
		final EntityCustomBolt projectile = super.createBolt(world, ammunition, shooter);
		MaterialRangedDamage.applyComponent(projectile, getMMDMaterial());
		return projectile;
	}
}
