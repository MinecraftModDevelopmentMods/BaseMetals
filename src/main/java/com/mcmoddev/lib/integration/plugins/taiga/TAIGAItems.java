package com.mcmoddev.lib.integration.plugins.taiga;

import java.util.List;

import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;

public class TAIGAItems extends Items {
	public static boolean initDone = false;

	public static void init(List<MMDMaterial> materials) {
		for (MMDMaterial m : materials) {
			createItemsFull(m);
		}

		initDone = true;
	}
}
