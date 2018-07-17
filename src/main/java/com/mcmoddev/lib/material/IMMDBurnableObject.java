package com.mcmoddev.lib.material;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IMMDBurnableObject {

	void setBurnTime(final int timeInTicks);
	int getItemBurnTime(@Nonnull final ItemStack itemStack);
}
