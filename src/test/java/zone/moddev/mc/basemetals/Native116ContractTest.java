package zone.moddev.mc.basemetals;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.jupiter.api.Test;

class Native116ContractTest {
    @Test
    void targetsForge36InEveryModDescriptor() throws Exception {
        for (String sourceSet : new String[] {"main", "integrationTest", "clientIntegrationTest"}) {
            String metadata = source("src/" + sourceSet + "/resources/META-INF/mods.toml");
            assertTrue(metadata.contains("loaderVersion=\"[36,)\""), sourceSet);
        }
        String metadata = source("src/main/resources/META-INF/mods.toml");
        assertTrue(metadata.contains("versionRange=\"[36,)\""));
        assertTrue(metadata.contains("versionRange=\"[1.16.5]\""));
    }

    @Test
    void registersBothProjectileTypesAndNativeRenderLayers() throws Exception {
        String setup = source("src/main/java/zone/moddev/mc/basemetals/client/ClientSetup.java");
        assertTrue(setup.contains("ModEntities.CUSTOM_ARROW.get()"));
        assertTrue(setup.contains("ModEntities.CUSTOM_BOLT.get()"));
        assertTrue(setup.contains("RenderTypeLookup.setRenderLayer"));
        assertTrue(setup.contains("RenderType.cutoutMipped()"));
        assertTrue(setup.contains("RenderType.translucent()"));
        assertFalse(setup.contains("registerEntityRenderingHandler(MaterialProjectile.class"));
        assertFalse(source("src/main/java/zone/moddev/mc/basemetals/content/MoltenFluid.java")
                .contains("BlockRenderLayer"));
        assertFalse(source("src/main/java/zone/moddev/mc/basemetals/content/ModContent.java")
                .contains("BlockRenderLayer"));
    }

    @Test
    void pinsThePublishedOreSpawnRelease() throws Exception {
        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(Paths.get("gradle.properties"))) {
            properties.load(input);
        }

        assertEquals("release", properties.getProperty("orespawn_dependency_mode"));
        assertEquals("4.1.0.116051", properties.getProperty("orespawn_version"));
        assertEquals("245586", properties.getProperty("orespawn_curse_project_id"));
        assertEquals("9080646", properties.getProperty("orespawn_curse_file_id"));
        assertEquals("CDF59D83F191C5FBCD8921228DB577066A63A8FEB25CA79DB4EAEEA32D32DB04",
                properties.getProperty("orespawn_sha256"));
        assertTrue(source("build.gradle").contains("curse.maven:${orespawnModule}:${orespawn_curse_file_id}"));
        assertTrue(source("build.gradle").contains("zone.moddev.mc.orespawn:OreSpawn:${orespawn_version}"));
    }

    @Test
    void keepsTheLocalCandidatePublicationGuard() throws Exception {
        assertTrue(source("gradle/stage-orespawn-release.sh")
                .contains("Publication is blocked"));
        String artifacts = source("gradle/release/artifacts.gradle");
        assertTrue(artifacts.contains("PublishToMavenLocal"));
        assertTrue(artifacts.contains("Publication is blocked until the public OreSpawn release is pinned"));
    }

    @Test
    void samplesOresWithANonzeroFixedWorldSeed() throws Exception {
        String packaged = source("gradle/verification/packaged-forge.gradle");
        assertFalse(packaged.contains("level-seed=0"));
        assertEquals(2, packaged.split("level-seed=8675309", -1).length - 1);
        assertTrue(source("gradle/verification/development.gradle").contains("level-seed=8675309"));

        String probe = source("src/integrationTest/java/zone/moddev/mc/basemetals/testmod/OrePlacementChecks.java");
        assertTrue(probe.contains("server.overworld().getSeed()"));
        assertTrue(probe.contains("worldSeed != SAMPLE_WORLD_SEED"));
    }

    private static String source(String path) throws Exception {
        return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
    }
}
