package com.mcmoddev.lib.integration.plugins.tinkers.events;

import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;

import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;
import net.minecraftforge.registries.IForgeRegistry;


public class MaterialRegistrationEvent extends GenericEvent<TinkersMaterial> implements IContextSetter {

    private final IForgeRegistry<TinkersMaterial> registry;

    public MaterialRegistrationEvent(IForgeRegistry<TinkersMaterial> registry)
    {
        super(registry.getRegistrySuperType());
        this.registry = registry;
    }

    public IForgeRegistry<TinkersMaterial> getRegistry()
    {
        return registry;
    }
}
