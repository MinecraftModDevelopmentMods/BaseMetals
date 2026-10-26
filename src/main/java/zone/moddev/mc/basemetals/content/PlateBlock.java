package zone.moddev.mc.basemetals.content;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.Mth;

public final class PlateBlock extends Block {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
    private static final VoxelShape[] SHAPES = {
            box(0, 15, 0, 16, 16, 16), box(0, 0, 0, 16, 1, 16),
            box(0, 0, 15, 16, 16, 16), box(0, 0, 0, 16, 16, 1),
            box(15, 0, 0, 16, 16, 16), box(0, 0, 0, 1, 16, 16)};

    public PlateBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction face = context.getClickedFace();
        Vec3 click = context.getClickLocation();
        float x = (float) (click.x - Mth.floor(click.x));
        float y = (float) (click.y - Mth.floor(click.y));
        float z = (float) (click.z - Mth.floor(click.z));
        float up;
        float right;
        Direction.Axis upAxis;
        Direction.Axis rightAxis;
        switch (face) {
            case UP -> { up = z - 0.5F; right = x - 0.5F; upAxis = Direction.Axis.X; rightAxis = Direction.Axis.Z; }
            case EAST -> { up = y - 0.5F; right = z - 0.5F; upAxis = Direction.Axis.Z; rightAxis = Direction.Axis.Y; }
            case SOUTH -> { up = 0.5F - y; right = 0.5F - x; upAxis = Direction.Axis.X; rightAxis = Direction.Axis.Y; }
            case DOWN -> { up = 0.5F - z; right = 0.5F - x; upAxis = Direction.Axis.X; rightAxis = Direction.Axis.Z; }
            case WEST -> { up = 0.5F - y; right = 0.5F - z; upAxis = Direction.Axis.Z; rightAxis = Direction.Axis.Y; }
            case NORTH -> { up = y - 0.5F; right = x - 0.5F; upAxis = Direction.Axis.X; rightAxis = Direction.Axis.Y; }
            default -> throw new IllegalStateException("Unhandled face " + face);
        }
        Direction facing = face;
        if (Math.abs(up) >= 0.25F || Math.abs(right) >= 0.25F) {
            boolean upOrRight = up + right > 0;
            boolean upOrLeft = up - right > 0;
            if (upOrRight) {
                facing = upOrLeft ? face.getClockWise(upAxis)
                        : face.getClockWise(rightAxis).getOpposite();
            } else {
                facing = upOrLeft ? face.getClockWise(rightAxis)
                        : face.getClockWise(upAxis).getOpposite();
            }
        }
        return defaultBlockState().setValue(FACING, facing);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(FACING).get3DDataValue()];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
