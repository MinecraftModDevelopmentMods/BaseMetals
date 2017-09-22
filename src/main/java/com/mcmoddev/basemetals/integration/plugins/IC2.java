package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.integration.plugins.IC2Base;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = IC2.PLUGIN_MODID)
public class IC2 extends IC2Base implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(IC2.PLUGIN_MODID)) {
			return;
		}
		
		MinecraftForge.EVENT_BUS.register(this);
		
		initDone = true;
	}
	
    @SubscribeEvent
    public void regCallback(RegistryEvent.Register<IRecipe> ev) {
		final String[] baseNames = new String[] {
				MaterialNames.ADAMANTINE,
				MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH,
				MaterialNames.COLDIRON,
				MaterialNames.PLATINUM,
				MaterialNames.NICKEL,
				MaterialNames.STARSTEEL,
				MaterialNames.ZINC
		};

		for (int i = 0; i < baseNames.length; i++) {
			final String materialName = baseNames[i];
			if (Options.isMaterialEnabled(materialName)) {
				registerVanillaRecipes(materialName);
				addMaceratorRecipes(materialName);
				addOreWashingPlantRecipes(materialName);
				addThermalCentrifugeRecipes(materialName);
				addMetalFormerRecipes(materialName);
				addCompressorRecipes(materialName);
			}
		}
    }

}
