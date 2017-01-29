package cyano.basemetals.init;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.material.MetalMaterial;
import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.block.Block;

public class Blocks extends com.mcmoddev.basemetals.init.Blocks {

	// Backwards Compatibility
	public static Block adamantine_block;
	public static Block adamantine_ore;

	public static Block antimony_block;
	public static Block antimony_ore;

	public static Block aquarium_block;

	public static Block bismuth_block;
	public static Block bismuth_ore;

	public static Block brass_block;

	public static Block bronze_block;

	public static Block coldiron_block;
	public static Block coldiron_ore;

	public static Block copper_block;
	public static Block copper_ore;

	public static Block cupronickel_block;

	public static Block electrum_block;

	public static Block invar_block;

	public static Block lead_block;
	public static Block lead_ore;

	public static Block mithril_block;

	public static Block nickel_block;
	public static Block nickel_ore;

	public static Block pewter_block;

	public static Block platinum_block;
	public static Block platinum_ore;

	public static Block silver_block;
	public static Block silver_ore;

	public static Block starsteel_block;
	public static Block starsteel_ore;

	public static Block steel_block;
	public static Block steel_plate;

	public static Block tin_block;
	public static Block tin_ore;

	public static Block zinc_block;
	public static Block zinc_ore;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		ItemGroups.init();

		@Nonnull
		MetalMaterial material;

		if (Options.ENABLE_ADAMANTINE) {
			material = Materials.adamantine;
			adamantine_block = material.block;
			adamantine_ore = material.ore;
		}

		if (Options.ENABLE_ANTIMONY) {
			material = Materials.antimony;
			antimony_block = material.block;
			antimony_ore = material.ore;
		}

		if (Options.ENABLE_AQUARIUM) {
			material = Materials.aquarium;
			aquarium_block = material.block;
		}

		if (Options.ENABLE_BISMUTH) {
			material = Materials.bismuth;
			bismuth_block = material.block;
			bismuth_ore = material.ore;
		}

		if (Options.ENABLE_BRASS) {
			material = Materials.brass;
			brass_block = material.block;
		}

		if (Options.ENABLE_BRONZE) {
			material = Materials.bronze;
			bronze_block = material.block;
		}

		if (Options.ENABLE_COLDIRON) {
			material = Materials.coldiron;
			coldiron_block = material.block;
			coldiron_ore = material.ore;
		}

		if (Options.ENABLE_COPPER) {
			material = Materials.copper;
			copper_block = material.block;
			copper_ore = material.ore;
		}

		if (Options.ENABLE_CUPRONICKEL) {
			material = Materials.cupronickel;
			cupronickel_block = material.block;
		}
		
		if (Options.ENABLE_ELECTRUM) {
			material = Materials.electrum;
			electrum_block = material.block;
		}

		if (Options.ENABLE_INVAR) {
			material = Materials.invar;
			invar_block = material.block;
		}

		if (Options.ENABLE_LEAD) {
			material = Materials.lead;
			lead_block = material.block;
			lead_ore = material.ore;
		}

		if (Options.ENABLE_MITHRIL) {
			material = Materials.mithril;
			mithril_block = material.block;
		}

		if (Options.ENABLE_NICKEL) {
			material = Materials.nickel;
			nickel_block = material.block;
			nickel_ore = material.ore;
		}

		if (Options.ENABLE_PEWTER) {
			material = Materials.pewter;
			pewter_block = material.block;
		}

		if (Options.ENABLE_PLATINUM) {
			material = Materials.platinum;
			platinum_block = material.block;
			platinum_ore = material.ore;
		}

		if (Options.ENABLE_SILVER) {
			material = Materials.silver;
			silver_block = material.block;
			silver_ore = material.ore;
		}

		if (Options.ENABLE_STARSTEEL) {
			material = Materials.starsteel;
			starsteel_block = material.block;
			starsteel_ore = material.ore;
		}

		if (Options.ENABLE_STEEL) {
			material = Materials.steel;
			steel_block = material.block;
			steel_plate = material.plate; // For Power Advantage
		}
		
		if (Options.ENABLE_TIN) {
			material = Materials.tin;
			tin_block = material.block;
			tin_ore = material.ore;
		}

		if (Options.ENABLE_ZINC) {
			material = Materials.zinc;
			zinc_block = material.block;
			zinc_ore = material.ore;
		}


		initDone = true;
	}

	/**
	 * Gets a map of all blocks added, sorted by material
	 * 
	 * @deprecated
	 * @return An unmodifiable map of added items catagorized by material
	 */
	@Deprecated
	public static Map<MetalMaterial, List<Block>> getBlocksByMetal() {
		return getBlocksByMaterial();
	}
}
