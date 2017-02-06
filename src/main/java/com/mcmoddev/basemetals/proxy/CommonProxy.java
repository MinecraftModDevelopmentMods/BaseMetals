package com.mcmoddev.basemetals.proxy;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.init.*;
import com.mcmoddev.basemetals.integration.IntegrationManager;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.EventHandler;
import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Base Metals Common Proxy
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event) {

		Config.init();

		Materials.init();
		cyano.basemetals.init.Materials.init();
		Fluids.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();

		FMLInterModComms.sendFunctionMessage("orespawn", "api", "com.mcmoddev.orespawn.BaseMetalsOreSpawn");

		IntegrationManager.INSTANCE.preInit(event);
	}

	public void onRemap(FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMetals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					if ((mapping.resourceLocation.getResourcePath().equals("liquid_mercury")) && (mapping.type.equals(GameRegistry.Type.BLOCK))) {
						 if (Options.enableMercury) {
							 mapping.remap(Fluids.fluidBlockMercury);
						 }
					}
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					if (mapping.resourceLocation.getResourcePath().equals("carbon_powder")) {
						 if (Options.enableCoal) {
							 mapping.remap(Items.coal_powder);
						 }
					}
				}
			}
		}
	}

	public void init(FMLInitializationEvent event) {
		Recipes.init();
		//DungeonLoot.init();
		//Entities.init();

		Achievements.init();

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		//WorldGen.init();
		Config.postInit();
	}
}
