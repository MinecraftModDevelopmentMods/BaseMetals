/**
 * 
 */
package com.mcmoddev.lib.integration.plugins.tinkers;

import com.mcmoddev.lib.material.MetalMaterial;

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
}
