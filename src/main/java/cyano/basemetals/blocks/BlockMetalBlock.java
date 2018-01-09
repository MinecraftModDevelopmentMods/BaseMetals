package cyano.basemetals.blocks;

import cyano.basemetals.init.Achievements;
import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
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

public class BlockMetalBlock extends Block implements IOreDictionaryEntry, IMetalObject{

	final MetalMaterial metal;
	
	public BlockMetalBlock(MetalMaterial metal){
		this(metal,false);
	}
	public BlockMetalBlock(MetalMaterial metal, boolean glows) {
		super(Material.IRON);
		this.setSoundType(SoundType.METAL);
		this.fullBlock = true;
		this.lightOpacity = 255;
		this.translucent = false;
		this.metal = metal;
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		if(glows) this.setLightLevel(0.5f);
	}

	///// OVERRIDE OF ALL METHODS THAT DEPEND ON BLOCK MATERIAL: /////
	@Override
	public MapColor getMapColor(final IBlockState p_getMapColor_1_) {
        return MapColor.IRON;
    }
	
	@Override
	public boolean isFullBlock(IBlockState bs) {
        return true;
    }
    
	@Override
	public boolean isNormalCube(IBlockState bs) {
        return true;
    }
    
	@Override
	public boolean isVisuallyOpaque() {
        return true;
    }
    
	@Override
	public boolean isFullCube(IBlockState bs) {
        return true;
    }
    
	@Override
	public boolean isPassable(final IBlockAccess p_isPassable_1_, final BlockPos p_isPassable_2_) {
        return false;
    }
    
	@Override
	public boolean isReplaceable(final IBlockAccess w, final BlockPos p) {
        return false;
    }
    
	@Override
	public boolean isNormalCube(final IBlockState bs, final IBlockAccess w, final BlockPos coord) {
        return this.isNormalCube(bs);
    }

	@Override
	public String getOreDictionaryName() {
		return "block"+metal.getCapitalizedName();
	}

	@Override
	public void onBlockPlacedBy(final World w, final BlockPos coord, final IBlockState bs, final EntityLivingBase placer, final ItemStack src) {
		super.onBlockPlacedBy(w, coord, bs, placer, src);
    	// achievement
		if(placer instanceof EntityPlayer){
			((EntityPlayer)placer).addStat(Achievements.blocktastic, 1);
		}
	}

	@Override
	public MetalMaterial getMetalMaterial(){
		return metal;
	}
}
