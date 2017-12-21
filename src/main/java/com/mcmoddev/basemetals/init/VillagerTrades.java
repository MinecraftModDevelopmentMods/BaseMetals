package com.mcmoddev.basemetals.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;
import net.minecraft.entity.passive.EntityVillager.ITradeList;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

	private static boolean initDone = false;

	private VillagerTrades() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
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

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			if (charcoal.hasItem(Names.POWDER)) {
				final Item charcoalPowder = charcoal.getItem(Names.POWDER);

				tradesTable.computeIfAbsent(ARMOR_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoalPowder)));
				tradesTable.computeIfAbsent(WEAPON_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoalPowder)));
				tradesTable.computeIfAbsent(TOOL_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, charcoalPowder)));
			}
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
			if (coal.hasItem(Names.POWDER)) {
				final Item coalPowder = coal.getItem(Names.POWDER);

				tradesTable.computeIfAbsent(ARMOR_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coalPowder)));
				tradesTable.computeIfAbsent(WEAPON_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coalPowder)));
				tradesTable.computeIfAbsent(TOOL_SMITH | (1), (Integer key) -> new ArrayList<>()).addAll(Arrays.asList(makePurchasePalette(1, 10, coalPowder)));
			}
		}

		commitTrades(tradesTable);
	}
}
