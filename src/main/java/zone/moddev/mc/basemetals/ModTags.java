package zone.moddev.mc.basemetals;

import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class ModTags {
    public static final TagKey<Block> SCYTHE_HARVESTABLE = BlockTags.create(
            Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID, "scythe_harvestable"));
    public static final TagKey<Block> CRACKHAMMER_CRUSHABLE = BlockTags.create(
            Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID, "crackhammer_crushable"));

    private ModTags() {}
}
