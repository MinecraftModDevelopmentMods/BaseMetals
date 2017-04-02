package com.mcmoddev.lib.block;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockPressurePlate;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDPressurePlate extends BlockPressurePlate implements IMMDObject {

	final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the pressure plate is made from
	 */
	public BlockMMDPressurePlate(MMDMaterial material) {
		super(Material.IRON, BlockPressurePlate.Sensitivity.MOBS);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	public MMDMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MMDMaterial getMetalMaterial() {
		return this.material;
	}
}
