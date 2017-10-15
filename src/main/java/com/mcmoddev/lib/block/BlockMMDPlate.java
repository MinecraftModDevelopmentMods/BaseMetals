package com.mcmoddev.lib.block;

import java.util.List;

import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.block.properties.IProperty;
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

/**
 * Metal Plate
 * 
 * @author DrCyano
 *
 */
public class BlockMMDPlate extends net.minecraft.block.Block implements IMMDObject {

	/**
	 * Blockstate property
	 */
	public static final PropertyDirection FACING = PropertyDirection.create("facing");

	private final MMDMaterial material;

	private static final float THICKNESS = 1.0f / 16.0f;

	private static final AxisAlignedBB[] BOXES = new AxisAlignedBB[EnumFacing.values().length];

	/**
	 *
	 * @param material
	 *            The Material the plate is made from
	 */
	public BlockMMDPlate(MMDMaterial material) {
		super(material.getVanillaMaterial());
		this.material = material;
		this.blockSoundType = this.material.getSoundType();
		this.blockHardness = this.material.getBlockHardness();
		this.blockResistance = this.material.getBlastResistance();
		this.setHarvestLevel("pickaxe", this.material.getRequiredHarvestLevel());
		this.setDefaultState(this.blockState.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH));
		this.useNeighborBrightness = true;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	static {
		for (int i = 0; i < EnumFacing.values().length; i++) {
			final EnumFacing orientation = EnumFacing.values()[i];
			float x1 = 0;
			float x2 = 1;
			float y1 = 0;
			float y2 = 1;
			float z1 = 0;
			float z2 = 1;
			switch (orientation) {
				case DOWN:
					y1 = 1f - THICKNESS;
					break;
				case SOUTH:
					z2 = THICKNESS;
					break;
				case NORTH:
					z1 = 1f - THICKNESS;
					break;
				case EAST:
					x2 = THICKNESS;
					break;
				case WEST:
					x1 = 1f - THICKNESS;
					break;
				case UP:
				default:
					y2 = THICKNESS;
					break;
			}
			BOXES[orientation.ordinal()] = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
		}
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState bs) {
		return false;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public boolean isFullCube(IBlockState bs) {
		return false;
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public IBlockState getStateForPlacement(final World w, final BlockPos coord, final EnumFacing face,
									 final float partialX, final float partialY, final float partialZ,
									 final int i, final EntityLivingBase placer) {
		final IBlockState defaultState = this.getDefaultState().withProperty(FACING, face);
		// redimension to face-local up and right dimensions
		float up;
		float right;
		EnumFacing.Axis upRotationAxis;
		EnumFacing.Axis rightRotationAxis;
		switch (face) {
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
			default:
				return defaultState;
		}
		if ((Math.abs(up) < 0.25F) && (Math.abs(right) < 0.25F))
			// no rotation
			return defaultState;
		final boolean upOrRight = (up + right) > 0;
		final boolean upOrLeft = (up - right) > 0;
		if (upOrRight) {
			// up or right
			if (upOrLeft) {
				// up
				return defaultState.withProperty(FACING, face.rotateAround(upRotationAxis));
			} else {
				// right
				return defaultState.withProperty(FACING, face.rotateAround(rightRotationAxis).getOpposite());
			}
		} else {
			// down or left
			if (upOrLeft) {
				// left
				return defaultState.withProperty(FACING, face.rotateAround(rightRotationAxis));
			} else {
				// down
				return defaultState.withProperty(FACING, face.rotateAround(upRotationAxis).getOpposite());
			}
		}
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public IBlockState getStateFromMeta(final int meta) {
		return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
	}

	@Override
	public int getMetaFromState(final IBlockState bs) {
		return (bs.getValue(FACING)).getIndex();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING });
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(final IBlockState bs, final IBlockAccess world, final BlockPos coord) {
		final EnumFacing orientation = bs.getValue(FACING);
		return BOXES[orientation.ordinal()];
	}

	/**
	 * @deprecated
	 */
	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox,
			List<AxisAlignedBB> collisionBoxList, Entity entityIn, boolean p_185477_7_) {
		final EnumFacing orientation = world.getBlockState(pos).getValue(FACING);
		addCollisionBoxToList(pos, entityBox, collisionBoxList, BOXES[orientation.ordinal()]);
	}
	
	@Override
	public MMDMaterial getMMDMaterial() {
		return this.material;
	}
}
