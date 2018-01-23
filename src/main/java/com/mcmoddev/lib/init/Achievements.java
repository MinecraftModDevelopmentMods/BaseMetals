package com.mcmoddev.lib.init;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

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

	private static final BiMap<String, Achievement> achievementRegistry = HashBiMap.create(16);
	private static Map<MMDMaterial, List<Achievement>> achievementsByMaterial = new HashMap<>();

	protected Achievements() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
	}

	protected static Achievement makeAchievement(@Nonnull final String baseName, @Nonnull final Achievement requirement, @Nonnull final int x, @Nonnull final int y, @Nonnull final Item icon, @Nonnull final AchievementPage page) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon), page);
	}

	protected static Achievement makeAchievement(@Nonnull final String baseName, @Nonnull final Achievement requirement, @Nonnull final int x, @Nonnull final int y, @Nonnull final Block icon, @Nonnull final AchievementPage page) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon), page);
	}

	protected static Achievement makeAchievement(@Nonnull String baseName, @Nonnull Achievement requirement, @Nonnull final int x, @Nonnull final int y, @Nonnull final ItemStack icon, @Nonnull final AchievementPage page) {
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
	public static Achievement getAchievementByName(@Nonnull final String name) {
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
	public static String getNameOfAchievement(@Nonnull final Achievement a) {
		return achievementRegistry.inverse().get(a);
	}

	public static Map<String, Achievement> getAchievementRegistry() {
		return Collections.unmodifiableMap(achievementRegistry);
	}

	/**
	 * Gets a map of all achievement added, sorted by material
	 *
	 * @return An unmodifiable map of added items categorized by material
	 */
	public static Map<MMDMaterial, List<Achievement>> getAchievementsByMaterial() {
		return Collections.unmodifiableMap(achievementsByMaterial);
	}
}
