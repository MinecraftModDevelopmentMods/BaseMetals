package com.mcmoddev.basemetals.integration.plugins;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.ConfigBase.Options;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = EnderIO.PLUGIN_MODID)
public class EnderIO extends com.mcmoddev.lib.integration.plugins.EnderIOBase
		implements IIntegration {

	/**
	 *
	 */
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		final List<String> materials = Arrays.asList(MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY, MaterialNames.AQUARIUM, MaterialNames.BISMUTH,
				MaterialNames.BRASS, MaterialNames.BRONZE, MaterialNames.COLDIRON,
				MaterialNames.CUPRONICKEL, MaterialNames.ELECTRUM, MaterialNames.INVAR,
				MaterialNames.MITHRIL, MaterialNames.PEWTER, MaterialNames.PLATINUM,
				MaterialNames.STARSTEEL, MaterialNames.STEEL, MaterialNames.TIN,
				MaterialNames.ZINC);

		materials.stream().filter(Materials::hasMaterial)
				.filter(materialName -> !Materials.getMaterialByName(materialName).isEmpty())
				.forEach(materialName -> addSagMillRecipe(materialName, 3600));

		addSagMillRecipe(MaterialNames.COPPER, 2, MaterialNames.GOLD, 1, 3600);
		addSagMillRecipe(MaterialNames.LEAD, 2, MaterialNames.SILVER, 1, 3600);
		addSagMillRecipe(MaterialNames.NICKEL, 2, MaterialNames.PLATINUM, 1, 3600);
		addSagMillRecipe(MaterialNames.SILVER, 2, MaterialNames.LEAD, 1, 3600);
		addSagMillRecipe(MaterialNames.IRON, 2, MaterialNames.NICKEL, 1, 3600);
		
		List<Pair<Integer, Triple<String, Integer, Object[]>>> alloys = Arrays.asList(
				Pair.of(3, Triple.of(MaterialNames.AQUARIUM, 20000, 
						new Object[] { Oredicts.INGOT+"Copper", 2, Oredicts.INGOT+"Zinc", 1, Oredicts.DUST+"Prismarine", 1 })),
				Pair.of(3, Triple.of(MaterialNames.BRASS, 2000, 
						new Object[] { Oredicts.INGOT+"Copper", 2, Oredicts.INGOT+"Zinc", 1 })),
				Pair.of(4, Triple.of(MaterialNames.BRONZE, 2000, 
						new Object[] { Oredicts.INGOT+"Copper", 3, Oredicts.INGOT+"Tin", 1 })),
				Pair.of(4, Triple.of(MaterialNames.CUPRONICKEL, 3000, 
						new Object[] { Oredicts.INGOT+"Copper", 3, Oredicts.INGOT+"Nickel", 1})),
				Pair.of(3, Triple.of(MaterialNames.INVAR, 3000,
						new Object[] { Oredicts.INGOT+"Iron", 2, Oredicts.INGOT+"Nickel", 1})),
				Pair.of(2, Triple.of(MaterialNames.ELECTRUM, 2000, 
						new Object[] { Oredicts.INGOT+"Gold", 1, Oredicts.INGOT+"Silver", 1 })),
				Pair.of(2, Triple.of(MaterialNames.MITHRIL, 10000, 
						new Object[] { Oredicts.INGOT+"Silver", 2, Oredicts.INGOT+"Coldiron", 1, Oredicts.INGOT+"Mercury", 1 })),
				Pair.of(3, Triple.of(MaterialNames.PEWTER, 2000,
						new Object[] { Oredicts.INGOT+"Tin", 1, Oredicts.INGOT+"Copper", 1, Oredicts.INGOT+"Lead", 1 })),
				Pair.of(8, Triple.of(MaterialNames.STEEL, 5000,
						new Object[] { Oredicts.INGOT+"Iron", 8, "itemCoal", 1 })));
		alloys.stream()
		.filter(p -> Materials.hasMaterial(p.getRight().getLeft()))
		.forEach(p-> {
			String name = p.getRight().getLeft();
			int count = p.getLeft();
			int cost = p.getRight().getMiddle();
			Object[] recipe = p.getRight().getRight();
			
			MMDMaterial mat = Materials.getMaterialByName(name);
			addAlloySmelterAlloy(mat, cost, Oredicts.INGOT+mat.getCapitalizedName(), count, recipe);
		});
		
	}
}
