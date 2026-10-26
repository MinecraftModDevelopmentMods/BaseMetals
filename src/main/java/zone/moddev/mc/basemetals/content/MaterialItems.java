package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.BaseMetals;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.network.chat.Component;

import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorType;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public final class MaterialItems {
    private MaterialItems() {}

    public static final class Basic extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        private final int burnTime;
        public Basic(MaterialDefinition material, int burnTime, Properties properties) {
            super(properties);
            this.material = material;
            this.burnTime = burnTime;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public int getBurnTime(ItemStack stack,
                @Nullable net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
            return burnTime;
        }
    }

    public static final class Pickaxe extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        public Pickaxe(MaterialDefinition material, Properties properties) {
            super(properties.pickaxe(new MaterialTier(material).toolMaterial(), 1, -2.8F));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Axe extends AxeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Axe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material).toolMaterial(), 4.0F + material.baseAttackDamage(),
                    material.axeAttackSpeed(), properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Shovel extends ShovelItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shovel(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material).toolMaterial(), 1.5F, -3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Hoe extends HoeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Hoe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material).toolMaterial(), -material.baseAttackDamage(),
                    material.baseAttackDamage() - 3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }

        @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Sword extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        public Sword(MaterialDefinition material, Properties properties) {
            super(properties.sword(new MaterialTier(material).toolMaterial(), 3, -2.4F));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Armor extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        public Armor(MaterialDefinition material, EquipmentSlot slot, Properties properties) {
            super(properties.humanoidArmor(MaterialArmor.get(material), armorType(slot)));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addArmorTooltip(material, tooltip);
        }
    }

    public static final class HorseArmor extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        public HorseArmor(MaterialDefinition material, Properties properties) {
            super(properties.horseArmor(MaterialArmor.get(material)).stacksTo(1));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
    }

    private static ArmorType armorType(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> ArmorType.HELMET;
            case CHEST -> ArmorType.CHESTPLATE;
            case LEGS -> ArmorType.LEGGINGS;
            case FEET -> ArmorType.BOOTS;
            default -> throw new IllegalArgumentException("Not an armour slot: " + slot);
        };
    }

    public static final class BurnableBlock extends BlockItem {
        private final int burnTime;
        public BurnableBlock(net.minecraft.world.level.block.Block block, int burnTime, Properties properties) {
            super(block, properties);
            this.burnTime = burnTime;
        }
        @Override public int getBurnTime(ItemStack stack,
                @Nullable net.minecraft.world.item.crafting.RecipeType<?> recipeType) {
            return burnTime;
        }
    }

    public static final class Shears extends ShearsItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shears(MaterialDefinition material, Properties properties) { super(repairable(material, properties)); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static class Shield extends ShieldItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shield(MaterialDefinition material, Properties properties) { super(repairable(material, properties)); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static class Bow extends BowItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Bow(MaterialDefinition material, Properties properties) { super(repairable(material, properties)); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public Predicate<ItemStack> getAllSupportedProjectiles() {
            return stack -> stack.getItem() instanceof net.minecraft.world.item.ArrowItem
                    && (!(stack.getItem() instanceof BaseMetalAmmoItem ammo)
                            || ammo.kind() == BaseMetalAmmoItem.Kind.ARROW);
        }

        @Override public Predicate<ItemStack> getSupportedHeldProjectiles() {
            return getAllSupportedProjectiles();
        }

        @Override public AbstractArrow customArrow(AbstractArrow arrow) {
            applyLauncherDamage(arrow, material);
            return arrow;
        }
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static final class Crossbow extends BowItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Crossbow(MaterialDefinition material, Properties properties) { super(repairable(material, properties)); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public Predicate<ItemStack> getAllSupportedProjectiles() {
            return stack -> stack.getItem() instanceof BaseMetalAmmoItem ammo
                    && ammo.kind() == BaseMetalAmmoItem.Kind.BOLT;
        }
        @Override
        public boolean releaseUsing(ItemStack bow, Level level, LivingEntity user, int timeLeft) {
            if (!(user instanceof Player player)) return false;
            boolean hasInfiniteAmmo = player.getAbilities().instabuild
                    || EnchantmentHelper.getItemEnchantmentLevel(player.registryAccess()
                            .lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.INFINITY), bow) > 0;
            ItemStack ammunition = findBolt(player);
            int charge = getUseDuration(bow, user) - timeLeft;
            charge = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(
                    bow, level, player, charge, !ammunition.isEmpty() || hasInfiniteAmmo);
            if (charge < 0 || ammunition.isEmpty() && !hasInfiniteAmmo) return false;

            if (ammunition.isEmpty()) {
                ammunition = ModContent.item("iron_bolt").get().getDefaultInstance();
            }
            float power = getPowerForTime(charge);
            if (power < 0.1F) return false;

            BaseMetalAmmoItem boltItem = ammunition.getItem() instanceof BaseMetalAmmoItem ammo
                    && ammo.kind() == BaseMetalAmmoItem.Kind.BOLT
                    ? ammo
                    : (BaseMetalAmmoItem) ModContent.item("iron_bolt").get();
            boolean infiniteShot = player.getAbilities().instabuild
                    || boltItem.isInfinite(ammunition, bow, player);
            if (!level.isClientSide()) {
                AbstractArrow bolt = boltItem.createArrow(level, ammunition, player, bow);
                applyLauncherDamage(bolt, material);
                bolt.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F,
                        power * 3.0F, 1.0F);
                if (power == 1.0F) bolt.setCritArrow(true);
                // Keep the weapon for hit effects, and apply launch effects through vanilla's spawn path.
                bow.hurtAndBreak(1, player, zone.moddev.mc.basemetals.content.MaterialItems.handSlot(player.getUsedItemHand()));
                if (infiniteShot) bolt.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                net.minecraft.world.entity.projectile.Projectile.spawnProjectile(
                        bolt, (net.minecraft.server.level.ServerLevel) level, ammunition);
            }
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT,
                    SoundSource.PLAYERS, 1.0F,
                    1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + power * 0.5F);
            if (!infiniteShot && !player.getAbilities().instabuild) {
                ammunition.shrink(1);
                if (ammunition.isEmpty()) player.getInventory().removeItem(ammunition);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
            return true;
        }

        private ItemStack findBolt(Player player) {
            ItemStack offhand = player.getOffhandItem();
            if (getAllSupportedProjectiles().test(offhand)) return offhand;
            ItemStack mainhand = player.getMainHandItem();
            if (getAllSupportedProjectiles().test(mainhand)) return mainhand;
            for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
                ItemStack candidate = player.getInventory().getItem(slot);
                if (getAllSupportedProjectiles().test(candidate)) return candidate;
            }
            return ItemStack.EMPTY;
        }

        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static final class FishingRod extends FishingRodItem implements MaterialBacked {
        private final MaterialDefinition material;
        public FishingRod(MaterialDefinition material, Properties properties) { super(repairable(material, properties)); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override
        public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context,
                TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static EquipmentSlot handSlot(net.minecraft.world.InteractionHand hand) {
        return hand == net.minecraft.world.InteractionHand.MAIN_HAND
                ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }

    private static void applyLauncherDamage(AbstractArrow arrow, MaterialDefinition launcher) {
        double ammunitionDamage = arrow instanceof zone.moddev.mc.basemetals.entity.MaterialProjectile projectile
                && projectile.getItem().getItem() instanceof MaterialBacked backed
                ? backed.baseMetalsMaterial().baseAttackDamage() : 1.0D;
        arrow.setBaseDamage(launcher.baseAttackDamage() + ammunitionDamage);
    }

    private static Item.Properties repairable(MaterialDefinition material, Item.Properties properties) {
        return properties.repairable(net.minecraft.tags.ItemTags.create(
                Identifier.parse(material.repairIngredientTag())))
                .enchantable(material.enchantability());
    }

    static void addToolTooltip(MaterialDefinition material, Consumer<Component> tooltip) {
        Component component = switch (material.name()) {
            case "adamantine" -> net.minecraft.network.chat.Component.translatable("tooltip.adamantine.tool", 4);
            case "aquarium" -> net.minecraft.network.chat.Component.translatable("tooltip.aquarium.tool", 4);
            case "coldiron" -> net.minecraft.network.chat.Component.translatable("tooltip.coldiron.tool", 3);
            case "mithril" -> net.minecraft.network.chat.Component.translatable("tooltip.mithril.tool");
            case "starsteel" -> net.minecraft.network.chat.Component.translatable("tooltip.starsteel.tool", 10);
            default -> null;
        };
        if (component != null) tooltip.accept(component);
    }

    private static void addArmorTooltip(MaterialDefinition material, Consumer<Component> tooltip) {
        if (material.name().equals("adamantine") || material.name().equals("aquarium")
                || material.name().equals("coldiron") || material.name().equals("mithril")
                || material.name().equals("starsteel")) {
            tooltip.accept(net.minecraft.network.chat.Component.translatable("tooltip." + material.name() + ".armor"));
        }
    }
}
