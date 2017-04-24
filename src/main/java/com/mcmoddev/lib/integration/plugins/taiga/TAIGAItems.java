package com.mcmoddev.lib.integration.plugins.taiga;

import java.util.List;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.TabContainer;

public class TAIGAItems extends Items {
	public static boolean initDone = false;

	public static void init(List<MMDMaterial> materials) {
		for (MMDMaterial m : materials) {
			createItemsFull(m, new TabContainer(ItemGroups.blocksTab, ItemGroups.itemsTab, ItemGroups.toolsTab));
		}

		initDone = true;
	}
}
