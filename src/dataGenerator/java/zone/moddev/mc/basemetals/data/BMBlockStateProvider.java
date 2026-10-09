package zone.moddev.mc.basemetals.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

/**
 * Builds ores from a vanilla host texture and a transparent ore overlay.
 *
 * @author KiriCattus (Kiri), original two-layer model
 */
@Mod.EventBusSubscriber(modid = "basemetals", bus = Mod.EventBusSubscriber.Bus.MOD)
public final class BMBlockStateProvider extends BlockStateProvider {
    private static final float OVERLAY_OFFSET = 0.05F;

    public BMBlockStateProvider(DataGenerator generator, ExistingFileHelper existingFiles) {
        super(generator, "basemetals", existingFiles);
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        if (event.includeServer()) {
            event.getGenerator().addProvider(new HarvestTagProvider(event.getGenerator(), event.getExistingFileHelper()));
        }
        if (event.includeClient()) {
            event.getGenerator().addProvider(new BMBlockStateProvider(
                    event.getGenerator(), event.getExistingFileHelper()));
        }
    }

    @Override
    protected void registerStatesAndModels() {
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;

            Block block = ModContent.blocksById().get(material.name() + "_ore").get();
            String host = "stone";
            if (material.name().equals("adamantine") || material.name().equals("coldiron")) {
                host = "netherrack";
            } else if (material.name().equals("starsteel")) {
                host = "end_stone";
            }

            createOverlayOre(block, mcLoc("block/" + host),
                    modLoc("block/ore_overlays/" + material.name() + "_ore"));
        }
    }

    private void createOverlayOre(Block block, ResourceLocation baseTexture, ResourceLocation overlayTexture) {
        String blockName = ForgeRegistries.BLOCKS.getKey(block).getPath();

        BlockModelBuilder builder = models().withExistingParent(blockName, mcLoc("block/block"))
                .texture("particle", baseTexture)
                .texture("base", baseTexture)
                .texture("overlay", overlayTexture)
                .element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((direction, face) -> face.texture("#base").cullface(direction))
                .end()
                // Keep the overlay clear of the host, including distant mip levels.
                .element()
                .from(-OVERLAY_OFFSET, -OVERLAY_OFFSET, -OVERLAY_OFFSET)
                .to(16 + OVERLAY_OFFSET, 16 + OVERLAY_OFFSET, 16 + OVERLAY_OFFSET)
                .allFaces((direction, face) -> face.texture("#overlay").cullface(direction))
                .end();

        simpleBlock(block, builder);
    }
}
