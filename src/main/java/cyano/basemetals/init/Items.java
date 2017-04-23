package cyano.basemetals.init;

import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

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
//	public static Item charcoal_powder;
//	public static Item charcoal_smallpowder;
//	public static Item coal_powder;
//	public static Item coal_smallpowder;

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

	private Items() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	public static void init() {
		if (initDone) {
			return;
		}

		MMDMaterial material;

		if (Options.enableAdamantine) {
			material = Materials.adamantine;
			adamantine_boots = material.getItem(Names.BOOTS);
			adamantine_chestplate = material.getItem(Names.CHESTPLATE);
			adamantine_helmet = material.getItem(Names.HELMET);
			adamantine_ingot = material.getItem(Names.INGOT);
			adamantine_leggings = material.getItem(Names.LEGGINGS);
			adamantine_nugget = material.getItem(Names.NUGGET);
			adamantine_powder = material.getItem(Names.POWDER);
			adamantine_sword = material.getItem(Names.SWORD);
		}

		if (Options.enableAntimony) {
			material = Materials.antimony;
			antimony_helmet = material.getItem(Names.HELMET);
			antimony_ingot = material.getItem(Names.INGOT);
			antimony_nugget = material.getItem(Names.NUGGET);
			antimony_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableAquarium) {
			material = Materials.aquarium;
			aquarium_boots = material.getItem(Names.BOOTS);
			aquarium_chestplate = material.getItem(Names.CHESTPLATE);
			aquarium_helmet = material.getItem(Names.HELMET);
			aquarium_ingot = material.getItem(Names.INGOT);
			aquarium_leggings = material.getItem(Names.LEGGINGS);
			aquarium_nugget = material.getItem(Names.NUGGET);
			aquarium_powder = material.getItem(Names.POWDER);
			aquarium_sword = material.getItem(Names.SWORD);
		}

		if (Options.enableBismuth) {
			material = Materials.bismuth;
			bismuth_helmet = material.getItem(Names.HELMET);
			bismuth_ingot = material.getItem(Names.INGOT);
			bismuth_nugget = material.getItem(Names.NUGGET);
			bismuth_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableBrass) {
			material = Materials.brass;
			brass_helmet = material.getItem(Names.HELMET);
			brass_ingot = material.getItem(Names.INGOT);
			brass_nugget = material.getItem(Names.NUGGET);
			brass_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableBronze) {
			material = Materials.bronze;
			bronze_helmet = material.getItem(Names.HELMET);
			bronze_ingot = material.getItem(Names.INGOT);
			bronze_nugget = material.getItem(Names.NUGGET);
			bronze_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableCoal) {
			carbon_powder = Materials.vanilla_coal.getItem(Names.POWDER);
			carbon_smallpowder = Materials.vanilla_coal.getItem(Names.SMALLPOWDER);
		}

		if (Options.enableColdIron) {
			material = Materials.coldiron;
			coldiron_boots = material.getItem(Names.BOOTS);
			coldiron_chestplate = material.getItem(Names.CHESTPLATE);
			coldiron_helmet = material.getItem(Names.HELMET);
			coldiron_ingot = material.getItem(Names.INGOT);
			coldiron_leggings = material.getItem(Names.LEGGINGS);
			coldiron_nugget = material.getItem(Names.NUGGET);
			coldiron_powder = material.getItem(Names.POWDER);
			coldiron_sword = material.getItem(Names.SWORD);
		}

		if (Options.enableCopper) {
			material = Materials.copper;
			copper_helmet = material.getItem(Names.HELMET);
			copper_ingot = material.getItem(Names.INGOT);
			copper_nugget = material.getItem(Names.NUGGET);
			copper_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableCupronickel) {
			material = Materials.cupronickel;
			cupronickel_helmet = material.getItem(Names.HELMET);
			cupronickel_ingot = material.getItem(Names.INGOT);
			cupronickel_nugget = material.getItem(Names.NUGGET);
			cupronickel_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableElectrum) {
			material = Materials.electrum;
			electrum_helmet = material.getItem(Names.HELMET);
			electrum_ingot = material.getItem(Names.INGOT);
			electrum_nugget = material.getItem(Names.NUGGET);
			electrum_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableGold) {
			material = Materials.vanilla_gold;
			gold_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableInvar) {
			material = Materials.invar;
			invar_helmet = material.getItem(Names.HELMET);
			invar_ingot = material.getItem(Names.INGOT);
			invar_nugget = material.getItem(Names.NUGGET);
			invar_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableIron) {
			material = Materials.vanilla_iron;
			iron_nugget = material.getItem(Names.NUGGET);
			iron_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableLead) {
			material = Materials.lead;
			lead_boots = material.getItem(Names.BOOTS);
			lead_chestplate = material.getItem(Names.CHESTPLATE);
			lead_helmet = material.getItem(Names.HELMET);
			lead_ingot = material.getItem(Names.INGOT);
			lead_leggings = material.getItem(Names.LEGGINGS);
			lead_nugget = material.getItem(Names.NUGGET);
			lead_powder = material.getItem(Names.POWDER);
			lead_sword = material.getItem(Names.SWORD);
		}

		if (Options.enablePlatinum) {
			material = Materials.platinum;
			platinum_helmet = material.getItem(Names.HELMET);
			platinum_ingot = material.getItem(Names.INGOT);
			platinum_nugget = material.getItem(Names.NUGGET);
			platinum_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableMithril) {
			material = Materials.mithril;
			mithril_boots = material.getItem(Names.BOOTS);
			mithril_chestplate = material.getItem(Names.CHESTPLATE);
			mithril_helmet = material.getItem(Names.HELMET);
			mithril_ingot = material.getItem(Names.INGOT);
			mithril_leggings = material.getItem(Names.LEGGINGS);
			mithril_nugget = material.getItem(Names.NUGGET);
			mithril_powder = material.getItem(Names.POWDER);
			mithril_sword = material.getItem(Names.SWORD);
		}

		if (Options.enableNickel) {
			material = Materials.nickel;
			nickel_helmet = material.getItem(Names.HELMET);
			nickel_ingot = material.getItem(Names.INGOT);
			nickel_nugget = material.getItem(Names.NUGGET);
			nickel_powder = material.getItem(Names.POWDER);
		}

		if (Options.enablePewter) {
			material = Materials.pewter;
			pewter_helmet = material.getItem(Names.HELMET);
			pewter_ingot = material.getItem(Names.INGOT);
			pewter_nugget = material.getItem(Names.NUGGET);
			pewter_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableSilver) {
			material = Materials.silver;
			silver_helmet = material.getItem(Names.HELMET);
			silver_ingot = material.getItem(Names.INGOT);
			silver_nugget = material.getItem(Names.NUGGET);
			silver_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableStarSteel) {
			material = Materials.starsteel;
			starsteel_boots = material.getItem(Names.BOOTS);
			starsteel_chestplate = material.getItem(Names.CHESTPLATE);
			starsteel_helmet = material.getItem(Names.HELMET);
			starsteel_ingot = material.getItem(Names.INGOT);
			starsteel_leggings = material.getItem(Names.LEGGINGS);
			starsteel_nugget = material.getItem(Names.NUGGET);
			starsteel_powder = material.getItem(Names.POWDER);
			starsteel_sword = material.getItem(Names.SWORD);
		}

		if (Options.enableSteel) {
			material = Materials.steel;
			steel_helmet = material.getItem(Names.HELMET);
			steel_ingot = material.getItem(Names.INGOT);
			steel_nugget = material.getItem(Names.NUGGET);
			steel_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableTin) {
			material = Materials.tin;
			tin_helmet = material.getItem(Names.HELMET);
			tin_ingot = material.getItem(Names.INGOT);
			tin_nugget = material.getItem(Names.NUGGET);
			tin_powder = material.getItem(Names.POWDER);
		}

		if (Options.enableZinc) {
			material = Materials.zinc;
			createItemsFull(material);
			createItemsModSupport(material);
			zinc_helmet = material.getItem(Names.HELMET);
			zinc_ingot = material.getItem(Names.INGOT);
			zinc_nugget = material.getItem(Names.NUGGET);
			zinc_powder = material.getItem(Names.POWDER);
		}

		initDone = true;
	}

	
	/**
	 * Gets a map of all items added, sorted by material
	 *
	 * 
	 * @deprecated
	 * @return An unmodifiable map of added items categorized by metal material
	 */
	@Deprecated
	public static Map<MMDMaterial, List<Item>> getItemsByMetal() {
		return getItemsByMaterial();
	}
}
