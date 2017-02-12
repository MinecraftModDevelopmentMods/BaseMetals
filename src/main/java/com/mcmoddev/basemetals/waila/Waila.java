package com.mcmoddev.basemetals.waila;

import net.minecraftforge.fml.common.event.FMLInterModComms;

import com.mcmoddev.lib.blocks.BlockMetalLever;

import mcp.mobius.waila.api.IWailaRegistrar;

public class Waila {
	public static void init() {
		FMLInterModComms.sendMessage("Waila",  "register", "com.mcmoddev.basemetals.waila.Waila.register");
	}
	
	public static void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider( new leverInfoController(), BlockMetalLever.class);
	}
}
