package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDFishingRod extends net.minecraft.item.ItemFishingRod implements IMMDObject {

	private final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the fishing rod from
	 */
	public ItemMMDFishingRod(final MMDMaterial material) {
		super();
		this.material = material;
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		return MMDItemHelper.isToolRepairable(repairMaterial, this.material.getCapitalizedName());
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player,
			final int inventoryIndex, final boolean isHeld) {
		MMDItemHelper.doRegeneration(item, world, isHeld, this.material.regenerates());
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer player,
			final List<String> tooltip, final boolean b) {
		super.addInformation(stack, player, tooltip, b);
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material.getName(), tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
