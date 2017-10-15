package com.mcmoddev.lib.block;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMoltenFluid extends net.minecraftforge.fluids.BlockFluidClassic {

	/**
	 *
	 * @param fluid
	 *            The fluid
	 */
	public BlockMoltenFluid(@Nonnull Fluid fluid) {
		super(fluid, Material.LAVA);

	}
}
