package com.mcmoddev.lib.integration.plugins;

//import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

//import net.minecraftforge.fml.common.Loader;

//@BaseMetalsPlugin(Thaumcraft.PLUGIN_MODID)
public class Thaumcraft implements IIntegration {

	public static final String PLUGIN_MODID = "thaumcraft";

//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_THAUMCRAFT) {
			return;
		}

		initDone = true;
	}
}
