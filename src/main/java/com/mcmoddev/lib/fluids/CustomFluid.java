package com.mcmoddev.lib.fluids;

import com.mcmoddev.basemetals.init.Materials;

import net.minecraft.util.text.translation.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class CustomFluid extends Fluid {

	private int color;

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 * @param still
	 *            ResourceLocation for still fluid
	 * @param flowing
	 *            ResourceLocation for flowing fluid
	 */
	public CustomFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
		if (Materials.getMaterialByName(fluidName) != null) {
			this.color = Materials.getMaterialByName(fluidName).getTintColor();
		}
		if (((this.color >> 24) & 0xFF) == 0) {
			this.color |= 0xFF << 24;
		}
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
	public CustomFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int tintARGB) {
		super(fluidName, still, flowing);
		this.color = tintARGB;
		if (((this.color >> 24) & 0xFF) == 0) {
			this.color |= 0xFF << 24;
		}
	}

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 */
	public CustomFluid(String fluidName) {
		super(fluidName,
				new ResourceLocation(Loader.instance().activeModContainer().getModId(), "blocks/molten_metal_still"),
				new ResourceLocation(Loader.instance().activeModContainer().getModId(), "blocks/molten_metal_flow"));
		if (Materials.getMaterialByName(fluidName) != null) {
			this.color = Materials.getMaterialByName(fluidName).getTintColor();
		}
		if (((this.color >> 24) & 0xFF) == 0) {
			this.color |= 0xFF << 24;
		}
	}

	/**
	 *
	 * @param fluidName
	 *            The name of the fluid
	 * @param tintARGB
	 *            The color
	 */
	public CustomFluid(String fluidName, int tintARGB) {
		super(fluidName,
				new ResourceLocation(Loader.instance().activeModContainer().getModId(), "blocks/molten_metal_still"),
				new ResourceLocation(Loader.instance().activeModContainer().getModId(), "blocks/molten_metal_flow"));
		this.color = tintARGB;
		if (((this.color >> 24) & 0xFF) == 0) {
			this.color |= 0xFF << 24;
		}
	}

	@Override
	public int getColor() {
		return this.color;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getLocalizedName(FluidStack stack) {
		String s = this.getUnlocalizedName();
		return s == null ? "" : I18n.translateToLocal(String.format("%s.name", s));
	}
}
