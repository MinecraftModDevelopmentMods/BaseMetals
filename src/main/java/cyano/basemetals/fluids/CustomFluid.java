package cyano.basemetals.fluids;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class CustomFluid extends Fluid {

	private final int color;

	/**
	 * 
	 * @param fluidName
	 * @param still
	 * @param flowing
	 * @param tintARGB
	 */
	public CustomFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int tintARGB) {
		super(fluidName, still, flowing);
		this.color = tintARGB;
	}

	@Override
	public int getColor() {
		return color;
	}
}
