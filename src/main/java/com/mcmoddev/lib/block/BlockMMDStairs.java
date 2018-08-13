package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDStairs extends net.minecraft.block.BlockStairs implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the stairs are made from
	 */
	public BlockMMDStairs(final MMDMaterial material) {
		super(material.getBlock(Names.BLOCK).getDefaultState());
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
