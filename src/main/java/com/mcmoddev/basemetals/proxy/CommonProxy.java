package com.mcmoddev.basemetals.proxy;

import java.util.HashSet;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.init.Blocks;
import com.mcmoddev.basemetals.init.Fluids;
import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.init.Items;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.init.Recipes;
import com.mcmoddev.basemetals.init.VillagerTrades;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.EventHandler;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.integration.IntegrationManager;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.oregen.FallbackGenerator;
import com.mcmoddev.lib.oregen.FallbackGeneratorData;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * Base Metals Common Proxy
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	public boolean allsGood = false;

	public void preInit(FMLPreInitializationEvent event) {

		Config.init();

		if ((Options.requireMMDOreSpawn()) && (!Loader.isModLoaded(SharedStrings.ORESPAWN_MODID))) {
			if (Options.fallbackOrespawn()) {
				GameRegistry.registerWorldGenerator(new FallbackGenerator(), 0);
			} else {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion(SharedStrings.ORESPAWN_VERSION));
				throw new MissingModsException(orespawnMod, SharedStrings.ORESPAWN_MODID,
						SharedStrings.ORESPAWN_MISSING_TEXT);
			}
		}

		com.mcmoddev.lib.init.Materials.init();
		Materials.init();
		FuelRegistry.init();
		Fluids.init();
		ItemGroups.init();
		com.mcmoddev.lib.init.Blocks.init();
		Blocks.init();
		com.mcmoddev.lib.init.Items.init();
		Items.init();

		// icons have to be setup after the blocks and items are initialized
		ItemGroups.setupIcons();
		VillagerTrades.init();

		FMLInterModComms.sendFunctionMessage("orespawn", "api", "com.mcmoddev.orespawn.BaseMetalsOreSpawn");

		IntegrationManager.INSTANCE.preInit(event);
		IntegrationManager.INSTANCE.runCallbacks("preInit");
		allsGood = true;
	}

	public void onRemap(FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMetals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					if ((Materials.hasMaterial(MaterialNames.MERCURY)) && ("liquid_mercury".equals(mapping.resourceLocation.getResourcePath()))) {
						mapping.remap(Materials.getMaterialByName(MaterialNames.MERCURY).getFluidBlock());
					}
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					if ((Materials.hasMaterial(MaterialNames.COAL)) && ("carbon_powder".equals(mapping.resourceLocation.getResourcePath()))) {
						mapping.remap(Materials.getMaterialByName(MaterialNames.COAL).getItem(Names.POWDER));
					}
				}
			}
		}
	}

	public void init(FMLInitializationEvent event) {
		allsGood = false;
		com.mcmoddev.lib.init.Recipes.init();
		Recipes.init();

		Achievements.init();
		FuelRegistry.register();
		IntegrationManager.INSTANCE.runCallbacks("init");
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		allsGood = true;

		// by this point all materials should have been registered both with MMDLib and
		// Minecraft
		// move to a separate function - potentially in FallbackGeneratorData - after
		// the test
		for (final MMDMaterial material : Materials.getAllMaterials()) {
			if (material.hasBlock(Names.ORE)) {
				FallbackGeneratorData.getInstance().addMaterial(material.getName(), Names.ORE.toString(),
						material.getDefaultDimension());

				if (material.hasBlock(Names.NETHERORE))
					FallbackGeneratorData.getInstance().addMaterial(material.getName(), Names.NETHERORE.toString(), -1);

				if (material.hasBlock(Names.ENDORE))
					FallbackGeneratorData.getInstance().addMaterial(material.getName(), Names.ENDORE.toString(), 1);
			}
		}
	}

	public void postInit(FMLPostInitializationEvent event) {
		allsGood = false;
		IntegrationManager.INSTANCE.runCallbacks("postInit");
		Config.postInit();
		allsGood = true;
		FallbackGeneratorData.getInstance().setup();
	}
}
