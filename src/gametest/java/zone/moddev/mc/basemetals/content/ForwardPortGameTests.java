package zone.moddev.mc.basemetals.content;

import java.util.List;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.GameTestDontPrefix;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.compat.VanillaCopper;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

@GameTestHolder(value = BaseMetals.MOD_ID, namespace = BaseMetals.MOD_ID)
@GameTestDontPrefix
public final class ForwardPortGameTests {
    private ForwardPortGameTests() {}

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void everyRangedMaterialContributesItsDamage(GameTestHelper helper) {
        var player = helper.makeMockServerPlayerInLevel();
        int checked = 0;
        for (var entry : ModContent.itemsById().entrySet()) {
            if (!entry.getKey().endsWith("_bow")) continue;
            MaterialItems.Bow bow = (MaterialItems.Bow) entry.getValue().get();
            String material = bow.baseMetalsMaterial().name();
            BaseMetalAmmoItem arrow = (BaseMetalAmmoItem) ModContent.item(material + "_arrow").get();
            BaseMetalAmmoItem bolt = (BaseMetalAmmoItem) ModContent.item(material + "_bolt").get();
            require(helper, arrow.kind() == BaseMetalAmmoItem.Kind.ARROW && bolt.kind() == BaseMetalAmmoItem.Kind.BOLT,
                    material + " ammunition identity changed");
            AbstractArrow shot = bow.customArrow(arrow.createArrow(helper.getLevel(), arrow.getDefaultInstance(), player,
                    bow.getDefaultInstance()));
            require(helper, close(shot.getBaseDamage(), 2 * bow.baseMetalsMaterial().baseAttackDamage()),
                    material + " did not use launcher plus ammunition damage");
            AbstractArrow ordinary = ((ArrowItem) Items.ARROW).createArrow(helper.getLevel(), Items.ARROW.getDefaultInstance(), player, null);
            require(helper, ordinary.getBaseDamage() == 2.0D, "Vanilla arrow damage changed");
            require(helper, close(bow.customArrow(ordinary).getBaseDamage(), 1 + bow.baseMetalsMaterial().baseAttackDamage()),
                    material + " bow with vanilla ammunition has the wrong damage");
            require(helper, close(arrow.createArrow(helper.getLevel(), arrow.getDefaultInstance(), player, null).getBaseDamage(),
                    1 + arrow.baseMetalsMaterial().baseAttackDamage()), "Vanilla launcher ammunition contribution changed");
            checked++;
        }
        require(helper, checked == 27, "Expected all 27 ranged material families, found " + checked);
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void rawCrushingAndSilkTouchInventoriesRemainSeparate(GameTestHelper helper) {
        int checked = 0;
        for (var material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ItemStack raw = material.name().equals("copper") ? Items.RAW_COPPER.getDefaultInstance()
                    : ModContent.item(material.name() + "_raw").get().getDefaultInstance();
            var crushed = helper.getLevel().getRecipeManager().getRecipeFor(CrushingRecipe.TYPE.get(),
                    new SingleRecipeInput(raw), helper.getLevel()).orElseThrow();
            require(helper, crushed.value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY).getCount() == 1, material.name() + " raw material was doubled");
            var ore = helper.getLevel().getRecipeManager().getRecipeFor(CrushingRecipe.TYPE.get(),
                    new SingleRecipeInput(ModContent.item(material.name() + "_ore").get().getDefaultInstance()),
                    helper.getLevel()).orElseThrow();
            require(helper, ore.value().getResultItem(net.minecraft.core.RegistryAccess.EMPTY).getCount() == 2, material.name() + " ore hammer yield changed");
            checked++;
        }
        require(helper, checked == 13, "Expected all 13 ore-bearing materials");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void thinTransparentBlocksDoNotHideTheirNeighbours(GameTestHelper helper) {
        BlockPos position = helper.absolutePos(new BlockPos(1, 1, 1));
        for (var entry : ModContent.blocksById().entrySet()) {
            if (!List.of("door", "trapdoor", "bars").stream().anyMatch(form -> entry.getKey().endsWith("_" + form))) continue;
            var block = entry.getValue().get();
            for (var state : block.getStateDefinition().getPossibleStates()) {
                require(helper, !state.canOcclude(), entry.getKey() + " hides neighbouring block faces");
                require(helper, !state.getCollisionShape(helper.getLevel(), position).isEmpty(),
                        entry.getKey() + " lost its collision shape");
            }
        }
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void vanillaCopperKeepsExistingRecipeChainsUsable(GameTestHelper helper) {
        var level = helper.getLevel();

        for (var entry : VanillaCopper.equivalents().entrySet()) {
            var legacy = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(entry.getKey()));
            var vanilla = ForgeRegistries.ITEMS.getValue(ResourceLocation.parse(entry.getValue()));
            var tag = ItemTags.create(ResourceLocation.parse(VanillaCopper.ingredientTag(entry.getKey())));

            require(helper, legacy != null && legacy != Items.AIR && vanilla != null && vanilla != Items.AIR,
                    "A copper save identity or its vanilla equivalent is missing: " + entry);
            require(helper, legacy.getDefaultInstance().is(tag) && vanilla.getDefaultInstance().is(tag),
                    "Copper equivalents cannot share an ingredient: " + entry);

            String path = entry.getKey().substring("basemetals:".length());
            String recipeId = path.equals("copper_ingot") ? "copper_ingot_from_nuggets" : path;
            var recipe = level.getRecipeManager().byKey(ResourceLocation.fromNamespaceAndPath(
                    BaseMetals.MOD_ID, recipeId)).orElseThrow();
            require(helper, recipe.value().getResultItem(level.registryAccess()).is(vanilla),
                    recipeId + " still crafts the old duplicate item");
        }

        for (ItemStack ingot : List.of(Items.COPPER_INGOT.getDefaultInstance(),
                ModContent.item("copper_ingot").get().getDefaultInstance())) {
            var input = CraftingInput.of(3, 1,
                    List.of(ingot.copy(), ingot.copy(), ingot.copy()));
            var plate = level.getRecipeManager().getRecipeFor(RecipeType.CRAFTING,
                    input, level).orElseThrow().value().assemble(input, level.registryAccess());
            require(helper, plate.is(ModContent.item("copper_plate").get()) && plate.getCount() == 3,
                    "Copper plate crafting rejected a supported ingot");
        }

        for (var entry : java.util.Map.of(Items.COPPER_TRAPDOOR, 3,
                ModContent.item("copper_trapdoor").get(), 4).entrySet()) {
            var input = new SingleRecipeInput(entry.getKey().getDefaultInstance());
            ItemStack recycled = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
                    input, level).orElseThrow().value().assemble(input, level.registryAccess());
            require(helper, recycled.is(Items.COPPER_INGOT) && recycled.getCount() == entry.getValue(),
                    "Copper trapdoor recycling used the wrong material value");
        }

        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void nativeEnchantmentsStillSupportMaterialEquipment(GameTestHelper helper) {
        var enchantments = helper.getLevel().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        int checked = 0;

        for (var registered : ModContent.itemsById().values()) {
            var item = registered.get();
            ItemStack stack = item.getDefaultInstance();
            if (!(item instanceof MaterialBacked) || !stack.isDamageableItem()) continue;

            require(helper, enchantments.getOrThrow(Enchantments.UNBREAKING)
                    .value().canEnchant(stack), "Unbreaking no longer supports " + stack);

            if (item instanceof BowItem) {
                require(helper, enchantments.getOrThrow(Enchantments.POWER)
                        .value().canEnchant(stack), "Power no longer supports " + stack);
            }
            if (item instanceof DiggerItem || item instanceof ShearsItem) {
                require(helper, enchantments.getOrThrow(Enchantments.EFFICIENCY)
                        .value().canEnchant(stack), "Efficiency no longer supports " + stack);
            }
            if (item instanceof FishingRodItem) {
                require(helper, enchantments.getOrThrow(Enchantments.LURE)
                        .value().canEnchant(stack), "Lure no longer supports " + stack);
            }
            if (item instanceof ArmorItem) {
                require(helper, enchantments.getOrThrow(Enchantments.PROTECTION)
                        .value().canEnchant(stack), "Protection no longer supports " + stack);
            }

            checked++;
        }

        require(helper, checked > 300, "The complete equipment catalogue was not checked");
        helper.succeed();
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 0.0001D;
    }

    private static void require(GameTestHelper helper, boolean value, String message) {
        if (!value) helper.fail(message);
    }
}
