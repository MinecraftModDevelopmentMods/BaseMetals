package zone.moddev.mc.basemetals.smoke;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.client.MaterialProjectileRenderer;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Development-only proof that the real client can create, join, tick, and
 * cleanly stop an integrated server with Base Metals and OreSpawn.
 */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, value = Dist.CLIENT,
        bus = Mod.EventBusSubscriber.Bus.MOD)
public final class IntegratedWorldSmoke {
    private static final String ENABLED_PROPERTY = "basemetals.integratedWorldSmoke";
    private static final String WORLD_PROPERTY = "basemetals.integratedWorldSmokeName";
    private static final int READY_TICKS = 200;
    private static final int TIMEOUT_TICKS = 6000;

    private static int elapsedTicks;
    private static int joinedTicks;
    private static boolean creationStarted;
    private static boolean completed;
    private static boolean renderLayersChecked;
    private static boolean creativeBucketsChecked;
    private static boolean projectileRenderersChecked;
    private static boolean projectileSyncProbesSpawned;
    private static boolean projectileSyncChecked;
    private static boolean registered;

    private IntegratedWorldSmoke() {}

    @SubscribeEvent
    public static void setup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
        register();
    }

    public static void register() {
        if (!registered) {
            TickEvent.ClientTickEvent.Post.BUS.addListener(IntegratedWorldSmoke::onClientTick);
            registered = true;
        }
    }

    public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
        if (completed
                || !Boolean.getBoolean(ENABLED_PROPERTY)) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.options.pauseOnLostFocus = false;
        if (minecraft.screen instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen) {
            minecraft.screen.onClose();
            return;
        }
        elapsedTicks++;
        if (elapsedTicks == 1) {
            String expectedMods = System.getProperty("basemetals.expectedSmokeMods", "");
            if (!expectedMods.isEmpty()) {
                var expected = new java.util.TreeSet<>(java.util.List.of(expectedMods.split(",")));
                var actual = new java.util.TreeSet<>(net.minecraftforge.fml.ModList.get().getMods().stream()
                        .map(net.minecraftforge.forgespi.language.IModInfo::getModId).toList());
                boolean allowDependencies = Boolean.getBoolean("basemetals.allowExtraSmokeMods");
                if (allowDependencies ? !actual.containsAll(expected) : !actual.equals(expected)) {
                    throw new IllegalStateException("Client loaded " + actual + "; expected " + expected);
                }
                BaseMetals.LOGGER.info("BASEMETALS_CLIENT_PROFILE PASS mods={}", actual);
            }
        }
        if (!renderLayersChecked && minecraft.screen instanceof TitleScreen) {
            verifyBlockRenderLayers(minecraft);
            renderLayersChecked = true;
        }
        if (!creationStarted && elapsedTicks % 200 == 0) {
            BaseMetals.LOGGER.info("BASEMETALS_INTEGRATED_SMOKE waiting for title screen current={} overlay={}",
                    minecraft.screen == null ? "none" : minecraft.screen.getClass().getName(),
                    minecraft.getOverlay() == null ? "none" : minecraft.getOverlay().getClass().getName());
        }
        if (!creationStarted && minecraft.screen instanceof TitleScreen && minecraft.getOverlay() == null) {
            ConfigScreenSmoke.verify(minecraft, minecraft.screen);
            verifyOreOverlayModels(minecraft);
            verifyBarsModels(minecraft);
            creationStarted = true;
            String worldName = System.getProperty(WORLD_PROPERTY, "basemetals-integrated-smoke");
            LevelSettings levelSettings = new LevelSettings(
                    "Base Metals Integrated Smoke",
                    GameType.CREATIVE,
                    false,
                    Difficulty.NORMAL,
                    true,
                    new GameRules(net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS),
                    net.minecraft.world.level.WorldDataConfiguration.DEFAULT);
            BaseMetals.LOGGER.info("BASEMETALS_INTEGRATED_SMOKE creating world={}", worldName);
            minecraft.createWorldOpenFlows().createFreshLevel(worldName, levelSettings,
                    new net.minecraft.world.level.levelgen.WorldOptions(0L, true, false),
                    net.minecraft.world.level.levelgen.presets.WorldPresets::createNormalWorldDimensions, minecraft.screen);
        }

        if (creationStarted && minecraft.screen instanceof net.minecraft.client.gui.screens.ConfirmScreen) {
            String title = minecraft.screen.getTitle().getString();
            if (!title.toLowerCase(java.util.Locale.ROOT).contains("experimental")) {
                throw new IllegalStateException("Unexpected confirmation in the disposable world: " + title);
            }
            // Forge marks modded registries experimental. This applies only to the new smoke-test world.
            minecraft.screen.children().stream().filter(net.minecraft.client.gui.components.Button.class::isInstance)
                    .map(net.minecraft.client.gui.components.Button.class::cast).findFirst().orElseThrow().onPress(new net.minecraft.client.input.KeyEvent(257, 0, 0));
        }

        if (minecraft.hasSingleplayerServer() && minecraft.getSingleplayerServer() != null
                && minecraft.level != null && minecraft.player != null) {
            ServerLevel serverLevel = minecraft.getSingleplayerServer().getLevel(minecraft.level.dimension());
            if (serverLevel != null && serverLevel.getServer().isRunning()) {
                if (!creativeBucketsChecked) {
                    // Other mods may use server settings while building creative tooltips.
                    verifyCreativeBuckets(minecraft);
                    creativeBucketsChecked = true;
                }
                if (!projectileRenderersChecked) {
                    verifyProjectileRenderers(minecraft);
                    projectileRenderersChecked = true;
                }
                if (!projectileSyncProbesSpawned && !serverLevel.players().isEmpty()) {
                    spawnProjectileSyncProbes(serverLevel);
                    projectileSyncProbesSpawned = true;
                }
                joinedTicks++;
                if (projectileSyncProbesSpawned && !projectileSyncChecked && joinedTicks >= 20) {
                    projectileSyncChecked = verifyProjectileSync(minecraft);
                    if (!projectileSyncChecked && joinedTicks >= 100) {
                        throw new IllegalStateException(
                                "BASEMETALS_INTEGRATED_SMOKE FAIL: projectile ammunition did not reach the client");
                    }
                }
                if (joinedTicks >= READY_TICKS
                        && minecraft.getSingleplayerServer().getTickCount() >= READY_TICKS) {
                    if (!projectileSyncChecked) {
                        throw new IllegalStateException(
                                "BASEMETALS_INTEGRATED_SMOKE FAIL: projectile sync was never verified");
                    }
                    completed = true;
                    BaseMetals.LOGGER.info(
                            "BASEMETALS_INTEGRATED_SMOKE PASS world={} dimension={} client_ticks={} server_ticks={}",
                            System.getProperty(WORLD_PROPERTY, "basemetals-integrated-smoke"),
                            minecraft.level.dimension().identifier(), elapsedTicks,
                            minecraft.getSingleplayerServer().getTickCount());
                    minecraft.stop();
                    return;
                }
            }
        }

        if (elapsedTicks >= TIMEOUT_TICKS) {
            completed = true;
            throw new IllegalStateException("BASEMETALS_INTEGRATED_SMOKE FAIL: timed out before a stable integrated world");
        }
    }

    private static java.util.List<net.minecraft.client.renderer.block.model.BakedQuad> quads(
            net.minecraft.client.renderer.block.model.BlockStateModel model, net.minecraft.core.Direction face) {
        var result = new java.util.ArrayList<net.minecraft.client.renderer.block.model.BakedQuad>();
        for (var part : model.collectParts(net.minecraft.util.RandomSource.create(0))) {
            result.addAll(part.getQuads(face));
        }

        return result;
    }

    private static int[] vertices(net.minecraft.client.renderer.block.model.BakedQuad quad) {
        int[] result = new int[32];
        for (int vertex = 0; vertex < 4; vertex++) {
            var position = quad.position(vertex);
            result[vertex * 8] = Float.floatToRawIntBits(position.x());
            result[vertex * 8 + 1] = Float.floatToRawIntBits(position.y());
            result[vertex * 8 + 2] = Float.floatToRawIntBits(position.z());
        }

        return result;
    }

    private static void verifyOreOverlayModels(Minecraft minecraft) {
        int ores = 0;
        for (var entry : ModContent.blocksById().entrySet()) {
            if (!entry.getKey().endsWith("_ore")) continue;
            var state = entry.getValue().get().defaultBlockState();
            var model = minecraft.getBlockRenderer().getBlockModel(state);
            for (var face : net.minecraft.core.Direction.values()) {
                var quads = quads(model, face);
                if (quads.size() != 2) throw new IllegalStateException("Ore model lost a layer: " + entry.getKey());
                boolean offset = quads.stream().anyMatch(quad -> {
                    int[] vertices = vertices(quad);
                    for (int index = 0; index < vertices.length; index += 8) {
                        for (int axis = 0; axis < 3; axis++) {
                            float coordinate = Float.intBitsToFloat(vertices[index + axis]);
                            if (coordinate < -0.003F || coordinate > 1.003F) return true;
                        }
                    }
                    return false;
                });
                if (!offset) throw new IllegalStateException("Ore overlay is flush with its host: " + entry.getKey());
            }
            var layers = model.getRenderTypes(state, net.minecraft.util.RandomSource.create(0),
                    net.minecraftforge.client.model.data.ModelData.EMPTY);
            if (!layers.contains(ChunkSectionLayer.CUTOUT) || layers.contains(ChunkSectionLayer.SOLID)) {
                throw new IllegalStateException("Ore overlay is not cutout: " + entry.getKey());
            }
            ores++;
        }
        if (ores != 13) throw new IllegalStateException("Expected 13 ore models, found " + ores);
        if (Boolean.getBoolean("basemetals.hostTextureProbe")) {
            try {
                var host = minecraft.getResourceManager().getResource(
                        net.minecraft.resources.Identifier.parse("minecraft:textures/block/stone.png"));
                if (!host.orElseThrow().sourcePackId().contains("basemetals-host-test")) {
                    throw new IllegalStateException("The ore host texture did not come from the replacement pack");
                }
            } catch (java.util.NoSuchElementException exception) {
                throw new IllegalStateException("The replacement ore host texture is missing", exception);
            }
            BaseMetals.LOGGER.info("BASEMETALS_OVERLAY_HOST_PACK PASS vanilla_host=stone");
        }
        BaseMetals.LOGGER.info("BASEMETALS_OVERLAY_MODELS PASS ores={} model_offset=0.05", ores);
    }

    private static void verifyBarsModels(Minecraft minecraft) {
        int patterns = 0;
        for (var entry : ModContent.blocksById().entrySet()) {
            if (!(entry.getValue().get() instanceof IronBarsBlock bars)) continue;

            for (var state : bars.getStateDefinition().getPossibleStates()) {
                if (state.getValue(net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED)) {
                    continue;
                }

                var model = minecraft.getBlockRenderer().getBlockModel(state);
                var quads = new java.util.ArrayList<>(quads(model, null));
                for (var face : net.minecraft.core.Direction.values()) {
                    quads.addAll(quads(model, face));
                }

                for (int first = 0; first < quads.size(); first++) {
                    for (int second = first + 1; second < quads.size(); second++) {
                        var a = quads.get(first);
                        var b = quads.get(second);
                        if (a.direction() != b.direction()) continue;

                        int axis = switch (a.direction().getAxis()) {
                            case X -> 0;
                            case Y -> 1;
                            case Z -> 2;
                        };
                        float[][] aBounds = quadBounds(vertices(a));
                        float[][] bBounds = quadBounds(vertices(b));
                        if (Math.abs(aBounds[0][axis] - bBounds[0][axis]) > 0.01F / 16) continue;

                        boolean overlaps = true;
                        for (int dimension = 0; dimension < 3; dimension++) {
                            if (dimension == axis) continue;
                            if (Math.min(aBounds[1][dimension], bBounds[1][dimension])
                                    - Math.max(aBounds[0][dimension], bBounds[0][dimension]) < 0.000001F) {
                                overlaps = false;
                            }
                        }

                        if (overlaps) {
                            throw new IllegalStateException("Overlapping baked bar faces: " + state);
                        }
                    }
                }
                patterns++;
            }
        }

        if (patterns != 416) throw new IllegalStateException("Expected 416 bar patterns, found " + patterns);
        BaseMetals.LOGGER.info("BASEMETALS_BARS_MODELS PASS materials=26 connection_patterns={}", patterns);
    }

    private static float[][] quadBounds(int[] vertices) {
        float[][] bounds = {{Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY},
                {Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY}};

        for (int vertex = 0; vertex < vertices.length; vertex += 8) {
            for (int axis = 0; axis < 3; axis++) {
                float coordinate = Float.intBitsToFloat(vertices[vertex + axis]);
                bounds[0][axis] = Math.min(bounds[0][axis], coordinate);
                bounds[1][axis] = Math.max(bounds[1][axis], coordinate);
            }
        }

        return bounds;
    }

    private static void verifyBlockRenderLayers(Minecraft minecraft) {
        int bars = 0;
        int doors = 0;
        int trapdoors = 0;
        for (var reference : ModContent.blocksById().values()) {
            Block block = reference.get();
            ChunkSectionLayer expected = null;
            if (block instanceof IronBarsBlock) {
                expected = ChunkSectionLayer.CUTOUT;
                bars++;
            } else if (block instanceof DoorBlock) {
                expected = ChunkSectionLayer.CUTOUT;
                doors++;
            } else if (block instanceof TrapDoorBlock) {
                expected = ChunkSectionLayer.CUTOUT;
                trapdoors++;
            }
            var state = block.defaultBlockState();
            var layers = minecraft.getBlockRenderer().getBlockModel(state).getRenderTypes(state,
                    net.minecraft.util.RandomSource.create(0), net.minecraftforge.client.model.data.ModelData.EMPTY);
            if (expected != null && (!layers.contains(expected) || layers.contains(ChunkSectionLayer.SOLID))) {
                throw new IllegalStateException("BASEMETALS_INTEGRATED_SMOKE FAIL: wrong render layer for "
                        + net.minecraftforge.registries.ForgeRegistries.BLOCKS.getKey(block));
            }
        }
        if (bars != 26 || doors != 26 || trapdoors != 26) {
            throw new IllegalStateException(
                    "BASEMETALS_INTEGRATED_SMOKE FAIL: incomplete cutout family bars=" + bars
                    + " doors=" + doors + " trapdoors=" + trapdoors);
        }
        BaseMetals.LOGGER.info(
                "BASEMETALS_INTEGRATED_SMOKE render layers PASS bars={} doors={} trapdoors={}",
                bars, doors, trapdoors);
    }

    private static void verifyCreativeBuckets(Minecraft minecraft) {
        NonNullList<ItemStack> itemsTab = NonNullList.create();
        NonNullList<ItemStack> searchTab = NonNullList.create();
        var previousMenu = minecraft.player.containerMenu;
        try {
            // Opening the native inventory also builds this connection's search index.
            new net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen(
                    minecraft.player, minecraft.level.enabledFeatures(), true);
        } finally {
            minecraft.player.containerMenu = previousMenu;
        }
        itemsTab.addAll(ModTabs.ITEMS.get().getDisplayItems());
        searchTab.addAll(net.minecraft.world.item.CreativeModeTabs.searchTab().getDisplayItems());
        var shown = new java.util.HashSet<net.minecraft.world.item.Item>();
        for (var tab : java.util.List.of(ModTabs.BLOCKS.get(), ModTabs.ITEMS.get(), ModTabs.TOOLS.get(), ModTabs.COMBAT.get())) {
            tab.getDisplayItems().forEach(stack -> shown.add(stack.getItem()));
        }
        var indexed = minecraft.getConnection().searchTrees().creativeNameSearch().search("basemetals:")
                .stream().map(ItemStack::getItem).collect(java.util.stream.Collectors.toSet());
        ModContent.itemsById().forEach((id, item) -> {
            boolean expected = !ModContent.fluids().containsKey(id);
            if (shown.contains(item.get()) != expected || indexed.contains(item.get()) != expected) {
                throw new IllegalStateException("Creative visibility changed for " + id
                        + ": tabs=" + shown.contains(item.get()) + ", search=" + indexed.contains(item.get())
                        + ", expected=" + expected);
            }
        });
        BaseMetals.LOGGER.info("BASEMETALS_CREATIVE_INVENTORY PASS visible={} hidden_fluid_blocks=36", shown.size());
        verifyItemModels(minecraft);
        java.util.List<ItemStack> bucketSearch = minecraft.getConnection().searchTrees()
                .creativeNameSearch().search("bucket");
        for (var fluid : ModContent.fluids().entrySet()) {
            net.minecraft.world.item.Item bucket = fluid.getValue().bucket().get();
            ItemStack bucketStack = bucket.getDefaultInstance();
            if (itemsTab.stream().noneMatch(stack -> stack.is(bucket))
                    || searchTab.stream().noneMatch(stack -> stack.is(bucket))
                    || bucketSearch.stream().noneMatch(stack -> stack.is(bucket))) {
                throw new IllegalStateException(
                        "BASEMETALS_INTEGRATED_SMOKE FAIL: hidden creative bucket "
                                + fluid.getKey() + " direct_items=" + itemsTab.size()
                                + " direct_search=" + searchTab.size()
                                + " indexed_bucket_results=" + bucketSearch.size());
            }
            var model = new net.minecraft.client.renderer.item.ItemStackRenderState();
            minecraft.getItemModelResolver().updateForTopItem(model, bucketStack,
                    net.minecraft.world.item.ItemDisplayContext.GUI, minecraft.level, null, 0);
            if (model.isEmpty() || model.getModelBoundingBox().getSize() == 0) {
                throw new IllegalStateException(
                        "BASEMETALS_INTEGRATED_SMOKE FAIL: invisible bucket model " + fluid.getKey());
            }

            var quads = itemQuads(model);
            var still = net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions
                    .of(fluid.getValue().source().get()).getStillTexture();
            if (quads.stream().noneMatch(quad -> quad.tintIndex() == 1
                    && quad.sprite().contents().name().equals(still))) {
                throw new IllegalStateException("Bucket has no tinted fluid layer: " + fluid.getKey());
            }
        }
        BaseMetals.LOGGER.info(
                "BASEMETALS_INTEGRATED_SMOKE creative buckets PASS count={}",
                ModContent.fluids().size());
    }

    private static void verifyItemModels(Minecraft minecraft) {
        for (var entry : ModContent.itemsById().entrySet()) {
            var state = new net.minecraft.client.renderer.item.ItemStackRenderState();
            minecraft.getItemModelResolver().updateForTopItem(state,
                    entry.getValue().get().getDefaultInstance(),
                    net.minecraft.world.item.ItemDisplayContext.GUI, minecraft.level, null, 0);
            var quads = itemQuads(state);

            if (state.isEmpty() || quads.isEmpty() || quads.stream().anyMatch(quad ->
                    quad.sprite().contents().name().getPath().equals("missingno"))) {
                throw new IllegalStateException("Missing item model or texture: " + entry.getKey());
            }
        }

        BaseMetals.LOGGER.info("BASEMETALS_ITEM_MODELS PASS items={}", ModContent.itemsById().size());
    }

    private static java.util.List<net.minecraft.client.renderer.block.model.BakedQuad> itemQuads(
            net.minecraft.client.renderer.item.ItemStackRenderState state) {
        try {
            var layersField = state.getClass().getDeclaredField("layers");
            var countField = state.getClass().getDeclaredField("activeLayerCount");
            layersField.setAccessible(true);
            countField.setAccessible(true);
            var layers = (net.minecraft.client.renderer.item.ItemStackRenderState.LayerRenderState[])
                    layersField.get(state);
            var result = new java.util.ArrayList<net.minecraft.client.renderer.block.model.BakedQuad>();

            for (int index = 0; index < countField.getInt(state); index++) {
                result.addAll(layers[index].prepareQuadList());
            }

            return result;
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not inspect the native item render state", exception);
        }
    }

    private static void verifyProjectileRenderers(Minecraft minecraft) {
        for (var type : java.util.List.of(ModEntities.CUSTOM_ARROW.get(), ModEntities.CUSTOM_BOLT.get())) {
            MaterialProjectile projectile = new MaterialProjectile(type, minecraft.level);
            if (!(minecraft.getEntityRenderDispatcher().getRenderer(projectile)
                    instanceof MaterialProjectileRenderer)) {
                throw new IllegalStateException(
                        "BASEMETALS_INTEGRATED_SMOKE FAIL: projectile still uses a billboard renderer "
                                + net.minecraftforge.registries.ForgeRegistries.ENTITY_TYPES.getKey(type));
            }
        }
        BaseMetals.LOGGER.info("BASEMETALS_INTEGRATED_SMOKE projectile renderers PASS");
    }

    private static void spawnProjectileSyncProbes(ServerLevel level) {
        var player = level.players().get(0);
        level.getServer().execute(() -> {
            MaterialProjectile arrow = new MaterialProjectile(ModEntities.CUSTOM_ARROW.get(), level, player,
                    ModContent.item("starsteel_arrow").get().getDefaultInstance());
            ((Entity) arrow).setNoGravity(true);
            ((Entity) arrow).setDeltaMovement(0.0D, 0.0D, 0.0D);
            level.addFreshEntity(arrow);

            MaterialProjectile bolt = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), level, player,
                    ModContent.item("tin_bolt").get().getDefaultInstance());
            ((Entity) bolt).setNoGravity(true);
            ((Entity) bolt).setDeltaMovement(0.0D, 0.0D, 0.0D);
            level.addFreshEntity(bolt);
        });
    }

    private static boolean verifyProjectileSync(Minecraft minecraft) {
        var projectiles = minecraft.level.getEntitiesOfClass(MaterialProjectile.class,
                minecraft.player.getBoundingBox().inflate(16.0D));
        boolean arrow = projectiles.stream().anyMatch(projectile ->
                ((Entity) projectile).getType() == ModEntities.CUSTOM_ARROW.get()
                        && ((ItemSupplier) projectile).getItem().is(ModContent.item("starsteel_arrow").get()));
        boolean bolt = projectiles.stream().anyMatch(projectile ->
                ((Entity) projectile).getType() == ModEntities.CUSTOM_BOLT.get()
                        && ((ItemSupplier) projectile).getItem().is(ModContent.item("tin_bolt").get()));
        if (arrow && bolt) {
            BaseMetals.LOGGER.info(
                    "BASEMETALS_INTEGRATED_SMOKE projectile ammunition sync PASS");
            return true;
        }
        return false;
    }
}
