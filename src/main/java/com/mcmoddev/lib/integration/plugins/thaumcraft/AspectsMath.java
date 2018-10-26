package com.mcmoddev.lib.integration.plugins.thaumcraft;

import com.mcmoddev.basemetals.data.MaterialNames;
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
        // Generic Material and Part handling
        aspectList.add(getPartsAspects(material, Thaumcraft.getPartMultiplier(name)));

        aspectList.add(getMaterialSpecificAspects(material, name, Thaumcraft.getPartMultiplier(name)));

        aspectList.add(getPartSpecificAspects(material, name, Thaumcraft.getPartMultiplier(name)));

        return aspectList;
    }

    private static AspectList getPartSpecificAspects(MMDMaterial material, String part, float multiplier){
        return getPartSpecificAspects(new AspectList(), material, part, multiplier);
    }

    private static AspectList getPartSpecificAspects(AspectList aspectList, MMDMaterial material, String part, float multiplier){
        // Specific Part handling
        if(part.contentEquals(Names.ORE.toString())){
            aspectList.add(getOreAspect(material));
        }
        else if(part.contentEquals(Names.BLEND.toString()) || part.contentEquals(Names.SMALLBLEND.toString())){
            aspectList.add(Aspect.EXCHANGE, 2).add(Aspect.CRYSTAL, 1);
        }
        else if(part.contentEquals(Names.POWDER.toString()) || part.contentEquals(Names.SMALLPOWDER.toString())){
            aspectList.add(Aspect.DARKNESS, 1).add(Aspect.TOOL, 5);
        }

        return aspectList;
    }

    private static AspectList getMaterialSpecificAspects(MMDMaterial material, String part, float multiplier){
        return getMaterialSpecificAspects(new AspectList(), material, part, multiplier);
    }

    private static AspectList getMaterialSpecificAspects(AspectList aspectList, MMDMaterial material, String part, float multiplier){
        // Specific Material handling
        if(material.getName().contentEquals(MaterialNames.COPPER)){
            aspectList.add(Aspect.EXCHANGE, 5);
        }
        else if(material.getName().contentEquals(MaterialNames.TIN)){
            aspectList.add(Aspect.CRYSTAL, 5);
        }
        else if(material.getName().contentEquals(MaterialNames.BRASS)
                || material.getName().contentEquals(MaterialNames.BRONZE)){
            aspectList.add(Aspect.TOOL, 5);
        }
        else if(material.getName().contentEquals(MaterialNames.STEEL)){
            aspectList.add(Aspect.ORDER, 5);
        }
        else if(material.getName().contentEquals(MaterialNames.SILVER)
                || material.getName().contentEquals(MaterialNames.DIAMOND)
                || material.getName().contentEquals(MaterialNames.EMERALD)){
            aspectList.add(getDesireAspect(material, Thaumcraft.getPartMultiplier(part)));
        }

        return aspectList;
    }

    private static AspectList getPartsAspects(MMDMaterial material, float multiplier){
        return getPartsAspects(new AspectList(), material, multiplier);
    }

    private static AspectList getPartsAspects(AspectList aspectList, MMDMaterial material, float multiplier) {
        aspectList.add(getMetalAspect(material, multiplier));
        aspectList.add(getCrystalAspect(material, multiplier));
        aspectList.add(getMagicAspect(material, multiplier));
        aspectList.add(getDesireAspect(material, multiplier));
        return aspectList;
    }

    private static AspectList getMetalAspect(MMDMaterial material, float multiplier){
        return getMetalAspect(new AspectList(), material, multiplier);
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

    private static AspectList getCrystalAspect(MMDMaterial material, float multiplier){
        return getCrystalAspect(new AspectList(), material, multiplier);
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

    private static AspectList getDesireAspect(MMDMaterial material, float multiplier){
        return getDesireAspect(new AspectList(), material, multiplier);
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

    private static AspectList getMagicAspect(MMDMaterial material, float multiplier){
        return getMagicAspect(new AspectList(), material, multiplier);
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
