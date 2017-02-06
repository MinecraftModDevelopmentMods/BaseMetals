package com.mcmoddev.lib.integration.plugins;

//import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

//import net.minecraftforge.fml.common.Loader;

//@BaseMetalsPlugin(IC2.PLUGIN_MODID)
public class IC2 implements IIntegration {

	public static final String PLUGIN_MODID = "IC2";

//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableIC2) {
			return;
		}

		initDone = true;
	}
}
