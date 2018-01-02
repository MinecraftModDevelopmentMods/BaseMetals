package com.mcmoddev.lib.item;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

/**
 * Gears
 * 
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDGear extends GenericMMDItem {

	/**
	 *
	 * @param material
	 *            The material to make the gear from
	 */
	public ItemMMDGear(MMDMaterial material) {
		super(material);
		if (material.equals(Materials.getMaterialByName(MaterialNames.WOOD))) {
			setBurnTime(300); // Coal has 80 second burn, doing 8 items - that's 10s/it or 200t/it - Planks
								// are reported at 1.5 items per plank, ie: 300 ticks. So we're using that
								// as the burn-time for gears
		}
	}
}
