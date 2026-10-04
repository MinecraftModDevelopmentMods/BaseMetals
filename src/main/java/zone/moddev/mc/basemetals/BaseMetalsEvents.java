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

import net.minecraft.block.state.IBlockState;
import net.minecraft.advancements.Advancement;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTiered;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/** Handles crushing, shield upgrades and material equipment effects. */
public final class BaseMetalsEvents {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getPlayer() instanceof EntityPlayerMP)) return;
        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();

        // An advancement can be earned while its recipe is disabled. Restore the
        // unlock after a mode change without resetting the player's progress.
        for (IRecipe recipe : player.getServer().getRecipeManager().getRecipes()) {
            if (!BaseMetals.MOD_ID.equals(recipe.getId().getNamespace()) || recipe.isDynamic()
                    || player.getRecipeBook().isUnlocked(recipe)) continue;

            Advancement advancement = player.getServer().getAdvancementManager().getAdvancement(
                    new ResourceLocation(BaseMetals.MOD_ID, "recipes/" + recipe.getId().getPath()));
            if (advancement != null && player.getAdvancements().getProgress(advancement).isDone()) {
                player.unlockRecipes(Collections.singletonList(recipe));
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBreak(BlockEvent.BreakEvent event) {
        EntityPlayer player = event.getPlayer();
        World world = player.world;
        ItemStack held = player.getHeldItemMainhand();
        if (world.isRemote || player.isCreative() || !(held.getItem() instanceof CrackhammerItem)
                || !event.getState().canHarvestBlock(world, event.getPos(), player)) return;

        CrushingRecipe recipe = crushing(world, new ItemStack(event.getState().getBlock()));
        if (recipe == null) return;
        event.setCanceled(true);
        IBlockState state = event.getState();
        state.getBlock().onBlockHarvested(world, event.getPos(), state, player);
        if (!world.removeBlock(event.getPos())) return;
        state.getBlock().onPlayerDestroy(world, event.getPos(), state);
        ItemStack result = recipe.getRecipeOutput().copy();
        EntityItem drop = new EntityItem(world, event.getPos().getX() + 0.5D,
                event.getPos().getY() + 0.5D, event.getPos().getZ() + 0.5D, result);
        drop.setDefaultPickupDelay();
        world.spawnEntity(drop);
        player.addStat(net.minecraft.stats.StatList.BLOCK_MINED.get(state.getBlock()));
        player.addExhaustion(0.005F);
        held.damageItem(1, player);
    }

    private static CrushingRecipe crushing(World world, ItemStack input) {
        InventoryBasic inventory = new InventoryBasic(new TextComponentString("crushing"), 1);
        inventory.setInventorySlotContents(0, input);
        for (net.minecraft.item.crafting.IRecipe candidate : world.getRecipeManager().getRecipes()) {
            if (candidate instanceof CrushingRecipe && candidate.matches(inventory, world)) {
                return (CrushingRecipe) candidate;
            }
        }
        return null;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (event.phase != TickEvent.Phase.END || player.world.isRemote) return;
        if (BaseMetalsConfig.STARSTEEL_REGENERATION.get() && player.ticksExisted % 200 == 0) {
            repairStarsteel(player.getHeldItemMainhand());
            repairStarsteel(player.getHeldItemOffhand());
        }
        if (player.ticksExisted % 20 != 0) return;
        if (player instanceof EntityPlayerMP) BaseMetalsAdvancements.onEquipment((EntityPlayerMP) player);
        if (BaseMetalsConfig.SPECIAL_EFFECTS.get()) applyArmorEffects(player, armorMaterials(player));
    }

    private static void repairStarsteel(ItemStack stack) {
        if (stack.getItem() instanceof MaterialBacked && !(stack.getItem() instanceof ItemArmor)
                && "starsteel".equals(((MaterialBacked) stack.getItem()).baseMetalsMaterial().name())
                && stack.isDamaged()) stack.setDamage(Math.max(0, stack.getDamage() - 1));
    }

    private static Map<EntityEquipmentSlot, String> armorMaterials(EntityPlayer player) {
        Map<EntityEquipmentSlot, String> result = new EnumMap<EntityEquipmentSlot, String>(EntityEquipmentSlot.class);
        EntityEquipmentSlot[] slots = { EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
                EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET };
        for (EntityEquipmentSlot slot : slots) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (stack.getItem() instanceof ItemArmor && stack.getItem() instanceof MaterialBacked) {
                result.put(slot, ((MaterialBacked) stack.getItem()).baseMetalsMaterial().name());
            }
        }
        return result;
    }

    private static int count(Map<EntityEquipmentSlot, String> armor, String material) {
        int count = 0;
        for (String entry : armor.values()) if (material.equals(entry)) count++;
        return count;
    }

    private static void applyArmorEffects(EntityPlayer player, Map<EntityEquipmentSlot, String> armor) {
        int adamantine = count(armor, "adamantine");
        if (adamantine >= 2) add(player, MobEffects.RESISTANCE, adamantine == 4 ? 1 : 0);
        int lead = count(armor, "lead");
        if (lead >= 2) add(player, MobEffects.SLOWNESS, lead == 4 ? 1 : 0);
        if (count(armor, "coldiron") == 4) add(player, MobEffects.FIRE_RESISTANCE, 0);
        if (count(armor, "aquarium") == 4 && player.isInWater()) {
            add(player, MobEffects.WATER_BREATHING, 0);
            add(player, MobEffects.RESISTANCE, 0);
            player.removePotionEffect(MobEffects.MINING_FATIGUE);
        }
        if (count(armor, "mithril") == 4) {
            for (PotionEffect effect : new ArrayList<PotionEffect>(player.getActivePotionEffects())) {
                if (effect.getPotion().isBadEffect()) player.removePotionEffect(effect.getPotion());
            }
        }
        int starsteel = count(armor, "starsteel");
        if (starsteel > 0) add(player, MobEffects.JUMP_BOOST, starsteel - 1);
        if (starsteel >= 2) add(player, MobEffects.SPEED, Math.min(2, starsteel - 2));
    }

    private static void add(EntityPlayer player, Potion potion, int amplifier) {
        player.addPotionEffect(new PotionEffect(potion, 45, amplifier, false, false, true));
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        if (!BaseMetalsConfig.SPECIAL_EFFECTS.get() || event.getEntityLiving().world.isRemote) return;
        Entity source = event.getSource().getTrueSource();
        if (!(source instanceof EntityLivingBase)) return;
        EntityLivingBase attacker = (EntityLivingBase) source;
        ItemStack held = attacker.getHeldItemMainhand();
        if (!(held.getItem() instanceof MaterialBacked) || !(held.getItem() instanceof ItemTiered)) return;
        String material = ((MaterialBacked) held.getItem()).baseMetalsMaterial().name();
        EntityLivingBase target = event.getEntityLiving();
        if ("adamantine".equals(material) && target.getMaxHealth() > 20.0F) {
            event.setAmount(event.getAmount() + 4.0F);
        } else if ("aquarium".equals(material) && target.canBreatheUnderwater()) {
            event.setAmount(event.getAmount() + 4.0F);
        } else if ("coldiron".equals(material) && target.isImmuneToFire()) {
            event.setAmount(event.getAmount() + 3.0F);
        } else if ("mithril".equals(material) && target.getCreatureAttribute() == CreatureAttribute.UNDEAD) {
            target.addPotionEffect(new PotionEffect(MobEffects.WITHER, 60, 3));
            target.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 1));
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
            ItemTags.Wrapper plate = new ItemTags.Wrapper(
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
        if (event.getPlayer() instanceof EntityPlayerMP)
            BaseMetalsAdvancements.onCrafted((EntityPlayerMP) event.getPlayer(), event.getCrafting());
    }
    @SubscribeEvent public void onItemSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getPlayer() instanceof EntityPlayerMP)
            BaseMetalsAdvancements.onSmelted((EntityPlayerMP) event.getPlayer(), event.getSmelting());
    }
    @SubscribeEvent public void onBlockPlaced(BlockEvent.EntityPlaceEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP)
            BaseMetalsAdvancements.onPlaced((EntityPlayerMP) event.getEntity(), event.getPlacedBlock());
    }
}
