package com.mcmoddev.lib.integration.plugins.thaumcraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.mcmoddev.lib.data.NameToken;
import com.mcmoddev.lib.data.NameTokens;
import com.mcmoddev.lib.material.MMDMaterial;

import thaumcraft.api.aspects.Aspect;

public class BaseAspectGetter implements IAspectGetter {
	private final MMDMaterial wrapped;
	
	private final IAspectCalculation genericCalc = (multiplier) -> doGenericCalc(multiplier);
	private final IAspectCalculation genericMagicCalc = (multiplier) -> doGenericMagicCalc(multiplier);
	private final IAspectCalculation fixedValue = (multiplier) -> 5;
	
	public BaseAspectGetter(MMDMaterial baseClass) {
		this.wrapped = baseClass;
	}

	@Override
	public IAspectCalculation getCalcFor(Aspect aspect) {
		if(aspect.equals(Aspect.MAGIC))
			return genericMagicCalc;
		
		return genericCalc;
	}
	
	@Override
	public List<Pair<Aspect, IAspectCalculation>> getAspectForPart(NameToken part) {
		List<Pair<Aspect, IAspectCalculation>> rv = new ArrayList<>();
		
		if(part.equals(MATERIAL_WIDE)) {
			switch(wrapped.getType()) {
			case METAL:
				rv.add(Pair.of(Aspect.METAL, fixedValue));
				break;
			case CRYSTAL:
				rv.add(Pair.of(Aspect.CRYSTAL, fixedValue));
				break;
			case GEM:
				rv.add(Pair.of(Aspect.CRYSTAL, fixedValue));
				rv.add(Pair.of(Aspect.DESIRE, fixedValue));
				break;
			case MINERAL:
				rv.add(Pair.of(Aspect.ALCHEMY, fixedValue));
				break;
			case ROCK:
				rv.add(Pair.of(Aspect.ORDER, fixedValue));
				break;
			case WOOD:
				rv.add(Pair.of(Aspect.PLANT, fixedValue));
				break;
			default:
				return Collections.emptyList();
			}
		} else if( part.equals(NameTokens.ORE) && 
				(wrapped.getDefaultDimension() == 0 || 
				wrapped.getDefaultDimension() == Integer.MIN_VALUE) ) {
			rv.add(Pair.of(Aspect.EARTH, fixedValue));
		} else if(part.equals(NameTokens.NETHERORE) || wrapped.getDefaultDimension() == -1) {
			rv.add(Pair.of(Aspect.FIRE, fixedValue));
		} else if(part.equals(NameTokens.ENDORE) || wrapped.getDefaultDimension() == 1) {
			rv.add(Pair.of(Aspect.DARKNESS, fixedValue));
		} else if( part.equals(NameTokens.BLEND) ||
				part.equals(NameTokens.SMALLBLEND) ) {
			rv.add(Pair.of(Aspect.EXCHANGE, fixedValue));
			rv.add(Pair.of(Aspect.CRYSTAL, fixedValue));
		} else if( part.equals(NameTokens.POWDER) ||
				part.equals(NameTokens.SMALLPOWDER) ) {
			rv.add(Pair.of(Aspect.TOOL, fixedValue));
			rv.add(Pair.of(Aspect.DARKNESS, fixedValue));
		} else if( part.equals(NameTokens.GEM) ||
				part.equals(NameTokens.CRYSTAL) ||
				part.equals(NameTokens.MEK_CRYSTAL) ) {
			rv.add(Pair.of(Aspect.CRYSTAL, fixedValue));
		}
		
		return rv.isEmpty()?Collections.emptyList():rv;
	}
	
	private int doGenericMagicCalc(float multiplier){
    	float enchantability = wrapped.getEnchantability();
    	float harvestLevel = wrapped.getRequiredHarvestLevel();

    	return (int)(enchantability * harvestLevel * multiplier / 10);
	}

	private int doGenericCalc(float multiplier) {
        float harvestLevel = wrapped.getRequiredHarvestLevel();
        float blockHardness = wrapped.getBlockHardness();
        if(blockHardness < 0.1f){
            blockHardness = 0.1f;
        }
        float value;
        if(harvestLevel <= 0f){
            value = 0.1f * blockHardness * 20;
        }
        else if(harvestLevel < 1f){
            value = harvestLevel * blockHardness * 5;
        }
        else if(harvestLevel < 2f){
            value = harvestLevel * blockHardness  * 1.2f;
        }
        else if(harvestLevel < 3f){
            value = harvestLevel * blockHardness / 4f;
        }
        else{
            value = harvestLevel * blockHardness / 6f;
        }

        if(wrapped.isAlloy()){
            value *= 3;
        }

        value = value / 27 * multiplier +1;

        if(value < 2 && value > 0){
            value += 1;
        }

        return (int)(value);
	}
}
