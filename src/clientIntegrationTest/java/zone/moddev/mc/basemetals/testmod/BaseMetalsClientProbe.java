package zone.moddev.mc.basemetals.testmod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.imageio.ImageIO;

import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.resources.IResource;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.GameType;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Checks models and renders a disposable world in the packaged Forge client. */
@Mod(BaseMetalsClientProbe.MODID)
@Mod.EventBusSubscriber(modid = BaseMetalsClientProbe.MODID, value = Dist.CLIENT)
public final class BaseMetalsClientProbe {
    static final String MODID = "basemetalsclientprobe";
    private static final String WORLD_DIRECTORY = "basemetals-client-smoke-world";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    private static volatile BaseMetalsClientProbe instance;

    private int state;
    private int stateTicks;
    private int renderedFrames;

    public BaseMetalsClientProbe() {
        instance = this;
    }

    @SubscribeEvent
    public static void onWorldRendered(RenderWorldLastEvent event) {
        BaseMetalsClientProbe probe = instance;
        if (probe != null && Boolean.getBoolean("basemetalsclientprobe.enabled") && probe.state == 1) {
            probe.renderedFrames++;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        BaseMetalsClientProbe probe = instance;
        if (probe == null || event.phase != TickEvent.Phase.END
                || !Boolean.getBoolean("basemetalsclientprobe.enabled")) return;
        probe.tick();
    }

    private void tick() {
        Minecraft minecraft = Minecraft.getInstance();
        if (++stateTicks > 3600) fail(minecraft, "Timed out in client probe state " + state);
        try {
            if (state == 0 && minecraft.currentScreen instanceof GuiMainMenu) {
                validateClientContent(minecraft);
                minecraft.launchIntegratedServer(WORLD_DIRECTORY, "Base Metals Client Smoke",
                        new WorldSettings(0L, GameType.CREATIVE, false, false, WorldType.DEFAULT));
                nextState(1);
            } else if (state == 1 && minecraft.world != null && minecraft.player != null
                    && renderedFrames >= 8 && stateTicks >= 100) {
                writeMarker();
                LOGGER.info("BASEMETALS_CLIENT_PROBE PASS frames={}", Integer.valueOf(renderedFrames));
                minecraft.shutdown();
                nextState(2);
            }
        } catch (RuntimeException | IOException failure) {
            fail(minecraft, failure.toString());
        }
    }

    private static void validateClientContent(Minecraft minecraft) throws IOException {
        require(ModList.get().isLoaded("basemetals"), "Base Metals is not loaded");
        require(ModList.get().isLoaded("orespawn"), "OreSpawn is not loaded");
        require(MaterialCatalogue.ALL.size() == 22, "material catalogue size");
        require(ModContent.blocksById().size() == 360, "block catalogue size");
        require(ModContent.itemsById().size() == 1115, "item catalogue size");
        require(ModContent.fluids().size() == 36, "fluid catalogue size");
        require(ForgeRegistries.ITEMS.containsKey(new ResourceLocation("basemetals", "mercury_bucket")),
                "mercury bucket registration");
        for (FluidContent fluid : ModContent.fluids().values()) {
            require(fluid.bucket().get().getGroup() == ModTabs.ITEMS, "bucket creative group");
        }
        validateBucketModel(minecraft, "mercury_bucket");
        validateBucketModel(minecraft, "tin_bucket");
        validateOreModels(minecraft);
        minecraft.getResourceManager().getResource(
                new ResourceLocation("basemetals", "textures/item/adamantine_sword.png"));
        minecraft.getResourceManager().getResource(
                new ResourceLocation("basemetals", "textures/block/adamantine_block.png"));
    }

    private static void validateOreModels(Minecraft minecraft) throws IOException {
        int ores = 0;
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ores++;

            String name = material.name() + "_ore";
            String host = "minecraft:block/stone";
            if ("adamantine".equals(material.name()) || "coldiron".equals(material.name())) {
                host = "minecraft:block/netherrack";
            } else if ("starsteel".equals(material.name())) {
                host = "minecraft:block/end_stone";
            }

            Block block = ModContent.blocksById().get(name).get();
            require(block.getRenderLayer() == BlockRenderLayer.CUTOUT_MIPPED, name + " is not cutout-mipped");

            IBakedModel blockModel = minecraft.getBlockRendererDispatcher().getModelForState(block.getDefaultState());
            validateOreModel(blockModel, block, name, host);
            ItemStack stack = new ItemStack(ModContent.item(name).get());
            IBakedModel itemModel = minecraft.getItemRenderer().getItemModelWithOverrides(stack, null, null);
            validateOreModel(itemModel, null, name + " item", host);
        }

        require(ores == 13, "overlay ore count");
        validateHostOverride(minecraft, "stone", "tin");
        validateHostOverride(minecraft, "netherrack", "coldiron");
        validateHostOverride(minecraft, "end_stone", "starsteel");
        LOGGER.info("BASEMETALS_ORE_OVERLAY_PROBE PASS ores={} host_overrides=3", Integer.valueOf(ores));
    }

    private static void validateOreModel(IBakedModel model, Block block, String name, String host) {
        require(host.equals(model.getParticleTexture().getName().toString()), name + " particle host");
        Random random = new Random(42L);
        require(model.getQuads(block == null ? null : block.getDefaultState(), null, random).isEmpty(),
                name + " has unexpected unculled faces");

        for (EnumFacing side : EnumFacing.values()) {
            List<BakedQuad> quads = model.getQuads(block == null ? null : block.getDefaultState(), side, random);
            require(quads.size() == 2, name + " should have base and overlay on " + side);
            boolean hasBase = false;
            boolean hasOverlay = false;
            for (BakedQuad quad : quads) {
                String texture = quad.getSprite().getName().toString();
                hasBase |= host.equals(texture);
                hasOverlay |= "minecraft:block/glass".equals(texture);
                require(!"missingno".equals(quad.getSprite().getName().getPath()), name + " missing texture");
                validateOreVertices(quad, "minecraft:block/glass".equals(texture), name);
                if ("minecraft:block/glass".equals(texture)) {
                    require(quad.getSprite().isPixelTransparent(0, 8, 8), name + " overlay lost transparency");
                }
            }

            require(hasBase && hasOverlay, name + " lost a texture layer on " + side);
        }
    }

    private static void validateOreVertices(BakedQuad quad, boolean overlay, String name) {
        int[] vertices = quad.getVertexData();
        int stride = vertices.length / 4;
        float low = overlay ? -0.001F / 16 : 0;
        float high = overlay ? 16.001F / 16 : 1;

        for (int vertex = 0; vertex < 4; vertex++) {
            for (int axis = 0; axis < 3; axis++) {
                float coordinate = Float.intBitsToFloat(vertices[vertex * stride + axis]);
                require(Math.min(Math.abs(coordinate - low), Math.abs(coordinate - high)) < 0.000001F,
                        name + " incorrect " + (overlay ? "overlay" : "base") + " vertex");
            }
        }
    }

    private static void validateHostOverride(Minecraft minecraft, String host, String source) throws IOException {
        try (IResource replacement = minecraft.getResourceManager().getResource(
                    new ResourceLocation("minecraft", "textures/block/" + host + ".png"));
                IResource fixture = minecraft.getResourceManager().getResource(
                    new ResourceLocation("basemetals", "textures/block/" + source + "_ore.png"))) {
            BufferedImage actual = ImageIO.read(replacement.getInputStream());
            BufferedImage expected = ImageIO.read(fixture.getInputStream());
            require(actual.getWidth() == expected.getWidth() && actual.getHeight() == expected.getHeight(),
                    host + " resource-pack image size");

            for (int y = 0; y < actual.getHeight(); y++) {
                for (int x = 0; x < actual.getWidth(); x++) {
                    require(actual.getRGB(x, y) == expected.getRGB(x, y), host + " resource-pack replacement");
                }
            }
        }
    }

    private static void validateBucketModel(Minecraft minecraft, String name) {
        ItemStack stack = new ItemStack(ModContent.item(name).get());
        IBakedModel model = minecraft.getItemRenderer().getItemModelWithOverrides(stack, null, null);
        Random random = new Random(42L);
        List<BakedQuad> quads = new ArrayList<BakedQuad>(model.getQuads(null, null, random));
        for (net.minecraft.util.EnumFacing side : net.minecraft.util.EnumFacing.values()) {
            quads.addAll(model.getQuads(null, side, random));
        }
        require(!quads.isEmpty(), name + " has no rendered quads");
        boolean hasTintedFluidLayer = false;
        for (BakedQuad quad : quads) {
            require(!"missingno".equals(quad.getSprite().getName().getPath()),
                    name + " uses the missing-texture sprite");
            if (quad.hasTintIndex() && quad.getTintIndex() == 1) hasTintedFluidLayer = true;
        }
        require(hasTintedFluidLayer, name + " has no tintable fluid layer");
        String fluidName = name.substring(0, name.length() - "_bucket".length());
        FluidContent fluid = ModContent.fluids().get(fluidName);
        require(fluid != null, name + " has no matching fluid content");
        require(minecraft.getItemColors().getColor(stack, 1) == ModContent.fluidColour(fluid.source().get()),
                name + " does not use its material fluid colour");
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private void writeMarker() throws IOException {
        Properties values = new Properties();
        values.setProperty("content_verified", "true");
        values.setProperty("integrated_world_rendered", Boolean.toString(renderedFrames >= 8));
        values.setProperty("rendered_frames", Integer.toString(renderedFrames));
        try (FileOutputStream output = new FileOutputStream(new File("client-smoke-pass.properties"))) {
            values.store(output, "Base Metals Forge 1.13.2 packaged-client gate");
        }
    }

    private void nextState(int next) {
        state = next;
        stateTicks = 0;
    }

    private static void fail(Minecraft minecraft, String message) {
        try {
            Properties values = new Properties();
            values.setProperty("failure", message);
            try (FileOutputStream output = new FileOutputStream(new File("client-smoke-failure.properties"))) {
                values.store(output, "Base Metals packaged-client failure");
            }
        } catch (IOException ignored) {
        }
        minecraft.shutdown();
        throw new IllegalStateException(message);
    }
}
