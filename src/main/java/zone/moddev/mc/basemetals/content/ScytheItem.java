package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.ModTags;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public final class ScytheItem extends DiggerItem implements MaterialBacked {
    private static final ThreadLocal<Boolean> HARVESTING = new ThreadLocal<Boolean>() {
        @Override protected Boolean initialValue() { return Boolean.FALSE; }
    };
    private final MaterialDefinition material;

    public ScytheItem(MaterialDefinition material, Item.Properties properties) {
        super(0.0F, 0.0F, new MaterialTier(material), ModTags.SCYTHE_HARVESTABLE, properties);
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }
    @Override public float getDestroySpeed(ItemStack stack, BlockState state) {
        return state.is(ModTags.SCYTHE_HARVESTABLE) ? material.toolEfficiency() : 1.0F;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos position, Player player) {
        if (player.level.isClientSide || !(player instanceof ServerPlayer) || HARVESTING.get()) return false;
        if (!player.level.getBlockState(position).is(ModTags.SCYTHE_HARVESTABLE)) return false;
        HARVESTING.set(Boolean.TRUE);
        try {
            ServerPlayer serverPlayer = (ServerPlayer) player;
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

    @Override public void appendHoverText(ItemStack stack, @Nullable Level world,
            List<Component> tooltip, TooltipFlag flag) { MaterialItems.addToolTooltip(material, tooltip); }
}
