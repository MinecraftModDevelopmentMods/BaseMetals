package zone.moddev.mc.basemetals.content;

import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.BaseMetals;


import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.AnimalArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;
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

    public static final class Pickaxe extends PickaxeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Pickaxe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), properties.attributes(
                    PickaxeItem.createAttributes(new MaterialTier(material), 1, -2.8F))); this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Axe extends AxeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Axe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), properties.attributes(AxeItem.createAttributes(
                    new MaterialTier(material), 4.0F + material.baseAttackDamage(), material.axeAttackSpeed())));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Shovel extends ShovelItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shovel(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), properties.attributes(
                    ShovelItem.createAttributes(new MaterialTier(material), 1.5F, -3.0F))); this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Hoe extends HoeItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Hoe(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), properties.attributes(ItemAttributeModifiers.builder()
                    // Preserve the legacy hoe's zero added attack damage.
                    .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID,
                            0.0D, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID,
                            material.baseAttackDamage() - 3.0D, AttributeModifier.Operation.ADD_VALUE),
                            EquipmentSlotGroup.MAINHAND)
                    .build()));
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static class Sword extends SwordItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Sword(MaterialDefinition material, Properties properties) {
            super(new MaterialTier(material), properties.attributes(
                    SwordItem.createAttributes(new MaterialTier(material), 3, -2.4F))); this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) { addToolTooltip(material, tooltip); }
    }

    public static final class Armor extends ArmorItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Armor(MaterialDefinition material, EquipmentSlot slot, Properties properties) {
            super(armorMaterial(material), armorType(slot),
                    properties.durability(armorType(slot).getDurability(material.armorDurabilityFactor()))); this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addArmorTooltip(material, tooltip);
        }
    }

    public static final class HorseArmor extends AnimalArmorItem implements MaterialBacked {
        private final MaterialDefinition material;
        public HorseArmor(MaterialDefinition material, Properties properties) {
            super(armorMaterial(material), AnimalArmorItem.BodyType.EQUESTRIAN, false, properties);
            this.material = material;
        }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
    }

    // MaterialArmor must register these named materials before the item registry runs.
    private static Holder<ArmorMaterial> armorMaterial(MaterialDefinition material) {
        return DeferredHolder.create(Registries.ARMOR_MATERIAL,
                ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, material.name()));
    }

    private static ArmorItem.Type armorType(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> ArmorItem.Type.HELMET;
            case CHEST -> ArmorItem.Type.CHESTPLATE;
            case LEGS -> ArmorItem.Type.LEGGINGS;
            case FEET -> ArmorItem.Type.BOOTS;
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
        public Shears(MaterialDefinition material, Properties properties) { super(properties); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return isMaterialRepairIngredient(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static class Shield extends ShieldItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Shield(MaterialDefinition material, Properties properties) { super(properties); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return isMaterialRepairIngredient(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static class Bow extends BowItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Bow(MaterialDefinition material, Properties properties) { super(properties); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public Predicate<ItemStack> getAllSupportedProjectiles() {
            return stack -> stack.getItem() instanceof net.minecraft.world.item.ArrowItem
                    && (!(stack.getItem() instanceof BaseMetalAmmoItem ammo)
                            || ammo.kind() == BaseMetalAmmoItem.Kind.ARROW);
        }

        @Override public Predicate<ItemStack> getSupportedHeldProjectiles() {
            return getAllSupportedProjectiles();
        }

        @Override public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack, ItemStack weaponStack) {
            arrow.setBaseDamage(arrow.getBaseDamage() + material.baseAttackDamage() - 1.0D);
            return arrow;
        }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return isMaterialRepairIngredient(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static final class Crossbow extends BowItem implements MaterialBacked {
        private final MaterialDefinition material;
        public Crossbow(MaterialDefinition material, Properties properties) { super(properties); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public Predicate<ItemStack> getAllSupportedProjectiles() {
            return stack -> stack.getItem() instanceof BaseMetalAmmoItem ammo
                    && ammo.kind() == BaseMetalAmmoItem.Kind.BOLT;
        }
        @Override public Predicate<ItemStack> getSupportedHeldProjectiles() {
            return getAllSupportedProjectiles();
        }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return isMaterialRepairIngredient(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override public ItemStack getDefaultCreativeAmmo(
                @Nullable net.minecraft.world.entity.player.Player player, ItemStack weaponStack) {
            return ModContent.item("iron_bolt").get().getDefaultInstance();
        }

        // BowItem's firing pipeline handles ammunition, Infinity, durability and
        // projectile enchantments, including the weapon stack passed to arrows.
        @Override public AbstractArrow customArrow(AbstractArrow arrow, ItemStack projectileStack,
                ItemStack weaponStack) {
            arrow.setBaseDamage(arrow.getBaseDamage() + material.baseAttackDamage() - 1.0D);
            return arrow;
        }

        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    public static final class FishingRod extends FishingRodItem implements MaterialBacked {
        private final MaterialDefinition material;
        public FishingRod(MaterialDefinition material, Properties properties) { super(properties); this.material = material; }
        @Override public MaterialDefinition baseMetalsMaterial() { return material; }
        @Override public boolean isValidRepairItem(ItemStack toRepair, ItemStack repair) {
            return isMaterialRepairIngredient(repair) || super.isValidRepairItem(toRepair, repair);
        }
        @Override
        public void appendHoverText(ItemStack stack, Item.TooltipContext context,
                List<Component> tooltip, TooltipFlag flag) {
            addToolTooltip(material, tooltip);
        }
    }

    static void addToolTooltip(MaterialDefinition material, List<Component> tooltip) {
        Component component = switch (material.name()) {
            case "adamantine" -> net.minecraft.network.chat.Component.translatable("tooltip.adamantine.tool", 4);
            case "aquarium" -> net.minecraft.network.chat.Component.translatable("tooltip.aquarium.tool", 4);
            case "coldiron" -> net.minecraft.network.chat.Component.translatable("tooltip.coldiron.tool", 3);
            case "mithril" -> net.minecraft.network.chat.Component.translatable("tooltip.mithril.tool");
            case "starsteel" -> net.minecraft.network.chat.Component.translatable("tooltip.starsteel.tool", 10);
            default -> null;
        };
        if (component != null) tooltip.add(component);
    }

    private static void addArmorTooltip(MaterialDefinition material, List<Component> tooltip) {
        if (material.name().equals("adamantine") || material.name().equals("aquarium")
                || material.name().equals("coldiron") || material.name().equals("mithril")
                || material.name().equals("starsteel")) {
            tooltip.add(net.minecraft.network.chat.Component.translatable("tooltip." + material.name() + ".armor"));
        }
    }
}
