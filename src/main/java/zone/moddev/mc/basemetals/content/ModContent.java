package zone.moddev.mc.basemetals.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.Function;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;
import zone.moddev.mc.basemetals.entity.ModEntities;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.AnvilBlock;
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
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.common.SoundActions;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/** All content registration, generated from the immutable catalogues. */
public final class ModContent {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, BaseMetals.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BaseMetals.MOD_ID);
    public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(ForgeRegistries.FLUIDS, BaseMetals.MOD_ID);
    public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, BaseMetals.MOD_ID);

    private static final Map<String, RegistryObject<? extends Block>> BLOCKS_BY_ID = new LinkedHashMap<>();
    private static final Map<String, RegistryObject<Item>> ITEMS_BY_ID = new LinkedHashMap<>();
    private static final Map<String, MaterialContent> MATERIALS = new LinkedHashMap<>();
    private static final Map<String, FluidContent> FLUID_CONTENT = new LinkedHashMap<>();
    private static final List<String> HIDDEN_BLOCKS = new ArrayList<>();

    public static final RegistryObject<Block> HUMAN_DETECTOR;

    private static final List<String> MATERIAL_BLOCK_FORMS = List.of(
            "block", "plate", "bars", "door", "trapdoor", "button", "slab", "lever",
            "pressure_plate", "stairs", "wall");
    private static final List<String> BASIC_ITEM_FORMS = List.of("ingot", "nugget", "powder", "smallpowder");
    private static final List<String> PROCESSING_FORMS = List.of(
            "casing", "dense_plate", "crushed", "crushed_purified", "crystal", "shard", "clump", "powder_dirty");

    static {
        MaterialCatalogue.ALL.forEach(MaterialArmor::register);
        vanillaDefinitions().values().forEach(MaterialArmor::register);
        MaterialCatalogue.ALL.forEach(ModContent::registerMaterial);
        registerAnvil("stone_anvil", BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                .sound(SoundType.STONE).strength(5.0F, 10.0F));
        registerAnvil("steel_anvil", metalProperties(MaterialCatalogue.get("steel")));
        registerAnvil("adamantine_anvil", metalProperties(MaterialCatalogue.get("adamantine")));
        HUMAN_DETECTOR = registerBlock("human_detector",
                () -> new HumanDetectorBlock(BlockBehaviour.Properties.of().setId(BLOCKS.key("human_detector")).mapColor(MapColor.METAL)
                        .sound(SoundType.METAL).strength(5.0F).noCollision()), true);
        registerVanillaBits();
        MaterialCatalogue.ALL.forEach(material -> registerFluid(material.name(), material.colour(),
                material.name().equals("mercury")));
        Map<String, MaterialDefinition> vanillaFluids = vanillaDefinitions();
        for (String name : List.of("charcoal", "coal", "diamond", "emerald", "ender", "gold", "iron",
                "lapis", "obsidian", "prismarine", "quartz", "wood", "redstone", "stone")) {
            int colour = vanillaFluids.containsKey(name) ? vanillaFluids.get(name).colour() : 0xFFFFFF;
            registerFluid(name, colour, false);
        }
    }

    private ModContent() {}

    private static final class MaterialProperties extends Item.Properties {
        @Override
        public Item.Properties enchantable(int value) {
            // Zero means un-enchantable in the material catalogue. The native
            // component accepts only positive values, so leave it absent.
            return value > 0 ? super.enchantable(value) : this;
        }
    }

    public static void register(BusGroup bus) {
        BLOCKS.register(bus);
        ITEMS.register(bus);
        FLUIDS.register(bus);
        FLUID_TYPES.register(bus);
    }

    public static Map<String, MaterialContent> materials() {
        return Collections.unmodifiableMap(MATERIALS);
    }

    public static Map<String, RegistryObject<? extends Block>> blocksById() {
        return Collections.unmodifiableMap(BLOCKS_BY_ID);
    }

    public static Map<String, RegistryObject<Item>> itemsById() {
        return Collections.unmodifiableMap(ITEMS_BY_ID);
    }

    public static List<String> hiddenBlocks() {
        return List.copyOf(HIDDEN_BLOCKS);
    }

    public static Map<String, FluidContent> fluids() {
        return Collections.unmodifiableMap(FLUID_CONTENT);
    }

    public static RegistryObject<Item> item(String id) {
        RegistryObject<Item> item = ITEMS_BY_ID.get(id);
        if (item == null) throw new IllegalArgumentException("Unknown item " + id);
        return item;
    }

    private static void registerMaterial(MaterialDefinition material) {
        Map<String, RegistryObject<Block>> blocks = new LinkedHashMap<>();
        Map<String, RegistryObject<Item>> items = new LinkedHashMap<>();
        if (material.hasEquipment()) {
            for (String form : MATERIAL_BLOCK_FORMS) {
                String id = material.name() + "_" + form;
                RegistryObject<Block> block = registerMaterialBlock(id, form, material, true);
                blocks.put(form, block);
                items.put(form, ITEMS_BY_ID.get(id));
            }
            String doubleSlab = "double_" + material.name() + "_slab";
            blocks.put("double_slab", registerBlock(doubleSlab,
                    () -> new CompatibilityDoubleSlabBlock(metalProperties(material).setId(BLOCKS.key(doubleSlab))), false));
            HIDDEN_BLOCKS.add(doubleSlab);
        }
        if (material.hasOre()) {
            String id = material.name() + "_ore";
            blocks.put("ore", registerBlock(id, () -> new Block(oreProperties(material).setId(BLOCKS.key(id))), true));
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

    private static RegistryObject<Block> registerMaterialBlock(String id, String form,
            MaterialDefinition material, boolean item) {
        return registerMaterialBlock(id, form, material, item,
                () -> metalProperties(material),
                () -> MATERIALS.containsKey(material.name())
                        ? MATERIALS.get(material.name()).blocks().get("block").get().defaultBlockState()
                        : net.minecraft.world.level.block.Blocks.IRON_BLOCK.defaultBlockState());
    }

    private static RegistryObject<Block> registerMaterialBlock(String id, String form,
            MaterialDefinition material, boolean item, Supplier<BlockBehaviour.Properties> properties,
            Supplier<net.minecraft.world.level.block.state.BlockState> stairBase) {
        Supplier<BlockBehaviour.Properties> keyedProperties = () -> properties.get().setId(BLOCKS.key(id));
        Supplier<Block> factory = switch (form) {
            case "plate" -> () -> new PlateBlock(keyedProperties.get().noOcclusion());
            case "bars" -> () -> new IronBarsBlock(keyedProperties.get().noOcclusion().dynamicShape());
            case "door" -> () -> new DoorBlock(doorType(material), keyedProperties.get().noOcclusion().dynamicShape());
            case "trapdoor" -> () -> new TrapDoorBlock(doorType(material), keyedProperties.get().noOcclusion().dynamicShape());
            case "button" -> () -> new ButtonBlock(BlockSetType.STONE, 20, keyedProperties.get().noCollision());
            case "slab" -> () -> new SlabBlock(keyedProperties.get());
            case "lever" -> () -> new LeverBlock(keyedProperties.get().noCollision());
            case "pressure_plate" -> () -> new PressurePlateBlock(
                    BlockSetType.STONE, keyedProperties.get());
            case "stairs" -> () -> new StairBlock(stairBase.get(), keyedProperties.get());
            case "wall" -> () -> new WallBlock(keyedProperties.get());
            default -> () -> new Block(keyedProperties.get());
        };
        return registerBlock(id, factory, item);
    }

    private static BlockSetType doorType(MaterialDefinition material) {
        return material.name().equals("quartz") ? BlockSetType.STONE : BlockSetType.IRON;
    }

    private static void registerEquipment(MaterialDefinition material, Map<String, RegistryObject<Item>> items) {
        items.put("pickaxe", registerItem(material.name() + "_pickaxe", itemProperties -> new MaterialItems.Pickaxe(material, itemProperties)));
        items.put("axe", registerItem(material.name() + "_axe", itemProperties -> new MaterialItems.Axe(material,
                itemProperties)));
        items.put("shovel", registerItem(material.name() + "_shovel", itemProperties -> new MaterialItems.Shovel(material,
                itemProperties)));
        items.put("hoe", registerItem(material.name() + "_hoe", itemProperties -> new MaterialItems.Hoe(material,
                itemProperties)));
        items.put("sword", registerItem(material.name() + "_sword", itemProperties -> new MaterialItems.Sword(material,
                itemProperties)));
        items.put("crackhammer", registerItem(material.name() + "_crackhammer", itemProperties -> new CrackhammerItem(material,
                itemProperties)));
        items.put("scythe", registerItem(material.name() + "_scythe", itemProperties -> new ScytheItem(material,
                itemProperties)));
        items.put("shears", registerItem(material.name() + "_shears", itemProperties -> new MaterialItems.Shears(material,
                itemProperties.durability(material.toolDurability()))));
        items.put("fishing_rod", registerItem(material.name() + "_fishing_rod", itemProperties -> new MaterialItems.FishingRod(material,
                itemProperties.durability(material.toolDurability()))));
        items.put("bow", registerItem(material.name() + "_bow", itemProperties -> new MaterialItems.Bow(material,
                itemProperties.durability(material.toolDurability()))));
        items.put("crossbow", registerItem(material.name() + "_crossbow", itemProperties -> new MaterialItems.Crossbow(material,
                itemProperties.durability(material.toolDurability()))));
        items.put("shield", registerItem(material.name() + "_shield", itemProperties -> new MaterialItems.Shield(material,
                itemProperties.durability(material.shieldDurability()))));
        items.put("arrow", registerItem(material.name() + "_arrow", itemProperties -> new BaseMetalAmmoItem(material,
                BaseMetalAmmoItem.Kind.ARROW, ModEntities.CUSTOM_ARROW::get,
                itemProperties)));
        items.put("bolt", registerItem(material.name() + "_bolt", itemProperties -> new BaseMetalAmmoItem(material,
                BaseMetalAmmoItem.Kind.BOLT, ModEntities.CUSTOM_BOLT::get,
                itemProperties)));
        items.put("rod", registerSimpleMaterialItem(material.name() + "_rod", material));
        items.put("gear", registerSimpleMaterialItem(material.name() + "_gear", material));
        items.put("horse_armor", registerItem(material.name() + "_horse_armor",
                itemProperties -> new MaterialItems.HorseArmor(material, itemProperties.stacksTo(1))));
        items.put("helmet", registerArmor(material, EquipmentSlot.HEAD));
        items.put("chestplate", registerArmor(material, EquipmentSlot.CHEST));
        items.put("leggings", registerArmor(material, EquipmentSlot.LEGS));
        items.put("boots", registerArmor(material, EquipmentSlot.FEET));
    }

    private static RegistryObject<Item> registerArmor(MaterialDefinition material, EquipmentSlot slot) {
        String id = material.name() + "_" + switch (slot) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> throw new IllegalArgumentException("Not armor: " + slot);
        };
        return registerItem(id, itemProperties -> new MaterialItems.Armor(material, slot,
                itemProperties));
    }

    private static void registerVanillaBits() {
        Map<String, MaterialDefinition> vanilla = vanillaDefinitions();
        RegistryObject<Block> charcoalBlock = registerBlock("charcoal_block",
                () -> new Block(BlockBehaviour.Properties.of().setId(BLOCKS.key("charcoal_block")).mapColor(MapColor.STONE)
                        .sound(SoundType.SAND).requiresCorrectToolForDrops().strength(5.0F)), false);
        registerItem("charcoal_block", itemProperties -> new MaterialItems.BurnableBlock(
                charcoalBlock.get(), 16000, itemProperties));
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
        registerBlock("gold_plate", () -> new PlateBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.GOLD_BLOCK).setId(BLOCKS.key("gold_plate")).noOcclusion()), true);
        registerBlock("iron_plate", () -> new PlateBlock(BlockBehaviour.Properties.ofFullCopy(net.minecraft.world.level.block.Blocks.IRON_BLOCK).setId(BLOCKS.key("iron_plate")).noOcclusion()), true);

        for (String name : List.of("diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            registerVanillaCombatAndUtility(vanilla.get(name));
        }
        for (String name : List.of("diamond", "emerald", "gold", "iron", "obsidian", "quartz", "stone", "wood")) {
            MaterialDefinition material = vanilla.get(name);
            registerItem(name + "_crackhammer", itemProperties -> new CrackhammerItem(material,
                    itemProperties));
            registerSimpleMaterialItem(name + "_gear", material);
            registerItem(name + "_scythe", itemProperties -> new ScytheItem(material,
                    itemProperties));
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
                    vanillaDecorativeProperties(name, "slab", material).setId(BLOCKS.key(id))), false);
            HIDDEN_BLOCKS.add(id);
        }
    }

    private static BlockBehaviour.Properties vanillaDecorativeProperties(String name, String form,
            MaterialDefinition material) {
        boolean gem = name.equals("diamond") || name.equals("emerald")
                || name.equals("obsidian") || name.equals("quartz");
        boolean lockedGemDoor = gem && !name.equals("quartz")
                && (form.equals("door") || form.equals("trapdoor"));
        MapColor mapColour = name.equals("gold") || name.equals("iron") || lockedGemDoor
                ? MapColor.METAL : MapColor.STONE;
        return BlockBehaviour.Properties.of().mapColor(mapColour)
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
        String name = material.name();
        registerItem(name + "_arrow", itemProperties -> new BaseMetalAmmoItem(material, BaseMetalAmmoItem.Kind.ARROW,
                ModEntities.CUSTOM_ARROW::get, itemProperties));
        registerItem(name + "_bolt", itemProperties -> new BaseMetalAmmoItem(material, BaseMetalAmmoItem.Kind.BOLT,
                ModEntities.CUSTOM_BOLT::get, itemProperties));
        registerItem(name + "_bow", itemProperties -> new MaterialItems.Bow(material,
                itemProperties.durability(material.toolDurability())));
        registerItem(name + "_crossbow", itemProperties -> new MaterialItems.Crossbow(material,
                itemProperties.durability(material.toolDurability())));
        registerItem(name + "_fishing_rod", itemProperties -> new MaterialItems.FishingRod(material,
                itemProperties.durability(material.toolDurability())));
        registerSimpleMaterialItem(name + "_rod", material);
        if (!name.equals("iron")) {
            registerItem(name + "_shears", itemProperties -> new MaterialItems.Shears(material,
                    itemProperties.durability(material.toolDurability())));
        }
        registerItem(name + "_shield", itemProperties -> new MaterialItems.Shield(material,
                itemProperties.durability(material.shieldDurability())));
        // Preserve IDs which were added for vanilla materials by MMDLib. The
        // vanilla equipment itself remains the canonical tool/armor where present.
        if (name.equals("emerald") || name.equals("obsidian") || name.equals("quartz")) {
            registerItem(name + "_axe", itemProperties -> new MaterialItems.Axe(material, itemProperties));
            registerItem(name + "_pickaxe", itemProperties -> new MaterialItems.Pickaxe(material, itemProperties));
            registerItem(name + "_shovel", itemProperties -> new MaterialItems.Shovel(material, itemProperties));
            registerItem(name + "_hoe", itemProperties -> new MaterialItems.Hoe(material, itemProperties));
            registerArmor(material, EquipmentSlot.HEAD);
            registerArmor(material, EquipmentSlot.CHEST);
            registerArmor(material, EquipmentSlot.LEGS);
            registerArmor(material, EquipmentSlot.FEET);
            registerItem(name + "_horse_armor", itemProperties -> new MaterialItems.HorseArmor(material,
                    itemProperties.stacksTo(1)));
        }
        if (name.equals("emerald") || name.equals("obsidian") || name.equals("quartz")) {
            registerItem(name + "_sword", itemProperties -> new MaterialItems.Sword(material,
                    itemProperties));
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

    @SuppressWarnings("unchecked")
    private static void registerFluid(String name, int colour, boolean mercury) {
        RegistryObject<ForgeFlowingFluid.Source>[] source = new RegistryObject[1];
        RegistryObject<ForgeFlowingFluid.Flowing>[] flowing = new RegistryObject[1];
        RegistryObject<LiquidBlock>[] block = new RegistryObject[1];
        RegistryObject<Item>[] bucket = new RegistryObject[1];
        Identifier still = Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID,
                mercury ? "block/mercury_still" : "block/molten_metal_still");
        Identifier flow = Identifier.fromNamespaceAndPath(BaseMetals.MOD_ID,
                mercury ? "block/mercury_flow" : "block/molten_metal_flow");
        RegistryObject<FluidType> type = FLUID_TYPES.register(name, () -> new MetalFluidType(
                FluidType.Properties.create().density(mercury ? 13594 : 2000)
                        .viscosity(mercury ? 2000 : 10000).temperature(769)
                        .lightLevel(mercury ? 0 : 10).descriptionId("fluid.basemetals." + name)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), still, flow, colour));
        ForgeFlowingFluid.Properties properties = new ForgeFlowingFluid.Properties(
                type, () -> source[0].get(), () -> flowing[0].get())
                .bucket(() -> bucket[0].get())
                .block(() -> block[0].get())
                .slopeFindDistance(2)
                .levelDecreasePerBlock(2)
                .tickRate(30)
                .explosionResistance(100.0F);
        source[0] = FLUIDS.register(name, () -> new ForgeFlowingFluid.Source(properties));
        flowing[0] = FLUIDS.register("flowing_" + name, () -> new ForgeFlowingFluid.Flowing(properties));
        block[0] = BLOCKS.register(name, () -> new MoltenMetalBlock(() -> source[0].get(),
                BlockBehaviour.Properties.of().setId(BLOCKS.key(name)).mapColor(MapColor.FIRE).liquid().replaceable()
                        .pushReaction(PushReaction.POPPED).noCollision().strength(100.0F).noLootTable(), mercury));
        BLOCKS_BY_ID.put(name, block[0]);
        // Forge 1.12 registered an ItemBlock under every molten block ID. Keep
        // those hidden inventory IDs so old stacks can survive a world upgrade.
        registerItem(name, itemProperties -> new BlockItem(block[0].get(), itemProperties));
        bucket[0] = registerItem(name + "_bucket", itemProperties -> new BaseMetalBucketItem(() -> source[0].get(),
                itemProperties.craftRemainder(Items.BUCKET).stacksTo(1)));
        FLUID_CONTENT.put(name, new FluidContent(source[0], flowing[0], block[0], bucket[0]));
    }

    private static RegistryObject<Block> registerAnvil(String id, BlockBehaviour.Properties properties) {
        return registerBlock(id, () -> new BaseMetalAnvilBlock(properties.setId(BLOCKS.key(id))), true);
    }

    private static RegistryObject<Block> registerBlock(String id, Supplier<? extends Block> factory, boolean hasItem) {
        if (BLOCKS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate block " + id);
        RegistryObject<Block> block = BLOCKS.register(id, factory);
        BLOCKS_BY_ID.put(id, block);
        if (hasItem) {
            registerItem(id, itemProperties -> new BlockItem(block.get(), itemProperties));
        }
        return block;
    }

    private static RegistryObject<Item> registerSimpleMaterialItem(String id, MaterialDefinition material) {
        return registerItem(id, itemProperties -> new MaterialItems.Basic(material, burnTime(id),
                itemProperties));
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

    private static RegistryObject<Item> registerItem(String id, Function<Item.Properties, ? extends Item> factory) {
        if (ITEMS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate item " + id);
        RegistryObject<Item> item = ITEMS.register(id,
                () -> factory.apply(new MaterialProperties().setId(ITEMS.key(id))));
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
