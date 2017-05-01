package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockBookshelf;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;

public class BlockMMDBookshelf extends BlockBookshelf implements IMMDObject {

	final MMDMaterial material;
	private boolean fullBlock = true;

	public BlockMMDBookshelf(MMDMaterial material) {
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("axe", material.getRequiredHarvestLevel());
	}

	public void setFullBlock(boolean val) {
		this.fullBlock = val;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return this.fullBlock;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return this.fullBlock;
	}

}
