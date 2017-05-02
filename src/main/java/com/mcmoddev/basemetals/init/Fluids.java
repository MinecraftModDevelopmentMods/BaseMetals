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

		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			addFluid(Materials.getMaterialByName(MaterialNames.ADAMANTINE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ADAMANTINE));
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			addFluid(Materials.getMaterialByName(MaterialNames.ANTIMONY), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ANTIMONY));
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			addFluid(Materials.getMaterialByName(MaterialNames.AQUARIUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.AQUARIUM));
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			addFluid(Materials.getMaterialByName(MaterialNames.BISMUTH), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BISMUTH));
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			addFluid(Materials.getMaterialByName(MaterialNames.BRASS), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BRASS));
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			addFluid(Materials.getMaterialByName(MaterialNames.BRONZE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.BRONZE));
		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.CHARCOAL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.CHARCOAL));
		}

		if (Options.materialEnabled(MaterialNames.COAL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.COAL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COAL));
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			addFluid(Materials.getMaterialByName(MaterialNames.COLDIRON), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COLDIRON));
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			addFluid(Materials.getMaterialByName(MaterialNames.COPPER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.COPPER));
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.CUPRONICKEL));
		}

		if (Options.materialEnabled(MaterialNames.DIAMOND)) {
			addFluid(Materials.getMaterialByName(MaterialNames.DIAMOND), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.DIAMOND));
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			addFluid(Materials.getMaterialByName(MaterialNames.ELECTRUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ELECTRUM));
		}

		if (Options.materialEnabled(MaterialNames.EMERALD)) {
			addFluid(Materials.getMaterialByName(MaterialNames.EMERALD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.EMERALD));
		}

		if (Options.materialEnabled(MaterialNames.ENDER)) {
			addFluid(Materials.getMaterialByName(MaterialNames.ENDER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ENDER));
		}

		if (Options.materialEnabled(MaterialNames.GOLD)) {
			addFluid(Materials.getMaterialByName(MaterialNames.GOLD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.GOLD));
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			addFluid(Materials.getMaterialByName(MaterialNames.INVAR), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.INVAR));
		}

		if (Options.materialEnabled(MaterialNames.IRON)) {
			addFluid(Materials.getMaterialByName(MaterialNames.IRON), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.IRON));
		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			addFluid(Materials.getMaterialByName(MaterialNames.LEAD), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.LEAD));
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			addFluid(Materials.getMaterialByName(MaterialNames.MERCURY), 13594, 2000, 769, 0);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.MERCURY));
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.MITHRIL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.MITHRIL));
		}

		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.NICKEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.NICKEL));
		}

		if (Options.materialEnabled(MaterialNames.OBSIDIAN)) {
			addFluid(Materials.getMaterialByName(MaterialNames.OBSIDIAN), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.OBSIDIAN));
		}

		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			addFluid(Materials.getMaterialByName(MaterialNames.PEWTER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PEWTER));
		}

		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			addFluid(Materials.getMaterialByName(MaterialNames.PLATINUM), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PLATINUM));
		}

		if (Options.materialEnabled(MaterialNames.PRISMARINE)) {
			addFluid(Materials.getMaterialByName(MaterialNames.PRISMARINE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.PRISMARINE));
		}

		if (Options.materialEnabled(MaterialNames.REDSTONE)) {
			addFluid(Materials.getMaterialByName(MaterialNames.REDSTONE), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.REDSTONE));
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			addFluid(Materials.getMaterialByName(MaterialNames.SILVER), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.SILVER));
		}

		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.STARSTEEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.STARSTEEL));
		}

		if (Options.materialEnabled(MaterialNames.STEEL)) {
			addFluid(Materials.getMaterialByName(MaterialNames.STEEL), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.STEEL));
		}

		if (Options.materialEnabled(MaterialNames.TIN)) {
			addFluid(Materials.getMaterialByName(MaterialNames.TIN), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.TIN));
		}

		if (Options.materialEnabled(MaterialNames.ZINC)) {
			addFluid(Materials.getMaterialByName(MaterialNames.ZINC), 2000, 10000, 769, 10);
			addFluidBlock(Materials.getMaterialByName(MaterialNames.ZINC));
		}

		initDone = true;
	}
}
