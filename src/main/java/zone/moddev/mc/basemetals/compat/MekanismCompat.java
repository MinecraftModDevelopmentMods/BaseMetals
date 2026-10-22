package zone.moddev.mc.basemetals.compat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.Chemical;
import mekanism.api.chemical.ChemicalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

/** The only code-backed optional integration: Mekanism slurries are registry entries, not data-pack entries. */
public final class MekanismCompat {
    public static final List<String> PROCESSING_MATERIALS = List.of(
            "adamantine", "antimony", "bismuth", "coldiron",
            "nickel", "platinum", "starsteel", "zinc");

    private static final DeferredRegister<Chemical> SLURRIES = DeferredRegister.create(
            MekanismAPI.CHEMICAL_REGISTRY_NAME, BaseMetals.MOD_ID);
    private static final Map<String, DeferredHolder<Chemical, Chemical>> DIRTY = new LinkedHashMap<>();
    private static final Map<String, DeferredHolder<Chemical, Chemical>> CLEAN = new LinkedHashMap<>();

    static {
        for (String name : PROCESSING_MATERIALS) {
            int colour = MaterialCatalogue.get(name).colour();
            ResourceLocation oreTag = ResourceLocation.fromNamespaceAndPath("c", "ores/" + name);
            DIRTY.put(name, SLURRIES.register("dirty_" + name,
                    () -> new Chemical(ChemicalBuilder.dirtySlurry().ore(oreTag).tint(colour))));
            CLEAN.put(name, SLURRIES.register("clean_" + name,
                    () -> new Chemical(ChemicalBuilder.cleanSlurry().tint(colour).ore(oreTag))));
        }
    }

    private MekanismCompat() {}

    public static void register(IEventBus modBus) {
        SLURRIES.register(modBus);
    }
}
