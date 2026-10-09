package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.material.MaterialDefinition;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;

public final class MaterialContent {
    private final MaterialDefinition definition;
    private final Map<String, RegistryHandle<Block>> blocks;
    private final Map<String, RegistryHandle<Item>> items;

    public MaterialContent(MaterialDefinition definition, Map<String, RegistryHandle<Block>> blocks,
            Map<String, RegistryHandle<Item>> items) {
        this.definition = definition;
        this.blocks = Collections.unmodifiableMap(new LinkedHashMap<String, RegistryHandle<Block>>(blocks));
        this.items = Collections.unmodifiableMap(new LinkedHashMap<String, RegistryHandle<Item>>(items));
    }

    public MaterialDefinition definition() { return definition; }
    public Map<String, RegistryHandle<Block>> blocks() { return blocks; }
    public Map<String, RegistryHandle<Item>> items() { return items; }
}
