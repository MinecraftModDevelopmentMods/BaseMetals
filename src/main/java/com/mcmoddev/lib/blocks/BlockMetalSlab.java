package com.mcmoddev.lib.blocks;

import java.util.Random;

import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class BlockMetalSlab extends BlockSlab implements IMetalObject {

	public static final PropertyEnum<BlockMetalSlab.Variant> VARIANT = PropertyEnum.<BlockMetalSlab.Variant>create("variant", BlockMetalSlab.Variant.class);

	final MetalMaterial material;

	/**
	 *
	 * @param material The material the slab is made from
	 */
	public BlockMetalSlab(MetalMaterial material) {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.material = material;
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble())
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		this.setDefaultState(iblockstate.withProperty(VARIANT, BlockMetalSlab.Variant.DEFAULT));
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT, BlockMetalSlab.Variant.DEFAULT);

		if (!this.isDouble())
			iblockstate = iblockstate.withProperty(HALF, (meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int i = 0;

		if (!this.isDouble() && (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP))
			i |= 8;

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { VARIANT }) : new BlockStateContainer(this, new IProperty[] { HALF, VARIANT });
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public boolean isDouble() {
		return false;
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return BlockMetalSlab.Variant.DEFAULT;
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return this.material.slab;
	}

	@Override
	@Deprecated
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.material.slab);
	}

	/**
	 *
	 * @author Jasmine Iwanek
	 *
	 */
	public enum Variant implements IStringSerializable {
		DEFAULT;
		@Override
		public String getName() {
			return "default";
		}
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

	// @Override
	// public String getOreDictionaryName() {
	// return Oredicts.SLAB + material.getCapitalizedName();
	// }
}
