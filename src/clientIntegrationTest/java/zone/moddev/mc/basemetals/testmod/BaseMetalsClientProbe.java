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

import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Registry;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fmlclient.ConfigGuiHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fmlclient.gui.screen.ModListScreen;
import net.minecraftforge.fmlclient.gui.widget.ModListWidget;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zone.moddev.mc.orespawn.api.client.WorldSettingsExtensionRegistry;
import zone.moddev.mc.basemetals.client.BaseMetalsConfigScreen;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.resources.language.I18n;

/** Checks models and renders a disposable world in the packaged Forge client. */
@Mod(BaseMetalsClientProbe.MODID)
@Mod.EventBusSubscriber(modid = BaseMetalsClientProbe.MODID, value = Dist.CLIENT)
public final class BaseMetalsClientProbe {
    static final String MODID = "basemetalsclientprobe";
    private static final String WORLD_DIRECTORY = "basemetals-client-smoke-world";
    private boolean pendingCreativeBucketScreenshot;
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    private static volatile BaseMetalsClientProbe instance;

    private int state;
    private int stateTicks;
    private int renderedFrames;
    private final java.util.Set<Integer> renderedProjectiles = new java.util.HashSet<>();

    public BaseMetalsClientProbe() {
        instance = this;
        LOGGER.info("Client probe loaded; enabled={}", Boolean.getBoolean("basemetalsclientprobe.enabled"));
    }

    @SubscribeEvent
    public static void onScreenRendered(net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent.Post event) {
        BaseMetalsClientProbe probe = instance;
        if (probe == null || !probe.pendingCreativeBucketScreenshot
                || !(event.getGui() instanceof net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen)) return;

        Minecraft minecraft = Minecraft.getInstance();
        try (com.mojang.blaze3d.platform.NativeImage screenshot = net.minecraft.client.Screenshot
                .takeScreenshot(minecraft.getMainRenderTarget())) {
            screenshot.writeToFile(new File("creative-buckets.png"));
            LOGGER.info("BASEMETALS_CREATIVE_BUCKET_SCREENSHOT saved=creative-buckets.png");
        } catch (IOException failure) {
            fail(minecraft, failure.toString());
        } finally {
            probe.pendingCreativeBucketScreenshot = false;
            minecraft.setScreen(null);
        }
    }

    @SubscribeEvent
    public static void onWorldRendered(RenderWorldLastEvent event) {
        BaseMetalsClientProbe probe = instance;
        if (probe != null && Boolean.getBoolean("basemetalsclientprobe.enabled")
                && (probe.state == 1 || probe.state == 4)) {
            probe.renderedFrames++;
        }
    }

    @SubscribeEvent
    public static void onProjectileRendered(net.minecraftforge.client.event.RenderNameplateEvent event) {
        BaseMetalsClientProbe probe = instance;
        if (probe != null && probe.state == 4
                && event.getEntity() instanceof zone.moddev.mc.basemetals.entity.MaterialProjectile) {
            probe.renderedProjectiles.add(event.getEntity().getId());
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
        if (stateTicks == 0) LOGGER.info("Client probe state={} screen={}", state,
                minecraft.screen == null ? "none" : minecraft.screen.getClass().getName());
        if (++stateTicks > 3600) fail(minecraft, "Timed out in client probe state " + state);
        try {
            if (state == 0 && minecraft.screen instanceof TitleScreen) {
                if (Boolean.getBoolean("basemetalsclientprobe.login")) {
                    require(BaseMetalsConfig.activeMode().serializedName().equals(
                            System.getProperty("basemetalsclientprobe.expectedMode")), "startup mode differs from profile");
                    net.minecraft.client.gui.screens.ConnectScreen.startConnecting(minecraft.screen, minecraft,
                            net.minecraft.client.multiplayer.resolver.ServerAddress.parseString(
                                    "127.0.0.1:" + Integer.getInteger("basemetalsclientprobe.port", 25565)), null);
                    nextState(3);
                    return;
                }
                validateClientContent(minecraft);
                createWorld(minecraft);
                nextState(1);
            } else if (state == 1 && minecraft.level != null && minecraft.player != null
                    && renderedFrames >= 8 && stateTicks >= 100) {
                validateCreativeBucketScreen(minecraft);
                validateCreativeCrossbows(minecraft);
                spawnProjectileRenderChecks(minecraft);
                nextState(4);
            } else if (state == 4 && renderedProjectiles.size() == 6 && stateTicks >= 40) {
                LOGGER.info("BASEMETALS_PROJECTILE_RENDER_PROBE PASS rendered={}", renderedProjectiles.size());
                writeMarker();
                LOGGER.info("BASEMETALS_CLIENT_PROBE PASS frames={}", Integer.valueOf(renderedFrames));
                minecraft.stop();
                nextState(2);
            } else if (state == 3) {
                checkLogin(minecraft);
            }
        } catch (RuntimeException | IOException failure) {
            fail(minecraft, failure.toString());
        }
    }

    private static void createWorld(Minecraft minecraft) {
        if (Boolean.getBoolean("basemetalsclientprobe.reload")) {
            minecraft.loadLevel(WORLD_DIRECTORY);
            return;
        }

        RegistryAccess.RegistryHolder registries = RegistryAccess.builtin();
        WorldGenSettings generator = WorldGenSettings.makeDefault(
                registries.registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY),
                registries.registryOrThrow(Registry.BIOME_REGISTRY),
                registries.registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY))
                .withSeed(false, java.util.OptionalLong.of(0L));
        LevelSettings settings = new LevelSettings("Base Metals Client Smoke", GameType.CREATIVE,
                false, Difficulty.NORMAL, true, new GameRules(), DataPackConfig.DEFAULT);

        minecraft.createLevel(WORLD_DIRECTORY, settings, registries, generator);
    }

    private static void spawnProjectileRenderChecks(Minecraft minecraft) {
        java.util.UUID playerId = minecraft.player.getUUID();
        minecraft.getSingleplayerServer().execute(() -> {
            net.minecraft.server.level.ServerPlayer player = minecraft.getSingleplayerServer()
                    .getPlayerList().getPlayer(playerId);
            net.minecraft.server.level.ServerLevel world = player.getLevel();
            if (Boolean.getBoolean("basemetalsclientprobe.reload")) {
                java.util.Set<String> savedAmmo = new java.util.HashSet<>();
                for (net.minecraft.world.entity.Entity entity : world.getAllEntities()) {
                    if (entity instanceof zone.moddev.mc.basemetals.entity.MaterialProjectile) {
                        savedAmmo.add(((zone.moddev.mc.basemetals.entity.MaterialProjectile) entity)
                                .getAmmunition().getItem().getRegistryName().toString());
                    }
                }
                for (String material : new String[] {"gold", "steel", "adamantine"}) {
                    for (String form : new String[] {"arrow", "bolt"}) {
                        require(savedAmmo.contains("basemetals:" + material + "_" + form),
                                "projectile identity lost on client-world reload: " + material + " " + form);
                    }
                }
                LOGGER.info("BASEMETALS_CLIENT_RELOAD_PROBE PASS ammunition={}", savedAmmo.size());
                return;
            }

            net.minecraft.world.phys.Vec3 look = player.getLookAngle();
            int index = 0;

            for (String material : new String[] {"gold", "steel", "adamantine"}) {
                for (boolean bolt : new boolean[] {false, true}) {
                    String name = material + (bolt ? "_bolt" : "_arrow");
                    zone.moddev.mc.basemetals.entity.MaterialProjectile projectile =
                            new zone.moddev.mc.basemetals.entity.MaterialProjectile(
                                    bolt ? zone.moddev.mc.basemetals.entity.ModEntities.CUSTOM_BOLT.get()
                                            : zone.moddev.mc.basemetals.entity.ModEntities.CUSTOM_ARROW.get(),
                                    world, player, new ItemStack(ModContent.item(name).get()));
                    net.minecraft.world.entity.Entity entity = projectile;
                    entity.setPos(player.getX() + look.x * 3 + (index++ - 2.5) * 0.25,
                            player.getY() + player.getEyeHeight(), player.getZ() + look.z * 3);
                    entity.setNoGravity(true);
                    world.addFreshEntity(projectile);
                }
            }
        });
    }

    private static void validateCreativeCrossbows(Minecraft minecraft) {
        require(minecraft.level.isClientSide, "crossbow probe must run on the client");
        require(minecraft.player.getAbilities().instabuild, "crossbow probe needs a Creative player");
        int shots = 0;

        for (String name : ModContent.itemsById().keySet()) {
            if (!name.endsWith("_crossbow")) continue;
            String boltName = name.substring(0, name.length() - "_crossbow".length()) + "_bolt";
            ItemStack[] ammunition = {ItemStack.EMPTY, new ItemStack(Items.ARROW),
                    new ItemStack(ModContent.item("adamantine_arrow").get()),
                    new ItemStack(ModContent.item(boltName).get())};

            for (ItemStack supply : ammunition) {
                minecraft.player.getInventory().clearContent();
                ItemStack launcher = new ItemStack(ModContent.item(name).get());
                ItemStack original = supply.copy();
                minecraft.player.setItemInHand(InteractionHand.MAIN_HAND, launcher);
                minecraft.player.setItemInHand(InteractionHand.OFF_HAND, supply);
                BowItem crossbow = (BowItem) launcher.getItem();

                crossbow.releaseUsing(launcher, minecraft.level, minecraft.player,
                        crossbow.getUseDuration(launcher) - 20);

                require(ItemStack.matches(original, supply),
                        "Creative crossbow consumed client ammunition: " + name);
                require(launcher.getDamageValue() == 0, "Creative crossbow wore out on the client: " + name);
                shots++;
            }
        }

        minecraft.player.getInventory().clearContent();
        require(shots == 108, "all 27 crossbows need four client ammunition cases");
        LOGGER.info("BASEMETALS_CLIENT_CROSSBOWS PASS shots={}", Integer.valueOf(shots));
    }

    private void checkLogin(Minecraft minecraft) throws IOException {
        boolean rejected = Boolean.getBoolean("basemetalsclientprobe.expectReject");
        if (minecraft.level != null && minecraft.player != null && stateTicks > 60) {
            require(!rejected, "mismatched modes entered gameplay");
            net.minecraft.world.item.crafting.Recipe bow = minecraft.level.getRecipeManager().byKey(new ResourceLocation("basemetals", "tin_bow")).orElse(null);
            require(bow != null, "recipe IDs changed during mode synchronization");
            require(bow.isSpecial() == (BaseMetalsConfig.activeMode() == ContentMode.LOW_FANTASY),
                    "client recipe policy did not match the server");
            if (Boolean.getBoolean("basemetalsclientprobe.modeSwitch")) {
                require(minecraft.player.getRecipeBook().contains(bow)
                                == (BaseMetalsConfig.activeMode() == ContentMode.HIGH_FANTASY),
                        "earned recipe-book unlock did not survive the mode change");
                for (String name : new String[] {"steel_bow", "adamantine_bow", "adamantine_crossbow", "adamantine_gear",
                        "adamantine_arrow", "adamantine_rod", "adamantine_pickaxe"}) {
                    net.minecraft.world.item.crafting.Recipe recipe = minecraft.level.getRecipeManager()
                            .byKey(new ResourceLocation("basemetals", name)).orElse(null);
                    require(minecraft.player.getRecipeBook().contains(recipe) != recipe.isSpecial(),
                            "material discovery did not synchronize to the client: " + name);
                }
                require(!minecraft.player.getRecipeBook().contains(minecraft.level.getRecipeManager()
                        .byKey(new ResourceLocation("basemetals", "gold_bow")).orElse(null)),
                        "mode change unlocked an unearned recipe");
            }
            require(minecraft.player.getInventory().getItem(0).getItem() == ModContent.item("tin_bow").get(),
                    "existing restricted item did not synchronize");
            finishLogin(minecraft, "connected");
        } else if (minecraft.screen instanceof net.minecraft.client.gui.screens.DisconnectedScreen) {
            StringBuilder message = new StringBuilder();
            try {
                for (java.lang.reflect.Field field : minecraft.screen.getClass().getDeclaredFields()) {
                    if (net.minecraft.network.chat.Component.class.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        message.append(((net.minecraft.network.chat.Component) field.get(minecraft.screen)).getString());
                    }
                }
            } catch (ReflectiveOperationException failure) {
                throw new IllegalStateException(failure);
            }
            require(rejected && minecraft.level == null, "unexpected disconnect: " + message);
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
        minecraft.stop();
        nextState(2);
    }

    private static void validateClientContent(Minecraft minecraft) throws IOException {
        require(ModList.get().isLoaded("basemetals"), "Base Metals is not loaded");
        require(ModList.get().isLoaded("orespawn"), "OreSpawn is not loaded");
        require(ModList.get().getModContainerById("basemetals").get()
                .getCustomExtension(ConfigGuiHandler.ConfigGuiFactory.class).isPresent(),
                "Base Metals Mods-list configuration screen is missing");
        require(WorldSettingsExtensionRegistry.extensions().stream()
                .filter(extension -> "basemetals".equals(extension.id().getNamespace())).count() == 1,
                "Base Metals OreSpawn configuration cog is missing or registered twice");
        validateConfigScreen(minecraft);
        require(MaterialCatalogue.ALL.size() == 22, "material catalogue size");
        require(ModContent.blocksById().size() == 360, "block catalogue size");
        require(ModContent.itemsById().size() == 1127, "item catalogue size");
        require(ModContent.fluids().size() == 36, "fluid catalogue size");
        require(ForgeRegistries.ITEMS.containsKey(new ResourceLocation("basemetals", "mercury_bucket")),
                "mercury bucket registration");
        validateCreativeBuckets(minecraft);
        int rawItems = 0;
        for (String name : ModContent.itemsById().keySet()) {
            if (!name.endsWith("_raw")) continue;

            ItemStack stack = new ItemStack(ModContent.item(name).get());
            BakedModel model = minecraft.getItemRenderer().getModel(stack, null, null, 0);
            require(stack.getItem().getItemCategory() == ModTabs.ITEMS, name + " creative group");
            require(model != minecraft.getModelManager().getMissingModel(), name + " model is missing");
            require(model.getParticleIcon().getName().equals(new ResourceLocation("basemetals", "item/" + name)),
                    name + " uses the wrong raw-metal sprite");
            rawItems++;
        }
        require(rawItems == 12, "raw-metal item model count");

        for (FluidContent fluid : ModContent.fluids().values()) {
            require(fluid.bucket().get().getItemCategory() == ModTabs.ITEMS, "bucket creative group");
            require(ItemBlockRenderTypes.canRenderInLayer(fluid.block().get().defaultBlockState(),
                    RenderType.translucent()), "molten block render layer");
            require(ItemBlockRenderTypes.canRenderInLayer(fluid.source().get().defaultFluidState(),
                    RenderType.translucent()), "molten source render layer");
            require(ItemBlockRenderTypes.canRenderInLayer(fluid.flowing().get().defaultFluidState(),
                    RenderType.translucent()), "molten flowing render layer");
            validateBucketModel(minecraft, fluid.bucket().get().getRegistryName().getPath());
        }
        for (net.minecraft.world.level.block.Block block : ForgeRegistries.BLOCKS) {
            if (!"basemetals".equals(block.getRegistryName().getNamespace())) continue;
            if (block instanceof net.minecraft.world.level.block.DoorBlock
                    || block instanceof net.minecraft.world.level.block.TrapDoorBlock
                    || block instanceof net.minecraft.world.level.block.IronBarsBlock) {
                require(ItemBlockRenderTypes.canRenderInLayer(block.defaultBlockState(),
                        RenderType.cutoutMipped()), "transparent block render layer: " + block);
                require(block.getStateDefinition().getPossibleStates().stream().noneMatch(
                        net.minecraft.world.level.block.state.BlockState::canOcclude),
                        "transparent block hides neighbouring faces: " + block);
            }
        }
        validateOreModels(minecraft);
        validateWallModels(minecraft);
        minecraft.getResourceManager().getResource(
                new ResourceLocation("basemetals", "textures/item/adamantine_sword.png"));
        minecraft.getResourceManager().getResource(
                new ResourceLocation("basemetals", "textures/block/adamantine_block.png"));
    }

    private static void validateConfigScreen(Minecraft minecraft) {
        Screen parent = minecraft.screen;
        boolean originalEffects = BaseMetalsConfig.SPECIAL_EFFECTS.get();
        String originalMode = BaseMetalsConfig.CONTENT_MODE.get();
        ContentMode activeMode = BaseMetalsConfig.activeMode();

        ModListScreen modsScreen = new ModListScreen(parent);
        minecraft.setScreen(modsScreen);
        int baseMetalsIndex = -1;
        for (int index = 0; index < ModList.get().getMods().size(); index++) {
            if ("basemetals".equals(ModList.get().getMods().get(index).getModId())) baseMetalsIndex = index;
        }
        require(baseMetalsIndex >= 0, "Base Metals is missing from the Mods list");
        selectMod(modsScreen, "basemetals");
        ForgeHooksClient.drawScreen(modsScreen, new PoseStack(), 0, 0, 0.0F);
        Button configButton = button(modsScreen, 20);
        require(configButton.active, "Base Metals Config button is disabled in the Mods list");
        require(modsScreen.mouseClicked(configButton.x + 1, configButton.y + 1, 0),
                "Mods-list Config button did not accept a mouse click");
        Screen forgeScreen = minecraft.screen;
        require(forgeScreen instanceof BaseMetalsConfigScreen, "Mods-list button opened the wrong screen");
        require(forgeScreen.children().size() == 9, "config screen must expose five settings and four actions");
        click(forgeScreen, 20);
        click(forgeScreen, 10);
        click(forgeScreen, 1);
        require(minecraft.screen == modsScreen, "Cancel did not return to the Mods list");
        require(originalMode.equals(BaseMetalsConfig.CONTENT_MODE.get())
                && originalEffects == BaseMetalsConfig.SPECIAL_EFFECTS.get(), "Cancel changed the loaded config");

        ForgeHooksClient.drawScreen(modsScreen, new PoseStack(), 0, 0, 0.0F);
        require(button(modsScreen, 20).active, "Config button was disabled on returning from the settings");
        modsScreen.setSelected(null);
        ForgeHooksClient.drawScreen(modsScreen, new PoseStack(), 0, 0, 0.0F);
        require(!button(modsScreen, 20).active, "Config button stayed enabled with no mod selected");
        int forgeIndex = -1;
        for (int index = 0; index < ModList.get().getMods().size(); index++) {
            if ("forge".equals(ModList.get().getMods().get(index).getModId())) forgeIndex = index;
        }
        require(forgeIndex >= 0, "Forge is missing from the Mods list");
        selectMod(modsScreen, "forge");
        ForgeHooksClient.drawScreen(modsScreen, new PoseStack(), 0, 0, 0.0F);
        require(!button(modsScreen, 20).active, "Base Metals enabled the Config button for Forge");
        click(modsScreen, 6);
        require(minecraft.screen == parent, "Mods-list Done did not return to the main menu");

        Screen oreSpawnScreen = WorldSettingsExtensionRegistry.extensions().stream()
                .filter(extension -> "basemetals:configuration".equals(extension.id().toString()))
                .findFirst().get().createScreen(parent);
        require(oreSpawnScreen instanceof BaseMetalsConfigScreen, "OreSpawn factory uses the wrong screen");
        minecraft.setScreen(oreSpawnScreen);
        click(oreSpawnScreen, 10);
        click(oreSpawnScreen, 20);
        click(oreSpawnScreen, 2);
        require(button(oreSpawnScreen, 20).getMessage().getString().contains(I18n.get(ContentMode.HIGH_FANTASY.translationKey())),
                "Defaults did not restore High Fantasy");
        click(oreSpawnScreen, 3);
        require(button(oreSpawnScreen, 10).getMessage().getString().endsWith(I18n.get(originalEffects ? "options.on" : "options.off")),
                "Undo did not restore the original boolean");
        click(oreSpawnScreen, 20);
        click(oreSpawnScreen, 0);
        require(minecraft.screen instanceof ConfirmScreen, "Mode change has no restart confirmation");
        confirmation(minecraft, false);
        require(minecraft.screen == oreSpawnScreen, "Declining confirmation lost pending edits");
        require(BaseMetalsConfig.CONTENT_MODE.get().equals(originalMode), "Declining confirmation saved the mode");
        click(oreSpawnScreen, 0);
        confirmation(minecraft, true);
        require(minecraft.screen == parent, "Done did not return to the OreSpawn parent");
        require(!BaseMetalsConfig.CONTENT_MODE.get().equals(originalMode), "Done did not save the string property");
        require(BaseMetalsConfig.activeMode() == activeMode, "GUI changes altered the startup-latched mode");

        // Restore the disposable profile before the integrated-world smoke test.
        BaseMetalsConfig.set(BaseMetalsConfig.CONTENT_MODE, originalMode);
        BaseMetalsConfig.set(BaseMetalsConfig.SPECIAL_EFFECTS, originalEffects);
        BaseMetalsConfig.save();
        require(!I18n.get("config.basemetals.title").startsWith("config."), "GUI title is untranslated");
        LOGGER.info("BASEMETALS_CONFIG_GUI_PROBE PASS entries=5 factories=2 mods_button=true cancel=true undo=true defaults=true save=true");
    }

    private static void selectMod(ModListScreen screen, String modId) {
        ModListWidget list = (ModListWidget) screen.children().stream()
                .filter(child -> child instanceof ModListWidget).findFirst().get();

        for (ModListWidget.ModEntry entry : list.children()) {
            if (entry.getInfo().getModId().equals(modId)) {
                entry.mouseClicked(0, 0, 0);
                return;
            }
        }

        throw new IllegalStateException("Missing mod " + modId);
    }

    private static void confirmation(Minecraft minecraft, boolean accepted) {
        List<Button> choices = new ArrayList<>();
        for (Object child : minecraft.screen.children()) {
            if (child instanceof Button) choices.add((Button) child);
        }
        choices.get(accepted ? 0 : 1).onPress();
    }

    private static Button button(Screen screen, int id) {
        List<Button> buttons = new ArrayList<>();
        for (Object child : screen.children()) {
            if (child instanceof Button) buttons.add((Button) child);
        }
        if (screen instanceof BaseMetalsConfigScreen) {
            int index = id == 20 ? 0 : id >= 10 ? id - 9 : id == 2 ? 5 : id == 3 ? 6 : id == 0 ? 7 : 8;
            return buttons.get(index);
        }
        String label = id == 20 ? "Config" : I18n.get("gui.done");
        return buttons.stream().filter(button -> button.getMessage().getString().equals(label)).findFirst()
                .orElseThrow(() -> new IllegalStateException("Missing config button " + id));
    }

    private static void click(Screen screen, int id) {
        button(screen, id).onPress();
    }

    private static void validateWallModels(Minecraft minecraft) {
        int states = 0;
        for (Block block : ForgeRegistries.BLOCKS) {
            if (!(block instanceof net.minecraft.world.level.block.WallBlock)
                    || !"basemetals".equals(block.getRegistryName().getNamespace())) continue;

            for (net.minecraft.world.level.block.state.BlockState state : block.getStateDefinition().getPossibleStates()) {
                BakedModel model = minecraft.getBlockRenderer().getBlockModel(state);
                require(!"missingno".equals(model.getParticleIcon().getName().getPath()),
                        "wall model missing: " + state);
                List<BakedQuad> quads = new ArrayList<>(model.getQuads(state, null, new Random(0)));
                for (Direction direction : Direction.values()) {
                    quads.addAll(model.getQuads(state, direction, new Random(0)));
                }
                for (BakedQuad quad : quads) {
                    require(!"missingno".equals(quad.getSprite().getName().getPath()),
                            "wall side texture missing: " + state);
                }
                states++;
            }
        }
        require(states == 27 * 324, "wall model state count: " + states);
        LOGGER.info("BASEMETALS_WALL_MODEL_PROBE PASS states={}", states);
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
            require(ItemBlockRenderTypes.canRenderInLayer(block.defaultBlockState(), RenderType.cutoutMipped()),
                    name + " is not cutout-mipped");
            String overlay = "basemetals:block/ore_overlays/" + name;

            BakedModel blockModel = minecraft.getBlockRenderer().getBlockModel(block.defaultBlockState());
            validateOreModel(blockModel, block, name, host, overlay);
            ItemStack stack = new ItemStack(ModContent.item(name).get());
            BakedModel itemModel = minecraft.getItemRenderer().getModel(stack, null, null, 0);
            validateOreModel(itemModel, null, name + " item", host, overlay);
        }

        require(ores == 13, "overlay ore count");
        validateHostOverride(minecraft, "stone", "tin");
        validateHostOverride(minecraft, "netherrack", "coldiron");
        validateHostOverride(minecraft, "end_stone", "starsteel");
        LOGGER.info("BASEMETALS_ORE_OVERLAY_PROBE PASS ores={} host_overrides=3 offset_model_units=0.05",
                Integer.valueOf(ores));
    }

    private static void validateOreModel(BakedModel model, Block block, String name, String host, String overlay) {
        require(host.equals(model.getParticleIcon().getName().toString()), name + " particle host");
        Random random = new Random(42L);
        require(model.getQuads(block == null ? null : block.defaultBlockState(), null, random).isEmpty(),
                name + " has unexpected unculled faces");

        for (Direction side : Direction.values()) {
            List<BakedQuad> quads = model.getQuads(block == null ? null : block.defaultBlockState(), side, random);
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
                            if (quad.getSprite().isTransparent(0, x, y)) transparent = true;
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
        int[] vertices = quad.getVertices();
        int stride = vertices.length / 4;
        float low = overlay ? -0.05F / 16 : 0;
        float high = overlay ? 16.05F / 16 : 1;

        for (int vertex = 0; vertex < 4; vertex++) {
            for (int axis = 0; axis < 3; axis++) {
                float coordinate = Float.intBitsToFloat(vertices[vertex * stride + axis]);
                require(Math.min(Math.abs(coordinate - low), Math.abs(coordinate - high)) < 0.000001F,
                        name + " incorrect " + (overlay ? "overlay" : "base") + " vertex");
            }
        }
    }

    private static void validateHostOverride(Minecraft minecraft, String host, String source) throws IOException {
        try (Resource replacement = minecraft.getResourceManager().getResource(
                    new ResourceLocation("minecraft", "textures/block/" + host + ".png"));
                Resource fixture = minecraft.getResourceManager().getResource(
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

    private static void validateCreativeBuckets(Minecraft minecraft) {
        net.minecraft.core.NonNullList<ItemStack> entries = net.minecraft.core.NonNullList.create();
        ModTabs.ITEMS.fillItemList(entries);
        List<String> missingFromTab = new ArrayList<>();
        List<String> missingFromSearch = new ArrayList<>();

        for (FluidContent fluid : ModContent.fluids().values()) {
            net.minecraft.world.item.Item bucket = fluid.bucket().get();
            String id = bucket.getRegistryName().toString();
            if (entries.stream().noneMatch(stack -> stack.getItem() == bucket)) missingFromTab.add(id);
            if (minecraft.getSearchTree(net.minecraft.client.searchtree.SearchRegistry.CREATIVE_NAMES)
                    .search(id).stream().noneMatch(stack -> stack.getItem() == bucket)) {
                missingFromSearch.add(id);
            }
        }

        LOGGER.info("BASEMETALS_CREATIVE_BUCKET_PROBE missing_tab={} missing_search={}",
                missingFromTab, missingFromSearch);
        require(missingFromTab.isEmpty(), "buckets missing from Base Metals item tab: " + missingFromTab);
        require(missingFromSearch.isEmpty(), "buckets missing from creative search: " + missingFromSearch);
    }

    private static void validateCreativeBucketScreen(Minecraft minecraft) {
        net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen screen =
                new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(minecraft.player);
        minecraft.setScreen(screen);

        try {
            java.lang.reflect.Method selectTab = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(
                    net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class,
                    "m_98560_", net.minecraft.world.item.CreativeModeTab.class);
            java.lang.reflect.Method refreshSearch = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findMethod(
                    net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class, "m_98630_");
            java.lang.reflect.Field searchField = net.minecraftforge.fml.util.ObfuscationReflectionHelper.findField(
                    net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.class, "f_98510_");

            selectTab.invoke(screen, ModTabs.ITEMS);
            requireCreativeBuckets(screen.getMenu().items, "Base Metals Items screen");
            for (String name : ModContent.itemsById().keySet()) {
                if (!name.endsWith("_raw")) continue;

                require(screen.getMenu().items.stream().anyMatch(stack ->
                        stack.getItem() == ModContent.item(name).get()),
                        "Base Metals Items screen is missing " + name);
            }
            selectTab.invoke(screen, net.minecraft.world.item.CreativeModeTab.TAB_SEARCH);

            net.minecraft.client.gui.components.EditBox search =
                    (net.minecraft.client.gui.components.EditBox) searchField.get(screen);
            search.setValue("bucket");
            refreshSearch.invoke(screen);
            requireCreativeBuckets(screen.getMenu().items, "creative search for bucket");

            search.setValue("mercury");
            refreshSearch.invoke(screen);
            require(screen.getMenu().items.stream().anyMatch(stack ->
                    stack.getItem() == ModContent.item("mercury_bucket").get()),
                    "mercury bucket missing from creative search for mercury");
            LOGGER.info("BASEMETALS_CREATIVE_BUCKET_SCREEN PASS buckets={} search=bucket,mercury",
                    ModContent.fluids().size());
            if (Boolean.getBoolean("basemetalsclientprobe.captureCreativeBuckets")) {
                search.setValue("bucket");
                refreshSearch.invoke(screen);
                instance.pendingCreativeBucketScreenshot = true;
            }
        } catch (ReflectiveOperationException failure) {
            throw new IllegalStateException("Could not test the creative inventory screen", failure);
        } finally {
            if (!instance.pendingCreativeBucketScreenshot) minecraft.setScreen(null);
        }
    }

    private static void requireCreativeBuckets(List<ItemStack> entries, String label) {
        for (FluidContent fluid : ModContent.fluids().values()) {
            net.minecraft.world.item.Item bucket = fluid.bucket().get();
            require(entries.stream().anyMatch(stack -> stack.getItem() == bucket),
                    label + " is missing " + bucket.getRegistryName());
        }
    }

    private static void validateBucketModel(Minecraft minecraft, String name) {
        ItemStack stack = new ItemStack(ModContent.item(name).get());
        BakedModel model = minecraft.getItemRenderer().getModel(stack, null, null, 0);
        String fluidName = name.substring(0, name.length() - "_bucket".length());
        FluidContent fluid = ModContent.fluids().get(fluidName);
        require(fluid != null, name + " has no matching fluid content");

        ResourceLocation texture = fluid.source().get().getAttributes().getStillTexture();
        int colour = fluid.source().get().getAttributes().getColor();
        int vertexColour = (colour & 0xFF00FF00) | ((colour & 0xFF) << 16) | ((colour >>> 16) & 0xFF);
        Random random = new Random(42L);
        List<BakedQuad> quads = new ArrayList<BakedQuad>(model.getQuads(null, null, random));
        for (net.minecraft.core.Direction side : net.minecraft.core.Direction.values()) {
            quads.addAll(model.getQuads(null, side, random));
        }
        require(!quads.isEmpty(), name + " has no rendered quads");
        boolean hasFluidTexture = false;
        for (BakedQuad quad : quads) {
            require(!"missingno".equals(quad.getSprite().getName().getPath()),
                    name + " uses the missing-texture sprite");
            if (texture.equals(quad.getSprite().getName())) {
                hasFluidTexture = true;
                require(quad.getVertices()[3] == vertexColour,
                        name + " does not bake its fluid colour into the model");
            }
        }
        require(hasFluidTexture, name + " does not render its actual fluid texture");
        require(minecraft.getItemColors().getColor(stack, 1) == -1,
                name + " applies its fluid colour twice");
        require(stack.getHoverName().getString().equals(I18n.get("item.bucket." + fluidName)),
                name + " does not describe its contents");
        LOGGER.info("BASEMETALS_BUCKET_MODEL PASS item={} texture={} name={}",
                name, texture, stack.getHoverName().getString());
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }

    private void writeMarker() throws IOException {
        Properties values = new Properties();
        values.setProperty("content_verified", "true");
        values.setProperty("integrated_world_rendered", Boolean.toString(renderedFrames >= 8));
        values.setProperty("integrated_world_reloaded", Boolean.toString(
                Boolean.getBoolean("basemetalsclientprobe.reload")));
        values.setProperty("rendered_frames", Integer.toString(renderedFrames));
        try (FileOutputStream output = new FileOutputStream(new File("client-smoke-pass.properties"))) {
            values.store(output, "Base Metals Forge 1.16.5 packaged-client gate");
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
        minecraft.stop();
        throw new IllegalStateException(message);
    }
}
