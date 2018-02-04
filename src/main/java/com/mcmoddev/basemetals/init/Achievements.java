package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.AchievementNames;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Loader;

/** initializer for achievements */
public class Achievements extends com.mcmoddev.lib.init.Achievements {

	private Achievements() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		if (Options.enableAchievements()) {
			final AchievementPage page = new AchievementPage(Loader.instance().activeModContainer().getModId());
			AchievementPage.registerAchievementPage(page);

			final MMDMaterial brass = Materials.getMaterialByName(MaterialNames.BRASS);
			final MMDMaterial copper = Materials.getMaterialByName(MaterialNames.COPPER);
			final Achievement thisIsNew = makeAchievement(AchievementNames.THIS_IS_NEW, AchievementList.BUILD_FURNACE, 0, 0, copper.getItem(Names.INGOT), page);
			makeAchievement(AchievementNames.BLOCKTASTIC, thisIsNew, 2, 0, copper.getBlock(Names.BLOCK), page);
			final Achievement geologist = makeAchievement(AchievementNames.GEOLOGIST, thisIsNew, 4, 2, Materials.getMaterialByName(MaterialNames.IRON).getItem(Names.CRACKHAMMER), page);
			final Achievement metallurgy = makeAchievement(AchievementNames.METALLURGY, geologist, 6, 2, brass.getItem(Names.BLEND), page);
			if (metallurgy != null) {
				if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
					makeAchievement(AchievementNames.BRASS_MAKER, metallurgy, 9, 3, brass.getItem(Names.INGOT), page);
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
				if (Options.isMaterialEnabled(MaterialNames.PEWTER)) {
					makeAchievement(AchievementNames.PEWTER_MAKER, metallurgy, 9, 9, Materials.getMaterialByName(MaterialNames.PEWTER).getItem(Names.INGOT), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
					final MMDMaterial mithril = Materials.getMaterialByName(MaterialNames.MITHRIL);
					final Achievement mithrilMaker = makeAchievement(AchievementNames.MITHRIL_MAKER, metallurgy, 11, 10, mithril.getItem(Names.INGOT), page);
					makeAchievement(AchievementNames.ANGEL_OF_DEATH, mithrilMaker, 11, 11, mithril.getItem(Names.SWORD), page);
				}
				if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
					final MMDMaterial aquarium = Materials.getMaterialByName(MaterialNames.AQUARIUM);
					final Achievement aquariumMaker = makeAchievement(AchievementNames.AQUARIUM_MAKER, metallurgy, 11, 12, aquarium.getItem(Names.INGOT), page);
					makeAchievement(AchievementNames.SCUBA_DIVER, aquariumMaker, 11, 13, aquarium.getItem(Names.SWORD), page).setSpecial();
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
	}
}
