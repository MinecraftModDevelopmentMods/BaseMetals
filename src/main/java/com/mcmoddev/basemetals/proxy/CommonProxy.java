package com.mcmoddev.basemetals.proxy;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.ItemGroups;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.init.Recipes;
import com.mcmoddev.basemetals.init.VillagerTrades;
import com.mcmoddev.basemetals.util.EventHandler;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.events.*;
import com.mcmoddev.lib.integration.IntegrationManager;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.oregen.FallbackGenerator;
import com.mcmoddev.lib.oregen.FallbackGeneratorData;
import com.mcmoddev.lib.util.ConfigBase;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.Block;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.MissingModsException;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;

/**
 * Base Metals Common Proxy.
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	/**
	 *
	 * @param event
	 */
	public void preInit(final FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.BaseMetals.class);

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
		com.mcmoddev.lib.init.Blocks.init();
		com.mcmoddev.lib.init.Items.init();
		com.mcmoddev.lib.init.Fluids.init();
		com.mcmoddev.lib.init.ItemGroups.init();
		com.mcmoddev.lib.init.Recipes.init();

		MinecraftForge.EVENT_BUS.post(new MMDLibRegisterMaterials());
		MinecraftForge.EVENT_BUS.post(new MMDLibRegisterBlocks());
		MinecraftForge.EVENT_BUS.post(new MMDLibRegisterItems());
		MinecraftForge.EVENT_BUS.post(new MMDLibRegisterFluids());
		
		Recipes.init();
		VillagerTrades.init();

		IntegrationManager.INSTANCE.preInit(event);
		MinecraftForge.EVENT_BUS.post(new MMLibPreInitSync());
		IntegrationManager.INSTANCE.preInitPhase();
	}

	/**
	 *
	 * @param event
	 */
	public void onRemapBlock(final RegistryEvent.MissingMappings<Block> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Block> mapping : event.getAllMappings()) {
			if (mapping.key.getResourceDomain().equals(BaseMetals.MODID)
					&& (Materials.hasMaterial(MaterialNames.MERCURY))
					&& ("liquid_mercury".equals(mapping.key.getResourcePath()))) {
				mapping.remap(com.mcmoddev.lib.init.Materials
						.getMaterialByName(MaterialNames.MERCURY).getFluidBlock());
			}
		}
	}

	/**
	 *
	 * @param event
	 */
	public void onRemapItem(final RegistryEvent.MissingMappings<Item> event) {
		for (final RegistryEvent.MissingMappings.Mapping<Item> mapping : event.getAllMappings()) {
			if (mapping.key.getResourceDomain().equals(BaseMetals.MODID)
					&& (Materials.hasMaterial(MaterialNames.COAL))
					&& ("carbon_powder".equals(mapping.key.getResourcePath()))) {
				mapping.remap(com.mcmoddev.lib.init.Materials.getMaterialByName(MaterialNames.COAL)
						.getItem(Names.POWDER));
			}
		}
	}

	/**
	 *
	 * @param event
	 */
	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandler());

		// by this point all materials should have been registered both with MMDLib and Minecraft
		// move to a separate function - potentially in FallbackGeneratorData - after the test
		for (final MMDMaterial material : com.mcmoddev.lib.init.Materials.getAllMaterials()) {
			if (material.hasBlock(Names.ORE)) {
				FallbackGeneratorData.getInstance().addMaterial(material.getName(),
						Names.ORE.toString(), material.getDefaultDimension());

				if (material.hasBlock(Names.NETHERORE)) {
					FallbackGeneratorData.getInstance().addMaterial(material.getName(),
							Names.NETHERORE.toString(), -1);
				}

				if (material.hasBlock(Names.ENDORE)) {
					FallbackGeneratorData.getInstance().addMaterial(material.getName(),
							Names.ENDORE.toString(), 1);
				}
			}
		}

		ItemGroups.setupIcons(MaterialNames.STARSTEEL);
		IntegrationManager.INSTANCE.initPhase();
		// dumpMaterials()
	}

	@SuppressWarnings("unused")
	private void dumpMaterials() {
		final Gson gson = new GsonBuilder().setPrettyPrinting().create();
		final JsonObject materials = new JsonObject();

		com.mcmoddev.lib.init.Materials.getAllMaterials().forEach(material -> {
			final JsonObject currentMaterial = new JsonObject();
			final JsonArray blocks = new JsonArray();
			final JsonArray items = new JsonArray();

			material.getBlockRegistry().entrySet().stream().forEach(ent -> {
				final JsonObject entry = new JsonObject();
				entry.addProperty("name", ent.getKey());
				entry.addProperty("ref", ent.getValue().toString());
				blocks.add(entry);
			});
			material.getItemRegistry().entrySet().stream().forEach(ent -> {
				final JsonObject entry = new JsonObject();
				entry.addProperty("name", ent.getKey());
				entry.addProperty("ref", ent.getValue().toString());
				items.add(entry);
			});

			currentMaterial.add("blocks", blocks);
			currentMaterial.add("items", items);
			materials.add(material.getName(), currentMaterial);
		});
		final String out = gson.toJson(materials);
		final Path p = Paths.get("logs", "mmd_materials_dump.json");
		try (BufferedWriter bw = Files.newBufferedWriter(p, StandardCharsets.UTF_8,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING)) {
			bw.write(out);
		} catch (final IOException e) {
			final CrashReport report = CrashReport.makeCrashReport(e,
					String.format("Unable to write dump of MMDMaterial registry to %s",
							p.toFile().getAbsolutePath()));
			report.getCategory().addCrashSection("BaseMetals Version", "2.5.0-beta5");
			BaseMetals.logger.fatal(report.getCompleteReport());
		}
	}

	/**
	 *
	 * @param event
	 */
	public void postInit(final FMLPostInitializationEvent event) {
		ConfigBase.postInit();
		FallbackGeneratorData.getInstance().setup();
		IntegrationManager.INSTANCE.postInitPhase();
	}
}
