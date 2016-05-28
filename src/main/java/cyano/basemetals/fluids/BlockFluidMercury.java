package cyano.basemetals.fluids;

import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;

public class BlockFluidMercury extends net.minecraftforge.fluids.BlockFluidClassic {

	public BlockFluidMercury(Fluid fluid, Material material) {
		super(fluid, material);
		super.setDensity(13594);
	}	
}
