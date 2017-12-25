package com.mcmoddev.basemetals.util;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
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
	public static void insertTrades(int professionID, int careerID, int tradeLevel, EntityVillager.ITradeList... trades) {
		final ResourceLocation profession = professionList[professionID];
		insertTrades(profession, careerID, tradeLevel, trades);
		/*
		VillagerRegistry.VillagerProfession _profession = ForgeRegistries.VILLAGER_PROFESSIONS.getKey(professionID);
		VillagerRegistry.VillagerCareer career = _profession.getCareer(careerID);
		career.addTrade(tradeLevel, trade);
		*/
	}

	/**
	 * Inserts one or more trades to the default villager trade table
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
	public static void insertTrades(String professionName, int careerID, int tradeLevel, EntityVillager.ITradeList... trades) {
		insertTrades(new ResourceLocation(professionName), careerID, tradeLevel, trades);
	}

	/**
	 * Inserts one or more trades to the default villager trade table
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
	public static void insertTrades(ResourceLocation professionRL, int careerID, int tradeLevel, EntityVillager.ITradeList... trades) {
		VillagerRegistry.VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(professionRL);
		VillagerRegistry.VillagerCareer career = profession.getCareer(careerID);
		career.addTrade(tradeLevel, trades);
	}
}
