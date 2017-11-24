package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMMDWall extends net.minecraft.block.BlockWall implements IMMDObject {

	final MMDMaterial material;

	/**
	 *
	 * @param material
	 *            The material the wall is made from
	 */
	public BlockMMDWall(MMDMaterial material) {
		super(material.getBlock(Names.BLOCK));
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, net.minecraft.block.BlockWall.EnumType.NORMAL.getMetadata()));
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
