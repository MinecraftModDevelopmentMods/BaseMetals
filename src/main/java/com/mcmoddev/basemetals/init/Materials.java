package com.mcmoddev.basemetals.init;

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

	/** Adamantine */
	public static MMDMaterial adamantine;
	/** Antimony */
	public static MMDMaterial antimony;
	/** Aquarium */
	public static MMDMaterial aquarium;
	/** Bismuth */
	public static MMDMaterial bismuth;
	/** Brass */
	public static MMDMaterial brass;
	/** Bronze */
	public static MMDMaterial bronze;
	/** Cold-Iron */
	public static MMDMaterial coldiron;
	/** Copper */
	public static MMDMaterial copper;
	/** Cupronickel */
	public static MMDMaterial cupronickel;
	/** Electrum */
	public static MMDMaterial electrum;
	/** Invar */
	public static MMDMaterial invar;
	/** Lead */
	public static MMDMaterial lead;
	/** Mercury */
	public static MMDMaterial mercury;
	/** Mithril */
	public static MMDMaterial mithril;
	/** Nickel */
	public static MMDMaterial nickel;
	/** Pewter */
	public static MMDMaterial pewter;
	/** Platinum */
	public static MMDMaterial platinum;
	/** Silver */
	public static MMDMaterial silver;
	/** Star-Steel */
	public static MMDMaterial starsteel;
	/** Steel */
	public static MMDMaterial steel;
	/** Tin */
	public static MMDMaterial tin;
	/** Zinc */
	public static MMDMaterial zinc;

	// vanilla imports
	/** Wood */
	public static MMDMaterial vanilla_wood;
	/** Stone */
	public static MMDMaterial vanilla_stone;
	/** Iron */
	public static MMDMaterial vanilla_iron;
	/** Gold */
	public static MMDMaterial vanilla_gold;
	/** Diamond */
	public static MMDMaterial vanilla_diamond;
	/** Emerald */
	public static MMDMaterial vanilla_emerald;
	/** Quartz */
	public static MMDMaterial vanilla_quartz;
	/** Obsidian */
	public static MMDMaterial vanilla_obsidian;
	/** Coal */
	public static MMDMaterial vanilla_coal;
	/** Charcoal */
	public static MMDMaterial vanilla_charcoal;
	/** Lapis */
	public static MMDMaterial vanilla_lapis;
	/** Prismarine */
	public static MMDMaterial vanilla_prismarine;
	/** Redstone */
	public static MMDMaterial vanilla_redstone;
	/** Ender */
	public static MMDMaterial vanilla_ender;

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
		vanilla_wood = createOrelessMaterial("wood", MaterialType.WOOD, 2, 2, 6, 0xFF695433);

		vanilla_stone = createOrelessMaterial("stone", MaterialType.ROCK, 5, 4, 2, 0xFF8F8F8F);

		vanilla_iron = createMaterial("iron", MaterialType.METAL, 8, 8, 4.5, 0xFFD8D8D8);

		vanilla_gold = createMaterial("gold", MaterialType.METAL, 1, 1, 10, 0xFFFFFF8B);

		vanilla_diamond = createMaterial("diamond", MaterialType.GEM, 10, 15, 4, 0xFF8CF4E1);

		if (Options.enableCoal) {
			vanilla_coal = createMaterial("coal", MaterialType.MINERAL, 4, 4, 2, 0xFF151515);
		}

		if (Options.enableCharcoal) {
			vanilla_charcoal = createOrelessMaterial("charcoal", MaterialType.MINERAL, 4, 4, 2, 0xFF231F18);
		}

		if (Options.enableEmerald) {
			vanilla_emerald = createMaterial("emerald", MaterialType.GEM, 10, 15, 4, 0xFF82F6AC);
		}

		if (Options.enableEnder) {
			vanilla_ender = createOrelessMaterial("ender", MaterialType.GEM, 2, 2, 6, 0xFF063931);
		}

		if (Options.enableQuartz) {
			vanilla_quartz = createMaterial("quartz", MaterialType.GEM, 5, 4, 2, 0xFFEAE3DB);
		}

		if (Options.enableObsidian) {
			vanilla_obsidian = createOrelessMaterial("obsidian", MaterialType.ROCK, 10, 15, 4, 0xFF101019);
		}

		if (Options.enableLapis) {
			vanilla_lapis = createMaterial("lapis", MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.enablePrismarine) {
			vanilla_prismarine = createMaterial("prismarine", MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		if (Options.enableRedstone) {
			vanilla_redstone = createMaterial("redstone", MaterialType.MINERAL, 1, 1, 1, 0xFF7B7B7B);
		}

		// Mod Materials
		if (Options.enableAdamantine) {
			adamantine = createMaterial("adamantine", MaterialType.METAL, 12, 100, 0, 0xFF53393F).setBlastResistance(2000f);
		}

		if (Options.enableAntimony) {
			antimony = createMaterial("antimony", MaterialType.METAL, 1, 1, 1, 0xFFD8E3DE);
		}

		if (Options.enableAquarium) {
			aquarium = createAlloyMaterial("aquarium", MaterialType.METAL, 4, 10, 15, 0xFF000000);
		}

		if (Options.enableBismuth) {
			bismuth = createMaterial("bismuth", MaterialType.METAL, 1, 1, 1, 0xFFDDD7CB);
		}

		if (Options.enableBrass) {
			brass = createAlloyMaterial("brass", MaterialType.METAL, 3.5, 3, 9, 0xFFFFE374);
		}

		if (Options.enableBronze) {
			bronze = createAlloyMaterial("bronze", MaterialType.METAL, 8, 4, 4.5, 0xFFF7A54F);
		}

		if (Options.enableColdIron) {
			coldiron = createMaterial("coldiron", MaterialType.METAL, 7, 7, 7, 0xFFC7CEF0);
		}

		if (Options.enableCopper) {
			copper = createMaterial("copper", MaterialType.METAL, 4, 4, 5, 0xFFFF9F78);
		}

		if (Options.enableCupronickel) {
			cupronickel = createAlloyMaterial("cupronickel", MaterialType.METAL, 6, 6, 6, 0xFFC8AB6F);
		}

		if (Options.enableElectrum) {
			electrum = createAlloyMaterial("electrum", MaterialType.METAL, 5, 4, 10, 0xFFFFF2B3);
		}

		if (Options.enableInvar) {
			invar = createAlloyMaterial("invar", MaterialType.METAL, 9, 10, 3, 0xFFD2CDB8);
		}

		if (Options.enableLead) {
			lead = createMaterial("lead", MaterialType.METAL, 1, 1, 1, 0xFF7B7B7B).setBaseDamage(4f);
		}

		if (Options.enableMercury) {
			mercury = createMaterial("mercury", MaterialType.METAL, 1, 1, 1, 0);
		}

		if (Options.enableMithril) {
			mithril = createAlloyMaterial("mithril", MaterialType.METAL, 9, 9, 9, 0xFFF4FFFF);
		}
		if (Options.enableNickel) {
			nickel = createMaterial("nickel", MaterialType.METAL, 4, 4, 7, 0xFFEEFFEB);
		}
		if (Options.enablePewter) {
			pewter = createAlloyMaterial("pewter", MaterialType.METAL, 1, 1, 1, 0xFF92969F);
		}
		if (Options.enablePlatinum) {
			platinum = createRareMaterial("platinum", MaterialType.METAL, 3, 5, 15, 0xFFF2FFFF);
		}

		if (Options.enableSilver) {
			silver = createMaterial("silver", MaterialType.METAL, 5, 4, 6, 0xFFFFFFFF);
		}
		if (Options.enableStarSteel) {
			starsteel = createMaterial("starsteel", MaterialType.METAL, 10, 25, 12, 0xFF53393F).setBlastResistance(2000f);
			starsteel.regenerates = true;
		}
		if (Options.enableSteel) {
			steel = createAlloyMaterial("steel", MaterialType.METAL, 8, 15, 2, 0xFFD5E3E5);
		}
		if (Options.enableTin) {
			tin = createMaterial("tin", MaterialType.METAL, 3, 1, 2, 0xFFFFF7EE);
		}
		if (Options.enableZinc) {
			zinc = createMaterial("zinc", MaterialType.METAL, 1, 1, 1, 0xFFBCBCBC);
		}

		initDone = true;
	}
}
