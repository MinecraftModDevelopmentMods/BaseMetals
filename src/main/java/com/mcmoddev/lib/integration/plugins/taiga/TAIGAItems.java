package com.mcmoddev.lib.integration.plugins.taiga;

import java.util.List;

import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;

public class TAIGAItems extends Items {
	private static boolean initDone = false;

	public static void init(List<MMDMaterial> materials) {
		if (initDone) {
			return;
		}

		for (MMDMaterial m : materials) {
			// TODO: This probably shouldn't use the BMe tabs
			createItemsFull(m, ItemGroups.myTabs);
		}

		initDone = true;
	}
}
