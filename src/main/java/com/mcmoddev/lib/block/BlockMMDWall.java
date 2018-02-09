package com.mcmoddev.lib.block;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
	public BlockMMDWall(final MMDMaterial material) {
		super(material.getBlock(Names.BLOCK));
		this.material = material;
		this.setSoundType(this.material.getSoundType());
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel(this.material.getHarvestTool(), this.material.getRequiredHarvestLevel());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item itemIn, final CreativeTabs tab, final NonNullList<ItemStack> list) {
        list.add(new ItemStack(itemIn));
	}

	@Override
	public boolean canPlaceTorchOnTop(final IBlockState state, final IBlockAccess world, final BlockPos pos) {
		return true;
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
