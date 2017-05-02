package com.mcmoddev.lib.item;

import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Ingots
 *
 * @author DrCyano
 *
 */
public class ItemMMDIngot extends GenericMMDItem {

	/**
	 *
	 * @param material
	 *            The material to make the ingot from
	 */
	public ItemMMDIngot(MMDMaterial material) {
		super(material);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		crafter.addStat(Achievements.getAchievementByName("this_is_new"), 1);
	}
}
