package cyano.basemetals.proxy;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.init.*;
import cyano.basemetals.integration.IntegrationManager;
import cyano.basemetals.util.Config;
import cyano.basemetals.util.Config.Options;
import cyano.basemetals.util.EventHandler;
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

		Fluids.init();
		Materials.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();

		FMLInterModComms.sendFunctionMessage("orespawn", "api", "mmd.orespawn.BaseMetalsOreSpawn");
		 
		IntegrationManager.INSTANCE.preInit(event);
	}

	public void onRemap(FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMetals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					if ((mapping.resourceLocation.getResourcePath().equals("liquid_mercury")) && (mapping.type.equals(GameRegistry.Type.BLOCK))) {
						 if (Options.ENABLE_MERCURY) {
							 mapping.remap(Fluids.fluidBlockMercury);
						 }
					}
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					if ((mapping.resourceLocation.getResourcePath().equals("carbon_powder"))) {
						 if (Options.ENABLE_COAL) {
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
//		WorldGen.init();
		Config.postInit();
	}
}
