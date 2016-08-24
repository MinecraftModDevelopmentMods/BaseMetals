package cyano.basemetals.blocks;

import cyano.basemetals.material.IMetalObject;
import cyano.basemetals.material.MetalMaterial;
import cyano.basemetals.registry.IOreDictionaryEntry;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Metal Plate
 * @author DrCyano
 *
 */
public class BlockMetalPlate extends Block implements IOreDictionaryEntry, IMetalObject {


	/**
	 * Blockstate property
	 */
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	private final MetalMaterial metal;
	private String oreDict;

	private static final float thickness = 1.0f / 16.0f;

	private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[EnumFacing.values().length];
	static {
		for(int i = 0; i < EnumFacing.values().length; i++) {
			EnumFacing orientation = EnumFacing.values()[i];
			float x1 = 0, x2 = 1, y1 = 0,y2 = 1, z1 = 0, z2 = 1;
			switch(orientation){
				case DOWN:
					y1 = 1f - thickness;
					break;
				case SOUTH:
					z2 = thickness;
					break;
				case NORTH:
					z1 = 1f - thickness;
					break;
				case EAST:
					x2 = thickness;
					break;
				case WEST:
					x1 = 1f - thickness;
					break;
				case UP:
				default:
					y2 = thickness;
					break;
			}
			BOXES[orientation.ordinal()] = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
		}
	}
	
	public BlockMetalPlate(MetalMaterial metal) {
		super(Material.IRON);
        this.blockSoundType = SoundType.METAL;
		this.metal = metal;
		this.oreDict = "plate" + metal.getCapitalizedName();
		this.blockHardness = metal.getMetalBlockHardness();
		this.blockResistance = metal.getBlastResistance();
		this.setHarvestLevel("pickaxe", metal.getRequiredHarvestLevel());
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING,EnumFacing.NORTH));
		this.useNeighborBrightness = true;
	}

	@Override
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState bs) {
		return false;
	}

	@Override
	public IBlockState onBlockPlaced(final World w, final BlockPos coord, final EnumFacing face,
									 final float partialX, final float partialY, final float partialZ,
									 final int i, final EntityLivingBase placer) {
		IBlockState defaultState = this.getDefaultState().withProperty(FACING, face);
		// redimension to face-local up and right dimensions
		float up, right;
		EnumFacing.Axis upRotationAxis, rightRotationAxis;
		switch(face){
			case UP: // works
				up = partialZ - 0.5F;
				right = partialX - 0.5F;
				upRotationAxis = EnumFacing.Axis.X;
				rightRotationAxis = EnumFacing.Axis.Z;
				break;
			case EAST: // works
				up = partialY - 0.5F;
				right = partialZ - 0.5F;
				upRotationAxis = EnumFacing.Axis.Z;
				rightRotationAxis = EnumFacing.Axis.Y;
				break;
			case SOUTH:
				up = 0.5F - partialY;
				right = 0.5F - partialX;
				upRotationAxis = EnumFacing.Axis.X;
				rightRotationAxis = EnumFacing.Axis.Y;
				break;
			case DOWN:
				up = 0.5F - partialZ;
				right = 0.5F - partialX;
				upRotationAxis = EnumFacing.Axis.X;
				rightRotationAxis = EnumFacing.Axis.Z;
				break;
			case WEST:
				up = 0.5F - partialY;
				right = 0.5F - partialZ;
				upRotationAxis = EnumFacing.Axis.Z;
				rightRotationAxis = EnumFacing.Axis.Y;
				break;
			case NORTH: // works
				up = partialY - 0.5F;
				right = partialX - 0.5F;
				upRotationAxis = EnumFacing.Axis.X;
				rightRotationAxis = EnumFacing.Axis.Y;
				break;
			default: return defaultState;
		}
		if(Math.abs(up) < 0.25F && Math.abs(right) < 0.25F) {
			// no rotation
			return defaultState;
		}
		boolean upOrRight = up + right > 0;
		boolean upOrLeft = up - right > 0;
		if(upOrRight) {
			// up or right
			if(upOrLeft) {
				// up
				return defaultState.withProperty(FACING,face.rotateAround(upRotationAxis));
			} else {
				// right
				return defaultState.withProperty(FACING,face.rotateAround(rightRotationAxis).getOpposite());
			}
		} else {
			// down or left
			if(upOrLeft) {
				// left
				return defaultState.withProperty(FACING,face.rotateAround(rightRotationAxis));
			} else {
				// down
				return defaultState.withProperty(FACING,face.rotateAround(upRotationAxis).getOpposite());
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(final IBlockState bs) {
		int i = (bs.getValue(FACING)).getIndex();
		return i;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState bs, final IBlockAccess world, final BlockPos coord) {
        final EnumFacing orientation =  bs.getValue(FACING);
		return BOXES[orientation.ordinal()];
	}

	@Override
	public void addCollisionBoxToList(final IBlockState bs, final World world, final BlockPos coord,
									  final AxisAlignedBB box, final List<AxisAlignedBB> collisionBoxList,
									  final Entity entity) {

		final EnumFacing orientation =  world.getBlockState(coord).getValue(FACING);
		addCollisionBoxToList(coord, box, collisionBoxList, BOXES[orientation.ordinal()]);
	}

	@Override
	public String getOreDictionaryName() {
		return oreDict;
	}

	@Override
	public MetalMaterial getMetalMaterial() {
		return metal;
	}
}
