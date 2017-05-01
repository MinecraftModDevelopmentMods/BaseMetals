package com.mcmoddev.lib.init;

import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/**
 * Initializer for Achievements
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Achievements {

	private static BiMap<String, Achievement> achievementRegistry = HashBiMap.create(16);

	private static boolean initDone = false;

	protected Achievements() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (Options.enableAchievements()) {

		}

		initDone = true;
	}

	protected static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Item icon, AchievementPage page) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon), page);
	}

	protected static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Block icon, AchievementPage page) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon), page);
	}

	protected static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, ItemStack icon, AchievementPage page) {
		final Achievement a = new Achievement(baseName, baseName, x, y, icon, requirement).registerStat();
		achievementRegistry.put(baseName, a);
		page.getAchievements().add(a);
		return a;
	}

	/**
	 * Gets an achievement by its name. The name is the name as it is registered in
	 * the GameRegistry, not its unlocalized name (the unlocalized name is the
	 * registered name plus the prefix "basemetals.")
	 *
	 * @param name
	 *            The name of the achievement in question
	 * @return The achievement matching that name, or null if there isn't one
	 */
	public static Achievement getAchievementByName(String name) {
		return achievementRegistry.get(name);
	}

	/**
	 * This is the reverse of the getAchievementByName(...) method, returning the
	 * registered name of an achievement instance (Base Metals achievements only).
	 *
	 * @param a
	 *            The achievement in question
	 * @return The name of the achievement, or null if the item is not a Base Metals
	 *         achievement.
	 */
	public static String getNameOfAchievement(Achievement a) {
		return achievementRegistry.inverse().get(a);
	}

	public static Map<String, Achievement> getAchievementRegistry() {
		return achievementRegistry;
	}

	/**
	 * Gets a map of all achievement added, sorted by material
	 *
	 * @return An unmodifiable map of added items catagorized by material
	 */
//	public static Map<MMDMaterial, List<Achievement>> getAchievementsByMaterial() {
//		return Collections.unmodifiableMap(achievementsByMaterial);
//	}
}
