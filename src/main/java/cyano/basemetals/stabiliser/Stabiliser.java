package cyano.basemetals.stabiliser;

import java.util.ArrayList;

/**
 * Used for compat with stabiliser
 *
 * @author TheCodedOne
 */
public class Stabiliser {

	private Stabiliser() {}

	public static Stabiliser INSTANCE = new Stabiliser();

	private ArrayList<IStableInfo> LOADED_ADDONS = new ArrayList<>();

	public void register(IStableInfo info) {
		if(!LOADED_ADDONS.contains(info))
			LOADED_ADDONS.add(info);
	}

	public boolean isAddonLoaded(String modid) {
		for (IStableInfo info : LOADED_ADDONS) {
			if(info.getModID().equalsIgnoreCase(modid))
				return true;
		}
		return false;
	}

	public IStableInfo[] getLoadedAddons() {
		return LOADED_ADDONS.toArray(new IStableInfo[LOADED_ADDONS.toArray().length]).clone();
	}
}
