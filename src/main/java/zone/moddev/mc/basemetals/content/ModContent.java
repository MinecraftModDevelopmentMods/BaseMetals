package zone.moddev.mc.basemetals.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.ModTabs;
import zone.moddev.mc.basemetals.entity.ModEntities;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.block.Block;
import net.minecraft.block.StoneButtonBlock;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.PaneBlock;
import net.minecraft.block.PressurePlateBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/** Registers material blocks, items and fluids from the catalogue. */
@Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModContent {
    private static final Map<String, RegistryHandle<Block>> BLOCKS_BY_ID =
            new LinkedHashMap<String, RegistryHandle<Block>>();
    private static final Map<String, RegistryHandle<Item>> ITEMS_BY_ID =
            new LinkedHashMap<String, RegistryHandle<Item>>();
    private static final Map<String, Supplier<? extends Block>> BLOCK_FACTORIES =
            new LinkedHashMap<String, Supplier<? extends Block>>();
    private static final Map<String, Supplier<? extends Item>> ITEM_FACTORIES =
            new LinkedHashMap<String, Supplier<? extends Item>>();
    private static final Map<String, MaterialContent> MATERIALS =
            new LinkedHashMap<String, MaterialContent>();
    private static final Map<String, FluidContent> FLUID_CONTENT =
            new LinkedHashMap<String, FluidContent>();
    private static final Map<String, Integer> FLUID_COLOURS =
            new LinkedHashMap<String, Integer>();
    private static final List<String> HIDDEN_BLOCKS = new ArrayList<String>();

    private static final List<String> MATERIAL_BLOCK_FORMS = list(
            "block", "plate", "bars", "door", "trapdoor", "button", "slab", "lever",
            "pressure_plate", "stairs", "wall");
    private static final List<String> BASIC_ITEM_FORMS = list("ingot", "nugget", "powder", "smallpowder");
    private static final List<String> PROCESSING_FORMS = list(
            "casing", "dense_plate", "crushed", "crushed_purified", "crystal", "shard", "clump", "powder_dirty");

    public static final RegistryHandle<Block> HUMAN_DETECTOR;

    static {
        for (MaterialDefinition material : MaterialCatalogue.ALL) registerMaterial(material);
        registerAnvil("stone_anvil", Block.Properties.create(Material.ROCK)
                .sound(SoundType.STONE).hardnessAndResistance(5.0F, 10.0F));
        registerAnvil("steel_anvil", metalProperties(MaterialCatalogue.get("steel")));
        registerAnvil("adamantine_anvil", metalProperties(MaterialCatalogue.get("adamantine")));
        HUMAN_DETECTOR = registerBlock("human_detector",
                new Supplier<Block>() {
                    @Override public Block get() {
                        return new HumanDetectorBlock(Block.Properties.create(Material.IRON)
                                .sound(SoundType.METAL).hardnessAndResistance(5.0F).doesNotBlockMovement());
                    }
                }, true);
        registerVanillaBits();
        for (MaterialDefinition material : MaterialCatalogue.ALL) {
            registerFluid(material.name(), material.colour(), "mercury".equals(material.name()));
        }
        Map<String, MaterialDefinition> vanillaFluids = vanillaDefinitions();
        for (String name : list("charcoal", "coal", "diamond", "emerald", "ender", "gold", "iron",
                "lapis", "obsidian", "prismarine", "quartz", "wood", "redstone", "stone")) {
            int colour = vanillaFluids.containsKey(name) ? vanillaFluids.get(name).colour() : 0xFFFFFF;
            registerFluid(name, colour, false);
        }
    }

    private ModContent() {}

    /** Create the paired fluids before blocks and buckets ask for them. */
    public static synchronized void initializeFluids() {
        for (Map.Entry<String, FluidContent> entry : FLUID_CONTENT.entrySet()) {
            FluidContent content = entry.getValue();
            if (isBound(content.source())) continue;
            MoltenFluid flowing = new MoltenFluid.Flowing(entry.getKey());
            MoltenFluid source = new MoltenFluid.Source(entry.getKey());
            content.flowing().bind(flowing);
            content.source().bind(source);
            flowing.setRegistryName(id("flowing_" + entry.getKey()));
            source.setRegistryName(id(entry.getKey()));
        }
    }

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        for (FluidContent content : FLUID_CONTENT.values()) {
            event.getRegistry().registerAll(content.source().get(), content.flowing().get());
        }
    }

    private static boolean isBound(RegistryHandle<?> handle) {
        try { handle.get(); return true; } catch (IllegalStateException ignored) { return false; }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for (Map.Entry<String, Supplier<? extends Block>> entry : BLOCK_FACTORIES.entrySet()) {
            Block value = entry.getValue().get();
            value.setRegistryName(BaseMetals.MOD_ID, entry.getKey());
            BLOCKS_BY_ID.get(entry.getKey()).bind(value);
            event.getRegistry().register(value);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        for (Map.Entry<String, Supplier<? extends Item>> entry : ITEM_FACTORIES.entrySet()) {
            Item value = entry.getValue().get();
            value.setRegistryName(BaseMetals.MOD_ID, entry.getKey());
            ITEMS_BY_ID.get(entry.getKey()).bind(value);
            event.getRegistry().register(value);
        }
    }

    public static Map<String, MaterialContent> materials() { return Collections.unmodifiableMap(MATERIALS); }
    public static Map<String, RegistryHandle<Block>> blocksById() { return Collections.unmodifiableMap(BLOCKS_BY_ID); }
    public static Map<String, RegistryHandle<Item>> itemsById() { return Collections.unmodifiableMap(ITEMS_BY_ID); }
    public static List<String> hiddenBlocks() { return Collections.unmodifiableList(HIDDEN_BLOCKS); }
    public static Map<String, FluidContent> fluids() { return Collections.unmodifiableMap(FLUID_CONTENT); }

    public static int fluidColour(Fluid fluid) {
        for (Map.Entry<String, FluidContent> entry : FLUID_CONTENT.entrySet()) {
            FluidContent content = entry.getValue();
            if (fluid == content.source().get() || fluid == content.flowing().get()) {
                Integer colour = FLUID_COLOURS.get(entry.getKey());
                return colour == null ? 0xFFFFFF : colour.intValue();
            }
        }
        return 0xFFFFFF;
    }

    public static boolean isBaseMetalsFluid(Fluid fluid) {
        for (FluidContent content : FLUID_CONTENT.values()) {
            if (fluid == content.source().get() || fluid == content.flowing().get()) return true;
        }
        return false;
    }

    public static RegistryHandle<Item> item(String id) {
        RegistryHandle<Item> item = ITEMS_BY_ID.get(id);
        if (item == null) throw new IllegalArgumentException("Unknown item " + id);
        return item;
    }

    public static FluidContent fluid(String id) {
        FluidContent fluid = FLUID_CONTENT.get(id);
        if (fluid == null) throw new IllegalArgumentException("Unknown fluid " + id);
        return fluid;
    }

    private static void registerMaterial(MaterialDefinition material) {
        Map<String, RegistryHandle<Block>> blocks = new LinkedHashMap<String, RegistryHandle<Block>>();
        Map<String, RegistryHandle<Item>> items = new LinkedHashMap<String, RegistryHandle<Item>>();
        if (material.hasEquipment()) {
            for (String form : MATERIAL_BLOCK_FORMS) {
                String id = material.name() + "_" + form;
                RegistryHandle<Block> block = registerMaterialBlock(id, form, material, true,
                        new Supplier<Block.Properties>() {
                            @Override public Block.Properties get() { return metalProperties(material); }
                        }, null);
                blocks.put(form, block);
                items.put(form, ITEMS_BY_ID.get(id));
            }
            String doubleSlab = "double_" + material.name() + "_slab";
            blocks.put("double_slab", registerBlock(doubleSlab,
                    new Supplier<Block>() {
                        @Override public Block get() { return new CompatibilityDoubleSlabBlock(metalProperties(material), material.requiredHarvestLevel()); }
                    }, false));
            HIDDEN_BLOCKS.add(doubleSlab);
        }
        if (material.hasOre()) {
            String id = material.name() + "_ore";
            blocks.put("ore", registerBlock(id,
                    new Supplier<Block>() {
                        @Override public Block get() { return new HarvestOre(oreProperties(material), material.requiredHarvestLevel()); }
                    }, true));
            items.put("ore", ITEMS_BY_ID.get(id));
        }
        for (String form : BASIC_ITEM_FORMS) {
            items.put(form, registerSimpleMaterialItem(material.name() + "_" + form, material));
        }
        if (material.isAlloy()) {
            items.put("blend", registerSimpleMaterialItem(material.name() + "_blend", material));
            items.put("smallblend", registerSimpleMaterialItem(material.name() + "_smallblend", material));
        }
        if (material.hasEquipment()) registerEquipment(material, items);
        if (material.processingForms()) {
            for (String form : PROCESSING_FORMS) {
                items.put(form, registerSimpleMaterialItem(material.name() + "_" + form, material));
            }
        }
        MATERIALS.put(material.name(), new MaterialContent(material, blocks, items));
    }

    private static RegistryHandle<Block> registerMaterialBlock(String id, String form,
            final MaterialDefinition material, boolean hasItem,
            final Supplier<Block.Properties> properties, final Supplier<Block> stairBase) {
        Supplier<Block> factory;
        if ("plate".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new PlateBlock(properties.get(), material.requiredHarvestLevel()); } };
        } else if ("bars".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestPane(properties.get().variableOpacity(), material.requiredHarvestLevel()); } };
        } else if ("door".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestDoor(properties.get().variableOpacity(), material.requiredHarvestLevel()); } };
        } else if ("trapdoor".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestTrapDoor(properties.get().variableOpacity(), material.requiredHarvestLevel()); } };
        } else if ("button".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestButton(properties.get().doesNotBlockMovement(), material.requiredHarvestLevel()); } };
        } else if ("slab".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestSlab(properties.get(), material.requiredHarvestLevel()); } };
        } else if ("lever".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestLever(properties.get().doesNotBlockMovement(), material.requiredHarvestLevel()); } };
        } else if ("pressure_plate".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestPressurePlate(properties.get(), material.requiredHarvestLevel()); } };
        } else if ("stairs".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() {
                Block base = stairBase == null ? Blocks.IRON_BLOCK : stairBase.get();
                return new HarvestStairs(base.getDefaultState(), properties.get(), material.requiredHarvestLevel());
            } };
        } else if ("wall".equals(form)) {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestWall(properties.get(), material.requiredHarvestLevel()); } };
        } else {
            factory = new Supplier<Block>() { @Override public Block get() { return new HarvestBlock(properties.get(), material.requiredHarvestLevel()); } };
        }
        return registerBlock(id, factory, hasItem);
    }

    private static void registerEquipment(final MaterialDefinition material,
            Map<String, RegistryHandle<Item>> items) {
        items.put("pickaxe", registerItem(material.name() + "_pickaxe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Pickaxe(material, toolProperties()); } }));
        items.put("axe", registerItem(material.name() + "_axe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Axe(material, toolProperties()); } }));
        items.put("shovel", registerItem(material.name() + "_shovel", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shovel(material, toolProperties()); } }));
        items.put("hoe", registerItem(material.name() + "_hoe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Hoe(material, toolProperties()); } }));
        items.put("sword", registerItem(material.name() + "_sword", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Sword(material, combatProperties()); } }));
        items.put("crackhammer", registerItem(material.name() + "_crackhammer", new Supplier<Item>() { @Override public Item get() { return new CrackhammerItem(material, toolProperties()); } }));
        items.put("scythe", registerItem(material.name() + "_scythe", new Supplier<Item>() { @Override public Item get() { return new ScytheItem(material, toolProperties()); } }));
        items.put("shears", registerItem(material.name() + "_shears", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shears(material, toolProperties()); } }));
        items.put("fishing_rod", registerItem(material.name() + "_fishing_rod", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.FishingRod(material, toolProperties()); } }));
        items.put("bow", registerItem(material.name() + "_bow", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Bow(material, combatProperties()); } }));
        items.put("crossbow", registerItem(material.name() + "_crossbow", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Crossbow(material, combatProperties()); } }));
        items.put("shield", registerItem(material.name() + "_shield", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shield(material, combatProperties()); } }));
        items.put("arrow", registerAmmo(material, BaseMetalAmmoItem.Kind.ARROW));
        items.put("bolt", registerAmmo(material, BaseMetalAmmoItem.Kind.BOLT));
        items.put("rod", registerSimpleMaterialItem(material.name() + "_rod", material));
        items.put("gear", registerSimpleMaterialItem(material.name() + "_gear", material));
        items.put("horse_armor", registerItem(material.name() + "_horse_armor", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.HorseArmor(material, combatProperties()); } }));
        items.put("helmet", registerArmor(material, EquipmentSlotType.HEAD));
        items.put("chestplate", registerArmor(material, EquipmentSlotType.CHEST));
        items.put("leggings", registerArmor(material, EquipmentSlotType.LEGS));
        items.put("boots", registerArmor(material, EquipmentSlotType.FEET));
    }

    private static RegistryHandle<Item> registerAmmo(final MaterialDefinition material, final BaseMetalAmmoItem.Kind kind) {
        String suffix = kind == BaseMetalAmmoItem.Kind.ARROW ? "_arrow" : "_bolt";
        return registerItem(material.name() + suffix, new Supplier<Item>() {
            @Override public Item get() {
                return new BaseMetalAmmoItem(material, kind,
                        kind == BaseMetalAmmoItem.Kind.ARROW ? ModEntities.CUSTOM_ARROW : ModEntities.CUSTOM_BOLT,
                        combatProperties());
            }
        });
    }

    private static RegistryHandle<Item> registerArmor(final MaterialDefinition material,
            final EquipmentSlotType slot) {
        String suffix;
        switch (slot) {
            case HEAD: suffix = "helmet"; break;
            case CHEST: suffix = "chestplate"; break;
            case LEGS: suffix = "leggings"; break;
            case FEET: suffix = "boots"; break;
            default: throw new IllegalArgumentException("Not armor: " + slot);
        }
        return registerItem(material.name() + "_" + suffix, new Supplier<Item>() {
            @Override public Item get() { return new MaterialItems.Armor(material, slot, combatProperties()); }
        });
    }

    private static void registerVanillaBits() {
        final Map<String, MaterialDefinition> vanilla = vanillaDefinitions();
        final RegistryHandle<Block> charcoalBlock = registerBlock("charcoal_block", new Supplier<Block>() {
            @Override public Block get() { return new HarvestBlock(Block.Properties.create(Material.ROCK)
                    .sound(SoundType.SAND).hardnessAndResistance(5.0F), 0); }
        }, false);
        registerItem("charcoal_block", new Supplier<Item>() { @Override public Item get() {
            return new MaterialItems.BurnableBlock(charcoalBlock.get(), 16000,
                    new Item.Properties().group(ModTabs.BLOCKS));
        } });
        registerVanillaDecorative("diamond", vanilla.get("diamond"), list("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("emerald", vanilla.get("emerald"), list("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("gold", vanilla.get("gold"), list("bars", "door", "trapdoor", "button", "slab", "lever", "stairs", "wall"));
        registerVanillaDecorative("obsidian", vanilla.get("obsidian"), list("bars", "door", "trapdoor", "button", "slab", "lever", "pressure_plate", "stairs", "wall"));
        registerVanillaDecorative("iron", vanilla.get("iron"), list("button", "slab", "lever", "stairs", "wall"));
        registerVanillaDecorative("quartz", vanilla.get("quartz"), list("bars", "door", "trapdoor", "button", "lever", "pressure_plate", "wall"));
        registerPlate("gold_plate", Blocks.GOLD_BLOCK, vanilla.get("gold"));
        registerPlate("iron_plate", Blocks.IRON_BLOCK, vanilla.get("iron"));

        for (String name : list("diamond", "emerald", "gold", "iron", "obsidian", "quartz")) registerVanillaCombatAndUtility(vanilla.get(name));
        for (String name : list("diamond", "emerald", "gold", "iron", "obsidian", "quartz", "stone", "wood")) {
            final MaterialDefinition material = vanilla.get(name);
            registerItem(name + "_crackhammer", new Supplier<Item>() { @Override public Item get() { return new CrackhammerItem(material, toolProperties()); } });
            registerSimpleMaterialItem(name + "_gear", material);
            registerItem(name + "_scythe", new Supplier<Item>() { @Override public Item get() { return new ScytheItem(material, toolProperties()); } });
        }
        for (String name : list("coal", "charcoal", "diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            registerSimpleMaterialItem(name + "_powder", vanilla.get(name));
            registerSimpleMaterialItem(name + "_smallpowder", vanilla.get(name));
        }
        registerSimpleMaterialItem("redstone_smallpowder", vanilla.get("redstone"));
        for (String name : list("coal", "charcoal", "diamond", "emerald", "obsidian", "quartz")) registerSimpleMaterialItem(name + "_nugget", vanilla.get(name));
        for (String name : list("diamond", "gold", "iron")) {
            registerSimpleMaterialItem(name + "_blend", vanilla.get(name));
            registerSimpleMaterialItem(name + "_smallblend", vanilla.get(name));
        }
        registerSimpleMaterialItem("lapis_smallpowder", vanilla.get("lapis"));
        registerSimpleMaterialItem("obsidian_ingot", vanilla.get("obsidian"));
        registerSimpleMaterialItem("redstone_ingot", vanilla.get("redstone"));
        registerSimpleMaterialItem("stone_rod", vanilla.get("stone"));
    }

    private static void registerPlate(String id, final Block base, final MaterialDefinition material) {
        registerBlock(id, new Supplier<Block>() { @Override public Block get() {
            return new PlateBlock(Block.Properties.from(base).variableOpacity(), material.requiredHarvestLevel());
        } }, true);
    }

    private static void registerVanillaDecorative(final String name, final MaterialDefinition material,
            List<String> forms) {
        for (String form : forms) {
            final String targetForm = form;
            registerMaterialBlock(name + "_" + form, form, material, true,
                    new Supplier<Block.Properties>() { @Override public Block.Properties get() { return vanillaDecorativeProperties(name, targetForm, material); } },
                    new Supplier<Block>() { @Override public Block get() { return vanillaStorageBlock(name); } });
        }
        if (forms.contains("slab")) {
            String id = "double_" + name + "_slab";
            registerBlock(id, new Supplier<Block>() { @Override public Block get() {
                return new CompatibilityDoubleSlabBlock(vanillaDecorativeProperties(name, "slab", material), material.requiredHarvestLevel());
            } }, false);
            HIDDEN_BLOCKS.add(id);
        }
    }

    private static Block.Properties vanillaDecorativeProperties(String name, String form,
            MaterialDefinition material) {
        boolean gem = "diamond".equals(name) || "emerald".equals(name) || "obsidian".equals(name) || "quartz".equals(name);
        boolean lockedGemDoor = gem && !"quartz".equals(name) && ("door".equals(form) || "trapdoor".equals(form));
        Material blockMaterial = "gold".equals(name) || "iron".equals(name) || lockedGemDoor ? Material.IRON : Material.ROCK;
        return Block.Properties.create(blockMaterial).sound(gem ? SoundType.GLASS : SoundType.METAL)
                .hardnessAndResistance(material.blockHardness(), material.blastResistance());
    }

    private static Block vanillaStorageBlock(String name) {
        if ("diamond".equals(name)) return Blocks.DIAMOND_BLOCK;
        if ("emerald".equals(name)) return Blocks.EMERALD_BLOCK;
        if ("gold".equals(name)) return Blocks.GOLD_BLOCK;
        if ("iron".equals(name)) return Blocks.IRON_BLOCK;
        if ("obsidian".equals(name)) return Blocks.OBSIDIAN;
        if ("quartz".equals(name)) return Blocks.QUARTZ_BLOCK;
        throw new IllegalArgumentException("No vanilla storage block for " + name);
    }

    private static void registerVanillaCombatAndUtility(final MaterialDefinition material) {
        String name = material.name();
        registerAmmo(material, BaseMetalAmmoItem.Kind.ARROW);
        registerAmmo(material, BaseMetalAmmoItem.Kind.BOLT);
        registerItem(name + "_bow", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Bow(material, combatProperties()); } });
        registerItem(name + "_crossbow", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Crossbow(material, combatProperties()); } });
        registerItem(name + "_fishing_rod", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.FishingRod(material, toolProperties()); } });
        registerSimpleMaterialItem(name + "_rod", material);
        if (!"iron".equals(name)) registerItem(name + "_shears", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shears(material, toolProperties()); } });
        registerItem(name + "_shield", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shield(material, combatProperties()); } });
        if ("emerald".equals(name) || "obsidian".equals(name) || "quartz".equals(name)) {
            registerItem(name + "_axe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Axe(material, toolProperties()); } });
            registerItem(name + "_pickaxe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Pickaxe(material, toolProperties()); } });
            registerItem(name + "_shovel", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Shovel(material, toolProperties()); } });
            registerItem(name + "_hoe", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Hoe(material, toolProperties()); } });
            registerArmor(material, EquipmentSlotType.HEAD);
            registerArmor(material, EquipmentSlotType.CHEST);
            registerArmor(material, EquipmentSlotType.LEGS);
            registerArmor(material, EquipmentSlotType.FEET);
            registerItem(name + "_horse_armor", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.HorseArmor(material, combatProperties()); } });
            registerItem(name + "_sword", new Supplier<Item>() { @Override public Item get() { return new MaterialItems.Sword(material, combatProperties()); } });
        }
    }

    private static Map<String, MaterialDefinition> vanillaDefinitions() {
        Map<String, MaterialDefinition> map = new LinkedHashMap<String, MaterialDefinition>();
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

    private static void registerFluid(final String name, int colour, final boolean mercury) {
        final RegistryHandle<FlowingFluid> source = new RegistryHandle<FlowingFluid>(name);
        final RegistryHandle<FlowingFluid> flowing = new RegistryHandle<FlowingFluid>("flowing_" + name);
        final RegistryHandle<FlowingFluidBlock> block = new RegistryHandle<FlowingFluidBlock>(name);
        final RegistryHandle<Item> bucket = new RegistryHandle<Item>(name + "_bucket");
        FLUID_CONTENT.put(name, new FluidContent(source, flowing, block, bucket));
        FLUID_COLOURS.put(name, Integer.valueOf(colour));
        RegistryHandle<Block> blockHandle = registerBlock(name, new Supplier<Block>() { @Override public Block get() {
            MoltenMetalBlock value = new MoltenMetalBlock(source.get(),
                    Block.Properties.create(Material.LAVA).doesNotBlockMovement()
                            .hardnessAndResistance(100.0F).variableOpacity(), mercury);
            block.bind(value);
            return value;
        } }, false);
        registerItem(name, new Supplier<Item>() { @Override public Item get() {
            return new BlockItem(blockHandle.get(), new Item.Properties());
        } });
        registerItem(name + "_bucket", new Supplier<Item>() { @Override public Item get() {
            BaseMetalBucketItem value = new BaseMetalBucketItem(source.get(),
                    new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(ModTabs.ITEMS));
            bucket.bind(value);
            return value;
        } });
    }

    private static RegistryHandle<Block> registerAnvil(String id, final Block.Properties properties) {
        return registerBlock(id, new Supplier<Block>() { @Override public Block get() { return new BaseMetalAnvilBlock(properties); } }, true);
    }

    private static RegistryHandle<Block> registerBlock(String id, Supplier<? extends Block> factory, boolean hasItem) {
        if (BLOCKS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate block " + id);
        final RegistryHandle<Block> handle = new RegistryHandle<Block>(id);
        BLOCKS_BY_ID.put(id, handle);
        BLOCK_FACTORIES.put(id, factory);
        if (hasItem) registerItem(id, new Supplier<Item>() { @Override public Item get() {
            return new BlockItem(handle.get(), new Item.Properties().group(ModTabs.BLOCKS));
        } });
        return handle;
    }

    private static RegistryHandle<Item> registerSimpleMaterialItem(final String id,
            final MaterialDefinition material) {
        return registerItem(id, new Supplier<Item>() { @Override public Item get() {
            return new MaterialItems.Basic(material, burnTime(id), new Item.Properties().group(ModTabs.ITEMS));
        } });
    }

    private static int burnTime(String id) {
        if ("charcoal_block".equals(id)) return 16000;
        if ("wood_gear".equals(id)) return 300;
        int separator = id.lastIndexOf('_');
        if (separator < 0) return -1;
        String material = id.substring(0, separator);
        if (!"coal".equals(material) && !"charcoal".equals(material)) return -1;
        if (id.endsWith("_powder")) return 1600;
        if (id.endsWith("_nugget") || id.endsWith("_smallpowder")) return 200;
        return -1;
    }

    private static RegistryHandle<Item> registerItem(String id, Supplier<? extends Item> factory) {
        if (ITEMS_BY_ID.containsKey(id)) throw new IllegalStateException("Duplicate item " + id);
        RegistryHandle<Item> handle = new RegistryHandle<Item>(id);
        ITEMS_BY_ID.put(id, handle);
        ITEM_FACTORIES.put(id, factory);
        return handle;
    }

    private static Item.Properties toolProperties() { return new Item.Properties().group(ModTabs.TOOLS); }
    private static Item.Properties combatProperties() { return new Item.Properties().group(ModTabs.COMBAT); }
    private static Block.Properties metalProperties(MaterialDefinition material) {
        return Block.Properties.create(Material.IRON).sound(SoundType.METAL)
                .hardnessAndResistance(material.blockHardness(), material.blastResistance());
    }
    private static Block.Properties oreProperties(MaterialDefinition material) {
        return Block.Properties.create(Material.ROCK).sound(SoundType.STONE)
                .hardnessAndResistance(material.oreHardness(), material.blastResistance());
    }
    private static ResourceLocation id(String path) { return new ResourceLocation(BaseMetals.MOD_ID, path); }
    private static List<String> list(String... values) {
        return Collections.unmodifiableList(Arrays.asList(values));
    }

    private static class HarvestBlock extends Block {
        private final int level;
        HarvestBlock(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }

    private static class HarvestOre extends HarvestBlock {
        HarvestOre(Properties properties, int level) { super(properties, level); }

        // Cutout rendering leaves the transparent parts of the ore overlay clear.
        @Override public BlockRenderLayer getRenderLayer() { return BlockRenderLayer.CUTOUT_MIPPED; }
    }
    private static class HarvestPane extends PaneBlock {
        private final int level;
        HarvestPane(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestDoor extends DoorBlock {
        private final int level;
        HarvestDoor(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestTrapDoor extends TrapDoorBlock {
        private final int level;
        HarvestTrapDoor(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestButton extends StoneButtonBlock {
        private final int level;
        HarvestButton(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestSlab extends SlabBlock {
        private final int level;
        HarvestSlab(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestLever extends LeverBlock {
        private final int level;
        HarvestLever(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestPressurePlate extends PressurePlateBlock {
        private final int level;
        HarvestPressurePlate(Properties properties, int level) { super(Sensitivity.MOBS, properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestStairs extends StairsBlock {
        private final int level;
        HarvestStairs(net.minecraft.block.BlockState base, Properties properties, int level) { super(base, properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
    private static class HarvestWall extends WallBlock {
        private final int level;
        HarvestWall(Properties properties, int level) { super(properties); this.level = level; }
        @Override public ToolType getHarvestTool(net.minecraft.block.BlockState state) { return ToolType.PICKAXE; }
        @Override public int getHarvestLevel(net.minecraft.block.BlockState state) { return level; }
    }
}
