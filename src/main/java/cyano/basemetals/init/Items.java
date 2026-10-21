package cyano.basemetals.init;

import net.minecraft.item.Item;
import zone.moddev.mc.basemetals.compat.CyanoMaterials;

/** These fields refer to current ingots, not another set of registered items. */
public final class Items {

	public static Item adamantine_ingot;
	public static Item aquarium_ingot;
	public static Item brass_ingot;
	public static Item bronze_ingot;
	public static Item coldiron_ingot;
	public static Item copper_ingot;
	public static Item cupronickel_ingot;
	public static Item electrum_ingot;
	public static Item invar_ingot;
	public static Item lead_ingot;
	public static Item mithril_ingot;
	public static Item nickel_ingot;
	public static Item platinum_ingot;
	public static Item silver_ingot;
	public static Item starsteel_ingot;
	public static Item steel_ingot;
	public static Item tin_ingot;
	public static Item zinc_ingot;

	private Items() {
	}

	public static void bind() {
		adamantine_ingot = CyanoMaterials.ingot("adamantine");
		aquarium_ingot = CyanoMaterials.ingot("aquarium");
		brass_ingot = CyanoMaterials.ingot("brass");
		bronze_ingot = CyanoMaterials.ingot("bronze");
		coldiron_ingot = CyanoMaterials.ingot("coldiron");
		copper_ingot = CyanoMaterials.ingot("copper");
		cupronickel_ingot = CyanoMaterials.ingot("cupronickel");
		electrum_ingot = CyanoMaterials.ingot("electrum");
		invar_ingot = CyanoMaterials.ingot("invar");
		lead_ingot = CyanoMaterials.ingot("lead");
		mithril_ingot = CyanoMaterials.ingot("mithril");
		nickel_ingot = CyanoMaterials.ingot("nickel");
		platinum_ingot = CyanoMaterials.ingot("platinum");
		silver_ingot = CyanoMaterials.ingot("silver");
		starsteel_ingot = CyanoMaterials.ingot("starsteel");
		steel_ingot = CyanoMaterials.ingot("steel");
		tin_ingot = CyanoMaterials.ingot("tin");
		zinc_ingot = CyanoMaterials.ingot("zinc");
	}
}
