package com.mcmoddev.lib.items;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

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
public class ItemMetalSmallBlend extends Item implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material to make the small blend from
	 */
	public ItemMetalSmallBlend(MetalMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = Oredicts.DUSTTINY + this.material.getCapitalizedName(); // same oreDict entry as powder
	}

	@Override
	public void onCreated(final ItemStack item, final World world, final EntityPlayer crafter) {
		super.onCreated(item, world, crafter);
		// achievement
    	if (Options.enableAchievements) {
    		crafter.addStat(Achievements.metallurgy, 1);
    	}
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}
}
