package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.ModTags;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.InteractionResult;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.ToolAction;

public final class CrackhammerItem extends DiggerItem implements MaterialBacked {
    private final MaterialDefinition material;

    public CrackhammerItem(MaterialDefinition material, Item.Properties properties) {
        super(material.crackhammerAttackDamage() - material.baseAttackDamage(), -3.5F,
                new MaterialTier(material), BlockTags.MINEABLE_WITH_PICKAXE,
                properties.defaultDurability(material.crackhammerDurability()));
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(ModTags.CRACKHAMMER_CRUSHABLE) && isCorrectToolForDrops(state)
                ? material.crackhammerDestroySpeed() : 1.0F;
    }

    @Override public boolean canPerformAction(ItemStack stack, ToolAction action) {
        return action == ToolActions.PICKAXE_DIG;
    }

    @Override public void appendHoverText(ItemStack stack, @Nullable Level world,
            List<Component> tooltip, TooltipFlag flag) { MaterialItems.addToolTooltip(material, tooltip); }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        Player player = context.getPlayer();
        if (context.getClickedFace() != Direction.UP || world.isClientSide || player == null) return InteractionResult.PASS;

        AABB area = new AABB(context.getClickedPos().above());
        List<ItemEntity> entities = world.getEntitiesOfClass(ItemEntity.class, area);
        for (ItemEntity entity : entities) {
            ItemStack input = entity.getItem();
            CrushingRecipe recipe = findRecipe(world, input);
            if (recipe == null || !canCrushDroppedBlock(input)) continue;
            int requested = player.isShiftKeyDown() ? input.getCount() : 1;
            ItemStack hammer = context.getItemInHand();
            int durability = hammer.isDamageableItem()
                    ? Math.max(0, hammer.getMaxDamage() - hammer.getDamageValue()) : requested;
            int operations = Math.min(requested, durability);
            if (operations <= 0) break;
            ItemStack output = recipe.getResultItem().copy();
            input.shrink(operations);
            if (input.isEmpty()) entity.discard(); else entity.setItem(input);
            spawnOutputs(world, entity, output, operations);
            hammer.hurtAndBreak(operations, player, holder -> holder.broadcastBreakEvent(context.getHand()));
            world.playSound(null, context.getClickedPos(), net.minecraft.sounds.SoundEvents.GRAVEL_BREAK,
                    SoundSource.BLOCKS, 0.5F, 0.5F + world.random.nextFloat() * 0.3F);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Nullable
    private static CrushingRecipe findRecipe(Level world, ItemStack input) {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, input);
        for (net.minecraft.world.item.crafting.Recipe candidate : world.getRecipeManager().getRecipes()) {
            if (candidate instanceof CrushingRecipe && candidate.matches(inventory, world)) {
                return (CrushingRecipe) candidate;
            }
        }
        return null;
    }

    private boolean canCrushDroppedBlock(ItemStack input) {
        if (!(input.getItem() instanceof BlockItem)) return true;
        return isCorrectToolForDrops(((BlockItem) input.getItem()).getBlock().defaultBlockState());
    }

    private static void spawnOutputs(Level world, ItemEntity source, ItemStack result, int operations) {
        int remaining = result.getCount() * operations;
        while (remaining > 0) {
            ItemStack output = result.copy();
            output.setCount(Math.min(output.getMaxStackSize(), remaining));
            remaining -= output.getCount();
            ItemEntity crushed = new ItemEntity(world, source.getX(), source.getY(), source.getZ(), output);
            crushed.setDefaultPickUpDelay();
            world.addFreshEntity(crushed);
        }
    }
}
