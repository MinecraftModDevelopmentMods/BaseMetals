package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
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
			addFluid(Materials.getMaterialByName(MaterialNames.ADAMANTINE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
		}

		if (Options.enableAntimony) {
			addFluid(Materials.getMaterialByName(MaterialNames.ANTIMONY), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ANTIMONY));
		}

		if (Options.enableAquarium) {
			addFluid(Materials.getMaterialByName(MaterialNames.AQUARIUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.AQUARIUM));
		}

		if (Options.enableBismuth) {
			addFluid(Materials.getMaterialByName(MaterialNames.BISMUTH), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BISMUTH));
		}

		if (Options.enableBrass) {
			addFluid(Materials.getMaterialByName(MaterialNames.BRASS), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BRASS));
		}

		if (Options.enableBronze) {
			addFluid(Materials.getMaterialByName(MaterialNames.BRONZE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BRONZE));
		}

		if (Options.enableCharcoal) {
			addFluid(Materials.getMaterialByName(MaterialNames.CHARCOAL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.CHARCOAL));
		}

		if (Options.enableCoal) {
			addFluid(Materials.getMaterialByName(MaterialNames.COAL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COAL));
		}

		if (Options.enableColdIron) {
			addFluid(Materials.getMaterialByName(MaterialNames.COLDIRON), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COLDIRON));
		}

		if (Options.enableCopper) {
			addFluid(Materials.getMaterialByName(MaterialNames.COPPER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COPPER));
		}

		if (Options.enableCupronickel) {
			addFluid(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.CUPRONICKEL));
		}

		if (Options.enableDiamond) {
			addFluid(Materials.getMaterialByName(MaterialNames.DIAMOND), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.DIAMOND));
		}

		if (Options.enableElectrum) {
			addFluid(Materials.getMaterialByName(MaterialNames.ELECTRUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ELECTRUM));
		}

		if (Options.enableEmerald) {
			addFluid(Materials.getMaterialByName(MaterialNames.EMERALD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.EMERALD));
		}

		if (Options.enableEnder) {
			addFluid(Materials.getMaterialByName(MaterialNames.ENDER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ENDER));
		}

		if (Options.enableGold) {
			addFluid(Materials.getMaterialByName(MaterialNames.GOLD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.GOLD));
		}

		if (Options.enableInvar) {
			addFluid(Materials.getMaterialByName(MaterialNames.INVAR), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.INVAR));
		}

		if (Options.enableIron) {
			addFluid(Materials.getMaterialByName(MaterialNames.IRON), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.IRON));
		}

		if (Options.enableLead) {
			addFluid(Materials.getMaterialByName(MaterialNames.LEAD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.LEAD));
		}

		if (Options.enableMercury) {
			addFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 13594, 2000, 769, 0);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.MERCURY));
		}

		if (Options.enableMithril) {
			addFluid(Materials.getMaterialByName(MaterialNames.MITHRIL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.MITHRIL));
		}

		if (Options.enableNickel) {
			addFluid(Materials.getMaterialByName(MaterialNames.NICKEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.NICKEL));
		}

		if (Options.enableObsidian) {
			addFluid(Materials.getMaterialByName(MaterialNames.OBSIDIAN), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.OBSIDIAN));
		}

		if (Options.enablePewter) {
			addFluid(Materials.getMaterialByName(MaterialNames.PEWTER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PEWTER));
		}

		if (Options.enablePlatinum) {
			addFluid(Materials.getMaterialByName(MaterialNames.PLATINUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PLATINUM));
		}

		if (Options.enablePrismarine) {
			addFluid(Materials.getMaterialByName(MaterialNames.PRISMARINE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PRISMARINE));
		}

		if (Options.enableRedstone) {
			addFluid(Materials.getMaterialByName(MaterialNames.REDSTONE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.REDSTONE));
		}

		if (Options.enableSilver) {
			addFluid(Materials.getMaterialByName(MaterialNames.SILVER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.SILVER));
		}

		if (Options.enableStarSteel) {
			addFluid(Materials.getMaterialByName(MaterialNames.STARSTEEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.STARSTEEL));
		}

		if (Options.enableSteel) {
			addFluid(Materials.getMaterialByName(MaterialNames.STEEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.STEEL));
		}

		if (Options.enableTin) {
			addFluid(Materials.getMaterialByName(MaterialNames.TIN), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.TIN));
		}

		if (Options.enableZinc) {
			addFluid(Materials.getMaterialByName(MaterialNames.ZINC), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ZINC));
		}

		initDone = true;
	}
}
