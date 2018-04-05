package com.mcmoddev.lib.item;

import java.util.List;

import com.mcmoddev.lib.data.SharedStrings;
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

	protected MMDItemHelper() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	private static final long REGEN_INTERVAL = 200;

	/**
	 * Return whether this item is repairable in an anvil.
	 *
	 * @param repairMaterial
	 *            ItemStack attempted to use for repair
	 * @param repairMatName
	 *            Name of the material used to repair
	 * @return Whether the Tool is repairable
	 */
	public static boolean isToolRepairable(final ItemStack repairMaterial,
			final String repairMatName) {
		final String repairOreDictName = Oredicts.INGOT + repairMatName;
		final List<ItemStack> acceptableItems = OreDictionary.getOres(repairOreDictName);
		for (final ItemStack i : acceptableItems) {
			if (ItemStack.areItemsEqual(i, repairMaterial)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Item Regeneration code.
	 *
	 * @param item
	 *            The Item
	 * @param world
	 *            The world
	 * @param isHeld
	 *            Is the item held
	 * @param regenerates
	 *            Whether the item Regenerates or not
	 */
	public static void doRegeneration(final ItemStack item, final World world, final boolean isHeld,
			final boolean regenerates) {
		if (world.isRemote) {
			return;
		}

		if (regenerates && isHeld && (item.getItemDamage() > 0)
				&& ((world.getTotalWorldTime() % REGEN_INTERVAL) == 0)) {
			item.setItemDamage(item.getItemDamage() - 1);
		}
	}
}
