package cyano.basemetals.proxy;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.init.Achievements;
import cyano.basemetals.init.Blocks;
import cyano.basemetals.init.DungeonLoot;
import cyano.basemetals.init.Entities;
import cyano.basemetals.init.Fluids;
import cyano.basemetals.init.ItemGroups;
import cyano.basemetals.init.Items;
import cyano.basemetals.init.Materials;
import cyano.basemetals.init.Recipes;
import cyano.basemetals.init.VillagerTrades;
import cyano.basemetals.init.WorldGen;
import cyano.basemetals.init.plugins.*;
import cyano.basemetals.util.Config;
import cyano.basemetals.util.Config.Options;
import cyano.basemetals.util.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	/**
	 *
	 */
	public void preInit(FMLPreInitializationEvent event) {

		Config.init();

		Fluids.init();
		Materials.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();

		if ((Loader.isModLoaded("EnderIO")) && Options.ENABLE_ENDER_IO) {
			EnderIO.init();
		}
		if ((Loader.isModLoaded("IC2")) && Options.ENABLE_IC2) {
			//IC2.init();
		}
		if ((Loader.isModLoaded("tconstruct")) && Options.ENABLE_TINKERS_CONSTRUCT) {
			TinkersConstruct.init();
		}
		if ((Loader.isModLoaded("Mekanism")) && Options.ENABLE_MEKANISM) {
			Mekanism.init();
		}
		if ((Loader.isModLoaded("thaumcraft")) && Options.ENABLE_THAUMCRAFT) {
			//Thaumcraft.init();
		}
		if ((Loader.isModLoaded("veinminer")) && Options.ENABLE_VEINMINER) {
			VeinMiner.init();
		}
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

	/**
	 *
	 */
	public void init(FMLInitializationEvent event) {
		Recipes.init();
		DungeonLoot.init();
		Entities.init();

		Achievements.init();

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	/**
	 *
	 */
	public void postInit(FMLPostInitializationEvent event) {
		WorldGen.init();
		Config.postInit();
	}
}
