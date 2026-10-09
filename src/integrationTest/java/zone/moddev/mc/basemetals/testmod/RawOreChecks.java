package zone.moddev.mc.basemetals.testmod;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.world.BlockEvent;
import zone.moddev.mc.basemetals.BaseMetalsEvents;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

/** Checks ore loot and both ways of hammering each raw metal. */
final class RawOreChecks {
    private RawOreChecks() {}

    static int run(MinecraftServer server) {
        ServerLevel world = server.overworld();
        BlockPos position = world.getSharedSpawnPos().above(12);
        FakePlayer player = FakePlayerFactory.getMinecraft(world);
        player.setPos(position.getX(), position.getY(), position.getZ());
        ItemStack pickaxe = new ItemStack(Items.NETHERITE_PICKAXE);
        ItemStack fortune = pickaxe.copy();
        fortune.enchant(Enchantments.BLOCK_FORTUNE, 3);
        ItemStack silk = pickaxe.copy();
        silk.enchant(Enchantments.SILK_TOUCH, 1);
        int checks = 0;

        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            String name = material.name();
            Block ore = ModContent.blocksById().get(name + "_ore").get();
            Item raw = name.equals("copper") ? Items.RAW_COPPER : ModContent.item(name + "_raw").get();
            Item powder = ModContent.item(name + "_powder").get();
            int ordinaryMaximum = name.equals("copper") ? 3 : 1;
            int ordinaryMinimum = name.equals("copper") ? 2 : 1;

            require(pickaxe.isCorrectToolForDrops(ore.defaultBlockState()), "adequate pickaxe " + name);
            require(!new ItemStack(Items.WOODEN_PICKAXE).isCorrectToolForDrops(ore.defaultBlockState())
                    || ModContent.requiredHarvestLevel(name) == 0, "minimum mining tier " + name);
            assertLoot(world, ore, position, pickaxe, raw, ordinaryMinimum, ordinaryMaximum);
            assertLoot(world, ore, position, silk, ore.asItem(), 1, 1);
            int highest = 0;
            for (int sample = 0; sample < 32; sample++) {
                highest = Math.max(highest, assertLoot(world, ore, position, fortune, raw,
                        ordinaryMinimum, ordinaryMaximum * 4));
            }
            require(highest > ordinaryMaximum, "Fortune increases raw drops " + name);

            LootContext.Builder explosion = new LootContext.Builder(world).withRandom(new Random(123))
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(position))
                    .withParameter(LootContextParams.BLOCK_STATE, ore.defaultBlockState())
                    .withParameter(LootContextParams.TOOL, pickaxe)
                    .withParameter(LootContextParams.EXPLOSION_RADIUS, 16.0F);
            List<ItemStack> exploded = server.getLootTables().get(ore.getLootTable())
                    .getRandomItems(explosion.create(LootContextParamSets.BLOCK));
            require(exploded.stream().allMatch(stack -> stack.isEmpty()
                    || (stack.getItem() == raw && stack.getCount() <= ordinaryMaximum)),
                    "explosion cannot duplicate raw " + name);

            checkCrushing(world, ore.asItem(), powder, 2);
            checkCrushing(world, raw, powder, 1);
            if (!name.equals("copper")) {
                Item ingot = ModContent.item(name + "_ingot").get();
                checkCooking(world, raw, ingot, RecipeType.SMELTING);
                checkCooking(world, raw, ingot, RecipeType.BLASTING);
            }
            checkHammer(world, player, position, ore, raw, powder);
            checkBlendRejection(world, raw);
            checks += 42;
        }

        for (String name : new String[] {"iron", "gold", "copper"}) {
            Item raw = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new net.minecraft.resources.ResourceLocation("minecraft", "raw_" + name));
            Item ingot = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new net.minecraft.resources.ResourceLocation("minecraft", name + "_ingot"));
            checkCrushing(world, raw, ModContent.item(name + "_powder").get(), 1);
            checkCooking(world, raw, ingot, RecipeType.SMELTING);
            checkCooking(world, raw, ingot, RecipeType.BLASTING);
            checkBlendRejection(world, raw);
            checks += 3;
        }
        for (String name : new String[] {"iron", "gold", "copper", "coal", "diamond", "emerald", "lapis", "redstone"}) {
            Item ordinaryOre = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new net.minecraft.resources.ResourceLocation("minecraft", name + "_ore"));
            ItemStack expected = world.getRecipeManager().getRecipeFor(CrushingRecipe.TYPE,
                    new SimpleContainer(new ItemStack(ordinaryOre)), world).orElseThrow().getResultItem();
            Item ore = net.minecraftforge.registries.ForgeRegistries.ITEMS.getValue(
                    new net.minecraft.resources.ResourceLocation("minecraft", "deepslate_" + name + "_ore"));
            checkCrushing(world, ore, expected.getItem(), expected.getCount());
            checks++;
        }
        org.apache.logging.log4j.LogManager.getLogger("basemetalsprobe")
                .info("BASEMETALS_RAW_ORE_PROBE PASS checks={}", checks);
        return checks;
    }

    private static int assertLoot(ServerLevel world, Block ore, BlockPos position, ItemStack tool,
            Item expected, int minimum, int maximum) {
        List<ItemStack> drops = Block.getDrops(ore.defaultBlockState(), world, position, null, null, tool);
        require(drops.size() == 1 && drops.get(0).getItem() == expected, "ore drop identity " + ore);
        int count = drops.get(0).getCount();
        require(count >= minimum && count <= maximum, "ore drop count " + ore + " " + count);
        return count;
    }

    private static void checkCrushing(ServerLevel world, Item input, Item output, int count) {
        CrushingRecipe recipe = world.getRecipeManager().getRecipeFor(CrushingRecipe.TYPE,
                new SimpleContainer(new ItemStack(input)), world).orElseThrow();
        require(recipe.getResultItem().getItem() == output && recipe.getResultItem().getCount() == count,
                "crushing yield " + input);
    }

    private static void checkBlendRejection(ServerLevel world, Item raw) {
        ItemStack input = new ItemStack(raw);
        for (Recipe<?> recipe : world.getRecipeManager().getRecipes()) {
            if (!recipe.getId().getNamespace().equals("basemetals")
                    || !recipe.getId().getPath().contains("blend")) continue;
            require(recipe.getIngredients().stream().noneMatch(ingredient -> ingredient.test(input)),
                    "raw material cannot replace blend powder " + raw + " in " + recipe.getId());
        }
    }

    private static void checkCooking(ServerLevel world, Item input, Item output, RecipeType type) {
        Recipe recipe = (Recipe) world.getRecipeManager().getRecipeFor(type,
                new SimpleContainer(new ItemStack(input)), world).orElseThrow();
        require(recipe.getResultItem().getItem() == output && recipe.getResultItem().getCount() == 1,
                "cooking yield " + input + " " + type);
    }

    private static void checkHammer(ServerLevel world, FakePlayer player, BlockPos position,
            Block ore, Item raw, Item powder) {
        AABB area = new AABB(position).inflate(1);
        world.getEntitiesOfClass(ItemEntity.class, area).forEach(ItemEntity::discard);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModContent.item("adamantine_crackhammer").get()));
        world.setBlock(position, ore.defaultBlockState(), 3);
        BlockEvent.BreakEvent broken = new BlockEvent.BreakEvent(world, position, ore.defaultBlockState(), player);
        new BaseMetalsEvents().onBreak(broken);
        require(broken.isCanceled() && world.isEmptyBlock(position), "direct hammer replaces normal drops " + ore);
        require(world.getEntitiesOfClass(ItemEntity.class, area).stream()
                .map(ItemEntity::getItem).filter(stack -> stack.getItem() == powder)
                .mapToInt(ItemStack::getCount).sum() == 2, "direct hammer gives two powders " + ore);

        world.getEntitiesOfClass(ItemEntity.class, area).forEach(ItemEntity::discard);
        world.setBlock(position, Blocks.STONE.defaultBlockState(), 3);
        ItemEntity ground = new ItemEntity(world, position.getX() + 0.5, position.getY() + 1.2,
                position.getZ() + 0.5, new ItemStack(raw));
        world.addFreshEntity(ground);
        UseOnContext use = new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(position), Direction.UP, position, false));
        require(player.getMainHandItem().getItem().useOn(use) == InteractionResult.SUCCESS,
                "ground hammer accepts raw " + raw);
        require(world.getEntitiesOfClass(ItemEntity.class, area).stream()
                .map(ItemEntity::getItem).filter(stack -> stack.getItem() == powder)
                .mapToInt(ItemStack::getCount).sum() == 1, "ground raw gives one powder " + raw);
        world.getEntitiesOfClass(ItemEntity.class, area).forEach(ItemEntity::discard);
        world.removeBlock(position, false);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalStateException(message);
    }
}
