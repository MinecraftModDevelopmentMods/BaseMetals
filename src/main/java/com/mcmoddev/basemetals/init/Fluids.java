package com.mcmoddev.basemetals.init;

import java.util.Arrays;
import java.util.List;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 * This class initializes all fluids in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public final class Fluids extends com.mcmoddev.lib.init.Fluids {

	private Fluids() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		// Vanilla Materials need to always have fluids available in case of tie-in mods
		final List<String> vanillaMaterials = Arrays.asList(MaterialNames.CHARCOAL, MaterialNames.COAL,
				MaterialNames.DIAMOND, MaterialNames.EMERALD, MaterialNames.GOLD, MaterialNames.IRON,
				MaterialNames.OBSIDIAN, MaterialNames.PRISMARINE, MaterialNames.REDSTONE);

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.AQUARIUM, MaterialNames.BISMUTH, MaterialNames.BRASS, MaterialNames.BRONZE,
				MaterialNames.COLDIRON, MaterialNames.COPPER, MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM,
				MaterialNames.INVAR, MaterialNames.LEAD, MaterialNames.MITHRIL, MaterialNames.NICKEL,
				MaterialNames.PEWTER, MaterialNames.PLATINUM, MaterialNames.SILVER, MaterialNames.STARSTEEL,
				MaterialNames.STEEL, MaterialNames.TIN, MaterialNames.ZINC);

		vanillaMaterials.stream().filter(Options::isFluidEnabled).forEach(materialName -> {
			addFluid(materialName, 2000, 10000, 769, 10);
			addFluidBlock(materialName);
		});

		materials.stream().filter(Materials::hasMaterial).filter(Options::isFluidEnabled).forEach(materialName -> {
			addFluid(materialName, 2000, 10000, 769, 10);
			addFluidBlock(materialName);
		});

		if (Materials.hasMaterial(MaterialNames.MERCURY)) {
			addFluid(MaterialNames.MERCURY, 13594, 2000, 769, 0);
			addFluidBlock(MaterialNames.MERCURY);
		}
	}
}
