package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * This class initializes all item groups in Base Metals.
 *
 * @author Jasmine Iwanek
 *
 */
public final class ItemGroups extends com.mcmoddev.lib.init.ItemGroups {
/*
	private static final MMDCreativeTab blocksTab = addTab(SharedStrings.TAB_BLOCKS);
	private static final MMDCreativeTab itemsTab = addTab(SharedStrings.TAB_ITEMS);
	private static final MMDCreativeTab toolsTab = addTab(SharedStrings.TAB_TOOLS);
	private static final MMDCreativeTab combatTab = addTab(SharedStrings.TAB_COMBAT);
*/
	private ItemGroups() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 * Initializer.
	 */
	public static void init() {
		// Blank Initializer
	}

	/**
	 * Sets up icons for a CreativeTab.
	 *
	 * @param materialName
	 *            Name of the preferred Material to use for Tab Icons
	 */
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
