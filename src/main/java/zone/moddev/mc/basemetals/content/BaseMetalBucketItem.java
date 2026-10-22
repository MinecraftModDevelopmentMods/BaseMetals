package zone.moddev.mc.basemetals.content;

import java.util.function.Supplier;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * A dedicated filled bucket which remains visible in both the Base Metals item
 * tab and vanilla's global search, with fluid placement through NeoForge's
 * fluid handler API.
 */
@EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class BaseMetalBucketItem extends BucketItem {
    public BaseMetalBucketItem(Supplier<? extends Fluid> fluid, Properties properties) {
        super(fluid.get(), properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, net.minecraft.world.entity.player.Player player,
            InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        BlockHitResult hit = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hit.getType() != HitResult.Type.BLOCK) return InteractionResultHolder.pass(held);

        BlockPos clicked = hit.getBlockPos();
        Direction direction = hit.getDirection();
        BlockPos adjacent = clicked.relative(direction);
        if (!level.mayInteract(player, clicked) || !player.mayUseItemAt(adjacent, direction, held)) {
            return InteractionResultHolder.fail(held);
        }

        BlockState clickedState = level.getBlockState(clicked);
        BlockPos destination = canBlockContainFluid(player, level, clicked, clickedState) ? clicked : adjacent;
        if (!emptyContents(player, level, destination, hit, held, hand)) {
            return InteractionResultHolder.fail(held);
        }

        checkExtraContent(player, level, held, destination);
        if (player instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, destination, held);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        level.gameEvent(player, GameEvent.FLUID_PLACE, destination);
        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(held, player), level.isClientSide());
    }

    @Override
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
        FluidStack contents = new FluidStack(content, FluidType.BUCKET_VOLUME);
        return FluidUtil.tryPlaceFluid(player, level, hand,
                position, bucket, contents).isSuccess();
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        ModContent.fluids().values().forEach(fluid -> event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, context) -> new FluidBucketWrapper(stack),
                fluid.bucket().get()));
    }
}
