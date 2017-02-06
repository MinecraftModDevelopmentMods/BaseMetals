package com.mcmoddev.basemetals.init;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

//	public static CreativeTabs tab_blocks;
//	public static CreativeTabs tab_items;
//	public static CreativeTabs tab_tools;

	private static boolean initDone = false;

	private ItemGroups() {
		throw new IllegalAccessError("Not a instantiable class");
	}

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
