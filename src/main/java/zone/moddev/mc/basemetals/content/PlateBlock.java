package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.BlockGetter;

/** A one-pixel-thick plate attached flush to the selected block face. */
public final class PlateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape[] SHAPES = {
            box(0, 15, 0, 16, 16, 16), box(0, 0, 0, 16, 1, 16),
            box(0, 0, 15, 16, 16, 16), box(0, 0, 0, 16, 16, 1),
            box(15, 0, 0, 16, 16, 16), box(0, 0, 0, 1, 16, 16)};
    private final int harvestLevel;

    public PlateBlock(Properties properties, int harvestLevel) {
        super(properties);
        this.harvestLevel = harvestLevel;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getClickedFace());
    }

    @Override public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos,
            net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPES[state.getValue(FACING).get3DDataValue()];
    }
    @Override public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }
    @Override public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
