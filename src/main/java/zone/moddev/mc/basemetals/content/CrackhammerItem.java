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
                properties.defaultMaxDamage(material.crackhammerDurability())
                        .addToolType(ToolType.PICKAXE, material.toolLevel()));
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.isIn(ModTags.CRACKHAMMER_CRUSHABLE) && canHarvestBlock(state)
                ? material.crackhammerDestroySpeed() : 1.0F;
    }

    @Override public boolean canHarvestBlock(BlockState state) {
        if (state.getHarvestTool() == ToolType.PICKAXE) return material.toolLevel() >= state.getHarvestLevel();
        Material blockMaterial = state.getMaterial();
        return blockMaterial == Material.ROCK || blockMaterial == Material.IRON
                || blockMaterial == Material.ANVIL;
    }

    @Override public void addInformation(ItemStack stack, @Nullable World world,
            List<ITextComponent> tooltip, ITooltipFlag flag) { MaterialItems.addToolTooltip(material, tooltip); }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (context.getFace() != Direction.UP || world.isRemote || player == null) return ActionResultType.PASS;

        AxisAlignedBB area = new AxisAlignedBB(context.getPos().up());
        List<ItemEntity> entities = world.getEntitiesWithinAABB(ItemEntity.class, area);
        for (ItemEntity entity : entities) {
            ItemStack input = entity.getItem();
            CrushingRecipe recipe = findRecipe(world, input);
            if (recipe == null || !canCrushDroppedBlock(input)) continue;
            int requested = player.isSneaking() ? input.getCount() : 1;
            ItemStack hammer = context.getItem();
            int durability = hammer.isDamageable()
                    ? Math.max(0, hammer.getMaxDamage() - hammer.getDamage()) : requested;
            int operations = Math.min(requested, durability);
            if (operations <= 0) break;
            ItemStack output = recipe.getRecipeOutput().copy();
            input.shrink(operations);
            if (input.isEmpty()) entity.remove(); else entity.setItem(input);
            spawnOutputs(world, entity, output, operations);
            hammer.damageItem(operations, player, holder -> holder.sendBreakAnimation(context.getHand()));
            world.playSound(null, context.getPos(), net.minecraft.util.SoundEvents.BLOCK_GRAVEL_BREAK,
                    SoundCategory.BLOCKS, 0.5F, 0.5F + world.rand.nextFloat() * 0.3F);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Nullable
    private static CrushingRecipe findRecipe(World world, ItemStack input) {
        Inventory inventory = new Inventory(1);
        inventory.setInventorySlotContents(0, input);
        for (net.minecraft.item.crafting.IRecipe candidate : world.getRecipeManager().getRecipes()) {
            if (candidate instanceof CrushingRecipe && candidate.matches(inventory, world)) {
                return (CrushingRecipe) candidate;
            }
        }
        return null;
    }

    private boolean canCrushDroppedBlock(ItemStack input) {
        if (!(input.getItem() instanceof BlockItem)) return true;
        return canHarvestBlock(((BlockItem) input.getItem()).getBlock().getDefaultState());
    }

    private static void spawnOutputs(World world, ItemEntity source, ItemStack result, int operations) {
        int remaining = result.getCount() * operations;
        while (remaining > 0) {
            ItemStack output = result.copy();
            output.setCount(Math.min(output.getMaxStackSize(), remaining));
            remaining -= output.getCount();
            ItemEntity crushed = new ItemEntity(world, source.getPosX(), source.getPosY(), source.getPosZ(), output);
            crushed.setDefaultPickupDelay();
            world.addEntity(crushed);
        }
    }
}
