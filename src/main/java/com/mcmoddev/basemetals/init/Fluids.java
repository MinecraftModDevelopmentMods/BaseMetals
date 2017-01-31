package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.util.Config.Options;

import cyano.basemetals.init.Materials;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

/**
 * This class initializes all fluids in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Fluids extends com.mcmoddev.lib.init.Fluids {

	static {
		FluidRegistry.enableUniversalBucket();
	}

	public static Fluid fluidMercury = null;
	public static BlockFluidBase fluidBlockMercury = null;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		MetalMaterial material;

		// fluids and fluid blocks
		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_AQUARIUM) {
			material = Materials.aquarium;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BRASS) {
			material = Materials.brass;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_BRONZE) {
			material = Materials.bronze;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_COPPER) {
			material = Materials.copper;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_CUPRONICKEL) {
			material = Materials.cupronickel;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ELECTRUM) {
			material = Materials.electrum;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_INVAR) {
			material = Materials.invar;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_LEAD) {
			material = Materials.lead;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_MERCURY) {
//			material = Materials.mercury;

			fluidMercury = addFluid("mercury", 13594, 2000, 769, 0);
			fluidBlockMercury = addFluidBlock("mercury");
		}
		if (Options.ENABLE_MITHRIL) {
			material = Materials.mithril;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_PEWTER) {
			material = Materials.pewter;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_SILVER) {
			material = Materials.silver;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_STEEL) {
			material = Materials.steel;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_TIN) {
			material = Materials.tin;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}
		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;

			addFluid(material, 2000, 10000, 769, 10);
			addFluidBlock(material);
		}

		initDone = true;
	}
}
