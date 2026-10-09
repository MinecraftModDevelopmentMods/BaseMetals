package zone.moddev.mc.basemetals.testmod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import zone.moddev.mc.basemetals.content.BaseMetalAmmoItem;
import zone.moddev.mc.basemetals.content.MaterialBacked;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.content.RegistryHandle;
import zone.moddev.mc.basemetals.entity.MaterialProjectile;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.recipe.PlateRepairRecipe;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.Items;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import io.netty.buffer.Unpooled;

/** Runs against real Forge registries and recipes, not stand-in item classes. */
final class GameplayRegressionChecks {
    private final List<String> failures = new ArrayList<String>();
    private int checks;
    private AbstractArrow firedArrow;
    private String stage;
    private int stageFailures;

    static int run(MinecraftServer server) {
        GameplayRegressionChecks probe = new GameplayRegressionChecks();
        ServerLevel world = server.getLevel(Level.OVERWORLD);

        probe.begin("fuels");
        probe.fuels();
        probe.begin("crafting");
        probe.crafting(server, world);
        probe.begin("repairs");
        probe.repairs(server, world);
        MinecraftForge.EVENT_BUS.register(probe);
        try {
            probe.begin("ranged weapons");
            probe.rangedWeapons(world);
            probe.begin("crossbow ammunition");
            probe.crossbowAmmunition(world);
        } finally {
            MinecraftForge.EVENT_BUS.unregister(probe);
        }
        probe.begin("armor");
        probe.armor(world);

        if (!probe.failures.isEmpty()) {
            throw new IllegalStateException("Gameplay regressions (" + probe.failures.size() + "): "
                    + String.join("; ", probe.failures));
        }
        return probe.checks;
    }

    private void fuels() {
        for (Map.Entry<String, RegistryHandle<Item>> entry : ModContent.itemsById().entrySet()) {
            String name = entry.getKey();
            if (!name.endsWith("_nugget") && !name.endsWith("_powder")
                    && !name.endsWith("_smallpowder")) continue;

            ItemStack stack = new ItemStack(entry.getValue().get());
            boolean combustible = name.startsWith("coal_") || name.startsWith("charcoal_");
            int expected = combustible ? (name.endsWith("_powder") ? 1600 : 200) : 0;
            int actual = furnaceBurnTime(stack);
            check(actual == expected, "fuel " + name + " expected=" + expected + " actual=" + actual);
            check(AbstractFurnaceBlockEntity.isFuel(stack) == combustible, "furnace accepts " + name);
        }
        check(furnaceBurnTime(stack("charcoal_block")) == 16000,
                "charcoal block fuel");
    }

    private void repairs(MinecraftServer server, ServerLevel world) {
        int repairs = 0;
        for (Recipe recipe : server.getRecipeManager().getRecipes()) {
            if (!(recipe instanceof PlateRepairRecipe)) continue;
            repairs++;
            ItemStack original = recipe.getResultItem().copy();
            original.setDamageValue(7);
            original.setHoverName(new TextComponent("Keep my name"));
            original.enchant(Enchantments.UNBREAKING, 2);
            original.getOrCreateTag().putString("custom_proof", "keep my data");
            String targetName = recipe.getResultItem().getItem().getRegistryName().getPath();
            String plateName = targetName.substring(0, targetName.indexOf('_')) + "_plate";
            if (!ModContent.itemsById().containsKey(plateName)) {
                // Empty compatibility tags may be filled by another mod; they cannot repair anything alone.
                CraftingContainer grid = grid();
                grid.setItem(0, original.copy());
                check(!recipe.matches(grid, world), "unavailable repair plate " + recipe.getId());
                check(recipe.isSpecial(), "unavailable repair stays hidden " + recipe.getId());
                continue;
            }
            ItemStack plate = stack(plateName);

            for (int targetSlot = 0; targetSlot < 9; targetSlot++) {
                for (int plateSlot = 0; plateSlot < 9; plateSlot++) {
                    if (targetSlot == plateSlot) continue;
                    CraftingContainer grid = grid();
                    grid.setItem(targetSlot, original.copy());
                    grid.setItem(plateSlot, plate.copy());
                    check(recipe.matches(grid, world), "repair slots " + recipe.getId());
                    ItemStack repaired = recipe.assemble(grid);
                    ItemStack expected = original.copy();
                    expected.setDamageValue(0);
                    check(ItemStack.matches(expected, repaired), "repair NBT " + recipe.getId());
                }
            }

            CraftingContainer grid = grid();
            grid.setItem(4, plate.copy());
            check(!recipe.matches(grid, world), "plate alone " + recipe.getId());
            grid.setItem(0, original.copy());
            grid.setItem(8, new ItemStack(Items.STICK));
            check(!recipe.matches(grid, world), "repair extra ingredient " + recipe.getId());
            check(recipe.assemble(grid).isEmpty(), "invalid repair result " + recipe.getId());
            grid.setItem(8, ItemStack.EMPTY);
            grid.getItem(0).setDamageValue(0);
            check(!recipe.matches(grid, world), "undamaged repair " + recipe.getId());
            check(recipe.isSpecial(), "repair hidden from recipe book " + recipe.getId());
            check(original.getDamageValue() == 7, "repair does not mutate input " + recipe.getId());
        }
        check(repairs == 110, "all 110 plate repairs tested");
    }

    private void crafting(MinecraftServer server, ServerLevel world) {
        String[] materials = {"diamond", "emerald", "gold", "quartz"};
        Item[] ingredients = {Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, Items.QUARTZ};
        for (int i = 0; i < materials.length; i++) {
            Recipe door = server.getRecipeManager().byKey(id(materials[i] + "_door")).orElse(null);
            Recipe trapdoor = server.getRecipeManager().byKey(id(materials[i] + "_trapdoor")).orElse(null);
            CraftingContainer grid = craftingGrid();
            for (int slot : new int[] {0, 1, 3, 4, 6, 7}) {
                grid.setItem(slot, new ItemStack(ingredients[i]));
            }
            check(door.matches(grid, world), materials[i] + " door pattern");
            check(!trapdoor.matches(grid, world), materials[i] + " door is not a trapdoor");
            check(door.assemble(grid).getCount() == 3, materials[i] + " three doors");
            grid.setItem(6, ItemStack.EMPTY);
            grid.setItem(7, ItemStack.EMPTY);
            check(!door.matches(grid, world) && trapdoor.matches(grid, world),
                    materials[i] + " trapdoor pattern");
        }

        QuietPlayer player = new QuietPlayer(world);
        player.getInventory().clearContent();
        player.getInventory().setItem(0, stack("coldiron_ingot"));
        Advancement unlock = server.getAdvancements().getAdvancement(id("recipes/coldiron_sword"));
        check(unlock != null, "Cold Iron recipe unlock loaded");
        if (unlock != null) {
            InventoryChangeTrigger.TriggerInstance condition = (InventoryChangeTrigger.TriggerInstance)
                    unlock.getCriteria().get("has_ingredient_1").getTrigger();
            check(condition.matches(player.getInventory(), player.getInventory().getItem(0), 1, 0, 35), "Cold Iron ingot matches recipe unlock");
            player.getInventory().setItem(0, stack("copper_ingot"));
            check(!condition.matches(player.getInventory(), player.getInventory().getItem(0), 1, 0, 35), "unrelated ingot cannot unlock Cold Iron sword");
        }
        check(server.getAdvancements().getAdvancement(id("recipes/coldiron_chestplate_plate_repair"))
                == null, "repair has no recipe-book unlock");
    }

    private void rangedWeapons(ServerLevel world) {
        QuietPlayer player = new QuietPlayer(world);
        net.minecraft.core.BlockPos origin = world.getSharedSpawnPos().above(5);
        world.getChunk(origin);
        player.setPos(origin.getX() + 0.5D, origin.getY(), origin.getZ() + 0.5D);
        int families = 0;
        for (Map.Entry<String, RegistryHandle<Item>> entry : ModContent.itemsById().entrySet()) {
            if (!entry.getKey().endsWith("_bow")) continue;
            families++;
            String materialName = entry.getKey().substring(0, entry.getKey().length() - 4);
            MaterialDefinition material = ((MaterialBacked) entry.getValue().get()).baseMetalsMaterial();
            ItemStack arrow = stack(materialName + "_arrow");
            ArrowItem arrowItem = (ArrowItem) arrow.getItem();
            AbstractArrow projectile = arrowItem.createArrow(world, arrow, player);
            check(close(projectile.getBaseDamage(), 1.0D + material.baseAttackDamage()),
                    "vanilla bow + " + materialName + " arrow");
            check(stack(materialName + "_fishing_rod").getMaxDamage() == material.toolDurability(),
                    "material fishing rod durability " + materialName);

            fire(world, player, stack(materialName + "_bow"), arrow,
                    2.0D * material.baseAttackDamage(), materialName + " bow");
            fire(world, player, stack(materialName + "_crossbow"), stack(materialName + "_bolt"),
                    2.0D * material.baseAttackDamage(), materialName + " crossbow");
            fire(world, player, stack(materialName + "_bow"), new ItemStack(Items.ARROW),
                    1.0D + material.baseAttackDamage(), materialName + " bow + vanilla arrow");
        }
        check(families == 27, "27 ranged material families");
        fire(world, player, new ItemStack(Items.BOW), new ItemStack(Items.ARROW), 2.0D, "vanilla shot");
        fire(world, player, stack("gold_bow"), stack("adamantine_arrow"),
                damage("gold_bow") + damage("adamantine_arrow"), "mixed bow materials");
        fire(world, player, stack("adamantine_bow"), stack("gold_arrow"),
                damage("gold_bow") + damage("adamantine_arrow"), "swapped bow materials");
        ItemStack enchanted = stack("steel_bow");
        enchanted.enchant(Enchantments.POWER_ARROWS, 3);
        fire(world, player, enchanted, stack("gold_arrow"),
                damage("steel_bow") + damage("gold_arrow") + 2.0D, "Power after material damage");

        ItemStack named = stack("adamantine_arrow");
        named.setHoverName(new TextComponent("Recovered arrow"));
        named.getOrCreateTag().putString("ammo_proof", "preserved");
        MaterialProjectile original = (MaterialProjectile) ((ArrowItem) named.getItem())
                .createArrow(world, named, player);
        CompoundTag saved = new CompoundTag();
        ((Entity) original).saveWithoutId(saved);
        MaterialProjectile restored = new MaterialProjectile(ModEntities.CUSTOM_ARROW.get(), world);
        ((Entity) restored).load(saved);
        check(ItemStack.matches(named, restored.getAmmunition()), "arrow pickup NBT after reload");
        check(close(((AbstractArrow) original).getBaseDamage(), ((AbstractArrow) restored).getBaseDamage()),
                "arrow damage after reload");
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            original.writeSpawnData(buffer);
            restored.readSpawnData(buffer);
            check(ItemStack.matches(named, restored.getAmmunition()), "arrow spawn NBT");
        } finally {
            buffer.release();
        }
    }

    private void fire(ServerLevel world, QuietPlayer player, ItemStack launcher,
            ItemStack ammunition, double expected, String label) {
        player.getInventory().clearContent();
        ItemStack supply = ammunition.copy();
        supply.setCount(8);
        player.setItemInHand(InteractionHand.MAIN_HAND, launcher);
        player.setItemInHand(InteractionHand.OFF_HAND, supply);
        firedArrow = null;

        BowItem bow = (BowItem) launcher.getItem();
        bow.releaseUsing(launcher, world, player, bow.getUseDuration(launcher) - 20);
        check(firedArrow != null, label + " spawned on server");
        if (firedArrow != null) {
            check(close(firedArrow.getBaseDamage(), expected), label + " damage expected=" + expected
                    + " actual=" + firedArrow.getBaseDamage());
            check(firedArrow.isCritArrow(), label + " fully drawn critical");
        }
        check(supply.getCount() == 7, label + " consumes one round");
        check(launcher.getDamageValue() == 1, label + " uses one durability");
    }

    private void crossbowAmmunition(ServerLevel world) {
        QuietPlayer player = new QuietPlayer(world);
        net.minecraft.core.BlockPos origin = world.getSharedSpawnPos().above(5);
        world.getChunk(origin);
        player.setPos(origin.getX() + 0.5D, origin.getY(), origin.getZ() + 0.5D);

        for (Map.Entry<String, RegistryHandle<Item>> entry : ModContent.itemsById().entrySet()) {
            if (!entry.getKey().endsWith("_crossbow")) continue;

            String name = entry.getKey();
            String boltName = name.substring(0, name.length() - "_crossbow".length()) + "_bolt";
            player.getAbilities().instabuild = true;
            player.getInventory().clearContent();
            ItemStack launcher = stack(name);
            player.setItemInHand(InteractionHand.MAIN_HAND, launcher);

            release(world, player, launcher);
            checkBolt("iron_bolt", damage(name) + damage("iron_bolt"),
                    name + " Creative without ammunition");
            check(firedArrow != null
                    && firedArrow.pickup == AbstractArrow.Pickup.CREATIVE_ONLY,
                    name + " virtual bolt cannot be collected in Survival");
            check(launcher.getDamageValue() == 0, name + " Creative preserves durability");

            for (boolean creative : new boolean[] {false, true}) {
                player.getAbilities().instabuild = creative;
                player.getInventory().clearContent();
                launcher = stack(name);
                player.setItemInHand(InteractionHand.MAIN_HAND, launcher);
                ItemStack supply = stack(boltName);
                supply.setCount(8);
                supply.setHoverName(new TextComponent("Inventory bolt"));
                supply.getOrCreateTag().putString("ammo_proof", "inventory");
                player.getInventory().setItem(9, supply);

                release(world, player, launcher);
                String label = name + " inventory bolt " + (creative ? "Creative" : "Survival");
                checkBolt(boltName, damage(name) + damage(boltName), label);
                if (firedArrow instanceof MaterialProjectile) {
                    ItemStack recovered = ((MaterialProjectile) firedArrow).getAmmunition();
                    check("inventory".equals(recovered.getOrCreateTag().getString("ammo_proof")),
                            label + " preserves ammunition NBT");
                }
                check(supply.getCount() == (creative ? 8 : 7), label + " ammunition use");
                check(launcher.getDamageValue() == (creative ? 0 : 1), label + " durability use");
            }
        }

        ItemStack[] wrongAmmunition = {new ItemStack(Items.ARROW), new ItemStack(Items.TIPPED_ARROW),
                new ItemStack(Items.SPECTRAL_ARROW), stack("adamantine_arrow"), ItemStack.EMPTY};
        for (boolean creative : new boolean[] {false, true}) {
            for (ItemStack wrong : wrongAmmunition) {
                player.getAbilities().instabuild = creative;
                player.getInventory().clearContent();
                ItemStack launcher = stack("adamantine_crossbow");
                ItemStack supply = wrong.copy();
                if (!supply.isEmpty()) supply.setCount(8);
                ItemStack original = supply.copy();
                player.setItemInHand(InteractionHand.MAIN_HAND, launcher);
                player.setItemInHand(InteractionHand.OFF_HAND, supply);

                release(world, player, launcher);
                String label = "wrong crossbow ammunition " + wrong + " "
                        + (creative ? "Creative" : "Survival");
                if (creative) {
                    checkBolt("iron_bolt", damage("adamantine_crossbow") + damage("iron_bolt"), label);
                    check(firedArrow != null
                            && firedArrow.pickup == AbstractArrow.Pickup.CREATIVE_ONLY,
                            label + " virtual bolt pickup");
                } else {
                    check(firedArrow == null, label + " does not fire");
                }
                check(ItemStack.matches(original, supply), label + " is not consumed");
                check(launcher.getDamageValue() == 0, label + " does not wear the launcher");
            }
        }

        // Bows still use Minecraft's virtual arrow when Creative players carry no ammunition.
        player.getAbilities().instabuild = true;
        player.getInventory().clearContent();
        ItemStack bow = stack("adamantine_bow");
        player.setItemInHand(InteractionHand.MAIN_HAND, bow);
        release(world, player, bow);
        check(firedArrow != null && !(firedArrow instanceof MaterialProjectile),
                "Creative bow keeps the vanilla arrow fallback");
        check(firedArrow != null && close(firedArrow.getBaseDamage(), damage("adamantine_bow") + 1.0D),
                "Creative bow fallback retains launcher damage");
    }

    private void release(ServerLevel world, QuietPlayer player, ItemStack launcher) {
        firedArrow = null;
        BowItem bow = (BowItem) launcher.getItem();
        bow.releaseUsing(launcher, world, player, bow.getUseDuration(launcher) - 20);
    }

    private void checkBolt(String ammunition, double expected, String label) {
        check(firedArrow instanceof MaterialProjectile, label + " creates a material projectile");
        if (!(firedArrow instanceof MaterialProjectile)) return;

        check(firedArrow.getType() == ModEntities.CUSTOM_BOLT.get(), label + " creates a bolt");
        check(((MaterialProjectile) firedArrow).getAmmunition().getItem() == stack(ammunition).getItem(),
                label + " keeps the correct ammunition");
        check(close(firedArrow.getBaseDamage(), expected), label + " material damage");
        check(firedArrow.isCritArrow(), label + " fully drawn critical");
    }

    @SubscribeEvent
    public void arrowSpawned(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AbstractArrow) firedArrow = (AbstractArrow) event.getEntity();
    }

    private void armor(ServerLevel world) {
        QuietPlayer player = new QuietPlayer(world);
        player.getAttribute(Attributes.MAX_HEALTH).setBaseValue(40.0D);
        player.setHealth(40.0F);
        equip(player, "adamantine");
        tick(player);
        check(player.getHealth() == 40.0F, "Adamantine armor cannot hurt high-health wearer");
        check(player.hasEffect(MobEffects.DAMAGE_RESISTANCE)
                && player.getEffect(MobEffects.DAMAGE_RESISTANCE).getAmplifier() == 1,
                "damaged Adamantine armor gives Resistance II");

        player.getActiveEffectsMap().clear();
        equip(player, "starsteel");
        tick(player);
        check(player.hasEffect(MobEffects.JUMP)
                && player.getEffect(MobEffects.JUMP).getAmplifier() == 3,
                "damaged Starsteel armor gives Jump Boost IV");
        check(player.hasEffect(MobEffects.MOVEMENT_SPEED)
                && player.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() == 2,
                "damaged Starsteel armor gives Speed III");

        player.getActiveEffectsMap().clear();
        equip(player, "aquarium");
        player.submerge();
        player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 200));
        tick(player);
        check(player.hasEffect(MobEffects.WATER_BREATHING)
                && player.hasEffect(MobEffects.DAMAGE_RESISTANCE)
                && !player.hasEffect(MobEffects.DIG_SLOWDOWN), "damaged submerged Aquarium armor");
    }

    private static void equip(QuietPlayer player, String material) {
        EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST,
                EquipmentSlot.LEGS, EquipmentSlot.FEET};
        String[] forms = {"helmet", "chestplate", "leggings", "boots"};
        for (int i = 0; i < slots.length; i++) {
            ItemStack piece = stack(material + "_" + forms[i]);
            piece.setDamageValue(7);
            player.setItemSlot(slots[i], piece);
        }
    }

    private static void tick(QuietPlayer player) {
        player.tickCount = 20;
        MinecraftForge.EVENT_BUS.post(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
    }

    private void check(boolean passed, String message) {
        checks++;
        if (!passed && stageFailures++ < 3) failures.add(stage + ": " + message);
    }

    private void begin(String name) {
        stage = name;
        stageFailures = 0;
    }

    private static double damage(String item) {
        return ((MaterialBacked) stack(item).getItem()).baseMetalsMaterial().baseAttackDamage();
    }

    private static boolean close(double actual, double expected) {
        return Math.abs(actual - expected) < 0.000001D;
    }

    private static int furnaceBurnTime(ItemStack stack) {
        int burnTime = stack.getBurnTime(net.minecraft.world.item.crafting.RecipeType.SMELTING);
        int fallback = AbstractFurnaceBlockEntity.getFuel().getOrDefault(stack.getItem(), 0);
        return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack,
                burnTime == -1 ? fallback : burnTime, net.minecraft.world.item.crafting.RecipeType.SMELTING);
    }

    private static ItemStack stack(String name) {
        return new ItemStack(ModContent.item(name).get());
    }

    private static CraftingContainer grid() {
        return craftingGrid();
    }

    private static CraftingContainer craftingGrid() {
        return new CraftingContainer(new AbstractContainerMenu(null, 0) {
            @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) {
                return true;
            }
        }, 3, 3);
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("basemetals", name);
    }

    private static final class QuietPlayer extends FakePlayer {
        private QuietPlayer(ServerLevel world) {
            super(world, new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000131"),
                    "GameplayProbe"));
        }
        @Override protected void onEffectAdded(MobEffectInstance effect, net.minecraft.world.entity.Entity source) {}
        @Override protected void onEffectUpdated(MobEffectInstance effect, boolean reapply, net.minecraft.world.entity.Entity source) {}
        @Override protected void onEffectRemoved(MobEffectInstance effect) {}
        private void submerge() { wasTouchingWater = true; }
    }
}
