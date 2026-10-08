package zone.moddev.mc.basemetals;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;

public final class ModTags {
    public static final ITag<Block> SCYTHE_HARVESTABLE = BlockTags.createOptional(
            new ResourceLocation(BaseMetals.MOD_ID, "scythe_harvestable"));
    public static final ITag<Block> CRACKHAMMER_CRUSHABLE = BlockTags.createOptional(
            new ResourceLocation(BaseMetals.MOD_ID, "crackhammer_crushable"));

    private ModTags() {}

    /** Register named tags before the first data-pack load binds them. */
    public static void initialize() {}
}
