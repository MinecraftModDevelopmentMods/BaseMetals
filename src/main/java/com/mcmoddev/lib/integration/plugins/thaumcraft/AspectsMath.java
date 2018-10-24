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

    public static AspectList getAspects(MMDMaterial material, Names name){
        return getAspects(material, name.toString());
    }

    public static AspectList getAspects(MMDMaterial material, String name){
        return getAspects(new AspectList(), material, name);
    }

    public static AspectList getAspects(AspectList aspectList, MMDMaterial material, String name){
        aspectList.add(getPartsAspects(material, Thaumcraft.getPartMultiplier(name)));
        if(name.contentEquals(Names.ORE.toString())){
            aspectList.add(getOreAspect(material));
        }
        else if(name.contentEquals(Names.BLEND.toString()) || name.contentEquals(Names.SMALLBLEND.toString())){
            aspectList.add(Aspect.EXCHANGE, 2).add(Aspect.CRYSTAL, 1);
        }
        else if(name.contentEquals(Names.POWDER.toString()) || name.contentEquals(Names.SMALLPOWDER.toString())){
            aspectList.add(Aspect.DARKNESS, 1).add(Aspect.TOOL, 5);
        }
        return aspectList;
    }

    private static AspectList getPartsAspects(MMDMaterial material, float multiplier){
        return getPartsAspects(new AspectList(), material, multiplier);
    }

    private static AspectList getPartsAspects(AspectList aspectList, MMDMaterial material, float multiplier) {
        getMetalAspect(aspectList, material, multiplier);
        getCrystalAspect(aspectList, material, multiplier);
        getMagicAspect(aspectList, material, multiplier);
        getDesireAspect(aspectList, material, multiplier);

        return aspectList;
    }

    private static AspectList getMetalAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getMetalAspectAmount(material, multiplier);
        if(value > 0){
            if(material.getType() == MMDMaterialType.MaterialType.METAL){
                aspectList.add(Aspect.METAL, value);
            }
        }
        return aspectList;
    }

    private static int getMetalAspectAmount(MMDMaterial material, float multiplier){
        // TODO Seriously rethink this
        if(multiplier <= 0f){
            return 0;
        }

        float harvestLevel = material.getRequiredHarvestLevel();
        float blockHardness = material.getBlockHardness();
        if(blockHardness < 0.1f){
            blockHardness = 0.1f;
        }

        float value;
        if(harvestLevel <= 0f){
            value = 0.1f * blockHardness * 240;
        }
        else if(harvestLevel < 1f){
            value = harvestLevel * blockHardness * 60;
        }
        else if(harvestLevel < 2f){
            value = harvestLevel * blockHardness  * 2;
        }
        else if(harvestLevel < 3f){
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

    private static AspectList getCrystalAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getMetalAspectAmount(material, multiplier);
        if(value > 0){
            if(material.getType() == MMDMaterialType.MaterialType.CRYSTAL || material.getType() == MMDMaterialType.MaterialType.GEM){
                aspectList.add(Aspect.CRYSTAL, getMetalAspectAmount(material, multiplier));
            }
        }
        return aspectList;
    }

    private static AspectList getDesireAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getDesireAspectAmount(material, multiplier);
        if(value > 0){
            aspectList.add(Aspect.DESIRE, value);
        }
        return aspectList;
    }

    private static int getDesireAspectAmount(MMDMaterial material, float multiplier){
        return getDesireAspectAmount(material.isRare(), material.getEnchantability(), multiplier);
    }

    private static int getDesireAspectAmount(boolean isRare, float enchantability, float multiplier){
        if(isRare){
            return (int)(enchantability * 2 / 45 * multiplier);
        }
        return 0;
    }

    private static AspectList getMagicAspect(AspectList aspectList, MMDMaterial material, float multiplier){
        int value = getMagicAspectAmount(material, multiplier);
        if(value > 0){
            aspectList.add(Aspect.MAGIC, value);
        }
        return aspectList;
    }

    private static int getMagicAspectAmount(MMDMaterial material, float multiplier){
        return getMagicAspectAmount(material.getEnchantability(), multiplier);
    }

    private static int getMagicAspectAmount(float enchantability, float multiplier){
        if(enchantability >= 15){
            return (int)(enchantability / 18 *  multiplier);
        }
        return 0;
    }

    private static AspectList getOreAspect(MMDMaterial material){
        return getOreAspect(new AspectList(), material);
    }

    private static AspectList getOreAspect(AspectList aspectList, MMDMaterial material){
        switch (material.getDefaultDimension()) {
            case -1:
                aspectList.add(Aspect.FIRE, getOreAspectAmount());
                break;
            case 0:
                aspectList.add(Aspect.EARTH, getOreAspectAmount());
                break;
            case 1:
                aspectList.add(Aspect.EARTH, getOreAspectAmount()).add(Aspect.DARKNESS, getOreAspectAmount());
                break;
            default:
                aspectList.add(Aspect.EARTH, getOreAspectAmount());
                break;
        }
        return aspectList;
    }

    private static int getOreAspectAmount(){
        return 5;
    }
}
