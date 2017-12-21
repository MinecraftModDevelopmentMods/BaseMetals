package com.mcmoddev.basemetals.proxy;

import java.util.HashSet;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.*;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.basemetals.util.EventHandler;
import com.mcmoddev.lib.data.Names;
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

		if ((Options.requireMMDOreSpawn()) && (!Loader.isModLoaded("orespawn"))) {
			if(Options.fallbackOrespawn()) {
				GameRegistry.registerWorldGenerator(new FallbackGenerator(), 0);
			} else {
				final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
				orespawnMod.add(new DefaultArtifactVersion("3.2.0"));
				throw new MissingModsException(orespawnMod, "orespawn", "MMD Ore Spawn Mod (fallback generator disabled, MMD OreSpawn enabled)");
			}
		}

		Materials.init();
		FuelRegistry.init();
		Fluids.init();
		ItemGroups.init();
		Blocks.init();
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
		Recipes.init();

		Achievements.init();
		FuelRegistry.register();
		IntegrationManager.INSTANCE.runCallbacks("init");
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		allsGood = true;

		// by this point all materials should have been registered both with MMDLib and Minecraft
		// move to a separate function - potentially in FallbackGeneratorData - after the test
		for (MMDMaterial mat : Materials.getAllMaterials()) {
			if (mat.hasBlock(Names.ORE)){
				FallbackGeneratorData.getInstance().addMaterial(mat.getName(), Names.ORE.toString(), mat.getDefaultDimension());

				if (mat.hasBlock(Names.NETHERORE))
					FallbackGeneratorData.getInstance().addMaterial(mat.getName(), Names.NETHERORE.toString(), -1);

				if (mat.hasBlock(Names.ENDORE))
					FallbackGeneratorData.getInstance().addMaterial(mat.getName(), Names.ENDORE.toString(), 1);
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
