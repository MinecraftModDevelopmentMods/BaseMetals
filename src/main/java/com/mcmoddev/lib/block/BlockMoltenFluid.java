package com.mcmoddev.lib.block;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
// import net.minecraftforge.fluids.FluidRegistry;

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

		// setCreativeTab(TinkerRegistry.tabSmeltery);
	}

/*	@Nonnull
	@Override
	public String getUnlocalizedName() {
		final Fluid fluid = FluidRegistry.getFluid(this.fluidName);
		if (fluid != null)
			return fluid.getUnlocalizedName();
		return super.getUnlocalizedName();
	}
*/}
