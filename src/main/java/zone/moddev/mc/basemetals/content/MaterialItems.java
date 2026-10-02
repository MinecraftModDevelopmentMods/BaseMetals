package zone.moddev.mc.basemetals.content;

import java.util.List;

import javax.annotation.Nullable;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Enchantments;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
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

    public static final class Pickaxe extends ItemPickaxe implements MaterialBacked {
        private final MaterialDefinition material;
        public Pickaxe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 1, -2.8F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Axe extends ItemAxe implements MaterialBacked {
        private final MaterialDefinition material;
        public Axe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 4.0F + material.baseAttackDamage(),
                    material.axeAttackSpeed(), properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Shovel extends ItemSpade implements MaterialBacked {
        private final MaterialDefinition material;
        public Shovel(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 1.5F, -3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Hoe extends ItemHoe implements MaterialBacked {
        private final MaterialDefinition material;
        public Hoe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), material.baseAttackDamage() - 3.0F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Sword extends ItemSword implements MaterialBacked {
        private final MaterialDefinition material;
        public Sword(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), 3, -2.4F, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Armor extends ItemArmor implements MaterialBacked {
        private final MaterialDefinition material;
        public Armor(MaterialDefinition material, EntityEquipmentSlot slot, Properties properties) {
            super(new MaterialArmor(material), slot, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addArmorTooltip(material, tooltip); }
    }

    public static final class HorseArmor extends Item implements MaterialBacked {
        private final MaterialDefinition material;
        private final HorseArmorType armorType;
        public HorseArmor(MaterialDefinition material, Properties properties) {
            super(properties.maxStackSize(1));
            this.material = material;
            String texture = BaseMetals.MOD_ID + ":textures/entity/horse/armor/horse_armor_"
                    + material.name() + ".png";
            String hash = Integer.toHexString(material.name().hashCode());
            this.armorType = HorseArmorType.create("BASEMETALS_" + material.name().toUpperCase(),
                    material.horseArmorProtection(), texture, hash, this);
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public HorseArmorType getHorseArmorType(ItemStack stack) { return armorType; }
    }

    public static final class BurnableBlock extends ItemBlock {
        private final int burnTime;
        public BurnableBlock(net.minecraft.block.Block block, int burnTime, Properties properties) {
            super(block, properties);
            this.burnTime = burnTime;
        }
        @Override public int getBurnTime(ItemStack stack) { return burnTime; }
    }

    public static final class Shears extends ItemShears implements MaterialBacked {
        private final MaterialDefinition material;
        public Shears(MaterialDefinition material, Properties properties) {
            super(properties.defaultMaxDamage(material.toolDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
        }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Shield extends ItemShield implements MaterialBacked {
        private final MaterialDefinition material;
        public Shield(MaterialDefinition material, Properties properties) {
            super(properties.defaultMaxDamage(material.shieldDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
        }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Bow extends ItemBow implements MaterialBacked {
        private final MaterialDefinition material;

        public Bow(MaterialDefinition material, Properties properties) {
            super(properties.defaultMaxDamage(material.toolDurability()));
            this.material = material;
        }

        @Override public MaterialDefinition baseMetalsMaterial() { return material; }

        @Override
        public EntityArrow customizeArrow(EntityArrow arrow) {
            // Forge calls this before applying Power, so enchantment bonuses stay unchanged.
            arrow.setDamage(arrow.getDamage() + material.baseAttackDamage() - 1.0D);

            return arrow;
        }

        @Override protected boolean isArrow(ItemStack stack) {
            if (stack.getItem() instanceof BaseMetalAmmoItem) {
                return ((BaseMetalAmmoItem) stack.getItem()).kind() == BaseMetalAmmoItem.Kind.ARROW;
            }
            return stack.getItem() instanceof ItemArrow;
        }
        @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
        }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
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
        public void onPlayerStoppedUsing(ItemStack bow, World world, EntityLivingBase user, int timeLeft) {
            if (!(user instanceof EntityPlayer)) return;
            EntityPlayer player = (EntityPlayer) user;
            boolean hasInfiniteAmmo = player.abilities.isCreativeMode
                    || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, bow) > 0;
            ItemStack ammunition = findAmmo(player);
            int charge = getUseDuration(bow) - timeLeft;
            charge = ForgeEventFactory.onArrowLoose(bow, world, player, charge,
                    !ammunition.isEmpty() || hasInfiniteAmmo);
            if (charge < 0 || ammunition.isEmpty() && !hasInfiniteAmmo) return;

            if (ammunition.isEmpty()) ammunition = new ItemStack(ModContent.item("iron_bolt").get());
            float power = getArrowVelocity(charge);
            if (power < 0.1F) return;

            BaseMetalAmmoItem bolt = (BaseMetalAmmoItem) ammunition.getItem();
            boolean infiniteShot = player.abilities.isCreativeMode
                    || bolt.isInfinite(ammunition, bow, player);
            if (!world.isRemote) {
                EntityArrow projectile = customizeArrow(bolt.createArrow(world, ammunition, player));
                projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0F,
                        power * 3.0F, 1.0F);
                if (power == 1.0F) projectile.setIsCritical(true);
                int powerLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, bow);
                if (powerLevel > 0) {
                    projectile.setDamage(projectile.getDamage() + powerLevel * 0.5D + 0.5D);
                }
                int punchLevel = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, bow);
                if (punchLevel > 0) projectile.setKnockbackStrength(punchLevel);
                if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, bow) > 0) {
                    projectile.setFire(100);
                }
                bow.damageItem(1, player);
                if (infiniteShot) projectile.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                world.spawnEntity(projectile);
            }
            world.playSound(null, player.posX, player.posY, player.posZ,
                    SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F,
                    1.0F / (random.nextFloat() * 0.4F + 1.2F) + power * 0.5F);
            if (!infiniteShot && !player.abilities.isCreativeMode) {
                ammunition.shrink(1);
                if (ammunition.isEmpty()) player.inventory.deleteStack(ammunition);
            }
            player.addStat(StatList.ITEM_USED.get(this));
        }
    }

    public static final class FishingRod extends ItemFishingRod implements MaterialBacked {
        private final MaterialDefinition material;
        public FishingRod(MaterialDefinition material, Properties properties) {
            super(properties.defaultMaxDamage(material.toolDurability()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
            return new MaterialTier(material).getRepairMaterial().test(repair) || super.getIsRepairable(toRepair, repair);
        }
        @Override public void addInformation(ItemStack stack, @Nullable World world,
                List<ITextComponent> tooltip, ITooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    static void addToolTooltip(MaterialDefinition material, List<ITextComponent> tooltip) {
        if ("adamantine".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip.adamantine.tool", 4));
        } else if ("aquarium".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip.aquarium.tool", 4));
        } else if ("coldiron".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip.coldiron.tool", 3));
        } else if ("mithril".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip.mithril.tool"));
        } else if ("starsteel".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip.starsteel.tool", 10));
        }
    }

    private static void addArmorTooltip(MaterialDefinition material, List<ITextComponent> tooltip) {
        if ("adamantine".equals(material.name()) || "aquarium".equals(material.name())
                || "coldiron".equals(material.name()) || "mithril".equals(material.name())
                || "starsteel".equals(material.name())) {
            tooltip.add(new TextComponentTranslation("tooltip." + material.name() + ".armor"));
        }
    }
}
