package zone.moddev.mc.basemetals.effects;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.lib.api.MaterialEvent;
import zone.moddev.mc.lib.api.MaterialExtensions;
import zone.moddev.mc.lib.common.runtime.MaterialBacked;
import zone.moddev.mc.lib.common.types.MMDMaterial;

import net.minecraft.core.Holder;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

/**
 * Material effects, declared as MMDLib handlers in {@code mmdlib/extension/effects.json}
 * and bound by each material's {@code event} map. MMDLib's event bridge runs the
 * {@code player-tick} handlers for a player wearing or holding the material.
 */
public final class MaterialEffects {
    private static final EquipmentSlot[] ARMOR = {
            EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    /**
     * Melee hits that can change the damage dealt. The built-in {@code weapon-hit} contract
     * runs before damage is known, so Base Metals bridges its own contract from
     * {@link LivingIncomingDamageEvent}, keyed by the attacker's held weapon.
     */
    public static MaterialEvent<LivingIncomingDamageEvent> MELEE_DAMAGE;

    private MaterialEffects() {}

    /** Called from the mod constructor, before MMDLib resolves {@code effects.json}. */
    public static void register() {
        var events = MaterialExtensions.GLOBAL.events();
        MELEE_DAMAGE = events.registerEvent(BaseMetals.MOD_ID + ":melee-damage", LivingIncomingDamageEvent.class,
                MethodType.methodType(void.class, LivingEntity.class, LivingEntity.class, LivingIncomingDamageEvent.class),
                (method, event) -> {
                    method.invokeExact((LivingEntity) event.getSource().getEntity(),
                            (LivingEntity) event.getEntity(), (LivingIncomingDamageEvent) event);
                });
        events.listen(NeoForge.EVENT_BUS, MELEE_DAMAGE, MaterialEffects::weaponMaterial);
    }

    private static List<MMDMaterial> weaponMaterial(LivingIncomingDamageEvent event) {
        if (!BaseMetalsConfig.SPECIAL_EFFECTS.get() || event.getEntity().level().isClientSide
                || !(event.getSource().getEntity() instanceof LivingEntity attacker)) return List.of();
        ItemStack held = attacker.getMainHandItem();
        return held.getItem() instanceof TieredItem && held.getItem() instanceof MaterialBacked backed
                ? List.of(backed.material()) : List.of();
    }

    public static void adamantine(Player player) {
        if (!armorTick(player)) return;
        int pieces = pieces(player, "adamantine");
        if (pieces >= 2) add(player, MobEffects.DAMAGE_RESISTANCE, pieces == 4 ? 1 : 0);
    }

    public static void aquarium(Player player) {
        if (!armorTick(player) || pieces(player, "aquarium") != 4 || !player.isUnderWater()) return;
        add(player, MobEffects.WATER_BREATHING, 0);
        add(player, MobEffects.DAMAGE_RESISTANCE, 0);
        player.removeEffect(MobEffects.DIG_SLOWDOWN);
    }

    public static void coldiron(Player player) {
        if (armorTick(player) && pieces(player, "coldiron") == 4) add(player, MobEffects.FIRE_RESISTANCE, 0);
    }

    public static void lead(Player player) {
        if (!armorTick(player)) return;
        int pieces = pieces(player, "lead");
        if (pieces >= 2) add(player, MobEffects.MOVEMENT_SLOWDOWN, pieces == 4 ? 1 : 0);
    }

    public static void mithril(Player player) {
        if (!armorTick(player) || pieces(player, "mithril") != 4) return;
        for (MobEffectInstance effect : new ArrayList<>(player.getActiveEffects())) {
            if (effect.getEffect().value().getCategory() == MobEffectCategory.HARMFUL) {
                player.removeEffect(effect.getEffect());
            }
        }
    }

    public static void starsteel(Player player) {
        if (player.level().isClientSide) return;
        if (BaseMetalsConfig.STARSTEEL_REGENERATION.get() && player.tickCount % 200 == 0) {
            regenerate(player.getMainHandItem());
            regenerate(player.getOffhandItem());
        }
        if (!armorTick(player)) return;
        int pieces = pieces(player, "starsteel");
        if (pieces > 0) add(player, MobEffects.JUMP, pieces - 1);
        if (pieces >= 2) add(player, MobEffects.MOVEMENT_SPEED, Math.min(2, pieces - 2));
    }

    public static void adamantineMelee(LivingEntity attacker, LivingEntity target, LivingIncomingDamageEvent event) {
        if (target.getMaxHealth() > 20.0F) event.setAmount(event.getAmount() + 4.0F);
    }

    public static void aquariumMelee(LivingEntity attacker, LivingEntity target, LivingIncomingDamageEvent event) {
        if (!target.canDrownInFluidType(NeoForgeMod.WATER_TYPE.value())) event.setAmount(event.getAmount() + 4.0F);
    }

    public static void coldironMelee(LivingEntity attacker, LivingEntity target, LivingIncomingDamageEvent event) {
        if (target.fireImmune()) event.setAmount(event.getAmount() + 3.0F);
    }

    public static void mithrilMelee(LivingEntity attacker, LivingEntity target, LivingIncomingDamageEvent event) {
        if (target.getType().is(EntityTypeTags.UNDEAD)) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 60, 3));
            target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 1));
        }
    }

    /** Starsteel arrows and bolts that deal damage lift their target. */
    public static void starsteelShot(Projectile projectile, LivingEntity target, ItemStack ammo) {
        if (BaseMetalsConfig.SPECIAL_EFFECTS.get()) {
            target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 100));
        }
    }

    private static boolean armorTick(Player player) {
        return !player.level().isClientSide && player.tickCount % 20 == 0 && BaseMetalsConfig.SPECIAL_EFFECTS.get();
    }

    private static void regenerate(ItemStack stack) {
        if (stack.isDamaged() && !(stack.getItem() instanceof ArmorItem) && is(stack, "starsteel")) {
            stack.setDamageValue(stack.getDamageValue() - 1);
        }
    }

    private static int pieces(Player player, String material) {
        int pieces = 0;
        for (EquipmentSlot slot : ARMOR) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem && is(stack, material)) pieces++;
        }
        return pieces;
    }

    /** True for Base Metals items of the material, whatever their form. */
    public static boolean is(ItemStack stack, String material) {
        return stack.getItem() instanceof MaterialBacked backed && backed.material().name()
                .filter(id -> id.getNamespace().equals(BaseMetals.MOD_ID) && id.getPath().equals(material))
                .isPresent();
    }

    private static void add(Player player, Holder<MobEffect> effect, int amplifier) {
        player.addEffect(new MobEffectInstance(effect, 45, amplifier, false, false, true));
    }
}
