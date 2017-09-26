package com.mcmoddev.basemetals;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.basemetals.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * This is the entry point for this Mod. If you are writing your own Mod that
 * uses this Mod, the classes of interest to you are the init classes (classes
 * in package com.mcmoddev.basemetals.init) and the CrusherRecipeRegistry class
 * (in package com.mcmoddev.basemetals.registry). Note that you should add
 * 'dependencies = "required-after:basemetals"' to your &#64;Mod annotation
 * (e.g. <br>
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3",
 * dependencies = "required-after:basemetals") <br>
 * )
 *
 * @author Jasmine Iwanek
 *
 */
@Mod(
		modid = BaseMetals.MODID,
		name = BaseMetals.NAME,
		version = BaseMetals.VERSION,
		dependencies = "required-after:forge@[14.21.0.2327,);after:taiga;after:ic2;after:tconstruct;before:buildingbricks",
		acceptedMinecraftVersions = "[1.12,)",
		updateJSON = BaseMetals.UPDATEJSON)
public class BaseMetals {

	@Instance
	public static BaseMetals instance;

	/** ID of this Mod */
	public static final String MODID = "basemetals";

	/** Display name of this Mod */
	static final String NAME = "Base Metals";

	/**
	 * Version number, in Major.Minor.Patch format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "2.5.0-beta2";

	static final String UPDATEJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMetals/master/update.json";

	private static final String PROXY_BASE = "com.mcmoddev." + MODID + ".proxy.";

	@SidedProxy(clientSide = PROXY_BASE + "ClientProxy", serverSide = PROXY_BASE + "ServerProxy")
	public static CommonProxy proxy;

	public static final Logger logger = LogManager.getFormatterLogger(BaseMetals.MODID);

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public static void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		
		if( proxy.allsGood ) {
			MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.init.Items.class);
			MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.init.Blocks.class);
			MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.BaseMetals.class);
			MinecraftForge.EVENT_BUS.register(com.mcmoddev.basemetals.init.Recipes.class);
		} else {
			logger.fatal("Something went wrong during the proxt preInit() call!");
		}
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		proxy.init(event);
		// if we have anything else to do here, check 'proxy.allsGood' first
	}

	@EventHandler
	public static void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		// if we have anything else to do here, check 'proxy.allsGood' first
	}

	@SubscribeEvent
	public void onRemapBlock(RegistryEvent.MissingMappings<Block> event) {
		proxy.onRemapBlock(event);
	}
	
	@SubscribeEvent
	public void onRemapItem(RegistryEvent.MissingMappings<Item> event) {
		proxy.onRemapItem(event);
	}
	
}
