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

		if (Options.materialEnabled(MaterialNames.COAL)) {
			createMaterial(MaterialNames.COAL, MaterialType.MINERAL, 4, 4, 2, 0xFF151515);
		}

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			createOrelessMaterial(MaterialNames.CHARCOAL, MaterialType.MINERAL, 4, 4, 2, 0xFF231F18);
		}

		if (Options.materialEnabled(MaterialNames.EMERALD)) {
			createMaterial(MaterialNames.EMERALD, MaterialType.GEM, 10, 15, 4, 0xFF82F6AC);
		}

		if (Options.materialEnabled(MaterialNames.ENDER)) {
			createOrelessMaterial(MaterialNames.ENDER, MaterialType.GEM, 2, 2, 6, 0xFF063931);
		}

		if (Options.materialEnabled(MaterialNames.QUARTZ)) {
			createMaterial(MaterialNames.QUARTZ, MaterialType.GEM, 5, 4, 2, 0xFFEAE3DB);
		}

		if (Options.materialEnabled(MaterialNames.OBSIDIAN)) {
			createOrelessMaterial(MaterialNames.OBSIDIAN, MaterialType.ROCK, 10, 15, 4, 0xFF101019);
		}

		if (Options.materialEnabled(MaterialNames.LAPIS)) {
			createMaterial(MaterialNames.LAPIS, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.materialEnabled(MaterialNames.PRISMARINE)) {
			createMaterial(MaterialNames.PRISMARINE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.materialEnabled(MaterialNames.REDSTONE)) {
			createMaterial(MaterialNames.REDSTONE, MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		// Mod Materials
		if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
			createMaterial(MaterialNames.ADAMANTINE, MaterialType.METAL, 12, 100, 0, 0xFF53393F).setBlastResistance(2000f);
		}

		if (Options.materialEnabled(MaterialNames.ANTIMONY)) {
			createMaterial(MaterialNames.ANTIMONY, MaterialType.METAL, 1, 1, 1, 0xFFD8E3DE);
		}

		if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
			createAlloyMaterial(MaterialNames.AQUARIUM, MaterialType.METAL, 4, 10, 15, 0xFF000000);
		}

		if (Options.materialEnabled(MaterialNames.BISMUTH)) {
			createMaterial(MaterialNames.BISMUTH, MaterialType.METAL, 1, 1, 1, 0xFFDDD7CB);
		}

		if (Options.materialEnabled(MaterialNames.BRASS)) {
			createAlloyMaterial(MaterialNames.BRASS, MaterialType.METAL, 3.5, 3, 9, 0xFFFFE374);
		}

		if (Options.materialEnabled(MaterialNames.BRONZE)) {
			createAlloyMaterial(MaterialNames.BRONZE, MaterialType.METAL, 8, 4, 4.5, 0xFFF7A54F);
		}

		if (Options.materialEnabled(MaterialNames.COLDIRON)) {
			createMaterial(MaterialNames.COLDIRON, MaterialType.METAL, 7, 7, 7, 0xFFC7CEF0);
		}

		if (Options.materialEnabled(MaterialNames.COPPER)) {
			createMaterial(MaterialNames.COPPER, MaterialType.METAL, 4, 4, 5, 0xFFFF9F78);
		}

		if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
			createAlloyMaterial(MaterialNames.CUPRONICKEL, MaterialType.METAL, 6, 6, 6, 0xFFC8AB6F);
		}

		if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
			createAlloyMaterial(MaterialNames.ELECTRUM, MaterialType.METAL, 5, 4, 10, 0xFFFFF2B3);
		}

		if (Options.materialEnabled(MaterialNames.INVAR)) {
			createAlloyMaterial(MaterialNames.INVAR, MaterialType.METAL, 9, 10, 3, 0xFFD2CDB8);
		}

		if (Options.materialEnabled(MaterialNames.LEAD)) {
			createMaterial(MaterialNames.LEAD, MaterialType.METAL, 1, 1, 1, 0xFF7B7B7B).setBaseDamage(4f);
		}

		if (Options.materialEnabled(MaterialNames.MERCURY)) {
			createMaterial(MaterialNames.MERCURY, MaterialType.METAL, 1, 1, 1, 0);
		}

		if (Options.materialEnabled(MaterialNames.MITHRIL)) {
			createAlloyMaterial(MaterialNames.MITHRIL, MaterialType.METAL, 9, 9, 9, 0xFFF4FFFF);
		}
		if (Options.materialEnabled(MaterialNames.NICKEL)) {
			createMaterial(MaterialNames.NICKEL, MaterialType.METAL, 4, 4, 7, 0xFFEEFFEB);
		}
		if (Options.materialEnabled(MaterialNames.PEWTER)) {
			createAlloyMaterial(MaterialNames.PEWTER, MaterialType.METAL, 1, 1, 1, 0xFF92969F);
		}
		if (Options.materialEnabled(MaterialNames.PLATINUM)) {
			createRareMaterial(MaterialNames.PLATINUM, MaterialType.METAL, 3, 5, 15, 0xFFF2FFFF);
		}

		if (Options.materialEnabled(MaterialNames.SILVER)) {
			createMaterial(MaterialNames.SILVER, MaterialType.METAL, 5, 4, 6, 0xFFFFFFFF);
		}
		if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
			MMDMaterial starsteel = createMaterial(MaterialNames.STARSTEEL, MaterialType.METAL, 10, 25, 12, 0xFF53393F).setBlastResistance(2000f);
			starsteel.setRegenerates(true);
		}
		if (Options.materialEnabled(MaterialNames.STEEL)) {
			createAlloyMaterial(MaterialNames.STEEL, MaterialType.METAL, 8, 15, 2, 0xFFD5E3E5);
		}
		if (Options.materialEnabled(MaterialNames.TIN)) {
			createMaterial(MaterialNames.TIN, MaterialType.METAL, 3, 1, 2, 0xFFFFF7EE);
		}
		if (Options.materialEnabled(MaterialNames.ZINC)) {
			createMaterial(MaterialNames.ZINC, MaterialType.METAL, 1, 1, 1, 0xFFBCBCBC);
		}

		initDone = true;
	}
}
