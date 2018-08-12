package com.mcmoddev.lib.integration.plugins.tinkers.events;

import java.util.Optional;

import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;

import net.minecraftforge.fml.common.eventhandler.GenericEvent;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;
import net.minecraftforge.registries.IForgeRegistry;


public class TinkersAlloyRecipeEvent extends GenericEvent<TinkersMaterial> implements IContextSetter {

    private final IForgeRegistry<TinkersMaterial> registry;
    
    public TinkersAlloyRecipeEvent(IForgeRegistry<TinkersMaterial> registry) {
    	super(registry.getRegistrySuperType());
    	this.registry = registry;
    }
    
    public IForgeRegistry<TinkersMaterial> getRegistry()
    {
        return registry;
    }

    public void addAlloyRecipe(TinkersMaterial material, int outputAmount, Object...inputs) {
    	material.addAlloyRecipe(outputAmount, inputs);
    }
    
    public void addAlloyRecipe(String materialName, int outputAmount, Object...inputs) {
    	Optional<TinkersMaterial> res = this.registry.getEntries().stream().filter(ent -> ent.getKey().getPath().equals(materialName))
    	.map(ent -> ent.getValue()).findFirst();
    	if(res.isPresent()) {
    		res.get().addAlloyRecipe(outputAmount, inputs);
    	}
    }
}
