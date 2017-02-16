package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

@BaseMetalsPlugin(DenseOres.PLUGIN_MODID)
public class DenseOres extends com.mcmoddev.lib.integration.plugins.DenseOres implements IIntegration {

	private static boolean initDone = false;
	
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableDenseOres) {
			return;
		}
		
		registerOres();
		
		initDone = true;
	}
	
	/**
	 * Register all ores that are currently known by the materials registry
	 * 
	 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
	 */
	public static void registerOres() {
		String [] baseNames = new String[] { "adamantine" , "antimony", "bismuth", "coldiron", "copper", "lead", "mercury", "nickel", "platinum", "silver", "starsteel", "tin", "zinc"};
		boolean[] baseEnabled = new boolean[] { Options.enableAdamantine, Options.enableAntimony, Options.enableBismuth, Options.enableColdIron, Options.enableCopper, Options.enableLead, Options.enableMercury, Options.enableNickel, Options.enablePlatinum, Options.enableSilver, Options.enableStarSteel, Options.enableTin, Options.enableZinc };
		for( int i = 0; i < baseNames.length; i++ ) {
			String ore = baseNames[i];
			MetalMaterial mat = Materials.getMaterialByName(ore);
			if( mat != null && baseEnabled[i] ) {
				String baseMaterial = "stone";
				switch( ore ) {
				case "starsteel":
					baseMaterial = "end_stone";
					break;
				case "adamantine":
				case "coldiron":
					baseMaterial = "netherrack";
					break;
				}
				registerOre(String.format("%s_%s", ore, Oredicts.ORE), "basemetals", baseMaterial, 0);
			}
		}
	}	
}
