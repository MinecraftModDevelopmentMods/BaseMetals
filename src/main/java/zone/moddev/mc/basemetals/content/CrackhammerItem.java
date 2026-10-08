package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.ModTags;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ItemUseContext;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

public final class CrackhammerItem extends ToolItem implements MaterialBacked {
    private final MaterialDefinition material;

    public CrackhammerItem(MaterialDefinition material, Item.Properties properties) {
        super(material.crackhammerAttackDamage() - material.baseAttackDamage(), -3.5F,
                new MaterialTier(material), Collections.<Block>emptySet(),
                properties.defaultDurability(material.crackhammerDurability())
                        .addToolType(ToolType.PICKAXE, material.toolLevel()));
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(ModTags.CRACKHAMMER_CRUSHABLE) && isCorrectToolForDrops(state)
                ? material.crackhammerDestroySpeed() : 1.0F;
    }

    @Override public boolean isCorrectToolForDrops(BlockState state) {
        if (state.getHarvestTool() == ToolType.PICKAXE) return material.toolLevel() >= state.getHarvestLevel();
        Material blockMaterial = state.getMaterial();
        return blockMaterial == Material.STONE || blockMaterial == Material.METAL
                || blockMaterial == Material.HEAVY_METAL;
    }

    @Override public void appendHoverText(ItemStack stack, @Nullable World world,
            List<ITextComponent> tooltip, ITooltipFlag flag) { MaterialItems.addToolTooltip(material, tooltip); }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World world = context.getLevel();
        PlayerEntity player = context.getPlayer();
        if (context.getClickedFace() != Direction.UP || world.isClientSide || player == null) return ActionResultType.PASS;

        AxisAlignedBB area = new AxisAlignedBB(context.getClickedPos().above());
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
            if (input.isEmpty()) entity.remove(); else entity.setItem(input);
            spawnOutputs(world, entity, output, operations);
            hammer.hurtAndBreak(operations, player, holder -> holder.broadcastBreakEvent(context.getHand()));
            world.playSound(null, context.getClickedPos(), net.minecraft.util.SoundEvents.GRAVEL_BREAK,
                    SoundCategory.BLOCKS, 0.5F, 0.5F + world.random.nextFloat() * 0.3F);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    private static CrushingRecipe findRecipe(World world, ItemStack input) {
        Inventory inventory = new Inventory(1);
        inventory.setItem(0, input);
        for (net.minecraft.item.crafting.IRecipe candidate : world.getRecipeManager().getRecipes()) {
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

    private static void spawnOutputs(World world, ItemEntity source, ItemStack result, int operations) {
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
