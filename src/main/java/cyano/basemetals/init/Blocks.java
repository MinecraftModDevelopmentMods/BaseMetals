package cyano.basemetals.init;

import net.minecraft.block.Block;
import zone.moddev.mc.basemetals.compat.CyanoMaterials;

/** Old field names retained for the unchanged Base Metal Golems add-on. */
public final class Blocks {

	public static Block adamantine_block;
	public static Block aquarium_block;
	public static Block brass_block;
	public static Block bronze_block;
	public static Block coldiron_block;
	public static Block copper_block;
	public static Block cupronickel_block;
	public static Block electrum_block;
	public static Block invar_block;
	public static Block lead_block;
	public static Block mithril_block;
	public static Block nickel_block;
	public static Block platinum_block;
	public static Block silver_block;
	public static Block starsteel_block;
	public static Block steel_block;
	public static Block tin_block;
	public static Block zinc_block;

	private Blocks() {
	}

	public static void bind() {
		adamantine_block = CyanoMaterials.block("adamantine");
		aquarium_block = CyanoMaterials.block("aquarium");
		brass_block = CyanoMaterials.block("brass");
		bronze_block = CyanoMaterials.block("bronze");
		coldiron_block = CyanoMaterials.block("coldiron");
		copper_block = CyanoMaterials.block("copper");
		cupronickel_block = CyanoMaterials.block("cupronickel");
		electrum_block = CyanoMaterials.block("electrum");
		invar_block = CyanoMaterials.block("invar");
		lead_block = CyanoMaterials.block("lead");
		mithril_block = CyanoMaterials.block("mithril");
		nickel_block = CyanoMaterials.block("nickel");
		platinum_block = CyanoMaterials.block("platinum");
		silver_block = CyanoMaterials.block("silver");
		starsteel_block = CyanoMaterials.block("starsteel");
		steel_block = CyanoMaterials.block("steel");
		tin_block = CyanoMaterials.block("tin");
		zinc_block = CyanoMaterials.block("zinc");
	}
}
