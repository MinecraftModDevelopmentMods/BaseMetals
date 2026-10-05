package zone.moddev.mc.basemetals.content;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

/** A one-pixel-thick plate attached flush to the selected block face. */
public final class PlateBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape[] SHAPES = {
            makeCuboidShape(0, 15, 0, 16, 16, 16), makeCuboidShape(0, 0, 0, 16, 1, 16),
            makeCuboidShape(0, 0, 15, 16, 16, 16), makeCuboidShape(0, 0, 0, 16, 16, 1),
            makeCuboidShape(15, 0, 0, 16, 16, 16), makeCuboidShape(0, 0, 0, 1, 16, 16)};
    private final int harvestLevel;

    public PlateBlock(Properties properties, int harvestLevel) {
        super(properties);
        this.harvestLevel = harvestLevel;
        setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getFace());
    }

    @Override public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos,
            net.minecraft.util.math.shapes.ISelectionContext context) {
        return SHAPES[state.get(FACING).getIndex()];
    }
    @Override public BlockState rotate(BlockState state, Rotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }
    @Override public BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.toRotation(state.get(FACING)));
    }
    @Override protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    @Override public ToolType getHarvestTool(BlockState state) { return ToolType.PICKAXE; }
    @Override public int getHarvestLevel(BlockState state) { return harvestLevel; }
}
