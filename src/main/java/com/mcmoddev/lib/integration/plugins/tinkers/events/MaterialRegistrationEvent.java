package com.mcmoddev.lib.integration.plugins.tinkers.events;

import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.registries.IForgeRegistry;


public class MaterialRegistrationEvent extends Event {

    private final IForgeRegistry<TinkersMaterial> registry;

    public MaterialRegistrationEvent(IForgeRegistry<TinkersMaterial> registry)
    {
        this.registry = registry;
    }

    public IForgeRegistry<TinkersMaterial> getRegistry()
    {
        return registry;
    }
}
