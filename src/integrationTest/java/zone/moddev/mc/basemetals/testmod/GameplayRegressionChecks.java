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

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import io.netty.buffer.Unpooled;

/** Runs against real Forge registries and recipes, not stand-in item classes. */
final class GameplayRegressionChecks {
    private final List<String> failures = new ArrayList<String>();
    private int checks;
    private EntityArrow firedArrow;
    private String stage;
    private int stageFailures;

    static int run(MinecraftServer server) {
        GameplayRegressionChecks probe = new GameplayRegressionChecks();
        WorldServer world = server.getWorld(DimensionType.OVERWORLD);

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
            check(TileEntityFurnace.isItemFuel(stack) == combustible, "furnace accepts " + name);
        }
        check(furnaceBurnTime(stack("charcoal_block")) == 16000,
                "charcoal block fuel");
    }

    private void repairs(MinecraftServer server, WorldServer world) {
        int repairs = 0;
        for (IRecipe recipe : server.getRecipeManager().getRecipes()) {
            if (!(recipe instanceof PlateRepairRecipe)) continue;
            repairs++;
            ItemStack original = recipe.getRecipeOutput().copy();
            original.setDamage(7);
            original.setDisplayName(new TextComponentString("Keep my name"));
            original.addEnchantment(Enchantments.UNBREAKING, 2);
            original.getOrCreateTag().setString("custom_proof", "keep my data");
            String targetName = recipe.getRecipeOutput().getItem().getRegistryName().getPath();
            String plateName = targetName.substring(0, targetName.indexOf('_')) + "_plate";
            if (!ModContent.itemsById().containsKey(plateName)) {
                // Empty compatibility tags may be filled by another mod; they cannot repair anything alone.
                InventoryBasic grid = grid();
                grid.setInventorySlotContents(0, original.copy());
                check(!recipe.matches(grid, world), "unavailable repair plate " + recipe.getId());
                check(recipe.isDynamic(), "unavailable repair stays hidden " + recipe.getId());
                continue;
            }
            ItemStack plate = stack(plateName);

            for (int targetSlot = 0; targetSlot < 9; targetSlot++) {
                for (int plateSlot = 0; plateSlot < 9; plateSlot++) {
                    if (targetSlot == plateSlot) continue;
                    InventoryBasic grid = grid();
                    grid.setInventorySlotContents(targetSlot, original.copy());
                    grid.setInventorySlotContents(plateSlot, plate.copy());
                    check(recipe.matches(grid, world), "repair slots " + recipe.getId());
                    ItemStack repaired = recipe.getCraftingResult(grid);
                    ItemStack expected = original.copy();
                    expected.setDamage(0);
                    check(ItemStack.areItemStacksEqual(expected, repaired), "repair NBT " + recipe.getId());
                }
            }

            InventoryBasic grid = grid();
            grid.setInventorySlotContents(4, plate.copy());
            check(!recipe.matches(grid, world), "plate alone " + recipe.getId());
            grid.setInventorySlotContents(0, original.copy());
            grid.setInventorySlotContents(8, new ItemStack(Items.STICK));
            check(!recipe.matches(grid, world), "repair extra ingredient " + recipe.getId());
            check(recipe.getCraftingResult(grid).isEmpty(), "invalid repair result " + recipe.getId());
            grid.setInventorySlotContents(8, ItemStack.EMPTY);
            grid.getStackInSlot(0).setDamage(0);
            check(!recipe.matches(grid, world), "undamaged repair " + recipe.getId());
            check(recipe.isDynamic(), "repair hidden from recipe book " + recipe.getId());
            check(original.getDamage() == 7, "repair does not mutate input " + recipe.getId());
        }
        check(repairs == 110, "all 110 plate repairs tested");
    }

    private void crafting(MinecraftServer server, WorldServer world) {
        String[] materials = {"diamond", "emerald", "gold", "quartz"};
        Item[] ingredients = {Items.DIAMOND, Items.EMERALD, Items.GOLD_INGOT, Items.QUARTZ};
        for (int i = 0; i < materials.length; i++) {
            IRecipe door = server.getRecipeManager().getRecipe(id(materials[i] + "_door"));
            IRecipe trapdoor = server.getRecipeManager().getRecipe(id(materials[i] + "_trapdoor"));
            InventoryCrafting grid = craftingGrid();
            for (int slot : new int[] {0, 1, 3, 4, 6, 7}) {
                grid.setInventorySlotContents(slot, new ItemStack(ingredients[i]));
            }
            check(door.matches(grid, world), materials[i] + " door pattern");
            check(!trapdoor.matches(grid, world), materials[i] + " door is not a trapdoor");
            check(door.getCraftingResult(grid).getCount() == 3, materials[i] + " three doors");
            grid.setInventorySlotContents(6, ItemStack.EMPTY);
            grid.setInventorySlotContents(7, ItemStack.EMPTY);
            check(!door.matches(grid, world) && trapdoor.matches(grid, world),
                    materials[i] + " trapdoor pattern");
        }

        QuietPlayer player = new QuietPlayer(world);
        player.inventory.clear();
        player.inventory.setInventorySlotContents(0, stack("coldiron_ingot"));
        Advancement unlock = server.getAdvancementManager().getAdvancement(id("recipes/coldiron_sword"));
        check(unlock != null, "Cold Iron recipe unlock loaded");
        if (unlock != null) {
            InventoryChangeTrigger.Instance condition = (InventoryChangeTrigger.Instance)
                    unlock.getCriteria().get("has_ingredient_1").getCriterionInstance();
            check(condition.test(player.inventory), "Cold Iron ingot matches recipe unlock");
            player.inventory.setInventorySlotContents(0, stack("copper_ingot"));
            check(!condition.test(player.inventory), "unrelated ingot cannot unlock Cold Iron sword");
        }
        check(server.getAdvancementManager().getAdvancement(id("recipes/coldiron_chestplate_plate_repair"))
                == null, "repair has no recipe-book unlock");
    }

    private void rangedWeapons(WorldServer world) {
        QuietPlayer player = new QuietPlayer(world);
        net.minecraft.util.math.BlockPos origin = world.getSpawnPoint().up(5);
        world.getChunk(origin);
        player.setPosition(origin.getX() + 0.5D, origin.getY(), origin.getZ() + 0.5D);
        int families = 0;
        for (Map.Entry<String, RegistryHandle<Item>> entry : ModContent.itemsById().entrySet()) {
            if (!entry.getKey().endsWith("_bow")) continue;
            families++;
            String materialName = entry.getKey().substring(0, entry.getKey().length() - 4);
            MaterialDefinition material = ((MaterialBacked) entry.getValue().get()).baseMetalsMaterial();
            ItemStack arrow = stack(materialName + "_arrow");
            ItemArrow arrowItem = (ItemArrow) arrow.getItem();
            EntityArrow projectile = arrowItem.createArrow(world, arrow, player);
            check(close(projectile.getDamage(), 1.0D + material.baseAttackDamage()),
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
        enchanted.addEnchantment(Enchantments.POWER, 3);
        fire(world, player, enchanted, stack("gold_arrow"),
                damage("steel_bow") + damage("gold_arrow") + 2.0D, "Power after material damage");

        ItemStack named = stack("adamantine_arrow");
        named.setDisplayName(new TextComponentString("Recovered arrow"));
        named.getOrCreateTag().setString("ammo_proof", "preserved");
        MaterialProjectile original = (MaterialProjectile) ((ItemArrow) named.getItem())
                .createArrow(world, named, player);
        NBTTagCompound saved = new NBTTagCompound();
        ((Entity) original).writeWithoutTypeId(saved);
        MaterialProjectile restored = new MaterialProjectile(ModEntities.CUSTOM_ARROW.get(), world);
        ((Entity) restored).read(saved);
        check(ItemStack.areItemStacksEqual(named, restored.getAmmunition()), "arrow pickup NBT after reload");
        check(close(((EntityArrow) original).getDamage(), ((EntityArrow) restored).getDamage()),
                "arrow damage after reload");
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        try {
            original.writeSpawnData(buffer);
            restored.readSpawnData(buffer);
            check(ItemStack.areItemStacksEqual(named, restored.getAmmunition()), "arrow spawn NBT");
        } finally {
            buffer.release();
        }
    }

    private void fire(WorldServer world, QuietPlayer player, ItemStack launcher,
            ItemStack ammunition, double expected, String label) {
        player.inventory.clear();
        ItemStack supply = ammunition.copy();
        supply.setCount(8);
        player.setHeldItem(EnumHand.MAIN_HAND, launcher);
        player.setHeldItem(EnumHand.OFF_HAND, supply);
        firedArrow = null;

        ItemBow bow = (ItemBow) launcher.getItem();
        bow.onPlayerStoppedUsing(launcher, world, player, bow.getUseDuration(launcher) - 20);
        check(firedArrow != null, label + " spawned on server");
        if (firedArrow != null) {
            check(close(firedArrow.getDamage(), expected), label + " damage expected=" + expected
                    + " actual=" + firedArrow.getDamage());
            check(firedArrow.getIsCritical(), label + " fully drawn critical");
        }
        check(supply.getCount() == 7, label + " consumes one round");
        check(launcher.getDamage() == 1, label + " uses one durability");
    }

    @SubscribeEvent
    public void arrowSpawned(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityArrow) firedArrow = (EntityArrow) event.getEntity();
    }

    private void armor(WorldServer world) {
        QuietPlayer player = new QuietPlayer(world);
        player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.0D);
        player.setHealth(40.0F);
        equip(player, "adamantine");
        tick(player);
        check(player.getHealth() == 40.0F, "Adamantine armor cannot hurt high-health wearer");
        check(player.isPotionActive(MobEffects.RESISTANCE)
                && player.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() == 1,
                "damaged Adamantine armor gives Resistance II");

        player.getActivePotionMap().clear();
        equip(player, "starsteel");
        tick(player);
        check(player.isPotionActive(MobEffects.JUMP_BOOST)
                && player.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() == 3,
                "damaged Starsteel armor gives Jump Boost IV");
        check(player.isPotionActive(MobEffects.SPEED)
                && player.getActivePotionEffect(MobEffects.SPEED).getAmplifier() == 2,
                "damaged Starsteel armor gives Speed III");

        player.getActivePotionMap().clear();
        equip(player, "aquarium");
        player.submerge();
        player.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200));
        tick(player);
        check(player.isPotionActive(MobEffects.WATER_BREATHING)
                && player.isPotionActive(MobEffects.RESISTANCE)
                && !player.isPotionActive(MobEffects.MINING_FATIGUE), "damaged submerged Aquarium armor");
    }

    private static void equip(QuietPlayer player, String material) {
        EntityEquipmentSlot[] slots = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST,
                EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
        String[] forms = {"helmet", "chestplate", "leggings", "boots"};
        for (int i = 0; i < slots.length; i++) {
            ItemStack piece = stack(material + "_" + forms[i]);
            piece.setDamage(7);
            player.setItemStackToSlot(slots[i], piece);
        }
    }

    private static void tick(QuietPlayer player) {
        player.ticksExisted = 20;
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
        int burnTime = stack.getBurnTime();
        int fallback = TileEntityFurnace.getBurnTimes().getOrDefault(stack.getItem(), 0);
        return net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack,
                burnTime == -1 ? fallback : burnTime);
    }

    private static ItemStack stack(String name) {
        return new ItemStack(ModContent.item(name).get());
    }

    private static InventoryBasic grid() {
        return new InventoryBasic(new TextComponentString("repair probe"), 9);
    }

    private static InventoryCrafting craftingGrid() {
        return new InventoryCrafting(new Container() {
            @Override public boolean canInteractWith(net.minecraft.entity.player.EntityPlayer player) {
                return true;
            }
        }, 3, 3);
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation("basemetals", name);
    }

    private static final class QuietPlayer extends FakePlayer {
        private QuietPlayer(WorldServer world) {
            super(world, new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000131"),
                    "GameplayProbe"));
        }
        @Override protected void onNewPotionEffect(PotionEffect effect) {}
        @Override protected void onChangedPotionEffect(PotionEffect effect, boolean reapply) {}
        @Override protected void onFinishedPotionEffect(PotionEffect effect) {}
        private void submerge() { inWater = true; }
    }
}
