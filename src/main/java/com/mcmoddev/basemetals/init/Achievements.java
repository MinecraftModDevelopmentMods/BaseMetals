package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.util.Config.Options;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	/** Smelt a new metal ingot */
	public static Achievement thisIsNew;
	/** Make a metal block */
	public static Achievement blocktastic;
	/** Make a Crack Hammer */
	public static Achievement geologist;
	/** Make any alloy ingot */
	public static Achievement metallurgy;
	/** Make Brass blend */
	public static Achievement brassMaker;
	/** Make Bronze blend */
	public static Achievement bronzeMaker;
	/** Make Electrum blend */
	public static Achievement electrumMaker;
	/** Make Cupronickel blend */
	public static Achievement cupronickelMaker;
	/** Make Steel blend */
	public static Achievement steelMaker;
	/** Make Invar blend */
	public static Achievement invarMaker;
	/** Make Mithril blend */
	public static Achievement mithrilMaker;
	/** Make Aquarium blend */
	public static Achievement aquariumMaker;
	/** Decked out completely in Cold-Iron */
	public static Achievement demonSlayer;
	/** Decked out fully in Mithril */
	public static Achievement angelOfDeath;
	/** Full suit of Aquarium armor */
	public static Achievement scubaDiver;
	/** Craft Star-Steel boots */
	public static Achievement moonBoots;
	/** Full suit of Adamantine armor */
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
