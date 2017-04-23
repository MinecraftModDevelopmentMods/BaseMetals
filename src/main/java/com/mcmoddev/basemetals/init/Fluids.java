package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

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

		if (Options.enableCharcoal) {
			addFluid(Materials.vanillaCharcoal, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaCharcoal);
		}

		if (Options.enableCoal) {
			addFluid(Materials.vanillaCoal, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaCoal);
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

		if (Options.enableDiamond) {
			addFluid(Materials.vanillaDiamond, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaDiamond);
		}

		if (Options.enableElectrum) {
			addFluid(Materials.electrum, 2000, 10000, 769, 10);
			addFluidBlock(Materials.electrum);
		}

		if (Options.enableEmerald) {
			addFluid(Materials.vanillaEmerald, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaEmerald);
		}

		if (Options.enableEnder) {
			addFluid(Materials.vanillaEnder, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaEnder);
		}

		if (Options.enableGold) {
			addFluid(Materials.vanillaGold, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaGold);
		}

		if (Options.enableInvar) {
			addFluid(Materials.invar, 2000, 10000, 769, 10);
			addFluidBlock(Materials.invar);
		}

		if (Options.enableIron) {
			addFluid(Materials.vanillaIron, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaIron);
		}

		if (Options.enableLead) {
			addFluid(Materials.lead, 2000, 10000, 769, 10);
			addFluidBlock(Materials.lead);
		}

		if (Options.enableMercury) {
			addFluid(Materials.mercury, 13594, 2000, 769, 0);
			addFluidBlock(Materials.mercury);
		}

		if (Options.enableMithril) {
			addFluid(Materials.mithril, 2000, 10000, 769, 10);
			addFluidBlock(Materials.mithril);
		}

		if (Options.enableNickel) {
			addFluid(Materials.nickel, 2000, 10000, 769, 10);
			addFluidBlock(Materials.nickel);
		}

		if (Options.enableObsidian) {
			addFluid(Materials.vanillaObsidian, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaObsidian);
		}

		if (Options.enablePewter) {
			addFluid(Materials.pewter, 2000, 10000, 769, 10);
			addFluidBlock(Materials.pewter);
		}

		if (Options.enablePlatinum) {
			addFluid(Materials.platinum, 2000, 10000, 769, 10);
			addFluidBlock(Materials.platinum);
		}

		if (Options.enablePrismarine) {
			addFluid(Materials.vanillaPrismarine, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaPrismarine);
		}

		if (Options.enableRedstone) {
			addFluid(Materials.vanillaRedstone, 2000, 10000, 769, 10);
			addFluidBlock(Materials.vanillaRedstone);
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
