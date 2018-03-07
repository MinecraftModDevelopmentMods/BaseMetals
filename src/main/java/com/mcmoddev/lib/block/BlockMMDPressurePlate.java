package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDPressurePlate extends net.minecraft.block.BlockPressurePlate implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the pressure plate is made from
	 */
	public BlockMMDPressurePlate(final MMDMaterial material) {
		super(material.getVanillaMaterial(), Sensitivity.MOBS);
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(), this.material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
