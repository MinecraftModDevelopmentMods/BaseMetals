package com.mcmoddev.lib.waila;

import com.mcmoddev.lib.block.BlockMMDLever;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class Waila {

	private Waila() {

	}

	public static void init() {
		FMLInterModComms.sendMessage("Waila", "register", "com.mcmoddev.basemetals.waila.Waila.register");
	}

	public static void register(IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new LeverInfoController(), BlockMMDLever.class);
	}
}
