package zone.moddev.mc.basemetals.content;

import java.util.List;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.item.HorseArmorItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.SoundEvents;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

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
        @Override public int getBurnTime(ItemStack stack) { return burnTime; }
    }

    public static final class Pickaxe extends PickaxeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Pickaxe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 1, -2.8F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Axe extends AxeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Axe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 4.0F + material.baseAttackDamage(),
                    material.axeAttackSpeed(), properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Shovel extends ShovelItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shovel(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 1.5F, -3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Hoe extends HoeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Hoe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), -(int) material.baseAttackDamage(),
                    material.baseAttackDamage() - 3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public float getAttackDamage() { return 0.0F; }
        @Override public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlotType slot) {
            if (slot != EquipmentSlotType.MAINHAND) return super.getDefaultAttributeModifiers(slot);

            // Hoes had no attack-damage bonus before 1.16, even for hard materials.
            return ImmutableMultimap.of(
                    Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
                            "Weapon modifier", 0.0D,
                            AttributeModifier.Operation.ADDITION),
                    Attributes.ATTACK_SPEED,
                    new AttributeModifier(BASE_ATTACK_SPEED_UUID,
                            "Weapon modifier", material.baseAttackDamage() - 3.0F,
                            AttributeModifier.Operation.ADDITION));
        }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Sword extends SwordItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Sword(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 3, -2.4F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Armor extends ArmorItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Armor(MaterialDefinition material, EquipmentSlotType slot, Properties properties) {
            super(new MaterialArmor(material), slot, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addArmorTooltip(material, tooltip); }
    }

    public static final class HorseArmor extends HorseArmorItem implements MaterialBacked {
        private final MaterialDefinition material;
        public HorseArmor(MaterialDefinition material, Properties properties) {
            super(material.horseArmorProtection(), new ResourceLocation(BaseMetals.MOD_ID,
                    "textures/entity/horse/armor/horse_armor_" + material.name() + ".png"),
                    properties.stacksTo(1));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
    }

    public static final class BurnableBlock extends BlockItem {
        private final int burnTime;
        public BurnableBlock(net.minecraft.block.Block block, int burnTime, Properties properties) {
            super(block, properties);
            this.burnTime = burnTime;
        }
        @Override public int getBurnTime(ItemStack stack) { return burnTime; }
    }

    public static final class Shears extends ShearsItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shears(MaterialDefinition material, Properties properties) {
            super(properties.defaultDurability(material.toolDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Shield extends ShieldItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shield(MaterialDefinition material, Properties properties) {
            super(properties.defaultDurability(material.shieldDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Bow extends BowItem implements MaterialBacked {
        private final MaterialDefinition material;

        public Bow(MaterialDefinition material, Properties properties) {
            super(properties.defaultDurability(material.toolDurability()));
            this.material = material;
        }

        @Override public MaterialDefinition baseMetalsMaterial() { return material; }

        @Override
        public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
            // Forge calls this before applying Power, so enchantment bonuses stay unchanged.
            arrow.setBaseDamage(arrow.getBaseDamage() + material.baseAttackDamage() - 1.0D);

            return arrow;
        }

        @Override public java.util.function.Predicate<ItemStack> getSupportedHeldProjectiles() { return this::isArrow; }
        @Override public java.util.function.Predicate<ItemStack> getAllSupportedProjectiles() { return this::isArrow; }

        protected boolean isArrow(ItemStack stack) {
            if (stack.getItem() instanceof BaseMetalAmmoItem) {
                return ((BaseMetalAmmoItem) stack.getItem()).kind() == BaseMetalAmmoItem.Kind.ARROW;
            }
            return stack.getItem() instanceof ArrowItem;
        }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Crossbow extends Bow {
        public Crossbow(MaterialDefinition material, Properties properties) {
            super(material, properties);
        }

        @Override protected boolean isArrow(ItemStack stack) {
            return stack.getItem() instanceof BaseMetalAmmoItem
                    && ((BaseMetalAmmoItem) stack.getItem()).kind() == BaseMetalAmmoItem.Kind.BOLT;
        }
        @Override
        public void releaseUsing(ItemStack bow, World world, LivingEntity user, int timeLeft) {
            if (!(user instanceof PlayerEntity)) return;
            PlayerEntity player = (PlayerEntity) user;
            boolean hasInfiniteAmmo = player.abilities.instabuild
                    || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
            ItemStack ammunition = player.getProjectile(bow);

            // Creative's vanilla arrow fallback is not a bolt. Use our fallback below instead.
            if (!isArrow(ammunition)) ammunition = ItemStack.EMPTY;

            int charge = getUseDuration(bow) - timeLeft;
            charge = ForgeEventFactory.onArrowLoose(bow, world, player, charge,
                    !ammunition.isEmpty() || hasInfiniteAmmo);
            if (charge < 0 || ammunition.isEmpty() && !hasInfiniteAmmo) return;

            if (ammunition.isEmpty()) ammunition = new ItemStack(ModContent.item("iron_bolt").get());
            float power = getPowerForTime(charge);
            if (power < 0.1F) return;

            BaseMetalAmmoItem bolt = (BaseMetalAmmoItem) ammunition.getItem();
            boolean infiniteShot = player.abilities.instabuild
                    || bolt.isInfinite(ammunition, bow, player);
            if (!world.isClientSide) {
                AbstractArrowEntity projectile = customArrow(bolt.createArrow(world, ammunition, player));
                projectile.shootFromRotation(player, player.xRot, player.yRot, 0.0F,
                        power * 3.0F, 1.0F);
                if (power == 1.0F) projectile.setCritArrow(true);
                int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, bow);
                if (powerLevel > 0) {
                    projectile.setBaseDamage(projectile.getBaseDamage() + powerLevel * 0.5D + 0.5D);
                }
                int punchLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, bow);
                if (punchLevel > 0) projectile.setKnockback(punchLevel);
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, bow) > 0) {
                    projectile.setSecondsOnFire(100);
                }
                bow.hurtAndBreak(1, player, entity -> entity.broadcastBreakEvent(player.getUsedItemHand()));
                if (infiniteShot) projectile.pickup = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                world.addFreshEntity(projectile);
            }
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                    1.0F / (random.nextFloat() * 0.4F + 1.2F) + power * 0.5F);
            if (!infiniteShot && !player.abilities.instabuild) {
                ammunition.shrink(1);
                if (ammunition.isEmpty()) player.inventory.removeItem(ammunition);
            }
            player.awardStat(Stats.ITEM_USED.get(this));
        }
    }

    public static final class FishingRod extends FishingRodItem implements MaterialBacked {
        private final MaterialDefinition material;
        public FishingRod(MaterialDefinition material, Properties properties) {
            super(properties.defaultDurability(material.toolDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairIngredient().test(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override public void appendHoverText(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    static void addToolTooltip(MaterialDefinition material, List<ITextComponent> tooltip) {
        if ("adamantine".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip.adamantine.tool", 4));
        } else if ("aquarium".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip.aquarium.tool", 4));
        } else if ("coldiron".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip.coldiron.tool", 3));
        } else if ("mithril".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip.mithril.tool"));
        } else if ("starsteel".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip.starsteel.tool", 10));
        }
    }

    private static void addArmorTooltip(MaterialDefinition material, List<ITextComponent> tooltip) {
        if ("adamantine".equals(material.name()) || "aquarium".equals(material.name())
                || "coldiron".equals(material.name()) || "mithril".equals(material.name())
                || "starsteel".equals(material.name())) {
            tooltip.add(new TranslationTextComponent("tooltip." + material.name() + ".armor"));
        }
    }
}
