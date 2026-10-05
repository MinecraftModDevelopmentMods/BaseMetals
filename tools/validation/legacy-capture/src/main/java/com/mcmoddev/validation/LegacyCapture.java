package com.mcmoddev.validation;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.mojang.authlib.GameProfile;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod(modid = LegacyCapture.MOD_ID, name = "Base Metals Legacy Capture", version = "1.0",
        acceptableRemoteVersions = "*", serverSideOnly = true)
public final class LegacyCapture {
    public static final String MOD_ID = "legacycapture";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static String sourceVersion() {
        return System.getProperty("legacycapture.minecraft", "1.10.2");
    }

    private static String markerPrefix() {
        return sourceVersion().startsWith("1.12.") ? "BASEMETALS_1_12" : "BASEMETALS_1_10";
    }

    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) throws IOException {
        net.minecraft.server.MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
        Path worldRoot = server.worldServerForDimension(0).getSaveHandler().getWorldDirectory().toPath();
        if (Files.isRegularFile(worldRoot.resolve("legacy_registry_manifest_runtime.json"))) {
            verifySavedFixture(server, worldRoot);
            server.saveAllWorlds(false);
            server.initiateShutdown();
            return;
        }

        net.minecraftforge.common.DimensionManager.initDimension(-1);
        net.minecraftforge.common.DimensionManager.initDimension(1);
        List<WorldServer> worlds = new ArrayList<>();
        worlds.add(server.worldServerForDimension(0));
        worlds.add(server.worldServerForDimension(-1));
        worlds.add(server.worldServerForDimension(1));
        WorldServer world = worlds.get(0);
        JsonObject manifest = new JsonObject();
        manifest.addProperty("source", "live Forge " + sourceVersion() + " registries");
        manifest.addProperty("forge", net.minecraftforge.common.ForgeVersion.getVersion());
        manifest.addProperty("fixture_format", 2);
        manifest.addProperty("source_minecraft", sourceVersion());
        net.minecraftforge.fml.common.ModContainer mod = net.minecraftforge.fml.common.Loader.instance()
                .getIndexedModList().get("basemetals");
        manifest.addProperty("source_basemetals", mod.getVersion());

        List<ResourceLocation> blockIds = ids(ForgeRegistries.BLOCKS.getEntries().stream()
                .map(Map.Entry::getKey).collect(Collectors.toList()));
        List<ResourceLocation> itemIds = ids(ForgeRegistries.ITEMS.getEntries().stream()
                .map(Map.Entry::getKey).collect(Collectors.toList()));
        manifest.add("blocks", captureBlocks(worlds, blockIds));
        manifest.add("items", captureItems(world, itemIds));
        manifest.add("fluids", captureFluids(world));
        manifest.add("players", capturePlayer(world, itemIds));

        Path gameDir = Paths.get(".").toAbsolutePath().normalize();
        write(gameDir.resolve("legacy_registry_manifest_runtime.json"), manifest);
        Path worldDir = world.getSaveHandler().getWorldDirectory().toPath();
        write(worldDir.resolve("legacy_registry_manifest_runtime.json"), manifest);
        Path os3 = gameDir.resolve("config").resolve("orespawn3").resolve("orespawn.json");
        if (Files.isRegularFile(os3)) {
            Files.copy(os3, worldDir.resolve("legacy_orespawn3_orespawn.json"),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
        try (InputStream baseMetalsRules = LegacyCapture.class.getResourceAsStream(
                "/assets/basemetals/orespawn/basemetals.json")) {
            if (baseMetalsRules != null) {
                Files.copy(baseMetalsRules, worldDir.resolve("legacy_orespawn3_basemetals.json"),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            }
        }
        Files.write(worldDir.resolve(markerPrefix() + "_FIXTURE_COMPLETE.txt"),
                ("Base Metals " + sourceVersion() + " fixture format 2 captured with " + blockIds.size()
                        + " blocks in all three dimensions, " + itemIds.size()
                        + " exact item stacks, filled fluid buckets, armor stands, and player data.\n")
                        .getBytes(StandardCharsets.UTF_8));

        server.saveAllWorlds(false);
        org.apache.logging.log4j.LogManager.getLogger(MOD_ID).info(
                "BASEMETALS_LEGACY_CAPTURE PASS version={} blocks={} items={}",
                mod.getVersion(), blockIds.size(), itemIds.size());
        server.initiateShutdown();
    }

    private static void verifySavedFixture(net.minecraft.server.MinecraftServer server, Path root)
            throws IOException {
        JsonObject manifest;
        try (java.io.Reader reader = Files.newBufferedReader(
                root.resolve("legacy_registry_manifest_runtime.json"), StandardCharsets.UTF_8)) {
            manifest = new com.google.gson.JsonParser().parse(reader).getAsJsonObject();
        }

        net.minecraftforge.common.DimensionManager.initDimension(-1);
        net.minecraftforge.common.DimensionManager.initDimension(1);
        int states = 0;
        for (com.google.gson.JsonElement element : manifest.getAsJsonArray("blocks")) {
            JsonObject block = element.getAsJsonObject();
            for (com.google.gson.JsonElement stateElement : block.getAsJsonArray("states")) {
                JsonObject saved = stateElement.getAsJsonObject();
                WorldServer world = server.worldServerForDimension(saved.get("dimension").getAsInt());
                BlockPos pos = new BlockPos(saved.get("x").getAsInt(), saved.get("y").getAsInt(),
                        saved.get("z").getAsInt());
                IBlockState actual = world.getBlockState(pos);
                String expectedId = block.get("id").getAsString();
                if (!actual.getBlock().getRegistryName().toString().equals(expectedId)) {
                    throw new IllegalStateException("Source fixture lost " + expectedId + " at " + pos);
                }

                // Record the state that 1.10 actually saved, not properties computed from neighbours.
                saved.addProperty("saved_metadata", actual.getBlock().getMetaFromState(actual));
                JsonObject properties = new JsonObject();
                for (Map.Entry<net.minecraft.block.properties.IProperty<?>, Comparable<?>> property
                        : actual.getProperties().entrySet()) {
                    properties.addProperty(property.getKey().getName(), property.getValue().toString()
                            .toLowerCase(java.util.Locale.ROOT));
                }
                saved.add("saved_properties", properties);
                states++;
            }
        }

        WorldServer world = server.worldServerForDimension(0);
        int stacks = 0;
        for (com.google.gson.JsonElement element : manifest.getAsJsonArray("items")) {
            JsonObject saved = element.getAsJsonObject();
            BlockPos pos = new BlockPos(saved.get("chest_x").getAsInt(), saved.get("chest_y").getAsInt(),
                    saved.get("chest_z").getAsInt());
            world.getBlockState(pos);
            TileEntityChest chest = (TileEntityChest) world.getTileEntity(pos);
            ItemStack stack = chest.getStackInSlot(saved.get("slot").getAsInt());
            if (stack == null || !stack.getItem().getRegistryName().toString().equals(saved.get("id").getAsString())
                    || stack.stackSize != saved.get("count").getAsInt()
                    || stack.getItemDamage() != saved.get("damage").getAsInt()) {
                throw new IllegalStateException("Source fixture inventory changed at " + pos);
            }
            if (saved.has("armor_x")) {
                BlockPos armorPos = new BlockPos(saved.get("armor_x").getAsDouble(),
                        saved.get("armor_y").getAsDouble(), saved.get("armor_z").getAsDouble());
                world.getChunkProvider().provideChunk(armorPos.getX() >> 4, armorPos.getZ() >> 4);
                List<EntityArmorStand> stands = world.getEntitiesWithinAABB(EntityArmorStand.class,
                        new net.minecraft.util.math.AxisAlignedBB(armorPos).expand(0.75D, 0.75D, 0.75D));
                boolean found = false;
                for (EntityArmorStand stand : stands) {
                    for (EntityEquipmentSlot equipment : EntityEquipmentSlot.values()) {
                        if (!equipment.getName().equals(saved.get("armor_slot").getAsString())) continue;
                        ItemStack worn = stand.getItemStackFromSlot(equipment);
                        found |= worn != null && worn.getItem().getRegistryName().toString()
                                .equals(saved.get("id").getAsString())
                                && worn.getItemDamage() == saved.get("damage").getAsInt();
                    }
                }
                if (!found) throw new IllegalStateException("Source armor stand was not saved: " + saved.get("id"));
            }
            stacks++;
        }

        write(root.resolve("legacy_registry_manifest_runtime.json"), manifest);
        Files.write(root.resolve(markerPrefix() + "_FIXTURE_RELOADED.txt"),
                ("states=" + states + " items=" + stacks + "\\n").getBytes(StandardCharsets.UTF_8));
        org.apache.logging.log4j.LogManager.getLogger(MOD_ID).info(
                "BASEMETALS_LEGACY_CAPTURE_RELOAD PASS states={} items={}", states, stacks);
    }

    private static JsonArray captureBlocks(List<WorldServer> worlds, List<ResourceLocation> ids) {
        JsonArray blocks = new JsonArray();
        for (ResourceLocation id : ids) {
            Block block = ForgeRegistries.BLOCKS.getValue(id);
            JsonObject entry = new JsonObject();
            entry.addProperty("id", id.toString());
            JsonArray states = new JsonArray();
            for (WorldServer world : worlds) {
                int ordinal = 0;
                for (ResourceLocation preceding : ids) {
                    if (preceding.equals(id)) break;
                    ordinal += ForgeRegistries.BLOCKS.getValue(preceding).getBlockState().getValidStates().size();
                }
                for (IBlockState state : block.getBlockState().getValidStates()) {
                    JsonObject stateEntry = new JsonObject();
                    stateEntry.addProperty("description", state.toString());
                    int metadata;
                    try {
                        metadata = block.getMetaFromState(state);
                    } catch (RuntimeException ignored) {
                        metadata = -1;
                    }
                    stateEntry.addProperty("metadata", metadata);
                    stateEntry.addProperty("dimension", world.provider.getDimension());
                    BlockPos pos = statePosition(ordinal++);
                    prepareSupport(world, pos, state);
                    world.setBlockState(pos, state, 2);
                    stateEntry.addProperty("x", pos.getX());
                    stateEntry.addProperty("y", pos.getY());
                    stateEntry.addProperty("z", pos.getZ());
                    states.add(stateEntry);
                }
            }
            entry.add("states", states);
            blocks.add(entry);
        }
        return blocks;
    }

    private static JsonArray captureItems(WorldServer world, List<ResourceLocation> ids) {
        JsonArray items = new JsonArray();
        int chestIndex = -1;
        TileEntityChest chest = null;
        int slot = 27;
        int armorStandIndex = 0;
        for (ResourceLocation id : ids) {
            Item item = ForgeRegistries.ITEMS.getValue(id);
            JsonObject entry = new JsonObject();
            entry.addProperty("id", id.toString());
            ItemStack stack = new ItemStack(item);
            if (stack.isItemStackDamageable() && stack.getMaxDamage() > 1) {
                stack.setItemDamage(Math.max(1, stack.getMaxDamage() / 3));
                if (stack.isItemEnchantable()) stack.addEnchantment(Enchantments.UNBREAKING, 2);
            }
            NBTTagCompound fixtureTag = stack.getSubCompound("basemetals_fixture", true);
            fixtureTag.setString("proof", id.toString());
            stack.setStackDisplayName("Fixture " + id.toString());
            entry.addProperty("custom_name", stack.getDisplayName());
            entry.addProperty("metadata", stack.getMetadata());
            entry.addProperty("damage", stack.getItemDamage());
            entry.addProperty("count", stack.stackSize);
            entry.addProperty("max_damage", stack.getMaxDamage());
            entry.addProperty("enchanted", stack.isItemEnchanted());
            entry.addProperty("tag_snbt", stack.getTagCompound().toString());

            if (slot >= 27) {
                chestIndex++;
                BlockPos chestPos = chestPosition(chestIndex, 120);
                world.setBlockState(chestPos, Blocks.CHEST.getDefaultState(), 3);
                chest = (TileEntityChest) world.getTileEntity(chestPos);
                slot = 0;
            }
            chest.setInventorySlotContents(slot, stack.copy());
            entry.addProperty("chest", chestIndex);
            entry.addProperty("chest_x", chest.getPos().getX());
            entry.addProperty("chest_y", chest.getPos().getY());
            entry.addProperty("chest_z", chest.getPos().getZ());
            entry.addProperty("slot", slot++);

            if (item instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) item;
                EntityArmorStand stand = new EntityArmorStand(world);
                stand.setPosition(-32.5D + armorStandIndex % 32, 121.0D, armorStandIndex / 32 + 0.5D);
                world.getChunkProvider().provideChunk(((int) Math.floor(stand.posX)) >> 4,
                        ((int) Math.floor(stand.posZ)) >> 4);
                stand.forceSpawn = true;
                world.setBlockState(new BlockPos(stand.posX, 120, stand.posZ), Blocks.BEDROCK.getDefaultState(), 2);
                stand.setNoGravity(true);
                stand.setItemStackToSlot(armor.armorType, stack.copy());
                if (!world.spawnEntity(stand)) {
                    throw new IllegalStateException("Source runtime rejected armor stand for " + id);
                }
                entry.addProperty("armor_stand", armorStandIndex++);
                entry.addProperty("armor_x", stand.posX);
                entry.addProperty("armor_y", stand.posY);
                entry.addProperty("armor_z", stand.posZ);
                entry.addProperty("armor_slot", armor.armorType.getName());
            }
            items.add(entry);
        }
        return items;
    }

    private static JsonArray captureFluids(WorldServer world) {
        JsonArray fluids = new JsonArray();
        final int[] ordinal = {0};
        FluidRegistry.getRegisteredFluids().entrySet().stream()
                .filter(entry -> isBaseName(entry.getKey()) || isBaseName(entry.getValue().getName())
                        || isBaseBlock(entry.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    Fluid fluid = entry.getValue();
                    JsonObject value = new JsonObject();
                    value.addProperty("name", entry.getKey());
                    value.addProperty("block", fluid.getBlock() == null || fluid.getBlock().getRegistryName() == null
                            ? "" : fluid.getBlock().getRegistryName().toString());
                    value.addProperty("temperature", fluid.getTemperature());
                    value.addProperty("density", fluid.getDensity());
                    net.minecraftforge.fluids.UniversalBucket universal =
                            net.minecraftforge.common.ForgeModContainer.getInstance().universalBucket;
                    ItemStack bucket = universal == null ? null
                            : net.minecraftforge.fluids.UniversalBucket.getFilledBucket(universal, fluid);
                    if (bucket != null && bucket.getItem() != null) {
                        int bucketOrdinal = ordinal[0]++;
                        int chestIndex = bucketOrdinal / 27;
                        int slot = bucketOrdinal % 27;
                        BlockPos chestPos = chestPosition(chestIndex, 130);
                        world.setBlockState(chestPos, Blocks.CHEST.getDefaultState(), 3);
                        TileEntityChest chest = (TileEntityChest) world.getTileEntity(chestPos);
                        chest.setInventorySlotContents(slot, bucket.copy());
                        value.addProperty("bucket_chest", chestIndex);
                        value.addProperty("bucket_chest_x", chestPos.getX());
                        value.addProperty("bucket_chest_y", chestPos.getY());
                        value.addProperty("bucket_chest_z", chestPos.getZ());
                        value.addProperty("bucket_slot", slot);
                        value.addProperty("bucket_id", bucket.getItem().getRegistryName().toString());
                        value.addProperty("bucket_tag_snbt",
                                bucket.hasTagCompound() ? bucket.getTagCompound().toString() : "");
                    }
                    fluids.add(value);
                });
        return fluids;
    }

    private static JsonArray capturePlayer(WorldServer world, List<ResourceLocation> itemIds) {
        JsonArray players = new JsonArray();
        FakePlayer player = FakePlayerFactory.get(world,
                new GameProfile(UUID.fromString("75d746b1-d457-4f47-a8f9-02c7669c2d74"),
                        "BaseMetalsFixture"));
        JsonObject entry = new JsonObject();
        entry.addProperty("uuid", player.getUniqueID().toString());
        entry.addProperty("name", player.getName());
        JsonArray inventory = new JsonArray();
        for (int slot = 0; slot < Math.min(9, itemIds.size()); slot++) {
            ResourceLocation id = itemIds.get(slot);
            ItemStack stack = new ItemStack(ForgeRegistries.ITEMS.getValue(id));
            NBTTagCompound fixtureTag = stack.getSubCompound("basemetals_fixture", true);
            fixtureTag.setString("player_proof", id.toString());
            player.inventory.setInventorySlotContents(slot, stack);
            JsonObject value = new JsonObject();
            value.addProperty("slot", slot);
            value.addProperty("id", id.toString());
            inventory.add(value);
        }
        entry.add("inventory", inventory);
        world.getSaveHandler().getPlayerNBTManager().writePlayerData(player);
        players.add(entry);
        return players;
    }

    private static List<ResourceLocation> ids(Iterable<ResourceLocation> keys) {
        List<ResourceLocation> result = new ArrayList<>();
        for (ResourceLocation id : keys) {
            if (id != null && (id.getResourceDomain().equals("basemetals") || id.getResourceDomain().equals("mmdlib"))) {
                result.add(id);
            }
        }
        return result.stream().sorted(Comparator.comparing(ResourceLocation::toString)).collect(Collectors.toList());
    }

    private static boolean isBaseName(String name) {
        return name != null && (name.startsWith("basemetals") || name.startsWith("mmdlib"));
    }

    private static boolean isBaseBlock(Fluid fluid) {
        if (fluid.getBlock() == null || fluid.getBlock().getRegistryName() == null) return false;
        String namespace = fluid.getBlock().getRegistryName().getResourceDomain();
        return namespace.equals("basemetals") || namespace.equals("mmdlib");
    }

    private static BlockPos statePosition(int ordinal) {
        return new BlockPos(1_024 + (ordinal % 32) * 4, 80, 1_024 + (ordinal / 32) * 4);
    }

    private static BlockPos chestPosition(int ordinal, int y) {
        return new BlockPos(-1_024 + (ordinal % 16) * 3, y, 1_024 + (ordinal / 16) * 3);
    }

    private static void prepareSupport(WorldServer world, BlockPos pos, IBlockState state) {
        for (EnumFacing direction : EnumFacing.values()) {
            world.setBlockState(pos.offset(direction), Blocks.BEDROCK.getDefaultState(), 2);
        }
        if (state.getBlock() instanceof BlockDoor) {
            BlockDoor.EnumDoorHalf half = state.getValue(BlockDoor.HALF);
            BlockPos companionPos = half == BlockDoor.EnumDoorHalf.LOWER ? pos.up() : pos.down();
            IBlockState companion = state.withProperty(BlockDoor.HALF,
                    half == BlockDoor.EnumDoorHalf.LOWER
                            ? BlockDoor.EnumDoorHalf.UPPER : BlockDoor.EnumDoorHalf.LOWER);
            world.setBlockState(companionPos, companion, 2);
            BlockPos lower = half == BlockDoor.EnumDoorHalf.LOWER ? pos : companionPos;
            world.setBlockState(lower.down(), Blocks.BEDROCK.getDefaultState(), 2);
        }
    }

    private static void write(Path path, JsonObject json) throws IOException {
        Files.createDirectories(path.getParent());
        Files.write(path, (GSON.toJson(json) + "\n").getBytes(StandardCharsets.UTF_8));
    }
}
