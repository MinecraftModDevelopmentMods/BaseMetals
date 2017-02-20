package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	/** smelt a new metal ingot */
	public static Achievement thisIsNew;
	/** make a metal block */
	public static Achievement blocktastic;
	/** make a crack hammer */
	public static Achievement geologist;
	/** make any alloy ingot */
	public static Achievement metallurgy;
	/** make brass blend */
	public static Achievement brassMaker;
	/** make bronze blend */
	public static Achievement bronzeMaker;
	/** make electrum blend */
	public static Achievement electrumMaker;
	/** make cupronickel blend */
	public static Achievement cupronickelMaker;
	/** make steel blend */
	public static Achievement steelMaker;
	/** make invar blend */
	public static Achievement invarMaker;
	/** make mithril blend */
	public static Achievement mithrilMaker;
	/** make aquarium blend */
	public static Achievement aquariumMaker;
	/** decked out completely in cold-iron */
	public static Achievement demonSlayer;
	/** decked out fully in mithril */
	public static Achievement angelOfDeath;
	/** full suit of aquarium armor */
	public static Achievement scubaDiver;
	/** craft star-steel boots */
	public static Achievement moonBoots;
	/** full suit of adamantine armor */
	public static Achievement juggernaut;

	private static boolean initDone = false;

	private Achievements() {
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
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			thisIsNew = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Materials.copper.ingot, page); // TODO: Make this check for all ingots
			blocktastic = makeAchievement("blocktastic", thisIsNew, 2, 0, Materials.copper.block, page); // TODO: Make this checks for all blocks
			geologist = makeAchievement("geologist", thisIsNew, 4, 2, Materials.vanilla_iron.crackhammer, page); // TODO: Make this check for all crackhammers
			metallurgy = makeAchievement("metallurgy", geologist, 6, 2, Materials.brass.blend, page); // TODO: Make this check for all blends
			if (Options.enableBrass) {
				brassMaker = makeAchievement("brass_maker", metallurgy, 9, 3, Materials.brass.ingot, page);
			}
			if (Options.enableBronze) {
				bronzeMaker = makeAchievement("bronze_maker", metallurgy, 9, 4, Materials.bronze.ingot, page);
			}
			if (Options.enableCupronickel) {
				cupronickelMaker = makeAchievement("cupronickel_maker", metallurgy, 9, 5, Materials.cupronickel.ingot, page);
			}
			if (Options.enableElectrum) {
				electrumMaker = makeAchievement("electrum_maker", metallurgy, 9, 6, Materials.electrum.ingot, page);
			}
			if (Options.enableSteel) {
				steelMaker = makeAchievement("steel_maker", metallurgy, 9, 7, Materials.steel.ingot, page);
			}
			if (Options.enableInvar) {
				invarMaker = makeAchievement("invar_maker", metallurgy, 9, 8, Materials.invar.ingot, page);
			}
			if (Options.enableMithril) {
				mithrilMaker = makeAchievement("mithril_maker", metallurgy, 11, 10, Materials.mithril.ingot, page);
			}
			if (Options.enableAquarium) {
				aquariumMaker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Materials.aquarium.ingot, page);
			}
			if (Options.enableColdIron) {
				demonSlayer = makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Materials.coldiron.sword, page);
			}
			if (Options.enableMithril) {
				angelOfDeath = makeAchievement("angel_of_death", mithrilMaker, 11, 11, Materials.mithril.sword, page);
			}
			if (Options.enableAquarium) {
				scubaDiver = makeAchievement("scuba_diver", aquariumMaker, 11, 13, Materials.aquarium.sword, page).setSpecial();
			}
			if (Options.enableAdamantine) {
				juggernaut = makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Materials.adamantine.helmet, page).setSpecial();
			}
			if (Options.enableStarSteel) {
				moonBoots = makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Materials.starsteel.boots, page).setSpecial();
			}
		}

		initDone = true;
	}
}
