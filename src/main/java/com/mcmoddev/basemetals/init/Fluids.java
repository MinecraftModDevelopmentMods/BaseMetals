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
		
		// Vanilla Materials need to always have fluids available in case of tie-in mods
		addFluid(MaterialNames.CHARCOAL, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.CHARCOAL);
		addFluid(MaterialNames.COAL, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.COAL);
		addFluid(MaterialNames.DIAMOND, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.DIAMOND);
		addFluid(MaterialNames.EMERALD, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.EMERALD);
		addFluid(MaterialNames.ENDER, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.ENDER);
		addFluid(MaterialNames.GOLD, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.GOLD);
		addFluid(MaterialNames.IRON, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.IRON);
		addFluid(MaterialNames.OBSIDIAN, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.OBSIDIAN);
		addFluid(MaterialNames.PRISMARINE, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.PRISMARINE);
		addFluid(MaterialNames.REDSTONE, 2000, 10000, 769, 10);
		addFluidBlock(MaterialNames.REDSTONE);

		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			addFluid(MaterialNames.ADAMANTINE, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.ADAMANTINE);
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			addFluid(MaterialNames.ANTIMONY, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.ANTIMONY);
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			addFluid(MaterialNames.AQUARIUM, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.AQUARIUM);
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			addFluid(MaterialNames.BISMUTH, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.BISMUTH);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			addFluid(MaterialNames.BRASS, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.BRASS);
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			addFluid(MaterialNames.BRONZE, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.BRONZE);
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			addFluid(MaterialNames.COLDIRON, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.COLDIRON);
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			addFluid(MaterialNames.COPPER, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.COPPER);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			addFluid(MaterialNames.CUPRONICKEL, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.CUPRONICKEL);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			addFluid(MaterialNames.ELECTRUM, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.ELECTRUM);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			addFluid(MaterialNames.INVAR, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.INVAR);
		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			addFluid(MaterialNames.LEAD, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.LEAD);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			addFluid(MaterialNames.MERCURY, 13594, 2000, 769, 0);
			addFluidBlock(MaterialNames.MERCURY);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			addFluid(MaterialNames.MITHRIL, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.MITHRIL);
		}

		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			addFluid(MaterialNames.NICKEL, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.NICKEL);
		}

		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			addFluid(MaterialNames.PEWTER, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.PEWTER);
		}

		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			addFluid(MaterialNames.PLATINUM, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.PLATINUM);
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			addFluid(MaterialNames.SILVER, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.SILVER);
		}

		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			addFluid(MaterialNames.STARSTEEL, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.STARSTEEL);
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			addFluid(MaterialNames.STEEL, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.STEEL);
		}

		if (Options.materialEnabled(MaterialNames.TIN)) {
			addFluid(MaterialNames.TIN, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.TIN);
		}

		if (Options.materialEnabled(MaterialNames.ZINC)) {
			addFluid(MaterialNames.ZINC, 2000, 10000, 769, 10);
			addFluidBlock(MaterialNames.ZINC);
		}

		initDone = true;
	}
}
