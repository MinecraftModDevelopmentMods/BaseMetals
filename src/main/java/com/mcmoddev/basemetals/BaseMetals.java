package com.mcmoddev.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.basemetals.proxy.CommonProxy;
import com.mcmoddev.basemetals.util.BMeConfig;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.IntegrationManager;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.versioning.InvalidVersionSpecificationException;

/**
 * <h2><u>BaseMetals:</u></h2>
 * - Forge Mod entry point <br>
 * This entry point handles basic Forge events for the initialization phases of Base Metals;
 * the handling the remapping of items and blocks;
 * and the construction of Base Metals addons/plugins. <br>
 * <br>
 * <b>Annotation Info:</b> <br>
 * - <b>Dependencies: </b> <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; - Forge <i>[v 14.21.1.2387 or higher] (loads after)</i> <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; - Tinkers Construct <i>[any] (loads after)</i> <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; - IC2 <i>[any] (loads after)</i> <br>
 * &nbsp;&nbsp;&nbsp;&nbsp; - Building Bricks <i>[any] (loads before)</i> <br>
 * - <b>Minecraft Versions: </b>1.12 or greater <br>
 * <br>
 * <b>Project pages: </b> <br>
 * - <a href="https://github.com/MinecraftModDevelopmentMods/BaseMetals">github</a> <br>
 * - <a href="https://minecraft.curseforge.com/projects/base-metals">curse forge</a>
 * <br><br>
 *
 * <h2><u>Notes for BaseMetals addons:</u></h2>
 * - Make sure to include <strong style="background-color:#2F3136"><em>'dependencies = "required-after:basemetals"'</em></strong> to your Mod's <strong>&#64;Mod</strong> annotation. <br>
 * <strong><em>(e.g. &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3", dependencies = "required-after:basemetals")</em></strong>
 * <br><br>
 * Classes of interest:
 * @see com.mcmoddev.basemetals.init
 * @see com.mcmoddev.lib.registry.CrusherRecipeRegistry
 * @author Jasmine Iwanek
 * @author Java doc author: Vase of Petunias
 */
@Mod(
	modid = BaseMetals.MODID,
	name = BaseMetals.NAME,
	version = BaseMetals.VERSION,
	dependencies = "required-after:forge@[14.21.1.2387,);after:tconstruct;after:conarm;after:thaumcraft;after:ic2;before:buildingbricks",
	acceptedMinecraftVersions = "[1.12,)",
	certificateFingerprint = "@FINGERPRINT@",
	updateJSON = BaseMetals.UPDATEJSON)
public final class BaseMetals {
	
	@Instance
	public static BaseMetals instance;

	/**
	 * Mod ID used by forge in the <strong>&#64;Mod</strong> annotation.
	 */
	public static final String MODID = "basemetals";

	/**
	 * Used in the <strong>&#64;Mod</strong> annotation. <br>
	 * This is the vanity display name that will appear to users of the mod.
	 * <strong><em>(e.g. The mods menu in game) </em></strong>
	 */
	protected static final String NAME = "Base Metals";

	/**
	 * <b>Version format:</b> <em>Major.Minor.Patch</em> <br>
	 * The Minor version number will be increased when ever there is potential for compatibility issues
	 * with dependant mods.
	 */
	protected static final String VERSION = "2.5.0-beta6";

	protected static final String UPDATEJSON = SharedStrings.UPDATE_JSON_URL
			+ "BaseMetals/master/update.json";

	private static final String PROXY_BASE = SharedStrings.MMD_PROXY_GROUP + MODID
			+ SharedStrings.DOT_PROXY_DOT;

	@SidedProxy(
				clientSide = PROXY_BASE + SharedStrings.CLIENTPROXY,
				serverSide = PROXY_BASE + SharedStrings.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMetals.MODID);

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

	/**
	 * @return Returns the current version of the mod.
	 */
	public static String getVersion() {
		return VERSION;
	}

	/**
	 * <b>Called when Forge sets up Base Metals:</b> <br>
	 * - This method will set up all addons for Base Metals. <br>
	 * Refer to {@link com.mcmoddev.lib.integration.IntegrationManager#doSetupTasks(FMLConstructionEvent)} documentation.
	 * <br>
	 * - Initializes Base Metals config {@link Config#init()}.
	 * <br><br>
	 * <b>Errors:</b> <br>
	 * Errors occur when ever there was an issue loading a Base Metals plugin.
	 * @param event The event representing the construction of Base Metals.
	 */
	@EventHandler
	public static void constructing(final FMLConstructionEvent event) {
		try {
			IntegrationManager.INSTANCE.doSetupTasks(event);
		} catch (InvalidVersionSpecificationException e) {
			logger.error("Error loading version information for plugins: %s", e);
		}

		BMeConfig.init();
	}

	/**
	 * Logs a warning when ever the mod finger print does not match the certificate loaded from the mod jar.
	 * @param event The event that represents the finger print violation.
	 */
	@EventHandler
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
		logger.warn(SharedStrings.INVALID_FINGERPRINT);
	}

	/**
	 * <b>Pre-initialization of Base Metals:</b> <br>
	 * Kicks off the initialization process of Base Metals. <br>
	 * Refer to {@link CommonProxy#preInit(FMLPreInitializationEvent)} documentation for what goes on
	 * during the pre-initialization procedure.
	 *
	 * @param event The event representing the pre-initialization phase of Forge.
	 */
	@EventHandler
	public static void preInit(final FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	/**
	 * <b>Initialization of Base Metals:</b> <br>
	 * Refer to {@link CommonProxy#init(FMLInitializationEvent)} documentation for what goes on
	 * during the initialization procedure.
	 *
	 * @param event The event representing the initialization phase of Forge.
	 */
	@EventHandler
	public static void init(final FMLInitializationEvent event) {
		if(Loader.isModLoaded("crafttweaker")) {
			com.mcmoddev.lib.crafttweaker.CrusherRecipes.loadComplete();
		}
		proxy.init(event);
	}

	/**
	 * <b>Post-initialization of Base Metals:</b> <br>
	 * Refer to {@link CommonProxy#postInit(FMLPostInitializationEvent)} documentation for what goes on
	 * during the post-initialization procedure.
	 *
	 * @param event The event representing the post-initialization phase of Forge.
	 */
	@EventHandler
	public static void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);
		//com.mcmoddev.lib.init.Materials.dumpRegistry();
		//com.mcmoddev.lib.init.Recipes.dumpFurnaceRecipes();
		//com.mcmoddev.lib.init.ItemGroups.dumpTabs();
	}
	
	/**
	 * Fired when ever a block in Forged has been remapped. <br>
	 * Refer to {@link CommonProxy#onRemapBlock(RegistryEvent.MissingMappings)} documentation as to how remapped blocks are handled.
	 *
	 * @param event The event representing a list of missing mapped blocks that need to be remapped.
	 */
	@SubscribeEvent
	public void onRemapBlock(final RegistryEvent.MissingMappings<Block> event) {
		proxy.onRemapBlock(event);
	}

	/**
	 * Fired when ever an item in Forged has been remapped. <br>
	 * Refer to {@link CommonProxy#onRemapItem(RegistryEvent.MissingMappings)} documentation as to how remapped items are handled.
	 *
	 * @param event The event representing a list of missing mapped items that need to be remapped.
	 */
	@SubscribeEvent
	public void onRemapItem(final RegistryEvent.MissingMappings<Item> event) {
		proxy.onRemapItem(event);
	}
}
