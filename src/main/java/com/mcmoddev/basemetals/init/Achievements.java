package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

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

			thisIsNew = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Materials.getMaterialByName(MaterialNames.COPPER).getItem(Names.INGOT), page); // TODO: Make this check for all ingots
			blocktastic = makeAchievement("blocktastic", thisIsNew, 2, 0, Materials.getMaterialByName(MaterialNames.COPPER).getBlock(Names.BLOCK), page); // TODO: Make this checks for all blocks
			geologist = makeAchievement("geologist", thisIsNew, 4, 2, Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.CRACKHAMMER), page); // TODO: Make this check for all crackhammers
			metallurgy = makeAchievement("metallurgy", geologist, 6, 2, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.BLEND), page); // TODO: Make this check for all blends
			if (Options.enableBrass) {
				brassMaker = makeAchievement("brass_maker", metallurgy, 9, 3, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.INGOT), page);
			}
			if (Options.enableBronze) {
				bronzeMaker = makeAchievement("bronze_maker", metallurgy, 9, 4, Materials.getMaterialByName(MaterialNames.BRONZE).getItem(Names.INGOT), page);
			}
			if (Options.enableCupronickel) {
				cupronickelMaker = makeAchievement("cupronickel_maker", metallurgy, 9, 5, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getItem(Names.INGOT), page);
			}
			if (Options.enableElectrum) {
				electrumMaker = makeAchievement("electrum_maker", metallurgy, 9, 6, Materials.getMaterialByName(MaterialNames.ELECTRUM).getItem(Names.INGOT), page);
			}
			if (Options.enableSteel) {
				steelMaker = makeAchievement("steel_maker", metallurgy, 9, 7, Materials.getMaterialByName(MaterialNames.STEEL).getItem(Names.INGOT), page);
			}
			if (Options.enableInvar) {
				invarMaker = makeAchievement("invar_maker", metallurgy, 9, 8, Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT), page);
			}
			if (Options.enableMithril) {
				mithrilMaker = makeAchievement("mithril_maker", metallurgy, 11, 10, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.INGOT), page);
			}
			if (Options.enableAquarium) {
				aquariumMaker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.INGOT), page);
			}
			if (Options.enableColdIron) {
				demonSlayer = makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.SWORD), page);
			}
			if (Options.enableMithril) {
				angelOfDeath = makeAchievement("angel_of_death", mithrilMaker, 11, 11, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.SWORD), page);
			}
			if (Options.enableAquarium) {
				scubaDiver = makeAchievement("scuba_diver", aquariumMaker, 11, 13, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.SWORD), page).setSpecial();
			}
			if (Options.enableAdamantine) {
				juggernaut = makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Materials.getMaterialByName(MaterialNames.ADAMANTINE).getItem(Names.HELMET), page).setSpecial();
			}
			if (Options.enableStarSteel) {
				moonBoots = makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Materials.getMaterialByName(MaterialNames.STARSTEEL).getItem(Names.BOOTS), page).setSpecial();
			}
		}

		initDone = true;
	}
}
