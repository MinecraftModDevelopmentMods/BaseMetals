package zone.moddev.mc.basemetals;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.AnvilUpdateEvent;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;

@GameTestHolder(BaseMetals.MOD_ID)
@PrefixGameTestTemplate(false)
public final class NativePortGameTests {
    static {
        StructureUtils.testStructuresDir = Path.of(System.getProperty("basemetals.testStructures")).toString();
    }

    private NativePortGameTests() {}

    @GameTest(template = "empty")
    public static void harderCommonPlatesUpgradeShields(GameTestHelper helper) {
        ItemStack shield = ModContent.item("gold_shield").get().getDefaultInstance();
        ItemStack plate = ModContent.item("adamantine_plate").get().getDefaultInstance();
        AnvilUpdateEvent event = new AnvilUpdateEvent(shield, plate, null, 0,
                helper.makeMockPlayer(GameType.SURVIVAL));

        new BaseMetalsEvents().onShieldUpgrade(event);

        helper.assertTrue(event.getOutput().is(ModContent.item("adamantine_shield").get()),
                "A harder plate in c:plates must upgrade a shield");
        helper.assertTrue(event.getMaterialCost() == 1, "An upgrade must consume one plate");
        helper.assertTrue(event.getCost() >= 5, "An upgrade must keep its experience cost");
        helper.assertTrue(shield.getCount() == 1 && plate.getCount() == 1,
                "Calculating the output must not consume the inputs");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void oldAmmunitionKeepsItsComponentsAndReloads(GameTestHelper helper) {
        for (String form : List.of("arrow", "bolt")) {
            CompoundTag ammunition = oldAmmunition(form);
            CompoundTag original = ammunition.copy();
            CompoundTag entity = new CompoundTag();
            entity.put("Ammunition", ammunition);

            MaterialProjectile projectile = projectile(helper, form);
            projectile.readAdditionalSaveData(entity);
            ItemStack item = projectile.getItem();

            helper.assertTrue(item.is(ModContent.item("adamantine_" + form).get()),
                    "Old ammunition must keep its material and form");
            helper.assertTrue(item.getHoverName().getString().equals("An old projectile"),
                    "Old ammunition must keep its custom name");
            helper.assertTrue(item.get(DataComponents.LORE).lines().getFirst().getString().equals("Before components"),
                    "Old ammunition must keep its lore");
            helper.assertTrue(!item.getEnchantments().isEmpty(), "Old ammunition must keep its enchantments");
            helper.assertTrue(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
                    .copyTag().getInt("Proof") == 147, "Old ammunition must keep custom data");
            helper.assertTrue(ammunition.equals(original), "Loading ammunition must not rewrite the source tag");

            CompoundTag saved = new CompoundTag();
            projectile.addAdditionalSaveData(saved);
            MaterialProjectile reloaded = projectile(helper, form);
            reloaded.readAdditionalSaveData(saved);

            helper.assertTrue(ItemStack.matches(item, reloaded.getItem()), "Converted ammunition must survive reload");
        }

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void currentAmmunitionTakesPrecedence(GameTestHelper helper) {
        MaterialProjectile projectile = projectile(helper, "bolt");
        ItemStack item = ModContent.item("steel_bolt").get().getDefaultInstance();
        item.set(DataComponents.CUSTOM_NAME, Component.literal("A current bolt"));

        CompoundTag entity = new CompoundTag();
        entity.put("item", item.save(helper.getLevel().registryAccess()));
        entity.put("Ammunition", oldAmmunition("arrow"));
        projectile.readAdditionalSaveData(entity);

        helper.assertTrue(ItemStack.matches(item, projectile.getItem()),
                "A stale Ammunition tag must not replace the current saved item");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void anEmptyBoltDoesNotBecomeAnArrow(GameTestHelper helper) {
        helper.assertTrue(projectile(helper, "bolt").getItem().is(ModContent.item("copper_bolt").get()),
                "A bolt without saved ammunition needs a bolt fallback");
        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 600)
    public static void everyRecipeSurvivesNetworkEncoding(GameTestHelper helper) {
        int checked = 0;

        for (var holder : helper.getLevel().getRecipeManager().getRecipes()) {
            if (!holder.id().getNamespace().equals(BaseMetals.MOD_ID)) {
                continue;
            }

            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(),
                    helper.getLevel().registryAccess());

            try {
                Recipe.STREAM_CODEC.encode(buffer, holder.value());
                Recipe<?> decoded = Recipe.STREAM_CODEC.decode(buffer);

                helper.assertTrue(decoded.getSerializer() == holder.value().getSerializer(),
                        "Recipe serializer changed: " + holder.id());
                helper.assertTrue(ItemStack.matches(holder.value().getResultItem(helper.getLevel().registryAccess()),
                        decoded.getResultItem(helper.getLevel().registryAccess())),
                        "Recipe output changed: " + holder.id());
                checked++;
            } finally {
                buffer.release();
            }
        }

        helper.assertTrue(checked > 2_000, "The recipe test must cover the full catalogue");
        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void smithTradesCreateEnchantedEquipment(GameTestHelper helper) {
        Villager villager = new Villager(net.minecraft.world.entity.EntityType.VILLAGER, helper.getLevel());

        for (VillagerProfession profession : List.of(VillagerProfession.ARMORER,
                VillagerProfession.TOOLSMITH, VillagerProfession.WEAPONSMITH)) {
            var trades = new Int2ObjectOpenHashMap<List<VillagerTrades.ItemListing>>();

            for (int level = 1; level <= 5; level++) {
                trades.put(level, new ArrayList<>());
            }

            BaseMetalsTrades.add(new VillagerTradesEvent(trades, profession, helper.getLevel().registryAccess()));
            int enchanted = 0;

            for (var listings : trades.values()) {
                for (var listing : listings) {
                    var offer = listing.getOffer(villager, RandomSource.create(147));

                    helper.assertTrue(offer != null && !offer.getResult().isEmpty(),
                            "A smith trade must create its advertised item");

                    if (offer.getResult().isEnchanted()) {
                        enchanted++;
                    }
                }
            }

            helper.assertTrue(enchanted > 0, "Each smith profession needs working enchanted trades");
        }

        helper.succeed();
    }

    @GameTest(template = "empty", timeoutTicks = 600)
    public static void villageChestsReceiveBaseMetalsLoot(GameTestHelper helper) {
        var server = helper.getLevel().getServer();

        for (String profession : List.of("armorer", "toolsmith", "weaponsmith")) {
            var table = server.reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE,
                    ResourceLocation.withDefaultNamespace("chests/village/village_" + profession)));
            LootParams params = new LootParams.Builder(helper.getLevel())
                    .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                    .create(LootContextParamSets.CHEST);
            int additions = 0;

            for (long seed = 1; seed <= 32; seed++) {
                additions += table.getRandomItems(params, seed).stream()
                        .filter(item -> net.minecraft.core.registries.BuiltInRegistries.ITEM
                                .getKey(item.getItem()).getNamespace().equals(BaseMetals.MOD_ID))
                        .count();
            }

            helper.assertTrue(additions > 0, "No Base Metals loot reached the " + profession + " chest");
        }

        helper.succeed();
    }

    @GameTest(template = "empty")
    public static void recipeUnlocksLoadFromTheDataPack(GameTestHelper helper) {
        var advancements = helper.getLevel().getServer().getAdvancements();

        helper.assertTrue(advancements.get(ResourceLocation.parse("basemetals:recipes/adamantine_bow")) != null,
                "The Adamantine bow recipe unlock must load");
        helper.assertTrue(advancements.get(ResourceLocation.parse("basemetals:juggernaut")) != null,
                "The armour advancement must load");
        helper.succeed();
    }

    private static MaterialProjectile projectile(GameTestHelper helper, String form) {
        return new MaterialProjectile(form.equals("bolt") ? ModEntities.CUSTOM_BOLT.get()
                : ModEntities.CUSTOM_ARROW.get(), helper.getLevel());
    }

    private static CompoundTag oldAmmunition(String form) {
        CompoundTag ammunition = new CompoundTag();
        ammunition.putString("id", "basemetals:adamantine_" + form);
        ammunition.putByte("Count", (byte) 1);

        CompoundTag data = new CompoundTag();
        data.putInt("Proof", 147);
        CompoundTag display = new CompoundTag();
        display.putString("Name", "{\"text\":\"An old projectile\"}");
        ListTag lore = new ListTag();
        lore.add(StringTag.valueOf("{\"text\":\"Before components\"}"));
        display.put("Lore", lore);
        data.put("display", display);

        CompoundTag enchantment = new CompoundTag();
        enchantment.putString("id", "minecraft:unbreaking");
        enchantment.putShort("lvl", (short) 2);
        ListTag enchantments = new ListTag();
        enchantments.add(enchantment);
        data.put("Enchantments", enchantments);
        ammunition.put("tag", data);

        return ammunition;
    }
}
