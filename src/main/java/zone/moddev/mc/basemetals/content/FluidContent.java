package zone.moddev.mc.basemetals.content;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.LiquidBlock;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.registries.DeferredHolder;

public record FluidContent(
        DeferredHolder<Fluid, BaseFlowingFluid.Source> source,
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing,
        DeferredHolder<Block, ? extends LiquidBlock> block,
        DeferredHolder<Item, ? extends Item> bucket) {
}
