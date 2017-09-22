package com.mcmoddev.lib.integration.plugins.mekanism;

import mekanism.api.gas.Gas;
import net.minecraftforge.fluids.Fluid;

public class MMDMekGas extends Gas {
	private MMDMekGas() {
		super("this shouldn't ever be called", "I exist to satisfy Eclipse");
	}
	
	public MMDMekGas(Fluid f) {
		super(f);
	}
	
	public String getName() {
		return "clean"+super.getName();
	}
}
