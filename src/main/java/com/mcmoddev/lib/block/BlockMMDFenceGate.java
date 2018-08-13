package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockPlanks;

public class BlockMMDFenceGate extends net.minecraft.block.BlockFenceGate implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material The material the Fence Gate is made from
	 */
	public BlockMMDFenceGate(final MMDMaterial material) {
		super(BlockPlanks.EnumType.OAK);
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
