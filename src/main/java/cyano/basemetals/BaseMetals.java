package cyano.basemetals;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import cyano.basemetals.data.DataConstants;
import cyano.basemetals.events.PotionBrewEventHandler;
import cyano.basemetals.registry.CrusherRecipeRegistry;


/**
 * This is the entry point for this mod. If you are writing your own mod that 
 * uses this mod, the classes of interest to you are the init classes (classes 
 * in package cyano.basemetals.init) and the CrusherRecipeRegistry class (in 
 * package cyano.basemetals.registry). Note that you should add 
 * 'dependencies = "required-after:poweradvantage"' to your &#64;Mod annotation 
 * (e.g. <br> 
 * &#64;Mod(modid = "moremetals", name="More Metals!", version = "1.2.3", dependencies = "required-after:basemetals")
 * <br>)
 * @author DrCyano
 *
 */
@Mod(modid = BaseMetals.MODID, name=BaseMetals.NAME, version = BaseMetals.VERSION)
public class BaseMetals
{

	public static final String MODID = "basemetals";
	public static final String NAME ="Base Metals";
	public static final String VERSION = "1.1.2";
	
	
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		// load config
	//	Configuration config = new Configuration(event.getSuggestedConfigurationFile());
	//	config.load();
	//	config.save();
		
		
		Path oreSpawnFile = Paths.get(event.getSuggestedConfigurationFile().toPath().getParent().toString(),"basemetals","ore-spawn.json");
		if(Files.exists(oreSpawnFile) == false){
			try {
				Files.createDirectories(oreSpawnFile.getParent());
				Files.write(oreSpawnFile, Arrays.asList(DataConstants.defaultOreSpawnJSON.split("\n")), Charset.forName("UTF-8"));
			} catch (IOException e) {
				FMLLog.severe(MODID+": Error: Failed to write file "+oreSpawnFile);
			}
		}

		try {
			cyano.basemetals.init.WorldGen.loadConfig(oreSpawnFile);
		} catch (IOException e) {
			FMLLog.log(Level.ERROR, e,MODID+": Error parsing ore-spawn config file "+oreSpawnFile);
		}
		cyano.basemetals.init.Materials.init();
		cyano.basemetals.init.Blocks.init();
		cyano.basemetals.init.Items.init();
		

		MinecraftForge.EVENT_BUS.register(new PotionBrewEventHandler());
		
		if(event.getSide() == Side.CLIENT){
			clientPreInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverPreInit(event);
		}
	}
	
	@SideOnly(Side.CLIENT)
	private void clientPreInit(FMLPreInitializationEvent event){
		// client-only code
	}
	@SideOnly(Side.SERVER)
	private void serverPreInit(FMLPreInitializationEvent event){
		// client-only code
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		
		cyano.basemetals.init.Recipes.init();
		cyano.basemetals.init.DungeonLoot.init();
		cyano.basemetals.init.VillagerTrades.init();
		
		
		// TODO: achievements
		

		if(event.getSide() == Side.CLIENT){
			clientInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverInit(event);
		}
	}
	

	@SideOnly(Side.CLIENT)
	private void clientInit(FMLInitializationEvent event){
		// client-only code
		cyano.basemetals.init.Items.registerItemRenders(event);
		cyano.basemetals.init.Blocks.registerItemRenders(event);
	}
	@SideOnly(Side.SERVER)
	private void serverInit(FMLInitializationEvent event){
		// client-only code
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		
		cyano.basemetals.init.WorldGen.init();

		if(event.getSide() == Side.CLIENT){
			clientPostInit(event);
		}
		if(event.getSide() == Side.SERVER){
			serverPostInit(event);
		}
		
		CrusherRecipeRegistry.getInstance().clearCache();
	}
	

	@SideOnly(Side.CLIENT)
	private void clientPostInit(FMLPostInitializationEvent event){
		// client-only code
	}
	@SideOnly(Side.SERVER)
	private void serverPostInit(FMLPostInitializationEvent event){
		// client-only code
	}
}
