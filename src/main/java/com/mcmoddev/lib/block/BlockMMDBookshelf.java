package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;

public class BlockMMDBookshelf extends net.minecraft.block.BlockBookshelf implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 */
	public BlockMMDBookshelf(final MMDMaterial material) {
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(),
				this.material.getRequiredHarvestLevel());
	}

	public void setFullBlock(final boolean val) {
		this.fullBlock = val;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
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
