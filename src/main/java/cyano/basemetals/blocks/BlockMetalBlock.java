package cyano.basemetals.blocks;

import cyano.basemetals.init.Achievements;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Metal Block
 */
public class BlockMetalBlock extends Block implements IOreDictionaryEntry, IMetalObject {

	final MetalMaterial metal;

	public BlockMetalBlock(MetalMaterial metal) {
		this(metal, false);
	}

	public BlockMetalBlock(MetalMaterial metal, boolean glows) {
		super(Material.iron);
		this.stepSound = Block.soundTypeMetal;
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.setCreativeTab(CreativeTabs.tabBlock);
		if(glows) this.setLightLevel(0.5f);
	}

	///// OVERRIDE OF ALL METHODS THAT DEPEND ON BLOCK MATERIAL: /////
	@Override
	public MapColor getMapColor(final IBlockState p_getMapColor_1_) {
		return MapColor.ironColor;
	}

	@Override
	public boolean isFullBlock() {
		return true;
	}

	@Override
	public boolean isNormalCube() {
		return true;
	}

	@Override
	public boolean isVisuallyOpaque() {
		return true;
	}

	@Override
	public boolean isFullCube() {
		return true;
	}

	@Override
	public boolean isPassable(final IBlockAccess p_isPassable_1_, final BlockPos p_isPassable_2_) {
		return false;
	}

	@Override
	public boolean isReplaceable(final World p_isReplaceable_1_, final BlockPos p_isReplaceable_2_) {
		return false;
	}

	@Override
	public boolean isNormalCube(final IBlockAccess bs, final BlockPos coord) {
		return this.isNormalCube();
	}

	@Override
	public String getOreDictionaryName() {
		return "block"+metal.getCapitalizedName();
	}

	@Override
	public void onBlockPlacedBy(final World w, final BlockPos coord, final IBlockState bs, final EntityLivingBase placer, final ItemStack src) {
		super.onBlockPlacedBy(w, coord, bs, placer, src);
		// achievement
		if(placer instanceof EntityPlayer) {
			((EntityPlayer)placer).addStat(Achievements.blocktastic, 1);
		}
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
