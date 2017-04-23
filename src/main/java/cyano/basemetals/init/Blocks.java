package cyano.basemetals.init;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

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

	private Blocks() {
		throw new IllegalAccessError("Not a instantiable class");
	}

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
		MMDMaterial material;

		if (Options.enableAdamantine) {
			material = Materials.adamantine;
			adamantine_block = material.getBlock(Names.BLOCK);
			adamantine_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableAntimony) {
			material = Materials.antimony;
			antimony_block = material.getBlock(Names.BLOCK);
			antimony_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableAquarium) {
			material = Materials.aquarium;
			aquarium_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableBismuth) {
			material = Materials.bismuth;
			bismuth_block = material.getBlock(Names.BLOCK);
			bismuth_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableBrass) {
			material = Materials.brass;
			brass_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableBronze) {
			material = Materials.bronze;
			bronze_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableColdIron) {
			material = Materials.coldiron;
			coldiron_block = material.getBlock(Names.BLOCK);
			coldiron_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableCopper) {
			material = Materials.copper;
			copper_block = material.getBlock(Names.BLOCK);
			copper_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableCupronickel) {
			material = Materials.cupronickel;
			cupronickel_block = material.getBlock(Names.BLOCK);
		}
		
		if (Options.enableElectrum) {
			material = Materials.electrum;
			electrum_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableInvar) {
			material = Materials.invar;
			invar_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableLead) {
			material = Materials.lead;
			lead_block = material.getBlock(Names.BLOCK);
			lead_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableMithril) {
			material = Materials.mithril;
			mithril_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enableNickel) {
			material = Materials.nickel;
			nickel_block = material.getBlock(Names.BLOCK);
			nickel_ore = material.getBlock(Names.ORE);
		}

		if (Options.enablePewter) {
			material = Materials.pewter;
			pewter_block = material.getBlock(Names.BLOCK);
		}

		if (Options.enablePlatinum) {
			material = Materials.platinum;
			platinum_block = material.getBlock(Names.BLOCK);
			platinum_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableSilver) {
			material = Materials.silver;
			silver_block = material.getBlock(Names.BLOCK);
			silver_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableStarSteel) {
			material = Materials.starsteel;
			starsteel_block = material.getBlock(Names.BLOCK);
			starsteel_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableSteel) {
			material = Materials.steel;
			steel_block = material.getBlock(Names.BLOCK);
			steel_plate = material.getBlock(Names.PLATE); // For Power Advantage
		}
		
		if (Options.enableTin) {
			material = Materials.tin;
			tin_block = material.getBlock(Names.BLOCK);
			tin_ore = material.getBlock(Names.ORE);
		}

		if (Options.enableZinc) {
			material = Materials.zinc;
			zinc_block = material.getBlock(Names.BLOCK);
			zinc_ore = material.getBlock(Names.ORE);
		}


		initDone = true;
	}

	/**
	 * Gets a map of all blocks added, sorted by material
	 * 
	 * @deprecated
	 * @return An unmodifiable map of added items categorized by material
	 */
	@Deprecated
	public static Map<MMDMaterial, List<Block>> getBlocksByMetal() {
		return getBlocksByMaterial();
	}
}
