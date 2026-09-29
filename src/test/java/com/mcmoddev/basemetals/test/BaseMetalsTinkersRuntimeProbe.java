package com.mcmoddev.basemetals.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.util.BMeConfig;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.oredict.OreDictionary;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.smeltery.MeltingRecipe;

/** Test-only Tinkers/TAIGA compatibility probe. This class never enters the public jar. */
@Mod(
        modid = BaseMetalsTinkersRuntimeProbe.MODID,
        name = "Base Metals Tinkers Runtime Probe",
        version = "1",
        dependencies = "required-after:basemetals;required-after:mmdlib;required-after:orespawn;"
                + "required-after:mantle;required-after:tconstruct",
        acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsTinkersRuntimeProbe {
    public static final String MODID = "basemetals_tinkers_runtime_probe";

    // The published TAIGA 1.3.4 jar retains 1.3.3 in its embedded mod metadata.
    private static final String TAIGA_EMBEDDED_VERSION = "1.12.2-1.3.3";

    private static final List<String> ADAMANT_FORMS = Arrays.asList(
            "oreAdamant", "blockAdamant", "plateAdamant", "doorAdamant",
            "nuggetAdamant", "trapdoorAdamant", "dustAdamant", "gearAdamant",
            "rodAdamant", "barsAdamant", "ingotAdamant", "dustTinyAdamant",
            "dustSmallAdamant");

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent event) throws IOException {
        final boolean expectedTaiga = Boolean.parseBoolean(
                requiredProperty("basemetals.probe.expectedTaiga"));
        final boolean expectedPrismarine = Boolean.parseBoolean(
                requiredProperty("basemetals.probe.expectedPrismarine"));
        final boolean expectedConArm = Boolean.parseBoolean(
                requiredProperty("basemetals.probe.expectedConArm"));
        final ContentMode expectedContentMode = ContentMode.fromSerializedName(
                requiredProperty("basemetals.probe.expectedContentMode"));
        final File marker = new File(requiredProperty("basemetals.probe.marker"));
        final Properties result = new Properties();

        require(Loader.isModLoaded("basemetals"), "Base Metals did not load");
        require(Loader.isModLoaded("mmdlib"), "MMDLib did not load");
        require(Loader.isModLoaded("orespawn"), "OreSpawn did not load");
        require(Loader.isModLoaded("mantle"), "Mantle did not load");
        require(Loader.isModLoaded("tconstruct"), "Tinkers Construct did not load");
        require(BMeConfig.getActiveContentMode() == expectedContentMode,
                "Content mode did not match expected mode " + expectedContentMode
                        + "; actual=" + BMeConfig.getActiveContentMode());
        require(Loader.isModLoaded("taiga") == expectedTaiga,
                "TAIGA loaded state did not match expected state " + expectedTaiga);
        require(Loader.isModLoaded("conarm") == expectedConArm,
                "Construct's Armory loaded state did not match expected state " + expectedConArm);
        verifyPrismarineConfiguration(expectedPrismarine);
        verifyCoreContentPolicy(expectedContentMode);

        final Item adamantineIngot = requireItem("basemetals", "adamantine_ingot");
        verifyBaseAliases("Adamantine");
        verifyBaseAliases("Adamantite");
        verifyBaseAliases("Adamantium");

        final Fluid adamantineFluid = requireFluid("adamantine");
        if (expectedContentMode == ContentMode.REALISM) {
            require(meltingFluids(adamantineIngot).isEmpty(),
                    "Realism exposes Adamantine Tinkers melting outputs: "
                            + meltingFluids(adamantineIngot));
        } else {
            require(meltingFluids(adamantineIngot).equals(singleton(adamantineFluid.getName())),
                    "Base Metals Adamantine ingot has ambiguous Tinkers melting outputs: "
                            + meltingFluids(adamantineIngot));
        }
        verifyMaterialStats(expectedContentMode);
        verifyArmorMaterialStats(expectedContentMode, expectedConArm);

        if (expectedTaiga) {
            require(TAIGA_EMBEDDED_VERSION.equals(version("taiga")),
                    "Unexpected TAIGA version " + version("taiga"));
            verifyNoBaseMetalsAdamantAliases();
            verifyTaigaAliases();

            final Item taigaAdamantIngot = requireItem("taiga", "adamant_ingot");
            final Fluid taigaAdamantFluid = requireFluid("adamant_fluid");
            require(adamantineFluid != taigaAdamantFluid,
                    "Base Metals Adamantine and TAIGA Adamant share one fluid instance");
            require(meltingFluids(taigaAdamantIngot).equals(singleton(taigaAdamantFluid.getName())),
                    "TAIGA Adamant ingot has ambiguous Tinkers melting outputs: "
                            + meltingFluids(taigaAdamantIngot));
        } else {
            verifyBaseAliases("Adamant");
            require(FluidRegistry.getFluid("adamant_fluid") == null,
                    "TAIGA Adamant fluid exists when TAIGA is absent");
        }

        result.setProperty("phase", System.getProperty("basemetals.probe.phase", "unknown"));
        result.setProperty("taiga", Boolean.toString(expectedTaiga));
        result.setProperty("conarm", Boolean.toString(expectedConArm));
        result.setProperty("prismarine", Boolean.toString(expectedPrismarine));
        result.setProperty("contentMode", expectedContentMode.serializedName());
        if (expectedTaiga) {
            result.setProperty("taigaEmbeddedVersion", version("taiga"));
        }
        result.setProperty("adamantineMeltingOutputs",
                meltingFluids(adamantineIngot).toString());
        result.setProperty("registered", "true");
        marker.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(marker)) {
            result.store(output, "Base Metals Tinkers/TAIGA qualification");
        }
        FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
    }

    private static void verifyArmorMaterialStats(final ContentMode mode,
            final boolean expectedConArm) {
        if (!expectedConArm) {
            return;
        }
        final String allowed = mode == ContentMode.REALISM ? "nickel" : "adamantine";
        requireStats(allowed, allowed, "core", "plates", "trim");
        if (mode != ContentMode.HIGH_FANTASY) {
            // Use a Base Metals-specific material here. Tinkers itself owns a
            // pre-existing Lead material and its third-party stats remain
            // authoritative even when Base Metals recipes are restricted.
            final Material bismuth = TinkerRegistry.getMaterial("bismuth");
            require(bismuth != Material.UNKNOWN,
                    "Missing restricted-mode Tinkers material Bismuth");
            require(!bismuth.hasStats("core") && !bismuth.hasStats("plates")
                            && !bismuth.hasStats("trim"),
                    "Restricted mode exposed Bismuth Construct's Armory stats");
        }
    }

    private static void verifyCoreContentPolicy(final ContentMode mode) {
        // Registry identities and creative recovery remain stable in every mode.
        for (final String path : Arrays.asList("adamantine_bow", "adamantine_pickaxe",
                "pewter_chestplate", "bronze_pickaxe", "mercury_ingot")) {
            final Item item = requireItem("basemetals", path);
            require(item.getCreativeTab() != null,
                    "Restricted item is unavailable for creative recovery: " + path);
        }

        final boolean high = mode == ContentMode.HIGH_FANTASY;
        final boolean realism = mode == ContentMode.REALISM;
        verifyRecipeOutput("adamantine_bow", high);
        verifyRecipeOutput("adamantine_pickaxe", !realism);
        verifyRecipeOutput("pewter_chestplate", high);
        verifyRecipeOutput("bronze_pickaxe", true);

        final ItemStack adamantinePowder = new ItemStack(
                requireItem("basemetals", "adamantine_powder"));
        final ItemStack adamantineIngot = new ItemStack(
                requireItem("basemetals", "adamantine_ingot"));
        final ItemStack smelted = FurnaceRecipes.instance().getSmeltingResult(adamantinePowder);
        require(smelted.isEmpty() == realism,
                "Adamantine furnace acquisition does not match " + mode + ": " + smelted);
        require((CrusherRecipeRegistry.getRecipeForInputItem(adamantineIngot) == null) == realism,
                "Adamantine crusher acquisition does not match " + mode);

        final ItemStack mercuryPowder = new ItemStack(
                requireItem("basemetals", "mercury_powder"));
        final ItemStack mercurySmelted = FurnaceRecipes.instance().getSmeltingResult(mercuryPowder);
        require(mercurySmelted.isEmpty() == realism,
                "Mercury reagent ingot acquisition does not match " + mode + ": "
                        + mercurySmelted);
    }

    private static void verifyRecipeOutput(final String path, final boolean expectedAllowed) {
        final ResourceLocation id = new ResourceLocation("basemetals", path);
        final IRecipe recipe = CraftingManager.REGISTRY.getObject(id);
        require(recipe != null, "Missing stable recipe identity " + id);
        require(!recipe.getRecipeOutput().isEmpty() == expectedAllowed,
                "Recipe policy for " + id + " does not match expected allowed="
                        + expectedAllowed + "; output=" + recipe.getRecipeOutput());
        require(recipe.isDynamic() != expectedAllowed,
                "Recipe-book visibility for " + id + " does not match expected allowed="
                        + expectedAllowed);
    }

    private static void verifyMaterialStats(final ContentMode mode) {
        if (mode == ContentMode.REALISM) {
            require(TinkerRegistry.getMaterial("adamantine") == Material.UNKNOWN,
                    "Realism exposed mythical Adamantine as a Tinkers material");
            verifyRestrictedStats("nickel", "Nickel");
            return;
        }

        if (mode == ContentMode.HIGH_FANTASY) {
            requireStats("adamantine", "Adamantine", "head", "handle", "extra", "bow",
                    "bowstring", "shaft", "fletching");
        } else {
            verifyRestrictedStats("adamantine", "Adamantine");
        }
    }

    private static void verifyRestrictedStats(final String identifier, final String displayName) {
        final Material material = TinkerRegistry.getMaterial(identifier);
        require(material != Material.UNKNOWN, "Missing Tinkers material " + identifier);
        requireStats(identifier, displayName, "head", "handle", "extra", "shaft", "fletching");
        require(!material.hasStats("bow"), displayName + " exposes restricted bow stats");
        require(!material.hasStats("bowstring"),
                displayName + " exposes restricted bowstring stats");
    }

    private static void requireStats(final String materialIdentifier, final String displayName,
            final String... identifiers) {
        final Material material = TinkerRegistry.getMaterial(materialIdentifier);
        require(material != Material.UNKNOWN, "Missing Tinkers material " + materialIdentifier);
        for (final String identifier : identifiers) {
            require(material.hasStats(identifier),
                    displayName + " is missing Tinkers stats " + identifier);
        }
    }

    private static void verifyPrismarineConfiguration(final boolean expectedPrismarine) {
        final Fluid prismarine = FluidRegistry.getFluid("prismarine");
        if (expectedPrismarine) {
            require(prismarine != null, "Prismarine fluid is missing when enabled");
            return;
        }

        require(prismarine == null, "Prismarine fluid exists when Prismarine additions are disabled");
        for (final AlloyRecipe recipe : TinkerRegistry.getAlloys()) {
            if (recipe.getResult() != null && recipe.getResult().getFluid() != null) {
                require(!"aquarium".equals(recipe.getResult().getFluid().getName()),
                        "Aquarium alloy was registered without molten Prismarine");
            }
        }
    }

    private static void verifyBaseAliases(final String suffix) {
        requireOreEntry("ingot" + suffix, "basemetals", "adamantine_ingot");
        requireOreEntry("dust" + suffix, "basemetals", "adamantine_powder");
        requireOreEntry("nugget" + suffix, "basemetals", "adamantine_nugget");
        requireOreEntry("block" + suffix, "basemetals", "adamantine_block");
    }

    private static void verifyNoBaseMetalsAdamantAliases() {
        for (final String oreName : ADAMANT_FORMS) {
            for (final ItemStack stack : OreDictionary.getOres(oreName, false)) {
                final ResourceLocation registryName = stack.getItem().getRegistryName();
                require(registryName == null || !"basemetals".equals(registryName.getNamespace()),
                        "TAIGA-owned Ore Dictionary name " + oreName
                                + " contains Base Metals item " + registryName);
            }
        }
    }

    private static void verifyTaigaAliases() {
        requireOreEntry("ingotAdamant", "taiga", "adamant_ingot");
        requireOreEntry("dustAdamant", "taiga", "adamant_dust");
        requireOreEntry("nuggetAdamant", "taiga", "adamant_nugget");
        requireOreEntry("blockAdamant", "taiga", "adamant_block");
    }

    private static void requireOreEntry(
            final String oreName,
            final String namespace,
            final String path) {
        final ResourceLocation expected = new ResourceLocation(namespace, path);
        final List<ResourceLocation> actual = new ArrayList<>();
        boolean found = false;
        for (final ItemStack stack : OreDictionary.getOres(oreName, false)) {
            final ResourceLocation registryName = stack.getItem().getRegistryName();
            actual.add(registryName);
            if (expected.equals(registryName)) {
                found = true;
            }
        }
        require(found, "Ore Dictionary name " + oreName + " is missing " + expected
                + "; entries=" + actual);
    }

    private static Set<String> meltingFluids(final Item item) {
        final ItemStack stack = new ItemStack(item);
        final Set<String> fluids = new LinkedHashSet<>();
        for (final MeltingRecipe recipe : TinkerRegistry.getAllMeltingRecipies()) {
            if (recipe.matches(stack)) {
                fluids.add(recipe.getResult().getFluid().getName());
            }
        }
        return fluids;
    }

    private static Set<String> singleton(final String value) {
        final Set<String> values = new LinkedHashSet<>();
        values.add(value);
        return values;
    }

    private static Item requireItem(final String namespace, final String path) {
        final ResourceLocation registryName = new ResourceLocation(namespace, path);
        final Item item = Item.REGISTRY.getObject(registryName);
        require(item != null && registryName.equals(item.getRegistryName()),
                "Missing item " + registryName);
        return item;
    }

    private static Fluid requireFluid(final String name) {
        final Fluid fluid = FluidRegistry.getFluid(name);
        require(fluid != null, "Missing fluid " + name);
        return fluid;
    }

    private static String version(final String modid) {
        return Loader.instance().getIndexedModList().get(modid).getVersion();
    }

    private static String requiredProperty(final String name) {
        final String value = System.getProperty(name);
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException("Missing system property " + name);
        }
        return value;
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
