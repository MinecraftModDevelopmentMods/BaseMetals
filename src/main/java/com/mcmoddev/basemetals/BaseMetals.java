package com.mcmoddev.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.basemetals.proxy.CommonProxy;
import com.mcmoddev.basemetals.util.BMeConfig;
import com.mcmoddev.basemetals.worldgen.BaseMetalsWorldgenProvider;
import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
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

/** Forge entry point for Base Metals. */
@Mod(
		modid = BaseMetals.MODID,
		name = BaseMetals.NAME,
		version = BaseMetals.VERSION,
		dependencies = "required-after:forge@[14.23.5.2859,);required-after:mmdlib;required-after:orespawn@[4.1.0.112021,5.0.0);after:tconstruct;after:conarm;after:thaumcraft;after:ic2;before:buildingbricks",
		acceptedMinecraftVersions = "[1.12,1.12.2]",
		guiFactory = "com.mcmoddev.basemetals.client.config.BaseMetalsGuiFactory",
		certificateFingerprint = "",
		updateJSON = BaseMetals.UPDATEJSON)
public final class BaseMetals {

	@Instance
	public static BaseMetals instance;

	/** Stable mod identifier used by Forge and saved registry names. */
	public static final String MODID = "basemetals";

	/** Display name shown by Forge. */
	protected static final String NAME = "Base Metals";

	/** Release version in the shared MMD major.minor.patch.target format. */
	protected static final String VERSION = "2.6.0.112021";

	protected static final String UPDATEJSON = SharedStrings.UPDATE_JSON_URL
			+ "BaseMetals/master/update.json";

	private static final String PROXY_BASE = SharedStrings.MMD_PROXY_GROUP + MODID
			+ SharedStrings.DOT_PROXY_DOT;

	@SidedProxy(clientSide = PROXY_BASE + SharedStrings.CLIENTPROXY, serverSide = PROXY_BASE
			+ SharedStrings.SERVERPROXY)
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMetals.MODID);

	static {
		// Forge requires universal buckets to be enabled before fluid registration begins.
		FluidRegistry.enableUniversalBucket();
	}

	/** Returns the current Base Metals version. */
	public static String getVersion() {
		return VERSION;
	}

	/** Loads configuration early enough for registration decisions. */
	@EventHandler
	public static void constructing(final FMLConstructionEvent event) {
		BMeConfig.init();
	}

	/** Reports a jar whose signing certificate does not match Forge's expectation. */
	@EventHandler
	public void onFingerprintViolation(final FMLFingerprintViolationEvent event) {
		logger.warn(SharedStrings.INVALID_FINGERPRINT);
	}

	/** Starts side-specific setup and registers common event handlers. */
	@EventHandler
	public static void preInit(final FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	/** Gives OreSpawn the provider before completing normal mod initialization. */
	@EventHandler
	public static void init(final FMLInitializationEvent event) {
		if (!BaseMetalsWorldgenProvider.enqueue()) {
			throw new IllegalStateException("OreSpawn rejected the Base Metals provider");
		}

		proxy.init(event);
	}

	@EventHandler
	public static void postInit(final FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	/** Restores historical block IDs when Forge loads an older save. */
	@SubscribeEvent
	public void onRemapBlock(final RegistryEvent.MissingMappings<Block> event) {
		proxy.onRemapBlock(event);
	}

	/** Restores historical item IDs when Forge loads an older save. */
	@SubscribeEvent
	public void onRemapItem(final RegistryEvent.MissingMappings<Item> event) {
		proxy.onRemapItem(event);
	}
}
