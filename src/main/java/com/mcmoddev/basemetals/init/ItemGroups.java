package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {

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

		blocksTab = addTab("blocks", true, Materials.getMaterialByName(MaterialNames.IRON));
		itemsTab = addTab("items", true, Materials.getMaterialByName(MaterialNames.IRON));
		toolsTab = addTab("tools", true, Materials.getMaterialByName(MaterialNames.IRON));

		initDone = true;
	}
}
