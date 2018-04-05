package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMMDFence extends net.minecraft.block.BlockFence implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 */
	public BlockMMDFence(final MMDMaterial material) {
		super(material.getVanillaMaterial(), material.getVanillaMaterial().getMaterialMapColor());
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(),
				this.material.getRequiredHarvestLevel());
	}

	// We don't specifically need this, but it does mean less logic being run on each check
	@Override
	public boolean canPlaceTorchOnTop(final IBlockState state, final IBlockAccess world,
			final BlockPos pos) {
		return true;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
