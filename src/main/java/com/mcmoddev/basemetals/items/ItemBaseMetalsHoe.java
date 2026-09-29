package com.mcmoddev.basemetals.items;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemMMDHoe;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * Base Metals hoe which exposes the real Forge tool-material name.
 *
 * <p>{@link net.minecraft.item.ItemHoe#getMaterialName()} is expected to return
 * the backing {@link net.minecraft.item.Item.ToolMaterial} enum constant. The
 * MMDLib implementation instead returns the lower-case material identifier,
 * which breaks consumers that use the vanilla contract.</p>
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
