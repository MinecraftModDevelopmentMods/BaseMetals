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
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.client.gui.GuiModList;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;
import zone.moddev.mc.basemetals.client.BaseMetalsConfigScreen;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.resources.I18n;

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
                if (Boolean.getBoolean("basemetalsclientprobe.login")) {
                    require(BaseMetalsConfig.activeMode().serializedName().equals(
                            System.getProperty("basemetalsclientprobe.expectedMode")), "startup mode differs from profile");
                    minecraft.displayGuiScreen(new net.minecraft.client.gui.GuiConnecting(
                            minecraft.currentScreen, minecraft, "127.0.0.1",
                            Integer.getInteger("basemetalsclientprobe.port", 25565)));
                    nextState(3);
                    return;
                }
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
            } else if (state == 3) {
                checkLogin(minecraft);
            }
        } catch (RuntimeException | IOException failure) {
            fail(minecraft, failure.toString());
        }
    }

    private void checkLogin(Minecraft minecraft) throws IOException {
        boolean rejected = Boolean.getBoolean("basemetalsclientprobe.expectReject");
        if (minecraft.world != null && minecraft.player != null && stateTicks > 60) {
            require(!rejected, "mismatched modes entered gameplay");
            net.minecraft.item.crafting.IRecipe bow = minecraft.world.getRecipeManager().getRecipe(
                    new ResourceLocation("basemetals", "tin_bow"));
            require(bow != null, "recipe IDs changed during mode synchronization");
            require(bow.isDynamic() == (BaseMetalsConfig.activeMode() == ContentMode.LOW_FANTASY),
                    "client recipe policy did not match the server");
            if (Boolean.getBoolean("basemetalsclientprobe.modeSwitch")) {
                require(minecraft.player.getRecipeBook().isUnlocked(bow)
                                == (BaseMetalsConfig.activeMode() == ContentMode.HIGH_FANTASY),
                        "earned recipe-book unlock did not survive the mode change");
                for (String name : new String[] {"steel_bow", "adamantine_bow", "adamantine_crossbow", "adamantine_gear",
                        "adamantine_arrow", "adamantine_rod", "adamantine_pickaxe"}) {
                    net.minecraft.item.crafting.IRecipe recipe = minecraft.world.getRecipeManager()
                            .getRecipe(new ResourceLocation("basemetals", name));
                    require(minecraft.player.getRecipeBook().isUnlocked(recipe) != recipe.isDynamic(),
                            "material discovery did not synchronize to the client: " + name);
                }
                require(!minecraft.player.getRecipeBook().isUnlocked(minecraft.world.getRecipeManager()
                        .getRecipe(new ResourceLocation("basemetals", "gold_bow"))),
                        "mode change unlocked an unearned recipe");
            }
            require(minecraft.player.inventory.getStackInSlot(0).getItem() == ModContent.item("tin_bow").get(),
                    "existing restricted item did not synchronize");
            finishLogin(minecraft, "connected");
        } else if (minecraft.currentScreen instanceof net.minecraft.client.gui.GuiDisconnected) {
            StringBuilder message = new StringBuilder();
            try {
                for (java.lang.reflect.Field field : minecraft.currentScreen.getClass().getDeclaredFields()) {
                    if (net.minecraft.util.text.ITextComponent.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        message.append(((net.minecraft.util.text.ITextComponent) field.get(minecraft.currentScreen)).getString());
                    }
                }
            } catch (ReflectiveOperationException failure) {
                throw new IllegalStateException(failure);
            }
            require(rejected && minecraft.world == null, "unexpected disconnect: " + message);
            require(message.toString().contains("Base Metals") && message.toString().contains("High Fantasy")
                    && message.toString().contains("Low Fantasy") && message.toString().contains("Config"),
                    "disconnect did not explain the mode mismatch: " + message);
            finishLogin(minecraft, "rejected");
        }
    }


    private void finishLogin(Minecraft minecraft, String result) throws IOException {
        Properties marker = new Properties();
        marker.setProperty("result", result);
        marker.setProperty("mode", BaseMetalsConfig.activeMode().serializedName());
        try (FileOutputStream output = new FileOutputStream("mode-login-pass.properties")) {
            marker.store(output, "Base Metals content-mode login test");
        }
        LOGGER.info("BASEMETALS_MODE_LOGIN_CLIENT PASS result={} mode={}", result, BaseMetalsConfig.activeMode());
        minecraft.shutdown();
        nextState(2);
    }

    private static void validateClientContent(Minecraft minecraft) throws IOException {
        require(ModList.get().isLoaded("basemetals"), "Base Metals is not loaded");
        require(ModList.get().isLoaded("orespawn"), "OreSpawn is not loaded");
        require(ModList.get().getModContainerById("basemetals").get()
                .getCustomExtension(ExtensionPoint.CONFIGGUIFACTORY).isPresent(),
                "Base Metals Mods-list configuration screen is missing");
        require(WorldSettingsExtensionRegistry.extensions().stream()
                .filter(extension -> "basemetals".equals(extension.id().getNamespace())).count() == 1,
                "Base Metals OreSpawn configuration cog is missing or registered twice");
        validateConfigScreen(minecraft);
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

    private static void validateConfigScreen(Minecraft minecraft) {
        GuiScreen parent = minecraft.currentScreen;
        boolean originalEffects = BaseMetalsConfig.SPECIAL_EFFECTS.get();
        String originalMode = BaseMetalsConfig.CONTENT_MODE.get();
        ContentMode activeMode = BaseMetalsConfig.activeMode();

        GuiModList modsScreen = new GuiModList(parent);
        minecraft.displayGuiScreen(modsScreen);
        int baseMetalsIndex = -1;
        for (int index = 0; index < ModList.get().getMods().size(); index++) {
            if ("basemetals".equals(ModList.get().getMods().get(index).getModId())) baseMetalsIndex = index;
        }
        require(baseMetalsIndex >= 0, "Base Metals is missing from the Mods list");
        modsScreen.selectModIndex(baseMetalsIndex);
        ForgeHooksClient.drawScreen(modsScreen, 0, 0, 0.0F);
        GuiButton configButton = button(modsScreen, 20);
        require(configButton.enabled, "Base Metals Config button is disabled in the Mods list");
        require(modsScreen.mouseClicked(configButton.x + 1, configButton.y + 1, 0),
                "Mods-list Config button did not accept a mouse click");
        GuiScreen forgeScreen = minecraft.currentScreen;
        require(forgeScreen instanceof BaseMetalsConfigScreen, "Mods-list button opened the wrong screen");
        require(forgeScreen.getChildren().size() == 9, "config screen must expose five settings and four actions");
        click(forgeScreen, 20);
        click(forgeScreen, 10);
        click(forgeScreen, 1);
        require(minecraft.currentScreen == modsScreen, "Cancel did not return to the Mods list");
        require(originalMode.equals(BaseMetalsConfig.CONTENT_MODE.get())
                && originalEffects == BaseMetalsConfig.SPECIAL_EFFECTS.get(), "Cancel changed the loaded config");

        ForgeHooksClient.drawScreen(modsScreen, 0, 0, 0.0F);
        require(button(modsScreen, 20).enabled, "Config button was disabled on returning from the settings");
        modsScreen.selectModIndex(baseMetalsIndex);
        ForgeHooksClient.drawScreen(modsScreen, 0, 0, 0.0F);
        require(!button(modsScreen, 20).enabled, "Config button stayed enabled with no mod selected");
        int forgeIndex = -1;
        for (int index = 0; index < ModList.get().getMods().size(); index++) {
            if ("forge".equals(ModList.get().getMods().get(index).getModId())) forgeIndex = index;
        }
        require(forgeIndex >= 0, "Forge is missing from the Mods list");
        modsScreen.selectModIndex(forgeIndex);
        ForgeHooksClient.drawScreen(modsScreen, 0, 0, 0.0F);
        require(!button(modsScreen, 20).enabled, "Base Metals enabled the Config button for Forge");
        click(modsScreen, 6);
        require(minecraft.currentScreen == parent, "Mods-list Done did not return to the main menu");

        GuiScreen oreSpawnScreen = WorldSettingsExtensionRegistry.extensions().stream()
                .filter(extension -> "basemetals:configuration".equals(extension.id().toString()))
                .findFirst().get().createScreen(parent);
        require(oreSpawnScreen instanceof BaseMetalsConfigScreen, "OreSpawn factory uses the wrong screen");
        minecraft.displayGuiScreen(oreSpawnScreen);
        click(oreSpawnScreen, 10);
        click(oreSpawnScreen, 20);
        click(oreSpawnScreen, 2);
        require(button(oreSpawnScreen, 20).displayString.contains(I18n.format(ContentMode.HIGH_FANTASY.translationKey())),
                "Defaults did not restore High Fantasy");
        click(oreSpawnScreen, 3);
        require(button(oreSpawnScreen, 10).displayString.endsWith(I18n.format(originalEffects ? "options.on" : "options.off")),
                "Undo did not restore the original boolean");
        click(oreSpawnScreen, 20);
        click(oreSpawnScreen, 0);
        require(minecraft.currentScreen instanceof GuiYesNo, "Mode change has no restart confirmation");
        oreSpawnScreen.confirmResult(false, 0);
        require(minecraft.currentScreen == oreSpawnScreen, "Declining confirmation lost pending edits");
        require(BaseMetalsConfig.CONTENT_MODE.get().equals(originalMode), "Declining confirmation saved the mode");
        click(oreSpawnScreen, 0);
        oreSpawnScreen.confirmResult(true, 0);
        require(minecraft.currentScreen == parent, "Done did not return to the OreSpawn parent");
        require(!BaseMetalsConfig.CONTENT_MODE.get().equals(originalMode), "Done did not save the string property");
        require(BaseMetalsConfig.activeMode() == activeMode, "GUI changes altered the startup-latched mode");

        // Restore the disposable profile before the integrated-world smoke test.
        BaseMetalsConfig.set(BaseMetalsConfig.CONTENT_MODE, originalMode);
        BaseMetalsConfig.set(BaseMetalsConfig.SPECIAL_EFFECTS, originalEffects);
        BaseMetalsConfig.save();
        require(!I18n.format("config.basemetals.title").startsWith("config."), "GUI title is untranslated");
        LOGGER.info("BASEMETALS_CONFIG_GUI_PROBE PASS entries=5 factories=2 mods_button=true cancel=true undo=true defaults=true save=true");
    }

    private static GuiButton button(GuiScreen screen, int id) {
        return screen.getChildren().stream().filter(child -> child instanceof GuiButton)
                .map(child -> (GuiButton) child).filter(button -> button.id == id).findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing config button " + id));
    }

    private static void click(GuiScreen screen, int id) {
        button(screen, id).onClick(0, 0);
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
            String overlay = "basemetals:block/ore_overlays/" + name;

            IBakedModel blockModel = minecraft.getBlockRendererDispatcher().getModelForState(block.getDefaultState());
            validateOreModel(blockModel, block, name, host, overlay);
            ItemStack stack = new ItemStack(ModContent.item(name).get());
            IBakedModel itemModel = minecraft.getItemRenderer().getItemModelWithOverrides(stack, null, null);
            validateOreModel(itemModel, null, name + " item", host, overlay);
        }

        require(ores == 13, "overlay ore count");
        validateHostOverride(minecraft, "stone", "tin");
        validateHostOverride(minecraft, "netherrack", "coldiron");
        validateHostOverride(minecraft, "end_stone", "starsteel");
        LOGGER.info("BASEMETALS_ORE_OVERLAY_PROBE PASS ores={} host_overrides=3", Integer.valueOf(ores));
    }

    private static void validateOreModel(IBakedModel model, Block block, String name, String host, String overlay) {
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
                hasOverlay |= overlay.equals(texture);
                require(!"missingno".equals(quad.getSprite().getName().getPath()), name + " missing texture");
                validateOreVertices(quad, overlay.equals(texture), name);
                if (overlay.equals(texture)) {
                    boolean transparent = false;
                    boolean visible = false;

                    for (int y = 0; y < quad.getSprite().getHeight(); y++) {
                        for (int x = 0; x < quad.getSprite().getWidth(); x++) {
                            if (quad.getSprite().isPixelTransparent(0, x, y)) transparent = true;
                            else visible = true;
                        }
                    }

                    require(transparent && visible, name + " overlay must contain ore and transparent host pixels");
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
