package com.mcmoddev.lib.init;

import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

/** initializer for achievements */
public abstract class Achievements {

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

		if (Options.enableAchievements) {

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
		page.getAchievements().add(a);
		return a;
	}
}
