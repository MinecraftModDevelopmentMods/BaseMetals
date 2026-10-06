package zone.moddev.mc.basemetals.testmod;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootTable;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.content.BaseMetalAnvilBlock;
import zone.moddev.mc.basemetals.content.FluidContent;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.RegistryHandle;

/** Checks native loot, molten-fluid attributes and village smith trades. */
final class Native115Checks {
    private Native115Checks() {}

    static int run(MinecraftServer server) {
        ServerWorld world = server.getWorld(DimensionType.OVERWORLD);
        int checks = 0;

        for (Map.Entry<String, RegistryHandle<Block>> entry : ModContent.blocksById().entrySet()) {
            String name = entry.getKey();
            if (ModContent.fluids().containsKey(name)) continue;

            Block block = entry.getValue().get();
            BlockState state = block.getDefaultState();
            if (block instanceof DoorBlock) state = state.with(DoorBlock.HALF, DoubleBlockHalf.LOWER);
            String dropName = name.startsWith("double_") ? name.substring(7) : name;
            int count = name.startsWith("double_") ? 2 : 1;
            assertDrop(world, state, dropName, count);
            checks++;

            if (block instanceof SlabBlock) {
                assertDrop(world, state.with(SlabBlock.TYPE, SlabType.DOUBLE), dropName, 2);
                checks++;
            } else if (block instanceof DoorBlock) {
                require(drops(world, state.with(DoorBlock.HALF, DoubleBlockHalf.UPPER)).isEmpty(),
                        "upper door half duplicates the item: " + name);
                checks++;
            } else if (block instanceof BaseMetalAnvilBlock) {
                for (int damage = 1; damage <= 2; damage++) {
                    ItemStack drop = drops(world, state.with(BaseMetalAnvilBlock.DAMAGE, damage)).get(0);
                    require(drop.getOrCreateTag().getCompound("BlockStateTag").getString("damage")
                            .equals(String.valueOf(damage)), "anvil wear lost in loot: " + name);
                    checks++;
                }
            }
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
            for (Map.Entry<Integer, VillagerTrades.ITrade[]> level : VillagerTrades.VILLAGER_DEFAULT_TRADES.get(profession).entrySet()) {
                for (VillagerTrades.ITrade factory : level.getValue()) {
                    if (!factory.getClass().getName().startsWith("zone.moddev.mc.basemetals.")) continue;
                    MerchantOffer offer = factory.getOffer(null, new Random(1));
                    if (offer == null) continue;
                    baseMetalsOffers++;
                    require(offer.getBuyingStackFirst().getItem() == Items.EMERALD
                            || offer.getSellingStack().getItem() == Items.EMERALD, "smith price currency");
                    require(ContentPolicy.active().allows(offer.getSellingStack().getItem().getRegistryName().toString()),
                            "smith bypasses content policy");
                    require(offer.func_222214_i() == 12 && offer.getGivenExp() > 0, "smith restocking/experience");
                    checks += 3;
                }
            }
            require((baseMetalsOffers > 0) == BaseMetalsConfig.VILLAGER_TRADES.get(), "smith offers missing");
            checks++;
        }

        for (String chest : new String[] {"armorer", "toolsmith", "weaponsmith"}) {
            LootTable table = server.getLootTableManager().getLootTableFromLocation(
                    new ResourceLocation("minecraft", "chests/village/village_" + chest));
            require(table.getPool("basemetals_injection") != null, "village chest additions missing: " + chest);
            checks++;
        }

        return checks;
    }

    private static List<ItemStack> drops(ServerWorld world, BlockState state) {
        return Block.getDrops(state, world, new BlockPos(0, 100, 0), null,
                net.minecraftforge.common.util.FakePlayerFactory.getMinecraft(world),
                new ItemStack(Items.DIAMOND_PICKAXE));
    }

    private static void assertDrop(ServerWorld world, BlockState state, String item, int count) {
        List<ItemStack> drops = drops(world, state);
        require(drops.size() == 1 && drops.get(0).getItem() == ModContent.item(item).get()
                && drops.get(0).getCount() == count, "incorrect block loot: " + state + " " + drops);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException("Native 1.15 check failed: " + message);
    }
}
