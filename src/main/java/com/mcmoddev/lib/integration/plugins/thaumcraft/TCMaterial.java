package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.data.Names;

import java.util.Map;
import java.util.TreeMap;

import net.minecraftforge.registries.IForgeRegistryEntry;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TCMaterial extends IForgeRegistryEntry.Impl<TCMaterial> implements IMMDObject {
    private MMDMaterial baseMaterial;
    private Map<String, AspectList> aspectMap;
    
    public TCMaterial(MMDMaterial baseMaterial) {
        this.baseMaterial = baseMaterial;
        this.aspectMap = new TreeMap<>();
        super.setRegistryName(this.baseMaterial.getRegistryName());
    }

    public TCMaterial addAspect(Names part, Aspect aspect, int amount){
    	if (this.aspectMap.containsKey(part.toString())) {
    		AspectList al = this.aspectMap.get(part.toString());
    		al.add(aspect, amount);
    		this.aspectMap.put(part.toString(), al);
    	}  else {
    		this.aspectMap.put(part.toString(), new AspectList().add(aspect, amount));
    	}
    	return this;
    }

    public TCMaterial addAspect(Names part, AspectList aspectList){
    	if (this.aspectMap.containsKey(part.toString())) {
    		AspectList al = this.aspectMap.get(part.toString());
    		al.add(aspectList);
    		this.aspectMap.put(part.toString(), al);
    	}  else {
    		this.aspectMap.put(part.toString(), aspectList);
    	}
    	return this;
    }

    public TCMaterial addAspect(String part, Aspect aspect, int amount){
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspect, amount);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, new AspectList().add(aspect, amount));
    	}
    	return this;
    }

    public TCMaterial addAspect(String part, AspectList aspectList){
    	if (this.aspectMap.containsKey(part)) {
    		AspectList al = this.aspectMap.get(part);
    		al.add(aspectList);
    		this.aspectMap.put(part, al);
    	}  else {
    		this.aspectMap.put(part, aspectList);
    	}
    	return this;
    }

    public String getName(){
        return this.baseMaterial.getName();
    }

    @Override
    public MMDMaterial getMMDMaterial() {
        return baseMaterial;
    }
}
