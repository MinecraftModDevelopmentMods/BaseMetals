package com.mcmoddev.lib.block;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMoltenFluid extends net.minecraftforge.fluids.BlockFluidClassic {

	/**
	 *
	 * @param fluid
	 *            The fluid
	 */
	public BlockMoltenFluid(@Nonnull final Fluid fluid) {
		super(fluid, Material.LAVA);
	}

	@Override
	public void onEntityCollidedWithBlock(final World world, final BlockPos pos,
			final IBlockState state, final Entity entity) {
		super.onEntityCollidedWithBlock(world, pos, state, entity);
		if (world.isRemote || !(entity instanceof EntityLivingBase) || entity.isImmuneToFire()) return;
		if (entity.getEntityBoundingBox().minY >= pos.getY() + getFluidHeightForRender(world, pos)) return;

		// Vanilla shrinks the lava contact bounds and handles damage once the entity is deeper.
		if (!entity.isInLava()) {
			entity.attackEntityFrom(DamageSource.lava, 4.0F);
			entity.setFire(15);
		}
	}
}
