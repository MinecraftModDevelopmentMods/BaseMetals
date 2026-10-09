package zone.moddev.mc.basemetals.content;

import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.Item;

public final class FluidContent {
    private final RegistryHandle<FlowingFluid> source;
    private final RegistryHandle<FlowingFluid> flowing;
    private final RegistryHandle<LiquidBlock> block;
    private final RegistryHandle<Item> bucket;

    public FluidContent(RegistryHandle<FlowingFluid> source, RegistryHandle<FlowingFluid> flowing,
            RegistryHandle<LiquidBlock> block, RegistryHandle<Item> bucket) {
        this.source = source;
        this.flowing = flowing;
        this.block = block;
        this.bucket = bucket;
    }

    public RegistryHandle<FlowingFluid> source() { return source; }
    public RegistryHandle<FlowingFluid> flowing() { return flowing; }
    public RegistryHandle<LiquidBlock> block() { return block; }
    public RegistryHandle<Item> bucket() { return bucket; }
}
