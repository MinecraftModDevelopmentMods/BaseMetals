package zone.moddev.mc.basemetals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.ModContent;

/** Uses the material catalogue for 1.17's mining and tool-tier tags. */
public final class HarvestTagProvider extends BlockTagsProvider {
    public HarvestTagProvider(DataGenerator generator, ExistingFileHelper existingFiles) {
        super(generator, BaseMetals.MOD_ID, existingFiles);
    }

    @Override
    protected void addTags() {
        ModContent.blocksById().forEach((name, handle) -> {
            if (ModContent.fluids().containsKey(name)) return;

            Block block = handle.get();
            String materialName = name.replaceFirst("^double_", "").split("_")[0];
            if ("wood".equals(materialName)) {
                tag(BlockTags.MINEABLE_WITH_AXE).add(block);
                return;
            }

            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(block);
            int level = ModContent.requiredHarvestLevel(materialName);
            if (level >= 3) tag(BlockTags.NEEDS_DIAMOND_TOOL).add(block);
            else if (level == 2) tag(BlockTags.NEEDS_IRON_TOOL).add(block);
            else if (level == 1) tag(BlockTags.NEEDS_STONE_TOOL).add(block);
        });
    }
}
