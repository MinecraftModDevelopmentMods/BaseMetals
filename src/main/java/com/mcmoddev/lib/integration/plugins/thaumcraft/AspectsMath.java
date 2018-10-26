package com.mcmoddev.lib.integration.plugins.thaumcraft;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.material.MMDMaterialType.MaterialType;

import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;

public class AspectsMath {
    private AspectsMath() {
    }

    public static AspectList getAspects(TCMaterial material, Names name){
        return getAspects(material, name.toString());
    }

    public static AspectList getAspects(TCMaterial material, String name){
        return getAspects(new AspectList(), material, name);
    }

    public static AspectList getAspects(AspectList aspectListIn, TCMaterial material, String name) {
    	AspectList aspectListOut = new AspectList();
    	aspectListOut.add(aspectListIn);
    	
    	aspectListOut.add(getMaterialSpecificAspects(material, name));
    	aspectListOut.add(getPartsAspects(material, Thaumcraft.getPartMultiplier(name), name));
    	
    	return aspectListOut;
    }
    
    public static AspectList getMaterialSpecificAspects(TCMaterial material, String name) {
    	AspectList aspectListOut = new AspectList();
    	
    	material.getMaterialAspects().stream().forEach( aspect -> {
    		aspectListOut.add(aspect.getKey(), (int)aspect.getValue().apply(1.0f));
    	});
    	
    	return aspectListOut;
    }
    
    public static AspectList getPartsAspects(TCMaterial materialIn, float mult, String partName) {
    	List<Aspect> aspects = Arrays.asList(Aspect.METAL, Aspect.TOOL, Aspect.CRYSTAL, Aspect.EXCHANGE, Aspect.ORDER, Aspect.DESIRE,
    			Aspect.DARKNESS, Aspect.MAGIC, Aspect.FIRE, Aspect.EARTH);
    	AspectList result = new AspectList();
    	
    	aspects.stream().forEach( aspect -> result.add(applyAspectCount(materialIn, mult, aspect, partName)));
    	
    	return result;
    }
    
    public static AspectList applyAspectCount(TCMaterial materialIn, float mult, Aspect aspect, String partName) {
    	if (materialIn.hasCalcFor(partName)) {
    		return runAspectCalcs(materialIn, mult, aspect, partName);
    	} else {
    		return genericAspectCalcs(materialIn, mult, aspect, partName);
    	}
    }

	private static AspectList genericAspectCalcs(TCMaterial materialIn, float mult, Aspect aspect, String partName) {
		List<Aspect> possibles = new LinkedList<>();
		
		possibles.add(getBaseAspectFor(materialIn.getMMDMaterial().getType()));
		
		switch(partName.toLowerCase(Locale.ENGLISH)) {
		case "ore":
			switch(materialIn.getMMDMaterial().getDefaultDimension()) {
			case -1:
				possibles.add(Aspect.FIRE);
				break;
			case 1:
				possibles.add(Aspect.DARKNESS);
			case 0:
			default:
				possibles.add(Aspect.EARTH);
			}
			break;
		case "blend":
		case "smallblend":
			possibles.add(Aspect.EXCHANGE);
			possibles.add(Aspect.CRYSTAL);
			break;
		case "powder":
		case "smallpowder":
			possibles.add(Aspect.TOOL);
			possibles.add(Aspect.DARKNESS);
			break;
		case "gem":
			possibles.add(Aspect.CRYSTAL);
			break;
		}
		
		AspectList rv = new AspectList();
		
		possibles.stream().forEach(pa -> rv.add(genericCalcFor(materialIn, mult, pa, partName)));
		
		return rv;
	}

	private static Aspect getBaseAspectFor(MaterialType type) {
		switch(type) {
		case METAL:
			return Aspect.METAL;
		case GEM:
		case CRYSTAL:
			return Aspect.CRYSTAL;
		case ROCK:
			return Aspect.EARTH;
		case MINERAL:
			return Aspect.ENERGY;
		case WOOD:
			return Aspect.PLANT;
		default:
			return Aspect.METAL;
		}
	}

	private static AspectList runAspectCalcs(TCMaterial materialIn, float mult, Aspect aspect, String partName) {
		AspectList rv = new AspectList();
		Map<Aspect, IAspectCalculation> calcs = materialIn.getCalcsFor(partName);
		
		if (calcs.containsKey(aspect)) {
			rv.add(aspect, (int)(calcs.get(aspect).apply(mult)));
		} else {
			rv.add(genericCalcFor(materialIn, mult, aspect, partName));
		}
		
		return rv;
	}

	private static AspectList genericCalcFor(TCMaterial materialIn, float mult, Aspect aspect, String partName) {
		int baseVal = getAspectCountForMaterial(materialIn, aspect, mult);
		float partMult = Thaumcraft.getPartMultiplier(partName);
		int finalVal = (int)(baseVal * partMult);
		AspectList rv = new AspectList();
		
		if (finalVal != 0) {
			rv.add(aspect, finalVal);
		}
		
		return rv;
	}
	
	private static int getAspectCountForMaterial(TCMaterial materialIn, Aspect aspect, float multiplier) {
	        float harvestLevel = materialIn.getMMDMaterial().getRequiredHarvestLevel();
	        float blockHardness = materialIn.getMMDMaterial().getBlockHardness();
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

	        if(materialIn.getMMDMaterial().isAlloy()){
	            value *= 3;
	        }

	        value = value / 27 * multiplier +1;

	        if(value < 2){
	            value += 1;
	        }

	        return (int)(value);
	}
}
