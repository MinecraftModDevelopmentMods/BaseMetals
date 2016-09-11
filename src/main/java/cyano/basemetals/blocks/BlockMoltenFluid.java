package cyano.basemetals.blocks;

import javax.annotation.Nonnull;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMoltenFluid extends BlockFluidClassic {

	/**
	 *
	 * @param fluid
	 */
	public BlockMoltenFluid(Fluid fluid) {
		super(fluid, Material.LAVA);

		// setCreativeTab(TinkerRegistry.tabSmeltery);
	}

	@Nonnull
	@Override
	public String getUnlocalizedName() {
		final Fluid fluid = FluidRegistry.getFluid(this.fluidName);
		if(fluid != null)
			return fluid.getUnlocalizedName();
		return super.getUnlocalizedName();
	}
}
