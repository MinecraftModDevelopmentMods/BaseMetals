package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

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

		if (Options.enableAchievements()) {
			AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			final Achievement thisIsNew = makeAchievement(AchievementNames.THIS_IS_NEW, AchievementList.BUILD_FURNACE, 0, 0, Materials.getMaterialByName(MaterialNames.COPPER).getItem(Names.INGOT), page);
			makeAchievement(AchievementNames.BLOCKTASTIC, thisIsNew, 2, 0, Materials.getMaterialByName(MaterialNames.COPPER).getBlock(Names.BLOCK), page);
			final Achievement geologist = makeAchievement(AchievementNames.GEOLOGIST, thisIsNew, 4, 2, Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.CRACKHAMMER), page);
			final Achievement metallurgy = makeAchievement(AchievementNames.METALLURGY, geologist, 6, 2, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.BLEND), page);
			if (metallurgy != null) {
				if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
					makeAchievement(AchievementNames.BRASS_MAKER, metallurgy, 9, 3, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.BRONZE)) {
					makeAchievement(AchievementNames.BRONZE_MAKER, metallurgy, 9, 4, Materials.getMaterialByName(MaterialNames.BRONZE).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL)) {
					makeAchievement(AchievementNames.CUPRONICKEL_MAKER, metallurgy, 9, 5, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.ELECTRUM)) {
					makeAchievement(AchievementNames.ELECTRUM_MAKER, metallurgy, 9, 6, Materials.getMaterialByName(MaterialNames.ELECTRUM).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
					makeAchievement(AchievementNames.STEEL_MAKER, metallurgy, 9, 7, Materials.getMaterialByName(MaterialNames.STEEL).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.INVAR)) {
					makeAchievement(AchievementNames.INVAR_MAKER, metallurgy, 9, 8, Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
					final Achievement mithrilMaker = makeAchievement(AchievementNames.MITHRIL_MAKER, metallurgy, 11, 10, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.INGOT), page);
					makeAchievement(AchievementNames.ANGEL_OF_DEATH, mithrilMaker, 11, 11, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.SWORD), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
					final Achievement aquariumMaker = makeAchievement(AchievementNames.AQUARIUM_MAKER, metallurgy, 11, 12, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.INGOT), page);
					makeAchievement(AchievementNames.SCUBA_DIVER, aquariumMaker, 11, 13, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.SWORD), page).setSpecial();
				}
				if (Options.isMaterialEnabled(MaterialNames.COLDIRON)) {
					makeAchievement(AchievementNames.DEMON_SLAYER, AchievementList.PORTAL, -5, 5, Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.SWORD), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.ADAMANTINE)) {
					makeAchievement(AchievementNames.JUGGERNAUT, AchievementList.PORTAL, -7, 3, Materials.getMaterialByName(MaterialNames.ADAMANTINE).getItem(Names.HELMET), page).setSpecial();
				}
				if (Options.isMaterialEnabled(MaterialNames.STARSTEEL)) {
					makeAchievement(AchievementNames.MOON_BOOTS, AchievementList.THE_END, -2, 6, Materials.getMaterialByName(MaterialNames.STARSTEEL).getItem(Names.BOOTS), page).setSpecial();
				}
			}
		}

		initDone = true;
	}
}
