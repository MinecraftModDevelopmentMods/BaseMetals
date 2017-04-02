package com.mcmoddev.basemetals;

import java.util.Arrays;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mcmoddev.basemetals.proxy.CommonProxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLMissingMappingsEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.FMLControlledNamespacedRegistry;
import net.minecraftforge.fml.common.registry.GameData;
import net.minecraftforge.oredict.OreDictionary;

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
		dependencies = "required-after:Forge@[12.18.3.2185,);after:taiga;before:buildingbricks",
		acceptedMinecraftVersions = "[1.10.2,)",
		updateJSON = BaseMetals.UPDATEJSON)
public class BaseMetals {

	@Instance
	public static BaseMetals instance;

	/** ID of this Mod */
	public static final String MODID = "basemetals";

	/** Display name of this Mod */
	static final String NAME = "Base Metals";

	/**
	 * Version number, in Major.Minor.Build format. The minor number is
	 * increased whenever a change is made that has the potential to break
	 * compatibility with other mods that depend on this one.
	 */
	public static final String VERSION = "2.5.0-beta1";

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
    
//	@EventHandler
//	public void serverStarted(FMLLoadCompleteEvent event) {
//		for( Map.Entry<ItemStack, ItemStack> ent : FurnaceRecipes.instance().getSmeltingList().entrySet() ) {
//			ItemStack stack = ent.getKey();
//			ItemStack outStack = ent.getValue();
//			logger.fatal("%s => %s", stack, outStack);
//			String orenames = "";
//			for( int i : OreDictionary.getOreIDs(stack))
//				orenames += " " + OreDictionary.getOreName(i);
//			logger.fatal("Ore 1: %s", orenames == "" ? "No Ores" : orenames);
//			
//			
//			orenames = "";
//			for( int i : OreDictionary.getOreIDs(new ItemStack(stack.getItem())))
//				orenames += " " + OreDictionary.getOreName(i);
//			logger.fatal("Ore 2: %s", orenames == "" ? "No Ores" : orenames);
//		}
//		
//		logger.fatal("=========== ORE DICTIONARY DUMP START ===========");
//		String[] odNames = OreDictionary.getOreNames();
//		Arrays.sort(odNames);
//		
//		for( String name : odNames ) {
//			logger.fatal("Entries for Ore Dictionary Name %s", name);
//			for( ItemStack stack : OreDictionary.getOres(name) ) {
//				logger.fatal("%s %s", getName(stack.getItem()), getMeta(stack));
//			}
//		}
//		logger.fatal("=========== ORE DICTIONARY DUMP END ===========");
//	}

}
