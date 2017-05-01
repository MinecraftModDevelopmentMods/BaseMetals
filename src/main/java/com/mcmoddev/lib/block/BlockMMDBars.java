package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * Metal Bars
 * 
 * @author DrCyano
 *
 */
public class BlockMMDBars extends net.minecraft.block.BlockPane implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the bars are made from
	 */
	public BlockMMDBars(MMDMaterial material) {
		super(material.getVanillaMaterial(), true);
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
