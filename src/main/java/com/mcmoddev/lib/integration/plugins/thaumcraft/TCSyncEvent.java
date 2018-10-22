package com.mcmoddev.lib.integration.plugins.thaumcraft;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.registries.IForgeRegistry;

public class TCSyncEvent extends Event {
	private final IForgeRegistry<TCMaterial> registry;
	
	public TCSyncEvent(IForgeRegistry<TCMaterial> regIn) {
		this.registry = regIn;
	}
	
	public void register(TCMaterial materialIn) {
		this.registry.register(materialIn);
	}
}
