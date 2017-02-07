package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

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

	private static boolean initDone = false;

	private Fluids() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if( Options.enableCoal ) {
			addFluid(Materials.vanilla_coal, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanilla_coal);
		}
		
		if (Options.enableAdamantine) {
			addFluid(Materials.adamantine, 2000, 10000, 769, 10);
			addFluidBlock(Materials.adamantine);
		}

		if (Options.enableAntimony) {
			addFluid(Materials.antimony, 2000, 10000, 769, 10);
			addFluidBlock(Materials.antimony);
		}

		if (Options.enableAquarium) {
			addFluid(Materials.aquarium, 2000, 10000, 769, 10);
			addFluidBlock(Materials.aquarium);
		}

		if (Options.enableBismuth) {
			addFluid(Materials.bismuth, 2000, 10000, 769, 10);
			addFluidBlock(Materials.bismuth);
		}

		if (Options.enableBrass) {
			addFluid(Materials.brass, 2000, 10000, 769, 10);
			addFluidBlock(Materials.brass);
		}

		if (Options.enableBronze) {
			addFluid(Materials.bronze, 2000, 10000, 769, 10);
			addFluidBlock(Materials.bronze);
		}

		if (Options.enableColdIron) {
			addFluid(Materials.coldiron, 2000, 10000, 769, 10);
			addFluidBlock(Materials.coldiron);
		}

		if (Options.enableCopper) {
			addFluid(Materials.copper, 2000, 10000, 769, 10);
			addFluidBlock(Materials.copper);
		}

		if (Options.enableCupronickel) {
			addFluid(Materials.cupronickel, 2000, 10000, 769, 10);
			addFluidBlock(Materials.cupronickel);
		}

		if (Options.enableElectrum) {
			addFluid(Materials.electrum, 2000, 10000, 769, 10);
			addFluidBlock(Materials.electrum);
		}

		if (Options.enableInvar) {
			addFluid(Materials.invar, 2000, 10000, 769, 10);
			addFluidBlock(Materials.invar);
		}

		if (Options.enableLead) {
			addFluid(Materials.lead, 2000, 10000, 769, 10);
			addFluidBlock(Materials.lead);
		}

		if (Options.enableMercury) {
			addFluid("mercury", 13594, 2000, 769, 0);
			addFluidBlock("mercury");
		}

		if (Options.enableMithril) {
			addFluid(Materials.mithril, 2000, 10000, 769, 10);
			addFluidBlock(Materials.mithril);
		}

		if (Options.enableNickel) {
			addFluid(Materials.nickel, 2000, 10000, 769, 10);
			addFluidBlock(Materials.nickel);
		}

		if (Options.enablePewter) {
			addFluid(Materials.pewter, 2000, 10000, 769, 10);
			addFluidBlock(Materials.pewter);
		}

		if (Options.enablePlatinum) {
			addFluid(Materials.platinum, 2000, 10000, 769, 10);
			addFluidBlock(Materials.platinum);
		}

		if (Options.enableSilver) {
			addFluid(Materials.silver, 2000, 10000, 769, 10);
			addFluidBlock(Materials.silver);
		}

		if (Options.enableStarSteel) {
			addFluid(Materials.starsteel, 2000, 10000, 769, 10);
			addFluidBlock(Materials.starsteel);
		}

		if (Options.enableSteel) {
			addFluid(Materials.steel, 2000, 10000, 769, 10);
			addFluidBlock(Materials.steel);
		}

		if (Options.enableTin) {
			addFluid(Materials.tin, 2000, 10000, 769, 10);
			addFluidBlock(Materials.tin);
		}

		if (Options.enableZinc) {
			addFluid(Materials.zinc, 2000, 10000, 769, 10);
			addFluidBlock(Materials.zinc);
		}

		initDone = true;
	}
}
