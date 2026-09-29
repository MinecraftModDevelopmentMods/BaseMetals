package com.mcmoddev.basemetals.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.items.ItemBaseMetalsArrow;
import com.mcmoddev.basemetals.items.ItemBaseMetalsBolt;
import com.mcmoddev.basemetals.items.ItemBaseMetalsBow;
import com.mcmoddev.basemetals.items.ItemBaseMetalsCrossbow;
import com.mcmoddev.basemetals.properties.ColdIronProperty;
import com.mcmoddev.basemetals.properties.StarSteelProperty;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.entity.EntityCustomArrow;
import com.mcmoddev.lib.entity.EntityCustomBolt;
import com.mcmoddev.lib.entity.EntityHelpers;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.item.ItemBolt;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.advancements.Advancement;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;

/** Test-only packaged-runtime probe. This class never enters the public jar. */
@Mod(
        modid = BaseMetalsRuntimeProbe.MODID,
        name = "Base Metals Runtime Probe",
        version = "1",
        dependencies = "required-after:basemetals",
        acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsRuntimeProbe {
    public static final String MODID = "basemetals_runtime_probe";

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent event) throws IOException {
        final String expectedOreSpawn = requiredProperty("basemetals.probe.expectedOrespawn");
        final boolean expectedAncientWarfare = Boolean.parseBoolean(
                System.getProperty("basemetals.probe.expectedAncientWarfare", "false"));
        final boolean expectedCoFHWorld = Boolean.parseBoolean(
                System.getProperty("basemetals.probe.expectedCoFHWorld", "false"));
        final File marker = new File(requiredProperty("basemetals.probe.marker"));
        final Properties result = new Properties();

        require(Loader.isModLoaded("basemetals"), "Base Metals did not load");
        require(Loader.isModLoaded("mmdlib"), "MMDLib did not load");
        require(Loader.isModLoaded("orespawn"), "OreSpawn did not load");
        require(Loader.isModLoaded("ancientwarfare") == expectedAncientWarfare,
                "Ancient Warfare presence did not match the requested compatibility profile");
        require(Loader.isModLoaded("ancientwarfarenpc") == expectedAncientWarfare,
                "Ancient Warfare NPC presence did not match the requested compatibility profile");
        require(Loader.isModLoaded("cofhworld") == expectedCoFHWorld,
                "CoFH World presence did not match the requested compatibility profile");
        requireRegistered(Block.REGISTRY.getObject(new ResourceLocation("basemetals", "copper_ore")),
                "basemetals:copper_ore");
        requireRegistered(Block.REGISTRY.getObject(new ResourceLocation("basemetals", "adamantine_ore")),
                "basemetals:adamantine_ore");
        requireRegistered(Item.REGISTRY.getObject(new ResourceLocation("basemetals", "steel_ingot")),
                "basemetals:steel_ingot");
        final int fuelFormsChecked = verifyFuelForms();
        final int fishingRodsChecked = verifyFishingRodDurability();
        final int hoeMaterialNamesChecked = verifyHoeMaterialNames();
        final int ancientWarfareHoeChecks = expectedAncientWarfare
                ? verifyAncientWarfareHoes(
                        FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld())
                : 0;
        final int doorRecipesChecked = verifyDoorAndTrapdoorRecipes(
                FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        final int railRecipesChecked = verifyRailRecipes(
                FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld());
        final int railRecipeAdvancementsChecked = verifyRailRecipeAdvancement(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final int crossbowBoltsChecked = verifyCrossbowBoltImpact(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final int bowArrowsChecked = verifyBowArrowImpact(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final int arrowPickupIdentitiesChecked = verifyArrowPickupIdentity(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final int crossbowsChecked = verifyCrossbowRegistrations();
        final int rangedItemsChecked = verifyRangedItemRegistrations();
        final int rangedDamageChecks = verifyRangedDamage(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final int armorEffectChecks = verifyArmorEffects(
                FMLCommonHandler.instance().getMinecraftServerInstance());
        final WorldgenCounts worldgen = expectedCoFHWorld
                ? verifyCoFHWorldgen(FMLCommonHandler.instance().getMinecraftServerInstance())
                : new WorldgenCounts(0, 0);

        final String baseMetalsVersion = version("basemetals");
        final String mmdLibVersion = version("mmdlib");
        final String oreSpawnVersion = version("orespawn");
        require("2.6.0.112021".equals(baseMetalsVersion),
                "Unexpected Base Metals version " + baseMetalsVersion);
        // The artifact build is rc2.36, while its public FML metadata retains
        // the upstream mod version rc2.
        require("1.0.0-rc2".equals(mmdLibVersion),
                "Unexpected MMDLib version " + mmdLibVersion);
        require(expectedOreSpawn.equals(oreSpawnVersion),
                "Unexpected OreSpawn version " + oreSpawnVersion + ", expected " + expectedOreSpawn);

        result.setProperty("basemetals", baseMetalsVersion);
        result.setProperty("mmdlib", mmdLibVersion);
        result.setProperty("orespawn", oreSpawnVersion);
        result.setProperty("phase", System.getProperty("basemetals.probe.phase", "unknown"));
        result.setProperty("fuelFormsChecked", Integer.toString(fuelFormsChecked));
        result.setProperty("fishingRodsChecked", Integer.toString(fishingRodsChecked));
        result.setProperty("hoeMaterialNamesChecked", Integer.toString(hoeMaterialNamesChecked));
        result.setProperty("ancientWarfare", Boolean.toString(expectedAncientWarfare));
        result.setProperty("cofhWorld", Boolean.toString(expectedCoFHWorld));
        result.setProperty("managedOres", Integer.toString(worldgen.managedOres));
        result.setProperty("cofhAntimony", Integer.toString(worldgen.cofhAntimony));
        result.setProperty("ancientWarfareHoeChecks", Integer.toString(ancientWarfareHoeChecks));
        result.setProperty("doorRecipesChecked", Integer.toString(doorRecipesChecked));
        result.setProperty("railRecipesChecked", Integer.toString(railRecipesChecked));
        result.setProperty("railRecipeAdvancementsChecked", Integer.toString(railRecipeAdvancementsChecked));
        result.setProperty("crossbowBoltsChecked", Integer.toString(crossbowBoltsChecked));
        result.setProperty("bowArrowsChecked", Integer.toString(bowArrowsChecked));
        result.setProperty("arrowPickupIdentitiesChecked",
                Integer.toString(arrowPickupIdentitiesChecked));
        result.setProperty("crossbowsChecked", Integer.toString(crossbowsChecked));
        result.setProperty("rangedItemsChecked", Integer.toString(rangedItemsChecked));
        result.setProperty("rangedDamageChecks", Integer.toString(rangedDamageChecks));
        result.setProperty("armorEffectChecks", Integer.toString(armorEffectChecks));
        result.setProperty("registered", "true");
        marker.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(marker)) {
            result.store(output, "Base Metals packaged-runtime qualification");
        }
        FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
    }

    private static WorldgenCounts verifyCoFHWorldgen(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        final BlockPos spawn = world.getSpawnPoint();
        final int centerX = spawn.getX() >> 4;
        final int centerZ = spawn.getZ() >> 4;

        // Load a one-chunk border so Forge population runs for every chunk in
        // the bounded 9x9 sample at the centre.
        for (int chunkX = centerX - 5; chunkX <= centerX + 5; chunkX++) {
            for (int chunkZ = centerZ - 5; chunkZ <= centerZ + 5; chunkZ++) {
                world.getChunkProvider().provideChunk(chunkX, chunkZ);
            }
        }

        final Block antimony = Block.REGISTRY.getObject(
                new ResourceLocation("basemetals", "antimony_ore"));
        int managedOres = 0;
        int cofhAntimony = 0;
        for (int chunkX = centerX - 4; chunkX <= centerX + 4; chunkX++) {
            for (int chunkZ = centerZ - 4; chunkZ <= centerZ + 4; chunkZ++) {
                final Chunk chunk = world.getChunkProvider().provideChunk(chunkX, chunkZ);
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 128; y++) {
                            final Block block = chunk.getBlockState(
                                    new BlockPos(x, y, z)).getBlock();
                            if (block == antimony) {
                                cofhAntimony++;
                            } else if (isManagedBaseMetalsOre(block)) {
                                managedOres++;
                            }
                        }
                    }
                }
            }
        }
        require(managedOres == 0,
                "Disabled OreSpawn provider placed " + managedOres + " managed Base Metals ores");
        require(cofhAntimony > 0,
                "CoFH World did not place the Base Metals antimony test ore");
        return new WorldgenCounts(managedOres, cofhAntimony);
    }

    private static boolean isManagedBaseMetalsOre(final Block block) {
        final ResourceLocation name = block.getRegistryName();
        if (name == null || !"basemetals".equals(name.getNamespace())) {
            return false;
        }
        switch (name.getPath()) {
            case "coldiron_ore":
            case "adamantine_ore":
            case "starsteel_ore":
            case "copper_ore":
            case "silver_ore":
            case "tin_ore":
            case "lead_ore":
            case "zinc_ore":
            case "mercury_ore":
            case "nickel_ore":
            case "platinum_ore":
                return true;
            default:
                return false;
        }
    }

    private static final class WorldgenCounts {
        private final int managedOres;
        private final int cofhAntimony;

        private WorldgenCounts(final int managedOres, final int cofhAntimony) {
            this.managedOres = managedOres;
            this.cofhAntimony = cofhAntimony;
        }
    }

    private static int verifyFuelForms() {
        int checked = 0;
        for (final Item item : Item.REGISTRY) {
            final ResourceLocation registryName = item.getRegistryName();
            if (registryName == null || !"basemetals".equals(registryName.getNamespace())) {
                continue;
            }

            final String path = registryName.getPath();
            if (!isNuggetOrPowder(path)) {
                continue;
            }

            final int expected = expectedBurnTime(path);
            final int actual = TileEntityFurnace.getItemBurnTime(new ItemStack(item));
            require(actual == expected,
                    registryName + " has furnace burn time " + actual + ", expected " + expected);
            checked++;
        }
        require(checked > 0, "No Base Metals nugget or powder fuel forms were checked");
        return checked;
    }

    private static int verifyFishingRodDurability() {
        int checked = 0;
        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (!material.hasItem(Names.FISHING_ROD)) {
                continue;
            }

            final Item fishingRod = material.getItem(Names.FISHING_ROD);
            final ResourceLocation registryName = fishingRod.getRegistryName();
            if (registryName == null || !"basemetals".equals(registryName.getNamespace())) {
                continue;
            }

            final int expected = material.getToolDurability();
            final int actual = new ItemStack(fishingRod).getMaxDamage();
            require(actual == expected,
                    registryName + " has maximum durability " + actual + ", expected " + expected);
            checked++;
        }
        require(checked == 27,
                "Checked " + checked + " Base Metals fishing rods, expected 27");
        return checked;
    }

    private static int verifyHoeMaterialNames() {
        int checked = 0;
        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (!material.hasItem(Names.HOE)) {
                continue;
            }

            final Item hoe = material.getItem(Names.HOE);
            final ResourceLocation registryName = hoe.getRegistryName();
            if (registryName == null || !"basemetals".equals(registryName.getNamespace())) {
                continue;
            }

            require(hoe instanceof ItemHoe,
                    registryName + " is not an ItemHoe");
            final String materialName = ((ItemHoe) hoe).getMaterialName();
            final ToolMaterial resolved;
            try {
                resolved = ToolMaterial.valueOf(materialName);
            } catch (final IllegalArgumentException exception) {
                throw new IllegalStateException(registryName + " exposes invalid ToolMaterial name "
                        + materialName, exception);
            }
            final ToolMaterial expected = Materials.getToolMaterialFor(material);
            require(resolved == expected,
                    registryName + " exposes ToolMaterial " + resolved
                            + ", expected " + expected);
            checked++;
        }
		require(checked == 24,
				"Checked " + checked + " Base Metals hoe material names, expected 24");
        return checked;
    }

    private static int verifyAncientWarfareHoes(final World world) {
        try {
            final Class<?> workerClass = Class.forName(
                    "net.shadowmage.ancientwarfare.npc.entity.NpcWorker");
            final Class<?> workTypeClass = Class.forName(
                    "net.shadowmage.ancientwarfare.core.interfaces.IWorkSite$WorkType");
            final Object farming = enumConstant(workTypeClass, "FARMING");
            final Method effectivenessMethod = workerClass.getMethod(
                    "getWorkEffectiveness", workTypeClass);
            int checked = 0;
            for (final String path : new String[] {
                    "emerald_hoe", "steel_hoe", "adamantine_hoe" }) {
                final EntityLivingBase worker = (EntityLivingBase) workerClass
                        .getConstructor(World.class).newInstance(world);
                worker.setHeldItem(EnumHand.MAIN_HAND,
                        new ItemStack(requireItem("basemetals", path)));
                final float effectiveness = ((Number) effectivenessMethod
                        .invoke(worker, farming)).floatValue();
                require(!Float.isNaN(effectiveness) && !Float.isInfinite(effectiveness)
                                && effectiveness > 0.0F,
                        "Ancient Warfare returned invalid work effectiveness "
                                + effectiveness + " for basemetals:" + path);
                checked++;
            }
            require(checked == 3,
                    "Checked " + checked + " Ancient Warfare hoes, expected 3");
            return checked;
        } catch (final InvocationTargetException exception) {
            throw new IllegalStateException("Ancient Warfare rejected a Base Metals hoe",
                    exception.getCause());
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException(
                    "Could not exercise Ancient Warfare's farming effectiveness path", exception);
        }
    }

    private static Object enumConstant(final Class<?> enumClass, final String name) {
        for (final Object constant : enumClass.getEnumConstants()) {
            if (((Enum<?>) constant).name().equals(name)) {
                return constant;
            }
        }
        throw new IllegalStateException("Missing enum constant " + enumClass.getName() + "." + name);
    }

    private static boolean isNuggetOrPowder(final String path) {
        return path.endsWith("_nugget")
                || path.endsWith("_powder")
                || path.endsWith("_smallpowder");
    }

    private static int expectedBurnTime(final String path) {
        if (path.startsWith("coal_") || path.startsWith("charcoal_")) {
            return path.endsWith("_powder") ? 1600 : 200;
        }
        return 0;
    }

    private static int verifyDoorAndTrapdoorRecipes(final World world) {
        int checked = 0;
        checked += verifyDoorAndTrapdoorRecipe(world, net.minecraft.init.Items.DIAMOND, "diamond");
        checked += verifyDoorAndTrapdoorRecipe(world, net.minecraft.init.Items.EMERALD, "emerald");
        checked += verifyDoorAndTrapdoorRecipe(world, net.minecraft.init.Items.GOLD_INGOT, "gold");
        checked += verifyDoorAndTrapdoorRecipe(world, net.minecraft.init.Items.QUARTZ, "quartz");
        return checked;
    }

    private static int verifyRailRecipes(final World world) {
        final Item steel = requireItem("basemetals", "steel_ingot");
        final Item stick = net.minecraft.init.Items.STICK;
        final Item redstone = net.minecraft.init.Items.REDSTONE;
        final Item redstoneTorch = Item.getItemFromBlock(Blocks.REDSTONE_TORCH);
        final Item stonePressurePlate = Item.getItemFromBlock(Blocks.STONE_PRESSURE_PLATE);

        verifyRailRecipe(world, "rail", Item.getItemFromBlock(Blocks.RAIL), 16,
                steel, null, steel,
                steel, stick, steel,
                steel, null, steel);
        verifyRailRecipe(world, "activator_rail", Item.getItemFromBlock(Blocks.ACTIVATOR_RAIL), 6,
                steel, stick, steel,
                steel, redstoneTorch, steel,
                steel, stick, steel);
        verifyRailRecipe(world, "detector_rail", Item.getItemFromBlock(Blocks.DETECTOR_RAIL), 6,
                steel, null, steel,
                steel, stonePressurePlate, steel,
                steel, redstone, steel);
        return 3;
    }

    private static int verifyRailRecipeAdvancement(final MinecraftServer server) {
        final ResourceLocation advancementId = new ResourceLocation(
                "basemetals", "recipes/transportation/rail");
        final Advancement advancement = server.getAdvancementManager().getAdvancement(advancementId);
        require(advancement != null, "Recipe-book advancement was not loaded: " + advancementId);
        require(advancement.getCriteria().containsKey("has_iron_ingot"),
                advancementId + " does not recognize iron ingots");
        require(advancement.getCriteria().containsKey("has_steel_ingot"),
                advancementId + " does not recognize Base Metals steel ingots");
        require(advancement.getCriteria().containsKey("has_the_recipe"),
                advancementId + " lacks its recipe-unlocked criterion");
        require(advancement.getRequirements().length == 1
                        && advancement.getRequirements()[0].length == 3,
                advancementId + " discovery criteria are not alternatives");
        return 1;
    }

    private static int verifyCrossbowBoltImpact(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        final Item crossbow = requireItem("basemetals", "steel_crossbow");
        final Item boltItem = requireItem("basemetals", "steel_bolt");
        final ItemStack crossbowStack = new ItemStack(crossbow);
        final ItemStack ammunition = new ItemStack(boltItem);
        final EntityPlayerMP shooter = new EntityPlayerMP(server, world,
                new GameProfile(UUID.randomUUID(), "BaseMetalsBoltProbe"),
                new PlayerInteractionManager(world));
        final BlockPos spawn = world.getSpawnPoint();
        final double x = spawn.getX() + 0.5D;
        final double y = spawn.getY() + 16.0D;
        final double z = spawn.getZ() + 0.5D;
        shooter.setPositionAndRotation(x, y, z, 0.0F, 0.0F);
        shooter.setHeldItem(EnumHand.OFF_HAND, ammunition);

        final EntityIronGolem target = new EntityIronGolem(world);
        target.setPosition(x, y, z + 2.5D);
        require(world.spawnEntity(target), "Could not spawn the crossbow impact target");
        final float healthBefore = target.getHealth();
        final int boltsBefore = countBolts(world);
        EntityCustomBolt projectile = null;
        try {
            crossbow.onPlayerStoppedUsing(crossbowStack, world, shooter,
                    crossbow.getMaxItemUseDuration(crossbowStack) - 20);
            require(countBolts(world) == boltsBefore + 1,
                    "Base Metals crossbow did not create a server-side bolt");
            projectile = findLiveBolt(world);
            require(projectile != null, "Spawned Base Metals bolt could not be located");
            final MMDMaterial steel = Materials.getMaterialByName(MaterialNames.STEEL);
            requireDamage(steel.getBaseAttackDamage() * 2.0D, projectile.getDamage(),
                    "steel crossbow and bolt impact projectile");
            final ItemStack savedAmmunition = EntityHelpers.readFromNBTItemStack(
                    projectile.writeToNBT(new NBTTagCompound()));
            require(savedAmmunition.getItem() == boltItem && savedAmmunition.getCount() == 1,
                    "Base Metals bolt did not preserve its ammunition item");
            for (int tick = 0; tick < 4 && target.getHealth() == healthBefore; tick++) {
                projectile.onUpdate();
            }
            require(target.getHealth() < healthBefore,
                    "Base Metals crossbow bolt did not damage its target");
            require(ammunition.isEmpty(),
                    "Base Metals crossbow consumed an unexpected number of bolts");
            require(crossbowStack.getItemDamage() == 1,
                    "Base Metals crossbow did not lose one durability after firing");
        } finally {
            if (projectile != null) {
                projectile.setDead();
            }
            target.setDead();
        }
        return 1;
    }

    private static int verifyBowArrowImpact(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        final Item bow = requireItem("basemetals", "steel_bow");
        final Item arrowItem = requireItem("basemetals", "steel_arrow");
        final ItemStack bowStack = new ItemStack(bow);
        final ItemStack ammunition = new ItemStack(arrowItem);
        final EntityPlayerMP shooter = new EntityPlayerMP(server, world,
                new GameProfile(UUID.randomUUID(), "BaseMetalsArrowProbe"),
                new PlayerInteractionManager(world));
        final BlockPos spawn = world.getSpawnPoint();
        final double x = spawn.getX() + 0.5D;
        final double y = spawn.getY() + 16.0D;
        final double z = spawn.getZ() + 0.5D;
        shooter.setPositionAndRotation(x, y, z, 0.0F, 0.0F);
        shooter.setHeldItem(EnumHand.OFF_HAND, ammunition);

        final EntityIronGolem target = new EntityIronGolem(world);
        target.setPosition(x, y, z + 2.5D);
        require(world.spawnEntity(target), "Could not spawn the bow impact target");
        final float healthBefore = target.getHealth();
        final int arrowsBefore = countArrows(world);
        EntityArrow projectile = null;
        try {
            bow.onPlayerStoppedUsing(bowStack, world, shooter,
                    bow.getMaxItemUseDuration(bowStack) - 20);
            require(countArrows(world) == arrowsBefore + 1,
                    "Base Metals bow did not create a server-side arrow");
            projectile = findLiveArrow(world);
            require(projectile != null, "Spawned Base Metals arrow could not be located");
            final MMDMaterial steel = Materials.getMaterialByName(MaterialNames.STEEL);
            requireDamage(steel.getBaseAttackDamage() * 2.0D, projectile.getDamage(),
                    "steel bow and arrow impact projectile");
            final ItemStack savedAmmunition = EntityHelpers.readFromNBTItemStack(
                    projectile.writeToNBT(new NBTTagCompound()));
            require(savedAmmunition.getItem() == arrowItem && savedAmmunition.getCount() == 1,
                    "Base Metals arrow did not preserve its ammunition item");
            for (int tick = 0; tick < 4 && target.getHealth() == healthBefore; tick++) {
                projectile.onUpdate();
            }
            require(target.getHealth() < healthBefore,
                    "Base Metals bow arrow did not damage its target");
            require(ammunition.isEmpty(),
                    "Base Metals bow consumed an unexpected number of arrows");
            require(bowStack.getItemDamage() == 1,
                    "Base Metals bow did not lose one durability after firing");
        } finally {
            if (projectile != null) {
                projectile.setDead();
            }
            target.setDead();
        }
        return 1;
    }

    private static int verifyArrowPickupIdentity(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        final EntityPlayerMP shooter = new EntityPlayerMP(server, world,
                new GameProfile(UUID.randomUUID(), "BaseMetalsArrowIdentityProbe"),
                new PlayerInteractionManager(world));
        final BlockPos spawn = world.getSpawnPoint();
        shooter.setPositionAndRotation(spawn.getX() + 0.5D, spawn.getY() + 16.0D,
                spawn.getZ() + 0.5D, 0.0F, 0.0F);

        int checked = 0;
        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (!isBaseMetalsItem(material, Names.ARROW)) {
                continue;
            }
            final Item arrowItem = material.getItem(Names.ARROW);
            final ItemStack inventoryAmmunition = new ItemStack(arrowItem);
            final EntityCustomArrow projectile = ((ItemBaseMetalsArrow) arrowItem)
                    .createArrow(world, inventoryAmmunition, shooter);
            inventoryAmmunition.shrink(1);
            require(inventoryAmmunition.isEmpty(),
                    arrowItem.getRegistryName() + " identity test did not consume its source stack");

            final NBTTagCompound saved = projectile.writeToNBT(new NBTTagCompound());
            final ItemStack pickup = EntityHelpers.readFromNBTItemStack(saved);
            require(pickup.getItem() == arrowItem && pickup.getCount() == 1,
                    arrowItem.getRegistryName() + " became another item before pickup");

            final EntityCustomArrow reloaded = new EntityCustomArrow(world);
            reloaded.readFromNBT(saved);
            final ItemStack reloadedPickup = EntityHelpers.readFromNBTItemStack(
                    reloaded.writeToNBT(new NBTTagCompound()));
            require(reloadedPickup.getItem() == arrowItem && reloadedPickup.getCount() == 1,
                    arrowItem.getRegistryName() + " lost its identity after save and reload");
            projectile.setDead();
            reloaded.setDead();
            checked++;
        }
        require(checked == 27,
                "Checked " + checked + " Base Metals arrow identities, expected 27");

        final Item arrowItem = requireItem("basemetals", "steel_arrow");
        final ItemStack ammunition = new ItemStack(arrowItem);
        final ItemStack vanillaBow = new ItemStack(net.minecraft.init.Items.BOW);
        shooter.setHeldItem(EnumHand.OFF_HAND, ammunition);
        final int arrowsBefore = countArrows(world);
        EntityArrow projectile = null;
        try {
            net.minecraft.init.Items.BOW.onPlayerStoppedUsing(vanillaBow, world, shooter,
                    vanillaBow.getMaxItemUseDuration() - 20);
            require(countArrows(world) == arrowsBefore + 1,
                    "Vanilla bow did not create a Base Metals arrow projectile");
            projectile = findLiveArrow(world);
            require(projectile instanceof EntityCustomArrow,
                    "Vanilla bow did not retain the Base Metals arrow entity");
            require(ammunition.isEmpty(),
                    "Vanilla bow did not consume the last Base Metals arrow");
            final ItemStack pickup = EntityHelpers.readFromNBTItemStack(
                    projectile.writeToNBT(new NBTTagCompound()));
            require(pickup.getItem() == arrowItem && pickup.getCount() == 1,
                    "Vanilla bow changed a Base Metals arrow into another pickup item");
        } finally {
            if (projectile != null) {
                projectile.setDead();
            }
        }
        return checked + 1;
    }

    private static int verifyCrossbowRegistrations() {
        int checked = 0;
        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (!material.hasItem(Names.CROSSBOW)) {
                continue;
            }
            final Item crossbow = material.getItem(Names.CROSSBOW);
            final ResourceLocation registryName = crossbow.getRegistryName();
            if (registryName == null || !"basemetals".equals(registryName.getNamespace())) {
                continue;
            }
            require(crossbow instanceof ItemBaseMetalsCrossbow,
                    registryName + " does not use the corrected Base Metals crossbow implementation");
            checked++;
        }
        require(checked == 27, "Checked " + checked + " Base Metals crossbows, expected 27");
        return checked;
    }

    private static int verifyRangedItemRegistrations() {
        int arrows = 0;
        int bolts = 0;
        int bows = 0;
        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (isBaseMetalsItem(material, Names.ARROW)) {
                require(material.getItem(Names.ARROW) instanceof ItemBaseMetalsArrow,
                        material.getItem(Names.ARROW).getRegistryName()
                                + " does not use the material-damage arrow implementation");
                arrows++;
            }
            if (isBaseMetalsItem(material, Names.BOLT)) {
                require(material.getItem(Names.BOLT) instanceof ItemBaseMetalsBolt,
                        material.getItem(Names.BOLT).getRegistryName()
                                + " does not use the material-damage bolt implementation");
                bolts++;
            }
            if (isBaseMetalsItem(material, Names.BOW)) {
                require(material.getItem(Names.BOW) instanceof ItemBaseMetalsBow,
                        material.getItem(Names.BOW).getRegistryName()
                                + " does not use the material-damage bow implementation");
                bows++;
            }
        }
        require(arrows == 27, "Checked " + arrows + " Base Metals arrows, expected 27");
        require(bolts == 27, "Checked " + bolts + " Base Metals bolts, expected 27");
        require(bows == 27, "Checked " + bows + " Base Metals bows, expected 27");
        return arrows + bolts + bows;
    }

    private static boolean isBaseMetalsItem(final MMDMaterial material, final Names name) {
        if (!material.hasItem(name)) {
            return false;
        }
        final ResourceLocation registryName = material.getItem(name).getRegistryName();
        return registryName != null && "basemetals".equals(registryName.getNamespace());
    }

    private static int verifyRangedDamage(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        final EntityPlayerMP shooter = new EntityPlayerMP(server, world,
                new GameProfile(UUID.randomUUID(), "BaseMetalsDamageProbe"),
                new PlayerInteractionManager(world));
        final BlockPos spawn = world.getSpawnPoint();
        shooter.setPositionAndRotation(spawn.getX() + 0.5D, spawn.getY() + 16.0D,
                spawn.getZ() + 0.5D, 0.0F, 0.0F);
        int checked = 0;

        for (final MMDMaterial material : Materials.getAllMaterials()) {
            if (!isBaseMetalsItem(material, Names.ARROW)
                    || !isBaseMetalsItem(material, Names.BOLT)
                    || !isBaseMetalsItem(material, Names.BOW)
                    || !isBaseMetalsItem(material, Names.CROSSBOW)) {
                continue;
            }
            final double component = material.getBaseAttackDamage();
            requireDamage(1.0D + component, createArrowDamage(world, shooter, material),
                    material.getName() + " arrow ammunition");
            requireDamage(1.0D + component, createBoltDamage(world, shooter, material),
                    material.getName() + " bolt ammunition");
            requireDamage(component + component,
                    fireBowDamage(world, shooter, material.getItem(Names.BOW),
                            material.getItem(Names.ARROW), 0),
                    material.getName() + " matching bow and arrow");
            requireDamage(component + component,
                    fireCrossbowDamage(world, shooter, material.getItem(Names.CROSSBOW),
                            material.getItem(Names.BOLT), 0),
                    material.getName() + " matching crossbow and bolt");
            checked += 4;
        }
        require(checked == 108, "Checked " + checked + " ranged material combinations, expected 108");

        final MMDMaterial gold = Materials.getMaterialByName(MaterialNames.GOLD);
        final MMDMaterial iron = Materials.getMaterialByName(MaterialNames.IRON);
        final MMDMaterial adamantine = Materials.getMaterialByName(MaterialNames.ADAMANTINE);
        final MMDMaterial steel = Materials.getMaterialByName(MaterialNames.STEEL);
        require(createArrowDamage(world, shooter, gold)
                        < createArrowDamage(world, shooter, iron)
                        && createArrowDamage(world, shooter, iron)
                        < createArrowDamage(world, shooter, adamantine),
                "Gold, iron, and Adamantine arrows do not have increasing material damage");
        require(createBoltDamage(world, shooter, gold)
                        < createBoltDamage(world, shooter, iron)
                        && createBoltDamage(world, shooter, iron)
                        < createBoltDamage(world, shooter, adamantine),
                "Gold, iron, and Adamantine bolts do not have increasing material damage");

        requireDamage(2.0D,
                fireBowDamage(world, shooter, net.minecraft.init.Items.BOW,
                        net.minecraft.init.Items.ARROW, 0),
                "vanilla bow and arrow");
        checked++;
        requireDamage(1.0D + adamantine.getBaseAttackDamage(),
                fireBowDamage(world, shooter, net.minecraft.init.Items.BOW,
                        adamantine.getItem(Names.ARROW), 0),
                "vanilla bow and Adamantine arrow");
        checked++;
        requireDamage(1.0D + adamantine.getBaseAttackDamage(),
                fireBowDamage(world, shooter, adamantine.getItem(Names.BOW),
                        net.minecraft.init.Items.ARROW, 0),
                "Adamantine bow and vanilla arrow");
        checked++;

        final double goldBowAdamantineArrow = fireBowDamage(world, shooter,
                gold.getItem(Names.BOW), adamantine.getItem(Names.ARROW), 0);
        final double adamantineBowGoldArrow = fireBowDamage(world, shooter,
                adamantine.getItem(Names.BOW), gold.getItem(Names.ARROW), 0);
        requireDamage(gold.getBaseAttackDamage() + adamantine.getBaseAttackDamage(),
                goldBowAdamantineArrow, "gold bow and Adamantine arrow");
        requireDamage(goldBowAdamantineArrow, adamantineBowGoldArrow,
                "swapped bow and arrow materials");
        checked += 2;

        final double goldCrossbowAdamantineBolt = fireCrossbowDamage(world, shooter,
                gold.getItem(Names.CROSSBOW), adamantine.getItem(Names.BOLT), 0);
        final double adamantineCrossbowGoldBolt = fireCrossbowDamage(world, shooter,
                adamantine.getItem(Names.CROSSBOW), gold.getItem(Names.BOLT), 0);
        requireDamage(gold.getBaseAttackDamage() + adamantine.getBaseAttackDamage(),
                goldCrossbowAdamantineBolt, "gold crossbow and Adamantine bolt");
        requireDamage(goldCrossbowAdamantineBolt, adamantineCrossbowGoldBolt,
                "swapped crossbow and bolt materials");
        checked += 2;

        final int powerLevel = 2;
        final double powerBonus = powerLevel * 0.5D + 0.5D;
        requireDamage(steel.getBaseAttackDamage() * 2.0D + powerBonus,
                fireBowDamage(world, shooter, steel.getItem(Names.BOW),
                        steel.getItem(Names.ARROW), powerLevel),
                "Power-enchanted steel bow and arrow");
        requireDamage(steel.getBaseAttackDamage() * 2.0D + powerBonus,
                fireCrossbowDamage(world, shooter, steel.getItem(Names.CROSSBOW),
                        steel.getItem(Names.BOLT), powerLevel),
                "Power-enchanted steel crossbow and bolt");
        checked += 2;

        final ItemBolt neutralBolt = new ItemBolt();
        requireDamage(1.0D + adamantine.getBaseAttackDamage(),
                fireCrossbowDamage(world, shooter, adamantine.getItem(Names.CROSSBOW),
                        neutralBolt, 0),
                "Adamantine crossbow and neutral third-party bolt");
        checked++;

        final ItemArrow thirdPartyArrow = new ItemArrow() {
            @Override
            public EntityArrow createArrow(final World arrowWorld, final ItemStack stack,
                    final net.minecraft.entity.EntityLivingBase arrowShooter) {
                final EntityArrow projectile = super.createArrow(arrowWorld, stack, arrowShooter);
                projectile.setDamage(4.25D);
                return projectile;
            }
        };
        requireDamage(4.25D - 1.0D + adamantine.getBaseAttackDamage(),
                fireBowDamage(world, shooter, adamantine.getItem(Names.BOW),
                        thirdPartyArrow, 0),
                "Adamantine bow and third-party arrow");
        checked++;
        require(checked == 119, "Checked " + checked + " ranged damage cases, expected 119");
        return checked;
    }

    private static double fireBowDamage(final World world, final EntityPlayer shooter,
            final Item bow, final Item arrow, final int powerLevel) {
        final ItemStack bowStack = new ItemStack(bow);
        if (powerLevel > 0) {
            bowStack.addEnchantment(Enchantments.POWER, powerLevel);
        }
        final ItemStack ammunition = new ItemStack(arrow, 2);
        shooter.setHeldItem(EnumHand.OFF_HAND, ammunition);
        final int before = countArrows(world);
        EntityArrow projectile = null;
        try {
            bow.onPlayerStoppedUsing(bowStack, world, shooter,
                    bow.getMaxItemUseDuration(bowStack) - 20);
            require(countArrows(world) == before + 1,
                    bow.getRegistryName() + " did not create a server-side arrow");
            projectile = findLiveArrow(world);
            require(projectile != null, "Spawned arrow could not be located");
            require(ammunition.getCount() == 1,
                    bow.getRegistryName() + " consumed an unexpected number of arrows");
            require(bowStack.getItemDamage() == 1,
                    bow.getRegistryName() + " did not lose one durability after firing");
            return projectile.getDamage();
        } finally {
            if (projectile != null) {
                projectile.setDead();
            }
            shooter.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    private static double fireCrossbowDamage(final World world, final EntityPlayer shooter,
            final Item crossbow, final Item bolt, final int powerLevel) {
        final ItemStack crossbowStack = new ItemStack(crossbow);
        if (powerLevel > 0) {
            crossbowStack.addEnchantment(Enchantments.POWER, powerLevel);
        }
        final ItemStack ammunition = new ItemStack(bolt, 2);
        shooter.setHeldItem(EnumHand.OFF_HAND, ammunition);
        final int before = countBolts(world);
        EntityCustomBolt projectile = null;
        try {
            crossbow.onPlayerStoppedUsing(crossbowStack, world, shooter,
                    crossbow.getMaxItemUseDuration(crossbowStack) - 20);
            require(countBolts(world) == before + 1,
                    crossbow.getRegistryName() + " did not create a server-side bolt");
            projectile = findLiveBolt(world);
            require(projectile != null, "Spawned bolt could not be located");
            require(ammunition.getCount() == 1,
                    crossbow.getRegistryName() + " consumed an unexpected number of bolts");
            require(crossbowStack.getItemDamage() == 1,
                    crossbow.getRegistryName() + " did not lose one durability after firing");
            return projectile.getDamage();
        } finally {
            if (projectile != null) {
                projectile.setDead();
            }
            shooter.setHeldItem(EnumHand.OFF_HAND, ItemStack.EMPTY);
        }
    }

    private static int countArrows(final World world) {
        int count = 0;
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityArrow && !(entity instanceof EntityCustomBolt)
                    && !entity.isDead) {
                count++;
            }
        }
        return count;
    }

    private static EntityArrow findLiveArrow(final World world) {
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityArrow && !(entity instanceof EntityCustomBolt)
                    && !entity.isDead) {
                return (EntityArrow) entity;
            }
        }
        return null;
    }

    private static void requireDamage(final double expected, final double actual,
            final String description) {
        require(Math.abs(expected - actual) < 0.000001D,
                description + " has projectile base damage " + actual + ", expected " + expected);
    }

    private static double createArrowDamage(final World world, final EntityPlayer shooter,
            final MMDMaterial material) {
        final Item item = material.getItem(Names.ARROW);
        require(item instanceof ItemArrow,
                material.getName() + " arrow is not an ItemArrow");
        final EntityArrow projectile = ((ItemArrow) item).createArrow(
                world, new ItemStack(item), shooter);
        final double damage = projectile.getDamage();
        projectile.setDead();
        return damage;
    }

    private static double createBoltDamage(final World world, final EntityPlayer shooter,
            final MMDMaterial material) {
        final Item item = material.getItem(Names.BOLT);
        require(item instanceof ItemBolt,
                material.getName() + " bolt is not an ItemBolt");
        final EntityCustomBolt projectile = ((ItemBolt) item).createBolt(
                world, new ItemStack(item), shooter);
        final double damage = projectile.getDamage();
        projectile.setDead();
        return damage;
    }

    private static int countBolts(final World world) {
        int count = 0;
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityCustomBolt && !entity.isDead) {
                count++;
            }
        }
        return count;
    }

    private static EntityCustomBolt findLiveBolt(final World world) {
        for (final Entity entity : world.loadedEntityList) {
            if (entity instanceof EntityCustomBolt && !entity.isDead) {
                return (EntityCustomBolt) entity;
            }
        }
        return null;
    }

    private static int verifyArmorEffects(final MinecraftServer server) {
        final WorldServer world = server.getWorld(0);
        int checked = 0;

        for (int pieces = 1; pieces <= 4; pieces++) {
            final EntityPlayer player = armorTestPlayer(server, world,
                    "StarSteel" + pieces);
            final ItemStack trigger = equipArmor(player, MaterialNames.STARSTEEL, pieces);
            Materials.getMaterialByName(MaterialNames.STARSTEEL).applyEffect(trigger, player);
            requireEffect(player, MobEffects.JUMP_BOOST, pieces - 1,
                    "Star-Steel " + pieces + "-piece jump boost");
            if (pieces == 1) {
                requireNoEffect(player, MobEffects.SPEED,
                        "Star-Steel one-piece speed boost");
            } else {
                requireEffect(player, MobEffects.SPEED, pieces - 2,
                        "Star-Steel " + pieces + "-piece speed boost");
            }
            checked += 2;
        }

        for (int pieces = 1; pieces <= 4; pieces++) {
            final EntityPlayer player = armorTestPlayer(server, world,
                    "Adamantine" + pieces);
            final ItemStack trigger = equipArmor(player, MaterialNames.ADAMANTINE, pieces);
            Materials.getMaterialByName(MaterialNames.ADAMANTINE).applyEffect(trigger, player);
            if (pieces == 1) {
                requireNoEffect(player, MobEffects.RESISTANCE,
                        "Adamantine one-piece resistance");
            } else {
                requireEffect(player, MobEffects.RESISTANCE, pieces == 4 ? 1 : 0,
                        "Adamantine " + pieces + "-piece resistance");
            }
            checked++;
        }

        for (int pieces = 1; pieces <= 4; pieces++) {
            final EntityPlayer player = armorTestPlayer(server, world,
                    "Lead" + pieces);
            final ItemStack trigger = equipArmor(player, MaterialNames.LEAD, pieces);
            Materials.getMaterialByName(MaterialNames.LEAD).applyEffect(trigger, player);
            if (pieces == 1) {
                requireNoEffect(player, MobEffects.SLOWNESS,
                        "Lead one-piece slowness");
            } else {
                requireEffect(player, MobEffects.SLOWNESS, pieces == 4 ? 1 : 0,
                        "Lead " + pieces + "-piece slowness");
            }
            checked++;
        }

        final EntityPlayer coldIronPartial = armorTestPlayer(server, world,
                "ColdIronPartial");
        final ItemStack coldIronPartialTrigger = equipArmor(
                coldIronPartial, MaterialNames.COLDIRON, 3);
        Materials.getMaterialByName(MaterialNames.COLDIRON)
                .applyEffect(coldIronPartialTrigger, coldIronPartial);
        requireNoEffect(coldIronPartial, MobEffects.FIRE_RESISTANCE,
                "Cold-Iron partial-suit fire resistance");
        checked++;

        final EntityPlayer coldIronFull = armorTestPlayer(server, world,
                "ColdIronFull");
        final ItemStack coldIronFullTrigger = equipArmor(
                coldIronFull, MaterialNames.COLDIRON, 4);
        Materials.getMaterialByName(MaterialNames.COLDIRON)
                .applyEffect(coldIronFullTrigger, coldIronFull);
        requireEffect(coldIronFull, MobEffects.FIRE_RESISTANCE, 0,
                "Cold-Iron full-suit fire resistance");
        checked++;

        final EntityPlayer mithrilPartial = armorTestPlayer(server, world,
                "MithrilPartial");
        final ItemStack mithrilPartialTrigger = equipArmor(
                mithrilPartial, MaterialNames.MITHRIL, 3);
        mithrilPartial.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
        Materials.getMaterialByName(MaterialNames.MITHRIL)
                .applyEffect(mithrilPartialTrigger, mithrilPartial);
        requireEffect(mithrilPartial, MobEffects.POISON, 0,
                "Mithril partial-suit harmful-effect preservation");
        checked++;

        final EntityPlayer mithrilFull = armorTestPlayer(server, world,
                "MithrilFull");
        final ItemStack mithrilFullTrigger = equipArmor(
                mithrilFull, MaterialNames.MITHRIL, 4);
        mithrilFull.addPotionEffect(new PotionEffect(MobEffects.POISON, 200, 0));
        Materials.getMaterialByName(MaterialNames.MITHRIL)
                .applyEffect(mithrilFullTrigger, mithrilFull);
        requireNoEffect(mithrilFull, MobEffects.POISON,
                "Mithril full-suit harmful-effect removal");
        checked++;

        final BlockPos waterFeet = new BlockPos(24, 80, 24);
        final BlockPos waterHead = waterFeet.up();
        final net.minecraft.block.state.IBlockState originalFeet = world.getBlockState(waterFeet);
        final net.minecraft.block.state.IBlockState originalHead = world.getBlockState(waterHead);
        try {
            world.setBlockState(waterFeet, Blocks.FLOWING_WATER.getDefaultState(), 2);
            world.setBlockState(waterHead, Blocks.FLOWING_WATER.getDefaultState(), 2);

            final EntityPlayer aquariumPartial = armorTestPlayer(server, world,
                    "AquariumPartial");
            aquariumPartial.setPosition(24.5D, 80.0D, 24.5D);
            aquariumPartial.handleWaterMovement();
            final ItemStack aquariumPartialTrigger = equipArmor(
                    aquariumPartial, MaterialNames.AQUARIUM, 3);
            Materials.getMaterialByName(MaterialNames.AQUARIUM)
                    .applyEffect(aquariumPartialTrigger, aquariumPartial);
            requireNoEffect(aquariumPartial, MobEffects.WATER_BREATHING,
                    "Aquarium partial-suit water breathing");
            checked++;

            final EntityPlayer aquariumFull = armorTestPlayer(server, world,
                    "AquariumFull");
            aquariumFull.setPosition(24.5D, 80.0D, 24.5D);
            aquariumFull.handleWaterMovement();
            final ItemStack aquariumFullTrigger = equipArmor(
                    aquariumFull, MaterialNames.AQUARIUM, 4);
            aquariumFull.addPotionEffect(new PotionEffect(MobEffects.MINING_FATIGUE, 200, 0));
            Materials.getMaterialByName(MaterialNames.AQUARIUM)
                    .applyEffect(aquariumFullTrigger, aquariumFull);
            requireEffect(aquariumFull, MobEffects.WATER_BREATHING, 0,
                    "Aquarium full-suit water breathing");
            requireEffect(aquariumFull, MobEffects.RESISTANCE, 0,
                    "Aquarium full-suit resistance");
            requireNoEffect(aquariumFull, MobEffects.MINING_FATIGUE,
                    "Aquarium full-suit mining-fatigue removal");
            checked += 3;
        } finally {
            world.setBlockState(waterFeet, originalFeet, 2);
            world.setBlockState(waterHead, originalHead, 2);
        }

        final EntityPlayer aquariumDry = armorTestPlayer(server, world,
                "AquariumDry");
        aquariumDry.setPosition(24.5D, 82.0D, 24.5D);
        final ItemStack aquariumDryTrigger = equipArmor(
                aquariumDry, MaterialNames.AQUARIUM, 4);
        Materials.getMaterialByName(MaterialNames.AQUARIUM)
                .applyEffect(aquariumDryTrigger, aquariumDry);
        requireNoEffect(aquariumDry, MobEffects.WATER_BREATHING,
                "Aquarium dry full-suit water breathing");
        checked++;

        require(!new ColdIronProperty().hasEffect(
                        new ItemStack(net.minecraft.init.Items.DIAMOND), coldIronFull),
                "Cold-Iron property accepts an unrelated item");
        checked++;

        final EntityPlayer starSteelContract = armorTestPlayer(server, world,
                "StarSteelContract");
        final ItemStack starSteelContractTrigger = equipArmor(
                starSteelContract, MaterialNames.STARSTEEL, 1);
        require(new StarSteelProperty().hasEffect(
                        starSteelContractTrigger, starSteelContract),
                "Star-Steel property rejected its own equipped armor");
        checked++;

        final EntityPlayer starSteelTickPlayer = armorTestPlayer(server, world,
                "StarSteelArmorTick");
        final ItemStack starSteelTickTrigger = equipArmor(
                starSteelTickPlayer, MaterialNames.STARSTEEL, 4);
        if (world.getTotalWorldTime() == 0L) {
            world.getWorldInfo().setWorldTotalTime(1L);
        }
        starSteelTickTrigger.getItem().onArmorTick(
                world, starSteelTickPlayer, starSteelTickTrigger);
        starSteelTickTrigger.getItem().onArmorTick(
                world, starSteelTickPlayer, starSteelTickTrigger);
        requireEffect(starSteelTickPlayer, MobEffects.JUMP_BOOST, 3,
                "Star-Steel armor-tick jump boost");
        requireEffect(starSteelTickPlayer, MobEffects.SPEED, 2,
                "Star-Steel armor-tick speed boost");
        checked += 2;

        final BlockPos tickWaterFeet = new BlockPos(28, 80, 28);
        final BlockPos tickWaterHead = tickWaterFeet.up();
        final net.minecraft.block.state.IBlockState originalTickFeet =
                world.getBlockState(tickWaterFeet);
        final net.minecraft.block.state.IBlockState originalTickHead =
                world.getBlockState(tickWaterHead);
        try {
            world.setBlockState(tickWaterFeet, Blocks.WATER.getDefaultState(), 2);
            world.setBlockState(tickWaterHead, Blocks.WATER.getDefaultState(), 2);
            final EntityPlayer aquariumTickPlayer = armorTestPlayer(server, world,
                    "AquariumArmorTick");
            aquariumTickPlayer.setPosition(28.5D, 80.0D, 28.5D);
            aquariumTickPlayer.handleWaterMovement();
            final ItemStack aquariumTickTrigger = equipArmor(
                    aquariumTickPlayer, MaterialNames.AQUARIUM, 4);
            aquariumTickTrigger.getItem().onArmorTick(
                    world, aquariumTickPlayer, aquariumTickTrigger);
            aquariumTickTrigger.getItem().onArmorTick(
                    world, aquariumTickPlayer, aquariumTickTrigger);
            requireEffect(aquariumTickPlayer, MobEffects.WATER_BREATHING, 0,
                    "Aquarium armor-tick water breathing");
            requireEffect(aquariumTickPlayer, MobEffects.RESISTANCE, 0,
                    "Aquarium armor-tick resistance");
            checked += 2;
        } finally {
            world.setBlockState(tickWaterFeet, originalTickFeet, 2);
            world.setBlockState(tickWaterHead, originalTickHead, 2);
        }

        return checked;
    }

    private static EntityPlayer armorTestPlayer(
            final MinecraftServer server,
            final WorldServer world,
            final String name) {
        return new ProbePlayer(world,
                new GameProfile(UUID.nameUUIDFromBytes(name.getBytes()), name));
    }

    private static ItemStack equipArmor(
            final EntityPlayer player,
            final String materialName,
            final int pieces) {
        final MMDMaterial material = Materials.getMaterialByName(materialName);
        final EntityEquipmentSlot[] slots = {
                EntityEquipmentSlot.FEET,
                EntityEquipmentSlot.LEGS,
                EntityEquipmentSlot.CHEST,
                EntityEquipmentSlot.HEAD
        };
        final Names[] forms = {
                Names.BOOTS,
                Names.LEGGINGS,
                Names.CHESTPLATE,
                Names.HELMET
        };
        ItemStack trigger = ItemStack.EMPTY;
        for (int i = 0; i < pieces; i++) {
            final ItemStack armor = new ItemStack(material.getItem(forms[i]));
            player.setItemStackToSlot(slots[i], armor);
            if (trigger.isEmpty()) {
                trigger = armor;
            }
        }
        return trigger;
    }

    private static void requireEffect(
            final EntityPlayer player,
            final net.minecraft.potion.Potion potion,
            final int amplifier,
            final String description) {
        final PotionEffect effect = player.getActivePotionEffect(potion);
        require(effect != null, description + " was not applied");
        require(effect.getAmplifier() == amplifier,
                description + " has amplifier " + effect.getAmplifier()
                        + ", expected " + amplifier);
    }

    private static void requireNoEffect(
            final EntityPlayer player,
            final net.minecraft.potion.Potion potion,
            final String description) {
        require(player.getActivePotionEffect(potion) == null,
                description + " was unexpectedly applied");
    }

    private static final class ProbePlayer extends EntityPlayer {
        private ProbePlayer(final World world, final GameProfile profile) {
            super(world, profile);
        }

        @Override
        public boolean isSpectator() {
            return false;
        }

        @Override
        public boolean isCreative() {
            return false;
        }
    }

    private static void verifyRailRecipe(
            final World world,
            final String recipePath,
            final Item expectedItem,
            final int expectedCount,
            final Item... grid) {
        require(grid.length == 9, "Rail recipe test grid must contain nine slots");
        final InventoryCrafting crafting = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(final EntityPlayer playerIn) {
                return false;
            }
        }, 3, 3);
        for (int slot = 0; slot < grid.length; slot++) {
            if (grid[slot] != null) {
                crafting.setInventorySlotContents(slot, new ItemStack(grid[slot]));
            }
        }

        final ResourceLocation expectedRecipe = new ResourceLocation("basemetals", recipePath);
        final IRecipe registeredRecipe = CraftingManager.REGISTRY.getObject(expectedRecipe);
        require(registeredRecipe != null, "Crafting recipe was not registered: " + expectedRecipe);
        require(registeredRecipe.matches(crafting, world),
                "Registered crafting recipe did not match its canonical input: " + expectedRecipe);
        final IRecipe recipe = CraftingManager.findMatchingRecipe(crafting, world);
        require(recipe != null, "No crafting recipe matched " + expectedRecipe);
        require(expectedRecipe.equals(recipe.getRegistryName()),
                "Crafting input for " + expectedRecipe + " matched " + recipe.getRegistryName());

        final ItemStack result = recipe.getCraftingResult(crafting);
        require(!result.isEmpty(), "Crafting recipe " + expectedRecipe + " returned an empty result");
        require(result.getItem() == expectedItem,
                "Crafting recipe " + expectedRecipe + " returned " + result.getItem().getRegistryName()
                        + ", expected " + expectedItem.getRegistryName());
        require(result.getCount() == expectedCount,
                "Crafting recipe " + expectedRecipe + " returned " + result.getCount()
                        + " items, expected " + expectedCount);
    }

    private static int verifyDoorAndTrapdoorRecipe(
            final World world,
            final Item ingredient,
            final String material) {
        requireCreativeRegistryEntry(material + "_door");
        requireCreativeRegistryEntry(material + "_trapdoor");
        verifyCraftingResult(world, ingredient, material + "_door", 3, 2, 3);
        if ("quartz".equals(material)) {
            verifyCraftingResult(world, Item.getItemFromBlock(Blocks.QUARTZ_BLOCK),
                    material + "_trapdoor", 1, 1, 1);
        } else {
            verifyCraftingResult(world, ingredient, material + "_trapdoor", 1, 2, 2);
        }
        return 2;
    }

    private static void requireCreativeRegistryEntry(final String path) {
        final ResourceLocation registryName = new ResourceLocation("basemetals", path);
        final Block block = Block.REGISTRY.getObject(registryName);
        require(block != null && registryName.equals(block.getRegistryName()),
                "Missing registered block " + registryName);
        final Item item = Item.REGISTRY.getObject(registryName);
        require(item != null && registryName.equals(item.getRegistryName()),
                "Missing registered item " + registryName);
        require(item.getCreativeTab() != null,
                "Registered item is absent from creative tabs: " + registryName);
    }

    private static void verifyCraftingResult(
            final World world,
            final Item ingredient,
            final String expectedPath,
            final int expectedCount,
            final int width,
            final int height) {
        final InventoryCrafting crafting = new InventoryCrafting(new Container() {
            @Override
            public boolean canInteractWith(final EntityPlayer playerIn) {
                return false;
            }
        }, 3, 3);
        for (int row = 0; row < height; row++) {
            for (int column = 0; column < width; column++) {
                crafting.setInventorySlotContents(column + row * 3, new ItemStack(ingredient));
            }
        }

        final ResourceLocation expectedRecipe = new ResourceLocation("basemetals", expectedPath);
        final IRecipe registeredRecipe = CraftingManager.REGISTRY.getObject(expectedRecipe);
        require(registeredRecipe != null, "Crafting recipe was not registered: " + expectedRecipe);
        require(registeredRecipe.matches(crafting, world),
                "Registered crafting recipe did not match its canonical input: " + expectedRecipe);
        final IRecipe recipe = CraftingManager.findMatchingRecipe(crafting, world);
        require(recipe != null, "No crafting recipe matched " + expectedRecipe);
        require(expectedRecipe.equals(recipe.getRegistryName()),
                "Crafting input for " + expectedRecipe + " matched " + recipe.getRegistryName());

        final ItemStack result = recipe.getCraftingResult(crafting);
        require(!result.isEmpty(), "Crafting recipe " + expectedRecipe + " returned an empty result");
        require(expectedRecipe.equals(result.getItem().getRegistryName()),
                "Crafting recipe " + expectedRecipe + " returned " + result.getItem().getRegistryName());
        require(result.getCount() == expectedCount,
                "Crafting recipe " + expectedRecipe + " returned " + result.getCount()
                        + " items, expected " + expectedCount);
    }

    private static String requiredProperty(final String name) {
        final String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing system property " + name);
        }
        return value;
    }

    private static String version(final String modid) {
        for (final ModContainer mod : Loader.instance().getModList()) {
            if (modid.equals(mod.getModId())) {
                return mod.getVersion();
            }
        }
        throw new IllegalStateException("Missing loaded mod " + modid);
    }

    private static Item requireItem(final String namespace, final String path) {
        final ResourceLocation registryName = new ResourceLocation(namespace, path);
        final Item item = Item.REGISTRY.getObject(registryName);
        require(item != null && registryName.equals(item.getRegistryName()),
                "Missing registered item " + registryName);
        return item;
    }

    private static void requireRegistered(final Object value, final String registryName) {
        require(value != null, "Missing registry object " + registryName);
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
