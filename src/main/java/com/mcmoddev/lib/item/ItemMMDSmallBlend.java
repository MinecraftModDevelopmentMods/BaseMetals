package com.mcmoddev.lib.item;

import com.mcmoddev.lib.init.Achievements;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMMDSmallBlend extends Item implements IMMDObject {

	protected final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material to make the small blend from
	 */
	public ItemMMDSmallBlend(MMDMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		// achievement
		if (Options.enableAchievements()) {
			crafter.addStat(Achievements.getAchievementByName("metallurgy"), 1);
		}
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
