package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

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

		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC };

		for (final String materialName : baseNames) {
			if (Materials.hasMaterial(materialName)) {
				addGassesForMaterial(materialName);
			}
		}

		initDone = true;
	}

    @SubscribeEvent
    public void regCallback(RegistryEvent.Register<IRecipe> event) {
		final String[] baseNames = new String[] { MaterialNames.ADAMANTINE, MaterialNames.ANTIMONY,
				MaterialNames.BISMUTH, MaterialNames.COLDIRON, MaterialNames.PLATINUM, MaterialNames.NICKEL,
				MaterialNames.STARSTEEL, MaterialNames.ZINC };

	    for (final String materialName : baseNames) {
		    if (Materials.hasMaterial(materialName)) {
			    addOreMultiplicationRecipes(materialName);
		    }
	    }

		if (Materials.hasMaterial(MaterialNames.DIAMOND)) {
			final MMDMaterial diamond = Materials.getMaterialByName(MaterialNames.DIAMOND);

			if (diamond.hasBlock(Names.ORE) && (diamond.hasItem(Names.INGOT))) {
				addCrusherRecipe(new ItemStack(diamond.getBlock(Names.ORE)), new ItemStack(diamond.getItem(Names.INGOT), 2));
			}
			if (diamond.hasItem(Names.INGOT) && (diamond.hasItem(Names.POWDER))) {
				addCrusherRecipe(new ItemStack(diamond.getItem(Names.INGOT)), new ItemStack(diamond.getItem(Names.POWDER)));
			}
			if (diamond.hasBlock(Names.ORE) && (diamond.hasItem(Names.POWDER))) {
				addPurificationChamberRecipe(new ItemStack(diamond.getBlock(Names.ORE)), new ItemStack(diamond.getItem(Names.POWDER), 2));
			}
		}

		if (Materials.hasMaterial(MaterialNames.EMERALD)) {
			final MMDMaterial emerald = Materials.getMaterialByName(MaterialNames.EMERALD);

			if (emerald.hasBlock(Names.ORE) && (emerald.hasItem(Names.INGOT))) {
				addCrusherRecipe(new ItemStack(emerald.getBlock(Names.ORE)), new ItemStack(emerald.getItem(Names.INGOT), 2));
			}
			if (emerald.hasItem(Names.INGOT) && (emerald.hasItem(Names.POWDER))) {
				addCrusherRecipe(new ItemStack(emerald.getItem(Names.INGOT)), new ItemStack(emerald.getItem(Names.POWDER)));
			}
			if (emerald.hasBlock(Names.ORE) && (emerald.hasItem(Names.POWDER))) {
				addPurificationChamberRecipe(new ItemStack(emerald.getBlock(Names.ORE)), new ItemStack(emerald.getItem(Names.POWDER), 2));
			}
		}
	}
}
