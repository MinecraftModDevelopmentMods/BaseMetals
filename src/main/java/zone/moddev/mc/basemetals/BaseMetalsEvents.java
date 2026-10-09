package zone.moddev.mc.basemetals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import zone.moddev.mc.basemetals.config.MaterialForm;
import zone.moddev.mc.basemetals.content.CrackhammerItem;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.content.MaterialItems;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.CrushingRecipe;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.advancements.Advancement;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;

/** Handles crushing, shield upgrades and material equipment effects. */
public final class BaseMetalsEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer)) return;
        ServerPlayer player = (ServerPlayer) event.getPlayer();

        // An advancement can be earned while its recipe is disabled. Restore the
        // unlock after a mode change without resetting the player's progress.
        for (Recipe recipe : player.getServer().getRecipeManager().getRecipes()) {
            if (!BaseMetals.MOD_ID.equals(recipe.getId().getNamespace()) || recipe.isSpecial()
                    || player.getRecipeBook().contains(recipe)) continue;

            Advancement advancement = player.getServer().getAdvancements().getAdvancement(
                    new ResourceLocation(BaseMetals.MOD_ID, "recipes/" + recipe.getId().getPath()));
            if (advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone()) {
                player.awardRecipes(Collections.singletonList(recipe));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level world = player.level;
        ItemStack held = player.getMainHandItem();
        if (world.isClientSide || player.isCreative() || !(held.getItem() instanceof CrackhammerItem)
                || !event.getState().canHarvestBlock(world, event.getPos(), player)) return;

        CrushingRecipe recipe = crushing(world, new ItemStack(event.getState().getBlock()));
        if (recipe == null) return;
        event.setCanceled(true);
        BlockState state = event.getState();
        state.getBlock().playerWillDestroy(world, event.getPos(), state, player);
        if (!world.removeBlock(event.getPos(), false)) return;
        state.getBlock().destroy(world, event.getPos(), state);
        ItemStack result = recipe.getResultItem().copy();
        ItemEntity drop = new ItemEntity(world, event.getPos().getX() + 0.5D,
                event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, result);
        drop.setDefaultPickUpDelay();
        world.addFreshEntity(drop);
        player.awardStat(net.minecraft.stats.Stats.BLOCK_MINED.get(state.getBlock()));
        player.causeFoodExhaustion(0.005F);
        held.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(net.minecraft.world.InteractionHand.MAIN_HAND));
    }

    private static CrushingRecipe crushing(Level world, ItemStack input) {
        SimpleContainer inventory = new SimpleContainer(1);
        inventory.setItem(0, input);
        for (net.minecraft.world.item.crafting.Recipe candidate : world.getRecipeManager().getRecipes()) {
            if (candidate instanceof CrushingRecipe && candidate.matches(inventory, world)) {
                return (CrushingRecipe) candidate;
            }
        }
        return null;
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
        if (player instanceof ServerPlayer) BaseMetalsAdvancements.onEquipment((ServerPlayer) player);
        if (BaseMetalsConfig.SPECIAL_EFFECTS.get()) applyArmorEffects(player, armorMaterials(player));
    }

    private static void repairStarsteel(ItemStack stack) {
        if (stack.getItem() instanceof MaterialBacked && !(stack.getItem() instanceof ArmorItem)
                && "starsteel".equals(((MaterialBacked) stack.getItem()).baseMetalsMaterial().name())
                && stack.isDamaged()) stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
    }

    private static Map<EquipmentSlot, String> armorMaterials(Player player) {
        Map<EquipmentSlot, String> result = new EnumMap<EquipmentSlot, String>(EquipmentSlot.class);
        EquipmentSlot[] slots = { EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET };
        for (EquipmentSlot slot : slots) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem && stack.getItem() instanceof MaterialBacked) {
                result.put(slot, ((MaterialBacked) stack.getItem()).baseMetalsMaterial().name());
            }
        }
        return result;
    }

    private static int count(Map<EquipmentSlot, String> armor, String material) {
        int count = 0;
        for (String entry : armor.values()) if (material.equals(entry)) count++;
        return count;
    }

    private static void applyArmorEffects(Player player, Map<EquipmentSlot, String> armor) {
        int adamantine = count(armor, "adamantine");
        if (adamantine >= 2) add(player, MobEffects.DAMAGE_RESISTANCE, adamantine == 4 ? 1 : 0);
        int lead = count(armor, "lead");
        if (lead >= 2) add(player, MobEffects.MOVEMENT_SLOWDOWN, lead == 4 ? 1 : 0);
        if (count(armor, "coldiron") == 4) add(player, MobEffects.FIRE_RESISTANCE, 0);
        if (count(armor, "aquarium") == 4 && player.isInWater()) {
            add(player, MobEffects.WATER_BREATHING, 0);
            add(player, MobEffects.DAMAGE_RESISTANCE, 0);
            player.removeEffect(MobEffects.DIG_SLOWDOWN);
        }
        if (count(armor, "mithril") == 4) {
            for (MobEffectInstance effect : new ArrayList<MobEffectInstance>(player.getActiveEffects())) {
                if (effect.getEffect().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) player.removeEffect(effect.getEffect());
            }
        }
        int starsteel = count(armor, "starsteel");
        if (starsteel > 0) add(player, MobEffects.JUMP, starsteel - 1);
        if (starsteel >= 2) add(player, MobEffects.MOVEMENT_SPEED, Math.min(2, starsteel - 2));
    }

    private static void add(Player player, MobEffect potion, int amplifier) {
        player.addEffect(new MobEffectInstance(potion, 45, amplifier, false, false, true));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!BaseMetalsConfig.SPECIAL_EFFECTS.get() || event.getEntityLiving().level.isClientSide) return;
        Entity source = event.getSource().getEntity();
        if (!(source instanceof LivingEntity)) return;
        LivingEntity attacker = (LivingEntity) source;
        ItemStack held = attacker.getMainHandItem();
        if (!(held.getItem() instanceof MaterialBacked) || !(held.getItem() instanceof TieredItem)) return;
        String material = ((MaterialBacked) held.getItem()).baseMetalsMaterial().name();
        LivingEntity target = event.getEntityLiving();
        if ("adamantine".equals(material) && target.getMaxHealth() > 20.0F) {
            event.setAmount(event.getAmount() + 4.0F);
        } else if ("aquarium".equals(material) && target.canBreatheUnderwater()) {
            event.setAmount(event.getAmount() + 4.0F);
        } else if ("coldiron".equals(material) && target.fireImmune()) {
            event.setAmount(event.getAmount() + 3.0F);
        } else if ("mithril".equals(material) && target.getMobType() == MobType.UNDEAD) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 3));
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
        }
    }

    @SubscribeEvent
    public void onShieldUpgrade(AnvilUpdateEvent event) {
        if (!(event.getLeft().getItem() instanceof MaterialItems.Shield)
                || event.getLeft().getCount() != 1 || event.getRight().getCount() != 1) return;
        MaterialDefinition current = ((MaterialItems.Shield) event.getLeft().getItem()).baseMetalsMaterial();
        MaterialDefinition upgrade = null;
        for (MaterialDefinition candidate : shieldMaterials().values()) {
            if (!ContentPolicy.active().allows(candidate.name(), MaterialForm.SHIELD)) continue;
            if (candidate.hardness() <= current.hardness()) continue;
            net.minecraft.tags.Tag<net.minecraft.world.item.Item> plate = ItemTags.getAllTags().getTagOrEmpty(
                    new ResourceLocation("forge", "plates/" + candidate.name()));
            if (!plate.contains(event.getRight().getItem())) continue;
            if (upgrade == null || candidate.hardness() < upgrade.hardness()
                    || candidate.hardness() == upgrade.hardness()
                    && candidate.name().compareTo(upgrade.name()) < 0) upgrade = candidate;
        }
        if (upgrade == null) return;
        ItemStack output = new ItemStack(ModContent.item(upgrade.name() + "_shield").get());
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(event.getLeft()), output);
        event.setOutput(output);
        event.setCost(shieldUpgradeCost(current, upgrade,
                EnchantmentHelper.getEnchantments(event.getLeft()).size()));
        event.setMaterialCost(1);
    }

    private static Map<String, MaterialDefinition> shieldMaterials() {
        Map<String, MaterialDefinition> result = new LinkedHashMap<String, MaterialDefinition>();
        for (zone.moddev.mc.basemetals.content.RegistryHandle<net.minecraft.world.item.Item> reference
                : ModContent.itemsById().values()) {
            if (reference.get() instanceof MaterialItems.Shield) {
                MaterialDefinition material = ((MaterialItems.Shield) reference.get()).baseMetalsMaterial();
                if (!result.containsKey(material.name())) result.put(material.name(), material);
            }
        }
        return result;
    }

    static int shieldUpgradeCost(MaterialDefinition current, MaterialDefinition upgrade, int enchantments) {
        return Math.max(5, (int) (5.0D * (upgrade.hardness() - current.hardness())
                + upgrade.magic() * enchantments));
    }

    @SubscribeEvent public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer)
            BaseMetalsAdvancements.onCrafted((ServerPlayer) event.getPlayer(), event.getCrafting());
    }
    @SubscribeEvent public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getPlayer() instanceof ServerPlayer)
            BaseMetalsAdvancements.onSmelted((ServerPlayer) event.getPlayer(), event.getSmelting());
    }
    @SubscribeEvent public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayer)
            BaseMetalsAdvancements.onPlaced((ServerPlayer) event.getEntity(), event.getPlacedBlock());
    }
}
