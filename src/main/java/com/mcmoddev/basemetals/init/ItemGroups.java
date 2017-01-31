package com.mcmoddev.basemetals.init;

//import net.minecraft.creativetab.CreativeTabs;

import cyano.basemetals.init.Materials;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author DrCyano
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

//	public static CreativeTabs tab_blocks;
//	public static CreativeTabs tab_items;
//	public static CreativeTabs tab_tools;

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		tab_blocks = addTab("blocks", true, Materials.vanilla_iron);
		tab_items = addTab("items", true, Materials.vanilla_iron);
		tab_tools = addTab("tools", true, Materials.vanilla_iron);

		initDone = true;
	}
}
