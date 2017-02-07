package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.MetalMaterial;

/**
 * This class initializes all of the materials in Base Metals. It also
 * contains utility methods for looking up materials by name and finding the
 * tool and armor material equivalents for a given material.
 *
 * @author DrCyano
 *
 */
public abstract class Materials extends com.mcmoddev.lib.init.Materials {

	public static MetalMaterial adamantine;
	public static MetalMaterial antimony;
	public static MetalMaterial aquarium;
	public static MetalMaterial bismuth;
	public static MetalMaterial brass;
	public static MetalMaterial bronze;
	public static MetalMaterial coldiron;
	public static MetalMaterial copper;
	public static MetalMaterial cupronickel;
	public static MetalMaterial electrum;
	public static MetalMaterial invar;
	public static MetalMaterial lead;
	public static MetalMaterial mercury;
	public static MetalMaterial mithril;
	public static MetalMaterial nickel;
	public static MetalMaterial pewter;
	public static MetalMaterial platinum;
	public static MetalMaterial silver;
	public static MetalMaterial starsteel;
	public static MetalMaterial steel;
	public static MetalMaterial tin;
	public static MetalMaterial zinc;

	// vanilla imports
	public static MetalMaterial vanilla_wood;
	public static MetalMaterial vanilla_stone;
	public static MetalMaterial vanilla_iron;
	public static MetalMaterial vanilla_gold;
	public static MetalMaterial vanilla_diamond;
	public static MetalMaterial vanilla_coal;
	public static MetalMaterial vanilla_charcoal;
	
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
		vanilla_wood = createOrelessMaterial("wood", 2, 2, 6, 0xFF000000);
		vanilla_wood.isVanilla = true;
		vanilla_wood.isBasic = true;
		vanilla_wood.materialType = MetalMaterial.MaterialType.WOOD;

		vanilla_stone = createOrelessMaterial("stone", 5, 4, 2, 0xFF000000);
		vanilla_stone.isVanilla = true;
		vanilla_stone.isBasic = true;
		vanilla_stone.materialType = MetalMaterial.MaterialType.ROCK;

		vanilla_iron = createMaterial("iron", 8, 8, 4.5, 0xFF000000);
		vanilla_iron.isVanilla = true;
		vanilla_iron.materialType = MetalMaterial.MaterialType.METAL;

		vanilla_gold = createMaterial("gold", 1, 1, 10, 0xFF000000);
		vanilla_gold.isVanilla = true;
		vanilla_gold.materialType = MetalMaterial.MaterialType.METAL;

		vanilla_diamond = createMaterial("diamond", 10, 15, 4, 0xFF000000);
		vanilla_diamond.isVanilla = true;
		vanilla_diamond.materialType = MetalMaterial.MaterialType.GEM;

		if( Options.enableCoal ) {
			vanilla_coal = createMaterial("coal", 4, 4, 2, 0xFF000000);
			vanilla_coal.isVanilla = true;
			vanilla_coal.materialType = MetalMaterial.MaterialType.MINERAL;
		}		

		if( Options.enableCharcoal ) {
			vanilla_charcoal = createOrelessMaterial("charcoal", 4, 4, 2, 0xFF000000);
			vanilla_charcoal.isVanilla = true;
			vanilla_charcoal.materialType = MetalMaterial.MaterialType.MINERAL;
		}		
		
		// Mod Materials
		if (Options.enableAdamantine) {
			adamantine = createMaterial("adamantine", 12, 100, 0, 0xFF53393F).setBlastResistance(2000f);
			adamantine.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableAntimony) {
			antimony = createMaterial("antimony", 1, 1, 1, 0xFFD8E3DE);
			antimony.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableAquarium) {
			aquarium = createAlloyMaterial("aquarium", 4, 10, 15, 0xFF000000);
			aquarium.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableBismuth) {
			bismuth = createMaterial("bismuth", 1, 1, 1, 0xFFDDD7CB);
			bismuth.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableBrass) {
			brass = createAlloyMaterial("brass", 3.5, 3, 9, 0xFFFFE374);
			brass.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableBronze) {
			bronze = createAlloyMaterial("bronze", 8, 4, 4.5, 0xFFF7A54F);
			bronze.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableColdIron) {
			coldiron = createMaterial("coldiron", 7, 7, 7, 0xFFC7CEF0);
			coldiron.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableCopper) {
			copper = createMaterial("copper", 4, 4, 5, 0xFFFF9F78);
			copper.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableCupronickel) {
			cupronickel = createAlloyMaterial("cupronickel", 6, 6, 6, 0xFFC8AB6F);
			cupronickel.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableElectrum) {
			electrum = createAlloyMaterial("electrum", 5, 4, 10, 0xFFFFF2B3);
			electrum.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableInvar) {
			invar = createAlloyMaterial("invar", 9, 10, 3, 0xFFD2CDB8);
			invar.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableLead) {
			lead = createMaterial("lead", 1, 1, 1, 0xFF7B7B7B).setBaseDamage(4f);
			lead.materialType = MetalMaterial.MaterialType.METAL;
		}
		
		if (Options.enableMercury) {
			mercury = createMaterial("mercury", 1, 1, 1, 0);
			mercury.materialType = MetalMaterial.MaterialType.METAL;
		}
		
		if (Options.enableMithril) {
			mithril = createAlloyMaterial("mithril", 9, 9, 9, 0xFFF4FFFF);
			mithril.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableNickel) {
			nickel = createMaterial("nickel", 4, 4, 7, 0xFFEEFFEB);
			nickel.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enablePewter) {
			pewter = createAlloyMaterial("pewter", 1, 1, 1, 0xFF92969F);
			pewter.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enablePlatinum) {
			platinum = createRareMaterial("platinum", 3, 5, 15, 0xFFF2FFFF);
			platinum.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableSilver) {
			silver = createMaterial("silver", 5, 4, 6, 0xFFFFFFFF);
			silver.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableStarSteel) {
			starsteel = createMaterial("starsteel", 10, 25, 12, 0xFF53393F).setBlastResistance(2000f);
			starsteel.regenerates = true;
			starsteel.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableSteel) {
			steel = createAlloyMaterial("steel", 8, 15, 2, 0xFFD5E3E5);
			steel.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableTin) {
			tin = createMaterial("tin", 3, 1, 2, 0xFFFFF7EE);
			tin.materialType = MetalMaterial.MaterialType.METAL;
		}
		if (Options.enableZinc) {
			zinc = createMaterial("zinc", 1, 1, 1, 0xFFBCBCBC);
			zinc.materialType = MetalMaterial.MaterialType.METAL;
		}

		initDone = true;
	}
}
