package com.mcmoddev.lib.blocks;

import java.util.List;

import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockWall;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMetalWall extends BlockWall implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material the wall is made from
	 */
	public BlockMetalWall(MetalMaterial material) {
		super(material.block);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.oreDict = Oredicts.WALL + this.material.getCapitalizedName();
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	/**
	 *
	 * @param material The material the wall is made from
	 * @param modelBlock The block to get the model from
	 */
	public BlockMetalWall(MetalMaterial material, Block modelBlock) {
		super(modelBlock);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.oreDict = "wall" + this.material.getCapitalizedName();
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
		list.add(new ItemStack(itemIn, 1, BlockWall.EnumType.NORMAL.getMetadata()));
	}

	@Override
	public MetalMaterial getMaterial() {
		return this.material;
	}

	@Override
	@Deprecated
	public MetalMaterial getMetalMaterial() {
		return this.material;
	}

	@Override
	public String getOreDictionaryName() {
		return this.oreDict;
	}
}
