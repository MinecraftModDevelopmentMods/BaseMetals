package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.basemetals.items.MMDToolEffects;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDCrossbow extends ItemCrossbow implements IMMDObject {

	protected final MMDMaterial material;
	protected final String repairOreDictName;
	protected static final long REGEN_INTERVAL = 200;

	/**
	 *
	 * @param material
	 *            The material to make the crossbow from
	 */
	public ItemMMDCrossbow(MMDMaterial material) {
		this.material = material;
		this.maxStackSize = 1;
		this.setMaxDamage(this.material.getToolDurability());
		this.repairOreDictName = Oredicts.INGOT + this.material.getCapitalizedName();
	}

	@Override
	public boolean getIsRepairable(final ItemStack intputItem, final ItemStack repairMaterial) {
		final List<ItemStack> acceptableItems = OreDictionary.getOres(this.repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;

		return false;
	}

	@Override
	public void onUpdate(final ItemStack item, final World world, final Entity player, final int inventoryIndex, final boolean isHeld) {
		if (world.isRemote)
			return;

		if (this.material.regenerates() && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MMDToolEffects.addToolSpecialPropertiesToolTip(this.material, tooltip);
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
