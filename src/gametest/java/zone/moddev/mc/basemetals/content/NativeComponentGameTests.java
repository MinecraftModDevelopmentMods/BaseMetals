package zone.moddev.mc.basemetals.content;

import io.netty.buffer.Unpooled;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;
import net.minecraftforge.gametest.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.gametest.GameTestDontPrefix;
import net.minecraftforge.gametest.GameTestNamespace;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;

@GameTestNamespace(BaseMetals.MOD_ID)
@GameTestDontPrefix
public final class NativeComponentGameTests {
    private static final net.minecraft.resources.Identifier CAPABILITY_ID =
            net.minecraft.resources.Identifier.parse("basemetals_tests:world_data");
    private static final java.util.Map<net.minecraft.world.level.Level, SavedCapability> CAPABILITIES =
            new java.util.WeakHashMap<>();

    private NativeComponentGameTests() {}

    public static void registerCapabilityProbe() {
        net.minecraftforge.event.AttachCapabilitiesEvent.Levels.BUS.addListener(event -> {
            var capability = new SavedCapability();
            CAPABILITIES.put(event.getObject(), capability);
            event.addCapability(CAPABILITY_ID, capability);
        });
    }

    @GameTest(structure = "basemetals:empty")
    public static void savedWorldCapabilitiesKeepLegacyAndCurrentPayloads(GameTestHelper helper) {
        var codec = net.minecraftforge.common.util.LevelCapabilityData.type(helper.getLevel()).codec();
        var payload = new CompoundTag();
        payload.putString("owner", "Existing world");
        payload.putInt("counter", 37);
        payload.putLongArray("custom_array", new long[] {11, 22, 33});
        var legacy = new CompoundTag();
        legacy.put(CAPABILITY_ID.toString(), payload);

        var loaded = codec.parse(net.minecraft.nbt.NbtOps.INSTANCE, legacy);
        helper.assertTrue(loaded.isSuccess(), "Forge rejected the old capability payload: " + loaded.error());
        var provider = CAPABILITIES.get(helper.getLevel());
        helper.assertTrue(provider != null && payload.equals(provider.received),
                "The legacy capability data did not reach its owner");

        var encoded = codec.encodeStart(net.minecraft.nbt.NbtOps.INSTANCE, loaded.getOrThrow()).getOrThrow();
        helper.assertTrue(encoded instanceof CompoundTag compound
                && payload.equals(compound.getCompoundOrEmpty("data").get(CAPABILITY_ID.toString())),
                "New saves did not use Forge's current capability format");
        provider.received = null;
        helper.assertTrue(codec.parse(net.minecraft.nbt.NbtOps.INSTANCE, encoded).isSuccess()
                && payload.equals(provider.received), "The current capability format did not reload");
        helper.assertTrue(codec.parse(net.minecraft.nbt.NbtOps.INSTANCE, new CompoundTag()).isSuccess(),
                "Empty legacy capability data did not load");

        var malformed = new CompoundTag();
        malformed.putInt("data", 12);
        helper.assertTrue(codec.parse(net.minecraft.nbt.NbtOps.INSTANCE, malformed).isError(),
                "Malformed current data must not be mistaken for a legacy capability");
        helper.succeed();
    }

    private static final class SavedCapability implements
            net.minecraftforge.common.capabilities.ICapabilitySerializable<CompoundTag> {
        private CompoundTag received;

        @Override
        public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(
                net.minecraftforge.common.capabilities.Capability<T> capability, net.minecraft.core.Direction side) {
            return net.minecraftforge.common.util.LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT(net.minecraft.core.HolderLookup.Provider registries) {
            return received == null ? new CompoundTag() : received.copy();
        }

        @Override
        public void deserializeNBT(net.minecraft.core.HolderLookup.Provider registries, CompoundTag data) {
            received = data.copy();
        }
    }

    @GameTest(structure = "basemetals:empty")
    public static void oreHostTagsResolveAfterWorldStartup(GameTestHelper helper) throws Exception {
        helper.assertTrue(zone.moddev.mc.orespawn.api.OreSpawnApi.getProviderStatus(BaseMetals.MOD_ID)
                == zone.moddev.mc.orespawn.api.ProviderStatus.ACTIVE, "The Base Metals provider is not active");

        var resolver = Class.forName("zone.moddev.mc.orespawn.worldgen.OreSpawnOreGeneration")
                .getDeclaredMethod("resolveTag", net.minecraft.tags.TagKey.class);
        resolver.setAccessible(true);

        var expected = java.util.Map.of(
                "minecraft:stone_ore_replaceables", net.minecraft.world.level.block.Blocks.STONE,
                "minecraft:deepslate_ore_replaceables", net.minecraft.world.level.block.Blocks.DEEPSLATE,
                "forge:netherrack", net.minecraft.world.level.block.Blocks.NETHERRACK,
                "forge:end_stones", net.minecraft.world.level.block.Blocks.END_STONE);

        for (var entry : expected.entrySet()) {
            var tag = net.minecraft.tags.TagKey.create(Registries.BLOCK,
                    net.minecraft.resources.Identifier.parse(entry.getKey()));
            var hosts = (java.util.Set<?>) resolver.invoke(null, tag);

            helper.assertTrue(hosts.contains(entry.getValue()),
                    "OreSpawn did not resolve the loaded host tag " + entry.getKey());
        }

        helper.succeed();
    }

    @GameTest(structure = "basemetals:empty", maxTicks = 600)
    public static void everyNativeRecipeSurvivesNetworkEncoding(GameTestHelper helper) {
        int checked = 0;
        for (var holder : helper.getLevel().getServer().getRecipeManager().getRecipes()) {
            if (!holder.id().identifier().getNamespace().equals(BaseMetals.MOD_ID)) continue;
            RegistryFriendlyByteBuf buffer = new RegistryFriendlyByteBuf(Unpooled.buffer(), helper.getLevel().registryAccess());
            try {
                Recipe.STREAM_CODEC.encode(buffer, holder.value());
                byte[] original = new byte[buffer.readableBytes()];
                buffer.getBytes(buffer.readerIndex(), original);
                Recipe<?> decoded = Recipe.STREAM_CODEC.decode(buffer);
                helper.assertTrue(decoded.getSerializer() == holder.value().getSerializer(), "Recipe serializer changed: " + holder.id());

                // Native shaped recipes discard their source pattern after network decoding.
                buffer.clear();
                Recipe.STREAM_CODEC.encode(buffer, decoded);
                byte[] encodedAgain = new byte[buffer.readableBytes()];
                buffer.getBytes(buffer.readerIndex(), encodedAgain);
                helper.assertTrue(java.util.Arrays.equals(original, encodedAgain),
                        "Recipe fields changed during network encoding: " + holder.id());

                var ops = helper.getLevel().registryAccess().createSerializationContext(
                        com.mojang.serialization.JsonOps.INSTANCE);
                var json = Recipe.CODEC.encodeStart(ops, holder.value()).getOrThrow();
                var parsed = Recipe.CODEC.parse(ops, json).getOrThrow();
                helper.assertTrue(json.equals(Recipe.CODEC.encodeStart(ops, parsed).getOrThrow()),
                        "Recipe fields changed during JSON encoding: " + holder.id());
                helper.assertTrue(decoded.isSpecial() == holder.value().isSpecial(), "Recipe discovery changed: " + holder.id());
                checked++;
            } catch (RuntimeException failure) {
                throw new IllegalStateException("Recipe " + holder.id().identifier() + ": "
                        + failure.getMessage(), failure);
            } finally {
                buffer.release();
            }
        }
        helper.assertTrue(checked > 2000, "The native recipe round-trip did not cover the catalogue");
        helper.succeed();
    }

    @GameTest(structure = "basemetals:empty")
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
        zone.moddev.mc.basemetals.content.RuntimeTestData.load(projectile, entity);
        var item = projectile.getItem();
        helper.assertTrue(item.is(ModContent.item("adamantine_bolt").get()), "An old bolt changed its material");
        helper.assertTrue(item.getHoverName().getString().equals("An old bolt"), "An old bolt lost its name");
        helper.assertTrue(item.get(DataComponents.LORE).lines().getFirst().getString().equals("Saved before components"), "An old bolt lost its lore");
        helper.assertTrue(item.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("Proof", 0) == 147,
                "An old bolt lost custom data");
        CompoundTag saved = new CompoundTag();
        saved.merge(zone.moddev.mc.basemetals.content.RuntimeTestData.save(projectile));
        MaterialProjectile reloaded = new MaterialProjectile(ModEntities.CUSTOM_BOLT.get(), helper.getLevel());
        zone.moddev.mc.basemetals.content.RuntimeTestData.load(reloaded, saved);
        helper.assertTrue(net.minecraft.world.item.ItemStack.matches(item, reloaded.getItem()), "A converted bolt changed on reload");
        helper.succeed();
    }

    @GameTest(structure = "basemetals:empty")
    public static void projectilesKeepWeaponEnchantmentsAfterReload(GameTestHelper helper) {
        var level = helper.getLevel();
        var enchantments = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        var shooter = EntityTypes.ZOMBIE.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);
        var target = EntityTypes.ZOMBIE.create(level, net.minecraft.world.entity.EntitySpawnReason.COMMAND);

        for (boolean bolt : new boolean[] {false, true}) {
            ItemStack weapon = ModContent.item("steel_" + (bolt ? "crossbow" : "bow")).get().getDefaultInstance();
            weapon.enchant(enchantments.getOrThrow(Enchantments.POWER), 3);
            weapon.enchant(enchantments.getOrThrow(Enchantments.PUNCH), 2);
            weapon.enchant(enchantments.getOrThrow(Enchantments.FLAME), 1);

            ItemStack ammunition = ModContent.item("steel_" + (bolt ? "bolt" : "arrow")).get().getDefaultInstance();
            var type = bolt ? ModEntities.CUSTOM_BOLT.get() : ModEntities.CUSTOM_ARROW.get();
            MaterialProjectile projectile = new MaterialProjectile(type, level, shooter, ammunition, weapon);
            net.minecraft.world.entity.projectile.Projectile.spawnProjectile(projectile, level, ammunition);

            helper.assertTrue(projectile.isOnFire(), "Flame did not ignite the material projectile");

            CompoundTag saved = zone.moddev.mc.basemetals.content.RuntimeTestData.save(projectile);
            MaterialProjectile reloaded = new MaterialProjectile(type, level);
            zone.moddev.mc.basemetals.content.RuntimeTestData.load(reloaded, saved);

            helper.assertTrue(ItemStack.matches(weapon, reloaded.getWeaponItem()),
                    "The saved projectile lost its firing weapon or enchantments");
            helper.assertTrue(ItemStack.matches(ammunition, reloaded.getItem()),
                    "The saved projectile lost its ammunition");
            helper.assertTrue(reloaded.isOnFire(), "The saved projectile lost Flame");

            var source = level.damageSources().arrow(reloaded, shooter);
            float knockback = EnchantmentHelper.modifyKnockback(level, reloaded.getWeaponItem(), target, source, 0.0F);
            float damage = EnchantmentHelper.modifyDamage(level, reloaded.getWeaponItem(), target, source, 2.0F);

            helper.assertTrue(knockback > 0.0F, "Punch did not apply after reload");
            helper.assertTrue(damage > 2.0F, "Power did not apply after reload");
        }

        helper.succeed();
    }
}
