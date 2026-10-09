package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;

/** Uses the vanilla repair screen and keeps the same block ID as the anvil wears out. */
public final class BaseMetalAnvilBlock extends AnvilBlock {
    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 2);

    public BaseMetalAnvilBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(DAMAGE, Integer.valueOf(0)));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<net.minecraft.world.level.block.Block, BlockState> builder) {
        builder.add(FACING, DAMAGE);
    }

    /** Called by the anvil wear hook in AnvilBlock.damage. */
    public static boolean isBaseMetalAnvil(BlockState state) {
        return state != null && state.getBlock() instanceof BaseMetalAnvilBlock;
    }

    public static BlockState damageBaseMetalAnvil(BlockState state) {
        int damage = state.getValue(DAMAGE).intValue();
        return damage >= 2 ? null : state.setValue(DAMAGE, Integer.valueOf(damage + 1));
    }

    @Override
    public net.minecraft.world.MenuProvider getMenuProvider(
            BlockState state, Level world, BlockPos pos) {
        return new net.minecraft.world.SimpleMenuProvider((windowId, inventory, player) ->
                new AnvilMenu(windowId, inventory, net.minecraft.world.inventory.ContainerLevelAccess.create(world, pos)) {
                    @Override public boolean stillValid(Player candidate) {
                        return world.getBlockState(pos).getBlock() == BaseMetalAnvilBlock.this
                                && candidate.distanceToSqr(pos.getX() + 0.5D, pos.getY() + 0.5D,
                                        pos.getZ() + 0.5D) <= 64.0D;
                    }
                }, new TranslatableComponent(getDescriptionId()));
    }
}
