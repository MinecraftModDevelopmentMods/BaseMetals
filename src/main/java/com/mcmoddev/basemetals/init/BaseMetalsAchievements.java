package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Achievements;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class BaseMetalsAchievements extends Achievements {

	private static boolean initDone = false;

	private BaseMetalsAchievements() {
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

			final Achievement thisIsNew = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Materials.getMaterialByName(MaterialNames.COPPER).getItem(Names.INGOT), page);
			makeAchievement("blocktastic", thisIsNew, 2, 0, Materials.getMaterialByName(MaterialNames.COPPER).getBlock(Names.BLOCK), page);
			final Achievement geologist = makeAchievement("geologist", thisIsNew, 4, 2, Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.CRACKHAMMER), page);
			final Achievement metallurgy = makeAchievement("metallurgy", geologist, 6, 2, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.BLEND), page);
			if (Options.materialEnabled(MaterialNames.BRASS)) {
				makeAchievement("brass_maker", metallurgy, 9, 3, Materials.getMaterialByName(MaterialNames.BRASS).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.BRONZE)) {
				makeAchievement("bronze_maker", metallurgy, 9, 4, Materials.getMaterialByName(MaterialNames.BRONZE).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.CUPRONICKEL)) {
				makeAchievement("cupronickel_maker", metallurgy, 9, 5, Materials.getMaterialByName(MaterialNames.CUPRONICKEL).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.ELECTRUM)) {
				makeAchievement("electrum_maker", metallurgy, 9, 6, Materials.getMaterialByName(MaterialNames.ELECTRUM).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.STEEL)) {
				makeAchievement("steel_maker", metallurgy, 9, 7, Materials.getMaterialByName(MaterialNames.STEEL).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.INVAR)) {
				makeAchievement("invar_maker", metallurgy, 9, 8, Materials.getMaterialByName(MaterialNames.INVAR).getItem(Names.INGOT), page);
			}
			if (Options.materialEnabled(MaterialNames.MITHRIL)) {
				final Achievement mithrilMaker = makeAchievement("mithril_maker", metallurgy, 11, 10, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.INGOT), page);
				makeAchievement("angel_of_death", mithrilMaker, 11, 11, Materials.getMaterialByName(MaterialNames.MITHRIL).getItem(Names.SWORD), page);
			}
			if (Options.materialEnabled(MaterialNames.AQUARIUM)) {
				final Achievement aquariumMaker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.INGOT), page);
				makeAchievement("scuba_diver", aquariumMaker, 11, 13, Materials.getMaterialByName(MaterialNames.AQUARIUM).getItem(Names.SWORD), page).setSpecial();
			}
			if (Options.materialEnabled(MaterialNames.COLDIRON)) {
				makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Materials.getMaterialByName(MaterialNames.COLDIRON).getItem(Names.SWORD), page);
			}
			if (Options.materialEnabled(MaterialNames.ADAMANTINE)) {
				makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Materials.getMaterialByName(MaterialNames.ADAMANTINE).getItem(Names.HELMET), page).setSpecial();
			}
			if (Options.materialEnabled(MaterialNames.STARSTEEL)) {
				makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Materials.getMaterialByName(MaterialNames.STARSTEEL).getItem(Names.BOOTS), page).setSpecial();
			}
		}

		initDone = true;
	}
}
