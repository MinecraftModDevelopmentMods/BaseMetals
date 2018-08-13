package com.mcmoddev.lib.block;

import java.util.Random;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.BlockSlab;
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
public abstract class BlockMMDSlab extends net.minecraft.block.BlockSlab implements IMMDObject {

	public static final PropertyEnum<BlockMMDSlab.Variant> VARIANT = PropertyEnum.<BlockMMDSlab.Variant>create(
			"variant", BlockMMDSlab.Variant.class);
	private final MMDMaterial mmdMaterial;

	/**
	 *
	 * @param material
	 *            The material the slab is made from
	 */
	public BlockMMDSlab(final MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.mmdMaterial = material;
		this.setSoundType(this.mmdMaterial.getSoundType());
		this.blockHardness = this.mmdMaterial.getBlockHardness();
		this.blockResistance = this.mmdMaterial.getBlastResistance();
		this.setHarvestLevel(this.mmdMaterial.getHarvestTool(),
				this.mmdMaterial.getRequiredHarvestLevel());

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble()) {
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}

		this.setDefaultState(iblockstate.withProperty(VARIANT, BlockMMDSlab.Variant.DEFAULT));
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 */
	@Override
	public Item getItemDropped(final IBlockState state, final Random rand, final int fortune) {
		return this.mmdMaterial.getItem(Names.SLAB);
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public ItemStack getItem(final World worldIn, final BlockPos pos, final IBlockState state) {
		return this.mmdMaterial.getItemStack(Names.SLAB);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block.
	 *
	 * @deprecated
	 */
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(final int meta) {
		IBlockState iblockstate = this.getDefaultState().withProperty(VARIANT,
				BlockMMDSlab.Variant.DEFAULT);

		if (!this.isDouble()) {
			iblockstate = iblockstate.withProperty(HALF,
					(meta & 8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
		}

		return iblockstate;
	}

	/**
	 * Convert the BlockState into the correct metadata value.
	 */
	@Override
	public int getMetaFromState(final IBlockState state) {
		int i = 0;

		if (!this.isDouble() && (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)) {
			i |= 8;
		}

		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return this.isDouble() ? new BlockStateContainer(this, (IProperty[]) new IProperty[] { VARIANT })
				: new BlockStateContainer(this, (IProperty[]) new IProperty[] { HALF, VARIANT });
	}

	/**
	 * Returns the slab block name with the type associated with it.
	 */
	@Override
	public String getTranslationKey(final int meta) {
		return super.getTranslationKey();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(final ItemStack stack) {
		return BlockMMDSlab.Variant.DEFAULT;
	}

	public static class Double extends BlockMMDSlab {

		public Double(final MMDMaterial material) {
			super(material);
		}

		@Override
		public boolean isDouble() {
			return true;
		}
	}

	public static class Half extends BlockMMDSlab {

		public Half(final MMDMaterial material) {
			super(material);
		}

		@Override
		public boolean isDouble() {
			return false;
		}
	}

	public enum Variant implements IStringSerializable {
		DEFAULT;

		@Override
		public String getName() {
			return "default";
		}
	}

	@Override
	public MMDMaterial getMMDMaterial() {
		return this.mmdMaterial;
	}
}
