package cyano.basemetals.init.plugins;

import net.minecraftforge.fml.common.Loader;

public class IC2 {

	protected static final String PLUGIN_MODID = "IC2";

	// protected static final String MODID = BaseMetals.MODID;
	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		initDone = true;
	}
}
