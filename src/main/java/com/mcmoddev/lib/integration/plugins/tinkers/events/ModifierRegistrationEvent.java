package com.mcmoddev.lib.integration.plugins.tinkers.events;

import com.mcmoddev.lib.integration.plugins.tinkers.TinkerModifierRegistry;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import slimeknights.tconstruct.tools.modifiers.ToolModifier;

public class ModifierRegistrationEvent extends Event {
	private TinkerModifierRegistry registry;
	
	public ModifierRegistrationEvent(TinkerModifierRegistry registry) {
		this.registry = registry;
	}
	
	public TinkerModifierRegistry getRegistry() {
		return this.registry;
	}
	
	public void register(ResourceLocation resourceLocation, ToolModifier modifier) {
		this.registry.register(resourceLocation, modifier);
	}
}
