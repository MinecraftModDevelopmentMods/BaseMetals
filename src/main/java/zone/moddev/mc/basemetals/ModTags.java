package zone.moddev.mc.basemetals;

import net.minecraft.world.level.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.resources.ResourceLocation;

public final class ModTags {
    public static final Tag<Block> SCYTHE_HARVESTABLE = BlockTags.createOptional(
            new ResourceLocation(BaseMetals.MOD_ID, "scythe_harvestable"));
    public static final Tag<Block> CRACKHAMMER_CRUSHABLE = BlockTags.createOptional(
            new ResourceLocation(BaseMetals.MOD_ID, "crackhammer_crushable"));

    private ModTags() {}

    /** Register named tags before the first data-pack load binds them. */
    public static void initialize() {}
}
