package zone.moddev.mc.basemetals.content;

import java.util.List;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

@GameTestHolder(BaseMetals.MOD_ID)
@PrefixGameTestTemplate(false)
public final class ForwardPortGameTests {
    private ForwardPortGameTests() {}

    @GameTest(template = "empty")
    public static void everyRangedMaterialContributesItsDamage(GameTestHelper helper) {
        var player = FakePlayerFactory.get(helper.getLevel(),
                new GameProfile(UUID.randomUUID(), "ranged-material-test"));
        int checked = 0;
        for (var entry : ModContent.itemsById().entrySet()) {
            if (!entry.getKey().endsWith("_bow")) continue;
            MaterialItems.Bow bow = (MaterialItems.Bow) entry.getValue().get();
            String material = bow.baseMetalsMaterial().name();
            BaseMetalAmmoItem arrow = (BaseMetalAmmoItem) ModContent.item(material + "_arrow").get();
            BaseMetalAmmoItem bolt = (BaseMetalAmmoItem) ModContent.item(material + "_bolt").get();
            require(helper, arrow.kind() == BaseMetalAmmoItem.Kind.ARROW && bolt.kind() == BaseMetalAmmoItem.Kind.BOLT,
                    material + " ammunition identity changed");
            AbstractArrow shot = bow.customArrow(arrow.createArrow(helper.getLevel(), arrow.getDefaultInstance(), player));
            require(helper, close(shot.getBaseDamage(), 2 * bow.baseMetalsMaterial().baseAttackDamage()),
                    material + " did not use launcher plus ammunition damage");
            AbstractArrow ordinary = ((ArrowItem) Items.ARROW).createArrow(helper.getLevel(), Items.ARROW.getDefaultInstance(), player);
            require(helper, ordinary.getBaseDamage() == 2.0D, "Vanilla arrow damage changed");
            require(helper, close(bow.customArrow(ordinary).getBaseDamage(), 1 + bow.baseMetalsMaterial().baseAttackDamage()),
                    material + " bow with vanilla ammunition has the wrong damage");
            require(helper, close(arrow.createArrow(helper.getLevel(), arrow.getDefaultInstance(), player).getBaseDamage(),
                    1 + arrow.baseMetalsMaterial().baseAttackDamage()), "Vanilla launcher ammunition contribution changed");
            checked++;
        }
        require(helper, checked == 27, "Expected all 27 ranged material families, found " + checked);
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void rawCrushingAndSilkTouchInventoriesRemainSeparate(GameTestHelper helper) {
        int checked = 0;
        for (var material : MaterialCatalogue.ALL) {
            if (!material.hasOre()) continue;
            ItemStack raw = material.name().equals("copper") ? Items.RAW_COPPER.getDefaultInstance()
                    : ModContent.item(material.name() + "_raw").get().getDefaultInstance();
            var crushed = helper.getLevel().getRecipeManager().getRecipeFor(CrushingRecipe.TYPE.get(),
                    new SimpleContainer(raw), helper.getLevel()).orElseThrow();
            require(helper, crushed.getResultItem().getCount() == 1, material.name() + " raw material was doubled");
            var ore = helper.getLevel().getRecipeManager().getRecipeFor(CrushingRecipe.TYPE.get(),
                    new SimpleContainer(ModContent.item(material.name() + "_ore").get().getDefaultInstance()),
                    helper.getLevel()).orElseThrow();
            require(helper, ore.getResultItem().getCount() == 2, material.name() + " ore hammer yield changed");
            checked++;
        }
        require(helper, checked == 13, "Expected all 13 ore-bearing materials");
        helper.succeed();
    }

    @GameTest(template = "empty")
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

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 0.0001D;
    }

    private static void require(GameTestHelper helper, boolean value, String message) {
        if (!value) helper.fail(message);
    }
}
