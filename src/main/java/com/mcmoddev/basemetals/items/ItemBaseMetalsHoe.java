package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDHoe;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * Returns the ToolMaterial enum name expected by vanilla and other mods.
 * MMDLib returns a lowercase material name instead, which breaks that lookup.
 */
public final class ItemBaseMetalsHoe extends ItemMMDHoe {

	public ItemBaseMetalsHoe(final MMDMaterial material) {
		super(material);
	}

	@Override
	public String getMaterialName() {
		return Materials.getToolMaterialFor(getMMDMaterial()).toString();
	}
}
