package cyano.basemetals.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * This class represents a fluid that applies a function to any living entities that swim in it.
 * @author DrCyano
 *
 */
public class InteractiveFluidBlock extends BlockFluidClassic{

	private final java.util.function.BiConsumer<net.minecraft.world.World, net.minecraft.entity.EntityLivingBase> immersionEffect;
	private final boolean isFlammable;
	/**
	 * Constructor for this fluid block.
	 * @param fluid The Fluid of this fluid block
	 * @param flammable If true, then this block can burn
	 * @param immersionEffect A function to define what happens to swimming entities. Can be null.
	 */
	public InteractiveFluidBlock(Fluid fluid, boolean flammable, java.util.function.BiConsumer<net.minecraft.world.World, net.minecraft.entity.EntityLivingBase> immersionEffect) {
		super(fluid, Material.WATER);
		this.isFlammable = flammable;
		this.immersionEffect = immersionEffect;
	}
	/**
	 * Constructor for this fluid block.
	 * @param fluid The Fluid of this fluid block
	 * @param immersionEffect A function to define what happens to swimming entities. Can be null.
	 */
	public InteractiveFluidBlock(Fluid fluid,  java.util.function.BiConsumer<net.minecraft.world.World, net.minecraft.entity.EntityLivingBase> immersionEffect) {
		this(fluid,false,immersionEffect);
	}
	/**
	 * Constructor for this fluid block.
	 * @param fluid The Fluid of this fluid block
	 */
	public InteractiveFluidBlock(Fluid fluid) {
		this(fluid,false,null);
	}

	
	@Override
	public void onEntityCollidedWithBlock( World world, BlockPos coord, IBlockState state,Entity entity ) {
        super.onEntityCollidedWithBlock(world,coord,state,entity);
		if (immersionEffect != null && entity instanceof EntityLivingBase 
				) {
			immersionEffect.accept(world,(EntityLivingBase)entity);
		}
	}
	
	/**
	 * Chance that fire will spread and consume this block.
	 * 300 being a 100% chance, 0, being a 0% chance.
	 *
	 * @param world The current world
	 * @param pos Block position in world
	 * @param face The face that the fire is coming from
	 * @return A number ranging from 0 to 300 relating used to determine if the block will be consumed by fire
	 */
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if(isFlammable) return 60;
		return 0;
	}
	
	/**
	 * Called when fire is updating on a neighbor block.
	 * The higher the number returned, the faster fire will spread around this block.
	 *
	 * @param world The current world
	 * @param pos Block position in world
	 * @param face The face that the fire is coming from
	 * @return A number that is used to determine the speed of fire growth around the block
	 */
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face)
	{
		if(isFlammable) return 30;
		return 0;
	}

	// TODO: remove the block overrides and see if fluids are working correctly yet
	@Override // block override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, World worldIn, BlockPos pos){
		return NULL_AABB;
	}
}
