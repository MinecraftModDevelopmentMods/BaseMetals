/**
 * 
 */
package com.mcmoddev.lib.integration.plugins.tinkers;

import com.mcmoddev.lib.material.MetalMaterial;

import slimeknights.tconstruct.library.traits.AbstractTrait;
import slimeknights.tconstruct.library.traits.ITrait;
import slimeknights.tconstruct.library.materials.MaterialTypes;

/**
 * @author madman
 *
 */
public class TCMetalMaterial {
	// integer values
	// durabilities...
	public int headDurability;
	public int bodyDurability;
	public int extraDurability;
	// Other ints...
	public int miningLevel;
	public int shaftBonusAmmo;
	
	// float values
	public float miningSpeed;
	public float headAttackDamage;
	public float bodyModifier;
	public float bowDrawingSpeed;
	public float bowRange;
	public float bowDamage;
	public float bowstringModifier;
	public float shaftModifier;
	public float fletchingAccuracy;
	public float fletchingModifier;

	private AbstractTrait[] traits = new AbstractTrait[9];
	
	private float calcDrawSpeed(int durability) {
		float val;
		if (durability < 204) {
			val = 1.0f;
		} else {
			val = ((durability - 200) + 1) / 10.0f;
			val -= Math.floor(val);
		}
		return val;
	}
	
	public TCMetalMaterial(MetalMaterial material) {
		headDurability = material.getToolDurability();
		miningSpeed = material.magicAffinity * 3 / 2;
		miningLevel = material.getToolHarvestLevel();
		headAttackDamage = material.getBaseAttackDamage() * 2;
		bodyDurability = material.getToolDurability() / 7;
		bodyModifier = (material.hardness + material.magicAffinity * 2) / 9;
		extraDurability = material.getToolDurability() / 10;
		bowDrawingSpeed = calcDrawSpeed(material.getToolDurability());
		bowDamage = material.getBaseAttackDamage() + 3;
		
		// From here on its fixed values as defaults
		bowRange = 15.0f;
		bowstringModifier = 1.0f;
		shaftModifier = 1.0f;
		fletchingAccuracy = 1.0f;
		fletchingModifier = 1.0f;
		shaftBonusAmmo = 1;
	}
	
	public void addTrait(ITrait trait) {
		addTrait(trait, null);
	}
	
	private int getIndexForName( String name ) {
		switch( name.toLowerCase() ) {
		case MaterialTypes.HEAD:
			return 1;
		case MaterialTypes.HANDLE:
			return 2;
		case MaterialTypes.EXTRA:
			return 3;
		case MaterialTypes.BOW:
			return 4;
		case MaterialTypes.BOWSTRING:
			return 5;
		case MaterialTypes.PROJECTILE:
			return 6;
		case MaterialTypes.SHAFT:
			return 7;
		case MaterialTypes.FLETCHING:
			return 8;
		default:
			return 128;
		}
	}
	
	public void addTrait(ITrait trait, String loc) {
		if( loc == null ) {
			if( traits[0] != null ) return;
			traits[0] = (AbstractTrait)trait;
		}
		
		int iLoc = getIndexForName(loc);
		if( iLoc > 8 ) throw new Error("Unkown MaterialType "+loc+" used for specifying a TiC Trait!");
		
		if( traits[iLoc] != null ) return;
		traits[iLoc] = (AbstractTrait) trait;
	}
	
	public ITrait getTrait(String loc) {
		if( loc == null ) return traits[0];

		int iLoc = getIndexForName(loc);
		if( iLoc > 8 ) throw new Error("Trait for unknown MaterialType "+loc+" requested!");
		return traits[iLoc];
	}
}
