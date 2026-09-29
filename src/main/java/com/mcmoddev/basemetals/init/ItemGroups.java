package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

/** Chooses representative icons for Base Metals creative tabs. */
public final class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {
	private ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	public static void init() {
	}

	/** Uses one material's block, gear, pickaxe, and sword as the four tab icons. */
	public static void setupIcons(final String materialName) {
		if (Materials.hasMaterial(materialName)) {
			final MMDMaterial material = Materials.getMaterialByName(materialName);

			if (material.hasBlock(Names.BLOCK)) {
				getTab(BaseMetals.MODID, SharedStrings.TAB_BLOCKS).setTabIconItem(material.getBlock(Names.BLOCK));
			}

			if (material.hasItem(Names.GEAR)) {
				getTab(BaseMetals.MODID, SharedStrings.TAB_ITEMS).setTabIconItem(material.getItem(Names.GEAR));
			}

			if (material.hasItem(Names.PICKAXE)) {
				getTab(BaseMetals.MODID, SharedStrings.TAB_TOOLS).setTabIconItem(material.getItem(Names.PICKAXE));
			}

			if (material.hasItem(Names.SWORD)) {
				getTab(BaseMetals.MODID, SharedStrings.TAB_COMBAT).setTabIconItem(material.getItem(Names.SWORD));
			}
		}
	}
}
