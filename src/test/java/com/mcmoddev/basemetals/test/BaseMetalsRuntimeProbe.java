package com.mcmoddev.basemetals.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
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
        acceptedMinecraftVersions = "[1.12.2]")
public final class BaseMetalsRuntimeProbe {
    public static final String MODID = "basemetals_runtime_probe";

    @EventHandler
    public void serverStarted(final FMLServerStartedEvent event) throws IOException {
        final String expectedOreSpawn = requiredProperty("basemetals.probe.expectedOrespawn");
        final File marker = new File(requiredProperty("basemetals.probe.marker"));
        final Properties result = new Properties();

        require(Loader.isModLoaded("basemetals"), "Base Metals did not load");
        require(Loader.isModLoaded("mmdlib"), "MMDLib did not load");
        require(Loader.isModLoaded("orespawn"), "OreSpawn did not load");
        requireRegistered(Block.REGISTRY.getObject(new ResourceLocation("basemetals", "copper_ore")),
                "basemetals:copper_ore");
        requireRegistered(Block.REGISTRY.getObject(new ResourceLocation("basemetals", "adamantine_ore")),
                "basemetals:adamantine_ore");
        requireRegistered(Item.REGISTRY.getObject(new ResourceLocation("basemetals", "steel_ingot")),
                "basemetals:steel_ingot");

        final String baseMetalsVersion = version("basemetals");
        final String mmdLibVersion = version("mmdlib");
        final String oreSpawnVersion = version("orespawn");
        require("2.5.1.112021".equals(baseMetalsVersion),
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
        result.setProperty("registered", "true");
        marker.getParentFile().mkdirs();
        try (FileOutputStream output = new FileOutputStream(marker)) {
            result.store(output, "Base Metals packaged-runtime qualification");
        }
        FMLCommonHandler.instance().getMinecraftServerInstance().initiateShutdown();
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

    private static void requireRegistered(final Object value, final String registryName) {
        require(value != null, "Missing registry object " + registryName);
    }

    private static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
