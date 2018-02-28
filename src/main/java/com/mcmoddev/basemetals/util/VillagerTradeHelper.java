package com.mcmoddev.basemetals.util;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/**
 * Helper for Villager Trades.
 *
 * @author Chris on 3/30/2016.
 */
public class VillagerTradeHelper {

	private static final ResourceLocation[] professionList = { new ResourceLocation("minecraft:farmer"),
			new ResourceLocation("minecraft:librarian"), new ResourceLocation("minecraft:priest"),
			new ResourceLocation("minecraft:smith"), new ResourceLocation("minecraft:butcher") };

	protected VillagerTradeHelper() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 * Inserts one or more trades to the default villager trade table using dark
	 * magic (aka java reflection).
	 *
	 * @param professionID
	 *            Villager profession ID (0-4)
	 * @param careerID
	 *            Villager career ID (1-3)
	 * @param tradeLevel
	 *            Level of trade (1+)
	 * @param trades
	 *            Trades to add to the given level
	 */
	public static void insertTrades(final int professionID, final int careerID, final int tradeLevel,
			final EntityVillager.ITradeList... trades) {
		final ResourceLocation profession = professionList[professionID];
		insertTrades(profession, careerID, tradeLevel, trades);
	}

	/**
	 * Inserts one or more trades to the default villager trade table.
	 *
	 * @param professionName
	 *            Villager profession
	 * @param careerID
	 *            Villager career ID (1-3)
	 * @param tradeLevel
	 *            Level of trade (1+)
	 * @param trades
	 *            Trades to add to the given level
	 */
	public static void insertTrades(final String professionName, final int careerID, final int tradeLevel,
			final EntityVillager.ITradeList... trades) {
		insertTrades(new ResourceLocation(professionName), careerID, tradeLevel, trades);
	}

	/**
	 * Inserts one or more trades to the default villager trade table.
	 *
	 * @param professionRL
	 *            Villager profession
	 * @param careerID
	 *            Villager career ID (1-3)
	 * @param tradeLevel
	 *            Level of trade (1+)
	 * @param trades
	 *            Trades to add to the given level
	 */
	public static void insertTrades(final ResourceLocation professionRL, final int careerID, final int tradeLevel,
			final EntityVillager.ITradeList... trades) {
		final VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(professionRL);
		final VillagerCareer career = profession.getCareer(careerID);
		career.addTrade(tradeLevel, trades);
	}
}
