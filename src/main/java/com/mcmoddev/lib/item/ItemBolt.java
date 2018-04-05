package com.mcmoddev.lib.item;

import com.mcmoddev.lib.entity.EntityCustomBolt;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
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
	public EntityCustomBolt createBolt(final World worldIn, final ItemStack stack,
			final EntityPlayer shooter) {
		return new EntityCustomBolt(worldIn, stack, shooter);
	}

	public boolean isInfinite(final ItemStack stack, final ItemStack crossbow,
			final EntityPlayer player) {
		final int enchant = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, crossbow);
		return enchant <= 0 ? false : this.getClass() == ItemBolt.class;
	}
}
