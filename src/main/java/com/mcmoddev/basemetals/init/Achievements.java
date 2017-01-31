package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

import cyano.basemetals.init.Materials;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	/** smelt a new metal ingot */
	public static Achievement this_is_new;
	/** make a metal block */
	public static Achievement blocktastic;
	/** make a crack hammer */
	public static Achievement geologist;
	/** make brass blend */
	public static Achievement brass_maker;
	/** make bronze blend */
	public static Achievement bronze_maker;
	/** make electrum blend */
	public static Achievement electrum_maker;
	/** make cupronickel blend */
	public static Achievement cupronickel_maker;
	/** make steel blend */
	public static Achievement steel_maker;
	/** make invar blend */
	public static Achievement invar_maker;
	/** make mithril blend */
	public static Achievement mithril_maker;
	/** make aquarium blend */
	public static Achievement aquarium_maker;
	/** make any alloy ingot */
	public static Achievement metallurgy;
	/** decked out completely in cold-iron */
	public static Achievement demon_slayer;
	/** decked out fully in mithril */
	public static Achievement angel_of_death;
	/** full suit of aquarium armor */
	public static Achievement scuba_diver;
	/** craft star-steel boots */
	public static Achievement moon_boots;
	/** full suit of adamantine armor */
	public static Achievement juggernaut;

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
}
