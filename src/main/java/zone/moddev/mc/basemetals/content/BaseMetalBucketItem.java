package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import javax.annotation.Nullable;


import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * A dedicated filled bucket which remains visible in both the Base Metals item
 * tab and vanilla's global search. Its interaction path consistently uses
 * Forge's deferred fluid supplier instead of vanilla's eagerly captured field.
 */
public final class BaseMetalBucketItem extends BucketItem {
    public BaseMetalBucketItem(Supplier<? extends Fluid> fluid, Properties properties) {
        super(fluid, properties);
    }

    @Override
    public InteractionResult use(Level level, net.minecraft.world.entity.player.Player player,
            InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        InteractionResult forgeResult = ForgeEventFactory.onBucketUse(player, level, held, hit);
        if (forgeResult != null) return forgeResult;
        if (hit.getType() != HitResult.Type.BLOCK) return InteractionResult.PASS;

        BlockPos clicked = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos adjacent = clicked.relative(direction);
        if (!level.mayInteract(player, clicked) || !player.mayUseItemAt(adjacent, direction, held)) {
            return InteractionResult.FAIL;
        }

        BlockState clickedState = level.getBlockState(clicked);
        BlockPos destination = canContainFluid(level, clicked, clickedState) ? clicked : adjacent;
        if (!emptyContents(player, level, destination, hit, held, hand)) {
            return InteractionResult.FAIL;
        }

        checkExtraContent(player, level, held, destination);
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, destination, held);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        level.gameEvent(player, GameEvent.FLUID_PLACE, destination);
        return InteractionResult.SUCCESS.heldItemTransformedTo(getEmptySuccessItem(held, player));
    }

    public boolean emptyContents(@Nullable net.minecraft.world.entity.player.Player player,
            Level level, BlockPos position, @Nullable BlockHitResult hit) {
        return emptyContents(player, level, position, hit, getDefaultInstance());
    }

    @Override
    public boolean emptyContents(@Nullable net.minecraft.world.entity.player.Player player,
            Level level, BlockPos position, @Nullable BlockHitResult hit, @Nullable ItemStack container) {
        return emptyContents(player, level, position, hit, container, InteractionHand.MAIN_HAND);
    }

    private boolean emptyContents(@Nullable net.minecraft.world.entity.player.Player player,
            Level level, BlockPos position, @Nullable BlockHitResult hit, @Nullable ItemStack container,
            InteractionHand hand) {
        ItemStack bucket = container == null ? getDefaultInstance() : container;
        FluidStack contents = new FluidStack(getFluid(), net.minecraftforge.fluids.FluidType.BUCKET_VOLUME);
        return FluidUtil.tryPlaceFluid(player, level, hand,
                position, new FluidBucketWrapper(bucket.copyWithCount(1)), contents);
    }

    private boolean canContainFluid(Level level, BlockPos position, BlockState state) {
        return state.getBlock() instanceof LiquidBlockContainer container
                && container.canPlaceLiquid(null, level, position, state, getFluid());
    }
}
