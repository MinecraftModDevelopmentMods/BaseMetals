package cyano.basemetals.blocks;

import java.util.Random;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
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
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class BlockMetalSlab extends BlockSlab implements IMetalObject {

	public static final PropertyEnum<BlockMetalSlab.Variant> VARIANT = PropertyEnum.<BlockMetalSlab.Variant>create("variant", BlockMetalSlab.Variant.class);

	final MetalMaterial metal;

	private Item slabItem;

	/**
	 * 
	 * @param metal
	 */
	public BlockMetalSlab(MetalMaterial metal) {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());

		IBlockState iblockstate = this.blockState.getBaseState();

		if (!this.isDouble())
			iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);

		this.setDefaultState(iblockstate.withProperty(VARIANT, BlockMetalSlab.Variant.DEFAULT));
	}

	@Override
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
		return this.isDouble() ? new BlockStateContainer(this, new IProperty[] { VARIANT }): new BlockStateContainer(this, new IProperty[] { HALF, VARIANT });
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

	private Item getSlabItem() {
		if(this.slabItem == null) {
			FMLLog.severe("getting item for slab: %s, %s", this.getRegistryName().getResourceDomain(), this.metal.getName() + "_slab");
			this.slabItem = Item.REGISTRY.getObject(new ResourceLocation(this.getRegistryName().getResourceDomain(), this.metal.getName() + "_slab"));
		}

		return this.slabItem;
	}

    /**
     * Get the Item that this Block should drop when harvested.
     */
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
    	return getSlabItem();
    }

	@Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
    	return new ItemStack(this.getSlabItem());
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
	public MetalMaterial getMetalMaterial() {
		return this.metal;
	}

	//@Override
	//public String getOreDictionaryName() {
	//	return "slab" + metal.getCapitalizedName();
	//}
}
