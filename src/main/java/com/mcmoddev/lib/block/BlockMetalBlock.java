package com.mcmoddev.lib.block;

import com.mcmoddev.basemetals.init.Achievements;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.material.IMetalObject;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.registry.IOreDictionaryEntry;
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
	private final boolean beaconBase;

	/**
	 *
	 * @param material
	 *            The material the block is made from
	 */
	public BlockMetalBlock(MetalMaterial material) {
		this(material, false);
	}

	public BlockMetalBlock(MetalMaterial material, boolean glows) {
		this(material, glows, false);
	}

	public BlockMetalBlock(MetalMaterial material, boolean glows, boolean isBeacon) {
		super(getMaterialFromMetalMaterial(material));

		this.setSoundType(getSoundFromMetalMaterial(material));
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.material = material;
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.oreDict = Oredicts.BLOCK + material.getCapitalizedName();
		this.blockHardness = material.getMetalBlockHardness();
		this.blockResistance = material.getBlastResistance();
		this.setHarvestLevel("pickaxe", material.getRequiredHarvestLevel());
		this.beaconBase = isBeacon;

		if (glows)
			this.setLightLevel(0.5f);
	}

	private static final Material getMaterialFromMetalMaterial(MetalMaterial material) {
		switch (material.getType()) {
			case METAL:
				return Material.IRON;
			case GEM:
			case ROCK:
				return Material.ROCK;
			case MINERAL:
				return Material.GRASS;
			case WOOD:
				return Material.WOOD;
			default:
				return Material.GROUND;
		}
	}

	private static final SoundType getSoundFromMetalMaterial(MetalMaterial material) {
		switch (material.getType()) {
			case METAL:
				return SoundType.METAL;
			case GEM:
				return SoundType.GLASS;
			case ROCK:
				return SoundType.STONE;
			case MINERAL:
				return SoundType.SAND;
			case WOOD:
				return SoundType.WOOD;
			default:
				return SoundType.GROUND;
		}
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return beaconBase;
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
