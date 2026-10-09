package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

/** Keeps old double slabs intact when a 1.10 or 1.12 world is upgraded. */
public final class CompatibilityDoubleSlabBlock extends SlabBlock {
    private final int harvestLevel;

    public CompatibilityDoubleSlabBlock(Properties properties, int harvestLevel) {
        super(properties);
        this.harvestLevel = harvestLevel;
        registerDefaultState(defaultBlockState().setValue(TYPE, SlabType.DOUBLE).setValue(WATERLOGGED, Boolean.FALSE));
    }
}
