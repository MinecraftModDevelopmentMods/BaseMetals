package cyano.basemetals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import cyano.basemetals.util.Config.Options;
import net.minecraftforge.fml.common.Loader;

@BaseMetalsPlugin(Thaumcraft.PLUGIN_MODID)
public class Thaumcraft implements IIntegration {

	protected static final String PLUGIN_MODID = "thaumcraft";

	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.ENABLE_THAUMCRAFT) {
			return;
		}

		initDone = true;
	}
}
