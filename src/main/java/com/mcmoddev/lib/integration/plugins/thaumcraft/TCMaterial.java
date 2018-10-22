package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import net.minecraftforge.registries.IForgeRegistryEntry;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class TCMaterial extends IForgeRegistryEntry.Impl<TCMaterial> implements IMMDObject {
    private MMDMaterial baseMaterial;
    private AspectList aspectList;

    public TCMaterial(MMDMaterial baseMaterial) {
        this.baseMaterial = baseMaterial;
        this.aspectList = new AspectList();
    }

    public TCMaterial(MMDMaterial baseMaterial, AspectList aspectList) {
        this.baseMaterial = baseMaterial;
        this.aspectList = aspectList;
    }

    public AspectList addAspect(Aspect aspect, int amount){
        return this.aspectList.add(aspect, amount);
    }

    public AspectList addAspect(AspectList aspectList){
        return this.aspectList.add(aspectList);
    }

    public String getName(){
        return this.baseMaterial.getName();
    }

    @Override
    public MMDMaterial getMMDMaterial() {
        return baseMaterial;
    }
}
