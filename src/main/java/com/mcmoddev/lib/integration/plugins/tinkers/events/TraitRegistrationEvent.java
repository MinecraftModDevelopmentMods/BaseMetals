package com.mcmoddev.lib.integration.plugins.tinkers.events;

import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitRegistry;

import net.minecraftforge.fml.common.eventhandler.Event;
import slimeknights.tconstruct.library.traits.ITrait;


public class TraitRegistrationEvent extends Event {
	private TinkerTraitRegistry registry;
	
	public TraitRegistrationEvent(TinkerTraitRegistry traitRegistry) {
		this.registry = traitRegistry;
	}
	
	public TinkerTraitRegistry getRegistry() {
		return this.registry;
	}
	
	public void register(String name, ITrait trait) {
		registry.register(name, trait);
	}
}
