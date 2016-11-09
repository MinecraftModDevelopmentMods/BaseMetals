package cyano.basemetals;

import java.io.IOException;
import java.nio.file.*;
import java.util.Collections;

import org.apache.logging.log4j.Level;

import cyano.basemetals.data.AdditionalLootTables;
import cyano.basemetals.proxy.CommonProxy;
import cyano.basemetals.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.Mod.*;
import net.minecraftforge.fml.common.event.*;

/**
 * This is the entry point for this mod. If you are writing your own mod that
 * uses this mod, the classes of interest to you are the init classes (classes
 * in package cyano.basemetals.init) and the CrusherRecipeRegistry class (in
 * package cyano.basemetals.registry). Note that you should add 'dependencies =
 * "required-after:basemetals"' to your &#64;Mod annotation (e.g. <br>
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3",
 * dependencies = "required-after:basemetals") <br>
 * )
 *
 * @author DrCyano
 *
 */
@Mod(modid = BaseMetals.MODID, name = BaseMetals.NAME, version = BaseMetals.VERSION, dependencies = "required-after:Forge", acceptedMinecraftVersions = "[1.10.2,)", updateJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMetals/master/update.json")
public class BaseMetals {
	// TODO: use metal plates to modify or repair shields

	@Instance
	public static BaseMetals INSTANCE = null;

	/** ID of this mod */
	public static final String MODID = "basemetals";

	/** display name of this mod */
	public static final String NAME = "Base Metals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "2.5.0";

	/** Configuration file location */
	public static final String CONFIG_FILE = "config/BaseMetals.cfg";

	@SidedProxy(clientSide = "cyano.basemetals.proxy.ClientProxy", serverSide = "cyano.basemetals.proxy.ServerProxy")
	public static CommonProxy PROXY = null;

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		INSTANCE = this;

		// load config
		Config.init();

		cyano.basemetals.init.Fluids.init();
		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.ItemGroups.init();
		cyano.basemetals.init.Blocks.init();
		cyano.basemetals.init.Items.init();
		cyano.basemetals.init.VillagerTrades.init();
		cyano.basemetals.init.EnderIOPlugin.init();
		if (Loader.isModLoaded("tconstruct")) {
			cyano.basemetals.init.TinkersConstructPlugin.init();
		}
		if (Loader.isModLoaded("Mekanism")) {
			cyano.basemetals.init.MekanismPlugin.init();
		}
		cyano.basemetals.init.VeinMinerPlugin.init();

		final Path ALTPath = Paths.get(event.getSuggestedConfigurationFile().getParent(), "additional-loot-tables");
		final Path myLootFolder = ALTPath.resolve(MODID);
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
			}
			catch (final IOException ex) {
				FMLLog.log(Level.ERROR, ex, "%s: Failed to extract additional loot tables", MODID);
			}
		}

		PROXY.preInit();
	}

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {

		cyano.basemetals.init.Recipes.init();
		cyano.basemetals.init.DungeonLoot.init();
		cyano.basemetals.init.Entities.init();

		cyano.basemetals.init.Achievements.init();

		PROXY.init();

		MinecraftForge.EVENT_BUS.register(new cyano.basemetals.util.EventHandler());
	}

	/**
	 *
	 * @param event
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		cyano.basemetals.init.WorldGen.init();
		Config.postInit();
		PROXY.postInit();
	}

}
