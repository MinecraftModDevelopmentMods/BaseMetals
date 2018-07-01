package com.mcmoddev.lib.material;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IMMDBurnableObject {

	public void setBurnTime(final int timeInTicks);

	public int getItemBurnTime(@Nonnull final ItemStack itemStack);
}
