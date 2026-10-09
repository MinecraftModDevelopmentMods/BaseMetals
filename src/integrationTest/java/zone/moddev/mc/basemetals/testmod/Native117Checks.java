package zone.moddev.mc.basemetals.testmod;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.LootTable;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.RegistryHandle;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.migration.LegacyWorldDataHook;

/** Checks native loot, molten-fluid attributes and village smith trades. */
final class Native117Checks {
    private Native117Checks() {}

    static int run(MinecraftServer server) {
        ServerLevel world = server.getLevel(Level.OVERWORLD);
        int checks = BlockOcclusionChecks.run();

        for (Map.Entry<String, RegistryHandle<Block>> entry : ModContent.blocksById().entrySet()) {
            String name = entry.getKey();
            if (ModContent.fluids().containsKey(name)) continue;

            Block block = entry.getValue().get();
            BlockState state = block.defaultBlockState();
            if (block instanceof WallBlock) {
                checks += checkOldWallConnections(block);
                for (WallSide height : WallSide.values()) {
                    BlockState connected = state.setValue(WallBlock.NORTH_WALL, height)
                            .setValue(WallBlock.EAST_WALL, height)
                            .setValue(WallBlock.SOUTH_WALL, height)
                            .setValue(WallBlock.WEST_WALL, height);
                    require(connected.getValue(WallBlock.NORTH_WALL) == height,
                            "wall connection state missing: " + name + " " + height);
                    checks++;
                }
            }
            if (block instanceof DoorBlock) state = state.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);
            String dropName = name.endsWith("_ore") ? name.replace("_ore", "_raw")
                    : name.startsWith("double_") ? name.substring(7) : name;
            int count = name.startsWith("double_") ? 2 : 1;
            if (name.equals("copper_ore")) {
                List<ItemStack> copperDrops = drops(world, state);
                require(copperDrops.size() == 1 && copperDrops.get(0).getItem() == Items.RAW_COPPER
                        && copperDrops.get(0).getCount() >= 2 && copperDrops.get(0).getCount() <= 3, "native copper loot");
                checks++;
                continue;
            }
            assertDrop(world, state, dropName, count);
            checks++;

            if (block instanceof SlabBlock) {
                assertDrop(world, state.setValue(SlabBlock.TYPE, SlabType.DOUBLE), dropName, 2);
                checks++;
            } else if (block instanceof DoorBlock) {
                require(drops(world, state.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER)).isEmpty(),
                        "upper door half duplicates the item: " + name);
                checks++;
            } else if (block instanceof BaseMetalAnvilBlock) {
                for (int damage = 1; damage <= 2; damage++) {
                    ItemStack drop = drops(world, state.setValue(BaseMetalAnvilBlock.DAMAGE, damage)).get(0);
                    require(drop.getOrCreateTag().getCompound("BlockStateTag").getString("damage")
                            .equals(String.valueOf(damage)), "anvil wear lost in loot: " + name);
                    checks++;
                }
            }
        }

        checks += checkCrushing(world, Items.NETHER_GOLD_ORE,
                ModContent.item("gold_powder").get(), 2);
        checks += checkCrushing(world, Items.ANCIENT_DEBRIS, Items.NETHERITE_SCRAP, 2);
        require(!world.getRecipeManager().getRecipeFor(CrushingRecipe.TYPE,
                new SimpleContainer(new ItemStack(Items.GILDED_BLACKSTONE)), world).isPresent(),
                "Gilded Blackstone must not have a crushing route");
        checks++;

        for (net.minecraft.world.item.Item item : net.minecraftforge.registries.ForgeRegistries.ITEMS) {
            if (!(item instanceof zone.moddev.mc.basemetals.content.MaterialItems.Hoe)) continue;
            require(item.getDefaultAttributeModifiers(net.minecraft.world.entity.EquipmentSlot.MAINHAND)
                    .get(net.minecraft.world.entity.ai.attributes.Attributes.ATTACK_DAMAGE).stream()
                    .allMatch(modifier -> modifier.getAmount() == 0.0D), "hoe gained attack damage: " + item);
            checks++;
        }

        for (FluidContent fluid : ModContent.fluids().values()) {
            require(fluid.source().get().getAttributes().getStillTexture()
                    .equals(new ResourceLocation("basemetals", "block/molten_metal_still")), "fluid still sprite");
            require(fluid.source().get().getAttributes().getFlowingTexture()
                    .equals(new ResourceLocation("basemetals", "block/molten_metal_flow")), "fluid flowing sprite");
            require(fluid.source().get().getAttributes().getColor()
                    == (0xff000000 | ModContent.fluidColour(fluid.source().get())), "native fluid colour");
            checks += 3;
        }

        for (VillagerProfession profession : new VillagerProfession[] {
                VillagerProfession.ARMORER, VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH}) {
            int baseMetalsOffers = 0;
            for (Map.Entry<Integer, VillagerTrades.ItemListing[]> level : VillagerTrades.TRADES.get(profession).entrySet()) {
                for (VillagerTrades.ItemListing factory : level.getValue()) {
                    if (!factory.getClass().getName().startsWith("zone.moddev.mc.basemetals.")) continue;
                    MerchantOffer offer = factory.getOffer(null, new Random(1));
                    if (offer == null) continue;
                    baseMetalsOffers++;
                    require(offer.getBaseCostA().getItem() == Items.EMERALD
                            || offer.getResult().getItem() == Items.EMERALD, "smith price currency");
                    require(ContentPolicy.active().allows(offer.getResult().getItem().getRegistryName().toString()),
                            "smith bypasses content policy");
                    require(offer.getMaxUses() == 12 && offer.getXp() > 0, "smith restocking/experience");
                    checks += 3;
                }
            }
            require((baseMetalsOffers > 0) == BaseMetalsConfig.VILLAGER_TRADES.get(), "smith offers missing");
            checks++;
        }

        for (String chest : new String[] {"armorer", "toolsmith", "weaponsmith"}) {
            LootTable table = server.getLootTables().get(
                    new ResourceLocation("minecraft", "chests/village/village_" + chest));
            require(table.getPool("basemetals_injection") != null, "village chest additions missing: " + chest);
            checks++;
        }

        return checks;
    }

    private static int checkOldWallConnections(Block block) {
        String[] directions = {"north", "east", "south", "west"};

        for (int mask = 0; mask < 16; mask++) {
            CompoundTag properties = new CompoundTag();
            properties.putString("up", "true");
            for (int side = 0; side < directions.length; side++) {
                properties.putString(directions[side], String.valueOf((mask & (1 << side)) != 0));
            }

            CompoundTag savedState = new CompoundTag();
            savedState.putString("Name", block.getRegistryName().toString());
            savedState.put("Properties", properties);
            ListTag palette = new ListTag();
            palette.add(savedState);
            CompoundTag section = new CompoundTag();
            section.put("Palette", palette);
            ListTag sections = new ListTag();
            sections.add(section);
            CompoundTag level = new CompoundTag();
            level.put("Sections", sections);
            CompoundTag chunk = new CompoundTag();
            chunk.put("Level", level);

            LegacyWorldDataHook.prepareLegacyChunk(chunk);
            BlockState converted = NbtUtils.readBlockState(savedState);
            require(converted.getBlock() == block, "old wall became a different block: " + block);
            for (int side = 0; side < directions.length; side++) {
                String expected = (mask & (1 << side)) != 0 ? "low" : "none";
                require(properties.getString(directions[side]).equals(expected),
                        "old wall connection was lost: " + block + " " + directions[side]);
            }
            require(converted.getValue(WallBlock.UP), "old wall post was lost: " + block);
        }

        return 16 * 6;
    }

    private static int checkCrushing(ServerLevel world, net.minecraft.world.item.Item input,
            net.minecraft.world.item.Item output, int count) {
        SimpleContainer inventory = new SimpleContainer(new ItemStack(input));
        CrushingRecipe recipe = world.getRecipeManager().getRecipeFor(CrushingRecipe.TYPE, inventory, world)
                .orElseThrow(() -> new IllegalStateException("No crushing recipe for " + input));
        ItemStack result = recipe.assemble(inventory);
        require(result.getItem() == output && result.getCount() == count,
                "incorrect Nether processing result: " + input + " -> " + result);
        return 1;
    }

    private static List<ItemStack> drops(ServerLevel world, BlockState state) {
        return Block.getDrops(state, world, new BlockPos(0, 100, 0), null,
                net.minecraftforge.common.util.FakePlayerFactory.getMinecraft(world),
                new ItemStack(Items.DIAMOND_PICKAXE));
    }

    private static void assertDrop(ServerLevel world, BlockState state, String item, int count) {
        List<ItemStack> drops = drops(world, state);
        require(drops.size() == 1 && drops.get(0).getItem() == ModContent.item(item).get()
                && drops.get(0).getCount() == count, "incorrect block loot: " + state + " " + drops);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("Native 1.17 check failed: " + message);
    }
}
