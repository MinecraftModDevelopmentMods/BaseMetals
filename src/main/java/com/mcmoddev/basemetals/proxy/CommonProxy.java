package com.mcmoddev.basemetals.proxy;

import java.util.HashSet;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.*;
import com.mcmoddev.basemetals.util.Config;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.basemetals.util.EventHandler;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.integration.IntegrationManager;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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

	public void preInit(FMLPreInitializationEvent event) {
		BaseMetals.logger.debug("CommonProxy preInit() with event %s", event.description());

		Config.init();

		if ((Options.requireMMDOreSpawn()) && (!Loader.isModLoaded("orespawn"))) {
			final HashSet<ArtifactVersion> orespawnMod = new HashSet<>();
			orespawnMod.add(new DefaultArtifactVersion("3.1.0"));
			throw new MissingModsException(orespawnMod, "orespawn", "MMD Ore Spawn Mod");
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

		IntegrationManager.INSTANCE.preInit(event);
		IntegrationManager.INSTANCE.runCallbacks("preInit");
	}

	public void onRemap(FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMetals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					if ((Options.isMaterialEnabled(MaterialNames.MERCURY)) && ("liquid_mercury".equals(mapping.resourceLocation.getResourcePath()))) {
						mapping.remap(Materials.getMaterialByName(MaterialNames.MERCURY).getFluidBlock());
					}
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					if ((Options.isMaterialEnabled(MaterialNames.COAL)) && ("carbon_powder".equals(mapping.resourceLocation.getResourcePath()))) {
						mapping.remap(Materials.getMaterialByName(MaterialNames.COAL).getItem(Names.POWDER));
					}
				}
			}
		}
	}

	public void init(FMLInitializationEvent event) {
		BaseMetals.logger.debug("CommonProxt init() with event %s", event.description());
		Recipes.init();

		Achievements.init();
		FuelRegistry.register();
		IntegrationManager.INSTANCE.runCallbacks("init");
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public void postInit(FMLPostInitializationEvent event) {
		BaseMetals.logger.debug("CommonProxt postInit() with event %s", event.description());
		IntegrationManager.INSTANCE.runCallbacks("postInit");
		Config.postInit();
	}
}
