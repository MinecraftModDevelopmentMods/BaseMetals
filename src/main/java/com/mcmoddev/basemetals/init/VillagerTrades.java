package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.SharedStrings;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public final class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

	private VillagerTrades() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		registerCommonTrades();
		registerModSpecificTrades();
	}

	protected static void registerModSpecificTrades() {
		registerBurnableTrades(MaterialNames.CHARCOAL);
		registerBurnableTrades(MaterialNames.COAL);
	}
}
