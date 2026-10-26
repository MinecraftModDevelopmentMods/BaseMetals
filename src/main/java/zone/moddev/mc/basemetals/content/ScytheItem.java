package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.ModTags;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import java.util.List;
import javax.annotation.Nullable;

public final class ScytheItem extends Item implements MaterialBacked {
    /**
     * The nested calls use the ordinary server break path so every affected
     * block still fires Forge's protection event and receives vanilla drops.
     * The guard lets those nested calls complete without recursively starting
     * another 3x3 harvest.
     */
    private static final ThreadLocal<Boolean> HARVESTING = ThreadLocal.withInitial(() -> false);
    private final MaterialDefinition material;

    public ScytheItem(MaterialDefinition material, Properties properties) {
        super(properties.tool(new MaterialTier(material).toolMaterial(),
                ModTags.SCYTHE_HARVESTABLE, 0.0F, 0.0F, 0.0F));
        this.material = material;
    }

    @Override public MaterialDefinition baseMetalsMaterial() { return material; }

    @Override
    public boolean onBlockStartBreak(ItemStack stack, BlockPos position, Player player) {
        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)
                || HARVESTING.get()) {
            return false;
        }
        if (!player.level().getBlockState(position).is(ModTags.SCYTHE_HARVESTABLE)) {
            return false;
        }

        HARVESTING.set(true);
        try {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    BlockPos target = position.offset(dx, 0, dz);
                    if (player.level().getBlockState(target).is(ModTags.SCYTHE_HARVESTABLE)) {
                        serverPlayer.gameMode.destroyBlock(target);
                    }
                }
            }
        } finally {
            HARVESTING.set(false);
        }
        // The centre was included above; prevent the outer break from paying
        // drops and durability a second time.
        return true;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
            TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        MaterialItems.addToolTooltip(material, tooltip);
    }
}
