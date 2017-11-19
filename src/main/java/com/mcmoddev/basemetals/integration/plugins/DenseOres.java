package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = DenseOres.PLUGIN_MODID)
public class DenseOres extends com.mcmoddev.lib.integration.plugins.DenseOresBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(DenseOres.PLUGIN_MODID)) {
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
	private static void registerOres() {
		final String[] baseNames = new String[] {
				MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH,
				MaterialNames.COLDIRON,
				MaterialNames.COPPER,
				MaterialNames.LEAD,
				MaterialNames.MERCURY,
				MaterialNames.NICKEL,
				MaterialNames.PLATINUM,
				MaterialNames.SILVER,
				MaterialNames.TIN,
				MaterialNames.ZINC
		};

		for ( final String materialName : baseNames ) {
			final com.mcmoddev.lib.material.MMDMaterial material = com.mcmoddev.lib.init.Materials.getMaterialByName( materialName );
			if ( material != null && com.mcmoddev.lib.util.ConfigBase.Options.isMaterialEnabled( materialName ) ) {
				String baseMaterial;
				switch ( materialName ) {
					case com.mcmoddev.basemetals.data.MaterialNames.ADAMANTINE:
					case com.mcmoddev.basemetals.data.MaterialNames.COLDIRON:
						baseMaterial = com.mcmoddev.lib.util.Oredicts.NETHERRACK;
						break;
					default:
						baseMaterial = com.mcmoddev.lib.util.Oredicts.STONE;
				}
				registerOre( String.format( "%s_%s", materialName, com.mcmoddev.lib.util.Oredicts.ORE ), baseMaterial, 0 );
			}
		}
	}
}
