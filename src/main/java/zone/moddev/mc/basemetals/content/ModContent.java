package zone.moddev.mc.basemetals.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.MissingMappings;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.lib.MMDLib;
import zone.moddev.mc.lib.api.MaterialExtensions;
import zone.moddev.mc.lib.common.factories.MaterialFactory;
import zone.moddev.mc.lib.common.runtime.MaterialStats;
import zone.moddev.mc.lib.common.runtime.RuntimeContentRegistrar;
import zone.moddev.mc.lib.common.types.MMDBlock;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Base Metals content. Materials, vanilla material extensions, anvils and the human
 * detector are MMDLib definitions under {@code mmdlib/}, registered through MMDLib's
 * runtime registrar; molten metals stay Base Metals fluids, filled through the
 * {@code basemetals:bucket} item form.
 */
public final class ModContent {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, BaseMetals.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, BaseMetals.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, BaseMetals.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, BaseMetals.MOD_ID);

    private static final Map<String, DeferredHolder<Block, ? extends Block>> BLOCKS_BY_ID = new LinkedHashMap<>();
    private static final Map<String, DeferredHolder<Item, ? extends Item>> ITEMS_BY_ID = new LinkedHashMap<>();
    private static final Map<String, MaterialContent> MATERIALS = new LinkedHashMap<>();
    private static final Map<String, FluidContent> FLUID_CONTENT = new LinkedHashMap<>();
    private static final List<String> HIDDEN_BLOCKS = new ArrayList<>();

    private static final ResourceLocation BUCKET = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, "bucket");

    private static RuntimeContentRegistrar registrar;

    private ModContent() {}

    public static void register(IEventBus bus) {
        registrar = new RuntimeContentRegistrar(BaseMetals.MOD_ID, MaterialExtensions.GLOBAL, bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        FLUIDS.register(bus);
        FLUID_TYPES.register(bus);
        // MMDLib loads definitions in deferred construction work; registries open after that.
        bus.addListener(NewRegistryEvent.class, event -> populate());
    }

    private static void populate() {
        if (MMDLib.loadedMaterials().isEmpty()) {
            throw new IllegalStateException("MMDLib has not loaded any material definitions");
        }
        for (MaterialDefinition material : MaterialCatalogue.all()) {
            registerFluid(material.name(), material.colour(), material.name().equals("mercury"),
                    material.moltenViscosity());
        }
        // Vanilla extensions with a basemetals:bucket form melt into Base Metals fluids too.
        MMDLib.loadedMaterials().forEach((id, result) -> {
            var material = result.material();
            if (MaterialCatalogue.isVanillaExtension(material) && material.parts().parts().stream()
                    .anyMatch(part -> part.templateId().equals(BUCKET))) {
                registerFluid(id.getPath(), material.properties().color() & 0xFFFFFF, false,
                        new MaterialStats(material).integer("viscosity", 10000, 1));
            }
        });
        MMDLib.loadedMaterials().forEach((id, result) -> {
            if (id.getNamespace().equals(BaseMetals.MOD_ID)) queue(result);
        });
        MMDLib.standaloneBlocks().forEach((id, result) -> {
            if (id.getNamespace().equals(BaseMetals.MOD_ID)) queue(result);
        });
        MissingMappings.registerAliases();
    }

    private static void queue(MaterialFactory.Result result) {
        registrar.queue(result);
        Map<String, DeferredHolder<Block, ? extends Block>> blocks = new LinkedHashMap<>();
        Map<String, DeferredHolder<Item, ? extends Item>> items = new LinkedHashMap<>();
        result.content().blocks().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    MMDBlock definition = entry.getValue();
                    var block = registrar.blocks().get(definition.name());
                    blocks.put(entry.getKey().templateId().getPath(), block);
                    BLOCKS_BY_ID.put(definition.name().getPath(), block);
                    if (entry.getKey().templateId().getPath().equals("double_slab")) {
                        HIDDEN_BLOCKS.add(definition.name().getPath());
                    }
                });
        result.content().items().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    var item = registrar.items().get(entry.getValue().name());
                    items.put(entry.getKey().templateId().getPath(), item);
                    ITEMS_BY_ID.put(entry.getValue().name().getPath(), item);
                });
        MaterialCatalogue.of(result.material()).ifPresent(definition -> MATERIALS.put(definition.name(),
                new MaterialContent(definition, Collections.unmodifiableMap(blocks), Collections.unmodifiableMap(items))));
    }

    public static RuntimeContentRegistrar registrar() {
        return registrar;
    }

    public static Map<String, MaterialContent> materials() {
        return Collections.unmodifiableMap(MATERIALS);
    }

    public static Map<String, DeferredHolder<Block, ? extends Block>> blocksById() {
        return Collections.unmodifiableMap(BLOCKS_BY_ID);
    }

    public static Map<String, DeferredHolder<Item, ? extends Item>> itemsById() {
        return Collections.unmodifiableMap(ITEMS_BY_ID);
    }

    public static List<String> hiddenBlocks() {
        return List.copyOf(HIDDEN_BLOCKS);
    }

    public static Map<String, FluidContent> fluids() {
        return Collections.unmodifiableMap(FLUID_CONTENT);
    }

    public static DeferredHolder<Item, ? extends Item> item(String id) {
        DeferredHolder<Item, ? extends Item> item = ITEMS_BY_ID.get(id);
        if (item == null) throw new IllegalArgumentException("Unknown item " + id);
        return item;
    }

    private static void registerFluid(String name, int colour, boolean mercury, int viscosity) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, name);
        DeferredHolder<Fluid, BaseFlowingFluid.Source> source = DeferredHolder.create(Registries.FLUID, id);
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing = DeferredHolder.create(
                Registries.FLUID, id.withPrefix("flowing_"));
        DeferredHolder<Block, MoltenMetalBlock> block = DeferredHolder.create(Registries.BLOCK, id);
        // The basemetals:bucket item form registers this ID through MMDLib.
        DeferredHolder<Item, BaseMetalBucketItem> bucket = DeferredHolder.create(
                Registries.ITEM, id.withSuffix("_bucket"));
        ResourceLocation still = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID,
                mercury ? "block/mercury_still" : "block/molten_metal_still");
        ResourceLocation flow = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID,
                mercury ? "block/mercury_flow" : "block/molten_metal_flow");
        DeferredHolder<FluidType, MetalFluidType> type = FLUID_TYPES.register(name, () -> new MetalFluidType(
                FluidType.Properties.create().density(mercury ? 13594 : 2000)
                        .viscosity(mercury ? 2000 : viscosity)
                        .temperature(mercury ? 300 : 1300)
                        .motionScale(mercury ? 0.014D : 0.007D)
                        .lightLevel(mercury ? 0 : 10).descriptionId("fluid.basemetals." + name)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), still, flow, colour));
        BaseFlowingFluid.Properties properties = new BaseFlowingFluid.Properties(
                type, source, flowing)
                .bucket(bucket)
                .block(block)
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2)
                .tickRate(30)
                .explosionResistance(100.0F);
        FLUIDS.register(name, () -> new BaseFlowingFluid.Source(properties));
        FLUIDS.register("flowing_" + name, () -> new BaseFlowingFluid.Flowing(properties));
        BLOCKS_BY_ID.put(name, BLOCKS.register(name, () -> new MoltenMetalBlock(source,
                BlockBehaviour.Properties.of().mapColor(mercury ? MapColor.METAL : MapColor.FIRE)
                        .noCollission().strength(100.0F).noLootTable()
                        .lightLevel(state -> mercury ? 0 : 10), mercury)));
        // Forge 1.12 registered an ItemBlock under every molten block ID. Keep
        // those hidden inventory IDs so old stacks can survive a world upgrade.
        ITEMS_BY_ID.put(name, ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties())));
        FLUID_CONTENT.put(name, new FluidContent(source, flowing, block, bucket));
    }
}
