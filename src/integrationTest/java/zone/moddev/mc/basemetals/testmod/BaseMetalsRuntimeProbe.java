package zone.moddev.mc.basemetals.testmod;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.BaseMetalAmmoItem;
import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.PlateBlock;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.NbtIo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.phys.AABB;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.fmlserverevents.FMLServerStartedEvent;
import net.minecraftforge.registries.ForgeRegistries;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Isolated runtime assertions; this class is never part of the public JAR. */
@Mod(BaseMetalsRuntimeProbe.MODID)
public final class BaseMetalsRuntimeProbe {
    static final String MODID = "basemetalsprobe";
    private static final Logger LOGGER = LogManager.getLogger(MODID);
    private int checks;
    private MinecraftServer pendingUpgrade;
    private final java.util.Set<net.minecraft.world.level.ChunkPos> equipmentChunks = new java.util.HashSet<>();
    private int upgradeWaitTicks;

    public BaseMetalsRuntimeProbe() {
        MinecraftForge.EVENT_BUS.addListener(this::serverStarted);
        MinecraftForge.EVENT_BUS.addListener(this::serverTick);
        MinecraftForge.EVENT_BUS.addListener(net.minecraftforge.eventbus.api.EventPriority.LOWEST, this::login);
    }

    private void login(net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent event) {
        if (!"login".equals(System.getProperty("basemetalsprobe.mode"))) return;
        event.getPlayer().getInventory().setItem(0,
                new ItemStack(ModContent.item("tin_bow").get()));
        if (Boolean.getBoolean("basemetalsprobe.modeSwitch")) {
            net.minecraft.server.level.ServerPlayer player = (net.minecraft.server.level.ServerPlayer) event.getPlayer();
            if (zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode()
                    == zone.moddev.mc.basemetals.config.ContentMode.LOW_FANTASY) {
                player.getInventory().setItem(1, new ItemStack(ModContent.item("tin_ingot").get()));
                player.getInventory().setItem(2, new ItemStack(ModContent.item("adamantine_ingot").get()));
                // Also save progress earned through the original 1.13 rod criterion.
                player.getInventory().setItem(3, new ItemStack(ModContent.item("steel_rod").get()));
                for (int slot = 1; slot <= 3; slot++) {
                    net.minecraft.advancements.CriteriaTriggers.INVENTORY_CHANGED.trigger(
                            player, player.getInventory(), player.getInventory().getItem(slot));
                }
            }

            for (String name : new String[] {"tin_bow", "steel_bow", "adamantine_bow", "adamantine_crossbow", "adamantine_gear",
                    "adamantine_arrow", "adamantine_rod", "adamantine_pickaxe"}) {
                net.minecraft.advancements.Advancement advancement = player.getServer().getAdvancements()
                        .getAdvancement(new ResourceLocation("basemetals", "recipes/" + name));
                if (!player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                    throw new IllegalStateException("Material discovery did not earn the recipe advancement: " + name);
                }
                Recipe recipe = player.getServer().getRecipeManager().byKey(new ResourceLocation("basemetals", name)).orElse(null);
                if (player.getRecipeBook().contains(recipe) == recipe.isSpecial()) {
                    throw new IllegalStateException("Wrong recipe-book visibility after mode change: " + name);
                }
            }
            LOGGER.info("BASEMETALS_MODE_SWITCH_RECIPE PASS mode={}",
                    zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode());
        }
        LOGGER.info("BASEMETALS_MODE_LOGIN_SERVER PASS mode={}",
                zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode().serializedName());
    }

    private void serverStarted(FMLServerStartedEvent event) {
        MinecraftServer server = event.getServer();
        if ("login".equals(System.getProperty("basemetalsprobe.mode"))) {
            try {
                int count = ContentModeChecks.run(server) + Native117Checks.run(server) + RawOreChecks.run(server);
                LOGGER.info("BASEMETALS_CONTENT_MODE_PROBE PASS mode={} checks={}",
                        zone.moddev.mc.basemetals.config.BaseMetalsConfig.activeMode().serializedName(), count);
            } catch (Exception failure) {
                throw new IllegalStateException(failure);
            }
            return;
        }
        boolean legacyUpgrade = "legacy-upgrade".equals(System.getProperty("basemetalsprobe.mode"));
        if (legacyUpgrade) {
            prepareEquipmentChunks(server);
            pendingUpgrade = server;
            return;
        }
        performServerChecks(server, false);
    }

    private void prepareEquipmentChunks(MinecraftServer server) {
        Path manifestFile = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT)
                .resolve("legacy_registry_manifest_runtime.json");
        if (!Files.isRegularFile(manifestFile)) return;

        try (Reader reader = Files.newBufferedReader(manifestFile, StandardCharsets.UTF_8)) {
            JsonObject manifest = new JsonParser().parse(reader).getAsJsonObject();
            for (JsonElement entry : manifest.getAsJsonArray("items")) {
                JsonObject item = entry.getAsJsonObject();
                if (!item.has("armor_stand")) continue;

                int x = (int) Math.floor(item.get("armor_x").getAsDouble()) >> 4;
                int z = (int) Math.floor(item.get("armor_z").getAsDouble()) >> 4;
                equipmentChunks.add(new net.minecraft.world.level.ChunkPos(x, z));
                server.overworld().setChunkForced(x, z, true);
                server.overworld().getChunk(x, z);
            }
        } catch (IOException failure) {
            throw new IllegalStateException("Cannot read the upgrade equipment manifest", failure);
        }
    }

    private void serverTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || pendingUpgrade == null) return;
        MinecraftServer server = pendingUpgrade;
        if (++upgradeWaitTicks < 20) return;
        boolean loaded = equipmentChunks.stream().allMatch(chunk -> server.overworld().areEntitiesLoaded(chunk.toLong()));
        if (!loaded && upgradeWaitTicks < 200) return;
        pendingUpgrade = null;

        // Entity storage is asynchronous in 1.17. Check equipment only after its chunks have loaded.
        if (!loaded) throw new IllegalStateException("Timed out loading upgrade equipment chunks");
        performServerChecks(server, true);
    }

    private void performServerChecks(MinecraftServer server, boolean legacyUpgrade) {
        try {
            runChecks(server);
            if (Boolean.getBoolean("basemetalsprobe.sampleOres")) checks += OrePlacementChecks.run(server);
            if (legacyUpgrade) {
                verifyLegacyWorld(server);
                // Keep distant fixture entities loaded until their checks and save have finished.
                server.saveAllChunks(true, true, false);
                equipmentChunks.forEach(chunk -> server.overworld().setChunkForced(chunk.x, chunk.z, false));
            }
            if (checks < 32) throw new IllegalStateException("Only ran " + checks + " runtime checks");
            LOGGER.info(legacyUpgrade ? "BASEMETALS_LEGACY_UPGRADE_PROBE PASS checks={}"
                    : "BASEMETALS_RUNTIME_PROBE PASS checks={}", Integer.valueOf(checks));
        } catch (Throwable failure) {
            LOGGER.error(legacyUpgrade ? "BASEMETALS_LEGACY_UPGRADE_PROBE FAIL after {} checks"
                    : "BASEMETALS_RUNTIME_PROBE FAIL after {} checks", Integer.valueOf(checks), failure);
            throw failure instanceof RuntimeException ? (RuntimeException) failure
                    : new IllegalStateException(failure);
        } finally {
            server.halt(false);
        }
    }

    private void verifyLegacyWorld(MinecraftServer server) throws IOException {
        ServerLevel overworld = require(server.getLevel(Level.OVERWORLD), "legacy overworld missing");
        Path root = server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT);
        if (Files.isRegularFile(root.resolve("legacy_registry_manifest_runtime.json"))) {
            verifyLegacyFixture(server, root);
        } else {
            verifyLegacyBlocks(server);
        }
        verifySavedAdvancements(server, overworld, root);
    }

    private void verifySavedAdvancements(MinecraftServer server, ServerLevel world, Path root) throws IOException {
        Path fixtureFile = root.resolve("basemetals_advancement_upgrade_fixture.json");
        if (!Files.isRegularFile(fixtureFile)) return;

        JsonObject fixture;
        try (Reader reader = Files.newBufferedReader(fixtureFile, StandardCharsets.UTF_8)) {
            fixture = new JsonParser().parse(reader).getAsJsonObject();
        }
        FakePlayer player = FakePlayerFactory.get(world,
                new GameProfile(UUID.fromString(fixture.get("uuid").getAsString()), "UpgradeProgress"));
        int retained = 0;

        for (Map.Entry<String, JsonElement> entry : fixture.getAsJsonObject("progress").entrySet()) {
            if ("DataVersion".equals(entry.getKey())) continue;
            net.minecraft.advancements.Advancement advancement = require(server.getAdvancements()
                    .getAdvancement(new ResourceLocation(entry.getKey())), "saved advancement missing: " + entry.getKey());
            net.minecraft.advancements.AdvancementProgress progress = player.getAdvancements().getOrStartProgress(advancement);
            check(progress.isDone(), "saved advancement completion: " + entry.getKey());

            for (Map.Entry<String, JsonElement> savedCriterion : entry.getValue().getAsJsonObject()
                    .getAsJsonObject("criteria").entrySet()) {
                String criterion = savedCriterion.getKey();
                check(progress.getCriterion(criterion) != null
                        && progress.getCriterion(criterion).isDone(),
                        "saved criterion: " + entry.getKey() + "/" + criterion);
                retained++;
            }
        }

        player.getAdvancements().save();
        LOGGER.info("BASEMETALS_UPGRADE_ADVANCEMENTS PASS criteria={}", retained);
    }

    private void verifyLegacyFixture(MinecraftServer server, Path root) throws IOException {
        List<String> failures = new ArrayList<String>();
        int blockMismatches = 0;
        int itemMismatches = 0;
        int checkedStates = 0;
        int checkedItems = 0;
        int checkedArmor = 0;
        int checkedBuckets = 0;
        int checkedPlayerItems = 0;

        boolean source110 = Files.isRegularFile(root.resolve("BASEMETALS_1_10_FIXTURE_COMPLETE.txt"));
        String upgradeMarker = source110 ? "BASEMETALS_1_10_TO_1_17_UPGRADE" : "BASEMETALS_1_12_TO_1_17_UPGRADE";
        if (!source110 && !Files.isRegularFile(root.resolve("BASEMETALS_1_12_FIXTURE_COMPLETE.txt"))) {
            failures.add("missing legacy fixture completion marker");
        }
        if (source110 && !Files.isRegularFile(root.resolve("BASEMETALS_1_10_FIXTURE_RELOADED.txt"))) {
            failures.add("1.10 fixture was not reopened in its source runtime");
        }
        JsonObject manifest;
        try (Reader reader = Files.newBufferedReader(root.resolve("legacy_registry_manifest_runtime.json"),
                StandardCharsets.UTF_8)) {
            manifest = new JsonParser().parse(reader).getAsJsonObject();
        }
        int format = manifest.has("fixture_format") ? manifest.get("fixture_format").getAsInt() : 1;
        if (format < 2) failures.add("fixture format " + format + " does not cover the complete contract");

        for (JsonElement blockElement : manifest.getAsJsonArray("blocks")) {
            JsonObject block = blockElement.getAsJsonObject();
            String expected = currentBlockId(block.get("id").getAsString());
            for (JsonElement stateElement : block.getAsJsonArray("states")) {
                JsonObject oldState = stateElement.getAsJsonObject();
                int dimension = oldState.has("dimension") ? oldState.get("dimension").getAsInt() : 0;
                ServerLevel world = fixtureWorld(server, dimension);
                BlockPos pos = new BlockPos(oldState.get("x").getAsInt(), oldState.get("y").getAsInt(),
                        oldState.get("z").getAsInt());
                BlockState actualState = world.getBlockState(pos);
                ResourceLocation actualId = ForgeRegistries.BLOCKS.getKey(actualState.getBlock());
                int metadata = oldState.has("saved_metadata") ? oldState.get("saved_metadata").getAsInt()
                        : oldState.get("metadata").getAsInt();
                boolean stateMatches = matchesLegacyMetadata(actualState, metadata)
                        && matchesSavedProperties(actualState, oldState);
                if (!expected.equals(String.valueOf(actualId)) || !stateMatches) {
                    if (failures.size() < 20) {
                        failures.add("block dim=" + dimension + " " + pos + " expected " + expected
                            + " metadata=" + metadata
                                + " but found " + actualState);
                    }
                    blockMismatches++;
                }
                checkedStates++;
            }
        }

        ServerLevel overworld = fixtureWorld(server, 0);
        for (JsonElement itemElement : manifest.getAsJsonArray("items")) {
            JsonObject oldItem = itemElement.getAsJsonObject();
            int chestIndex = oldItem.get("chest").getAsInt();
            int slot = oldItem.get("slot").getAsInt();
            BlockPos pos = new BlockPos(oldItem.get("chest_x").getAsInt(),
                    oldItem.get("chest_y").getAsInt(), oldItem.get("chest_z").getAsInt());
            overworld.getBlockState(pos); // Force the legacy container chunk through datafixing before lookup.
            BlockEntity blockEntity = overworld.getBlockEntity(pos);
            if (!(blockEntity instanceof ChestBlockEntity)) {
                if (failures.size() < 20) failures.add("missing inventory chest " + chestIndex + " at " + pos
                        + " state=" + overworld.getBlockState(pos) + " tile="
                        + (blockEntity == null ? "null" : blockEntity.getClass().getName()));
                itemMismatches++;
                continue;
            }
            ItemStack stack = ((ChestBlockEntity) blockEntity).getItem(slot);
            String expected = currentItemId(oldItem.get("id").getAsString());
            String actual = String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem()));
            int expectedCount = oldItem.has("count") ? oldItem.get("count").getAsInt() : 1;
            int expectedDamage = oldItem.has("damage") ? oldItem.get("damage").getAsInt() : 0;
            CompoundTag tag = stack.getTag();
            boolean proofMatches = format < 2 || tag != null
                    && tag.getCompound("basemetals_fixture").getString("proof")
                            .equals(oldItem.get("id").getAsString());
            boolean enchantmentMatches = !oldItem.has("enchanted") || !oldItem.get("enchanted").getAsBoolean()
                    || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack) == 2;
            boolean nameMatches = !oldItem.has("custom_name")
                    || oldItem.get("custom_name").getAsString().equals(stack.getHoverName().getString());
            if (!expected.equals(actual) || stack.getCount() != expectedCount
                    || stack.getDamageValue() != expectedDamage || !proofMatches || !enchantmentMatches || !nameMatches) {
                if (failures.size() < 20) {
                    failures.add("chest " + chestIndex + " slot " + slot + " expected " + expected
                            + " x" + expectedCount + " damage=" + expectedDamage + " with fixture NBT but found "
                            + stack + " damage=" + stack.getDamageValue() + " proof=" + proofMatches
                            + " unbreaking="
                            + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, stack));
                }
                itemMismatches++;
            }
            if (oldItem.has("armor_stand")) {
                BlockPos armorPos = new BlockPos((int) Math.floor(oldItem.get("armor_x").getAsDouble()),
                        (int) Math.floor(oldItem.get("armor_y").getAsDouble()),
                        (int) Math.floor(oldItem.get("armor_z").getAsDouble()));
                overworld.getBlockState(armorPos);
                // Some older fixtures have ordinary gravity, so the stand may fall within its column.
                List<ArmorStand> stands = overworld.getEntitiesOfClass(ArmorStand.class,
                        new AABB(armorPos.getX() - 0.75D, overworld.getMinBuildHeight(), armorPos.getZ() - 0.75D,
                                armorPos.getX() + 1.75D, overworld.getMaxBuildHeight(), armorPos.getZ() + 1.75D));
                EquipmentSlot equipmentSlot = equipmentSlot(oldItem.get("armor_slot").getAsString());
                boolean equipped = false;
                for (ArmorStand stand : stands) {
                    ItemStack worn = stand.getItemBySlot(equipmentSlot);
                    if (expected.equals(String.valueOf(ForgeRegistries.ITEMS.getKey(worn.getItem())))
                            && worn.getDamageValue() == expectedDamage
                            && worn.hasTag() && worn.getTag().getCompound("basemetals_fixture")
                                    .getString("proof").equals(oldItem.get("id").getAsString())
                            && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.UNBREAKING, worn) == 2) {
                        equipped = true;
                        break;
                    }
                }
                if (!equipped) {
                    if (failures.size() < 20) failures.add("missing armor-stand equipment " + expected
                            + " stands=" + stands.size() + " pos=" + armorPos);
                    itemMismatches++;
                }
                checkedArmor++;
            }
            checkedItems++;
        }

        for (JsonElement fluidElement : manifest.getAsJsonArray("fluids")) {
            JsonObject fluid = fluidElement.getAsJsonObject();
            if (!fluid.has("bucket_chest")) continue;
            int chestIndex = fluid.get("bucket_chest").getAsInt();
            int slot = fluid.get("bucket_slot").getAsInt();
            BlockPos pos = new BlockPos(fluid.get("bucket_chest_x").getAsInt(),
                    fluid.get("bucket_chest_y").getAsInt(), fluid.get("bucket_chest_z").getAsInt());
            overworld.getBlockState(pos); // Load the chunk before looking up its tile entity.
            BlockEntity blockEntity = overworld.getBlockEntity(pos);
            String expected = BaseMetals.MOD_ID + ":"
                    + MissingMappings.fluidTargetPath(fluid.get("name").getAsString()) + "_bucket";
            if (!(blockEntity instanceof ChestBlockEntity)
                    || !expected.equals(String.valueOf(ForgeRegistries.ITEMS.getKey(
                            ((ChestBlockEntity) blockEntity).getItem(slot).getItem())))) {
                if (failures.size() < 20) {
                    failures.add("legacy filled bucket " + chestIndex + ":" + slot
                            + " did not become " + expected);
                }
                itemMismatches++;
            }
            checkedBuckets++;
        }

        if (manifest.has("players")) {
            for (JsonElement playerElement : manifest.getAsJsonArray("players")) {
                JsonObject savedPlayer = playerElement.getAsJsonObject();
                File playerFile = root.resolve("playerdata")
                        .resolve(savedPlayer.get("uuid").getAsString() + ".dat").toFile();
                if (!playerFile.isFile()) {
                    failures.add("fixture playerdata did not load: " + playerFile.getName());
                    continue;
                }
                CompoundTag playerData;
                try (FileInputStream input = new FileInputStream(playerFile)) {
                    playerData = NbtIo.readCompressed(input);
                }
                ListTag inventory = playerData.getList("Inventory", 10);
                for (JsonElement inventoryElement : savedPlayer.getAsJsonArray("inventory")) {
                    JsonObject expectedItem = inventoryElement.getAsJsonObject();
                    int slot = expectedItem.get("slot").getAsInt();
                    CompoundTag actualItem = inventoryItem(inventory, slot);
                    String expected = currentItemId(expectedItem.get("id").getAsString());
                    boolean proof = actualItem != null && actualItem.getCompound("tag")
                            .getCompound("basemetals_fixture").getString("player_proof")
                            .equals(expectedItem.get("id").getAsString());
                    if (actualItem == null || !expected.equals(actualItem.getString("id")) || !proof) {
                        if (failures.size() < 20) failures.add("player slot " + slot + " lost " + expected);
                        itemMismatches++;
                    }
                    checkedPlayerItems++;
                }
            }
        }

        if (!source110 && !Files.isRegularFile(root.resolve("legacy_orespawn3_basemetals.json"))) {
            failures.add("missing packaged Base Metals OS3 rule fixture");
        }
        if (!source110 && !Files.isRegularFile(root.resolve("legacy_orespawn3_orespawn.json"))) {
            failures.add("missing configured OS3 rule fixture");
        }

        String summary = "states=" + checkedStates + " block_mismatches=" + blockMismatches
                + " items=" + checkedItems + " armor=" + checkedArmor + " buckets=" + checkedBuckets
                + " player_items=" + checkedPlayerItems + " item_mismatches=" + itemMismatches;
        checks += checkedStates + checkedItems + checkedArmor + checkedBuckets + checkedPlayerItems;
        if (blockMismatches != 0 || itemMismatches != 0 || !failures.isEmpty()) {
            writeFixtureResult(root, upgradeMarker + " FAIL " + summary
                    + " samples=" + failures);
            throw new IllegalStateException("Legacy fixture upgrade mismatch: " + summary + " samples=" + failures);
        }
        writeFixtureResult(root, upgradeMarker + " PASS " + summary);
        LOGGER.info("{} PASS {}", upgradeMarker, summary);
    }

    private static boolean matchesSavedProperties(BlockState actual, JsonObject saved) {
        if (!saved.has("saved_properties")) return true;
        JsonObject properties = saved.getAsJsonObject("saved_properties");
        Map<String, String> current = new java.util.LinkedHashMap<String, String>();
        for (Map.Entry<net.minecraft.world.level.block.state.properties.Property<?>, Comparable<?>> entry : actual.getValues().entrySet()) {
            current.put(entry.getKey().getName(), entry.getValue().toString().toLowerCase(java.util.Locale.ROOT));
        }

        for (Map.Entry<String, JsonElement> entry : properties.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().getAsString();
            if (actual.getBlock() instanceof SlabBlock && "half".equals(key)) key = "type";
            if (actual.getBlock() instanceof DoorBlock) {
                boolean upper = "upper".equals(current.get("half"));
                if (upper && ("facing".equals(key) || "open".equals(key))) continue;
                if (!upper && ("hinge".equals(key) || "powered".equals(key))) continue;
            }

            // Connected shapes are derived from neighbours; powered states and fluid levels can tick.
            if ("variant".equals(key) || "shape".equals(key) || "powered".equals(key) || "level".equals(key)
                    || "north".equals(key) || "south".equals(key) || "east".equals(key)
                    || "west".equals(key) || "up".equals(key) || "down".equals(key)) continue;
            if (actual.getBlock() instanceof ButtonBlock || actual.getBlock() instanceof net.minecraft.world.level.block.LeverBlock) {
                continue; // These changed from a single attachment property to face plus horizontal facing.
            }
            if (!value.equals(current.get(key))) return false;
        }
        return true;
    }

    private static CompoundTag inventoryItem(ListTag inventory, int slot) {
        for (int index = 0; index < inventory.size(); index++) {
            CompoundTag item = inventory.getCompound(index);
            if ((item.getByte("Slot") & 255) == slot) return item;
        }
        return null;
    }

    private static EquipmentSlot equipmentSlot(String name) {
        if ("feet".equals(name)) return EquipmentSlot.FEET;
        if ("legs".equals(name)) return EquipmentSlot.LEGS;
        if ("chest".equals(name)) return EquipmentSlot.CHEST;
        if ("head".equals(name)) return EquipmentSlot.HEAD;
        if ("offhand".equals(name)) return EquipmentSlot.OFFHAND;
        return EquipmentSlot.MAINHAND;
    }

    private static boolean matchesLegacyMetadata(BlockState actual, int meta) {
        Block block = actual.getBlock();
        BlockState expected;

        try {
            Method converter = LegacyWorldDataHook.class.getDeclaredMethod(
                    "legacyState", Block.class, String.class, int.class);
            converter.setAccessible(true);
            expected = (BlockState) converter.invoke(null, block, "", Integer.valueOf(meta));
        } catch (ReflectiveOperationException exception) {
            throw new IllegalStateException("Could not compare the legacy block state", exception);
        }

        // Door metadata saved different properties in the upper and lower halves.
        if (block instanceof DoorBlock) {
            if ((meta & 8) != 0) {
                return actual.getValue(DoorBlock.HALF) == expected.getValue(DoorBlock.HALF)
                        && actual.getValue(DoorBlock.HINGE) == expected.getValue(DoorBlock.HINGE)
                        && actual.getValue(DoorBlock.POWERED).equals(expected.getValue(DoorBlock.POWERED));
            }

            return actual.getValue(DoorBlock.HALF) == expected.getValue(DoorBlock.HALF)
                    && actual.getValue(DoorBlock.FACING) == expected.getValue(DoorBlock.FACING)
                    && actual.getValue(DoorBlock.OPEN).equals(expected.getValue(DoorBlock.OPEN));
        }

        // Buttons, pressure plates and fluids can change while the chunk prepares.
        if (block instanceof ButtonBlock || block instanceof PressurePlateBlock
                || block instanceof LiquidBlock) {
            return true;
        }

        return actual.equals(expected);
    }

    private static ServerLevel fixtureWorld(MinecraftServer server, int dimension) {
        ResourceKey<Level> type = dimension == -1 ? Level.NETHER
                : dimension == 1 ? Level.END : Level.OVERWORLD;
        return require(server.getLevel(type), "missing fixture dimension " + dimension);
    }

    private static String currentBlockId(String legacy) {
        ResourceLocation id = new ResourceLocation(legacy.toLowerCase(java.util.Locale.ROOT));
        if (BaseMetals.MOD_ID.equals(id.getNamespace()) || "mmdlib".equals(id.getNamespace())) {
            return BaseMetals.MOD_ID + ":" + MissingMappings.blockTargetPath(id.getPath());
        }
        return legacy;
    }

    private static String currentItemId(String legacy) {
        ResourceLocation id = new ResourceLocation(legacy.toLowerCase(java.util.Locale.ROOT));
        if (BaseMetals.MOD_ID.equals(id.getNamespace()) || "mmdlib".equals(id.getNamespace())) {
            return MissingMappings.itemTargetId(id.getPath()).toString();
        }
        return legacy;
    }

    private static void writeFixtureResult(Path root, String result) {
        try {
            Files.write(root.resolve("BASEMETALS_LEGACY_TO_1_17_UPGRADE_RESULT.txt"),
                    (result + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            LOGGER.error("Could not write legacy fixture result", exception);
        }
    }

    private void verifyLegacyBlocks(MinecraftServer server) {
        ServerLevel world = require(server.getLevel(Level.OVERWORLD), "legacy overworld missing");
        Map<Long, Integer> expected = LegacyWorldDataHook.legacyPreparedBlockCounts();
        int expectedBlocks = 0;
        int convertedBlocks = 0;
        int verifiedChunks = 0;
        for (Long position : new java.util.ArrayList<>(expected.keySet())) {
            LevelChunk chunk = world.getChunk((int) position.longValue(), (int) (position.longValue() >> 32));
            Integer expectedInChunk = expected.get(Long.valueOf(chunkKey(chunk.getPos().x, chunk.getPos().z)));
            if (expectedInChunk == null) continue;
            int actualInChunk = 0;
            for (LevelChunkSection section : chunk.getSections()) {
                if (section == null || section.isEmpty()) continue;
                for (int x = 0; x < 16; x++) for (int y = 0; y < 16; y++) for (int z = 0; z < 16; z++) {
                    ResourceLocation id = ForgeRegistries.BLOCKS.getKey(section.getBlockState(x, y, z).getBlock());
                    if (id != null && BaseMetals.MOD_ID.equals(id.getNamespace())) actualInChunk++;
                }
            }
            check(actualInChunk == expectedInChunk.intValue(),
                    "legacy block count in chunk " + chunk.getPos().x + "," + chunk.getPos().z);
            expectedBlocks += expectedInChunk.intValue();
            convertedBlocks += actualInChunk;
            verifiedChunks++;
        }
        check(verifiedChunks > 0, "legacy chunks with Base Metals content loaded");
        check(expectedBlocks > 0 && convertedBlocks == expectedBlocks,
                "legacy Base Metals blocks survived flattening");
        LOGGER.info("BASEMETALS_LEGACY_BLOCKS VERIFIED blocks={} chunks={}",
                Integer.valueOf(convertedBlocks), Integer.valueOf(verifiedChunks));
    }

    private void runChecks(MinecraftServer server) throws Exception {
        ServerLevel world = require(server.getLevel(Level.OVERWORLD), "overworld missing");
        check(MaterialCatalogue.ALL.size() == 22, "material catalogue size");
        check(ModContent.blocksById().size() == 360, "registered block catalogue size");
        check(ModContent.itemsById().size() == 1127, "registered item catalogue size");
        check(ModContent.fluids().size() == 36, "fluid family count");
        check(ForgeRegistries.ENTITIES.getKey(ModEntities.CUSTOM_ARROW.get()).equals(id("custom_arrow")),
                "custom arrow entity id");
        check(ForgeRegistries.ENTITIES.getKey(ModEntities.CUSTOM_BOLT.get()).equals(id("custom_bolt")),
                "custom bolt entity id");

        for (java.util.Map.Entry<String, zone.moddev.mc.basemetals.content.RegistryHandle<net.minecraft.world.level.block.Block>>
                entry : ModContent.blocksById().entrySet()) {
            check(ForgeRegistries.BLOCKS.getKey(entry.getValue().get()).equals(id(entry.getKey())),
                    "block id " + entry.getKey());
        }
        for (java.util.Map.Entry<String, zone.moddev.mc.basemetals.content.RegistryHandle<Item>>
                entry : ModContent.itemsById().entrySet()) {
            check(ForgeRegistries.ITEMS.getKey(entry.getValue().get()).equals(id(entry.getKey())),
                    "item id " + entry.getKey());
        }

        check(ModContent.hiddenBlocks().size() == 26, "hidden double slab count");
        for (String name : ModContent.hiddenBlocks()) {
            check(ModContent.blocksById().get(name).get().defaultBlockState()
                    .getValue(SlabBlock.TYPE).getSerializedName().equals("double"),
                    "hidden double slab state " + name);
            check(!ForgeRegistries.ITEMS.containsKey(id(name)), "hidden block item " + name);
        }

        for (java.util.Map.Entry<String, FluidContent> entry : ModContent.fluids().entrySet()) {
            String name = entry.getKey();
            FluidContent fluid = entry.getValue();
            check(ForgeRegistries.FLUIDS.getKey(fluid.source().get()).equals(id(name)),
                    "source fluid " + name);
            check(ForgeRegistries.FLUIDS.getKey(fluid.flowing().get()).equals(id("flowing_" + name)),
                    "flowing fluid " + name);
            check(ForgeRegistries.BLOCKS.getKey(fluid.block().get()).equals(id(name)), "fluid block " + name);
            check(ForgeRegistries.ITEMS.getKey(fluid.bucket().get()).equals(id(name + "_bucket")),
                    "fluid bucket " + name);
            check(fluid.bucket().get().getItemCategory() == ModTabs.ITEMS, "bucket creative tab " + name);
        }

        checkRecipe(server, "iron_ore_crushing", "iron_powder", 2);
        checkRecipe(server, "coal_ore_crushing", "coal_powder", 2);
        checkRecipe(server, "gravel_crushing", "minecraft:sand", 1);
        checkRecipe(server, "iron_powder_smelting", "minecraft:iron_ingot", 1);
        checkRecipe(server, "steel_blend_smelting", "steel_ingot", 1);
        check(server.getRecipeManager().getRecipes().size() >= 2000, "recipe catalogue loaded");

        Block plate = ModContent.blocksById().get("copper_plate").get();
        BlockState up = plate.defaultBlockState().setValue(PlateBlock.FACING, Direction.UP);
        BlockPos origin = new BlockPos(0, 0, 0);
        AABB plateBounds = plate.getShape(up, world, origin, net.minecraft.world.phys.shapes.CollisionContext.empty()).bounds();
        check(plateBounds.equals(new AABB(0, 0, 0, 1, 1.0D / 16.0D, 1)),
                "plate placement bounds");

        Block detector = ModContent.HUMAN_DETECTOR.get();
        BlockState detectorState = detector.defaultBlockState();
        check(detector.getSignal(detectorState, world, origin, Direction.UP) == 0,
                "detector has no signal without players");

        AnvilBlock anvil = (AnvilBlock) ModContent.blocksById().get("steel_anvil").get();
        BlockState intact = anvil.defaultBlockState();
        BlockState chipped = AnvilBlock.damage(intact);
        check(chipped != null && chipped.getValue(BaseMetalAnvilBlock.DAMAGE).intValue() == 1,
                "custom anvil first damage");
        BlockState damaged = AnvilBlock.damage(chipped);
        check(damaged != null && damaged.getValue(BaseMetalAnvilBlock.DAMAGE).intValue() == 2,
                "custom anvil second damage");
        check(AnvilBlock.damage(damaged) == null, "custom anvil final damage");

        Object providerStatus = Class.forName("zone.moddev.mc.orespawn.api.OreSpawnApi")
                .getMethod("getProviderStatus", String.class).invoke(null, BaseMetals.MOD_ID);
        check("ACTIVE".equals(String.valueOf(providerStatus)), "OreSpawn provider active");
        Class<?> oreSpawnApi = Class.forName("zone.moddev.mc.orespawn.api.OreSpawnApi");
        check(Boolean.TRUE.equals(oreSpawnApi.getMethod("isOreTakeoverActive", String.class)
                .invoke(null, BaseMetals.MOD_ID)), "OreSpawn owns Base Metals ore placement");
        java.util.Optional<?> activeProfile = (java.util.Optional<?>) oreSpawnApi
                .getMethod("getActiveProfile", MinecraftServer.class).invoke(null, server);
        check(activeProfile.isPresent(), "OreSpawn active profile is available");
        java.util.Set<?> oreIds = (java.util.Set<?>) activeProfile.get().getClass()
                .getMethod("oreIds").invoke(activeProfile.get());
        check(oreIds.stream().filter(value -> value.toString().startsWith("basemetals:"))
                .count() == 11, "exactly eleven Base Metals rules, including with Mineralogy");
        if (net.minecraftforge.fml.ModList.get().isLoaded("mineralogy")) {
            check("ACTIVE".equals(String.valueOf(oreSpawnApi.getMethod("getProviderStatus", String.class)
                    .invoke(null, "mineralogy"))), "Mineralogy provider active");
        }
        check(classMissing("zone.moddev.mc.basemetals.worldgen.BaseMetalsOreGenerator"),
                "native world generator absent");

        check(ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "ingots/copper"))
                .contains(ModContent.item("copper_ingot").get()), "copper ingot tag");
        check(ItemTags.getAllTags().getTagOrEmpty(new ResourceLocation("forge", "ingots/adamant"))
                .contains(ModContent.item("adamantine_ingot").get()), "adamant alias tag");

        LootTable chest = server.getLootTables().get(
                id("chests/inject/simple_dungeon"));
        check(chest != LootTable.EMPTY, "auxiliary chest loot table");
        check(server.getAdvancements().getAdvancement(id("steel_maker")) != null,
                "steel advancement loaded");
        check(server.getAdvancements().getAllAdvancements().stream()
                .filter(value -> BaseMetals.MOD_ID.equals(value.getId().getNamespace())
                        && !value.getId().getPath().startsWith("recipes/")).count() == 18,
                "eighteen advancements loaded");

        testProjectilePersistence(world);
        testStarsteelRepair(world);
        testAdamantineArmor(world);
        testShieldUpgrade();
        testCrossbowContract();
        checks += GameplayRegressionChecks.run(server);
        checks += ContentModeChecks.run(server);
        checks += Native117Checks.run(server);
        checks += RawOreChecks.run(server);
    }

    private void testProjectilePersistence(ServerLevel world) {
        ItemStack ammunition = new ItemStack(ModContent.item("copper_arrow").get(), 8);
        Player player = new QuietFakePlayer(world,
                new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000113"), "ArmorProbe"));
        MaterialProjectile original = new MaterialProjectile(ModEntities.CUSTOM_ARROW.get(), world, player,
                ammunition);
        CompoundTag tag = new CompoundTag();
        ((Entity) original).saveWithoutId(tag);
        MaterialProjectile restored = new MaterialProjectile(ModEntities.CUSTOM_ARROW.get(), world);
        ((Entity) restored).load(tag);
        check(restored.getAmmunition().getItem() == ModContent.item("copper_arrow").get()
                && restored.getAmmunition().getCount() == 1, "projectile ammunition persistence");
    }

    private void testStarsteelRepair(ServerLevel world) {
        Player player = FakePlayerFactory.getMinecraft(world);
        ItemStack tool = new ItemStack(ModContent.item("starsteel_pickaxe").get());
        tool.setDamageValue(5);
        player.setItemInHand(InteractionHand.MAIN_HAND, tool);
        player.tickCount = 200;
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        check(tool.getDamageValue() == 4, "starsteel held repair");
    }

    private void testAdamantineArmor(ServerLevel world) {
        Player player = new QuietFakePlayer(world,
                new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000114"), "ArmorProbe"));
        player.setItemSlot(EquipmentSlot.HEAD,
                new ItemStack(ModContent.item("adamantine_helmet").get()));
        player.setItemSlot(EquipmentSlot.CHEST,
                new ItemStack(ModContent.item("adamantine_chestplate").get()));
        player.setItemSlot(EquipmentSlot.LEGS,
                new ItemStack(ModContent.item("adamantine_leggings").get()));
        player.setItemSlot(EquipmentSlot.FEET,
                new ItemStack(ModContent.item("adamantine_boots").get()));
        player.tickCount = 20;
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
        check(player.hasEffect(MobEffects.DAMAGE_RESISTANCE)
                && player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1,
                "adamantine full-set resistance II");
    }

    private void testShieldUpgrade() {
        ItemStack shield = new ItemStack(ModContent.item("copper_shield").get());
        ItemStack plate = new ItemStack(ModContent.item("steel_plate").get());
        AnvilUpdateEvent event = new AnvilUpdateEvent(shield, plate, "", 0, null);
        MinecraftForge.EVENT_BUS.post(event);
        check(event.getOutput().getItem() == ModContent.item("steel_shield").get()
                && event.getMaterialCost() == 1 && event.getCost() >= 5, "shield plate upgrade");
    }

    private void testCrossbowContract() throws Exception {
        Item crossbow = ModContent.item("steel_crossbow").get();
        check(crossbow instanceof MaterialItems.Crossbow, "crossbow implementation");
        check(((BaseMetalAmmoItem) ModContent.item("steel_bolt").get()).kind()
                == BaseMetalAmmoItem.Kind.BOLT, "bolt item kind");
    }

    private void checkRecipe(MinecraftServer server, String recipeName, String resultName, int count) {
        Recipe recipe = require(server.getRecipeManager().byKey(id(recipeName)).orElse(null),
                "missing recipe " + recipeName);
        ResourceLocation expected = resultName.indexOf(':') >= 0
                ? new ResourceLocation(resultName) : id(resultName);
        check(ForgeRegistries.ITEMS.getKey(recipe.getResultItem().getItem()).equals(expected)
                && recipe.getResultItem().getCount() == count, "recipe " + recipeName);
        if (recipeName.endsWith("_crushing")) check(recipe instanceof CrushingRecipe,
                "crushing serializer " + recipeName);
    }

    private void check(boolean condition, String description) {
        if (!condition) throw new IllegalStateException("Runtime check failed: " + description);
        checks++;
    }

    private static boolean classMissing(String name) {
        try { Class.forName(name); return false; }
        catch (ClassNotFoundException expected) { return true; }
    }

    private static ResourceLocation id(String path) {
        return new ResourceLocation(BaseMetals.MOD_ID, path);
    }

    private static long chunkKey(int x, int z) {
        return ((long) x & 0xffffffffL) << 32 | ((long) z & 0xffffffffL);
    }

    private static <T> T require(T value, String message) {
        if (value == null) throw new IllegalStateException(message);
        return value;
    }

    /** FakePlayer has no network connection, so potion callbacks must not send packets. */
    private static final class QuietFakePlayer extends FakePlayer {
        private QuietFakePlayer(ServerLevel world, GameProfile profile) { super(world, profile); }
        @Override protected void onEffectAdded(MobEffectInstance effect, net.minecraft.world.entity.Entity source) {}
        @Override protected void onEffectUpdated(MobEffectInstance effect, boolean reapply, net.minecraft.world.entity.Entity source) {}
        @Override protected void onEffectRemoved(MobEffectInstance effect) {}
    }
}
