package com.mcmoddev.lib.item;

import com.mcmoddev.lib.entity.EntityCustomArrow;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemMetalArrow extends ItemArrow implements IOreDictionaryEntry, IMetalObject {

	protected final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material to make the arrow from
	 */
	public ItemMetalArrow(MetalMaterial material) {
		this.material = material;
		this.setCreativeTab(CreativeTabs.MATERIALS);
		this.oreDict = Oredicts.ARROW + this.material.getCapitalizedName();
	}

	/**
	 *
	 * @param worldIn The world
	 * @param stack The itemstack
	 * @param shooter The shooter
	 * @return The Custom Arrow
	 */
	public EntityCustomArrow createArrow(World worldIn, ItemStack stack, EntityPlayer shooter) {
		return new EntityCustomArrow(worldIn, stack, shooter);
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
