package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.VillagerTradeHelper;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.passive.EntityVillager.ITradeList;
import net.minecraft.item.Item;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VillagerTrades extends com.mcmoddev.lib.init.VillagerTrades {

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

		if (Materials.hasMaterial(MaterialNames.CHARCOAL)) {
			final MMDMaterial charcoal = Materials.getMaterialByName(MaterialNames.CHARCOAL);
			if (charcoal.hasItem(Names.POWDER)) {
				final Item charcoalPowder = charcoal.getItem(Names.POWDER);
				final ITradeList[] charcoalTrades = makePurchasePalette(1, 10, charcoalPowder);

				VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, 1, charcoalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, 1, charcoalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, 1, charcoalTrades);
			}
		}

		if (Materials.hasMaterial(MaterialNames.COAL)) {
			final MMDMaterial coal = Materials.getMaterialByName(MaterialNames.COAL);
			if (coal.hasItem(Names.POWDER)) {
				final Item coalPowder = coal.getItem(Names.POWDER);
				final ITradeList[] coalTrades = makePurchasePalette(1, 10, coalPowder);

				VillagerTradeHelper.insertTrades(SMITH_RL, ARMOR_SMITH_ID, 1, coalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, WEAPON_SMITH_ID, 1, coalTrades);
				VillagerTradeHelper.insertTrades(SMITH_RL, TOOL_SMITH_ID, 1, coalTrades);
			}
		}
	}
}
