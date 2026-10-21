package zone.moddev.mc.basemetals.compat;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.chemical.slurry.SlurryBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/** The only code-backed optional integration: Mekanism slurries are registry entries, not data-pack entries. */
public final class MekanismCompat {
    public static final List<String> PROCESSING_MATERIALS = List.of(
            "adamantine", "antimony", "bismuth", "coldiron",
            "nickel", "platinum", "starsteel", "zinc");

    private static final DeferredRegister<Slurry> SLURRIES = DeferredRegister.create(
            MekanismAPI.SLURRY_REGISTRY_NAME, BaseMetals.MOD_ID);
    private static final Map<String, RegistryObject<Slurry>> DIRTY = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Slurry>> CLEAN = new LinkedHashMap<>();

    static {
        for (String name : PROCESSING_MATERIALS) {
            int colour = MaterialCatalogue.get(name).colour();
            ResourceLocation oreTag = new ResourceLocation("forge", "ores/" + name);
            DIRTY.put(name, SLURRIES.register("dirty_" + name,
                    () -> new Slurry(SlurryBuilder.dirty().tint(colour).ore(oreTag))));
            CLEAN.put(name, SLURRIES.register("clean_" + name,
                    () -> new Slurry(SlurryBuilder.clean().tint(colour).ore(oreTag))));
        }
    }

    private MekanismCompat() {}

    public static void register(IEventBus modBus) {
        SLURRIES.register(modBus);
    }
}
