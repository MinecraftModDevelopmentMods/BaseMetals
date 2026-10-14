package zone.moddev.mc.basemetals;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import zone.moddev.mc.basemetals.trade.BaseMetalsTrades;
import zone.moddev.mc.basemetals.migration.Legacy112WorldMigrator;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraftforge.event.server.ServerAboutToStartEvent;

/** Forge event handlers for Base Metals. */
public final class BaseMetalsEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        var recipes = new ArrayList<net.minecraft.world.item.crafting.Recipe<?>>();

        for (var recipe : player.server.getRecipeManager().getRecipes()) {
            if (!recipe.getId().getNamespace().equals(BaseMetals.MOD_ID) || recipe.isSpecial()) continue;

            var unlock = player.server.getAdvancements().getAdvancement(new ResourceLocation(
                    BaseMetals.MOD_ID, "recipes/" + recipe.getId().getPath()));
            if (unlock != null && player.getAdvancements().getOrStartProgress(unlock).isDone()) recipes.add(recipe);
        }

        player.awardRecipes(recipes);
    }

    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        try {
            java.nio.file.Path worldRoot = event.getServer().getWorldPath(LevelResource.ROOT);
            Legacy112WorldMigrator.migrateIfNeeded(worldRoot);
            Legacy112WorldMigrator.refreshLoadedSingleplayerPlayer(event.getServer(), worldRoot);
        } catch (java.io.IOException exception) {
            throw new IllegalStateException(
                    "Base Metals could not safely pre-flatten the legacy Forge 1.10/1.12 world", exception);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(BlockEvent.BreakEvent event) {
        if (!(event.getWorld() instanceof ServerLevel level) || !(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        ItemStack held = player.getMainHandItem();
        if (held.getItem() instanceof CrackhammerItem && !player.isCreative()
                && event.getState().canHarvestBlock(level, event.getPos(), player)) {
            Optional<CrushingRecipe> recipe = level.getRecipeManager().getRecipeFor(CrushingRecipe.TYPE.get(),
                    new SimpleContainer(new ItemStack(event.getState().getBlock())), level);
            if (recipe.isPresent()) {
                event.setCanceled(true);
                event.getState().getBlock().playerWillDestroy(level, event.getPos(), event.getState(), player);
                boolean removed = event.getState().onDestroyedByPlayer(level, event.getPos(), player,
                        true, level.getFluidState(event.getPos()));
                if (!removed) return;
                event.getState().getBlock().destroy(level, event.getPos(), event.getState());
                ItemStack result = recipe.get().getResultItem().copy();
                ItemEntity entity = new ItemEntity(level, event.getPos().getX() + 0.5D,
                        event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, result);
                entity.setDefaultPickUpDelay();
                level.addFreshEntity(entity);
                player.awardStat(net.minecraft.stats.Stats.BLOCK_MINED.get(event.getState().getBlock()));
                player.causeFoodExhaustion(0.005F);
                held.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                return;
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase != TickEvent.Phase.END || player.level.isClientSide) return;
        if (BaseMetalsConfig.STARSTEEL_REGENERATION.get() && player.tickCount % 200 == 0) {
            repairStarsteel(player.getMainHandItem());
            repairStarsteel(player.getOffhandItem());
        }
        if (player.tickCount % 20 != 0) return;
        if (player instanceof ServerPlayer serverPlayer) {
            BaseMetalsAdvancements.onEquipment(serverPlayer);
        }
        if (!BaseMetalsConfig.SPECIAL_EFFECTS.get()) return;
        Map<EquipmentSlot, String> armor = armorMaterials(player);
        applyArmorEffects(player, armor);
    }

    private static void repairStarsteel(ItemStack stack) {
        if (stack.getItem() instanceof MaterialBacked backed
                && !(stack.getItem() instanceof ArmorItem)
                && backed.baseMetalsMaterial().name().equals("starsteel") && stack.isDamaged()) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    private static Map<EquipmentSlot, String> armorMaterials(Player player) {
        Map<EquipmentSlot, String> result = new EnumMap<>(EquipmentSlot.class);
        for (EquipmentSlot slot : new EquipmentSlot[] {
                EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem && stack.getItem() instanceof MaterialBacked backed) {
                result.put(slot, backed.baseMetalsMaterial().name());
            }
        }
        return result;
    }

    private static int count(Map<EquipmentSlot, String> armor, String material) {
        return (int) armor.values().stream().filter(material::equals).count();
    }

    private static void applyArmorEffects(Player player, Map<EquipmentSlot, String> armor) {
        int adamantine = count(armor, "adamantine");
        if (adamantine >= 2) add(player, MobEffects.DAMAGE_RESISTANCE, adamantine == 4 ? 1 : 0);
        int lead = count(armor, "lead");
        if (lead >= 2) add(player, MobEffects.MOVEMENT_SLOWDOWN, lead == 4 ? 1 : 0);
        if (count(armor, "coldiron") == 4) add(player, MobEffects.FIRE_RESISTANCE, 0);
        if (count(armor, "aquarium") == 4 && player.isUnderWater()) {
            add(player, MobEffects.WATER_BREATHING, 0);
            add(player, MobEffects.DAMAGE_RESISTANCE, 0);
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
        if (count(armor, "mithril") == 4) {
            for (MobEffectInstance effect : new ArrayList<>(player.getActiveEffects())) {
                if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                    player.removeEffect(effect.getEffect());
                }
            }
        }
        int starsteel = count(armor, "starsteel");
        if (starsteel > 0) add(player, MobEffects.JUMP, starsteel - 1);
        if (starsteel >= 2) add(player, MobEffects.MOVEMENT_SPEED, Math.min(2, starsteel - 2));
    }

    private static void add(Player player, net.minecraft.world.effect.MobEffect effect, int amplifier) {
        player.addEffect(new MobEffectInstance(effect, 45, amplifier, false, false, true));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!BaseMetalsConfig.SPECIAL_EFFECTS.get() || event.getEntityLiving().level.isClientSide
                || !(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof MaterialBacked backed) || !(held.getItem() instanceof TieredItem)) return;
        LivingEntity target = event.getEntityLiving();
        switch (backed.baseMetalsMaterial().name()) {
            case "adamantine" -> {
                if (target.getMaxHealth() > 20.0F) event.setAmount(event.getAmount() + 4.0F);
            }
            case "aquarium" -> {
                if (target.canBreatheUnderwater()) event.setAmount(event.getAmount() + 4.0F);
            }
            case "coldiron" -> {
                if (target.fireImmune()) event.setAmount(event.getAmount() + 3.0F);
            }
            case "mithril" -> {
                if (target.getMobType() == MobType.UNDEAD) {
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 3));
                    target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
                }
            }
            default -> { }
        }
    }

    @SubscribeEvent
    public void onShieldUpgrade(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof zone.moddev.mc.basemetals.content.MaterialItems.Shield shield)
                || event.getLeft().getCount() != 1 || event.getRight().getCount() != 1) return;
        MaterialDefinition current = shield.baseMetalsMaterial();
        MaterialDefinition upgrade = shieldMaterials().values().stream()
                .filter(candidate -> candidate.hardness() > current.hardness())
                .filter(candidate -> event.getRight().is(ItemTags.create(
                        new ResourceLocation("forge", "plates/" + candidate.name()))))
                .sorted(Comparator.comparingDouble(MaterialDefinition::hardness)
                        .thenComparing(MaterialDefinition::name))
                .findFirst().orElse(null);
        if (upgrade == null) return;
        if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows("basemetals:" + upgrade.name() + "_shield")) return;
        ItemStack output = ModContent.item(upgrade.name() + "_shield").get().getDefaultInstance();
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(event.getLeft()), output);
        int enchantments = EnchantmentHelper.getEnchantments(event.getLeft()).size();
        int cost = shieldUpgradeCost(current, upgrade, enchantments);
        event.setOutput(output);
        event.setCost(cost);
        event.setMaterialCost(1);
    }

    private static Map<String, MaterialDefinition> shieldMaterials() {
        Map<String, MaterialDefinition> materials = new LinkedHashMap<>();
        ModContent.itemsById().values().forEach(reference -> {
            if (reference.get() instanceof MaterialItems.Shield shield) {
                materials.putIfAbsent(shield.baseMetalsMaterial().name(), shield.baseMetalsMaterial());
            }
        });
        return materials;
    }

    static int shieldUpgradeCost(MaterialDefinition current, MaterialDefinition upgrade, int enchantments) {
        return Math.max(5, (int) (5.0D * (upgrade.hardness() - current.hardness())
                + upgrade.magic() * enchantments));
    }

    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onCrafted(player, event.getCrafting());
        }
    }

    @SubscribeEvent
    public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onSmelted(player, event.getSmelting());
        }
    }

    @SubscribeEvent
    public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BaseMetalsAdvancements.onPlaced(player, event.getPlacedBlock());
        }
    }

    @SubscribeEvent
    public void onVillagerTrades(VillagerTradesEvent event) {
        BaseMetalsTrades.add(event);
    }
}
