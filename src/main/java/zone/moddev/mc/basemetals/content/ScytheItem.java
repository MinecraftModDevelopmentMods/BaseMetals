package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.ModTags;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public final class ScytheItem extends ToolItem implements MaterialBacked {
    private static final ThreadLocal<Boolean> HARVESTING = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() { return Boolean.FALSE; }
    };
    private final MaterialDefinition material;

    public ScytheItem(MaterialDefinition material, Item.Properties properties) {
        super(0.0F, 0.0F, new MaterialTier(material), Collections.<Block>emptySet(), properties);
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }
    @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(ModTags.SCYTHE_HARVESTABLE) ? material.toolEfficiency() : 1.0F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos position, PlayerEntity player) {
        if (player.level.isClientSide || !(player instanceof ServerPlayerEntity) || HARVESTING.get()) return false;
        if (!player.level.getBlockState(position).is(ModTags.SCYTHE_HARVESTABLE)) return false;
        HARVESTING.set(Boolean.TRUE);
        try {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos target = position.offset(dx, 0, dz);
                    if (player.level.getBlockState(target).is(ModTags.SCYTHE_HARVESTABLE)) {
                        serverPlayer.gameMode.destroyBlock(target);
                    }
                }
            }
        } finally {
            HARVESTING.set(Boolean.FALSE);
        }
        return true;
    }

    @Override public void appendHoverText(ItemStack stack, @Nullable World world,
            List<ITextComponent> tooltip, ITooltipFlag flag) { MaterialItems.addToolTooltip(material, tooltip); }
}
