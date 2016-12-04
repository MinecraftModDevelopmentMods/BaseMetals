package cyano.basemetals.proxy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

import org.apache.logging.log4j.Level;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.data.AdditionalLootTables;
import cyano.basemetals.init.Blocks;
import cyano.basemetals.init.Fluids;
import cyano.basemetals.init.ItemGroups;
import cyano.basemetals.init.Items;
import cyano.basemetals.init.Materials;
import cyano.basemetals.init.VillagerTrades;
import cyano.basemetals.init.plugins.EnderIOPlugin;
import cyano.basemetals.init.plugins.MekanismPlugin;
import cyano.basemetals.init.plugins.TinkersConstructPlugin;
import cyano.basemetals.init.plugins.VeinMinerPlugin;
import cyano.basemetals.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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
		// load config
		Config.init();

		Fluids.init();
		Materials.init();
		ItemGroups.init();
		Blocks.init();
		Items.init();
		VillagerTrades.init();

		if (Loader.isModLoaded("EnderIO")) {
			EnderIOPlugin.init();
		}
		if (Loader.isModLoaded("IC2")) {
			//IC2Plugin.init();
		}
		if (Loader.isModLoaded("tconstruct")) {
			TinkersConstructPlugin.init();
		}
		if (Loader.isModLoaded("Mekanism")) {
			MekanismPlugin.init();
		}
		if (Loader.isModLoaded("thaumcraft")) {
			//ThaumcraftPlugin.init();
		}
		if (Loader.isModLoaded("veinminer")) {
			VeinMinerPlugin.init();
		}

		final Path ALTPATH = Paths.get(event.getSuggestedConfigurationFile().getParent(), "additional-loot-tables");
		final Path myLootFolder = ALTPATH.resolve(BaseMetals.MODID);
		if (Files.notExists(myLootFolder)) {
			try {
				Files.createDirectories(myLootFolder.resolve("chests"));
				Files.write(myLootFolder.resolve("chests").resolve("abandoned_mineshaft.json"), Collections.singletonList(AdditionalLootTables.abandoned_mineshaft));
				Files.write(myLootFolder.resolve("chests").resolve("desert_pyramid.json"), Collections.singletonList(AdditionalLootTables.desert_pyramid));
				Files.write(myLootFolder.resolve("chests").resolve("end_city_treasure.json"), Collections.singletonList(AdditionalLootTables.end_city_treasure));
				Files.write(myLootFolder.resolve("chests").resolve("jungle_temple.json"), Collections.singletonList(AdditionalLootTables.jungle_temple));
				Files.write(myLootFolder.resolve("chests").resolve("nether_bridge.json"), Collections.singletonList(AdditionalLootTables.nether_bridge));
				Files.write(myLootFolder.resolve("chests").resolve("simple_dungeon.json"), Collections.singletonList(AdditionalLootTables.simple_dungeon));
				Files.write(myLootFolder.resolve("chests").resolve("spawn_bonus_chest.json"), Collections.singletonList(AdditionalLootTables.spawn_bonus_chest));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_corridor.json"), Collections.singletonList(AdditionalLootTables.stronghold_corridor));
				Files.write(myLootFolder.resolve("chests").resolve("stronghold_crossing.json"), Collections.singletonList(AdditionalLootTables.stronghold_crossing));
				Files.write(myLootFolder.resolve("chests").resolve("village_blacksmith.json"), Collections.singletonList(AdditionalLootTables.village_blacksmith));
			} catch (final IOException ex) {
				FMLLog.log(Level.ERROR, ex, "%s: Failed to extract additional loot tables", BaseMetals.MODID);
			}
		}
	}

	/**
	 *
	 */
	public void init(FMLInitializationEvent event) {
		cyano.basemetals.init.Recipes.init();
		cyano.basemetals.init.DungeonLoot.init();
		cyano.basemetals.init.Entities.init();

		cyano.basemetals.init.Achievements.init();

		MinecraftForge.EVENT_BUS.register(new cyano.basemetals.util.EventHandler());
	}

	/**
	 *
	 */
	public void postInit(FMLPostInitializationEvent event) {
		cyano.basemetals.init.WorldGen.init();
		Config.postInit();
	}
}
