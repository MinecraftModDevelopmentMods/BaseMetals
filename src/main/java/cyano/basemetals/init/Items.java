package cyano.basemetals.init;

import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.item.Item;

public class Items extends com.mcmoddev.basemetals.init.Items {
	// Backwards Compatibility
	public static Item adamantine_boots;
	public static Item adamantine_chestplate;
	public static Item adamantine_helmet;
	public static Item adamantine_ingot;
	public static Item adamantine_leggings;
	public static Item adamantine_nugget;
	public static Item adamantine_powder;
	public static Item adamantine_sword;

	public static Item antimony_helmet;
	public static Item antimony_ingot;
	public static Item antimony_nugget;
	public static Item antimony_powder;

	public static Item aquarium_boots;
	public static Item aquarium_chestplate;
	public static Item aquarium_helmet;
	public static Item aquarium_ingot;
	public static Item aquarium_leggings;
	public static Item aquarium_nugget;
	public static Item aquarium_powder;
	public static Item aquarium_sword;

	public static Item bismuth_helmet;
	public static Item bismuth_ingot;
	public static Item bismuth_nugget;
	public static Item bismuth_powder;

	public static Item brass_helmet;
	public static Item brass_ingot;
	public static Item brass_nugget;
	public static Item brass_powder;

	public static Item bronze_helmet;
	public static Item bronze_ingot;
	public static Item bronze_nugget;
	public static Item bronze_powder;

	public static Item carbon_powder;
	public static Item carbon_smallpowder;
	public static Item charcoal_powder;
	public static Item charcoal_smallpowder;
	public static Item coal_powder;
	public static Item coal_smallpowder;

	public static Item coldiron_boots;
	public static Item coldiron_chestplate;
	public static Item coldiron_helmet;
	public static Item coldiron_ingot;
	public static Item coldiron_leggings;
	public static Item coldiron_nugget;
	public static Item coldiron_powder;
	public static Item coldiron_sword;

	public static Item copper_crackhammer;
	public static Item copper_helmet;
	public static Item copper_ingot;
	public static Item copper_nugget;
	public static Item copper_powder;

	public static Item cupronickel_helmet;
	public static Item cupronickel_ingot;
	public static Item cupronickel_nugget;
	public static Item cupronickel_powder;

	public static Item electrum_helmet;
	public static Item electrum_ingot;
	public static Item electrum_nugget;
	public static Item electrum_powder;

	public static Item gold_powder;

	public static Item invar_helmet;
	public static Item invar_ingot;
	public static Item invar_nugget;
	public static Item invar_powder;

	public static Item iron_nugget;
	public static Item iron_powder;

	public static Item lead_boots;
	public static Item lead_chestplate;
	public static Item lead_helmet;
	public static Item lead_ingot;
	public static Item lead_leggings;
	public static Item lead_nugget;
	public static Item lead_powder;
	public static Item lead_sword;

//	public static Item mercury_ingot;
//	public static Item mercury_powder;

	public static Item mithril_boots;
	public static Item mithril_chestplate;
	public static Item mithril_helmet;
	public static Item mithril_ingot;
	public static Item mithril_leggings;
	public static Item mithril_nugget;
	public static Item mithril_powder;
	public static Item mithril_sword;

	public static Item nickel_helmet;
	public static Item nickel_ingot;
	public static Item nickel_nugget;
	public static Item nickel_powder;

	public static Item pewter_helmet;
	public static Item pewter_ingot;
	public static Item pewter_nugget;
	public static Item pewter_powder;

	public static Item platinum_helmet;
	public static Item platinum_ingot;
	public static Item platinum_nugget;
	public static Item platinum_powder;

	public static Item silver_helmet;
	public static Item silver_ingot;
	public static Item silver_nugget;
	public static Item silver_powder;

	public static Item starsteel_boots;
	public static Item starsteel_chestplate;
	public static Item starsteel_helmet;
	public static Item starsteel_ingot;
	public static Item starsteel_leggings;
	public static Item starsteel_nugget;
	public static Item starsteel_powder;
	public static Item starsteel_sword;

	public static Item steel_helmet;
	public static Item steel_ingot;
	public static Item steel_nugget;
	public static Item steel_powder;

	public static Item tin_helmet;
	public static Item tin_ingot;
	public static Item tin_nugget;
	public static Item tin_powder;

	public static Item zinc_helmet;
	public static Item zinc_ingot;
	public static Item zinc_nugget;
	public static Item zinc_powder;

	private static boolean initDone = false;

	public static void init() {
		if (initDone) {
			return;
		}

		MetalMaterial material;

		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;
			adamantine_boots = material.boots;
			adamantine_chestplate = material.chestplate;
			adamantine_helmet = material.helmet;
			adamantine_ingot = material.ingot;
			adamantine_leggings = material.leggings;
			adamantine_nugget = material.nugget;
			adamantine_powder = material.powder;
			adamantine_sword = material.sword;
		}

		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;
			antimony_helmet = material.helmet;
			antimony_ingot = material.ingot;
			antimony_nugget = material.nugget;
			antimony_powder = material.powder;
		}

		if (Options.ENABLE_AQUARIUM) {
			material = Materials.aquarium;
			aquarium_boots = material.boots;
			aquarium_chestplate = material.chestplate;
			aquarium_helmet = material.helmet;
			aquarium_ingot = material.ingot;
			aquarium_leggings = material.leggings;
			aquarium_nugget = material.nugget;
			aquarium_powder = material.powder;
			aquarium_sword = material.sword;
		}

		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;
			bismuth_helmet = material.helmet;
			bismuth_ingot = material.ingot;
			bismuth_nugget = material.nugget;
			bismuth_powder = material.powder;
		}

		if (Options.ENABLE_BRASS) {
			material = Materials.brass;
			brass_helmet = material.helmet;
			brass_ingot = material.ingot;
			brass_nugget = material.nugget;
			brass_powder = material.powder;
		}

		if (Options.ENABLE_BRONZE) {
			material = Materials.bronze;
			bronze_helmet = material.helmet;
			bronze_ingot = material.ingot;
			bronze_nugget = material.nugget;
			bronze_powder = material.powder;
		}

		if (Options.ENABLE_COAL) {
			carbon_powder = coal_powder;
			carbon_smallpowder = coal_smallpowder;
		}

		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;
			coldiron_boots = material.boots;
			coldiron_chestplate = material.chestplate;
			coldiron_helmet = material.helmet;
			coldiron_ingot = material.ingot;
			coldiron_leggings = material.leggings;
			coldiron_nugget = material.nugget;
			coldiron_powder = material.powder;
			coldiron_sword = material.sword;
		}

		if (Options.ENABLE_COPPER) {
			material = Materials.copper;
			copper_helmet = material.helmet;
			copper_ingot = material.ingot;
			copper_nugget = material.nugget;
			copper_powder = material.powder;
		}

		if (Options.ENABLE_CUPRONICKEL) {
			material = Materials.cupronickel;
			cupronickel_helmet = material.helmet;
			cupronickel_ingot = material.ingot;
			cupronickel_nugget = material.nugget;
			cupronickel_powder = material.powder;
		}

		if (Options.ENABLE_ELECTRUM) {
			material = Materials.electrum;
			electrum_helmet = material.helmet;
			electrum_ingot = material.ingot;
			electrum_nugget = material.nugget;
			electrum_powder = material.powder;
		}

		if (Options.ENABLE_GOLD) {
			material = Materials.vanilla_gold;
			gold_powder = material.powder;
		}

		if (Options.ENABLE_INVAR) {
			material = Materials.invar;
			invar_helmet = material.helmet;
			invar_ingot = material.ingot;
			invar_nugget = material.nugget;
			invar_powder = material.powder;
		}

		if (Options.ENABLE_IRON) {
			material = Materials.vanilla_iron;
			iron_nugget = material.nugget;
			iron_powder = material.powder;
		}

		if (Options.ENABLE_LEAD) {
			material = Materials.lead;
			lead_boots = material.boots;
			lead_chestplate = material.chestplate;
			lead_helmet = material.helmet;
			lead_ingot = material.ingot;
			lead_leggings = material.leggings;
			lead_nugget = material.nugget;
			lead_powder = material.powder;
			lead_sword = material.sword;
		}

		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;
			platinum_helmet = material.helmet;
			platinum_ingot = material.ingot;
			platinum_nugget = material.nugget;
			platinum_powder = material.powder;
		}

		if (Options.ENABLE_MITHRIL) {
			material = Materials.mithril;
			mithril_boots = material.boots;
			mithril_chestplate = material.chestplate;
			mithril_helmet = material.helmet;
			mithril_ingot = material.ingot;
			mithril_leggings = material.leggings;
			mithril_nugget = material.nugget;
			mithril_powder = material.powder;
			mithril_sword = material.sword;
		}

		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;
			nickel_helmet = material.helmet;
			nickel_ingot = material.ingot;
			nickel_nugget = material.nugget;
			nickel_powder = material.powder;
		}

		if (Options.ENABLE_PEWTER) {
			material = Materials.pewter;
			pewter_helmet = material.helmet;
			pewter_ingot = material.ingot;
			pewter_nugget = material.nugget;
			pewter_powder = material.powder;
		}

		if (Options.ENABLE_SILVER) {
			material = Materials.silver;
			silver_helmet = material.helmet;
			silver_ingot = material.ingot;
			silver_nugget = material.nugget;
			silver_powder = material.powder;
		}

		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;
			starsteel_boots = material.boots;
			starsteel_chestplate = material.chestplate;
			starsteel_helmet = material.helmet;
			starsteel_ingot = material.ingot;
			starsteel_leggings = material.leggings;
			starsteel_nugget = material.nugget;
			starsteel_powder = material.powder;
			starsteel_sword = material.sword;
		}

		if (Options.ENABLE_STEEL) {
			material = Materials.steel;
			steel_helmet = material.helmet;
			steel_ingot = material.ingot;
			steel_nugget = material.nugget;
			steel_powder = material.powder;
		}

		if (Options.ENABLE_TIN) {
			material = Materials.tin;
			tin_helmet = material.helmet;
			tin_ingot = material.ingot;
			tin_nugget = material.nugget;
			tin_powder = material.powder;
		}

		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;
			createItemsFull(material);
			createItemsModSupport(material);
			zinc_helmet = material.helmet;
			zinc_ingot = material.ingot;
			zinc_nugget = material.nugget;
			zinc_powder = material.powder;
		}

		initDone = true;
	}

	
	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * 
	 * @deprecated
	 * @return An unmodifiable map of added items catagorized by metal material
	 */
	@Deprecated
	public static Map<MetalMaterial, List<Item>> getItemsByMetal() {
		return getItemsByMaterial();
	}
}
