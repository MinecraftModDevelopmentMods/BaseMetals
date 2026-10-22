package zone.moddev.mc.basemetals.content;

import java.util.Map;

import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public record MaterialContent(
        MaterialDefinition definition,
        Map<String, DeferredHolder<Block, ? extends Block>> blocks,
        Map<String, DeferredHolder<Item, ? extends Item>> items) {
}
