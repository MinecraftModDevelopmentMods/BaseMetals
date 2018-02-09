package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

// ItemMMDArmor (Not doRegeneration)
// ItemMMDAxe
// ItemMMDBow
// ItemMMDCrackhammer
// ItemMMDCrossbow
// ItemMMDFishingRod
// ItemMMDHoe
// ItemMMDPickaxe
// ItemMMDShears
// ItemMMDShield
// ItemMMDShovel
// ItemMMDSickle
// ItemMMDSword
public class MMDItemHelper {

	protected static final long REGEN_INTERVAL = 200;

	/**
     * Return whether this item is repairable in an anvil.
     */
	public static boolean isToolRepairable(final ItemStack repairMaterial, String repairMatName) {
		final String repairOreDictName = Oredicts.INGOT + repairMatName;
		final List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for (final ItemStack i : acceptableItems)
			if (ItemStack.areItemsEqual(i, repairMaterial))
				return true;
		return false;
	}

	public static void doRegeneration(final ItemStack item, final World world, final boolean isHeld, boolean regenerates) {
		if (world.isRemote)
			return;

		if (regenerates && isHeld && (item.getItemDamage() > 0) && ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}
}
