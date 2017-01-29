package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

import cyano.basemetals.init.Materials;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public abstract class Achievements {

	public static Achievement this_is_new; // smelt a new metal ingot
	public static Achievement blocktastic; // make a metal block
	public static Achievement geologist; // make a crack hammer
	public static Achievement brass_maker; // make blend
	public static Achievement bronze_maker; // make blend
	public static Achievement electrum_maker; // make blend
	public static Achievement cupronickel_maker; // make blend
	public static Achievement steel_maker; // make blend
	public static Achievement invar_maker; // make blend
	public static Achievement mithril_maker; // make blend
	public static Achievement aquarium_maker; // make blend
	public static Achievement metallurgy; // make any alloy ingot
	public static Achievement demon_slayer; // decked out completely in cold-iron
	public static Achievement angel_of_death; // decked out fully in mithril
	public static Achievement scuba_diver; // full suit of aquarium armor
	public static Achievement moon_boots; // craft star-steel boots
	public static Achievement juggernaut; // full adamantine armor

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		if (Options.ENABLE_ACHIEVEMENTS) {
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			this_is_new = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Materials.copper.ingot, page);  // Make sure this checks for all ingots
			blocktastic = makeAchievement("blocktastic", this_is_new, 2, 0, Materials.copper.block, page); // Make sure this checks for all blocks
			geologist = makeAchievement("geologist", this_is_new, 4, 2, Materials.vanilla_iron.crackhammer, page);
			metallurgy = makeAchievement("metallurgy", geologist, 6, 2, Materials.brass.blend, page);
			if (Options.ENABLE_BRASS) {
				brass_maker = makeAchievement("brass_maker", metallurgy, 9, 3, Materials.brass.ingot, page);
			}
			if (Options.ENABLE_BRONZE) {
				bronze_maker = makeAchievement("bronze_maker", metallurgy, 9, 4, Materials.bronze.ingot, page);
			}
			if (Options.ENABLE_CUPRONICKEL) {
				cupronickel_maker = makeAchievement("cupronickel_maker", metallurgy, 9, 5, Materials.cupronickel.ingot, page);
			}
			if (Options.ENABLE_ELECTRUM) {
				electrum_maker = makeAchievement("electrum_maker", metallurgy, 9, 6, Materials.electrum.ingot, page);
			}
			if (Options.ENABLE_STEEL) {
				steel_maker = makeAchievement("steel_maker", metallurgy, 9, 7, Materials.steel.ingot, page);
			}
			if (Options.ENABLE_INVAR) {
				invar_maker = makeAchievement("invar_maker", metallurgy, 9, 8, Materials.invar.ingot, page);
			}
			if (Options.ENABLE_MITHRIL) {
				mithril_maker = makeAchievement("mithril_maker", metallurgy, 11, 10, Materials.mithril.ingot, page);
			}
			if (Options.ENABLE_AQUARIUM) {
				aquarium_maker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Materials.aquarium.ingot, page);
			}
			if (Options.ENABLE_COLDIRON) {
				demon_slayer = makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Materials.coldiron.sword, page);
			}
			if (Options.ENABLE_MITHRIL) {
				angel_of_death = makeAchievement("angel_of_death", mithril_maker, 11, 11, Materials.mithril.sword, page);
			}
			if (Options.ENABLE_AQUARIUM) {
				scuba_diver = makeAchievement("scuba_diver", aquarium_maker, 11, 13, Materials.aquarium.sword, page).setSpecial();
			}
			if (Options.ENABLE_ADAMANTINE) {
				juggernaut = makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Materials.adamantine.helmet, page).setSpecial();
			}
			if (Options.ENABLE_STARSTEEL) {
				moon_boots = makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Materials.starsteel.boots, page).setSpecial();
			}
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
