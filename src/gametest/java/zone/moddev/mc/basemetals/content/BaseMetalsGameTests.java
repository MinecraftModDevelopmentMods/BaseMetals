package zone.moddev.mc.basemetals.content;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.BaseMetalsEvents;
import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;
import zone.moddev.mc.basemetals.recipe.PlateRepairRecipe;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.GameTestDontPrefix;
import net.minecraftforge.registries.ForgeRegistries;

/** Server-lifecycle checks which cannot safely run in a plain JUnit JVM. */
@GameTestHolder(value = BaseMetals.MOD_ID, namespace = BaseMetals.MOD_ID)
@GameTestDontPrefix
public final class BaseMetalsGameTests {
    private static final String EMPTY = "empty";

    private BaseMetalsGameTests() {}

    @net.minecraft.gametest.framework.BeforeBatch(batch = BaseMetals.MOD_ID)
    public static void loadServerConfiguration(net.minecraft.server.level.ServerLevel level) {
        net.minecraftforge.fml.config.ConfigTracker.INSTANCE.loadConfigs(
                net.minecraftforge.fml.config.ModConfig.Type.SERVER,
                level.getServer().getWorldPath(new net.minecraft.world.level.storage.LevelResource("serverconfig")));
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void platesUseTheClickedFaceAndEdge(GameTestHelper helper) {
        PlateBlock plate = (PlateBlock) ModContent.blocksById().get("copper_plate").get();
        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));

        Map<Direction, Vec3> faceCentres = Map.of(
                Direction.DOWN, new Vec3(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D),
                Direction.UP, new Vec3(position.getX() + 0.5D, position.getY() + 1.0D, position.getZ() + 0.5D),
                Direction.NORTH, new Vec3(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ()),
                Direction.SOUTH, new Vec3(position.getX() + 0.5D, position.getY() + 0.5D, position.getZ() + 1.0D),
                Direction.WEST, new Vec3(position.getX(), position.getY() + 0.5D, position.getZ() + 0.5D),
                Direction.EAST, new Vec3(position.getX() + 1.0D, position.getY() + 0.5D, position.getZ() + 0.5D));
        for (Map.Entry<Direction, Vec3> entry : faceCentres.entrySet()) {
            BlockState state = plate.getStateForPlacement(
                    context(helper, player, position, entry.getValue(), entry.getKey()));
            require(helper, state != null && state.getValue(PlateBlock.FACING) == entry.getKey(),
                    "Centred " + entry.getKey() + " placement did not retain the clicked face");
        }

        BlockState centred = plate.getStateForPlacement(context(helper, player, position,
                new Vec3(position.getX() + 0.5D, position.getY() + 1.0D, position.getZ() + 0.5D), Direction.UP));
        BlockState easternEdge = plate.getStateForPlacement(context(helper, player, position,
                new Vec3(position.getX() + 0.95D, position.getY() + 1.0D, position.getZ() + 0.5D), Direction.UP));

        require(helper, centred != null && centred.getValue(PlateBlock.FACING) == Direction.UP,
                "A centred top-face placement did not face upward");
        // Legacy BlockMMDPlate rotated the plate inward from the clicked edge;
        // retaining that WEST result is part of old block-state compatibility.
        require(helper, easternEdge != null && easternEdge.getValue(PlateBlock.FACING) == Direction.WEST,
                "A top-face east-edge placement did not retain legacy west-facing orientation");

        Map<Direction, AABB> expectedBounds = Map.of(
                Direction.DOWN, new AABB(0, 15.0D / 16.0D, 0, 1, 1, 1),
                Direction.UP, new AABB(0, 0, 0, 1, 1.0D / 16.0D, 1),
                Direction.NORTH, new AABB(0, 0, 15.0D / 16.0D, 1, 1, 1),
                Direction.SOUTH, new AABB(0, 0, 0, 1, 1, 1.0D / 16.0D),
                Direction.WEST, new AABB(15.0D / 16.0D, 0, 0, 1, 1, 1),
                Direction.EAST, new AABB(0, 0, 0, 1.0D / 16.0D, 1, 1));
        expectedBounds.forEach((facing, bounds) -> require(helper,
                plate.getShape(plate.defaultBlockState().setValue(PlateBlock.FACING, facing),
                        helper.getLevel(), position, CollisionContext.empty()).bounds().equals(bounds),
                facing + " plate bounds do not match the legacy attachment plane"));
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void humanDetectorIgnoresNonPlayers(GameTestHelper helper) {
        HumanDetectorBlock detector = (HumanDetectorBlock) ModContent.HUMAN_DETECTOR.get();
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));
        helper.spawnWithNoFreeWill(EntityType.COW, 1.5F, 1.0F, 1.5F);
        require(helper, detector.getSignalStrength(helper.getLevel(), position) == 0,
                "Human detector responded to a non-player entity");

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        player.moveTo(position.getX() + 0.5D, position.getY() + 0.1D, position.getZ() + 0.5D);
        helper.getLevel().addFreshEntity(player);
        require(helper, detector.getSignalStrength(helper.getLevel(), position) == 15,
                "Human detector did not respond to a player");
        player.discard();
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void compatibilitySlabsStayDoubleAndHidden(GameTestHelper helper) {
        for (String id : ModContent.hiddenBlocks()) {
            BlockState state = ModContent.blocksById().get(id).get().defaultBlockState();
            require(helper, state.getValue(CompatibilityDoubleSlabBlock.TYPE) == SlabType.DOUBLE,
                    id + " did not default to a double slab");
            require(helper, !ForgeRegistries.ITEMS.containsKey(ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, id)),
                    id + " unexpectedly has a block item");
        }
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void everyFluidHasSourceFlowingBlockAndBucket(GameTestHelper helper) {
        BlockPos position = helper.absolutePos(new BlockPos(2, 1, 2));
        ModContent.fluids().forEach((id, fluid) -> {
            require(helper, ForgeRegistries.FLUIDS.getKey(fluid.source().get()).equals(
                    ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, id)), id + " source fluid is not registered correctly");
            require(helper, ForgeRegistries.FLUIDS.getKey(fluid.flowing().get()).equals(
                    ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "flowing_" + id)), id + " flowing fluid is not registered correctly");
            require(helper, ForgeRegistries.BLOCKS.getKey(fluid.block().get()).equals(
                    ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, id)), id + " fluid block is not registered correctly");
            require(helper, ForgeRegistries.ITEMS.getKey(fluid.bucket().get()).equals(
                    ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, id + "_bucket")), id + " bucket is not registered correctly");
            require(helper, fluid.bucket().get() instanceof BaseMetalBucketItem,
                    id + " does not use the visible functional bucket implementation");

            BucketItem bucket = (BucketItem) fluid.bucket().get();
            require(helper, bucket.getFluid() == fluid.source().get(),
                    id + " bucket does not retain its source fluid");
            // Forge's creative-content rebuild is client-only. The packaged client checks visibility and search.
            require(helper, "items".equals(ModTabs.category(bucket)), id + " bucket has the wrong creative category");

            helper.getLevel().setBlock(position, Blocks.AIR.defaultBlockState(), 3);
            require(helper, bucket.emptyContents(null, helper.getLevel(), position, null,
                    bucket.getDefaultInstance()), id + " bucket could not place its fluid");
            require(helper, helper.getLevel().getFluidState(position).getType() == fluid.source().get()
                    && helper.getLevel().getFluidState(position).isSource(),
                    id + " bucket placed the wrong or non-source fluid");
            helper.getLevel().removeBlock(position, false);
        });
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void crushingRecipesLoadAndMatchOre(GameTestHelper helper) {
        MaterialCatalogue.ALL.stream().filter(material -> material.hasOre()).forEach(material ->
                assertCrushingRecipe(helper, ModContent.item(material.name() + "_ore").get().getDefaultInstance(),
                        material.name() + "_powder", 2));

        assertCrushingRecipe(helper, new ItemStack(Blocks.COAL_ORE), "coal_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_COAL_ORE), "coal_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.COPPER_ORE), "copper_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_COPPER_ORE), "copper_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DIAMOND_ORE), "diamond_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_DIAMOND_ORE), "diamond_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.EMERALD_ORE), "emerald_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_EMERALD_ORE), "emerald_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.GOLD_ORE), "gold_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_GOLD_ORE), "gold_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.NETHER_GOLD_ORE), "gold_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.IRON_ORE), "iron_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_IRON_ORE), "iron_powder", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.LAPIS_ORE), "minecraft:lapis_lazuli", 8);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_LAPIS_ORE), "minecraft:lapis_lazuli", 8);
        assertCrushingRecipe(helper, new ItemStack(Blocks.REDSTONE_ORE), "minecraft:redstone", 8);
        assertCrushingRecipe(helper, new ItemStack(Blocks.DEEPSLATE_REDSTONE_ORE), "minecraft:redstone", 8);
        assertCrushingRecipe(helper, new ItemStack(Blocks.NETHER_QUARTZ_ORE), "minecraft:quartz", 2);
        assertCrushingRecipe(helper, new ItemStack(Blocks.ANCIENT_DEBRIS), "minecraft:netherite_scrap", 2);
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void legacyFurnaceRecipesLoadAndRetainCountedOutputs(GameTestHelper helper) {
        MaterialCatalogue.ALL.forEach(material -> {
            assertSmeltingRecipeInTag(helper,
                    ModContent.item(material.name() + "_powder").get().getDefaultInstance(),
                    "forge:ingots/" + material.name(), 1);
            assertSmeltingRecipeInTag(helper,
                    ModContent.item(material.name() + "_smallpowder").get().getDefaultInstance(),
                    "forge:nuggets/" + material.name(), 1);
            if (material.isAlloy()) {
                assertSmeltingRecipeInTag(helper,
                        ModContent.item(material.name() + "_smallblend").get().getDefaultInstance(),
                        "forge:nuggets/" + material.name(), 1);
            }
        });

        Map<String, String> vanillaPowderOutputs = Map.of(
                "diamond", "minecraft:diamond",
                "emerald", "minecraft:emerald",
                "gold", "minecraft:gold_ingot",
                "iron", "minecraft:iron_ingot",
                "obsidian", "basemetals:obsidian_ingot",
                "quartz", "minecraft:quartz");
        vanillaPowderOutputs.forEach((material, output) ->
                assertSmeltingRecipe(helper, ModContent.item(material + "_powder").get().getDefaultInstance(),
                        output, 1));

        assertNoSmeltingRecipe(helper, ModContent.item("coal_powder").get().getDefaultInstance());
        assertNoSmeltingRecipe(helper, ModContent.item("charcoal_powder").get().getDefaultInstance());
        assertSmeltingRecipe(helper, ModContent.item("tin_crossbow").get().getDefaultInstance(),
                "basemetals:tin_ingot", 4);
        assertSmeltingRecipe(helper, ModContent.item("iron_crackhammer").get().getDefaultInstance(),
                "minecraft:iron_block", 1);
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void stoneCrackhammerCraftsFromTheLegacyMiddleColumnPattern(GameTestHelper helper) {
        CraftingContainer crafting = craftingContainer(3, 3);
        crafting.setItem(1, new ItemStack(Blocks.STONE_BRICKS));
        crafting.setItem(4, new ItemStack(Items.STICK));
        crafting.setItem(7, new ItemStack(Items.STICK));

        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(
                RecipeType.CRAFTING, crafting.asCraftInput(), helper.getLevel());
        require(helper, recipe.isPresent(), "Stone bricks above two sticks matched no crafting recipe");
        if (recipe.isEmpty()) return;
        require(helper, recipe.orElseThrow().id().equals(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "stone_crackhammer")),
                "The legacy stone crackhammer pattern matched the wrong recipe");
        ItemStack output = recipe.orElseThrow().value().assemble(crafting.asCraftInput(), net.minecraft.core.RegistryAccess.EMPTY);
        require(helper, output.is(ModContent.item("stone_crackhammer").get()),
                "The legacy middle-column pattern returned the wrong item");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY, timeoutTicks = 250)
    public static void furnaceProcessesDustAndCountedLegacyRecovery(GameTestHelper helper) {
        BlockPos ironFurnacePosition = new BlockPos(1, 1, 1);
        BlockPos recoveryFurnacePosition = new BlockPos(2, 1, 1);
        helper.setBlock(ironFurnacePosition, Blocks.FURNACE);
        helper.setBlock(recoveryFurnacePosition, Blocks.FURNACE);

        AbstractFurnaceBlockEntity ironFurnace = (AbstractFurnaceBlockEntity)
                helper.getBlockEntity(ironFurnacePosition);
        ironFurnace.setItem(0, ModContent.item("iron_powder").get().getDefaultInstance());
        ironFurnace.setItem(1, new ItemStack(Items.COAL));

        AbstractFurnaceBlockEntity recoveryFurnace = (AbstractFurnaceBlockEntity)
                helper.getBlockEntity(recoveryFurnacePosition);
        recoveryFurnace.setItem(0, ModContent.item("tin_crossbow").get().getDefaultInstance());
        recoveryFurnace.setItem(1, new ItemStack(Items.COAL));

        helper.runAfterDelay(205, () -> {
            assertItemStack(helper, ironFurnace.getItem(2), "minecraft:iron_ingot", 1,
                    "Iron powder furnace output");
            assertItemStack(helper, recoveryFurnace.getItem(2), "basemetals:tin_ingot", 4,
                    "Counted legacy furnace output");
            helper.succeed();
        });
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void crackhammerBreaksOreIntoFixedOutput(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        ItemStack hammer = ModContent.item("stone_crackhammer").get().getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, hammer);
        BlockPos relativePosition = new BlockPos(1, 1, 1);
        BlockPos position = helper.absolutePos(relativePosition);
        helper.setBlock(relativePosition, Blocks.IRON_ORE);

        BlockState state = helper.getLevel().getBlockState(position);
        BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(helper.getLevel(), position, state, player);
        new BaseMetalsEvents().onBreak(event);

        require(helper, event.isCanceled(), "Crackhammer did not replace the vanilla ore break");
        require(helper, helper.getLevel().getBlockState(position).isAir(), "Crackhammer did not remove the ore");
        require(helper, hammer.getDamageValue() == 1, "Crackhammer was not damaged exactly once");
        requireSingleDrop(helper, position, null, "iron_powder", 2);
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void crackhammerGroundCrushingUsesOneItemPerNormalUse(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        ItemStack hammer = ModContent.item("stone_crackhammer").get().getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, hammer);
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));
        ItemEntity source = new ItemEntity(helper.getLevel(), position.getX() + 0.5D,
                position.getY() + 1.1D, position.getZ() + 0.5D,
                new ItemStack(Blocks.DEEPSLATE_COAL_ORE, 2));
        helper.getLevel().addFreshEntity(source);
        BlockHitResult hit = new BlockHitResult(
                new Vec3(position.getX() + 0.5D, position.getY() + 1.0D, position.getZ() + 0.5D),
                Direction.UP, position, false);

        InteractionResult result = hammer.getItem().useOn(
                new UseOnContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, hammer, hit));

        require(helper, result.consumesAction(), "Ground crushing did not consume the interaction");
        require(helper, source.getItem().getCount() == 1, "Normal ground crushing did not consume exactly one ore");
        require(helper, hammer.getDamageValue() == 1, "Ground crushing did not damage the hammer exactly once");
        requireSingleDrop(helper, position.above(), source, "coal_powder", 2);
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void groundHammeringRawMetalsProducesOnePowder(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));
        BlockHitResult hit = new BlockHitResult(Vec3.atCenterOf(position.above()), Direction.UP, position, false);
        var rawItems = new java.util.LinkedHashMap<String, net.minecraft.world.item.Item>();
        ModContent.itemsById().forEach((id, item) -> {
            if (id.endsWith("_raw")) rawItems.put(id.substring(0, id.length() - 4), item.get());
        });
        rawItems.put("iron", Items.RAW_IRON);
        rawItems.put("gold", Items.RAW_GOLD);
        rawItems.put("copper", Items.RAW_COPPER);

        rawItems.forEach((material, item) -> {
            ItemStack hammer = ModContent.item("stone_crackhammer").get().getDefaultInstance();
            player.setItemInHand(InteractionHand.MAIN_HAND, hammer);
            ItemEntity source = new ItemEntity(helper.getLevel(), position.getX() + 0.5D,
                    position.getY() + 1.1D, position.getZ() + 0.5D, new ItemStack(item, 2));
            helper.getLevel().addFreshEntity(source);

            InteractionResult result = hammer.getItem().useOn(
                    new UseOnContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, hammer, hit));

            require(helper, result.consumesAction(), material + " raw crushing did not consume the interaction");
            require(helper, source.getItem().getCount() == 1, material + " raw crushing consumed more than one piece");
            require(helper, hammer.getDamageValue() == 1, material + " raw crushing used the wrong durability");
            requireSingleDrop(helper, position.above(), source, material + "_powder", 1);
            helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(position).inflate(2.0D))
                    .forEach(Entity::discard);
        });
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void scytheHarvestsHorizontalThreeByThreeAndPaysDurability(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        ItemStack scythe = ModContent.item("iron_scythe").get().getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, scythe);
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                // Leaves are stable without a supporting block, unlike short
                // grass placed on the GameTest template's stone floor.
                helper.setBlock(new BlockPos(x, 1, z), Blocks.OAK_LEAVES);
            }
        }

        boolean handled = scythe.getItem().onBlockStartBreak(scythe,
                helper.absolutePos(new BlockPos(1, 1, 1)), player);

        require(helper, handled, "Scythe did not handle a tagged central plant");
        int remaining = 0;
        for (int x = 0; x < 3; x++) {
            for (int z = 0; z < 3; z++) {
                if (!helper.getBlockState(new BlockPos(x, 1, z)).isAir()) remaining++;
            }
        }
        require(helper, remaining == 0,
                "Scythe left " + remaining + " of nine plants; durability=" + scythe.getDamageValue());
        require(helper, scythe.getDamageValue() == 9,
                "Scythe did not take one durability for each of nine harvested blocks");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void legacyToolAttributesAndFuelValuesSurviveThePort(GameTestHelper helper) {
        var bronze = MaterialCatalogue.get("bronze");
        ItemStack hoe = ModContent.item("bronze_hoe").get().getDefaultInstance();
        ItemStack scythe = ModContent.item("bronze_scythe").get().getDefaultInstance();
        ItemStack hammer = ModContent.item("bronze_crackhammer").get().getDefaultInstance();

        require(helper, attribute(hoe, Attributes.ATTACK_DAMAGE) == 0.0D,
                "Hoe added attack damage unlike 1.12");
        require(helper, close(attribute(hoe, Attributes.ATTACK_SPEED),
                bronze.baseAttackDamage() - 3.0F), "Hoe attack speed changed");
        require(helper, close(attribute(scythe, Attributes.ATTACK_DAMAGE), bronze.baseAttackDamage()),
                "Scythe attack damage changed");
        require(helper, attribute(scythe, Attributes.ATTACK_SPEED) == 0.0D,
                "Scythe attack speed changed");
        require(helper, close(attribute(hammer, Attributes.ATTACK_DAMAGE),
                bronze.crackhammerAttackDamage()), "Crackhammer attack damage changed");
        require(helper, attribute(hammer, Attributes.ATTACK_SPEED) == -3.5D,
                "Crackhammer attack speed changed");

        require(helper, burnTime("coal_powder") == 1600, "Coal powder no longer burns for one item");
        require(helper, burnTime("coal_smallpowder") == 200, "Tiny coal dust has the wrong burn time");
        require(helper, burnTime("wood_gear") == 300, "Wood gear has the wrong burn time");
        require(helper, burnTime("charcoal_block") == 16000, "Charcoal block has the wrong burn time");
        MaterialCatalogue.ALL.stream().filter(material -> material.hasEquipment()).forEach(material -> {
            require(helper, burnTime(material.name() + "_powder") == 0,
                    material.name() + " powder must not burn");
            require(helper, burnTime(material.name() + "_nugget") == 0,
                    material.name() + " nugget must not burn");
            require(helper, burnTime(material.name() + "_smallpowder") == 0,
                    material.name() + " small powder must not burn");
        });
        for (String id : List.of(
                "mercury_nugget", "mercury_powder", "mercury_smallpowder",
                "diamond_nugget", "diamond_powder", "diamond_smallpowder",
                "emerald_nugget", "emerald_powder", "emerald_smallpowder",
                "gold_powder", "gold_smallpowder",
                "iron_powder", "iron_smallpowder",
                "obsidian_nugget", "obsidian_powder", "obsidian_smallpowder",
                "quartz_nugget", "quartz_powder", "quartz_smallpowder",
                "redstone_smallpowder", "lapis_smallpowder")) {
            require(helper, burnTime(id) == 0, id + " must not burn");
        }
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void crackhammerUsesLegacySpeedForEveryHarvestableCrushingInput(GameTestHelper helper) {
        ItemStack stoneHammer = ModContent.item("stone_crackhammer").get().getDefaultInstance();
        float expected = ((MaterialBacked) stoneHammer.getItem()).baseMetalsMaterial()
                .crackhammerDestroySpeed();
        require(helper, close(stoneHammer.getDestroySpeed(Blocks.GRAVEL.defaultBlockState()), expected),
                "Crushable gravel retained hand-speed mining");
        require(helper, close(stoneHammer.getDestroySpeed(Blocks.GLASS.defaultBlockState()), expected),
                "Crushable glass retained hand-speed mining");
        require(helper, close(stoneHammer.getDestroySpeed(Blocks.SUGAR_CANE.defaultBlockState()), expected),
                "Crushable sugar cane retained hand-speed mining");
        require(helper, stoneHammer.getDestroySpeed(Blocks.OBSIDIAN.defaultBlockState()) == 1.0F,
                "An under-tier crackhammer mined Obsidian at crackhammer speed");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void legacyBlockCapabilitiesSoundsAndQuartzInteractionSurvive(GameTestHelper helper) {
        MaterialCatalogue.ALL.stream().filter(material -> material.hasEquipment()).forEach(material ->
                require(helper, ModContent.blocksById().get(material.name() + "_block").get()
                        .defaultBlockState().is(BlockTags.BEACON_BASE_BLOCKS),
                        material.name() + " storage block is missing from beacon bases"));
        require(helper, ModContent.blocksById().get("charcoal_block").get().defaultBlockState()
                .is(BlockTags.BEACON_BASE_BLOCKS), "Charcoal block is missing from beacon bases");
        require(helper, !ModContent.blocksById().get("mercury").get().defaultBlockState()
                .is(BlockTags.BEACON_BASE_BLOCKS), "Molten Mercury became a beacon base");

        require(helper, ModContent.blocksById().get("bronze_block").get().defaultBlockState()
                .getSoundType() == SoundType.METAL, "Base-metal storage blocks use the wrong sound");
        require(helper, ModContent.blocksById().get("diamond_wall").get().defaultBlockState()
                .getSoundType() == SoundType.GLASS, "Gem decorations use the wrong sound");
        require(helper, ModContent.blocksById().get("charcoal_block").get().defaultBlockState()
                .getSoundType() == SoundType.SAND, "Charcoal block uses the wrong sound");
        require(helper, ModContent.blocksById().get("tin_ore").get().defaultBlockState()
                .getSoundType() == SoundType.STONE, "Ore blocks use the wrong sound");
        require(helper, ModContent.blocksById().get("steel_anvil").get().defaultBlockState()
                .getSoundType() == SoundType.METAL, "Steel anvil uses the wrong sound");
        require(helper, ModContent.blocksById().get("stone_anvil").get().defaultBlockState()
                .getSoundType() == SoundType.STONE, "Stone anvil uses the wrong sound");

        Player player = helper.makeMockPlayer(GameType.SURVIVAL);
        for (String form : List.of("door", "trapdoor")) {
            BlockPos relative = form.equals("door") ? new BlockPos(1, 1, 1) : new BlockPos(2, 1, 1);
            BlockPos position = helper.absolutePos(relative);
            helper.setBlock(relative, ModContent.blocksById().get("quartz_" + form).get());
            BlockState state = helper.getLevel().getBlockState(position);
            InteractionResult result = state.useWithoutItem(helper.getLevel(), player,
                    new BlockHitResult(Vec3.atCenterOf(position), Direction.UP, position, false));
            require(helper, result.consumesAction(), "Quartz " + form + " cannot be opened by hand");
        }
        BlockPos diamondRelative = new BlockPos(3, 1, 1);
        BlockPos diamondPosition = helper.absolutePos(diamondRelative);
        helper.setBlock(diamondRelative, ModContent.blocksById().get("diamond_door").get());
        BlockState diamond = helper.getLevel().getBlockState(diamondPosition);
        require(helper, diamond.useWithoutItem(helper.getLevel(), player,
                new BlockHitResult(Vec3.atCenterOf(diamondPosition), Direction.UP, diamondPosition, false))
                == InteractionResult.PASS, "Diamond door became hand-openable");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void customAnvilMenusRemainValidWithoutTheVanillaAnvilTag(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        BlockPos relative = new BlockPos(1, 1, 3);
        BlockPos position = helper.absolutePos(relative);
        player.moveTo(position, 0.0F, 0.0F);

        int containerId = 1;
        for (String id : List.of("stone_anvil", "steel_anvil", "adamantine_anvil")) {
            BaseMetalAnvilBlock anvil = (BaseMetalAnvilBlock) ModContent.blocksById().get(id).get();
            helper.setBlock(relative, anvil);
            BlockState state = helper.getLevel().getBlockState(position);
            require(helper, !state.is(BlockTags.ANVIL),
                    id + " must remain outside the vanilla tag which deletes falling custom anvils");

            var provider = anvil.getMenuProvider(state, helper.getLevel(), position);
            require(helper, provider != null, id + " has no menu provider");
            AbstractContainerMenu menu = provider == null ? null
                    : provider.createMenu(containerId++, player.getInventory(), player);
            require(helper, menu != null && menu.stillValid(player),
                    id + " menu rejected its own workstation immediately: state="
                            + ForgeRegistries.BLOCKS.getKey(state.getBlock())
                            + ", workstation=" + position
                            + ", player=" + player.position()
                            + ", distance=" + player.distanceToSqr(Vec3.atCenterOf(position)));

            helper.getLevel().removeBlock(position, false);
            require(helper, menu == null || !menu.stillValid(player),
                    id + " menu remained valid after its workstation was removed");
            if (menu != null) menu.removed(player);
        }
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void smithsRetainTheLegacyMercuryIngotSale(GameTestHelper helper) {
        Int2ObjectOpenHashMap<List<ItemListing>> trades = new Int2ObjectOpenHashMap<>();
        for (int level = 1; level <= 5; level++) trades.put(level, new ArrayList<>());
        BaseMetalsTrades.add(new VillagerTradesEvent(trades, VillagerProfession.ARMORER));
        var trader = EntityType.VILLAGER.create(helper.getLevel());
        boolean found = trades.values().stream().flatMap(List::stream)
                .map(listing -> listing.getOffer(trader, net.minecraft.util.RandomSource.create(0L)))
                .anyMatch(offer -> offer != null
                        && offer.getResult().is(ModContent.item("mercury_ingot").get())
                        && offer.getResult().getCount() == 12);
        require(helper, found, "Armorer lost the legacy 12-Mercury-ingot sale");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void creativeTabsRetainTheirLegacyStarsteelIcons(GameTestHelper helper) {
        require(helper, ModTabs.BLOCKS.get().getIconItem().is(ModContent.item("starsteel_block").get()),
                "Blocks tab did not retain the Starsteel block icon");
        require(helper, ModTabs.ITEMS.get().getIconItem().is(ModContent.item("starsteel_gear").get()),
                "Items tab did not retain the Starsteel gear icon");
        require(helper, ModTabs.TOOLS.get().getIconItem().is(ModContent.item("starsteel_pickaxe").get()),
                "Tools tab did not retain the Starsteel pickaxe icon");
        require(helper, ModTabs.COMBAT.get().getIconItem().is(ModContent.item("starsteel_sword").get()),
                "Combat tab did not retain the Starsteel sword icon");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void plateRepairKeepsAllItemDataAndResetsDamage(GameTestHelper helper) {
        var recipe = helper.getLevel().getRecipeManager().byKey(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "adamantine_chestplate_plate_repair"));
        require(helper, recipe.isPresent(), "Adamantine chestplate plate-repair recipe did not load");
        if (recipe.isEmpty()) return;

        CraftingContainer crafting = craftingContainer(2, 1);
        ItemStack damaged = ModContent.item("adamantine_chestplate").get().getDefaultInstance();
        damaged.setDamageValue(20);
        damaged.enchant(enchantment(helper, Enchantments.PROTECTION), 2);
        damaged.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal("Old name"));
        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                damaged, tag -> tag.putBoolean("LegacyCustomData", true));
        crafting.setItem(0, damaged);
        crafting.setItem(1, ModContent.item("adamantine_plate").get().getDefaultInstance());

        require(helper, recipe.orElseThrow().value() instanceof PlateRepairRecipe,
                "Plate-repair recipe loaded with the wrong implementation");
        if (!(recipe.orElseThrow().value() instanceof PlateRepairRecipe repair)) return;
        require(helper, repair.matches(crafting.asCraftInput(), helper.getLevel()),
                "Plate-repair recipe rejected a damaged item and matching plate");
        ItemStack output = repair.assemble(crafting.asCraftInput(), net.minecraft.core.RegistryAccess.EMPTY);
        require(helper, output.is(ModContent.item("adamantine_chestplate").get()),
                "Plate repair returned the wrong armor item");
        require(helper, output.getDamageValue() == 0, "Plate repair did not fully restore durability");
        require(helper, EnchantmentHelper.getItemEnchantmentLevel(
                enchantment(helper, Enchantments.PROTECTION), output) == 2,
                "Plate repair lost enchantments");
        require(helper, output.has(net.minecraft.core.component.DataComponents.CUSTOM_NAME), "Plate repair lost the custom name");
        require(helper, output.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBoolean("LegacyCustomData"),
                "Plate repair lost custom item data");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void shieldUpgradeSupportsVanillaBitsPlatesAndKeepsItemData(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        ItemStack shield = ModContent.item("copper_shield").get().getDefaultInstance();
        shield.setDamageValue(10);
        shield.enchant(enchantment(helper, Enchantments.UNBREAKING), 2);
        shield.set(net.minecraft.core.component.DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal("Old shield"));
        net.minecraft.world.item.component.CustomData.update(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                shield, tag -> tag.putBoolean("LegacyCustomData", true));
        AnvilUpdateEvent event = new AnvilUpdateEvent(shield,
                ModContent.item("iron_plate").get().getDefaultInstance(), "", 0, player);

        new BaseMetalsEvents().onShieldUpgrade(event);

        require(helper, event.getOutput().is(ModContent.item("iron_shield").get()),
                "Iron Vanilla Bits plate did not upgrade a softer shield");
        require(helper, event.getOutput().getDamageValue() == 0,
                "Shield upgrade did not produce a fresh shield");
        require(helper, EnchantmentHelper.getItemEnchantmentLevel(enchantment(helper, Enchantments.UNBREAKING),
                event.getOutput()) == 2, "Shield upgrade lost enchantments");
        require(helper, event.getOutput().has(net.minecraft.core.component.DataComponents.CUSTOM_NAME),
                "Shield upgrade lost the custom name");
        require(helper, event.getOutput().getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.EMPTY).copyTag().contains("LegacyCustomData"),
                "Shield upgrade lost its custom data");
        require(helper, event.getMaterialCost() == 1, "Shield upgrade did not consume exactly one plate");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void bowsAndLegacyCrossbowsUseOnlyTheirOwnAmmunition(GameTestHelper helper) {
        MaterialItems.Bow bow = (MaterialItems.Bow) ModContent.item("tin_bow").get();
        MaterialItems.Crossbow crossbow = (MaterialItems.Crossbow) ModContent.item("tin_crossbow").get();
        ItemStack arrow = ModContent.item("tin_arrow").get().getDefaultInstance();
        ItemStack bolt = ModContent.item("tin_bolt").get().getDefaultInstance();
        require(helper, bow.getAllSupportedProjectiles().test(Items.ARROW.getDefaultInstance()),
                "Base Metals bow rejected a vanilla arrow");
        require(helper, bow.getAllSupportedProjectiles().test(arrow),
                "Base Metals bow rejected a Base Metals arrow");
        require(helper, !bow.getAllSupportedProjectiles().test(bolt),
                "Base Metals bow accepted a bolt");
        require(helper, crossbow.getAllSupportedProjectiles().test(bolt),
                "Legacy crossbow rejected a Base Metals bolt");
        require(helper, !crossbow.getAllSupportedProjectiles().test(arrow),
                "Legacy crossbow accepted an arrow");

        ServerPlayer player = survivalPlayer(helper);
        player.moveTo(helper.absolutePos(new BlockPos(1, 2, 1)), 0.0F, 0.0F);
        ItemStack weapon = crossbow.getDefaultInstance();
        player.setItemInHand(InteractionHand.MAIN_HAND, weapon);
        player.getInventory().setItem(1, bolt.copy());
        player.startUsingItem(InteractionHand.MAIN_HAND);
        crossbow.releaseUsing(weapon, helper.getLevel(), player, crossbow.getUseDuration(weapon, player) - 20);

        // Assert synchronously: the GameTest harness may purge fast projectiles
        // after they leave its deliberately tiny empty structure.
        var projectiles = new ArrayList<MaterialProjectile>();
        helper.getLevel().getAllEntities().forEach(entity -> {
            if (entity instanceof MaterialProjectile projectile
                    && projectile.getOwner() == player
                    && projectile.getItem().is(ModContent.item("tin_bolt").get())) {
                projectiles.add(projectile);
            }
        });
        require(helper, projectiles.size() == 1,
                "Legacy crossbow projectile count=" + projectiles.size()
                        + ", ammo=" + player.getInventory().getItem(1).getCount()
                        + ", damage=" + weapon.getDamageValue());
        if (!projectiles.isEmpty()) {
            require(helper, projectiles.get(0).getItem().is(ModContent.item("tin_bolt").get()),
                    "Legacy crossbow projectile did not retain its bolt item");
        }
        require(helper, player.getInventory().getItem(1).isEmpty(),
                "Legacy crossbow did not consume a normal bolt");
        require(helper, weapon.getDamageValue() == 1,
                "Legacy crossbow did not take exactly one durability");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void vanillaBitsEquipmentUsesItsActualMaterialForAnvilRepair(GameTestHelper helper) {
        ItemStack diamondBow = ModContent.item("diamond_bow").get().getDefaultInstance();
        ItemStack emeraldSword = ModContent.item("emerald_sword").get().getDefaultInstance();
        ItemStack quartzArmor = ModContent.item("quartz_chestplate").get().getDefaultInstance();
        ItemStack stoneScythe = ModContent.item("stone_scythe").get().getDefaultInstance();
        ItemStack woodScythe = ModContent.item("wood_scythe").get().getDefaultInstance();

        require(helper, diamondBow.getItem().isValidRepairItem(diamondBow, new ItemStack(Items.DIAMOND)),
                "Diamond bow rejected a diamond repair ingredient");
        require(helper, emeraldSword.getItem().isValidRepairItem(emeraldSword, new ItemStack(Items.EMERALD)),
                "Emerald sword rejected an emerald repair ingredient");
        require(helper, quartzArmor.getItem().isValidRepairItem(quartzArmor, new ItemStack(Items.QUARTZ)),
                "Quartz armor rejected a quartz repair ingredient");
        require(helper, stoneScythe.getItem().isValidRepairItem(stoneScythe, new ItemStack(Blocks.STONE)),
                "Stone scythe rejected a stone repair ingredient");
        require(helper, woodScythe.getItem().isValidRepairItem(woodScythe, new ItemStack(Blocks.OAK_PLANKS)),
                "Wood scythe rejected a plank repair ingredient");
        require(helper, !diamondBow.getItem().isValidRepairItem(diamondBow, new ItemStack(Items.IRON_INGOT)),
                "Diamond bow accepted an unrelated ingot");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void armorToolEffectsAndStarsteelRegenerationAreServerAuthoritative(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        equip(player, "adamantine");
        player.tickCount = 20;
        BaseMetalsEvents events = new BaseMetalsEvents();
        events.onPlayerTick(new TickEvent.PlayerTickEvent.Post(player));
        require(helper, player.getEffect(MobEffects.DAMAGE_RESISTANCE) != null
                && player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1,
                "Four Adamantine pieces did not grant Resistance II");
        require(helper, player.getEffect(MobEffects.DAMAGE_RESISTANCE).getDuration() == 45,
                "Armor effects linger far beyond the legacy refresh window");

        ItemStack starsteel = ModContent.item("starsteel_pickaxe").get().getDefaultInstance();
        starsteel.setDamageValue(5);
        ItemStack armor = ModContent.item("starsteel_helmet").get().getDefaultInstance();
        armor.setDamageValue(5);
        player.setItemInHand(InteractionHand.MAIN_HAND, starsteel);
        player.setItemInHand(InteractionHand.OFF_HAND, armor);
        player.tickCount = 200;
        events.onPlayerTick(new TickEvent.PlayerTickEvent.Post(player));
        require(helper, starsteel.getDamageValue() == 4,
                "Held Starsteel tool did not regenerate one durability");
        require(helper, armor.getDamageValue() == 5,
                "Starsteel armor regenerated despite the armor exclusion");

        LivingEntity target = helper.spawnWithNoFreeWill(EntityType.IRON_GOLEM, 2.0F, 1.0F, 2.0F);
        player.setItemInHand(InteractionHand.MAIN_HAND,
                ModContent.item("adamantine_sword").get().getDefaultInstance());
        LivingHurtEvent hurt = new LivingHurtEvent(target, player.damageSources().playerAttack(player), 3.0F);
        events.onLivingHurt(hurt);
        require(helper, hurt.getAmount() == 7.0F,
                "Adamantine melee bonus was not applied to a high-health target");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void remainingLegacyArmorEffectsUseTheCorrectPieceCounts(GameTestHelper helper) {
        BaseMetalsEvents events = new BaseMetalsEvents();

        ServerPlayer adamantine = survivalPlayer(helper);
        equip(adamantine, "adamantine");
        adamantine.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
        tickEffects(events, adamantine);
        require(helper, effectAmplifier(adamantine, MobEffects.DAMAGE_RESISTANCE) == 0,
                "Three Adamantine pieces did not grant Resistance I");

        ServerPlayer lead = survivalPlayer(helper);
        equip(lead, "lead");
        tickEffects(events, lead);
        require(helper, effectAmplifier(lead, MobEffects.MOVEMENT_SLOWDOWN) == 1,
                "Four Lead pieces did not grant Slowness II");

        ServerPlayer coldiron = survivalPlayer(helper);
        equip(coldiron, "coldiron");
        tickEffects(events, coldiron);
        require(helper, effectAmplifier(coldiron, MobEffects.FIRE_RESISTANCE) == 0,
                "A full Cold Iron suit did not grant Fire Resistance I");

        ServerPlayer mithril = survivalPlayer(helper);
        equip(mithril, "mithril");
        mithril.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 0));
        tickEffects(events, mithril);
        require(helper, !mithril.hasEffect(MobEffects.POISON),
                "A full Mithril suit did not remove a harmful effect");

        ServerPlayer starsteel = survivalPlayer(helper);
        equip(starsteel, "starsteel");
        tickEffects(events, starsteel);
        require(helper, effectAmplifier(starsteel, MobEffects.JUMP) == 3,
                "Four Starsteel pieces did not grant Jump Boost IV");
        require(helper, effectAmplifier(starsteel, MobEffects.MOVEMENT_SPEED) == 2,
                "Four Starsteel pieces did not grant Speed III");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void remainingLegacyMeleeEffectsUseTargetIdentity(GameTestHelper helper) {
        BaseMetalsEvents events = new BaseMetalsEvents();
        ServerPlayer player = survivalPlayer(helper);

        LivingEntity aquatic = helper.spawnWithNoFreeWill(EntityType.COD, 2.0F, 1.0F, 2.0F);
        player.setItemInHand(InteractionHand.MAIN_HAND,
                ModContent.item("aquarium_sword").get().getDefaultInstance());
        LivingHurtEvent aquariumHit = new LivingHurtEvent(aquatic, player.damageSources().playerAttack(player), 3.0F);
        events.onLivingHurt(aquariumHit);
        require(helper, aquariumHit.getAmount() == 7.0F,
                "Aquarium melee bonus did not use the target's water-breathing identity");

        LivingEntity blaze = helper.spawnWithNoFreeWill(EntityType.BLAZE, 3.0F, 1.0F, 2.0F);
        player.setItemInHand(InteractionHand.MAIN_HAND,
                ModContent.item("coldiron_sword").get().getDefaultInstance());
        LivingHurtEvent coldIronHit = new LivingHurtEvent(blaze, player.damageSources().playerAttack(player), 3.0F);
        events.onLivingHurt(coldIronHit);
        require(helper, coldIronHit.getAmount() == 6.0F,
                "Cold Iron melee bonus did not use the target's fire immunity");

        LivingEntity undead = helper.spawnWithNoFreeWill(EntityType.ZOMBIE, 4.0F, 1.0F, 2.0F);
        player.setItemInHand(InteractionHand.MAIN_HAND,
                ModContent.item("mithril_sword").get().getDefaultInstance());
        LivingHurtEvent mithrilHit = new LivingHurtEvent(undead, player.damageSources().playerAttack(player), 3.0F);
        events.onLivingHurt(mithrilHit);
        require(helper, effectAmplifier(undead, MobEffects.WITHER) == 3,
                "Mithril did not apply Wither IV to undead");
        require(helper, effectAmplifier(undead, MobEffects.BLINDNESS) == 1,
                "Mithril did not apply Blindness II to undead");
        require(helper, undead.getEffect(MobEffects.WITHER).getDuration() == 60
                && undead.getEffect(MobEffects.BLINDNESS).getDuration() == 60,
                "Mithril undead effects did not retain the 60-tick duration");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void eventBackedAdvancementsAwardForActualLegacyActions(GameTestHelper helper) {
        ServerPlayer player = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "basemetals-advancement-test"),
                net.minecraft.server.level.ClientInformation.createDefault());
        player.setGameMode(GameType.SURVIVAL);
        BaseMetalsEvents events = new BaseMetalsEvents();
        events.onItemCrafted(new PlayerEvent.ItemCraftedEvent(player,
                ModContent.item("iron_crackhammer").get().getDefaultInstance(), new SimpleContainer(0)));
        events.onItemCrafted(new PlayerEvent.ItemCraftedEvent(player,
                ModContent.item("bronze_blend").get().getDefaultInstance(), new SimpleContainer(0)));
        events.onItemSmelted(new PlayerEvent.ItemSmeltedEvent(player,
                ModContent.item("bronze_ingot").get().getDefaultInstance()));

        for (String advancement : new String[] {"geologist", "metallurgy", "this_is_new", "bronze_maker"}) {
            var definition = player.getServer().getAdvancements().get(
                    ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, advancement));
            require(helper, definition != null
                    && player.getAdvancements().getOrStartProgress(definition).isDone(),
                    "Legacy action did not award advancement " + advancement);
        }
        helper.succeed();
    }

    private static void assertCrushingRecipe(GameTestHelper helper, ItemStack input,
            String expectedItem, int expectedCount) {
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(
                CrushingRecipe.TYPE.get(), new net.minecraft.world.item.crafting.SingleRecipeInput(input), helper.getLevel());
        require(helper, recipe.isPresent(), "No crushing recipe loaded for " + input);
        ItemStack result = recipe.orElseThrow().value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY);
        ResourceLocation expectedId = expectedItem.contains(":")
                ? ResourceLocation.parse(expectedItem)
                : ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, expectedItem);
        require(helper, ForgeRegistries.ITEMS.getKey(recipe.orElseThrow().value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY).getItem()).equals(
                expectedId), input + " crushed to the wrong output");
        require(helper, result.getCount() == expectedCount,
                input + " crushed to " + result.getCount() + " items instead of " + expectedCount);
    }

    private static void tickEffects(BaseMetalsEvents events, ServerPlayer player) {
        player.tickCount = 20;
        events.onPlayerTick(new TickEvent.PlayerTickEvent.Post(player));
    }

    private static int effectAmplifier(LivingEntity entity, net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect) {
        MobEffectInstance instance = entity.getEffect(effect);
        return instance == null ? -1 : instance.getAmplifier();
    }

    private static void assertSmeltingRecipe(GameTestHelper helper, ItemStack input,
            String expectedItem, int expectedCount) {
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(
                RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), helper.getLevel());
        require(helper, recipe.isPresent(), "No furnace recipe loaded for " + input);
        if (recipe.isEmpty()) return;
        ItemStack result = recipe.orElseThrow().value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY);
        ResourceLocation actualItem = ForgeRegistries.ITEMS.getKey(result.getItem());
        require(helper, actualItem.equals(ResourceLocation.parse(expectedItem)),
                input + " smelted to " + actualItem + " instead of " + expectedItem);
        require(helper, result.getCount() == expectedCount,
                input + " smelted to " + result.getCount() + " items instead of " + expectedCount);
    }

    private static void assertSmeltingRecipeInTag(GameTestHelper helper, ItemStack input,
            String expectedTag, int expectedCount) {
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(
                RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), helper.getLevel());
        require(helper, recipe.isPresent(), "No furnace recipe loaded for " + input);
        if (recipe.isEmpty()) return;
        ItemStack result = recipe.orElseThrow().value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY);
        TagKey<net.minecraft.world.item.Item> tag = TagKey.create(
                net.minecraft.core.registries.Registries.ITEM, ResourceLocation.parse(expectedTag));
        ResourceLocation actualItem = ForgeRegistries.ITEMS.getKey(result.getItem());
        require(helper, result.is(tag),
                input + " smelted to " + actualItem + ", which is outside #" + expectedTag);
        require(helper, result.getCount() == expectedCount,
                input + " smelted to " + result.getCount() + " items instead of " + expectedCount);
    }

    private static void assertNoSmeltingRecipe(GameTestHelper helper, ItemStack input) {
        var recipe = helper.getLevel().getRecipeManager().getRecipeFor(
                RecipeType.SMELTING, new net.minecraft.world.item.crafting.SingleRecipeInput(input), helper.getLevel());
        require(helper, recipe.isEmpty(), input + " unexpectedly has a furnace recipe");
    }

    private static void assertItemStack(GameTestHelper helper, ItemStack stack, String expectedItem,
            int expectedCount, String description) {
        require(helper, ForgeRegistries.ITEMS.getKey(stack.getItem()).equals(
                ResourceLocation.parse(expectedItem)), description + " used the wrong item");
        require(helper, stack.getCount() == expectedCount,
                description + " contained " + stack.getCount() + " items instead of " + expectedCount);
    }

    private static void requireSingleDrop(GameTestHelper helper, BlockPos position, ItemEntity excluded,
            String expectedItem, int expectedCount) {
        var drops = helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(position).inflate(0.25D),
                entity -> entity != excluded);
        require(helper, drops.size() == 1, "Expected one crushing output entity but found " + drops.size());
        if (drops.size() != 1) return;
        ItemStack output = drops.get(0).getItem();
        require(helper, ForgeRegistries.ITEMS.getKey(output.getItem()).equals(
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, expectedItem)), "Crushing produced the wrong item");
        require(helper, output.getCount() == expectedCount,
                "Crushing produced " + output.getCount() + " items instead of " + expectedCount);
    }

    private static ServerPlayer survivalPlayer(GameTestHelper helper) {
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(
                new GameProfile(UUID.randomUUID(), "BMTest"), false);
        ServerPlayer player = new ServerPlayer(helper.getLevel().getServer(), helper.getLevel(),
                cookie.gameProfile(), cookie.clientInformation());
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        helper.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        player.setGameMode(GameType.SURVIVAL);
        return player;
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void projectilesRetainAmmunitionAcrossSaveAndSpawnData(GameTestHelper helper) {
        Player shooter = helper.makeMockPlayer(GameType.SURVIVAL);
        ItemStack ammunition = ModContent.item("starsteel_bolt").get().getDefaultInstance();
        MaterialProjectile original = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel(), shooter, ammunition);
        require(helper, original.getAddEntityPacket(null).getClass().getName().contains("CustomPayload"),
                "Projectile uses the vanilla packet, so Forge ammunition data cannot reach the client");

        CompoundTag saved = new CompoundTag();
        original.addAdditionalSaveData(saved);
        MaterialProjectile loaded = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel());
        loaded.readAdditionalSaveData(saved);
        require(helper, ItemStack.isSameItemSameComponents(ammunition, loaded.getItem()),
                "Projectile ammunition was not retained through save/load");

        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            original.writeSpawnData(buffer);
            MaterialProjectile spawned = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel());
            spawned.readSpawnData(buffer);
            require(helper, ItemStack.isSameItemSameComponents(ammunition, spawned.getItem()),
                    "Projectile ammunition was not retained through spawn data");
        } finally {
            buffer.release();
        }
        helper.succeed();
    }

    private static CraftingContainer craftingContainer(int width, int height) {
        return new net.minecraft.world.inventory.TransientCraftingContainer(new AbstractContainerMenu(null, -1) {
            @Override
            public ItemStack quickMoveStack(Player player, int slot) { return ItemStack.EMPTY; }
            @Override
            public boolean stillValid(Player player) {
                return true;
            }
        }, width, height);
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void oreLootSupportsRawFortuneSilkTouchAndHammering(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper);
        ItemStack pickaxe = Items.NETHERITE_PICKAXE.getDefaultInstance();
        ItemStack silk = pickaxe.copy();
        silk.enchant(enchantment(helper, Enchantments.SILK_TOUCH), 1);
        ItemStack fortune = pickaxe.copy();
        fortune.enchant(enchantment(helper, Enchantments.FORTUNE), 3);
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));
        for (var entry : ModContent.blocksById().entrySet()) {
            if (!entry.getKey().endsWith("_ore")) continue;
            String material = entry.getKey().replace("_ore", "");
            var state = entry.getValue().get().defaultBlockState();
            ItemStack expected = material.equals("copper") ? Items.RAW_COPPER.getDefaultInstance()
                    : ModContent.item(material + "_raw").get().getDefaultInstance();
            var ordinary = Block.getDrops(state, helper.getLevel(), position, null, player, pickaxe);
            require(helper, ordinary.size() == 1 && ordinary.get(0).is(expected.getItem()),
                    material + " ordinary mining did not drop raw metal");
            require(helper, ordinary.get(0).getCount() == 1 || material.equals("copper")
                    && ordinary.get(0).getCount() >= 2 && ordinary.get(0).getCount() <= 3,
                    material + " ordinary raw drop count changed");
            var silkDrops = Block.getDrops(state, helper.getLevel(), position, null, player, silk);
            require(helper, silkDrops.size() == 1 && silkDrops.get(0).is(state.getBlock().asItem())
                    && silkDrops.get(0).getCount() == 1, material + " Silk Touch lost the ore block");
            boolean increased = false;
            for (int attempt = 0; attempt < 32; attempt++) {
                var drops = Block.getDrops(state, helper.getLevel(), position, null, player, fortune);
                require(helper, drops.size() == 1 && drops.get(0).is(expected.getItem()), material + " Fortune used the wrong item");
                int count = drops.get(0).getCount();
                require(helper, count >= 1 && count <= (material.equals("copper") ? 12 : 4), material + " Fortune drop count=" + count);
                increased |= count > (material.equals("copper") ? 3 : 1);
            }
            require(helper, increased, material + " Fortune never increased raw drops");

            helper.getLevel().setBlock(position, state, 3);
            player.setItemInHand(InteractionHand.MAIN_HAND, ModContent.item("adamantine_crackhammer").get().getDefaultInstance());
            var event = new net.minecraftforge.event.level.BlockEvent.BreakEvent(helper.getLevel(), position, state, player);
            new BaseMetalsEvents().onBreak(event);
            require(helper, event.isCanceled() && helper.getLevel().getBlockState(position).isAir(), material + " hammer did not harvest the ore");
            requireSingleDrop(helper, position, null, material + "_powder", 2);
            helper.getLevel().getEntitiesOfClass(ItemEntity.class, new AABB(position).inflate(1.0D)).forEach(Entity::discard);
        }
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = EMPTY)
    public static void fullyDrawnWeaponsApplyMaterialDamageBeforePower(GameTestHelper helper) {
        for (String material : List.of("gold", "steel", "adamantine")) {
            for (boolean crossbow : List.of(false, true)) {
                for (int power : List.of(0, 3)) {
                    ServerPlayer player = survivalPlayer(helper);
                    player.moveTo(helper.absolutePos(new BlockPos(1, 2, 1)), 0.0F, 0.0F);
                    String launcher = material + (crossbow ? "_crossbow" : "_bow");
                    String ammo = material + (crossbow ? "_bolt" : "_arrow");
                    ItemStack weapon = ModContent.item(launcher).get().getDefaultInstance();
                    if (power > 0) weapon.enchant(enchantment(helper, Enchantments.POWER), power);
                    player.setItemInHand(InteractionHand.MAIN_HAND, weapon);
                    player.getInventory().setItem(1, ModContent.item(ammo).get().getDefaultInstance());
                    player.startUsingItem(InteractionHand.MAIN_HAND);
                    weapon.getItem().releaseUsing(weapon, helper.getLevel(), player, weapon.getUseDuration(player) - 20);
                    var projectiles = new ArrayList<MaterialProjectile>();
                    helper.getLevel().getAllEntities().forEach(entity -> {
                        if (entity instanceof MaterialProjectile arrow && arrow.getOwner() == player) projectiles.add(arrow);
                    });
                    double baseDamage = ((MaterialBacked) weapon.getItem()).baseMetalsMaterial().baseAttackDamage() * 2.0D;
                    require(helper, projectiles.size() == 1 && close(projectiles.get(0).getBaseDamage(), baseDamage),
                            launcher + " did not keep the material base damage=" + baseDamage);

                    MaterialProjectile projectile = projectiles.getFirst();
                    var target = EntityType.ZOMBIE.create(helper.getLevel());
                    double damage = EnchantmentHelper.modifyDamage(helper.getLevel(), projectile.getWeaponItem(), target,
                            helper.getLevel().damageSources().arrow(projectile, player), (float) baseDamage);
                    var vanilla = ((net.minecraft.world.item.ArrowItem) Items.ARROW)
                            .createArrow(helper.getLevel(), Items.ARROW.getDefaultInstance(), player, weapon);
                    double vanillaDamage = EnchantmentHelper.modifyDamage(helper.getLevel(), weapon, target,
                            helper.getLevel().damageSources().arrow(vanilla, player), 2.0F);
                    double expected = baseDamage + vanillaDamage - 2.0D;
                    require(helper, power == 0 || vanillaDamage > 2.0D, "The vanilla Power reference did not apply");
                    require(helper, close(damage, expected), launcher + " Power " + power + " damage=" + damage
                            + " instead of " + expected);
                    require(helper, player.getInventory().getItem(1).isEmpty() && weapon.getDamageValue() == 1,
                            launcher + " did not consume ammunition and durability");
                    projectiles.forEach(Entity::discard);
                }
            }
        }
        helper.succeed();
    }

    private static double attribute(ItemStack stack, net.minecraft.core.Holder<Attribute> attribute) {
        return stack.getOrDefault(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS,
                net.minecraft.world.item.component.ItemAttributeModifiers.EMPTY).modifiers().stream()
                .filter(entry -> entry.slot().test(EquipmentSlot.MAINHAND) && entry.attribute().equals(attribute))
                .mapToDouble(entry -> entry.modifier().amount()).sum();
    }

    private static net.minecraft.core.Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment(
            GameTestHelper helper, net.minecraft.resources.ResourceKey<net.minecraft.world.item.enchantment.Enchantment> key) {
        return helper.getLevel().registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.ENCHANTMENT)
                .getOrThrow(key);
    }

    private static int burnTime(String item) {
        ItemStack stack = ModContent.item(item).get().getDefaultInstance();
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
    }

    private static boolean close(double left, double right) {
        return Math.abs(left - right) < 0.0001D;
    }

    private static void equip(ServerPlayer player, String material) {
        player.setItemSlot(EquipmentSlot.HEAD,
                ModContent.item(material + "_helmet").get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.CHEST,
                ModContent.item(material + "_chestplate").get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.LEGS,
                ModContent.item(material + "_leggings").get().getDefaultInstance());
        player.setItemSlot(EquipmentSlot.FEET,
                ModContent.item(material + "_boots").get().getDefaultInstance());
    }

    private static BlockPlaceContext context(GameTestHelper helper, Player player, BlockPos position,
            Vec3 hit, Direction face) {
        return new BlockPlaceContext(helper.getLevel(), player, InteractionHand.MAIN_HAND, ItemStack.EMPTY,
                new BlockHitResult(hit, face, position, false));
    }

    private static void require(GameTestHelper helper, boolean condition, String message) {
        if (!condition) helper.fail(message);
    }
}
