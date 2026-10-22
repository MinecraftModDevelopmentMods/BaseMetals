package zone.moddev.mc.basemetals.content;

import java.util.List;
import java.util.Optional;

import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.ItemAbilities;

public final class CrackhammerItem extends DiggerItem implements MaterialBacked {
    private final MaterialDefinition material;

    public CrackhammerItem(MaterialDefinition material, Properties properties) {
        super(new MaterialTier(material),
                BlockTags.MINEABLE_WITH_PICKAXE,
                properties
                        .durability(material.crackhammerDurability())
                        .attributes(DiggerItem.createAttributes(
                                new MaterialTier(material),
                                5.0F + material.baseAttackDamage(),
                                -3.5F)));
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(zone.moddev.mc.basemetals.ModTags.CRACKHAMMER_CRUSHABLE)
                && canHarvest(stack, state)
                ? material.crackhammerDestroySpeed()
                : 1.0F;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility action) {
        return ItemAbilities.DEFAULT_PICKAXE_ACTIONS.contains(action);
    }

@Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
            List<net.minecraft.network.chat.Component> tooltip,
            net.minecraft.world.item.TooltipFlag flag) {
        MaterialItems.addToolTooltip(material, tooltip);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP || context.getLevel().isClientSide
                || context.getPlayer() == null) {
            return InteractionResult.PASS;
        }
        AABB area = new AABB(context.getClickedPos().above());
        List<ItemEntity> entities = context.getLevel().getEntitiesOfClass(ItemEntity.class, area,
                entity -> entity.isAlive() && !entity.getItem().isEmpty());
        for (ItemEntity entity : entities) {
            ItemStack input = entity.getItem();
            Optional<CrushingRecipe> recipe = context.getLevel().getRecipeManager().getRecipeFor(
                    CrushingRecipe.TYPE.get(), new SimpleContainer(input), context.getLevel());
            if (recipe.isEmpty() || !canCrushDroppedBlock(context.getItemInHand(), input)) continue;
            // Sneaking is the per-use full-stack control; ordinary use crushes
            // one item and never changes a persistent config contract.
            int requested = context.getPlayer().isShiftKeyDown() ? input.getCount() : 1;
            ItemStack hammer = context.getItemInHand();
            int durability = hammer.isDamageableItem()
                    ? Math.max(0, hammer.getMaxDamage() - hammer.getDamageValue())
                    : requested;
            int operations = Math.min(requested, durability);
            if (operations == 0) break;
            ItemStack output = recipe.get().getResultItem(net.minecraft.core.RegistryAccess.EMPTY).copy();
            input.shrink(operations);
            if (input.isEmpty()) entity.discard(); else entity.setItem(input);
            spawnOutputs(context.getLevel(), entity, output, operations);
            hammer.hurtAndBreak(operations, context.getPlayer(),
                    player -> player.broadcastBreakEvent(context.getHand()));
            context.getLevel().playSound(null, context.getClickedPos(),
                    net.minecraft.sounds.SoundEvents.GRAVEL_BREAK,
                    net.minecraft.sounds.SoundSource.BLOCKS, 0.5F,
                    0.5F + (context.getLevel().random.nextFloat() * 0.3F));
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    private static boolean canCrushDroppedBlock(ItemStack hammer, ItemStack input) {
        if (!(input.getItem() instanceof BlockItem blockItem)) return true;
        return canHarvest(hammer, blockItem.getBlock().defaultBlockState());
    }

    private static boolean canHarvest(ItemStack hammer, BlockState state) {
        return !state.requiresCorrectToolForDrops() || hammer.isCorrectToolForDrops(state);
    }

    private static void spawnOutputs(Level level, ItemEntity source, ItemStack result, int operations) {
        int remaining = Math.multiplyExact(result.getCount(), operations);
        while (remaining > 0) {
            ItemStack output = result.copy();
            output.setCount(Math.min(output.getMaxStackSize(), remaining));
            remaining -= output.getCount();
            ItemEntity crushed = new ItemEntity(level, source.getX(), source.getY(), source.getZ(), output);
            crushed.setDefaultPickUpDelay();
            level.addFreshEntity(crushed);
        }
    }
}
