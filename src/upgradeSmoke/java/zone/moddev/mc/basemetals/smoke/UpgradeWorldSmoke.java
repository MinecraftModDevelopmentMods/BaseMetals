package zone.moddev.mc.basemetals.smoke;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/** Validation-only reader for captured legacy Forge upgrade fixtures and worlds. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class UpgradeWorldSmoke {
    private static final String ENABLED_PROPERTY = "basemetals.upgradeWorldSmoke";
    private static final String LEGACY_WORLD_PROPERTY = "basemetals.legacyWorldSmoke";
    private static final java.util.Set<net.minecraft.world.level.ChunkPos> equipmentChunks = new java.util.HashSet<>();
    private static ServerStartedEvent pending;
    private static int waitTicks;
    private static boolean equipmentReady;
    private static boolean registered;

    private UpgradeWorldSmoke() {}

    @SubscribeEvent
    public static void setup(net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        register();
    }

    public static void register() {
        if (!registered) {
            ServerStartedEvent.BUS.addListener(UpgradeWorldSmoke::onServerStarted);
            net.minecraftforge.event.TickEvent.ServerTickEvent.Post.BUS.addListener(UpgradeWorldSmoke::onServerTick);
            registered = true;
        }
    }

    public static void onServerStarted(ServerStartedEvent event) {
        boolean fixture = Boolean.getBoolean(ENABLED_PROPERTY);
        boolean legacyWorld = Boolean.getBoolean(LEGACY_WORLD_PROPERTY);
        if (!fixture && !legacyWorld) return;

        if (fixture && !equipmentReady) {
            prepareEquipmentChunks(event);
            pending = event;
            return;
        }

        Path root = event.getServer().getWorldPath(LevelResource.ROOT);
        if (legacyWorld) {
            Path migrationMarker = root.resolve("BASEMETALS_1_12_BLOCK_MIGRATION_COMPLETE.txt");
            if (!Files.isRegularFile(migrationMarker)) {
                throw new IllegalStateException("BASEMETALS_LEGACY_WORLD_SMOKE FAIL missing migration marker");
            }
            writeResult(root, "BASEMETALS_LEGACY_WORLD_SMOKE PASS");
            BaseMetals.LOGGER.info("BASEMETALS_LEGACY_WORLD_SMOKE PASS world={}", root);
            event.getServer().halt(false);
            return;
        }

        Path manifestPath = root.resolve("legacy_registry_manifest_runtime.json");
        Path marker = root.resolve("BASEMETALS_1_12_FIXTURE_COMPLETE.txt");
        List<String> failures = new ArrayList<>();
        int blockMismatches = 0;
        int itemMismatches = 0;
        int checkedStates = 0;
        int checkedItems = 0;
        int checkedArmor = 0;
        int checkedBuckets = 0;
        int checkedPlayerItems = 0;
        try {
            boolean source110 = Files.isRegularFile(root.resolve("BASEMETALS_1_10_FIXTURE_COMPLETE.txt"));
            if (!Files.isRegularFile(marker) && !source110) failures.add("missing legacy fixture marker");
            JsonObject manifest;
            try (Reader reader = Files.newBufferedReader(manifestPath, StandardCharsets.UTF_8)) {
                manifest = JsonParser.parseReader(reader).getAsJsonObject();
            }
            int format = manifest.has("fixture_format") ? manifest.get("fixture_format").getAsInt() : 1;
            if (format < 2) failures.add("fixture format " + format + " does not cover the complete upgrade contract");

            for (JsonElement blockElement : manifest.getAsJsonArray("blocks")) {
                JsonObject block = blockElement.getAsJsonObject();
                String expected = currentBlockId(block.get("id").getAsString());
                for (JsonElement stateElement : block.getAsJsonArray("states")) {
                    JsonObject oldState = stateElement.getAsJsonObject();
                    int dimension = oldState.has("dimension") ? oldState.get("dimension").getAsInt() : 0;
                    ServerLevel world = level(event, dimension);
                    BlockPos pos = new BlockPos(oldState.get("x").getAsInt(), oldState.get("y").getAsInt(),
                            oldState.get("z").getAsInt());
                    String actual = String.valueOf(ForgeRegistries.BLOCKS.getKey(world.getBlockState(pos).getBlock()));
                    int metadata = oldState.has("saved_metadata") ? oldState.get("saved_metadata").getAsInt()
                            : oldState.get("metadata").getAsInt();
                    boolean stateMatches = Legacy112WorldMigrator.matchesLegacyMetadata(world.getBlockState(pos), metadata)
                            && matchesSavedProperties(world.getBlockState(pos), oldState);
                    if (!expected.equals(actual) || !stateMatches) {
                        if (blockMismatches < 10) {
                            failures.add("block dim=" + dimension + " " + pos + " expected " + expected + " metadata="
                                    + oldState.get("metadata").getAsInt() + " but found "
                                    + world.getBlockState(pos));
                        }
                        blockMismatches++;
                    }
                    checkedStates++;
                }
            }

            ServerLevel overworld = event.getServer().overworld();
            var unbreaking = overworld.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                    .getOrThrow(Enchantments.UNBREAKING);
            for (JsonElement itemElement : manifest.getAsJsonArray("items")) {
                JsonObject oldItem = itemElement.getAsJsonObject();
                int chestIndex = oldItem.get("chest").getAsInt();
                int slot = oldItem.get("slot").getAsInt();
                BlockPos pos = oldItem.has("chest_x")
                        ? new BlockPos(oldItem.get("chest_x").getAsInt(), oldItem.get("chest_y").getAsInt(),
                                oldItem.get("chest_z").getAsInt())
                        : gridPosition(chestIndex, -64, 120);
                BlockEntity blockEntity = overworld.getBlockEntity(pos);
                if (!(blockEntity instanceof ChestBlockEntity chest)) {
                    if (itemMismatches < 10) failures.add("missing inventory chest " + chestIndex + " at " + pos);
                    itemMismatches++;
                    continue;
                }
                ItemStack stack = chest.getItem(slot);
                String expected = currentItemId(oldItem.get("id").getAsString());
                String actual = String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem()));
                int expectedCount = oldItem.has("count") ? oldItem.get("count").getAsInt() : 1;
                int expectedDamage = oldItem.has("damage") ? oldItem.get("damage").getAsInt() : 0;
                boolean proofMatches = format < 2 || stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getCompoundOrEmpty("basemetals_fixture")
                        .getStringOr("proof", "").equals(oldItem.get("id").getAsString());
                boolean enchantmentMatches = !oldItem.has("enchanted") || !oldItem.get("enchanted").getAsBoolean()
                        || EnchantmentHelper.getItemEnchantmentLevel(unbreaking, stack) == 2;
                if (!expected.equals(actual) || stack.getCount() != expectedCount
                        || stack.getDamageValue() != expectedDamage || !proofMatches || !enchantmentMatches) {
                    if (itemMismatches < 10) {
                        failures.add("chest " + chestIndex + " slot " + slot + " expected "
                                + expected + " x" + expectedCount + " damage=" + expectedDamage
                                + " with fixture NBT but found " + stack + " damage="
                                + stack.getDamageValue() + " proof=" + proofMatches
                                + " unbreaking="
                                + EnchantmentHelper.getItemEnchantmentLevel(unbreaking, stack));
                    }
                    itemMismatches++;
                }
                if (oldItem.has("armor_stand")) {
                    BlockPos armorPos = new BlockPos(
                            (int) Math.floor(oldItem.get("armor_x").getAsDouble()),
                            (int) Math.floor(oldItem.get("armor_y").getAsDouble()),
                            (int) Math.floor(oldItem.get("armor_z").getAsDouble()));
                    overworld.getChunkAt(armorPos);
                    List<ArmorStand> stands = overworld.getEntitiesOfClass(ArmorStand.class,
                            new AABB(armorPos.getX() - 0.75D, overworld.getMinY(), armorPos.getZ() - 0.75D,
                                    armorPos.getX() + 1.75D, overworld.getMaxY(), armorPos.getZ() + 1.75D));
                    EquipmentSlot equipmentSlot = EquipmentSlot.byName(oldItem.get("armor_slot").getAsString());
                    boolean equipped = stands.stream().anyMatch(stand -> {
                        ItemStack worn = stand.getItemBySlot(equipmentSlot);
                        return expected.equals(String.valueOf(ForgeRegistries.ITEMS.getKey(worn.getItem())))
                                && worn.getDamageValue() == expectedDamage
                                && worn.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getCompoundOrEmpty("basemetals_fixture").getStringOr("proof", "")
                                        .equals(oldItem.get("id").getAsString())
                                && EnchantmentHelper.getItemEnchantmentLevel(unbreaking, worn) == 2;
                    });
                    if (!equipped) {
                        if (itemMismatches < 10) failures.add("missing armor-stand equipment " + expected);
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
                BlockPos pos = fluid.has("bucket_chest_x")
                        ? new BlockPos(fluid.get("bucket_chest_x").getAsInt(),
                                fluid.get("bucket_chest_y").getAsInt(),
                                fluid.get("bucket_chest_z").getAsInt())
                        : gridPosition(chestIndex, -64, 130);
                BlockEntity blockEntity = overworld.getBlockEntity(pos);
                String expected = BaseMetals.MOD_ID + ":"
                        + MissingMappings.fluidTargetPath(fluid.get("name").getAsString()) + "_bucket";
                if (!(blockEntity instanceof ChestBlockEntity chest)
                        || !expected.equals(String.valueOf(ForgeRegistries.ITEMS.getKey(chest.getItem(slot).getItem())))) {
                    if (itemMismatches < 10) failures.add("legacy filled bucket did not become " + expected);
                    itemMismatches++;
                }
                checkedBuckets++;
            }

            if (manifest.has("players")) {
                for (JsonElement playerElement : manifest.getAsJsonArray("players")) {
                    JsonObject savedPlayer = playerElement.getAsJsonObject();
                    String name = savedPlayer.has("name") ? savedPlayer.get("name").getAsString()
                            : "BaseMetalsFixture";
                    ServerPlayer player = new ServerPlayer(event.getServer(), overworld,
                            new GameProfile(UUID.fromString(savedPlayer.get("uuid").getAsString()), name),
                            net.minecraft.server.level.ClientInformation.createDefault());
                    CompoundTag loaded = event.getServer().getPlayerList().loadPlayerData(
                            new net.minecraft.server.players.NameAndId(player.getGameProfile())).orElse(null);
                    if (loaded == null) {
                        failures.add("fixture playerdata did not load");
                        continue;
                    }
                    player.load(net.minecraft.world.level.storage.TagValueInput.create(
                            net.minecraft.util.ProblemReporter.DISCARDING, overworld.registryAccess(), loaded));
                    for (JsonElement inventoryElement : savedPlayer.getAsJsonArray("inventory")) {
                        JsonObject expectedItem = inventoryElement.getAsJsonObject();
                        int slot = expectedItem.get("slot").getAsInt();
                        ItemStack stack = player.getInventory().getItem(slot);
                        String expected = currentItemId(expectedItem.get("id").getAsString());
                        boolean proof = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                                net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getCompoundOrEmpty("basemetals_fixture")
                                .getStringOr("player_proof", "").equals(expectedItem.get("id").getAsString());
                        if (!expected.equals(String.valueOf(ForgeRegistries.ITEMS.getKey(stack.getItem())))
                                || !proof) {
                            if (itemMismatches < 10) failures.add("player slot " + slot + " lost " + expected);
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
            verifySavedAdvancements(event.getServer(), overworld, root);
        } catch (IOException | RuntimeException exception) {
            failures.add(exception.toString());
        }

        if (!failures.isEmpty()) {
            String result = "BASEMETALS_UPGRADE_SMOKE FAIL states=" + checkedStates
                    + " block_mismatches=" + blockMismatches + " items=" + checkedItems
                    + " armor=" + checkedArmor + " buckets=" + checkedBuckets
                    + " player_items=" + checkedPlayerItems
                    + " item_mismatches=" + itemMismatches + " samples=" + failures;
            writeResult(root, result);
            throw new IllegalStateException(result);
        }

        writeResult(root, "BASEMETALS_UPGRADE_SMOKE PASS states=" + checkedStates + " items=" + checkedItems
                + " armor=" + checkedArmor + " buckets=" + checkedBuckets
                + " player_items=" + checkedPlayerItems);
        BaseMetals.LOGGER.info(
                "BASEMETALS_UPGRADE_SMOKE PASS states={} items={} armor={} buckets={} player_items={} world={}",
                checkedStates, checkedItems, checkedArmor, checkedBuckets, checkedPlayerItems, root);
        event.getServer().halt(false);
    }

    private static BlockPos gridPosition(int ordinal, int xOffset, int y) {
        return new BlockPos(xOffset + ordinal % 64, y, ordinal / 64);
    }

    private static void prepareEquipmentChunks(ServerStartedEvent event) {
        Path manifest = event.getServer().getWorldPath(LevelResource.ROOT).resolve("legacy_registry_manifest_runtime.json");
        try {
            for (var entry : JsonParser.parseString(Files.readString(manifest)).getAsJsonObject().getAsJsonArray("items")) {
                var item = entry.getAsJsonObject();
                if (!item.has("armor_stand")) continue;
                int x = (int) Math.floor(item.get("armor_x").getAsDouble()) >> 4;
                int z = (int) Math.floor(item.get("armor_z").getAsDouble()) >> 4;
                equipmentChunks.add(new net.minecraft.world.level.ChunkPos(x, z));
                event.getServer().overworld().setChunkForced(x, z, true);
                event.getServer().overworld().getChunk(x, z);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Could not read the equipment fixture", exception);
        }
    }

    public static void onServerTick(net.minecraftforge.event.TickEvent.ServerTickEvent.Post event) {
        if (pending == null || ++waitTicks < 20) return;
        var server = pending.getServer();
        boolean loaded = equipmentChunks.stream().allMatch(chunk -> server.overworld().areEntitiesLoaded(chunk.toLong()));
        if (!loaded && waitTicks < 200) return;
        if (!loaded) throw new IllegalStateException("Timed out loading upgrade equipment chunks");
        ServerStartedEvent started = pending;
        pending = null;
        equipmentReady = true;
        onServerStarted(started);
        server.saveAllChunks(true, true, false);
        equipmentChunks.forEach(chunk -> server.overworld().setChunkForced(chunk.x, chunk.z, false));
    }

    private static void verifySavedAdvancements(net.minecraft.server.MinecraftServer server,
            ServerLevel world, Path root) throws IOException {
        Path fixtureFile = root.resolve("basemetals_advancement_upgrade_fixture.json");
        if (!Files.isRegularFile(fixtureFile)) return;
        JsonObject fixture = JsonParser.parseString(Files.readString(fixtureFile)).getAsJsonObject();
        var player = new ServerPlayer(server, world,
                new GameProfile(UUID.fromString(fixture.get("uuid").getAsString()), "UpgradeProgress"),
                net.minecraft.server.level.ClientInformation.createDefault());
        int retained = 0;
        for (var entry : fixture.getAsJsonObject("progress").entrySet()) {
            if ("DataVersion".equals(entry.getKey())) continue;
            var advancement = server.getAdvancements().get(Identifier.parse(entry.getKey()));
            if (advancement == null) throw new IllegalStateException("Saved advancement missing: " + entry.getKey());
            var progress = player.getAdvancements().getOrStartProgress(advancement);
            if (!progress.isDone()) throw new IllegalStateException("Saved advancement is incomplete: " + entry.getKey());
            for (var saved : entry.getValue().getAsJsonObject().getAsJsonObject("criteria").entrySet()) {
                var criterion = progress.getCriterion(saved.getKey());
                if (criterion == null || !criterion.isDone()) {
                    throw new IllegalStateException("Lost advancement criterion: " + entry.getKey() + "/" + saved.getKey());
                }
                retained++;
            }
        }
        player.getAdvancements().save();
        BaseMetals.LOGGER.info("BASEMETALS_UPGRADE_ADVANCEMENTS PASS criteria={}", retained);
    }

    private static boolean matchesSavedProperties(net.minecraft.world.level.block.state.BlockState actual,
            JsonObject saved) {
        if (!saved.has("saved_properties")) return true;
        var current = new java.util.LinkedHashMap<String, String>();
        actual.getValues().forEach((property, value) -> current.put(property.getName(),
                value.toString().toLowerCase(java.util.Locale.ROOT)));
        for (var entry : saved.getAsJsonObject("saved_properties").entrySet()) {
            String key = entry.getKey();
            if (actual.getBlock() instanceof net.minecraft.world.level.block.SlabBlock && "half".equals(key)) key = "type";
            if (actual.getBlock() instanceof net.minecraft.world.level.block.DoorBlock) {
                boolean upper = "upper".equals(current.get("half"));
                if (upper && ("facing".equals(key) || "open".equals(key))) continue;
                if (!upper && ("hinge".equals(key) || "powered".equals(key))) continue;
            }
            // Neighbours, redstone and flowing fluids can change these after loading.
            if (java.util.Set.of("variant", "shape", "powered", "level", "north", "south", "east", "west", "up", "down")
                    .contains(key)) continue;
            if (actual.getBlock() instanceof net.minecraft.world.level.block.ButtonBlock
                    || actual.getBlock() instanceof net.minecraft.world.level.block.LeverBlock) continue;
            if (!entry.getValue().getAsString().equals(current.get(key))) return false;
        }
        return true;
    }

    private static ServerLevel level(ServerStartedEvent event, int dimension) {
        ServerLevel result = event.getServer().getLevel(switch (dimension) {
            case -1 -> Level.NETHER;
            case 1 -> Level.END;
            default -> Level.OVERWORLD;
        });
        if (result == null) throw new IllegalStateException("Missing fixture dimension " + dimension);
        return result;
    }

    private static String currentBlockId(String legacy) {
        Identifier id = Identifier.parse(legacy.toLowerCase(java.util.Locale.ROOT));
        if (id.getNamespace().equals("mmdlib") || id.getNamespace().equals(BaseMetals.MOD_ID)) {
            return BaseMetals.MOD_ID + ":" + MissingMappings.blockTargetPath(id.getPath());
        }
        return legacy;
    }

    private static String currentItemId(String legacy) {
        Identifier id = Identifier.parse(legacy.toLowerCase(java.util.Locale.ROOT));
        if (id.getNamespace().equals("mmdlib") || id.getNamespace().equals(BaseMetals.MOD_ID)) {
            return MissingMappings.itemTargetId(id.getPath()).toString();
        }
        return legacy;
    }

    private static void writeResult(Path root, String result) {
        try {
            Files.writeString(root.resolve("BASEMETALS_UPGRADE_SMOKE_RESULT.txt"), result + System.lineSeparator(),
                    StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            BaseMetals.LOGGER.error("Could not write upgrade smoke result", exception);
        }
    }
}
