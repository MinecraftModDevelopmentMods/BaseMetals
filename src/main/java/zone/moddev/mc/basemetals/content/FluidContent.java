package zone.moddev.mc.basemetals.content;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;

public record FluidContent(
        DeferredHolder<BaseFlowingFluid.Source, BaseFlowingFluid.Source> source,
        DeferredHolder<BaseFlowingFluid.Flowing, BaseFlowingFluid.Flowing> flowing,
        DeferredHolder<LiquidBlock, LiquidBlock> block,
        DeferredHolder<Item, Item> bucket) {
}
