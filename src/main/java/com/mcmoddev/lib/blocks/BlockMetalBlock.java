package com.mcmoddev.lib.blocks;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.registry.IOreDictionaryEntry;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Metal Block
 */
public class BlockMetalBlock extends Block implements IOreDictionaryEntry, IMetalObject {

	private final MetalMaterial material;
	private final String oreDict;

	/**
	 *
	 * @param material The material the block is made from
	 */
	public BlockMetalBlock(MetalMaterial material) {
		this(material, false);
	}

	public BlockMetalBlock(MetalMaterial material, boolean glows) {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.material = material;
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.oreDict = Oredicts.BLOCK + material.getCapitalizedName();
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		if (glows)
			this.setLightLevel(0.5f);
	}

	///// OVERRIDE OF ALL METHODS THAT DEPEND ON BLOCK MATERIAL: /////
	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public MapColor getMapColor(final IBlockState bs) {
		return MapColor.IRON;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullBlock(IBlockState bs) {
		return true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isNormalCube(IBlockState bs) {
		return true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullyOpaque(IBlockState bs) {
		return true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState bs) {
		return true;
	}

	@Override
	public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
		return false;
	}

	@Override
	public boolean isReplaceable(final IBlockAccess w, final BlockPos p) {
		return false;
	}

	@Override
	public boolean isNormalCube(final IBlockState bs, final IBlockAccess w, final BlockPos coord) {
		return true;
	}

	@Override
	public void onBlockPlacedBy(final World w, final BlockPos coord, final IBlockState bs,
			final EntityLivingBase placer, final ItemStack src) {
		super.onBlockPlacedBy(w, coord, bs, placer, src);
		// achievement
		if ((Options.enableAchievements) && (placer instanceof EntityPlayer)) {
				((EntityPlayer) placer).addStat(Achievements.blocktastic, 1);
		}
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
