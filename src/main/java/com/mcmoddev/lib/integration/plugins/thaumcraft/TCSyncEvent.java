package com.mcmoddev.lib.integration.plugins.thaumcraft;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Map;

import com.mcmoddev.basemetals.init.Materials;

import net.minecraft.util.ResourceLocation;

public class TCSyncEvent extends Event {
	private final IForgeRegistry<TCMaterial> registry;
	private final Map<String, ResourceLocation> nameMapping;
	
	public TCSyncEvent(IForgeRegistry<TCMaterial> regIn, Map<String, ResourceLocation> map) {
		this.registry = regIn;
		this.nameMapping = map;
	}
	
	public void register(TCMaterial materialIn) {
		
		this.nameMapping.putIfAbsent(materialIn.getName(), materialIn.getRegistryName());
		this.registry.register(materialIn);
	}
	
	public TCMaterial getMaterial(String materialName) {
		return this.nameMapping.containsKey(materialName)?
				this.registry.getValue(this.nameMapping.get(materialName)) :
					new TCMaterial(Materials.getMaterialByName(materialName));
	}

	public boolean hasMaterial(String materialName){
		return this.nameMapping.containsKey(materialName);
	}
}
