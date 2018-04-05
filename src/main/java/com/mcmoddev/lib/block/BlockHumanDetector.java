package com.mcmoddev.lib.block;

import java.util.List;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A pressure plate that only activates when a player steps on it.
 */
public class BlockHumanDetector extends net.minecraft.block.BlockPressurePlate {

	public BlockHumanDetector() {
		super(Material.IRON, Sensitivity.MOBS);
	}

	@Override
	protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
		final AxisAlignedBB axisalignedbb = PRESSURE_AABB.offset(pos);
		final List<? extends Entity> list = worldIn
				.<Entity>getEntitiesWithinAABB(EntityPlayer.class, axisalignedbb);

		if (!list.isEmpty()) {
			return 15;
		}
		return 0;
	}
}
