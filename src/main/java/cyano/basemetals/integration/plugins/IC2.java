package cyano.basemetals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import cyano.basemetals.util.Config.Options;
import net.minecraftforge.fml.common.Loader;

@BaseMetalsPlugin(IC2.PLUGIN_MODID)
public class IC2 implements IIntegration {

	public static final String PLUGIN_MODID = "IC2";

	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_IC2) {
			return;
		}

		initDone = true;
	}
}
