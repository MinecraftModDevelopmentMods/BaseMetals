package com.mcmoddev.basemetals.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

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

		if (Options.isMaterialEnabled(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			tradesTable.computeIfAbsent(ARMOR_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoal.getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(WEAPON_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoal.getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(TOOL_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoal.getItem(Names.POWDER))));
		}

		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
			tradesTable.computeIfAbsent(ARMOR_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coal.getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(WEAPON_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coal.getItem(Names.POWDER))));
			tradesTable.computeIfAbsent(TOOL_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coal.getItem(Names.POWDER))));
		}

		commitTrades(tradesTable);
	}
}
