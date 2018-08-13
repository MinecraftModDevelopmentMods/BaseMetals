package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;

public class BlockMMDBookshelf extends net.minecraft.block.BlockBookshelf implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material The material the Bookshelf is made from
	 */
	public BlockMMDBookshelf(final MMDMaterial material) {
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
	}

	public void setFullBlock(final boolean val) {
		this.fullBlock = val;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullCube(final IBlockState state) {
		return this.fullBlock;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isOpaqueCube(final IBlockState state) {
		return this.fullBlock;
	}

}
