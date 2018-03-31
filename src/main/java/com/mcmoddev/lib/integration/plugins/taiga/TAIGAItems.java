package com.mcmoddev.lib.integration.plugins.taiga;

import java.util.List;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Items;
import com.mcmoddev.lib.material.MMDMaterial;

public class TAIGAItems extends Items {

	public static void init(final List<MMDMaterial> materials) {
		for (final MMDMaterial material : materials) {
			// TODO: This probably shouldn't use the BMe tabs

			create(Names.BLEND, material);
			create(Names.INGOT, material);
			create(Names.NUGGET, material);
			create(Names.POWDER, material);
			create(Names.SMALLBLEND, material);
			create(Names.SMALLPOWDER, material);

			create(Names.ARROW, material);
			create(Names.AXE, material);
			create(Names.BOLT, material);
			create(Names.BOOTS, material);
			create(Names.BOW, material);
			create(Names.CHESTPLATE, material);
			create(Names.CRACKHAMMER, material);
			create(Names.CROSSBOW, material);
			create(Names.DOOR, material);
			create(Names.FISHING_ROD, material);
			create(Names.HELMET, material);
			create(Names.HOE, material);
			create(Names.HORSE_ARMOR, material);
			create(Names.LEGGINGS, material);
			create(Names.PICKAXE, material);
			create(Names.SHEARS, material);
			create(Names.SHIELD, material);
			create(Names.SHOVEL, material);
			create(Names.SLAB, material);
			create(Names.SWORD, material);
			create(Names.ROD, material);
			create(Names.GEAR, material);
		}
	}
}
