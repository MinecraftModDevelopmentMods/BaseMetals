package zone.moddev.mc.basemetals.content;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

/** A single registry ID retaining the 1.12 anvil damage states. */
public final class BaseMetalAnvilBlock extends AnvilBlock {
    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 2);

    public BaseMetalAnvilBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(DAMAGE, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(DAMAGE);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) ->
                new DurableAnvilMenu(containerId, inventory, level, pos),
                net.minecraft.network.chat.Component.translatable(getDescriptionId()));
    }

    private static final class DurableAnvilMenu extends AnvilMenu {
        private DurableAnvilMenu(int containerId, Inventory inventory, Level level, BlockPos pos) {
            super(containerId, inventory, ContainerLevelAccess.create(level, pos));
        }

        @Override
        protected boolean isValidBlock(BlockState state) {
            return state.getBlock() instanceof BaseMetalAnvilBlock;
        }

    }
}
