package zone.moddev.mc.basemetals.content;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.RepairContainer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/** Uses the vanilla repair screen and keeps the same block ID as the anvil wears out. */
public final class BaseMetalAnvilBlock extends AnvilBlock {
    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 2);

    public BaseMetalAnvilBlock(Properties properties) {
        super(properties);
        setDefaultState(stateContainer.getBaseState().with(FACING, Direction.NORTH)
                .with(DAMAGE, Integer.valueOf(0)));
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(FACING, context.getPlacementHorizontalFacing().rotateY());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<net.minecraft.block.Block, BlockState> builder) {
        builder.add(FACING, DAMAGE);
    }

    /** Entry points used by the Forge 28 compatibility transformer in AnvilBlock.damage. */
    public static boolean isBaseMetalAnvil(BlockState state) {
        return state != null && state.getBlock() instanceof BaseMetalAnvilBlock;
    }

    public static BlockState damageBaseMetalAnvil(BlockState state) {
        int damage = state.get(DAMAGE).intValue();
        return damage >= 2 ? null : state.with(DAMAGE, Integer.valueOf(damage + 1));
    }

    @Override
    public net.minecraft.inventory.container.INamedContainerProvider getContainer(
            BlockState state, World world, BlockPos pos) {
        return new net.minecraft.inventory.container.SimpleNamedContainerProvider((windowId, inventory, player) ->
                new RepairContainer(windowId, inventory, net.minecraft.util.IWorldPosCallable.of(world, pos)) {
                    @Override public boolean canInteractWith(PlayerEntity candidate) {
                        return world.getBlockState(pos).getBlock() == BaseMetalAnvilBlock.this
                                && candidate.getDistanceSq(pos.getX() + 0.5D, pos.getY() + 0.5D,
                                        pos.getZ() + 0.5D) <= 64.0D;
                    }
                }, new TranslationTextComponent(getTranslationKey()));
    }
}
