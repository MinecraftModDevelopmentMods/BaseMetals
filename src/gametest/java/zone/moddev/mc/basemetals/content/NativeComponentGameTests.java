package zone.moddev.mc.basemetals.content;

import io.netty.buffer.Unpooled;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.gametest.GameTestDontPrefix;
import net.minecraftforge.gametest.GameTestHolder;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;

@GameTestHolder(value = BaseMetals.MOD_ID, namespace = BaseMetals.MOD_ID)
@GameTestDontPrefix
public final class NativeComponentGameTests {
    private NativeComponentGameTests() {}

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty", timeoutTicks = 600)
    public static void everyNativeRecipeSurvivesNetworkEncoding(GameTestHelper helper) {
        int checked = 0;
        for (var holder : helper.getLevel().getRecipeManager().getRecipes()) {
            if (!holder.id().getNamespace().equals(BaseMetals.MOD_ID)) continue;
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
            try {
                Recipe.STREAM_CODEC.encode(buffer, holder.value());
                Recipe<?> decoded = Recipe.STREAM_CODEC.decode(buffer);
                helper.assertTrue(decoded.getSerializer() == holder.value().getSerializer(), "Recipe serializer changed: " + holder.id());
                helper.assertTrue(net.minecraft.world.item.ItemStack.matches(
                        holder.value().getResultItem(helper.getLevel().registryAccess()),
                        decoded.getResultItem(helper.getLevel().registryAccess())), "Recipe output changed: " + holder.id());
                helper.assertTrue(decoded.isSpecial() == holder.value().isSpecial(), "Recipe discovery changed: " + holder.id());
                checked++;
            } finally {
                buffer.release();
            }
        }
        helper.assertTrue(checked > 2000, "The native recipe round-trip did not cover the catalogue");
        helper.succeed();
    }

    @GameTest(batch = BaseMetals.MOD_ID, template = "empty")
    public static void oldEmbeddedBoltsKeepTheirCustomItemData(GameTestHelper helper) {
        CompoundTag ammunition = new CompoundTag();
        ammunition.putString("id", "basemetals:adamantine_bolt");
        ammunition.putByte("Count", (byte) 1);
        CompoundTag data = new CompoundTag();
        data.putInt("Proof", 147);
        CompoundTag display = new CompoundTag();
        display.putString("Name", "{\"text\":\"An old bolt\"}");
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf("{\"text\":\"Saved before components\"}"));
        display.put("Lore", lore);
        data.put("display", display);
        ammunition.put("tag", data);
        CompoundTag entity = new CompoundTag();
        entity.put("Ammunition", ammunition);

        MaterialProjectile projectile = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel());
        projectile.readAdditionalSaveData(entity);
        var item = projectile.getItem();
        helper.assertTrue(item.is(ModContent.item("adamantine_bolt").get()), "An old bolt changed its material");
        helper.assertTrue(item.getHoverName().getString().equals("An old bolt"), "An old bolt lost its name");
        helper.assertTrue(item.get(DataComponents.LORE).lines().getFirst().getString().equals("Saved before components"), "An old bolt lost its lore");
        helper.assertTrue(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getInt("Proof") == 147,
                "An old bolt lost custom data");
        CompoundTag saved = new CompoundTag();
        projectile.addAdditionalSaveData(saved);
        MaterialProjectile reloaded = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel());
        reloaded.readAdditionalSaveData(saved);
        helper.assertTrue(net.minecraft.world.item.ItemStack.matches(item, reloaded.getItem()), "A converted bolt changed on reload");
        helper.succeed();
    }
}
