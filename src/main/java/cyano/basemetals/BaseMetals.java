package cyano.basemetals;

import cyano.basemetals.proxy.CommonProxy;
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
@Mod(modid = BaseMetals.MODID, name = BaseMetals.NAME, version = BaseMetals.VERSION, dependencies = "required-after:Forge;before:buildingbricks", acceptedMinecraftVersions = "[1.10.2,)", updateJSON = "https://raw.githubusercontent.com/MinecraftModDevelopment/BaseMetals/master/update.json")
public class BaseMetals {

	// TODO: use metal plates to modify or repair shields

	@Instance
	public static BaseMetals INSTANCE = new BaseMetals();

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

	@SidedProxy(clientSide = "cyano.basemetals.proxy.ClientProxy", serverSide = "cyano.basemetals.proxy.ServerProxy")
	public static CommonProxy PROXY = null;

	static {
		// Forge says this needs to be statically initialized here.
		FluidRegistry.enableUniversalBucket();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
//		INSTANCE = this;
		PROXY.preInit(event);
	}

	@EventHandler
	public void onRemap(FMLMissingMappingsEvent event) {
		PROXY.onRemap(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		PROXY.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		PROXY.postInit(event);
	}
}
