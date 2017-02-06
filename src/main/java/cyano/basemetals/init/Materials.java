package cyano.basemetals.init;

import com.mcmoddev.lib.material.MetalMaterial;

public class Materials {

	// Backwards Compatibility
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
