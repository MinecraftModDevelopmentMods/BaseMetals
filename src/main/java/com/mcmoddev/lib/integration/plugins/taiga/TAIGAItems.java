package com.mcmoddev.lib.integration.plugins.taiga;

import java.util.List;

import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MetalMaterial;

public class TAIGAItems extends Items {
	public static boolean initDone = false;
	
	public static void init(List<MetalMaterial> materials) {
		for( MetalMaterial m : materials ) {
			createItemsFull(m);
		}
		
		initDone = true;
	}
}

