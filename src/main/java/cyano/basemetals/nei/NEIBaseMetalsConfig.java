package cyano.basemetals.nei;

import codechicken.nei.api.IConfigureNEI;
import cyano.basemetals.BaseMetals;

public class NEIBaseMetalsConfig implements IConfigureNEI{

	// see https://www.youtube.com/watch?v=8CtcExhsplg for a tutorial
	
	// NEI is supposed to automatically detect classes with the name NEI*Config 
	@Override
	public String getName() {
		return "PowerAdvantage NEI Plugin";
	}

	@Override
	public String getVersion() {
		return BaseMetals.VERSION;
	}

	@Override
	public void loadConfig() {
		CrusherRecipeHandler crusherHandler = new CrusherRecipeHandler();
		codechicken.nei.api.API.registerRecipeHandler(crusherHandler);
		codechicken.nei.api.API.registerUsageHandler(crusherHandler);
	}
}
