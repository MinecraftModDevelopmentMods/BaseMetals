package com.mcmoddev.lib.fluids;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class CustomFluid extends Fluid {

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 * @param still
	 *            ResourceLocation for still fluid
	 * @param flowing
	 *            ResourceLocation for flowing fluid
	 */
	public CustomFluid(final String fluidName, final ResourceLocation still,
			final ResourceLocation flowing) {
		super(fluidName, still, flowing);
		final MMDMaterial material = Materials.getMaterialByName(fluidName);
		if (!material.isEmpty()) {
			this.color = material.getTintColor();
		}
		this.checkColor();
	}

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 * @param still
	 *            ResourceLocation for still fluid
	 * @param flowing
	 *            ResourceLocation for flowing fluid
	 * @param tintARGB
	 *            The color
	 */
	public CustomFluid(final String fluidName, final ResourceLocation still,
			final ResourceLocation flowing, final int tintARGB) {
		super(fluidName, still, flowing);
		this.color = tintARGB;
		this.checkColor();
	}

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 */
	public CustomFluid(final String fluidName) {
		this(fluidName,
				new ResourceLocation(Loader.instance().activeModContainer().getModId(),
						"blocks/molten_metal_still"),
				new ResourceLocation(Loader.instance().activeModContainer().getModId(),
						"blocks/molten_metal_flow"));
	}

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 * @param tintARGB
	 *            The color
	 */
	public CustomFluid(final String fluidName, final int tintARGB) {
		super(fluidName,
				new ResourceLocation(Loader.instance().activeModContainer().getModId(),
						"blocks/molten_metal_still"),
				new ResourceLocation(Loader.instance().activeModContainer().getModId(),
						"blocks/molten_metal_flow"));
		this.color = tintARGB;
		this.checkColor();
	}

	@Override
	public int getColor() {
		return this.color;
	}

	@Override
	public String getLocalizedName(final FluidStack stack) {
		final String s = this.getUnlocalizedName();
		return s == null ? ""
				: new TextComponentTranslation(String.format("%s.name", s)).getFormattedText();
	}

	private void checkColor() {
		if (((this.color >> 24) & 0xFF) == 0) {
			this.color |= 0xFF << 24;
		}
	}
}
