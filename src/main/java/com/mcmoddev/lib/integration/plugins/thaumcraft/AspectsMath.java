package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterialType;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectsMath {
    private AspectsMath() {
    }

    public static AspectList addAspects(AspectList aspectList, MMDMaterial material, Names name){
        return addAspects(aspectList, material, Thaumcraft.getFromNameToMultiplier(name));
    }

    private static AspectList addAspects(AspectList aspectList, MMDMaterial material, float multiplier) {
        addMetalAspect(aspectList, material, multiplier);
        addCrystalAspect(aspectList, material, multiplier);
        addMagicAspect(aspectList, material, multiplier);
        addDesireAspect(aspectList, material, multiplier);

        return aspectList;
    }

    private static AspectList addMetalAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        if(material.getType() == MMDMaterialType.MaterialType.METAL){
            aspectList.add(Aspect.METAL, getMetalAspect(material, multiplier));
        }
        return aspectList;
    }

    private static int getMetalAspect(MMDMaterial material, float multiplier){

        // TODO Seriously rethink this
        float harvestLevel = material.getRequiredHarvestLevel();
        float blockHardness = material.getBlockHardness();
        if(blockHardness < 0.1f){
            blockHardness = 0.1f;
        }

        float value;
        if(harvestLevel <= 0){
            value = 0.1f * blockHardness * 240;
        }
        else if(harvestLevel < 1){
            value = harvestLevel * blockHardness * 60;
        }
        else if(harvestLevel < 2){
            value = harvestLevel * blockHardness  * 2;
        }
        else if(harvestLevel < 3){
            value = harvestLevel * blockHardness / 1.5f;
        }
        else{
            value = harvestLevel * blockHardness / 1.2f;
        }

        if(material.isAlloy()){
            value *= 3;
        }

        value = value / 27 * multiplier +1;

        if(value < 2){
            value += 1;
        }

        return (int)(value);
    }

    private static AspectList addCrystalAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        if(material.getType() == MMDMaterialType.MaterialType.CRYSTAL || material.getType() == MMDMaterialType.MaterialType.GEM){
            aspectList.add(Aspect.CRYSTAL, getMetalAspect(material, multiplier));
        }
        return aspectList;
    }

    private static AspectList addDesireAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getDesireAspect(material, multiplier);
        if(value > 0){
            aspectList.add(Aspect.DESIRE, value);
        }
        return aspectList;
    }

    private static int getDesireAspect(MMDMaterial material, float multiplier){
        return getDesireAspect(material.isRare(), material.getEnchantability(), multiplier);
    }

    private static int getDesireAspect(boolean isRare, float enchantability, float multiplier){
        if(isRare){
            return (int)(enchantability * 2 / 45 * multiplier);
        }
        return 0;
    }

    private static AspectList addMagicAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getMagicAspect(material, multiplier);
        if(value > 0){
            aspectList.add(Aspect.MAGIC, value);
        }
        return aspectList;
    }

    private static int getMagicAspect(MMDMaterial material, float multiplier){
        return getMagicAspect(material.getEnchantability(), multiplier);
    }

    private static int getMagicAspect(float enchantability, float multiplier){
        if(enchantability >= 15){
            return (int)(enchantability / 18 *  multiplier);
        }
        return 0;
    }
}
