package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import net.minecraft.item.crafting.IRecipe;
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = Mekanism.PLUGIN_MODID)
public class Mekanism extends com.mcmoddev.lib.integration.plugins.MekanismBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(Mekanism.PLUGIN_MODID)) {
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
				addGassesForMaterial(materialName);
				addOreMultiplicationRecipes(materialName);
			}
		}
    }
}
