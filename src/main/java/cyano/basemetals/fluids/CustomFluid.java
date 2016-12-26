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
	 * @param fluidName The name of the fluid
	 * @param still ResourceLocation for still fluid
	 * @param flowing ResourceLocation for flowing fluid
	 * @param tintARGB The color
	 */
	public CustomFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int tintARGB) {
		super(fluidName, still, flowing);
		this.color = tintARGB;
	}

	@Override
	public int getColor() {
		return this.color;
	}
}
