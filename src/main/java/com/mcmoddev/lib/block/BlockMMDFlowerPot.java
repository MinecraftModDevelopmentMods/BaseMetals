package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockFlowerPot;

public class BlockMMDFlowerPot extends net.minecraft.block.BlockFlowerPot implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material The material the Flower Pot is made from
	 */
	public BlockMMDFlowerPot(final MMDMaterial material) {
		super();
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(CONTENTS, BlockFlowerPot.EnumFlowerType.EMPTY)
				.withProperty(LEGACY_DATA, Integer.valueOf(0)));
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
