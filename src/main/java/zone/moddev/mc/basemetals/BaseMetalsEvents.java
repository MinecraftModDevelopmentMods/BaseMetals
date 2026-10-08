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

import net.minecraft.block.BlockState;
import net.minecraft.advancements.Advancement;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.TieredItem;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
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
        if (!(event.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();

        // An advancement can be earned while its recipe is disabled. Restore the
        // unlock after a mode change without resetting the player's progress.
        for (IRecipe recipe : player.getServer().getRecipeManager().getRecipes()) {
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
        PlayerEntity player = event.getPlayer();
        World world = player.level;
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
        held.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(net.minecraft.util.Hand.MAIN_HAND));
    }

    private static CrushingRecipe crushing(World world, ItemStack input) {
        Inventory inventory = new Inventory(1);
        inventory.setItem(0, input);
        for (net.minecraft.item.crafting.IRecipe candidate : world.getRecipeManager().getRecipes()) {
            if (candidate instanceof CrushingRecipe && candidate.matches(inventory, world)) {
                return (CrushingRecipe) candidate;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if (event.phase != TickEvent.Phase.END || player.level.isClientSide) return;
        if (BaseMetalsConfig.STARSTEEL_REGENERATION.get() && player.tickCount % 200 == 0) {
            repairStarsteel(player.getMainHandItem());
            repairStarsteel(player.getOffhandItem());
        }
        if (player.tickCount % 20 != 0) return;
        if (player instanceof ServerPlayerEntity) BaseMetalsAdvancements.onEquipment((ServerPlayerEntity) player);
        if (BaseMetalsConfig.SPECIAL_EFFECTS.get()) applyArmorEffects(player, armorMaterials(player));
    }

    private static void repairStarsteel(ItemStack stack) {
        if (stack.getItem() instanceof MaterialBacked && !(stack.getItem() instanceof ArmorItem)
                && "starsteel".equals(((MaterialBacked) stack.getItem()).baseMetalsMaterial().name())
                && stack.isDamaged()) stack.setDamageValue(Math.max(0, stack.getDamageValue() - 1));
    }

    private static Map<EquipmentSlotType, String> armorMaterials(PlayerEntity player) {
        Map<EquipmentSlotType, String> result = new EnumMap<EquipmentSlotType, String>(EquipmentSlotType.class);
        EquipmentSlotType[] slots = { EquipmentSlotType.HEAD, EquipmentSlotType.CHEST,
                EquipmentSlotType.LEGS, EquipmentSlotType.FEET };
        for (EquipmentSlotType slot : slots) {
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.getItem() instanceof ArmorItem && stack.getItem() instanceof MaterialBacked) {
                result.put(slot, ((MaterialBacked) stack.getItem()).baseMetalsMaterial().name());
            }
        }
        return result;
    }

    private static int count(Map<EquipmentSlotType, String> armor, String material) {
        int count = 0;
        for (String entry : armor.values()) if (material.equals(entry)) count++;
        return count;
    }

    private static void applyArmorEffects(PlayerEntity player, Map<EquipmentSlotType, String> armor) {
        int adamantine = count(armor, "adamantine");
        if (adamantine >= 2) add(player, Effects.DAMAGE_RESISTANCE, adamantine == 4 ? 1 : 0);
        int lead = count(armor, "lead");
        if (lead >= 2) add(player, Effects.MOVEMENT_SLOWDOWN, lead == 4 ? 1 : 0);
        if (count(armor, "coldiron") == 4) add(player, Effects.FIRE_RESISTANCE, 0);
        if (count(armor, "aquarium") == 4 && player.isInWater()) {
            add(player, Effects.WATER_BREATHING, 0);
            add(player, Effects.DAMAGE_RESISTANCE, 0);
            player.removeEffect(Effects.DIG_SLOWDOWN);
        }
        if (count(armor, "mithril") == 4) {
            for (EffectInstance effect : new ArrayList<EffectInstance>(player.getActiveEffects())) {
                if (effect.getEffect().getCategory() == net.minecraft.potion.EffectType.HARMFUL) player.removeEffect(effect.getEffect());
            }
        }
        int starsteel = count(armor, "starsteel");
        if (starsteel > 0) add(player, Effects.JUMP, starsteel - 1);
        if (starsteel >= 2) add(player, Effects.MOVEMENT_SPEED, Math.min(2, starsteel - 2));
    }

    private static void add(PlayerEntity player, Effect potion, int amplifier) {
        player.addEffect(new EffectInstance(potion, 45, amplifier, false, false, true));
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
        } else if ("mithril".equals(material) && target.getMobType() == CreatureAttribute.UNDEAD) {
            target.addEffect(new EffectInstance(Effects.WITHER, 60, 3));
            target.addEffect(new EffectInstance(Effects.BLINDNESS, 60, 1));
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
            net.minecraft.tags.ITag<net.minecraft.item.Item> plate = ItemTags.getAllTags().getTagOrEmpty(
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
        for (zone.moddev.mc.basemetals.content.RegistryHandle<net.minecraft.item.Item> reference
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
        if (event.getPlayer() instanceof ServerPlayerEntity)
            BaseMetalsAdvancements.onCrafted((ServerPlayerEntity) event.getPlayer(), event.getCrafting());
    }
    @SubscribeEvent public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getPlayer() instanceof ServerPlayerEntity)
            BaseMetalsAdvancements.onSmelted((ServerPlayerEntity) event.getPlayer(), event.getSmelting());
    }
    @SubscribeEvent public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof ServerPlayerEntity)
            BaseMetalsAdvancements.onPlaced((ServerPlayerEntity) event.getEntity(), event.getPlacedBlock());
    }
}
