package com.mcmoddev.lib.item;

import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Blends
 * 
 * @author DrCyano
 *
 */
public class ItemMMDBlend extends GenericMMDItem {

	/**
	 *
	 * @param material
	 *            The material to make the blend from
	 */
	public ItemMMDBlend(MMDMaterial material) {
		super(material);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		// achievement
		if (Options.enableAchievements()) {
			crafter.addStat(Achievements.getAchievementByName("metallurgy"), 1);
		}
	}
}
