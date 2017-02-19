package com.mcmoddev.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.basemetals.proxy.CommonProxy;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * This is the entry point for this mod. If you are writing your own mod that
 * uses this mod, the classes of interest to you are the init classes (classes
 * in package com.mcmoddev.basemetals.init) and the CrusherRecipeRegistry class (in
 * package com.mcmoddev.basemetals.registry). Note that you should add 'dependencies =
 * "required-after:basemetals"' to your &#64;Mod annotation (e.g. <br>
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3",
 * dependencies = "required-after:basemetals") <br>
 * )
 *
 * @author Jasmine Iwanek
 *
 */
@Mod(modid = BaseMetals.MODID, name = BaseMetals.NAME, version = BaseMetals.VERSION, dependencies = "required-after:Forge@[12.18.3.2185,);after:taiga;before:buildingbricks", acceptedMinecraftVersions = "[1.10.2,)", updateJSON = BaseMetals.UPDATEJSON)
public class BaseMetals {

	@Instance
	public static BaseMetals instance;

	/** ID of this Mod */
	public static final String MODID = "basemetals";

	/** Display name of this Mod */
	public static final String NAME = "Base Metals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "2.5.0-beta1";

	public static final String UPDATEJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMetals/master/update.json";

	private static final String PROXY_BASE = "com.mcmoddev." + MODID + ".proxy.";

	@SidedProxy(clientSide = PROXY_BASE + "ClientProxy", serverSide = PROXY_BASE + "ServerProxy")
	public static CommonProxy proxy;

	public static Logger logger;

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

/*
	private BaseMetals() {
		throw new IllegalAccessError("Not a instantiable class");
	}
*/
	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
//		logger = event.getModLog();
		logger = LogManager.getFormatterLogger(BaseMetals.MODID);

		proxy.preInit(event);
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@EventHandler
	public static void onRemap(FMLMissingMappingsEvent event) {
		proxy.onRemap(event);
	}
}
