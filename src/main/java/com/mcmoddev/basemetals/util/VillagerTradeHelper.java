package com.mcmoddev.basemetals.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

/**
 * @author Chris on 3/30/2016.
 */
public class VillagerTradeHelper {

	private static final ResourceLocation[] professionList = {
			new ResourceLocation("minecraft:farmer"),
			new ResourceLocation("minecraft:librarian"),
			new ResourceLocation("minecraft:priest"),
			new ResourceLocation("minecraft:smith"),
			new ResourceLocation("minecraft:butcher")
	};

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
	 * @throws NoSuchFieldException
	 *             Thrown if java reflection has been disabled for security reasons
	 * @throws IllegalAccessException
	 *             Thrown if java reflection has been disabled for security reasons
	 */
	public static void insertTrades(int professionID, int careerID, int tradeLevel, EntityVillager.ITradeList... trades)
			throws NoSuchFieldException, IllegalAccessException {
		final ResourceLocation profession = professionList[professionID];
		insertTrades(profession, careerID, tradeLevel, trades);
		/*
		BaseMetals.logger.info("%s: injecting villager trades %s into default trade array table at position [%s][%s][%s][*]", BaseMetals.MODID, Arrays.toString(trades), professionID, careerID - 1, tradeLevel - 1);
		Field vanillaTradeField = getTradeArrayFromClass(EntityVillager.class);
		unlockPrivateFinalField(vanillaTradeField);
		Object tradeTable = vanillaTradeField.get(null); // is static
		appendToMultidimensionalArray(trades, tradeTable, professionID, Math.max (0, careerID - 1), Math.max(0, tradeLevel - 1));
		*/
	}

	/**
	 * Inserts one or more trades to the default villager trade table using dark
	 * magic (aka java reflection).
	 * 
	 * @param profession
	 *            Villager profession
	 * @param careerID
	 *            Villager career ID (1-3)
	 * @param tradeLevel
	 *            Level of trade (1+)
	 * @param trades
	 *            Trades to add to the given level
	 */
	public static void insertTrades(ResourceLocation profession, int careerID, int tradeLevel, EntityVillager.ITradeList... trades) {
		for (final EntityVillager.ITradeList trade : trades) {
			VillagerRegistry.VillagerProfession _profession = ForgeRegistries.VILLAGER_PROFESSIONS.getValue(profession);
			((List<VillagerRegistry.VillagerCareer>)ObfuscationReflectionHelper.getPrivateValue(VillagerRegistry.VillagerProfession.class, _profession, "careers")).get(careerID - 1).addTrade(tradeLevel, trade);
		}
	}

	/**
	 *
	 * @param append
	 *            What to append to
	 * @param array
	 *            What
	 * @param indices
	 *            The indices
	 */
	public static void appendToMultidimensionalArray(Object append, Object array, int... indices) {
		appendToMultidimensionalArray(Collections.singletonList(append).toArray(), array, indices);
	}

	/**
	 *
	 * @param append
	 *            What to append to
	 * @param array
	 *            What
	 * @param indices
	 *            The indices
	 */
	public static void appendToMultidimensionalArray(Object[] append, Object array, int... indices) {
		// get the lowest level array
		Object prevArray = null;
		int i = 0;
		while (i < indices.length) {
			final int index = indices[i];
			prevArray = array;
			array = Array.get(array, index);
			i++;
			// make sure the next array is long enough
			if (i < indices.length) {
				final int nextIndex = indices[i];
				if (Array.getLength(array) <= nextIndex) {
					Object pad;
					if (array.getClass().getComponentType().isArray())
						pad = Array.newInstance(array.getClass().getComponentType().getComponentType(), 0);
					else
						// this shouldn't happen
						pad = null;
					final Object newArray = expandArray(array, nextIndex + 1, pad);
					Array.set(prevArray, index, newArray);
					array = newArray;
				}
			}
		}

		// expand lowest level array to new size
		final Class<?> aType = array.getClass().getComponentType();
		if (!aType.isAssignableFrom(append.getClass().getComponentType()))
			throw new IllegalArgumentException("Class type " + append.getClass().getComponentType().getCanonicalName() + " cannot be appended to " + aType.getCanonicalName() + " array");
		final Object newArray = expandArray(array, Array.getLength(array) + append.length, null);
		System.arraycopy(append, 0, newArray, Array.getLength(array), append.length);
		Array.set(prevArray, indices[indices.length - 1], newArray);
	}

	/**
	 *
	 * @param array
	 *            The array
	 * @param newSize
	 *            The new size for the array
	 * @param fill
	 *            The Object
	 * @return An Object
	 */
	public static Object expandArray(Object array, int newSize, Object fill) {
		final Object newArray = Array.newInstance(array.getClass().getComponentType(), newSize);
		if (Array.getLength(array) == 0)
			return newArray;
		System.arraycopy(array, 0, newArray, 0, Array.getLength(array));
		for (int i = Array.getLength(array); i < newSize; i++)
			Array.set(newArray, i, fill);
		return newArray;
	}

	/**
	 *
	 * @param v
	 *            The Field
	 * @throws NoSuchFieldException
	 *             The Exception
	 * @throws IllegalAccessException
	 *             The Exception
	 */
	public static void unlockPrivateFinalField(Field v) throws NoSuchFieldException, IllegalAccessException {
		v.setAccessible(true);
		final Field modField = Field.class.getDeclaredField("modifiers");
		modField.setAccessible(true);
		modField.setInt(v, v.getModifiers() & ~Modifier.FINAL);
	}

	/**
	 *
	 * @param c
	 *            The Class
	 * @return A Field
	 */
	public static Field getTradeArrayFromClass(Class<?> c) {
		// search for 4D array of ITradeList objects
		for (final Field f : c.getDeclaredFields())
			if (f.getType().isArray() // D1
					&& f.getType().getComponentType().isArray() // D2
					&& f.getType().getComponentType().getComponentType().isArray() // D3
					&& f.getType().getComponentType().getComponentType().getComponentType().isArray() // D4
					&& f.getType().getComponentType().getComponentType().getComponentType().getComponentType().isAssignableFrom(EntityVillager.ITradeList.class))
				return f;
		return null;
	}
}
