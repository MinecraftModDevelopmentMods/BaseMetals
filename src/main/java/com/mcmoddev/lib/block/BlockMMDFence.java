package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockMMDFence extends net.minecraft.block.BlockFence implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material The material the Fence is made from
	 */
	public BlockMMDFence(final MMDMaterial material) {
		super(material.getVanillaMaterial(), material.getVanillaMaterial().getMaterialMapColor());
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
	}

	// We don't specifically need this, but it does mean less logic being run on each check
	@Override
	public boolean canPlaceTorchOnTop(final IBlockState state, final IBlockAccess world,
			final BlockPos pos) {
		return true;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
