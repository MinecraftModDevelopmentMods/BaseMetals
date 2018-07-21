package com.mcmoddev.lib.item;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

public class ItemMMDAnvilBlock extends net.minecraft.item.ItemMultiTexture implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 */
	public ItemMMDAnvilBlock(final MMDMaterial material) {
		super(material.getBlock(Names.ANVIL), material.getBlock(Names.ANVIL),
				new String[] { "intact", "slightlyDamaged", "veryDamaged" });
		this.material = material;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}

	@Override
	public int getMetadata(final int damage) {
		return damage << 2;
	}
}
