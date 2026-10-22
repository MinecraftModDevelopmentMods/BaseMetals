package zone.moddev.mc.basemetals.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;

/** All content registration, generated from the immutable catalogues. */
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

    public static final DeferredHolder<Block, ? extends Block> HUMAN_DETECTOR;

    private static final List<String> MATERIAL_BLOCK_FORMS = List.of(
            "block", "plate", "bars", "door", "trapdoor", "button", "slab", "lever",
            "pressure_plate", "stairs", "wall");
    private static final List<String> BASIC_ITEM_FORMS = List.of("ingot", "nugget", "powder", "smallpowder");
    private static final List<String> PROCESSING_FORMS = List.of(
            "casing", "dense_plate", "crushed", "crushed_purified", "crystal", "shard", "clump", "powder_dirty");

    static {
        MaterialCatalogue.ALL.forEach(ModContent::registerMaterial);
        registerAnvil("stone_anvil", BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                .sound(SoundType.STONE).strength(5.0F, 10.0F));
        registerAnvil("steel_anvil", metalProperties(MaterialCatalogue.get("steel")));
        registerAnvil("adamantine_anvil", metalProperties(MaterialCatalogue.get("adamantine")));
        HUMAN_DETECTOR = registerBlock("human_detector",
                () -> new HumanDetectorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                        .sound(SoundType.METAL).strength(5.0F).noCollission()), true);
        registerVanillaBits();
        MaterialCatalogue.ALL.forEach(material -> registerFluid(material.name(), material.colour(),
                material.name().equals("mercury"), material.moltenViscosity()));
        Map<String, MaterialDefinition> vanillaFluids = vanillaDefinitions();
        for (String name : List.of("charcoal", "coal", "diamond", "emerald", "ender", "gold", "iron",
                "lapis", "obsidian", "prismarine", "quartz", "wood", "redstone", "stone")) {
            int colour = vanillaFluids.containsKey(name) ? vanillaFluids.get(name).colour() : 0xFFFFFF;
            int viscosity = vanillaFluids.containsKey(name)
                    ? vanillaFluids.get(name).moltenViscosity() : 15000;
            registerFluid(name, colour, false, viscosity);
        }
    }

    private ModContent() {}

    public static void register(IEventBus bus) {
        MaterialArmor.REGISTER.register(bus);
        BLOCKS.register(bus);
        ITEMS.register(bus);
        FLUIDS.register(bus);
        FLUID_TYPES.register(bus);
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

    private static void registerMaterial(MaterialDefinition material) {
        Map<String, DeferredHolder<Block, ? extends Block>> blocks = new LinkedHashMap<>();
        Map<String, DeferredHolder<Item, ? extends Item>> items = new LinkedHashMap<>();
        if (material.hasEquipment()) {
            for (String form : MATERIAL_BLOCK_FORMS) {
                String id = material.name() + "_" + form;
                DeferredHolder<Block, ? extends Block> block = registerMaterialBlock(id, form, material, true);
                blocks.put(form, block);
                items.put(form, ITEMS_BY_ID.get(id));
            }
            String doubleSlab = "double_" + material.name() + "_slab";
            blocks.put("double_slab", registerBlock(doubleSlab,
                    () -> new CompatibilityDoubleSlabBlock(metalProperties(material)), false));
            HIDDEN_BLOCKS.add(doubleSlab);
        }
        if (material.hasOre()) {
            String id = material.name() + "_ore";
            blocks.put("ore", registerBlock(id, () -> new Block(oreProperties(material)), true));
            items.put("ore", ITEMS_BY_ID.get(id));
            if (!material.name().equals("copper")) {
                items.put("raw", registerSimpleMaterialItem(material.name() + "_raw", material));
            }
        }
        for (String form : BASIC_ITEM_FORMS) {
            items.put(form, registerSimpleMaterialItem(material.name() + "_" + form, material));
        }
        if (material.isAlloy()) {
            items.put("blend", registerSimpleMaterialItem(material.name() + "_blend", material));
            items.put("smallblend", registerSimpleMaterialItem(material.name() + "_smallblend", material));
        }
        if (material.hasEquipment()) {
            registerEquipment(material, items);
        }
        if (material.processingForms()) {
            for (String form : PROCESSING_FORMS) {
                items.put(form, registerSimpleMaterialItem(material.name() + "_" + form, material));
            }
        }
        MATERIALS.put(material.name(), new MaterialContent(material,
                Collections.unmodifiableMap(blocks), Collections.unmodifiableMap(items)));
    }

    private static DeferredHolder<Block, ? extends Block> registerMaterialBlock(String id, String form,
            MaterialDefinition material, boolean item) {
        return registerMaterialBlock(id, form, material, item,
                () -> metalProperties(material),
                () -> MATERIALS.containsKey(material.name())
                        ? MATERIALS.get(material.name()).blocks().get("block").get().defaultBlockState()
                        : net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState());
    }

    private static DeferredHolder<Block, ? extends Block> registerMaterialBlock(String id, String form,
            MaterialDefinition material, boolean item, Supplier<BlockBehaviour.Properties> properties,
            Supplier<net.minecraft.world.level.block.state.BlockState> stairBase) {
        Supplier<Block> factory = switch (form) {
            case "plate" -> () -> new PlateBlock(properties.get().noOcclusion());
            case "bars" -> () -> new IronBarsBlock(properties.get().noOcclusion().dynamicShape());
            case "door" -> () -> new DoorBlock(doorType(material), properties.get().noOcclusion().dynamicShape());
            case "trapdoor" -> () -> new TrapDoorBlock(doorType(material), properties.get().noOcclusion().dynamicShape());
            case "button" -> () -> new ButtonBlock(BlockSetType.STONE, 20, properties.get().noCollission());
            case "slab" -> () -> new SlabBlock(properties.get());
            case "lever" -> () -> new LeverBlock(properties.get().noCollission());
            case "pressure_plate" -> () -> new PressurePlateBlock(
                    BlockSetType.STONE, properties.get());
            case "stairs" -> () -> new StairBlock(stairBase.get(), properties.get());
            case "wall" -> () -> new WallBlock(properties.get());
            default -> () -> new Block(properties.get());
        };
        return registerBlock(id, factory, item);
    }

    private static BlockSetType doorType(MaterialDefinition material) {
        return material.name().equals("quartz") ? BlockSetType.STONE : BlockSetType.IRON;
    }

    private static void registerEquipment(MaterialDefinition material, Map<String, DeferredHolder<Item, ? extends Item>> items) {
        MaterialArmor.register(material);
        Item.Properties tool = new Item.Properties().durability(material.toolDurability());
        Item.Properties combat = new Item.Properties();
        items.put("pickaxe", registerItem(material.name() + "_pickaxe", () -> new MaterialItems.Pickaxe(material, tool)));
        items.put("axe", registerItem(material.name() + "_axe", () -> new MaterialItems.Axe(material,
                new Item.Properties())));
        items.put("shovel", registerItem(material.name() + "_shovel", () -> new MaterialItems.Shovel(material,
                new Item.Properties())));
        items.put("hoe", registerItem(material.name() + "_hoe", () -> new MaterialItems.Hoe(material,
                new Item.Properties())));
        items.put("sword", registerItem(material.name() + "_sword", () -> new MaterialItems.Sword(material,
                new Item.Properties())));
        items.put("crackhammer", registerItem(material.name() + "_crackhammer", () -> new CrackhammerItem(material,
                new Item.Properties())));
        items.put("scythe", registerItem(material.name() + "_scythe", () -> new ScytheItem(material,
                new Item.Properties())));
        items.put("shears", registerItem(material.name() + "_shears", () -> new MaterialItems.Shears(material,
                new Item.Properties().durability(material.toolDurability()))));
        items.put("fishing_rod", registerItem(material.name() + "_fishing_rod", () -> new MaterialItems.FishingRod(material,
                new Item.Properties().durability(material.toolDurability()))));
        items.put("bow", registerItem(material.name() + "_bow", () -> new MaterialItems.Bow(material,
                combat.durability(material.toolDurability()))));
        items.put("crossbow", registerItem(material.name() + "_crossbow", () -> new MaterialItems.Crossbow(material,
                new Item.Properties().durability(material.toolDurability()))));
        items.put("shield", registerItem(material.name() + "_shield", () -> new MaterialItems.Shield(material,
                new Item.Properties().durability(material.shieldDurability()))));
        items.put("arrow", registerItem(material.name() + "_arrow", () -> new BaseMetalAmmoItem(material,
                BaseMetalAmmoItem.Kind.ARROW, ModEntities.CUSTOM_ARROW::get,
                new Item.Properties())));
        items.put("bolt", registerItem(material.name() + "_bolt", () -> new BaseMetalAmmoItem(material,
                BaseMetalAmmoItem.Kind.BOLT, ModEntities.CUSTOM_BOLT::get,
                new Item.Properties())));
        items.put("rod", registerSimpleMaterialItem(material.name() + "_rod", material));
        items.put("gear", registerSimpleMaterialItem(material.name() + "_gear", material));
        items.put("horse_armor", registerItem(material.name() + "_horse_armor",
                () -> new MaterialItems.HorseArmor(material, new Item.Properties().stacksTo(1))));
        items.put("helmet", registerArmor(material, EquipmentSlot.HEAD));
        items.put("chestplate", registerArmor(material, EquipmentSlot.CHEST));
        items.put("leggings", registerArmor(material, EquipmentSlot.LEGS));
        items.put("boots", registerArmor(material, EquipmentSlot.FEET));
    }

    private static DeferredHolder<Item, ? extends Item> registerArmor(MaterialDefinition material, EquipmentSlot slot) {
        String id = material.name() + "_" + switch (slot) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> throw new IllegalArgumentException("Not armor: " + slot);
        };
        return registerItem(id, () -> new MaterialItems.Armor(material, slot,
                new Item.Properties()));
    }

    private static void registerVanillaBits() {
        Map<String, MaterialDefinition> vanilla = vanillaDefinitions();
        DeferredHolder<Block, ? extends Block> charcoalBlock = registerBlock("charcoal_block",
                () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                        .sound(SoundType.SAND).requiresCorrectToolForDrops().strength(5.0F)), false);
        registerItem("charcoal_block", () -> new MaterialItems.BurnableBlock(
                charcoalBlock.get(), 16000, new Item.Properties()));
        registerVanillaDecorative("diamond", vanilla.get("diamond"),
                List.of("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("emerald", vanilla.get("emerald"),
                List.of("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("gold", vanilla.get("gold"),
                List.of("bars", "door", "trapdoor", "button", "slab", "lever", "stairs", "wall"));
        registerVanillaDecorative("obsidian", vanilla.get("obsidian"),
                List.of("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("iron", vanilla.get("iron"),
                List.of("button", "slab", "lever", "stairs", "wall"));
        registerVanillaDecorative("quartz", vanilla.get("quartz"),
                List.of("bars", "door", "trapdoor", "button", "lever", "pressure_plate", "wall"));
        registerBlock("gold_plate", () -> new PlateBlock(BlockBehaviour.Properties.ofLegacyCopy(net.minecraft.world.level.block.Blocks.GOLD_BLOCK).noOcclusion()), true);
        registerBlock("iron_plate", () -> new PlateBlock(BlockBehaviour.Properties.ofLegacyCopy(net.minecraft.world.level.block.Blocks.IRON_BLOCK).noOcclusion()), true);

        for (String name : List.of("diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            registerVanillaCombatAndUtility(vanilla.get(name));
        }
        for (String name : List.of("diamond", "emerald", "gold", "iron", "obsidian", "quartz", "stone", "wood")) {
            MaterialDefinition material = vanilla.get(name);
            registerItem(name + "_crackhammer", () -> new CrackhammerItem(material,
                    new Item.Properties()));
            registerSimpleMaterialItem(name + "_gear", material);
            registerItem(name + "_scythe", () -> new ScytheItem(material,
                    new Item.Properties()));
        }
        for (String name : List.of("coal", "charcoal", "diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            MaterialDefinition material = vanilla.get(name);
            registerSimpleMaterialItem(name + "_powder", material);
            registerSimpleMaterialItem(name + "_smallpowder", material);
        }
        registerSimpleMaterialItem("redstone_smallpowder", vanilla.get("redstone"));
        for (String name : List.of("coal", "charcoal", "diamond", "emerald", "obsidian", "quartz")) {
            registerSimpleMaterialItem(name + "_nugget", vanilla.get(name));
        }
        for (String name : List.of("diamond", "gold", "iron")) {
            registerSimpleMaterialItem(name + "_blend", vanilla.get(name));
            registerSimpleMaterialItem(name + "_smallblend", vanilla.get(name));
        }
        registerSimpleMaterialItem("lapis_smallpowder", vanilla.get("lapis"));
        registerSimpleMaterialItem("obsidian_ingot", vanilla.get("obsidian"));
        registerSimpleMaterialItem("redstone_ingot", vanilla.get("redstone"));
        registerSimpleMaterialItem("stone_rod", vanilla.get("stone"));
    }

    private static void registerVanillaDecorative(String name, MaterialDefinition material,
            List<String> forms) {
        for (String form : forms) {
            registerMaterialBlock(name + "_" + form, form, material, true,
                    () -> vanillaDecorativeProperties(name, form, material),
                    () -> vanillaStorageBlock(name).defaultBlockState());
        }
        if (forms.contains("slab")) {
            String id = "double_" + name + "_slab";
            registerBlock(id, () -> new CompatibilityDoubleSlabBlock(
                    vanillaDecorativeProperties(name, "slab", material)), false);
            HIDDEN_BLOCKS.add(id);
        }
    }

    private static BlockBehaviour.Properties vanillaDecorativeProperties(String name, String form,
            MaterialDefinition material) {
        boolean gem = name.equals("diamond") || name.equals("emerald")
                || name.equals("obsidian") || name.equals("quartz");
        boolean lockedGemDoor = gem && !name.equals("quartz")
                && (form.equals("door") || form.equals("trapdoor"));
        MapColor mapColor = name.equals("gold") || name.equals("iron") || lockedGemDoor
                ? MapColor.METAL : MapColor.STONE;
        return BlockBehaviour.Properties.of().mapColor(mapColor)
                .sound(gem ? SoundType.GLASS : SoundType.METAL)
                .requiresCorrectToolForDrops()
                .strength(material.blockHardness(), material.blastResistance());
    }

    private static Block vanillaStorageBlock(String name) {
        return switch (name) {
            case "diamond" -> net.minecraft.world.level.block.Blocks.DIAMOND_BLOCK;
            case "emerald" -> net.minecraft.world.level.block.Blocks.EMERALD_BLOCK;
            case "gold" -> net.minecraft.world.level.block.Blocks.GOLD_BLOCK;
            case "iron" -> net.minecraft.world.level.block.Blocks.IRON_BLOCK;
            case "obsidian" -> net.minecraft.world.level.block.Blocks.OBSIDIAN;
            case "quartz" -> net.minecraft.world.level.block.Blocks.QUARTZ_BLOCK;
            default -> throw new IllegalArgumentException("No vanilla storage block for " + name);
        };
    }

    private static void registerVanillaCombatAndUtility(MaterialDefinition material) {
        MaterialArmor.register(material);
        String name = material.name();
        registerItem(name + "_arrow", () -> new BaseMetalAmmoItem(material, BaseMetalAmmoItem.Kind.ARROW,
                ModEntities.CUSTOM_ARROW::get, new Item.Properties()));
        registerItem(name + "_bolt", () -> new BaseMetalAmmoItem(material, BaseMetalAmmoItem.Kind.BOLT,
                ModEntities.CUSTOM_BOLT::get, new Item.Properties()));
        registerItem(name + "_bow", () -> new MaterialItems.Bow(material,
                new Item.Properties().durability(material.toolDurability())));
        registerItem(name + "_crossbow", () -> new MaterialItems.Crossbow(material,
                new Item.Properties().durability(material.toolDurability())));
        registerItem(name + "_fishing_rod", () -> new MaterialItems.FishingRod(material,
                new Item.Properties().durability(material.toolDurability())));
        registerSimpleMaterialItem(name + "_rod", material);
        if (!name.equals("iron")) {
            registerItem(name + "_shears", () -> new MaterialItems.Shears(material,
                    new Item.Properties().durability(material.toolDurability())));
        }
        registerItem(name + "_shield", () -> new MaterialItems.Shield(material,
                new Item.Properties().durability(material.shieldDurability())));
        // Preserve IDs which were added for vanilla materials by MMDLib. The
        // vanilla equipment itself remains the canonical tool/armor where present.
        if (name.equals("emerald") || name.equals("obsidian") || name.equals("quartz")) {
            registerItem(name + "_axe", () -> new MaterialItems.Axe(material, new Item.Properties()));
            registerItem(name + "_pickaxe", () -> new MaterialItems.Pickaxe(material, new Item.Properties()));
            registerItem(name + "_shovel", () -> new MaterialItems.Shovel(material, new Item.Properties()));
            registerItem(name + "_hoe", () -> new MaterialItems.Hoe(material, new Item.Properties()));
            registerArmor(material, EquipmentSlot.HEAD);
            registerArmor(material, EquipmentSlot.CHEST);
            registerArmor(material, EquipmentSlot.LEGS);
            registerArmor(material, EquipmentSlot.FEET);
            registerItem(name + "_horse_armor", () -> new MaterialItems.HorseArmor(material,
                    new Item.Properties().stacksTo(1)));
        }
        if (name.equals("emerald") || name.equals("obsidian") || name.equals("quartz")) {
            registerItem(name + "_sword", () -> new MaterialItems.Sword(material,
                    new Item.Properties()));
        }
    }

    private static Map<String, MaterialDefinition> vanillaDefinitions() {
        Map<String, MaterialDefinition> map = new LinkedHashMap<>();
        addVanilla(map, "wood", 2, 2, 6, 0x695433);
        addVanilla(map, "stone", 5, 4, 2, 0x8F8F8F);
        addVanilla(map, "iron", 8, 8, 4.5, 0xD8D8D8);
        addVanilla(map, "gold", 1, 1, 10, 0xFFFF8B);
        addVanilla(map, "diamond", 10, 15, 4, 0x8CF4E1);
        addVanilla(map, "emerald", 10, 15, 4, 0x82F6AC);
        addVanilla(map, "obsidian", 10, 15, 4, 0x101019);
        addVanilla(map, "quartz", 5, 4, 2, 0xEAE3DB);
        addVanilla(map, "coal", 4, 4, 2, 0x151515);
        addVanilla(map, "charcoal", 4, 4, 2, 0x231F18);
        addVanilla(map, "redstone", 1, 1, 1, 0x720000);
        addVanilla(map, "lapis", 1, 1, 1, 0x26619C);
        return map;
    }

    private static void addVanilla(Map<String, MaterialDefinition> map, String name,
            double hardness, double strength, double magic, int colour) {
        map.put(name, new MaterialDefinition(name, MaterialDefinition.Kind.ALLOY,
                hardness, strength, magic, colour, false));
    }

    private static void registerFluid(String name, int colour, boolean mercury, int viscosity) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(BaseMetals.MOD_ID, name);
        DeferredHolder<Fluid, BaseFlowingFluid.Source> source = DeferredHolder.create(Registries.FLUID, id);
        DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing = DeferredHolder.create(
                Registries.FLUID, id.withPrefix("flowing_"));
        DeferredHolder<Block, MoltenMetalBlock> block = DeferredHolder.create(Registries.BLOCK, id);
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
        registerBlock(name, () -> new MoltenMetalBlock(source,
                BlockBehaviour.Properties.of().mapColor(mercury ? MapColor.METAL : MapColor.FIRE)
                        .noCollission().strength(100.0F).noLootTable()
                        .lightLevel(state -> mercury ? 0 : 10), mercury), false);
        // Forge 1.12 registered an ItemBlock under every molten block ID. Keep
        // those hidden inventory IDs so old stacks can survive a world upgrade.
        registerItem(name, () -> new BlockItem(block.get(), new Item.Properties()));
        registerItem(name + "_bucket", () -> new BaseMetalBucketItem(source,
                new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        FLUID_CONTENT.put(name, new FluidContent(source, flowing, block, bucket));
    }

    private static DeferredHolder<Block, ? extends Block> registerAnvil(String id, BlockBehaviour.Properties properties) {
        return registerBlock(id, () -> new BaseMetalAnvilBlock(properties), true);
    }

    private static DeferredHolder<Block, ? extends Block> registerBlock(String id, Supplier<? extends Block> factory, boolean hasItem) {
        if (BLOCKS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate block " + id);
        DeferredHolder<Block, ? extends Block> block = BLOCKS.register(id, factory);
        BLOCKS_BY_ID.put(id, block);
        if (hasItem) {
            registerItem(id, () -> new BlockItem(block.get(), new Item.Properties()));
        }
        return block;
    }

    private static DeferredHolder<Item, ? extends Item> registerSimpleMaterialItem(String id, MaterialDefinition material) {
        return registerItem(id, () -> new MaterialItems.Basic(material, burnTime(id),
                new Item.Properties()));
    }

    private static int burnTime(String id) {
        if (id.equals("charcoal_block")) return 16000;
        if (id.equals("wood_gear")) return 300;
        String material = id.substring(0, id.lastIndexOf('_'));
        if (!isLegacyFuelMaterial(material)) return -1;
        if (id.endsWith("_powder")) return 1600;
        if (id.endsWith("_nugget") || id.endsWith("_smallpowder")) return 200;
        return -1;
    }

    private static boolean isLegacyFuelMaterial(String name) {
        if (name.equals("coal") || name.equals("charcoal")) return true;
        return false;
    }

    private static DeferredHolder<Item, ? extends Item> registerItem(String id, Supplier<? extends Item> factory) {
        if (ITEMS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate item " + id);
        DeferredHolder<Item, ? extends Item> item = ITEMS.register(id, factory);
        ITEMS_BY_ID.put(id, item);
        return item;
    }

    private static BlockBehaviour.Properties metalProperties(MaterialDefinition material) {
        return BlockBehaviour.Properties.of().mapColor(MapColor.METAL)
                .sound(SoundType.METAL).requiresCorrectToolForDrops()
                .strength(material.blockHardness(), material.blastResistance());
    }

    private static BlockBehaviour.Properties oreProperties(MaterialDefinition material) {
        return BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                .sound(SoundType.STONE).requiresCorrectToolForDrops()
                .strength(material.oreHardness(), material.blastResistance());
    }
}
