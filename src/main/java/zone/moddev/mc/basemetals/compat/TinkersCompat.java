package zone.moddev.mc.basemetals.compat;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.event.AddReloadListenerEvent;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;
import slimeknights.tconstruct.library.materials.stats.UpdateMaterialStatsPacket;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.BaseMetalsConfig;
import zone.moddev.mc.basemetals.config.ContentMode;

/** Filters bundled material stats after Tinkers loads them and before login sync. */
public final class TinkersCompat {
    private TinkersCompat() {}

    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener((ResourceManagerReloadListener) TinkersCompat::filterBundledStats);
    }

    private static void filterBundledStats(ResourceManager resources) {
        ContentMode mode = BaseMetalsConfig.activeMode();
        if (mode == ContentMode.HIGH_FANTASY) return;

        var registry = MaterialRegistry.getInstance();
        Map<MaterialId, Collection<IMaterialStats>> filtered = new LinkedHashMap<>();
        for (var material : registry.getAllMaterials()) {
            MaterialId id = material.getIdentifier();
            Collection<IMaterialStats> stats = registry.getAllStats(id);
            if (BaseMetals.MOD_ID.equals(id.getNamespace()) && !hasExplicitOverride(resources, id)) {
                stats = stats.stream().filter(stat -> TinkersContentPolicy.allows(mode,
                        id.getPath(), stat.getIdentifier().toString())).toList();
            }
            filtered.put(id, stats);
        }

        // This public entry point updates the same snapshot Tinkers sends to clients.
        MaterialRegistry.updateMaterialStatsFromServer(new UpdateMaterialStatsPacket(filtered));
    }

    private static boolean hasExplicitOverride(ResourceManager resources, MaterialId material) {
        ResourceLocation file = new ResourceLocation(material.getNamespace(),
                "tinkering/materials/stats/" + material.getPath() + ".json");
        String bundledPack = net.minecraftforge.resource.ResourcePackLoader.getPackFor(BaseMetals.MOD_ID)
                .orElseThrow(() -> new IllegalStateException("Base Metals' data pack is missing"))
                .packId();
        return resources.getResourceStack(file).stream()
                .anyMatch(resource -> !bundledPack.equals(resource.sourcePackId()));
    }
}
