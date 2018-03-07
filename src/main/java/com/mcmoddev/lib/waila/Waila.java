package com.mcmoddev.lib.waila;

import com.mcmoddev.lib.block.BlockMMDLever;

import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public final class Waila {

	private Waila() {

	}

	public static void init() {
		FMLInterModComms.sendMessage("waila", "register", "com.mcmoddev.lib.waila.Waila.register");
	}

	public static void register(final IWailaRegistrar registrar) {
		registrar.registerBodyProvider(new LeverInfoController(), BlockMMDLever.class);
	}
}
