package cyano.basemetals.init;

import cyano.basemetals.BaseMetals;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.AchievementPage;

/** initializer for achievements */
public abstract class Achievements {

	public static AchievementPage page;

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
	public static void init() {
		if(initDone) return;

		page = new AchievementPage(BaseMetals.NAME);
		AchievementPage.registerAchievementPage(page);

		this_is_new = makeAchievement("this_is_new", AchievementList.BUILD_FURNACE, 0, 0, Items.copper_ingot);
		blocktastic = makeAchievement("blocktastic", this_is_new,2,0,Blocks.copper_block);
		geologist = makeAchievement("geologist", this_is_new,4,2,Items.iron_crackhammer);
		metallurgy = makeAchievement("metallurgy", geologist,6,2,Items.brass_blend);
		brass_maker = makeAchievement("brass_maker", metallurgy,9,3,Items.brass_ingot);
		bronze_maker = makeAchievement("bronze_maker", metallurgy, 9, 4, Items.bronze_ingot);
		cupronickel_maker = makeAchievement("cupronickel_maker", metallurgy, 9, 5, Items.cupronickel_ingot);
		electrum_maker = makeAchievement("electrum_maker", metallurgy, 9, 6, Items.electrum_ingot);
		steel_maker = makeAchievement("steel_maker", metallurgy, 9, 7, Items.steel_ingot);
		invar_maker = makeAchievement("invar_maker", metallurgy, 9, 8, Items.invar_ingot);
		mithril_maker = makeAchievement("mithril_maker", metallurgy, 11, 10, Items.mithril_ingot);
		aquarium_maker = makeAchievement("aquarium_maker", metallurgy, 11, 12, Items.aquarium_ingot);
		demon_slayer = makeAchievement("demon_slayer", AchievementList.PORTAL, -5, 5, Items.coldiron_sword);
		angel_of_death = makeAchievement("angel_of_death", mithril_maker, 11, 11, Items.mithril_sword);
		scuba_diver = makeAchievement("scuba_diver", aquarium_maker, 11, 13, Items.aquarium_sword);
		scuba_diver.setSpecial();
		juggernaut = makeAchievement("juggernaut", AchievementList.PORTAL, -7, 3, Items.adamantine_helmet);
		juggernaut.setSpecial();
		moon_boots = makeAchievement("moon_boots", AchievementList.THE_END, -2, 6, Items.starsteel_boots);
		moon_boots.setSpecial();

		initDone = true;
	}

	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Item icon) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon));
	}

	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, Block icon) {
		return makeAchievement(baseName, requirement, x, y, new ItemStack(icon));
	}

	private static Achievement makeAchievement(String baseName, Achievement requirement, int x, int y, ItemStack icon) {
		Achievement a = new Achievement(baseName, baseName, x, y, icon, requirement);
		a.registerStat();
		page.getAchievements().add(a);
		return a;
	}
}
