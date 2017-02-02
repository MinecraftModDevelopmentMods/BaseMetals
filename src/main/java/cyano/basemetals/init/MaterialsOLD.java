package cyano.basemetals.init;

import com.mcmoddev.lib.material.MetalMaterial;

public abstract class MaterialsOLD extends com.mcmoddev.lib.init.Materials {

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

	public static void init() {
		if (initDone) {
			return;
		}

		adamantine = cyano.basemetals.init.Materials.adamantine;
		antimony = cyano.basemetals.init.Materials.antimony;
		aquarium = cyano.basemetals.init.Materials.aquarium;
		bismuth = cyano.basemetals.init.Materials.bismuth;
		brass = cyano.basemetals.init.Materials.brass;
		bronze = cyano.basemetals.init.Materials.bronze;
		coldiron = cyano.basemetals.init.Materials.coldiron;
		copper = cyano.basemetals.init.Materials.copper;
		cupronickel = cyano.basemetals.init.Materials.cupronickel;
		electrum = cyano.basemetals.init.Materials.electrum;
		invar = cyano.basemetals.init.Materials.invar;
		lead = cyano.basemetals.init.Materials.lead;
		mithril = cyano.basemetals.init.Materials.mithril;
		nickel = cyano.basemetals.init.Materials.nickel;
		pewter = cyano.basemetals.init.Materials.pewter;
		platinum = cyano.basemetals.init.Materials.platinum;
		silver = cyano.basemetals.init.Materials.silver;
		starsteel = cyano.basemetals.init.Materials.starsteel;
		steel = cyano.basemetals.init.Materials.steel;
		tin = cyano.basemetals.init.Materials.tin;
		zinc = cyano.basemetals.init.Materials.zinc;

		vanilla_wood = cyano.basemetals.init.Materials.vanilla_wood;
		vanilla_stone = cyano.basemetals.init.Materials.vanilla_stone;
		vanilla_iron = cyano.basemetals.init.Materials.vanilla_iron;
		vanilla_gold = cyano.basemetals.init.Materials.vanilla_gold;
		vanilla_diamond = cyano.basemetals.init.Materials.vanilla_diamond;

		initDone = true;
	}
}
