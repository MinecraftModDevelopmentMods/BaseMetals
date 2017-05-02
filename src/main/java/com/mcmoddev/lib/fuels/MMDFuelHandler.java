package com.mcmoddev.lib.fuels;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.IFuelHandler;

class MMDFuelHandler implements IFuelHandler {

	public MMDFuelHandler() {
		// TODO - STUB
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		if (FuelRegistry.getFuels().containsKey(fuel.getItem())) {
			return FuelRegistry.getFuels().get(fuel.getItem());
		}

		return 0;
	}

}
