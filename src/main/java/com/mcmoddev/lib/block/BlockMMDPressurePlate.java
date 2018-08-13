package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDPressurePlate extends net.minecraft.block.BlockPressurePlate
		implements IMMDObject {

	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the pressure plate is made from
	 */
	public BlockMMDPressurePlate(final MMDMaterial material) {
		super(material.getVanillaMaterial(), Sensitivity.MOBS);
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
