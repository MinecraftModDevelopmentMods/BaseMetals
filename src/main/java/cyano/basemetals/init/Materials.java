package cyano.basemetals.init;

import com.mcmoddev.lib.material.MMDMaterial;

public class Materials {

	// Backwards Compatibility
	public static MMDMaterial adamantine;
	public static MMDMaterial antimony;
	public static MMDMaterial aquarium;
	public static MMDMaterial bismuth;
	public static MMDMaterial brass;
	public static MMDMaterial bronze;
	public static MMDMaterial coldiron;
	public static MMDMaterial copper;
	public static MMDMaterial cupronickel;
	public static MMDMaterial electrum;
	public static MMDMaterial invar;
	public static MMDMaterial lead;
	public static MMDMaterial mithril;
	public static MMDMaterial nickel;
	public static MMDMaterial pewter;
	public static MMDMaterial platinum;
	public static MMDMaterial silver;
	public static MMDMaterial starsteel;
	public static MMDMaterial steel;
	public static MMDMaterial tin;
	public static MMDMaterial zinc;

	// vanilla imports
	public static MMDMaterial vanilla_wood;
	public static MMDMaterial vanilla_stone;
	public static MMDMaterial vanilla_iron;
	public static MMDMaterial vanilla_gold;
	public static MMDMaterial vanilla_diamond;

	private static boolean initDone = false;

	private Materials() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	public static void init() {
		if (initDone) {
			return;
		}

		adamantine = com.mcmoddev.basemetals.init.Materials.adamantine;
		antimony = com.mcmoddev.basemetals.init.Materials.antimony;
		aquarium = com.mcmoddev.basemetals.init.Materials.aquarium;
		bismuth = com.mcmoddev.basemetals.init.Materials.bismuth;
		brass = com.mcmoddev.basemetals.init.Materials.brass;
		bronze = com.mcmoddev.basemetals.init.Materials.bronze;
		coldiron = com.mcmoddev.basemetals.init.Materials.coldiron;
		copper = com.mcmoddev.basemetals.init.Materials.copper;
		cupronickel = com.mcmoddev.basemetals.init.Materials.cupronickel;
		electrum = com.mcmoddev.basemetals.init.Materials.electrum;
		invar = com.mcmoddev.basemetals.init.Materials.invar;
		lead = com.mcmoddev.basemetals.init.Materials.lead;
		mithril = com.mcmoddev.basemetals.init.Materials.mithril;
		nickel = com.mcmoddev.basemetals.init.Materials.nickel;
		pewter = com.mcmoddev.basemetals.init.Materials.pewter;
		platinum = com.mcmoddev.basemetals.init.Materials.platinum;
		silver = com.mcmoddev.basemetals.init.Materials.silver;
		starsteel = com.mcmoddev.basemetals.init.Materials.starsteel;
		steel = com.mcmoddev.basemetals.init.Materials.steel;
		tin = com.mcmoddev.basemetals.init.Materials.tin;
		zinc = com.mcmoddev.basemetals.init.Materials.zinc;

		vanilla_wood = com.mcmoddev.basemetals.init.Materials.vanilla_wood;
		vanilla_stone = com.mcmoddev.basemetals.init.Materials.vanilla_stone;
		vanilla_iron = com.mcmoddev.basemetals.init.Materials.vanilla_iron;
		vanilla_gold = com.mcmoddev.basemetals.init.Materials.vanilla_gold;
		vanilla_diamond = com.mcmoddev.basemetals.init.Materials.vanilla_diamond;

		initDone = true;
	}
}
