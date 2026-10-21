package zone.moddev.mc.basemetals.proxy;

import java.util.HashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.data.MaterialNames;
import zone.moddev.mc.basemetals.init.Achievements;
import zone.moddev.mc.basemetals.init.Blocks;
import zone.moddev.mc.basemetals.init.Fluids;
import zone.moddev.mc.basemetals.init.ItemGroups;
import zone.moddev.mc.basemetals.init.Items;
import zone.moddev.mc.basemetals.init.Materials;
import zone.moddev.mc.basemetals.init.Recipes;
import zone.moddev.mc.basemetals.init.VillagerTrades;
import zone.moddev.mc.basemetals.util.Config;
import zone.moddev.mc.basemetals.util.EventHandler;
import zone.moddev.mc.basemetals.util.LegacyMappings;
import zone.moddev.mc.basemetals.worldgen.BaseMetalsWorldgenProvider;
import com.mcmoddev.lib.fuels.FuelRegistry;
import com.mcmoddev.lib.integration.IntegrationManager;
import com.mcmoddev.lib.util.ConfigBase;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Base Metals Common Proxy.
 *
 * @author Jasmine Iwanek
 *
 */
public class CommonProxy {

	private boolean allsGood = false;

	public boolean allsGood() {
		return this.allsGood;
	}

	public void preInit(final FMLPreInitializationEvent event) {

		Config.init();

		com.mcmoddev.lib.init.Materials.init();
		Materials.init();
		FuelRegistry.init();
		Fluids.init();
		ItemGroups.init();
		com.mcmoddev.lib.init.Blocks.init();
		Blocks.init();
		com.mcmoddev.lib.init.Items.init();
		Items.init();

		cyano.basemetals.init.Blocks.bind();
		cyano.basemetals.init.Items.bind();

		VillagerTrades.init();

		IntegrationManager.INSTANCE.preInit(event);
		IntegrationManager.INSTANCE.runCallbacks("preInit");
		this.allsGood = true;
	}

	public void onRemap(final FMLMissingMappingsEvent event) {
		for (final MissingMapping mapping : event.get()) {
			if (mapping.resourceLocation.getResourceDomain().equals(BaseMetals.MODID)) {
				if (mapping.type.equals(GameRegistry.Type.BLOCK)) {
					final ResourceLocation mercury = new ResourceLocation(BaseMetals.MODID, "mercury");

					if ("liquid_mercury".equals(mapping.resourceLocation.getResourcePath())
							&& ForgeRegistries.BLOCKS.containsKey(mercury)) {
						mapping.remap(ForgeRegistries.BLOCKS.getValue(mercury));
					}
				} else if (mapping.type.equals(GameRegistry.Type.ITEM)) {
					final String replacement = LegacyMappings.itemReplacement(
							mapping.resourceLocation.getResourcePath());

					if (replacement != null) {
						final ResourceLocation target = new ResourceLocation(BaseMetals.MODID, replacement);

						// Disabled materials must not be remapped to the registry's default item.
						if (ForgeRegistries.ITEMS.containsKey(target)) {
							// Cyano saved two item IDs for each door.
							// Add the alias after Forge loads the world's registry.
							mapping.ignore();
						}
					}
				}
			}
		}
	}

	public void onIdMapping() {
		final Map<ResourceLocation, ResourceLocation> aliases = new HashMap<>();

		for (final Map.Entry<String, String> replacement : LegacyMappings.itemReplacements().entrySet()) {
			final ResourceLocation target = new ResourceLocation(BaseMetals.MODID, replacement.getValue());

			if (ForgeRegistries.ITEMS.containsKey(target)) {
				aliases.put(new ResourceLocation(BaseMetals.MODID, replacement.getKey()), target);
			}
		}

		// Forge has now installed the world's registry.
		// Save the aliases for inventories in unopened chunks too.
		GameData.getItemRegistry().loadAliases(aliases);
	}

	public void init(final FMLInitializationEvent event) {
		this.allsGood = false;
		BaseMetalsWorldgenProvider.enqueue();
		com.mcmoddev.lib.init.Recipes.init();
		Recipes.init();

		Achievements.init();
		FuelRegistry.register();
		IntegrationManager.INSTANCE.runCallbacks("init");
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		this.allsGood = true;

		ItemGroups.setupIcons(MaterialNames.STARSTEEL);
	}

	public void postInit(final FMLPostInitializationEvent event) {
		this.allsGood = false;
		IntegrationManager.INSTANCE.runCallbacks("postInit");
		ConfigBase.postInit();
		this.allsGood = true;
	}
}
