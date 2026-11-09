package com.mcmoddev.basemetals.block;

import com.mcmoddev.lib.block.BlockMoltenFluid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

/** Makes shallow molten metal burn on contact. */
public final class BlockBaseMetalsMoltenFluid extends BlockMoltenFluid {
	public BlockBaseMetalsMoltenFluid(final Fluid fluid) {
		super(fluid);
	}

	@Override
	public void onEntityCollision(final World world, final BlockPos pos, final IBlockState state,
			final Entity entity) {
		super.onEntityCollision(world, pos, state, entity);
		if (world.isRemote || !(entity instanceof EntityLivingBase) || entity.isImmuneToFire()) {
			return;
		}

		final IBlockState above = world.getBlockState(pos.up());
		final double surface = pos.getY() + getFluidHeightForRender(world, pos, above);
		if (entity.getEntityBoundingBox().minY >= surface || entity.isInLava()) {
			return;
		}

		// Minecraft already handles deeper lava contact.
		entity.attackEntityFrom(DamageSource.LAVA, 4.0F);
		entity.setFire(15);
	}
}
