package com.mcmoddev.basemetals.util;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

/** Adds trades to Forge's registered vanilla villager careers. */
public final class VillagerTradeHelper {

	private static final ResourceLocation[] professionList = {
			new ResourceLocation("minecraft:farmer"), new ResourceLocation("minecraft:librarian"),
			new ResourceLocation("minecraft:priest"), new ResourceLocation("minecraft:smith"),
			new ResourceLocation("minecraft:butcher")};

	protected VillagerTradeHelper() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 * Adds trades to a vanilla profession by its numeric ID.
	 *
	 * @param professionID vanilla profession ID (0-4)
	 * @param careerID career ID within that profession (1-3)
	 * @param tradeLevel trade level, starting at 1
	 * @param trades offers to add at that level
	 */
	public static void insertTrades(final int professionID, final int careerID,
			final int tradeLevel, final EntityVillager.ITradeList... trades) {
		final ResourceLocation profession = professionList[professionID];

		insertTrades(profession, careerID, tradeLevel, trades);
	}

	/**
	 * Adds trades to a vanilla profession by name.
	 *
	 * @param professionName vanilla profession name
	 * @param careerID career ID within that profession (1-3)
	 * @param tradeLevel trade level, starting at 1
	 * @param trades offers to add at that level
	 */
	public static void insertTrades(final String professionName, final int careerID,
			final int tradeLevel, final EntityVillager.ITradeList... trades) {
		insertTrades(new ResourceLocation(professionName), careerID, tradeLevel, trades);
	}

	/**
	 * Adds trades to a registered profession.
	 *
	 * @param professionRL profession registry name
	 * @param careerID career ID within that profession (1-3)
	 * @param tradeLevel trade level, starting at 1
	 * @param trades offers to add at that level
	 */
	public static void insertTrades(final ResourceLocation professionRL, final int careerID,
			final int tradeLevel, final EntityVillager.ITradeList... trades) {
		final VillagerProfession profession = ForgeRegistries.VILLAGER_PROFESSIONS
				.getValue(professionRL);
		final VillagerCareer career = profession.getCareer(careerID);

		career.addTrade(tradeLevel, trades);
	}
}
