package com.mcmoddev.lib.common.item;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.ItemStack;

/**
 * This interface matches the forge Item additions, please don't change.
 */
public interface IHorseArmor {

	/**
	 * Returns the {@link HorseArmorType} of the custom horse armor.
	 *
	 * @param stack
	 *            {@link ItemStack} being checked (forge compat)
	 * @return The {@link HorseArmorType} that this horse armor will have the values of.
	 */
	HorseArmorType getHorseArmorType(ItemStack stack);

	/**
	 * Returns the location of the custom horse armor texture, similar to how player armor texture
	 * works.
	 *
	 * @param horse
	 *            The Horse, which has this item equipped
	 * @param stack
	 *            The ItemStack instance of this item from the Horse
	 *
	 * @return The location of the custom horse armor
	 */
	String getHorseArmorTexture(EntityLiving horse, ItemStack stack);
}
