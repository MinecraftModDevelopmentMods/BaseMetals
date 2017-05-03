package com.mcmoddev.basemetals.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import net.minecraft.entity.passive.EntityVillager.ITradeList;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

	private static boolean initDone = false;

	private VillagerTrades() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Items.init();

		registerCommonTrades();
		registerModSpecificTrades();

		initDone = true;
	}

	protected static void registerModSpecificTrades() {
		final Map<Integer, List<ITradeList>> tradesTable = new HashMap<>();

		if (Options.materialEnabled(MaterialNames.CHARCOAL)) {
			tradesTable.computeIfAbsent(armorsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(weaponsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(toolsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.CHARCOAL).getItem(Names.POWDER))));
		}

		if (Options.materialEnabled(MaterialNames.COAL)) {
			tradesTable.computeIfAbsent(armorsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.COAL).getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(weaponsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.COAL).getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(toolsmith | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, Materials.getMaterialByName(MaterialNames.COAL).getItem(Names.POWDER))));
		}

		commitTrades(tradesTable);
	}
}
