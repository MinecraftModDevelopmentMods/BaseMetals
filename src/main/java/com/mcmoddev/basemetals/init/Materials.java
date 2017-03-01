package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.material.MetalMaterial.MaterialType;

/**
 * This class initializes all of the materials in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class Materials extends com.mcmoddev.lib.init.Materials {

	/** Adamantine */
	public static MetalMaterial adamantine;
	/** Antimony */
	public static MetalMaterial antimony;
	/** Aquarium */
	public static MetalMaterial aquarium;
	/** Bismuth */
	public static MetalMaterial bismuth;
	/** Brass */
	public static MetalMaterial brass;
	/** Bronze */
	public static MetalMaterial bronze;
	/** Cold-Iron */
	public static MetalMaterial coldiron;
	/** Copper */
	public static MetalMaterial copper;
	/** Cupronickel */
	public static MetalMaterial cupronickel;
	/** Electrum */
	public static MetalMaterial electrum;
	/** Invar */
	public static MetalMaterial invar;
	/** Lead */
	public static MetalMaterial lead;
	/** Mercury */
	public static MetalMaterial mercury;
	/** Mithril */
	public static MetalMaterial mithril;
	/** Nickel */
	public static MetalMaterial nickel;
	/** Pewter */
	public static MetalMaterial pewter;
	/** Platinum */
	public static MetalMaterial platinum;
	/** Silver */
	public static MetalMaterial silver;
	/** Star-Steel */
	public static MetalMaterial starsteel;
	/** Steel */
	public static MetalMaterial steel;
	/** Tin */
	public static MetalMaterial tin;
	/** Zinc */
	public static MetalMaterial zinc;

	// vanilla imports
	/** Wood */
	public static MetalMaterial vanilla_wood;
	/** Stone */
	public static MetalMaterial vanilla_stone;
	/** Iron */
	public static MetalMaterial vanilla_iron;
	/** Gold */
	public static MetalMaterial vanilla_gold;
	/** Diamond */
	public static MetalMaterial vanilla_diamond;
	/** Emerald */
	public static MetalMaterial vanilla_emerald;
	/** Quartz */
	public static MetalMaterial vanilla_quartz;
	/** Obsidian */
	public static MetalMaterial vanilla_obsidian;
	/** Coal */
	public static MetalMaterial vanilla_coal;
	/** Charcoal */
	public static MetalMaterial vanilla_charcoal;
	/** Lapis */
	public static MetalMaterial vanilla_lapis;
	/** Prismarine */
	public static MetalMaterial vanilla_prismarine;
	/** Redstone */
	public static MetalMaterial vanilla_redstone;
	/** Ender */
	public static MetalMaterial vanilla_ender;

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
		vanilla_wood = createOrelessMaterial("wood", MaterialType.WOOD, 2, 2, 6, 0xFF000000);

		vanilla_stone = createOrelessMaterial("stone", MaterialType.ROCK, 5, 4, 2, 0xFF000000);

		vanilla_iron = createMaterial("iron", MaterialType.METAL, 8, 8, 4.5, 0xFF000000);

		vanilla_gold = createMaterial("gold", MaterialType.METAL, 1, 1, 10, 0xFF000000);

		vanilla_diamond = createMaterial("diamond", MaterialType.GEM, 10, 15, 4, 0xFF000000);

		if (Options.enableCoal) {
			vanilla_coal = createMaterial("coal", MaterialType.MINERAL, 4, 4, 2, 0xFF000000);
		}

		if (Options.enableCharcoal) {
			vanilla_charcoal = createOrelessMaterial("charcoal", MaterialType.MINERAL, 4, 4, 2, 0xFF000000);
		}

		if (Options.enableEmerald) {
			vanilla_emerald = createMaterial("emerald", MaterialType.GEM, 10, 15, 4, 0xFF000000);
		}

		if (Options.enableEnder) {
			vanilla_ender = createOrelessMaterial("ender", MaterialType.GEM, 2, 2, 6, 0xFF000000);
		}

		if (Options.enableQuartz) {
			vanilla_quartz = createMaterial("quartz", MaterialType.GEM, 5, 4, 2, 0xFF000000);
		}

		if (Options.enableObsidian) {
			vanilla_obsidian = createOrelessMaterial("obsidian", MaterialType.ROCK, 10, 15, 4, 0xFF000000);
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
