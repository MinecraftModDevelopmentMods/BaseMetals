package com.mcmoddev.lib.material;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IMMDBurnableObject {

	void setBurnTime(int timeInTicks);

	int getItemBurnTime(@Nonnull ItemStack itemStack);
}
