package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;

/**
 * This class initializes all of the materials in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	private static boolean initDone = false;

	protected Materials() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// Vanilla Materials
		createOrelessMaterial(MaterialNames.WOOD, MaterialType.WOOD, 2, 2, 6, 0xFF695433);

		createOrelessMaterial(MaterialNames.STONE, MaterialType.ROCK, 5, 4, 2, 0xFF8F8F8F);

		createMaterial(MaterialNames.IRON, MaterialType.METAL, 8, 8, 4.5, 0xFFD8D8D8);

		createMaterial(MaterialNames.GOLD, MaterialType.METAL, 1, 1, 10, 0xFFFFFF8B);

		createMaterial(MaterialNames.DIAMOND, MaterialType.GEM, 10, 15, 4, 0xFF8CF4E1);

		if (Options.enableCoal) {
			createMaterial(MaterialNames.COAL, MaterialType.MINERAL, 4, 4, 2, 0xFF151515);
		}

		if (Options.enableCharcoal) {
			createOrelessMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 4, 4, 2, 0xFF231F18);
		}

		if (Options.enableEmerald) {
			createMaterial(MaterialNames.EMERALD, MaterialType.GEM, 10, 15, 4, 0xFF82F6AC);
		}

		if (Options.enableEnder) {
			createOrelessMaterial(MaterialNames.ENDER, MaterialType.GEM, 2, 2, 6, 0xFF063931);
		}

		if (Options.enableQuartz) {
			createMaterial(MaterialNames.QUARTZ, MaterialType.GEM, 5, 4, 2, 0xFFEAE3DB);
		}

		if (Options.enableObsidian) {
			createOrelessMaterial(MaterialNames.OBSIDIAN, MaterialType.ROCK, 10, 15, 4, 0xFF101019);
		}

		if (Options.enableLapis) {
			createMaterial(MaterialNames.LAPIS, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.enablePrismarine) {
			createMaterial(MaterialNames.PRISMARINE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.enableRedstone) {
			createMaterial(MaterialNames.REDSTONE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		// Mod Materials
		if (Options.enableAdamantine) {
			createMaterial(MaterialNames.ADAMANTINE, MaterialType.METAL, 12, 100, 0, 0xFF53393F).setBlastResistance(2000f);
		}

		if (Options.enableAntimony) {
			createMaterial(MaterialNames.ANTIMONY, MaterialType.METAL, 1, 1, 1, 0xFFD8E3DE);
		}

		if (Options.enableAquarium) {
			createAlloyMaterial(MaterialNames.AQUARIUM, MaterialType.METAL, 4, 10, 15, 0xFF000000);
		}

		if (Options.enableBismuth) {
			createMaterial(MaterialNames.BISMUTH, MaterialType.METAL, 1, 1, 1, 0xFFDDD7CB);
		}

		if (Options.enableBrass) {
			createAlloyMaterial(MaterialNames.BRASS, MaterialType.METAL, 3.5, 3, 9, 0xFFFFE374);
		}

		if (Options.enableBronze) {
			createAlloyMaterial(MaterialNames.BRONZE, MaterialType.METAL, 8, 4, 4.5, 0xFFF7A54F);
		}

		if (Options.enableColdIron) {
			createMaterial(MaterialNames.COLDIRON, MaterialType.METAL, 7, 7, 7, 0xFFC7CEF0);
		}

		if (Options.enableCopper) {
			createMaterial(MaterialNames.COPPER, MaterialType.METAL, 4, 4, 5, 0xFFFF9F78);
		}

		if (Options.enableCupronickel) {
			createAlloyMaterial(MaterialNames.CUPRONICKEL, MaterialType.METAL, 6, 6, 6, 0xFFC8AB6F);
		}

		if (Options.enableElectrum) {
			createAlloyMaterial(MaterialNames.ELECTRUM, MaterialType.METAL, 5, 4, 10, 0xFFFFF2B3);
		}

		if (Options.enableInvar) {
			createAlloyMaterial(MaterialNames.INVAR, MaterialType.METAL, 9, 10, 3, 0xFFD2CDB8);
		}

		if (Options.enableLead) {
			createMaterial(MaterialNames.LEAD, MaterialType.METAL, 1, 1, 1, 0xFF7B7B7B).setBaseDamage(4f);
		}

		if (Options.enableMercury) {
			createMaterial(MaterialNames.MERCURY, MaterialType.METAL, 1, 1, 1, 0);
		}

		if (Options.enableMithril) {
			createAlloyMaterial(MaterialNames.MITHRIL, MaterialType.METAL, 9, 9, 9, 0xFFF4FFFF);
		}
		if (Options.enableNickel) {
			createMaterial(MaterialNames.NICKEL, MaterialType.METAL, 4, 4, 7, 0xFFEEFFEB);
		}
		if (Options.enablePewter) {
			createAlloyMaterial(MaterialNames.PEWTER, MaterialType.METAL, 1, 1, 1, 0xFF92969F);
		}
		if (Options.enablePlatinum) {
			createRareMaterial(MaterialNames.PLATINUM, MaterialType.METAL, 3, 5, 15, 0xFFF2FFFF);
		}

		if (Options.enableSilver) {
			createMaterial(MaterialNames.SILVER, MaterialType.METAL, 5, 4, 6, 0xFFFFFFFF);
		}
		if (Options.enableStarSteel) {
			MMDMaterial starsteel = createMaterial(MaterialNames.STARSTEEL, MaterialType.METAL, 10, 25, 12, 0xFF53393F).setBlastResistance(2000f);
			starsteel.setRegenerates(true);
		}
		if (Options.enableSteel) {
			createAlloyMaterial(MaterialNames.STEEL, MaterialType.METAL, 8, 15, 2, 0xFFD5E3E5);
		}
		if (Options.enableTin) {
			createMaterial(MaterialNames.TIN, MaterialType.METAL, 3, 1, 2, 0xFFFFF7EE);
		}
		if (Options.enableZinc) {
			createMaterial(MaterialNames.ZINC, MaterialType.METAL, 1, 1, 1, 0xFFBCBCBC);
		}

		initDone = true;
	}
}
