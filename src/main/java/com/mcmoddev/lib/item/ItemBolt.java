package com.mcmoddev.lib.item;

import com.mcmoddev.lib.entity.EntityCustomBolt;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ItemBolt extends net.minecraft.item.Item {

	/**
	 *
	 */
	public ItemBolt() {
		this.setCreativeTab(CreativeTabs.COMBAT);
	}

	/**
	 *
	 * @param worldIn
	 *            The world
	 * @param stack
	 *            The itemstack
	 * @param shooter
	 *            The shooter
	 * @return The Bolt
	 */
	public EntityCustomBolt createBolt(World worldIn, ItemStack stack, EntityPlayer shooter) {
		return new EntityCustomBolt(worldIn, stack, shooter);
	}

	public boolean isInfinite(ItemStack stack, ItemStack crossbow, net.minecraft.entity.player.EntityPlayer player) {
		int enchant = net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, crossbow);
		return enchant <= 0 ? false : this.getClass() == ItemBolt.class;
	}
}
