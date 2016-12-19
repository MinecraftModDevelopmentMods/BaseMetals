package cyano.basemetals.init;

import cyano.basemetals.init.Materials;
import cyano.basemetals.util.Config.Options;
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

			this_is_new = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Materials.getMaterialByName("copper").ingot, page);  // Make sure this checks for all ingots
			blocktastic = makeAchievement("blocktastic", this_is_new, 2, 0, Materials.getMaterialByName("copper").block, page); // Make sure this checks for all blocks
			geologist = makeAchievement("geologist", this_is_new, 4, 2, Materials.getMaterialByName("iron").crackhammer, page);
			metallurgy = makeAchievement("metallurgy", geologist, 6, 2, Materials.getMaterialByName("brass").blend, page);
			brass_maker = makeAchievement("brass_maker", metallurgy, 9, 3, Materials.getMaterialByName("brass").ingot, page);
			bronze_maker = makeAchievement("bronze_maker", metallurgy, 9, 4, Materials.getMaterialByName("bronze").ingot, page);
			cupronickel_maker = makeAchievement("cupronickel_maker", metallurgy, 9, 5, Materials.getMaterialByName("cupronickel").ingot, page);
			electrum_maker = makeAchievement("electrum_maker", metallurgy, 9, 6, Materials.getMaterialByName("electrum").ingot, page);
			steel_maker = makeAchievement("steel_maker", metallurgy, 9, 7, Materials.getMaterialByName("steel").ingot, page);
			invar_maker = makeAchievement("invar_maker", metallurgy, 9, 8, Materials.getMaterialByName("invar").ingot, page);
			mithril_maker = makeAchievement("mithril_maker", metallurgy, 11, 10, Materials.getMaterialByName("mithril").ingot, page);
			aquarium_maker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Materials.getMaterialByName("aquarium").ingot, page);
			demon_slayer = makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Materials.getMaterialByName("coldiron").sword, page);
			angel_of_death = makeAchievement("angel_of_death", mithril_maker, 11, 11, Materials.getMaterialByName("mithril").sword, page);
			scuba_diver = makeAchievement("scuba_diver", aquarium_maker, 11, 13, Materials.getMaterialByName("aquarium").sword, page).setSpecial();
			juggernaut = makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Materials.getMaterialByName("adamantine").helmet, page).setSpecial();
			moon_boots = makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Materials.getMaterialByName("starsteel").boots, page).setSpecial();
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
