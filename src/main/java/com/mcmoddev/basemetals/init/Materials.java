package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
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
		// always created and populated with their base item-sets
		// even if they are not enabled
		// Oreless because our Recipe code can tend to be silly otherwise
		createOrelessMaterial(MaterialNames.WOOD, MaterialType.WOOD, 2, 2, 6, 0xFF695433);

		createOrelessMaterial(MaterialNames.STONE, MaterialType.ROCK, 5, 4, 2, 0xFF8F8F8F);

		createOrelessMaterial(MaterialNames.IRON, MaterialType.METAL, 8, 8, 4.5, 0xFFD8D8D8);

		createOrelessMaterial(MaterialNames.GOLD, MaterialType.METAL, 1, 1, 10, 0xFFFFFF8B);

		createOrelessMaterial(MaterialNames.DIAMOND, MaterialType.GEM, 10, 15, 4, 0xFF8CF4E1);

		createOrelessMaterial(MaterialNames.COAL, MaterialType.MINERAL, 4, 4, 2, 0xFF151515);

		createOrelessMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 4, 4, 2, 0xFF231F18);

		createOrelessMaterial(MaterialNames.EMERALD, MaterialType.GEM, 10, 15, 4, 0xFF82F6AC);

		createOrelessMaterial(MaterialNames.ENDER, MaterialType.GEM, 2, 2, 6, 0xFF063931);

		createOrelessMaterial(MaterialNames.QUARTZ, MaterialType.GEM, 5, 4, 2, 0xFFEAE3DB);

		createOrelessMaterial(MaterialNames.OBSIDIAN, MaterialType.ROCK, 10, 15, 4, 0xFF101019);

		createOrelessMaterial(MaterialNames.LAPIS, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);

		createOrelessMaterial(MaterialNames.PRISMARINE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);

		createOrelessMaterial(MaterialNames.REDSTONE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);

		// Mod Materials
		if (Options.isMaterialEnabled(MaterialNames.ADAMANTINE)) {
			createMaterial(MaterialNames.ADAMANTINE, MaterialType.METAL, 12, 100, 0, 0xFF53393F).setBlastResistance(2000f);
		}

		if (Options.isMaterialEnabled(MaterialNames.ANTIMONY)) {
			createMaterial(MaterialNames.ANTIMONY, MaterialType.METAL, 1, 1, 1, 0xFFD8E3DE);
		}

		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
			createAlloyMaterial(MaterialNames.AQUARIUM, MaterialType.METAL, 4, 10, 15, 0xFF000000);
		}

		if (Options.isMaterialEnabled(MaterialNames.BISMUTH)) {
			createMaterial(MaterialNames.BISMUTH, MaterialType.METAL, 1, 1, 1, 0xFFDDD7CB);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
			createAlloyMaterial(MaterialNames.BRASS, MaterialType.METAL, 3.5, 3, 9, 0xFFFFE374);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRONZE)) {
			createAlloyMaterial(MaterialNames.BRONZE, MaterialType.METAL, 8, 4, 4.5, 0xFFF7A54F);
		}

		if (Options.isMaterialEnabled(MaterialNames.COLDIRON)) {
			createMaterial(MaterialNames.COLDIRON, MaterialType.METAL, 7, 7, 7, 0xFFC7CEF0);
		}

		if (Options.isMaterialEnabled(MaterialNames.COPPER)) {
			createMaterial(MaterialNames.COPPER, MaterialType.METAL, 4, 4, 5, 0xFFFF9F78);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL)) {
			createAlloyMaterial(MaterialNames.CUPRONICKEL, MaterialType.METAL, 6, 6, 6, 0xFFC8AB6F);
		}

		if (Options.isMaterialEnabled(MaterialNames.ELECTRUM)) {
			createAlloyMaterial(MaterialNames.ELECTRUM, MaterialType.METAL, 5, 4, 10, 0xFFFFF2B3);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR)) {
			createAlloyMaterial(MaterialNames.INVAR, MaterialType.METAL, 9, 10, 3, 0xFFD2CDB8);
		}

		if (Options.isMaterialEnabled(MaterialNames.LEAD)) {
			createMaterial(MaterialNames.LEAD, MaterialType.METAL, 1, 1, 1, 0xFF7B7B7B).setBaseDamage(4f);
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			createMaterial(MaterialNames.MERCURY, MaterialType.METAL, 1, 1, 1, 0);
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
			createAlloyMaterial(MaterialNames.MITHRIL, MaterialType.METAL, 9, 9, 9, 0xFFF4FFFF);
		}
		if (Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			createMaterial(MaterialNames.NICKEL, MaterialType.METAL, 4, 4, 7, 0xFFEEFFEB);
		}
		if (Options.isMaterialEnabled(MaterialNames.PEWTER)) {
			createAlloyMaterial(MaterialNames.PEWTER, MaterialType.METAL, 1, 1, 1, 0xFF92969F);
		}
		if (Options.isMaterialEnabled(MaterialNames.PLATINUM)) {
			createRareMaterial(MaterialNames.PLATINUM, MaterialType.METAL, 3, 5, 15, 0xFFF2FFFF);
		}

		if (Options.isMaterialEnabled(MaterialNames.SILVER)) {
			createMaterial(MaterialNames.SILVER, MaterialType.METAL, 5, 4, 6, 0xFFFFFFFF);
		}
		if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
			MMDMaterial starsteel = createMaterial(MaterialNames.STARSTEEL, MaterialType.METAL, 10, 25, 12, 0xFF53393F).setBlastResistance(2000f);
			starsteel.setRegenerates(true);
		}
		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			createAlloyMaterial(MaterialNames.STEEL, MaterialType.METAL, 8, 15, 2, 0xFFD5E3E5);
		}
		if (Options.isMaterialEnabled(MaterialNames.TIN)) {
			createMaterial(MaterialNames.TIN, MaterialType.METAL, 3, 1, 2, 0xFFFFF7EE);
		}
		if (Options.isMaterialEnabled(MaterialNames.ZINC)) {
			createMaterial(MaterialNames.ZINC, MaterialType.METAL, 1, 1, 1, 0xFFBCBCBC);
		}

		initDone = true;
	}
}
