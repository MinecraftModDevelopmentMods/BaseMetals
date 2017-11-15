package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;
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
			}
		}
		
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

	    for ( final String materialName : baseNames ) {
		    if ( Options.isMaterialEnabled( materialName ) ) {
			    addOreMultiplicationRecipes( materialName );
		    }
	    }
		
		if( Options.isMaterialEnabled(MaterialNames.DIAMOND) ) {
			MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);
			addCrusherRecipe( new ItemStack( diamond.getBlock(Names.ORE)), new ItemStack( diamond.getItem(Names.INGOT), 2));
			addCrusherRecipe( new ItemStack( diamond.getItem(Names.INGOT)), new ItemStack( diamond.getItem(Names.POWDER)));
			addPurificationChamberRecipe( new ItemStack( diamond.getBlock(Names.ORE)), new ItemStack( diamond.getItem(Names.POWDER), 2));
		}
		
		if( Options.isMaterialEnabled(MaterialNames.EMERALD) ) {
			MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);
			addCrusherRecipe( new ItemStack( emerald.getBlock(Names.ORE)), new ItemStack( emerald.getItem(Names.INGOT), 2));
			addCrusherRecipe( new ItemStack( emerald.getItem(Names.INGOT)), new ItemStack( emerald.getItem(Names.POWDER)));
			addPurificationChamberRecipe( new ItemStack( emerald.getBlock(Names.ORE)), new ItemStack( emerald.getItem(Names.POWDER), 2));
		}

    }
}
