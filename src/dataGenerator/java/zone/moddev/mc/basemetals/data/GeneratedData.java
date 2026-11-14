package zone.moddev.mc.basemetals.data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.content.ModContent;
import zone.moddev.mc.basemetals.material.MaterialCatalogue;
import zone.moddev.mc.basemetals.material.MaterialDefinition;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;

/** Small catalogue-driven generator for server data and compatibility manifests. */
final class GeneratedData implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final List<String> LOCALES = List.of("de_de", "en_au", "en_ca", "en_en", "en_gb",
            "en_pt", "en_us", "es_es", "es_mx", "fr_ca", "fr_fr", "it_it", "ja_jp", "nl_nl",
            "pt_br", "ru_ru", "zh_cn", "zh_tw");
    private static final List<String> ADVANCEMENTS = List.of("this_is_new", "blocktastic", "geologist",
            "metallurgy", "brass_maker", "bronze_maker", "cupronickel_maker", "electrum_maker",
            "steel_maker", "invar_maker", "mithril_maker", "aquarium_maker", "pewter_maker",
            "angel_of_death", "scuba_diver", "demon_slayer", "juggernaut", "moon_boots");
    private static final List<String> PROCESSING_MATERIALS = List.of(
            "adamantine", "antimony", "bismuth", "coldiron",
            "nickel", "platinum", "starsteel", "zinc");
    // MMDLib only plans these forms when Mekanism is installed.
    private static final List<String> MEKANISM_FORMS = List.of("crystals", "shards", "clumps", "dirty_dusts");
    private static final String WOODEN_ROD = "#c:rods/wooden";
    /** Vanilla materials whose extensions include equipment and decorative blocks. */
    private static final List<String> VANILLA_EQUIPMENT = List.of(
            "copper", "diamond", "emerald", "gold", "iron", "obsidian", "quartz");
    private static final Map<String, List<AlloyComponent>> ALLOYS = Map.ofEntries(
            Map.entry("aquarium", List.of(component("copper", 2), component("zinc", 1),
                    new AlloyComponent("#c:dusts/prismarine", 1))),
            Map.entry("brass", List.of(component("copper", 3), component("zinc", 1))),
            Map.entry("bronze", List.of(component("copper", 3), component("tin", 1))),
            Map.entry("cupronickel", List.of(component("copper", 3), component("nickel", 1))),
            Map.entry("electrum", List.of(component("gold", 1), component("silver", 1))),
            Map.entry("invar", List.of(component("iron", 2), component("nickel", 1))),
            Map.entry("mithril", List.of(component("silver", 2), component("coldiron", 1),
                    new AlloyComponent("#c:ingots/mercury", 1))),
            Map.entry("pewter", List.of(component("tin", 1), component("copper", 1), component("lead", 1))),
            Map.entry("steel", List.of(component("iron", 8), component("coal", 1))));
    private static final Map<String, Integer> ALLOY_OUTPUTS = Map.of(
            "aquarium", 3, "brass", 4, "bronze", 4, "cupronickel", 4,
            "electrum", 2, "invar", 3, "mithril", 3, "pewter", 3, "steel", 8);

    private final Path output;
    private final Path legacyReference;
    private final List<java.util.concurrent.CompletableFuture<?>> pendingWrites = new ArrayList<>();

    GeneratedData(net.minecraft.data.PackOutput generator) {
        this.output = generator.getOutputFolder();
        this.legacyReference = Path.of(System.getProperty("basemetals.projectDir")).resolve("reference/1.12");
    }

    @Override
    public java.util.concurrent.CompletableFuture<?> run(CachedOutput cache) {
        pendingWrites.clear();
        try {
            writeFluidBlockstates(cache);
            writeLoot(cache);
            writeTags(cache);
            writeRecipes(cache);
            writeRawProcessing(cache);
            writeAdvancements(cache);
            writeChestLoot(cache);
            writeLanguages(cache);
            writeManifest(cache);
        } catch (IOException exception) {
            return java.util.concurrent.CompletableFuture.failedFuture(exception);
        }
        return java.util.concurrent.CompletableFuture.allOf(
                pendingWrites.toArray(java.util.concurrent.CompletableFuture[]::new));
    }

    private void writeFluidBlockstates(CachedOutput cache) throws IOException {
        for (String name : ModContent.fluids().keySet()) {
            JsonObject custom = new JsonObject();
            custom.addProperty("fluid", BaseMetals.MOD_ID + ":" + name);
            JsonObject model = new JsonObject();
            // LiquidBlockRenderer supplies the fluid surface; the vanilla water
            // model is the canonical particle-only fallback for a liquid block.
            model.addProperty("model", "minecraft:block/water");
            model.add("custom", custom);
            JsonObject variants = new JsonObject();
            variants.add("", model);
            JsonObject root = new JsonObject();
            root.add("variants", variants);
            save(cache, root, "assets/basemetals/blockstates/" + name + ".json");
        }
    }

    private void writeLoot(CachedOutput cache) throws IOException {
        for (String id : ModContent.blocksById().keySet()) {
            if (ModContent.fluids().containsKey(id)) continue;
            if (id.endsWith("_ore")) {
                writeOreLoot(cache, id);
                continue;
            }
            String drop = id;
            boolean compatibilitySlab = id.endsWith("_double_slab");
            if (compatibilitySlab) drop = id.substring(0, id.length() - "_double_slab".length()) + "_slab";
            JsonObject entry = object("type", "minecraft:item", "name", "basemetals:" + drop);
            JsonArray functions = new JsonArray();
            if (compatibilitySlab) {
                functions.add(object("function", "minecraft:set_count", "count", 2));
            } else if (ModContent.blocksById().get(id).get() instanceof net.minecraft.world.level.block.SlabBlock) {
                JsonObject doubleCondition = object("condition", "minecraft:block_state_property",
                        "block", "basemetals:" + id);
                doubleCondition.add("properties", object("type", "double"));
                JsonArray conditions = new JsonArray(); conditions.add(doubleCondition);
                JsonObject count = object("function", "minecraft:set_count", "count", 2);
                count.add("conditions", conditions);
                functions.add(count);
            } else if (ModContent.blocksById().get(id).get()
                    instanceof zone.moddev.mc.lib.common.runtime.MaterialAnvilBlock) {
                JsonObject copyDamage = object("function", "minecraft:copy_state",
                        "block", "basemetals:" + id);
                copyDamage.add("properties", array("damage"));
                functions.add(copyDamage);
            }
            entry.add("functions", functions);
            JsonObject pool = new JsonObject();
            pool.addProperty("rolls", 1);
            JsonArray entries = new JsonArray(); entries.add(entry); pool.add("entries", entries);
            JsonArray conditions = new JsonArray(); conditions.add(object("condition", "minecraft:survives_explosion"));
            if (ModContent.blocksById().get(id).get() instanceof net.minecraft.world.level.block.DoorBlock) {
                JsonObject lowerHalf = object("condition", "minecraft:block_state_property",
                        "block", "basemetals:" + id);
                lowerHalf.add("properties", object("half", "lower"));
                conditions.add(lowerHalf);
            }
            pool.add("conditions", conditions);
            JsonObject root = object("type", "minecraft:block");
            JsonArray pools = new JsonArray(); pools.add(pool); root.add("pools", pools);
            save(cache, root, "data/basemetals/loot_table/blocks/" + id + ".json");
        }
    }

    private void writeTags(CachedOutput cache) throws IOException {
        List<String> allBlockDrops = new ArrayList<>();
        for (String id : ModContent.blocksById().keySet()) {
            if (!ModContent.fluids().containsKey(id)) allBlockDrops.add("basemetals:" + id);
        }
        tag(cache, "minecraft", "blocks/mineable/pickaxe", allBlockDrops);
        List<String> beaconBases = MaterialCatalogue.all().stream()
                .filter(MaterialDefinition::hasEquipment)
                .map(material -> "basemetals:" + material.name() + "_block")
                .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
        beaconBases.add("basemetals:charcoal_block");
        tag(cache, "minecraft", "blocks/beacon_base_blocks", beaconBases);
        writeShapeTags(cache);
        writeEquipmentTags(cache);
        writeOreConventionTags(cache);
        // MMDLib's scythe and crackhammer read these tags; add Base Metals blocks to them.
        tag(cache, "mmdlib", "blocks/scythe_harvestable", scytheHarvestableBlocks());
        tag(cache, "mmdlib", "blocks/crackhammer_crushable", crackhammerCrushableBlocks());
        writeHarvestTierTags(cache);
        tag(cache, "forge", "blocks/netherrack", List.of("minecraft:netherrack"));
        tag(cache, "forge", "blocks/end_stones", List.of("minecraft:end_stone"));
        // Bolts are arrows to MMDLib's crossbow, as they are in MMDLib's own tag.
        tag(cache, "minecraft", "items/arrows", java.util.stream.Stream.concat(
                MaterialCatalogue.all().stream().filter(MaterialDefinition::hasEquipment).map(MaterialDefinition::name),
                VANILLA_EQUIPMENT.stream())
                .flatMap(name -> java.util.stream.Stream.of(
                        "basemetals:" + name + "_arrow", "basemetals:" + name + "_bolt"))
                .toList());

        Map<String, List<String>> aggregateItems = new LinkedHashMap<>();
        Map<String, List<String>> aggregateBlocks = new LinkedHashMap<>();
        for (MaterialDefinition material : MaterialCatalogue.all()) {
            String name = material.name();
            itemTag(cache, "ingots", name, entries(name, "ingot"));
            itemTag(cache, "nuggets", name, entries(name, "nugget"));
            itemTag(cache, "dusts", name, entries(name, "powder"));
            itemTag(cache, "tiny_dusts", name, entries(name, "smallpowder"));
            if (material.hasEquipment()) {
                itemTag(cache, "plates", name, entries(name, "plate"));
                itemTag(cache, "rods", name, entries(name, "rod"));
                itemTag(cache, "gears", name, entries(name, "gear"));
                blockTag(cache, "storage_blocks", name, entries(name, "block"));
                itemTag(cache, "storage_blocks", name, entries(name, "block"));
            }
            if (material.hasOre()) {
                blockTag(cache, "ores", name, entries(name, "ore"));
                itemTag(cache, "ores", name, entries(name, "ore"));
            }
            if (material.isAlloy()) {
                itemTag(cache, "blends", name, entries(name, "blend"));
                itemTag(cache, "small_blends", name, entries(name, "smallblend"));
            }
            if (material.processingForms()) {
                for (String form : List.of("casings", "dense_plates", "crushed_ores", "purified_ores",
                        "crystals", "shards", "clumps", "dirty_dusts")) {
                    String itemSuffix = switch (form) {
                        case "casings" -> "casing";
                        case "dense_plates" -> "dense_plate";
                        case "crushed_ores" -> "crushed";
                        case "purified_ores" -> "crushed_purified";
                        case "dirty_dusts" -> "powder_dirty";
                        default -> form.substring(0, form.length() - 1);
                    };
                    if (MEKANISM_FORMS.contains(form)) {
                        optionalTag(cache, "c", "item/" + form + "/" + name, entries(name, itemSuffix));
                    } else {
                        itemTag(cache, form, name, entries(name, itemSuffix));
                    }
                }
            }
            addAggregate(aggregateItems, "ingots", "#c:ingots/" + name);
            addAggregate(aggregateItems, "nuggets", "#c:nuggets/" + name);
            addAggregate(aggregateItems, "dusts", "#c:dusts/" + name);
            addAggregate(aggregateItems, "tiny_dusts", "#c:tiny_dusts/" + name);
            if (material.hasEquipment()) {
                addAggregate(aggregateItems, "plates", "#c:plates/" + name);
                addAggregate(aggregateItems, "rods", "#c:rods/" + name);
                addAggregate(aggregateItems, "gears", "#c:gears/" + name);
                addAggregate(aggregateItems, "storage_blocks", "#c:storage_blocks/" + name);
                addAggregate(aggregateBlocks, "storage_blocks", "#c:storage_blocks/" + name);
            }
            if (material.hasOre()) {
                addAggregate(aggregateItems, "ores", "#c:ores/" + name);
                addAggregate(aggregateBlocks, "ores", "#c:ores/" + name);
            }
        }
        for (String name : List.of("copper", "diamond", "emerald", "gold", "iron", "obsidian", "quartz", "stone", "wood")) {
            addAggregate(aggregateItems, "gears", "#c:gears/" + name);
        }
        for (String name : List.of("copper", "diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            addAggregate(aggregateItems, "rods", "#c:rods/" + name);
        }
        for (String name : List.of("copper", "diamond", "emerald", "gold", "iron")) {
            addAggregate(aggregateItems, "plates", "#c:plates/" + name);
        }
        // Copper's vanilla tags stay in the aggregates, as they were when copper was a catalogue metal.
        for (String family : List.of("ingots", "nuggets", "dusts", "tiny_dusts", "storage_blocks", "ores")) {
            addAggregate(aggregateItems, family, "#c:" + family + "/copper");
        }
        for (String family : List.of("storage_blocks", "ores")) {
            addAggregate(aggregateBlocks, family, "#c:" + family + "/copper");
        }
        aggregateItems.forEach((path, values) -> uncheckedTag(cache, "forge", "items/" + path, values));
        aggregateBlocks.forEach((path, values) -> uncheckedTag(cache, "forge", "blocks/" + path, values));

        for (String alias : List.of("adamant", "adamantite", "adamantium")) {
            tag(cache, "forge", "items/ingots/" + alias, List.of("#c:ingots/adamantine"));
            tag(cache, "forge", "items/nuggets/" + alias, List.of("#c:nuggets/adamantine"));
            tag(cache, "forge", "items/dusts/" + alias, List.of("#c:dusts/adamantine"));
        }
        tag(cache, "forge", "items/ingots/quicksilver", List.of("#c:ingots/mercury"));
        tag(cache, "forge", "items/gears/steel", List.of("basemetals:steel_gear"));
        tag(cache, "forge", "items/sprockets/steel", List.of("#c:gears/steel"));
        tag(cache, "forge", "items/plates/diamond", List.of());
        tag(cache, "forge", "items/plates/emerald", List.of());
        writeVanillaBitsTags(cache);
        writeCompatibilityTags(cache);
    }

    /** Tags for the vanilla material extensions (1.12's Vanilla Bits). */
    private void writeVanillaBitsTags(CachedOutput cache) throws IOException {
        Map<String, String> vanillaIngot = Map.of(
                "copper", "minecraft:copper_ingot", "diamond", "minecraft:diamond", "emerald", "minecraft:emerald",
                "gold", "minecraft:gold_ingot", "iron", "minecraft:iron_ingot",
                "quartz", "minecraft:quartz", "obsidian", "basemetals:obsidian_ingot");
        Map<String, String> vanillaBlock = Map.of(
                "copper", "minecraft:copper_block", "diamond", "minecraft:diamond_block", "emerald", "minecraft:emerald_block",
                "gold", "minecraft:gold_block", "iron", "minecraft:iron_block",
                "quartz", "minecraft:quartz_block", "obsidian", "minecraft:obsidian");
        for (String material : VANILLA_EQUIPMENT) {
            if (hasItem(material + "_rod")) {
                tag(cache, "forge", "items/rods/" + material, List.of("basemetals:" + material + "_rod"));
            }
            if (hasItem(material + "_gear")) {
                tag(cache, "forge", "items/gears/" + material, List.of("basemetals:" + material + "_gear"));
            }
            tag(cache, "forge", "items/materials/" + material, List.of(vanillaIngot.get(material)));
            tag(cache, "forge", "items/storage_blocks/" + material, List.of(vanillaBlock.get(material)));
            tag(cache, "forge", "blocks/storage_blocks/" + material, List.of(vanillaBlock.get(material)));
        }
        tag(cache, "forge", "items/rods/stone", List.of("basemetals:stone_rod"));
        for (String material : List.of("wood", "stone")) {
            tag(cache, "forge", "items/gears/" + material, List.of("basemetals:" + material + "_gear"));
        }
        for (String material : List.of("coal", "charcoal", "copper", "diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            tag(cache, "forge", "items/dusts/" + material, List.of("basemetals:" + material + "_powder"));
            tag(cache, "forge", "items/tiny_dusts/" + material, List.of("basemetals:" + material + "_smallpowder"));
        }
        tag(cache, "forge", "items/tiny_dusts/redstone", List.of("basemetals:redstone_smallpowder"));
        tag(cache, "forge", "items/tiny_dusts/lapis", List.of("basemetals:lapis_smallpowder"));
        tag(cache, "forge", "items/dusts/redstone", List.of("minecraft:redstone"));
        tag(cache, "forge", "items/dusts/lapis", List.of("minecraft:lapis_lazuli"));
        tag(cache, "forge", "items/dusts/prismarine", List.of("minecraft:prismarine_shard"));
        for (String material : List.of("coal", "charcoal", "copper", "diamond", "emerald", "obsidian", "quartz")) {
            tag(cache, "forge", "items/nuggets/" + material, List.of("basemetals:" + material + "_nugget"));
        }
        tag(cache, "forge", "items/ingots/obsidian", List.of("basemetals:obsidian_ingot"));
        tag(cache, "forge", "items/ingots/redstone", List.of("basemetals:redstone_ingot"));
        tag(cache, "forge", "items/plates/gold", List.of("basemetals:gold_plate"));
        tag(cache, "forge", "items/plates/iron", List.of("basemetals:iron_plate"));
        tag(cache, "forge", "items/plates/copper", List.of("basemetals:copper_plate"));
        for (String material : List.of("diamond", "gold", "iron")) {
            tag(cache, "forge", "items/blends/" + material, List.of("basemetals:" + material + "_blend"));
            tag(cache, "forge", "items/small_blends/" + material, List.of("basemetals:" + material + "_smallblend"));
        }
        tag(cache, "forge", "items/storage_blocks/charcoal", List.of("basemetals:charcoal_block"));
        tag(cache, "forge", "blocks/storage_blocks/charcoal", List.of("basemetals:charcoal_block"));
    }

    private static List<String> scytheHarvestableBlocks() {
        LinkedHashSet<String> values = new LinkedHashSet<>(List.of(
                "#minecraft:leaves", "#minecraft:saplings", "#minecraft:flowers", "#minecraft:crops"));
        net.minecraft.core.registries.BuiltInRegistries.BLOCK.stream()
                .filter(block -> block instanceof net.minecraft.world.level.block.BushBlock
                        || block instanceof net.minecraft.world.level.block.LeavesBlock
                        || block instanceof net.minecraft.world.level.block.WebBlock
                        || block instanceof net.minecraft.world.level.block.CactusBlock
                        || block instanceof net.minecraft.world.level.block.BambooStalkBlock
                        || block instanceof net.minecraft.world.level.block.BambooSaplingBlock
                        || block instanceof net.minecraft.world.level.block.WaterlilyBlock
                        || block instanceof net.minecraft.world.level.block.GrowingPlantBlock
                        || block == net.minecraft.world.level.block.Blocks.MOSS_BLOCK
                        || block == net.minecraft.world.level.block.Blocks.MOSS_CARPET
                        || block == net.minecraft.world.level.block.Blocks.MELON
                        || block == net.minecraft.world.level.block.Blocks.PUMPKIN
                        || block == net.minecraft.world.level.block.Blocks.CARVED_PUMPKIN
                        || block == net.minecraft.world.level.block.Blocks.JACK_O_LANTERN)
                .map(net.minecraft.core.registries.BuiltInRegistries.BLOCK::getKey)
                .filter(java.util.Objects::nonNull)
                .map(ResourceLocation::toString)
                .forEach(values::add);
        return List.copyOf(values);
    }

    private static List<String> crackhammerCrushableBlocks() {
        LinkedHashSet<String> values = new LinkedHashSet<>();
        MaterialCatalogue.all().stream().filter(MaterialDefinition::hasOre)
                .map(material -> "#c:ores/" + material.name()).forEach(values::add);
        MaterialCatalogue.all().stream().filter(MaterialDefinition::hasEquipment)
                .map(material -> "#c:storage_blocks/" + material.name()).forEach(values::add);
        for (String name : List.of("coal", "copper", "diamond", "emerald", "gold", "iron",
                "lapis", "redstone", "quartz", "netherite_scrap")) {
            values.add("#c:ores/" + name);
        }
        for (String name : List.of("coal", "charcoal", "copper", "diamond", "emerald", "gold",
                "iron", "lapis", "redstone")) {
            values.add("#c:storage_blocks/" + name);
        }
        values.addAll(List.of(
                "#c:stones", "#c:cobblestones", "#c:gravels",
                "minecraft:stone_bricks", "minecraft:stone_slab", "minecraft:stone_brick_slab",
                "minecraft:mossy_cobblestone", "minecraft:cobblestone_wall",
                "minecraft:mossy_cobblestone_wall", "minecraft:sandstone",
                "minecraft:sandstone_slab", "minecraft:glass", "minecraft:red_sandstone",
                "minecraft:red_sandstone_slab", "minecraft:glowstone", "minecraft:obsidian",
                "minecraft:sugar_cane", "minecraft:bone_block", "minecraft:quartz_block",
                "minecraft:quartz_slab", "minecraft:prismarine", "minecraft:prismarine_bricks",
                "minecraft:dark_prismarine", "minecraft:sea_lantern", "minecraft:slime_block",
                "minecraft:smooth_stone", "minecraft:bricks", "minecraft:nether_bricks"));
        return List.copyOf(values);
    }

    private void writeHarvestTierTags(CachedOutput cache) throws IOException {
        Map<Integer, LinkedHashSet<String>> byTier = new LinkedHashMap<>();
        byTier.put(1, new LinkedHashSet<>());
        byTier.put(2, new LinkedHashSet<>());
        byTier.put(3, new LinkedHashSet<>());
        ModContent.materials().values().forEach(content -> {
            int level = content.definition().requiredHarvestLevel();
            if (level < 1) return;
            content.blocks().values().stream()
                    .map(net.neoforged.neoforge.registries.DeferredHolder::getId)
                    .map(ResourceLocation::toString)
                    .forEach(byTier.get(level)::add);
        });
        Map<String, Integer> vanillaLevels = Map.of(
                "diamond", 3, "emerald", 3, "obsidian", 3,
                "iron", 2, "copper", 1, "quartz", 1);
        vanillaLevels.forEach((name, level) -> ModContent.blocksById().keySet().stream()
                .filter(id -> id.startsWith(name + "_"))
                .map(id -> "basemetals:" + id)
                .forEach(byTier.get(level)::add));
        byTier.get(1).add("basemetals:charcoal_block");
        byTier.get(1).add("basemetals:stone_anvil");
        byTier.get(2).add("basemetals:steel_anvil");
        byTier.get(3).add("basemetals:adamantine_anvil");
        // The registry-backed maps above do not promise iteration order. Sort
        // these generated tags so runData is byte-for-byte reproducible.
        tag(cache, "minecraft", "blocks/needs_stone_tool",
                byTier.get(1).stream().sorted().toList());
        tag(cache, "minecraft", "blocks/needs_iron_tool",
                byTier.get(2).stream().sorted().toList());
        tag(cache, "minecraft", "blocks/needs_diamond_tool",
                byTier.get(3).stream().sorted().toList());
    }

    /**
     * Vanilla and NeoForge convention tags that follow from what a block or item is.
     * Vanilla behaviour depends on several of them: fences and walls connect through
     * minecraft:fences and minecraft:walls, ladders climb through minecraft:climbable,
     * and mobs and other mods find doors, plates and so on by tag.
     */
    private void writeShapeTags(CachedOutput cache) throws IOException {
        List<String> hiddenBlocks = ModContent.hiddenBlocks();
        Map<String, List<String>> blockTags = new LinkedHashMap<>();
        Map<String, List<String>> itemTags = new LinkedHashMap<>();
        ModContent.blocksById().forEach((id, holder) -> {
            if (ModContent.fluids().containsKey(id)) return;
            String entry = "basemetals:" + id;
            Block block = holder.get();
            if (hiddenBlocks.contains(id)) {
                // Compatibility double slabs are still slab blocks (as MMDLib tagged its own); they have no items.
                if (block instanceof SlabBlock) blockTags.computeIfAbsent("slabs", key -> new ArrayList<>()).add(entry);
                return;
            }
            // Tags vanilla keeps for both the block and its item.
            String shared = switch (block) {
                case DoorBlock door -> "doors";
                case TrapDoorBlock trapdoor -> "trapdoors";
                case ButtonBlock button -> "buttons";
                case SlabBlock slab -> "slabs";
                case StairBlock stairs -> "stairs";
                case WallBlock wall -> "walls";
                case FenceBlock fence -> "fences";
                case FenceGateBlock gate -> "fence_gates";
                case AnvilBlock anvil -> "anvil";
                case BaseRailBlock rail -> "rails";
                default -> null;
            };
            if (shared != null) {
                blockTags.computeIfAbsent(shared, key -> new ArrayList<>()).add(entry);
                if (ModContent.itemsById().containsKey(id)) {
                    itemTags.computeIfAbsent(shared, key -> new ArrayList<>()).add(entry);
                }
            }
            String blockOnly = switch (block) {
                case BasePressurePlateBlock plate -> "pressure_plates";
                case LadderBlock ladder -> "climbable";
                case FlowerPotBlock pot -> "flower_pots";
                default -> null;
            };
            if (blockOnly != null) blockTags.computeIfAbsent(blockOnly, key -> new ArrayList<>()).add(entry);
        });
        for (Map.Entry<String, List<String>> tag : blockTags.entrySet()) {
            tag(cache, "minecraft", "blocks/" + tag.getKey(), tag.getValue());
        }
        for (Map.Entry<String, List<String>> tag : itemTags.entrySet()) {
            tag(cache, "minecraft", "items/" + tag.getKey(), tag.getValue());
        }

        // NeoForge's tool tags name vanilla items rather than including #minecraft:swords and the like.
        Map<String, List<String>> toolTags = new LinkedHashMap<>();
        ModContent.itemsById().forEach((id, holder) -> {
            Item item = holder.get();
            List<String> tags = switch (item) {
                case SwordItem sword -> List.of("tools/melee_weapon");
                case AxeItem axe -> List.of("tools/melee_weapon");
                case PickaxeItem pickaxe -> List.of("tools/mining_tool");
                case ShieldItem shield -> List.of("tools/shield");
                case BowItem bow -> List.of("tools/bow", "tools/ranged_weapon");
                case CrossbowItem crossbow -> List.of("tools/crossbow", "tools/ranged_weapon");
                case ShearsItem shears -> List.of("tools/shear");
                case FishingRodItem rod -> List.of("tools/fishing_rod");
                default -> List.of();
            };
            tags.forEach(tag -> toolTags.computeIfAbsent(tag, key -> new ArrayList<>()).add("basemetals:" + id));
        });
        for (Map.Entry<String, List<String>> tag : toolTags.entrySet()) {
            tag(cache, "forge", "items/" + tag.getKey(), tag.getValue());
        }

        // Compatibility-only content: hidden double slabs, and molten block items kept for old saves.
        List<String> hiddenItems = new ArrayList<>();
        ModContent.itemsById().keySet().forEach(id -> {
            if (hiddenBlocks.contains(id) || ModContent.fluids().containsKey(id)) hiddenItems.add("basemetals:" + id);
        });
        tag(cache, "forge", "blocks/hidden_from_recipe_viewers",
                hiddenBlocks.stream().map(id -> "basemetals:" + id).toList());
        tag(cache, "forge", "items/hidden_from_recipe_viewers", hiddenItems);
    }

    /**
     * Vanilla's equipment and enchantability tags, by what each item is. Vanilla builds its
     * enchantable/sword, /armor and so on from these, so they decide which enchantments apply.
     */
    private void writeEquipmentTags(CachedOutput cache) throws IOException {
        Map<String, List<String>> tags = new LinkedHashMap<>();
        ModContent.itemsById().forEach((id, holder) -> {
            Item item = holder.get();
            List<String> itemTags = new ArrayList<>();
            if (item instanceof SwordItem) itemTags.add("swords");
            if (item instanceof AxeItem) itemTags.add("axes");
            // Crackhammers are MMDLib tools with the pickaxe ability.
            if (item.canPerformAction(item.getDefaultInstance(),
                    net.neoforged.neoforge.common.ItemAbilities.PICKAXE_DIG)) itemTags.add("pickaxes");
            if (item instanceof net.minecraft.world.item.ShovelItem) itemTags.add("shovels");
            if (item instanceof net.minecraft.world.item.HoeItem
                    || item instanceof zone.moddev.mc.lib.common.runtime.ScytheItem) itemTags.add("hoes");
            if (item instanceof net.minecraft.world.item.ArmorItem armor) {
                switch (armor.getType()) {
                    case HELMET -> itemTags.add("head_armor");
                    case CHESTPLATE -> itemTags.add("chest_armor");
                    case LEGGINGS -> itemTags.add("leg_armor");
                    case BOOTS -> itemTags.add("foot_armor");
                    default -> { } // horse armour
                }
            }
            if (item instanceof BowItem) itemTags.add("enchantable/bow");
            if (item instanceof CrossbowItem) itemTags.add("enchantable/crossbow");
            if (item instanceof FishingRodItem) itemTags.add("enchantable/fishing");
            // Vanilla lists shears itself in enchantable/mining (Efficiency).
            if (item instanceof ShearsItem) itemTags.add("enchantable/mining");
            if (item instanceof BowItem || item instanceof CrossbowItem || item instanceof FishingRodItem
                    || item instanceof ShearsItem || item instanceof ShieldItem) itemTags.add("enchantable/durability");
            itemTags.forEach(tag -> tags.computeIfAbsent(tag, key -> new ArrayList<>()).add("basemetals:" + id));
        });
        for (Map.Entry<String, List<String>> tag : tags.entrySet()) {
            tag(cache, "minecraft", "items/" + tag.getKey(), tag.getValue().stream().sorted().toList());
        }
    }

    /** NeoForge's ore convention tags: the stone an ore sits in and how much it drops. */
    private void writeOreConventionTags(CachedOutput cache) throws IOException {
        Map<String, List<String>> inGround = new LinkedHashMap<>();
        List<String> singular = new ArrayList<>();
        for (MaterialDefinition material : MaterialCatalogue.all()) {
            if (!material.hasOre()) continue;
            String ore = "basemetals:" + material.name() + "_ore";
            String host = BMBlockStateProvider.oreHost(material.name());
            // NeoForge has no end stone ore convention.
            if (!host.equals("end_stone")) inGround.computeIfAbsent(host, key -> new ArrayList<>()).add(ore);
            // Every Base Metals ore drops one raw item.
            singular.add(ore);
        }
        for (String family : List.of("blocks", "items")) {
            for (Map.Entry<String, List<String>> host : inGround.entrySet()) {
                tag(cache, "forge", family + "/ores_in_ground/" + host.getKey(), host.getValue());
            }
            tag(cache, "forge", family + "/ore_rates/singular", singular);
        }
    }

    private void writeCompatibilityTags(CachedOutput cache) throws IOException {
        // Chemical registry tags use the registry namespace as part of their directory.
        for (String state : List.of("dirty", "clean")) {
            tag(cache, "mekanism", "mekanism/chemical/" + state,
                    PROCESSING_MATERIALS.stream()
                            .map(material -> "basemetals:" + state + "_" + material).toList());
        }
        // Mekanism's slurry ore link (replaces ChemicalBuilder.ore); merged with Mekanism's own file.
        JsonObject solidTags = new JsonObject();
        for (String material : PROCESSING_MATERIALS) {
            for (String state : List.of("dirty", "clean")) {
                solidTags.add("basemetals:" + state + "_" + material,
                        object("representation", "c:ores/" + material));
            }
        }
        JsonObject solidTagMap = new JsonObject();
        solidTagMap.add("values", solidTags);
        save(cache, solidTagMap, "data/mekanism/data_maps/mekanism/chemical/chemical_solid_tag.json");
        for (String fluid : ModContent.fluids().keySet()) {
            tag(cache, "forge", "fluids/molten_" + fluid, List.of(
                    "basemetals:" + fluid, "basemetals:flowing_" + fluid));
        }
        for (String material : PROCESSING_MATERIALS) {
            for (String family : List.of("dirty_dusts", "clumps", "crystals", "shards")) {
                tag(cache, "mekanism", "items/" + family + "/" + material,
                        List.of("#c:" + family + "/" + material));
            }
        }
    }

    private void writeRecipes(CachedOutput cache) throws IOException {
        for (MaterialDefinition material : MaterialCatalogue.all()) {
            String name = material.name();
            String ingot = "basemetals:" + name + "_ingot";
            String ingotIngredient = "#c:ingots/" + name;
            if (material.hasEquipment()) {
                shaped(cache, name + "_block", List.of("XXX", "XXX", "XXX"),
                        Map.of('X', ingotIngredient),
                        "basemetals:" + name + "_block", 1);
                shapeless(cache, name + "_block_ingot", List.of("#c:storage_blocks/" + name), ingot, 9);
                shaped(cache, name + "_plate", List.of("XXX"), Map.of('X', ingotIngredient),
                        "basemetals:" + name + "_plate", 3);
                shaped(cache, name + "_rod", List.of("X", "X"), Map.of('X', ingotIngredient),
                        "basemetals:" + name + "_rod", 4);
                shaped(cache, name + "_gear", List.of(" X ", "XRX", " X "),
                        Map.of('X', ingotIngredient, 'R', "#c:rods/" + name),
                        "basemetals:" + name + "_gear", 4);
                equipmentRecipes(cache, name, ingotIngredient);
                decorativeRecipes(cache, name, ingotIngredient, "#c:storage_blocks/" + name);
            }
            shaped(cache, name + "_ingot_from_nuggets", List.of("XXX", "XXX", "XXX"),
                    Map.of('X', "#c:nuggets/" + name), ingot, 1);
            shapeless(cache, name + "_nuggets", List.of(ingotIngredient),
                    "basemetals:" + name + "_nugget", 9);
            shaped(cache, name + "_powder_from_small", List.of("XXX", "XXX", "XXX"),
                    Map.of('X', "#c:tiny_dusts/" + name), "basemetals:" + name + "_powder", 1);
            shapeless(cache, name + "_smallpowder", List.of("#c:dusts/" + name),
                    "basemetals:" + name + "_smallpowder", 9);
            smelting(cache, name + "_powder_smelting", "#c:dusts/" + name, ingot,
                    material.oreSmeltingExperience(), 200, "minecraft:smelting");
            smelting(cache, name + "_smallpowder_smelting", "#c:tiny_dusts/" + name,
                    "basemetals:" + name + "_nugget",
                    legacyNuggetExperience(material.oreSmeltingExperience()), 200, "minecraft:smelting");
            if (material.hasOre()) {
                smelting(cache, name + "_ore_smelting", "#c:ores/" + name, ingot,
                        material.oreSmeltingExperience(), 200, "minecraft:smelting");
                smelting(cache, name + "_ore_blasting", "#c:ores/" + name, ingot,
                        material.oreSmeltingExperience(), 100, "minecraft:blasting");
                crushing(cache, name + "_ore_crushing", "#c:ores/" + name,
                        "basemetals:" + name + "_powder", 2);
            }
            crushing(cache, name + "_ingot_crushing", "#c:ingots/" + name,
                    "basemetals:" + name + "_powder", 1);
            if (material.hasEquipment()) {
                crushing(cache, name + "_block_crushing", "#c:storage_blocks/" + name,
                        "basemetals:" + name + "_powder", 9);
            }
            crushing(cache, name + "_nugget_crushing", "#c:nuggets/" + name,
                    "basemetals:" + name + "_smallpowder", 1);
            if (material.isAlloy()) {
                shaped(cache, name + "_blend_from_small", List.of("XXX", "XXX", "XXX"),
                        Map.of('X', "#c:small_blends/" + name), "basemetals:" + name + "_blend", 1);
                shapeless(cache, name + "_smallblend", List.of("#c:blends/" + name),
                        "basemetals:" + name + "_smallblend", 9);
                smelting(cache, name + "_blend_smelting", "#c:blends/" + name, ingot,
                        material.oreSmeltingExperience(), 200, "minecraft:smelting");
                smelting(cache, name + "_smallblend_smelting", "#c:small_blends/" + name,
                        "basemetals:" + name + "_nugget",
                        legacyNuggetExperience(material.oreSmeltingExperience()), 200, "minecraft:smelting");
            }
            if (material.hasEquipment()) {
                legacyRecyclingRecipes(cache, name, ingot,
                        "basemetals:" + name + "_nugget", "basemetals:" + name + "_block",
                        material.oreSmeltingExperience(),
                        legacyNuggetExperience(material.oreSmeltingExperience()));
            }
        }
        alloyRecipes(cache);
        vanillaBitsRecipes(cache);
        plateRepairRecipes(cache);
        legacySpecialRecipes(cache);
        anvilRecipes(cache);
        writeCompatibilityRecipes(cache);
    }

    private void equipmentRecipes(CachedOutput cache, String name, String metal) throws IOException {
        Map<Character, String> tool = Map.of('X', metal, 'S', WOODEN_ROD);
        shaped(cache, name + "_pickaxe", List.of("XXX", " S ", " S "), tool, "basemetals:" + name + "_pickaxe", 1);
        shaped(cache, name + "_axe", List.of("XX", "XS", " S"), tool, "basemetals:" + name + "_axe", 1);
        shaped(cache, name + "_shovel", List.of("X", "S", "S"), tool, "basemetals:" + name + "_shovel", 1);
        shaped(cache, name + "_hoe", List.of("XX", " S", " S"), tool, "basemetals:" + name + "_hoe", 1);
        shaped(cache, name + "_sword", List.of("X", "X", "S"), tool, "basemetals:" + name + "_sword", 1);
        shaped(cache, name + "_helmet", List.of("XXX", "X X"), Map.of('X', metal), "basemetals:" + name + "_helmet", 1);
        shaped(cache, name + "_chestplate", List.of("X X", "XXX", "XXX"), Map.of('X', metal), "basemetals:" + name + "_chestplate", 1);
        shaped(cache, name + "_leggings", List.of("XXX", "X X", "X X"), Map.of('X', metal), "basemetals:" + name + "_leggings", 1);
        shaped(cache, name + "_boots", List.of("X X", "X X"), Map.of('X', metal), "basemetals:" + name + "_boots", 1);
        shaped(cache, name + "_crackhammer", List.of("X", "S", "S"),
                Map.of('X', "#c:storage_blocks/" + name, 'S', WOODEN_ROD),
                "basemetals:" + name + "_crackhammer", 1);
        shaped(cache, name + "_scythe", List.of("XX ", "  X", " S "), tool, "basemetals:" + name + "_scythe", 1);
        shaped(cache, name + "_shears", List.of(" X", "X "), Map.of('X', metal),
                "basemetals:" + name + "_shears", 1);
        shaped(cache, name + "_fishingrod", List.of("  R", " RS", "R S"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string"),
                "basemetals:" + name + "_fishing_rod", 1);
        shaped(cache, name + "_horsearmor", List.of("  X", "XWX", "XXX"),
                Map.of('X', metal, 'W', "#minecraft:wool"),
                "basemetals:" + name + "_horse_armor", 1);
        shaped(cache, name + "_arrow", List.of("N", "R", "F"),
                Map.of('N', "#c:nuggets/" + name, 'R', "#c:rods/" + name, 'F', "minecraft:feather"),
                "basemetals:" + name + "_arrow", 4);
        shaped(cache, name + "_bolt", List.of("R", "R", "F"),
                Map.of('R', "#c:rods/" + name, 'F', "minecraft:feather"),
                "basemetals:" + name + "_bolt", 4);
        shaped(cache, name + "_bow", List.of(" RS", "R S", " RS"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string"), "basemetals:" + name + "_bow", 1);
        shaped(cache, name + "_crossbow", List.of("GRR", " SR", "R G"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string", 'G', "#c:gears/" + name),
                "basemetals:" + name + "_crossbow", 1);
        shaped(cache, name + "_shield", List.of("XWX", "XXX", " X "),
                Map.of('X', metal, 'W', "#minecraft:planks"), "basemetals:" + name + "_shield", 1);
    }

    private void decorativeRecipes(CachedOutput cache, String name, String metal, String block) throws IOException {
        shaped(cache, name + "_bars", List.of("XXX", "XXX"), Map.of('X', metal), "basemetals:" + name + "_bars", 16);
        shaped(cache, name + "_bars_2", List.of("XXX"), Map.of('X', "#c:rods/" + name),
                "basemetals:" + name + "_bars", 4);
        shaped(cache, name + "_door", List.of("XX", "XX", "XX"), Map.of('X', metal), "basemetals:" + name + "_door", 3);
        shaped(cache, name + "_trapdoor", List.of("XX", "XX"), Map.of('X', metal),
                "basemetals:" + name + "_trapdoor", 1);
        shaped(cache, name + "_button", List.of("X", "X"), Map.of('X', "#c:nuggets/" + name),
                "basemetals:" + name + "_button", 1);
        shaped(cache, name + "_slab", List.of("XXX"), Map.of('X', block), "basemetals:" + name + "_slab", 6);
        shaped(cache, name + "_pressure_plate", List.of("XX"), Map.of('X', metal), "basemetals:" + name + "_pressure_plate", 1);
        shaped(cache, name + "_stairs", List.of("X  ", "XX ", "XXX"), Map.of('X', block), "basemetals:" + name + "_stairs", 4);
        shaped(cache, name + "_wall", List.of("XXX", "XXX"), Map.of('X', block), "basemetals:" + name + "_wall", 6);
        shaped(cache, name + "_lever", List.of("R", "X"),
                Map.of('R', "#c:rods/" + name, 'X', metal), "basemetals:" + name + "_lever", 1);
    }

    private void vanillaBitsRecipes(CachedOutput cache) throws IOException {
        Map<String, String> materials = Map.of(
                "copper", "#c:ingots/copper", "diamond", "#c:gems/diamond", "emerald", "#c:gems/emerald",
                "gold", "#c:ingots/gold", "iron", "#c:ingots/iron",
                "obsidian", "#c:ingots/obsidian", "quartz", "#c:gems/quartz");
        for (String name : VANILLA_EQUIPMENT) {
            String material = materials.get(name);
            if (hasItem(name + "_rod")) {
                shaped(cache, name + "_rod", List.of("X", "X"), Map.of('X', material),
                        "basemetals:" + name + "_rod", 4);
            }
            if (hasItem(name + "_gear")) {
                String gearRod = name.equals("emerald") ? "#c:rods/iron" : "#c:rods/" + name;
                shaped(cache, name + "_gear", List.of(" X ", "XRX", " X "),
                        Map.of('X', material, 'R', gearRod),
                        "basemetals:" + name + "_gear", 4);
            }
            vanillaEquipmentRecipes(cache, name, material);
            vanillaDecorativeRecipes(cache, name, material, "#c:storage_blocks/" + name);
        }
        shaped(cache, "gold_plate", List.of("XXX"), Map.of('X', "#c:ingots/gold"),
                "basemetals:gold_plate", 3);
        shaped(cache, "iron_plate", List.of("XXX"), Map.of('X', "#c:ingots/iron"),
                "basemetals:iron_plate", 3);
        shaped(cache, "copper_plate", List.of("XXX"), Map.of('X', "#c:ingots/copper"),
                "basemetals:copper_plate", 3);
        // Vanilla 1.21.1 has no copper nugget, so Base Metals keeps its own.
        shaped(cache, "copper_ingot_from_nuggets", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/copper"), "minecraft:copper_ingot", 1);
        shapeless(cache, "copper_nuggets", List.of("minecraft:copper_ingot"), "basemetals:copper_nugget", 9);
        for (String name : List.of("stone", "wood")) {
            if (hasItem(name + "_crackhammer")) {
                String hammerHead = name.equals("stone") ? "minecraft:stone_bricks" : "#minecraft:logs";
                shaped(cache, name + "_crackhammer", List.of("X", "S", "S"),
                        Map.of('X', hammerHead, 'S', WOODEN_ROD),
                        "basemetals:" + name + "_crackhammer", 1);
            }
            if (hasItem(name + "_scythe")) {
                String scytheMaterial = name.equals("stone") ? "#c:stones" : "#minecraft:planks";
                shaped(cache, name + "_scythe", List.of("XX ", "  X", " S "),
                        Map.of('X', scytheMaterial, 'S', WOODEN_ROD),
                        "basemetals:" + name + "_scythe", 1);
            }
            if (hasItem(name + "_gear")) {
                String gearMaterial = name.equals("stone") ? "#c:cobblestones" : "#minecraft:planks";
                String gearRod = name.equals("stone") ? "#c:rods/stone" : WOODEN_ROD;
                shaped(cache, name + "_gear", List.of(" X ", "XRX", " X "),
                        Map.of('X', gearMaterial, 'R', gearRod), "basemetals:" + name + "_gear", 4);
            }
        }
        if (hasItem("stone_rod")) {
            shaped(cache, "stone_rod", List.of("X", "X"), Map.of('X', "#c:cobblestones"),
                    "basemetals:stone_rod", 4);
        }
        // Vanilla Bits also allowed three Base Metals iron rods to make four
        // vanilla iron bars.
        shaped(cache, "iron_bars_2", List.of("XXX"), Map.of('X', "#c:rods/iron"),
                "minecraft:iron_bars", 4);
        vanillaPowderRecipes(cache);
        vanillaCrushingRecipes(cache);
    }

    private void vanillaCrushingRecipes(CachedOutput cache) throws IOException {
        // The generic vanilla crushing recipes every *Metals and *Advantage mod shares.
        crushing(cache, "cobblestone_crushing", "#c:cobblestones", "minecraft:gravel", 1);
        crushing(cache, "gravel_crushing", "#c:gravels", "minecraft:sand", 1);
        crushing(cache, "stone_crushing", "minecraft:stone", "minecraft:cobblestone", 1);
        crushing(cache, "smooth_stone_crushing", "minecraft:smooth_stone", "minecraft:stone", 1);
        crushing(cache, "bricks_crushing", "minecraft:bricks", "minecraft:brick", 4);
        crushing(cache, "nether_bricks_crushing", "minecraft:nether_bricks", "minecraft:nether_brick", 4);
        crushing(cache, "sandstone_crushing", "#c:sandstone/uncolored_blocks", "minecraft:sand", 1);
        crushing(cache, "red_sandstone_crushing", "#c:sandstone/red_blocks", "minecraft:red_sand", 1);

        // Default MMDLib 1.12 ore-dictionary behaviour, extended through
        // the common ore tags so normal and deepslate ores share a recipe.
        for (String name : List.of("coal", "copper", "diamond", "emerald", "gold", "iron")) {
            crushing(cache, name + "_ore_crushing", "#c:ores/" + name,
                    "basemetals:" + name + "_powder", 2);
        }

        crushing(cache, "coal_crushing", "minecraft:coal", "basemetals:coal_powder", 1);
        crushing(cache, "coal_block_crushing", "#c:storage_blocks/coal",
                "basemetals:coal_powder", 9);
        crushing(cache, "coal_nugget_crushing", "#c:nuggets/coal",
                "basemetals:coal_smallpowder", 1);
        crushing(cache, "charcoal_crushing", "minecraft:charcoal", "basemetals:charcoal_powder", 1);
        crushing(cache, "charcoal_block_crushing", "#c:storage_blocks/charcoal",
                "basemetals:charcoal_powder", 9);
        crushing(cache, "charcoal_nugget_crushing", "#c:nuggets/charcoal",
                "basemetals:charcoal_smallpowder", 1);

        for (String name : List.of("diamond", "emerald")) {
            crushing(cache, name + "_crushing", "minecraft:" + name,
                    "basemetals:" + name + "_powder", 1);
            crushing(cache, name + "_block_crushing", "#c:storage_blocks/" + name,
                    "basemetals:" + name + "_powder", 9);
            crushing(cache, name + "_nugget_crushing", "#c:nuggets/" + name,
                    "basemetals:" + name + "_smallpowder", 1);
        }
        for (String name : List.of("copper", "gold", "iron")) {
            crushing(cache, name + "_ingot_crushing", "#c:ingots/" + name,
                    "basemetals:" + name + "_powder", 1);
            crushing(cache, name + "_block_crushing", "#c:storage_blocks/" + name,
                    "basemetals:" + name + "_powder", 9);
            crushing(cache, name + "_nugget_crushing", "#c:nuggets/" + name,
                    "basemetals:" + name + "_smallpowder", 1);
        }
        crushing(cache, "obsidian_ingot_crushing", "#c:ingots/obsidian",
                "basemetals:obsidian_powder", 1);
        crushing(cache, "obsidian_nugget_crushing", "#c:nuggets/obsidian",
                "basemetals:obsidian_smallpowder", 1);
        crushing(cache, "quartz_crushing", "minecraft:quartz", "basemetals:quartz_powder", 1);
        crushing(cache, "quartz_nugget_crushing", "#c:nuggets/quartz",
                "basemetals:quartz_smallpowder", 1);

        crushing(cache, "obsidian_crushing", "minecraft:obsidian", "basemetals:obsidian_powder", 4);
        // Crushing that yields vanilla items (stone to cobblestone, bone to bone meal, ...)
        // is shared by every Metals and Advantage mod, so MMDLib registers it.
    }

    private void vanillaEquipmentRecipes(CachedOutput cache, String name, String material) throws IOException {
        if (hasItem(name + "_crackhammer")) shaped(cache, name + "_crackhammer", List.of("X", "S", "S"),
                Map.of('X', "#c:storage_blocks/" + name, 'S', WOODEN_ROD),
                "basemetals:" + name + "_crackhammer", 1);
        if (hasItem(name + "_scythe")) {
            // Vanilla Bits' Obsidian scythe uniquely used the 1.12 "obsidian"
            // Ore Dictionary entry (blocks), rather than ingotObsidian.
            String scytheMaterial = name.equals("obsidian")
                    ? "#c:storage_blocks/obsidian"
                    : material;
            shaped(cache, name + "_scythe", List.of("XX ", "  X", " S "),
                    Map.of('X', scytheMaterial, 'S', WOODEN_ROD),
                    "basemetals:" + name + "_scythe", 1);
        }
        if (hasItem(name + "_shears")) shaped(cache, name + "_shears", List.of(" X", "X "),
                Map.of('X', material), "basemetals:" + name + "_shears", 1);
        if (hasItem(name + "_fishing_rod")) shaped(cache, name + "_fishingrod", List.of("  R", " RS", "R S"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string"),
                "basemetals:" + name + "_fishing_rod", 1);
        if (hasItem(name + "_arrow")) shaped(cache, name + "_arrow", List.of("N", "R", "F"),
                Map.of('N', "#c:nuggets/" + name, 'R', "#c:rods/" + name, 'F', "minecraft:feather"),
                "basemetals:" + name + "_arrow", 4);
        if (hasItem(name + "_bolt")) shaped(cache, name + "_bolt", List.of("R", "R", "F"),
                Map.of('R', "#c:rods/" + name, 'F', "minecraft:feather"),
                "basemetals:" + name + "_bolt", 4);
        if (hasItem(name + "_bow")) shaped(cache, name + "_bow", List.of(" RS", "R S", " RS"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string"),
                "basemetals:" + name + "_bow", 1);
        if (hasItem(name + "_crossbow")) shaped(cache, name + "_crossbow", List.of("GRR", " SR", "R G"),
                Map.of('R', "#c:rods/" + name, 'S', "minecraft:string", 'G', "#c:gears/" + name),
                "basemetals:" + name + "_crossbow", 1);
        if (hasItem(name + "_shield")) shaped(cache, name + "_shield", List.of("XWX", "XXX", " X "),
                Map.of('X', material, 'W', "#minecraft:planks"), "basemetals:" + name + "_shield", 1);
        if (hasItem(name + "_horse_armor")) shaped(cache, name + "_horsearmor", List.of("  X", "XWX", "XXX"),
                Map.of('X', material, 'W', "#minecraft:wool"), "basemetals:" + name + "_horse_armor", 1);
        if (hasItem(name + "_sword")) {
            Map<Character, String> tool = Map.of('X', material, 'S', WOODEN_ROD);
            shaped(cache, name + "_pickaxe", List.of("XXX", " S ", " S "), tool, "basemetals:" + name + "_pickaxe", 1);
            shaped(cache, name + "_axe", List.of("XX", "XS", " S"), tool, "basemetals:" + name + "_axe", 1);
            shaped(cache, name + "_shovel", List.of("X", "S", "S"), tool, "basemetals:" + name + "_shovel", 1);
            shaped(cache, name + "_hoe", List.of("XX", " S", " S"), tool, "basemetals:" + name + "_hoe", 1);
            shaped(cache, name + "_sword", List.of("X", "X", "S"), tool, "basemetals:" + name + "_sword", 1);
            shaped(cache, name + "_helmet", List.of("XXX", "X X"), Map.of('X', material), "basemetals:" + name + "_helmet", 1);
            shaped(cache, name + "_chestplate", List.of("X X", "XXX", "XXX"), Map.of('X', material), "basemetals:" + name + "_chestplate", 1);
            shaped(cache, name + "_leggings", List.of("XXX", "X X", "X X"), Map.of('X', material), "basemetals:" + name + "_leggings", 1);
            shaped(cache, name + "_boots", List.of("X X", "X X"), Map.of('X', material), "basemetals:" + name + "_boots", 1);
        }
    }

    private void vanillaDecorativeRecipes(CachedOutput cache, String name, String material,
            String block) throws IOException {
        if (hasItem(name + "_bars")) shaped(cache, name + "_bars", List.of("XXX", "XXX"), Map.of('X', material), "basemetals:" + name + "_bars", 16);
        if (hasItem(name + "_bars") && hasItem(name + "_rod")) {
            shaped(cache, name + "_bars_2", List.of("XXX"), Map.of('X', "#c:rods/" + name),
                    "basemetals:" + name + "_bars", 4);
        }
        if (hasItem(name + "_door")) shaped(cache, name + "_door", List.of("XX", "XX", "XX"),
                Map.of('X', material), "basemetals:" + name + "_door", 3);
        if (hasItem(name + "_trapdoor")) shaped(cache, name + "_trapdoor", List.of("XX", "XX"),
                Map.of('X', material), "basemetals:" + name + "_trapdoor", 1);
        if (hasItem(name + "_button")) {
            // Gold and iron have no Base Metals nugget: use the vanilla one via the common tag.
            shaped(cache, name + "_button", List.of("X", "X"),
                    Map.of('X', "#c:nuggets/" + name), "basemetals:" + name + "_button", 1);
        }
        if (hasItem(name + "_slab")) shaped(cache, name + "_slab", List.of("XXX"), Map.of('X', block), "basemetals:" + name + "_slab", 6);
        if (hasItem(name + "_pressure_plate")) shaped(cache, name + "_pressure_plate", List.of("XX"), Map.of('X', material), "basemetals:" + name + "_pressure_plate", 1);
        if (hasItem(name + "_stairs")) shaped(cache, name + "_stairs", List.of("X  ", "XX ", "XXX"), Map.of('X', block), "basemetals:" + name + "_stairs", 4);
        if (hasItem(name + "_wall")) shaped(cache, name + "_wall", List.of("XXX", "XXX"), Map.of('X', block), "basemetals:" + name + "_wall", 6);
        if (hasItem(name + "_lever")) shaped(cache, name + "_lever", List.of("R", "X"),
                Map.of('R', "#c:rods/" + name, 'X', material), "basemetals:" + name + "_lever", 1);
    }

    private void vanillaPowderRecipes(CachedOutput cache) throws IOException {
        for (String name : List.of("coal", "charcoal", "copper", "diamond", "emerald", "gold", "iron", "obsidian", "quartz")) {
            shaped(cache, name + "_powder_from_small", List.of("XXX", "XXX", "XXX"),
                    Map.of('X', "#c:tiny_dusts/" + name), "basemetals:" + name + "_powder", 1);
            shapeless(cache, name + "_smallpowder", List.of("#c:dusts/" + name),
                    "basemetals:" + name + "_smallpowder", 9);
        }
        Map<String, String> ingots = Map.of(
                "copper", "minecraft:copper_ingot",
                "diamond", "minecraft:diamond",
                "emerald", "minecraft:emerald",
                "gold", "minecraft:gold_ingot",
                "iron", "minecraft:iron_ingot",
                "obsidian", "basemetals:obsidian_ingot",
                "quartz", "minecraft:quartz");
        Map<String, String> nuggets = Map.of(
                "copper", "basemetals:copper_nugget",
                "diamond", "basemetals:diamond_nugget",
                "emerald", "basemetals:emerald_nugget",
                "gold", "minecraft:gold_nugget",
                "iron", "minecraft:iron_nugget",
                "obsidian", "basemetals:obsidian_nugget",
                "quartz", "basemetals:quartz_nugget");
        Map<String, String> blocks = Map.of(
                "copper", "minecraft:copper_block",
                "diamond", "minecraft:diamond_block",
                "emerald", "minecraft:emerald_block",
                "gold", "minecraft:gold_block",
                "iron", "minecraft:iron_block",
                "obsidian", "minecraft:obsidian",
                "quartz", "minecraft:quartz_block");
        for (String name : ingots.keySet().stream().sorted().toList()) {
            float baseExperience = vanillaBaseSmeltingExperience(name);
            float ingotExperience = vanillaIngotSmeltingExperience(name);
            smelting(cache, name + "_powder_smelting", "#c:dusts/" + name,
                    ingots.get(name), ingotExperience, 200, "minecraft:smelting");
            smelting(cache, name + "_smallpowder_smelting", "#c:tiny_dusts/" + name,
                    nuggets.get(name), legacyNuggetExperience(baseExperience), 200, "minecraft:smelting");
            legacyRecyclingRecipes(cache, name, ingots.get(name), nuggets.get(name), blocks.get(name),
                    ingotExperience, legacyNuggetExperience(baseExperience));
        }
        // MMDLib added pure-material blends for these three; 1.12 had the small blends.
        for (String name : List.of("diamond", "gold", "iron")) {
            shapeless(cache, name + "_blend", List.of("#c:dusts/" + name, "#c:dusts/" + name),
                    "basemetals:" + name + "_blend", 2);
            shaped(cache, name + "_blend_from_small", List.of("XXX", "XXX", "XXX"),
                    Map.of('X', "#c:small_blends/" + name), "basemetals:" + name + "_blend", 1);
            shapeless(cache, name + "_smallblend", List.of("#c:blends/" + name),
                    "basemetals:" + name + "_smallblend", 9);
            smelting(cache, name + "_blend_smelting", "#c:blends/" + name, ingots.get(name),
                    vanillaIngotSmeltingExperience(name), 200, "minecraft:smelting");
            smelting(cache, name + "_smallblend_smelting", "#c:small_blends/" + name,
                    nuggets.get(name), legacyNuggetExperience(vanillaBaseSmeltingExperience(name)),
                    200, "minecraft:smelting");
        }
        shaped(cache, "redstone_from_smallpowder", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:tiny_dusts/redstone"), "minecraft:redstone", 1);
        shapeless(cache, "redstone_smallpowder", List.of("#c:dusts/redstone"),
                "basemetals:redstone_smallpowder", 9);
        shaped(cache, "lapis_from_smallpowder", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:tiny_dusts/lapis"), "minecraft:lapis_lazuli", 1);
        shapeless(cache, "lapis_smallpowder", List.of("#c:dusts/lapis"),
                "basemetals:lapis_smallpowder", 9);
        smelting(cache, "redstone_ingot", "#c:dusts/redstone", "basemetals:redstone_ingot",
                0.1F, 200, "minecraft:smelting");
    }

    private static float vanillaBaseSmeltingExperience(String name) {
        return switch (name) {
            case "gold" -> 1.0F;
            case "iron" -> 0.45F;
            // Base Metals' catalogue value for copper (0.1 x magic 5), kept from before it became an extension.
            case "copper" -> 0.5F;
            case "diamond", "emerald", "obsidian" -> 0.4F;
            case "quartz" -> 0.2F;
            default -> throw new IllegalArgumentException("No Vanilla Bits furnace material " + name);
        };
    }

    private static float vanillaIngotSmeltingExperience(String name) {
        return switch (name) {
            case "diamond", "emerald", "gold" -> 1.0F;
            case "iron" -> 0.7F;
            default -> vanillaBaseSmeltingExperience(name);
        };
    }

    private void legacyRecyclingRecipes(CachedOutput cache, String name, String ingot, String nugget,
            String block, float ingotExperience, float nuggetExperience) throws IOException {
        legacyRecycle(cache, name, "button", nugget, 2, nuggetExperience);
        legacyRecycle(cache, name, "rod", nugget, 4, nuggetExperience);
        legacyRecycle(cache, name, "slab", nugget, 4, nuggetExperience);
        legacyRecycle(cache, name, "lever", ingot, 1, ingotExperience);
        legacyRecycle(cache, name, "wall", block, 1, 0.0F);
        legacyRecycle(cache, name, "pressure_plate", ingot, 2, ingotExperience);
        legacyRecycle(cache, name, "door", ingot, 2, ingotExperience);
        legacyRecycle(cache, name, "trapdoor", ingot, 4, ingotExperience);
        legacyRecycle(cache, name, "plate", ingot, 1, ingotExperience);
        legacyRecycle(cache, name, "bars", nugget, 3, nuggetExperience);
        legacyRecycle(cache, name, "gear", ingot, 1, ingotExperience);
        legacyRecycle(cache, name, "arrow", nugget, 1, nuggetExperience);
        legacyRecycle(cache, name, "bow", ingot, 1, ingotExperience);
        legacyRecycle(cache, name, "crossbow", ingot, 4, ingotExperience);
        legacyRecycle(cache, name, "bolt", nugget, 2, nuggetExperience);
        legacyRecycle(cache, name, "shears", ingot, 2, ingotExperience);

        legacyRecycle(cache, name, "boots", ingot, 4, ingotExperience);
        legacyRecycle(cache, name, "helmet", ingot, 5, ingotExperience);
        legacyRecycle(cache, name, "sword", ingot, 2, ingotExperience);
        legacyRecycle(cache, name, "shovel", ingot, 1, ingotExperience);
        legacyRecycle(cache, name, "pickaxe", ingot, 3, ingotExperience);
        legacyRecycle(cache, name, "hoe", ingot, 2, ingotExperience);
        legacyRecycle(cache, name, "axe", ingot, 3, ingotExperience);
        legacyRecycle(cache, name, "leggings", ingot, 7, ingotExperience);
        legacyRecycle(cache, name, "chestplate", ingot, 8, ingotExperience);
        legacyRecycle(cache, name, "crackhammer", block, 1, 0.0F);
    }

    private void legacyRecycle(CachedOutput cache, String name, String form, String result,
            int count, float experience) throws IOException {
        String item = name + "_" + form;
        if (hasItem(item)) {
            legacySmelting(cache, item + "_recycling", "basemetals:" + item,
                    result, count, experience, 200);
        }
    }

    private static float legacyNuggetExperience(float experience) {
        return Math.round((experience / 9.0F) * 100.0F) / 100.0F;
    }

    private void plateRepairRecipes(CachedOutput cache) throws IOException {
        List<String> materials = new ArrayList<>(MaterialCatalogue.all().stream()
                .map(MaterialDefinition::name)
                .filter(name -> !List.of("aquarium", "invar", "mercury", "mithril").contains(name))
                .toList());
        materials.addAll(List.of("copper", "gold", "iron"));
        for (String material : materials) {
            for (String form : List.of("boots", "chestplate", "helmet", "leggings", "shield")) {
                // Vanilla gold and iron armour, with the Base Metals shield.
                String namespace = !form.equals("shield") && List.of("gold", "iron").contains(material)
                        ? "minecraft" : "basemetals";
                String targetMaterial = namespace.equals("minecraft") && material.equals("gold")
                        ? "golden" : material;
                plateRepair(cache, material + "_" + form + "_plate_repair",
                        namespace + ":" + targetMaterial + "_" + form, "#c:plates/" + material);
            }
        }
    }

    private void legacySpecialRecipes(CachedOutput cache) throws IOException {
        String steel = "#c:ingots/steel";
        shaped(cache, "activator_rail", List.of("XSX", "XRX", "XSX"),
                Map.of('X', steel, 'S', WOODEN_ROD, 'R', "minecraft:redstone_torch"),
                "minecraft:activator_rail", 1);
        shaped(cache, "detector_rail", List.of("X X", "XSX", "XRX"),
                Map.of('X', steel, 'S', WOODEN_ROD, 'R', "minecraft:redstone_torch"),
                "minecraft:detector_rail", 1);
        shaped(cache, "rail", List.of("X X", "XSX", "X X"),
                Map.of('X', steel, 'S', WOODEN_ROD), "minecraft:rail", 16);
        shaped(cache, "minecart", List.of("X X", "XXX"),
                Map.of('X', steel), "minecraft:minecart", 1);
        shaped(cache, "piston", List.of("WWW", "CXC", "CRC"),
                Map.of('W', "#minecraft:planks", 'C', "#c:cobblestones",
                        'X', steel, 'R', "#c:dusts/redstone"),
                "minecraft:piston", 1);
        shaped(cache, "tripwire_hook", List.of("X", "S", "W"),
                Map.of('X', steel, 'S', WOODEN_ROD, 'W', "#minecraft:planks"),
                "minecraft:tripwire_hook", 1);
        shapeless(cache, "flint_and_steel", List.of(steel, "minecraft:flint"),
                "minecraft:flint_and_steel", 1);
        shaped(cache, "human_detector", List.of("XX", "GG"),
                Map.of('X', "#c:bricks/normal", 'G', "#c:gears"),
                "basemetals:human_detector", 1);

        shaped(cache, "charcoal_block", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "minecraft:charcoal"), "basemetals:charcoal_block", 1);
        shapeless(cache, "charcoal_from_block", List.of("basemetals:charcoal_block"),
                "minecraft:charcoal", 9);
        shaped(cache, "charcoal_block_dust", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:dusts/charcoal"), "basemetals:charcoal_block", 1);
        shaped(cache, "charcoal_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/charcoal"), "minecraft:charcoal", 1);
        shapeless(cache, "charcoal_nugget", List.of("minecraft:charcoal"),
                "basemetals:charcoal_nugget", 9);
        shaped(cache, "coal_block_dust", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:dusts/coal"), "minecraft:coal_block", 1);
        shaped(cache, "coal_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/coal"), "minecraft:coal", 1);
        shapeless(cache, "coal_nugget", List.of("minecraft:coal"),
                "basemetals:coal_nugget", 9);
        shaped(cache, "obsidian_block", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:ingots/obsidian"), "minecraft:obsidian", 1);
        shaped(cache, "obsidian_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/obsidian"), "basemetals:obsidian_ingot", 1);
        shapeless(cache, "obsidian_nugget", List.of("#c:ingots/obsidian"),
                "basemetals:obsidian_nugget", 9);
        shaped(cache, "quartz_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/quartz"), "minecraft:quartz", 1);
        shapeless(cache, "quartz_nugget", List.of("#c:gems/quartz"),
                "basemetals:quartz_nugget", 9);
        shaped(cache, "diamond_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/diamond"), "minecraft:diamond", 1);
        shapeless(cache, "diamond_nugget", List.of("#c:gems/diamond"),
                "basemetals:diamond_nugget", 9);
        shaped(cache, "emerald_ingot", List.of("XXX", "XXX", "XXX"),
                Map.of('X', "#c:nuggets/emerald"), "minecraft:emerald", 1);
        shapeless(cache, "emerald_nugget", List.of("#c:gems/emerald"),
                "basemetals:emerald_nugget", 9);
    }

    private void anvilRecipes(CachedOutput cache) throws IOException {
        shaped(cache, "stone_anvil", List.of("BBB", " C ", "SSS"),
                Map.of('B', "minecraft:stone_bricks", 'C', "#c:stones", 'S', "minecraft:stone_slab"),
                "basemetals:stone_anvil", 1);
        for (String material : List.of("steel", "adamantine")) {
            shaped(cache, material + "_anvil", List.of("BBB", " B ", "III"),
                    Map.of('B', "#c:storage_blocks/" + material, 'I', "#c:ingots/" + material),
                    "basemetals:" + material + "_anvil", 1);
        }
    }

    private static boolean hasItem(String id) {
        return ModContent.itemsById().containsKey(id);
    }

    private void alloyRecipes(CachedOutput cache) throws IOException {
        alloy(cache, "aquarium", 3, "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/zinc", "#c:dusts/prismarine");
        alloy(cache, "brass", 4, "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/zinc");
        alloy(cache, "bronze", 4, "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/tin");
        alloy(cache, "cupronickel", 4, "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/copper", "#c:dusts/nickel");
        alloy(cache, "electrum", 2, "#c:dusts/gold", "#c:dusts/silver");
        alloy(cache, "invar", 3, "#c:dusts/iron", "#c:dusts/iron", "#c:dusts/nickel");
        alloy(cache, "mithril", 3, "#c:dusts/silver", "#c:dusts/silver", "#c:dusts/coldiron", "#c:ingots/mercury");
        alloy(cache, "pewter", 3, "#c:dusts/tin", "#c:dusts/copper", "#c:dusts/lead");
        List<String> steel = new ArrayList<>();
        for (int i = 0; i < 8; i++) steel.add("#c:dusts/iron");
        steel.add("#c:dusts/coal");
        alloy(cache, "steel", 8, steel.toArray(String[]::new));
    }

    private void alloy(CachedOutput cache, String name, int count, String... ingredients) throws IOException {
        shapeless(cache, name + "_blend", List.of(ingredients), "basemetals:" + name + "_blend", count);
    }

    private void writeCompatibilityRecipes(CachedOutput cache) throws IOException {
        writeMekanismRecipes(cache);
        writeThermalRecipes(cache);
    }

    private void writeMekanismRecipes(CachedOutput cache) throws IOException {
        for (String name : PROCESSING_MATERIALS) {
            String root = "compat/mekanism/" + name + "/";
            mekanismItemToItem(cache, root + "dirty_dust_to_dust", "mekanism:enriching",
                    "#c:dirty_dusts/" + name, "basemetals:" + name + "_powder", 1);
            mekanismItemToItem(cache, root + "clump_to_dirty_dust", "mekanism:crushing",
                    "#c:clumps/" + name, "basemetals:" + name + "_powder_dirty", 1);
            mekanismItemChemicalToItem(cache, root + "shard_to_clump", "mekanism:purifying",
                    "#c:shards/" + name, "mekanism:oxygen", 1,
                    "basemetals:" + name + "_clump", 1);
            mekanismItemChemicalToItem(cache, root + "crystal_to_shard", "mekanism:injecting",
                    "#c:crystals/" + name, "mekanism:hydrogen_chloride", 1,
                    "basemetals:" + name + "_shard", 1);
            mekanismCrystallizing(cache, root + "clean_slurry_to_crystal", name);
            mekanismWashing(cache, root + "dirty_slurry_to_clean_slurry", name);

            mekanismItemToItem(cache, root + "ore_to_dust", "mekanism:enriching",
                    "#c:ores/" + name, "basemetals:" + name + "_powder", 2);
            mekanismItemChemicalToItem(cache, root + "ore_to_clump", "mekanism:purifying",
                    "#c:ores/" + name, "mekanism:oxygen", 1,
                    "basemetals:" + name + "_clump", 3);
            mekanismItemChemicalToItem(cache, root + "ore_to_shard", "mekanism:injecting",
                    "#c:ores/" + name, "mekanism:hydrogen_chloride", 1,
                    "basemetals:" + name + "_shard", 4);
            mekanismDissolution(cache, root + "ore_to_dirty_slurry", name,
                    "#c:ores/" + name, 1, 1000);
            mekanismDissolution(cache, root + "raw_to_dirty_slurry", name,
                    "#c:raw_materials/" + name, 3, 2000);
            mekanismItemToItem(cache, root + "raw_to_dust", "mekanism:enriching",
                    "#c:raw_materials/" + name, 3, "basemetals:" + name + "_powder", 4);
            mekanismItemChemicalToItem(cache, root + "raw_to_clump", "mekanism:purifying",
                    "#c:raw_materials/" + name, 1, "mekanism:oxygen", 1,
                    "basemetals:" + name + "_clump", 2);
            mekanismItemChemicalToItem(cache, root + "raw_to_shard", "mekanism:injecting",
                    "#c:raw_materials/" + name, 3, "mekanism:hydrogen_chloride", 1,
                    "basemetals:" + name + "_shard", 8);
            mekanismItemToItem(cache, root + "ingot_to_dust", "mekanism:crushing",
                    "#c:ingots/" + name, "basemetals:" + name + "_powder", 1);
        }
    }

    private void mekanismItemToItem(CachedOutput cache, String id, String type, String input,
            String outputItem, int count) throws IOException {
        mekanismItemToItem(cache, id, type, input, 1, outputItem, count);
    }

    private void mekanismItemToItem(CachedOutput cache, String id, String type, String input,
            int inputCount, String outputItem, int count) throws IOException {
        JsonObject root = object("type", type);
        JsonObject itemInput = ingredient(input);
        itemInput.addProperty("count", inputCount);
        root.add("input", itemInput);
        root.add("output", object("id", outputItem, "count", count));
        saveConditionalRecipe(cache, root, id, "mekanism");
    }

    private void mekanismItemChemicalToItem(CachedOutput cache, String id, String type, String input,
            String chemical, int amount, String outputItem, int count) throws IOException {
        mekanismItemChemicalToItem(cache, id, type, input, 1, chemical, amount, outputItem, count);
    }

    private void mekanismItemChemicalToItem(CachedOutput cache, String id, String type, String input,
            int inputCount, String chemical, int amount, String outputItem, int count) throws IOException {
        JsonObject root = object("type", type, "per_tick_usage", true);
        JsonObject itemInput = ingredient(input);
        itemInput.addProperty("count", inputCount);
        root.add("item_input", itemInput);
        root.add("chemical_input", object("amount", amount, "chemical", chemical));
        root.add("output", object("id", outputItem, "count", count));
        saveConditionalRecipe(cache, root, id, "mekanism");
    }

    private void mekanismCrystallizing(CachedOutput cache, String id, String material) throws IOException {
        JsonObject root = object("type", "mekanism:crystallizing");
        root.add("input", object("amount", 200, "chemical", "basemetals:clean_" + material));
        root.add("output", object("id", "basemetals:" + material + "_crystal"));
        saveConditionalRecipe(cache, root, id, "mekanism");
    }

    private void mekanismWashing(CachedOutput cache, String id, String material) throws IOException {
        JsonObject root = object("type", "mekanism:washing");
        root.add("fluid_input", object("amount", 5, "tag", "minecraft:water"));
        root.add("chemical_input", object("amount", 1, "chemical", "basemetals:dirty_" + material));
        root.add("output", object("amount", 1, "id", "basemetals:clean_" + material));
        saveConditionalRecipe(cache, root, id, "mekanism");
    }

    private void mekanismDissolution(CachedOutput cache, String id, String material,
            String input, int inputCount, int slurryAmount) throws IOException {
        JsonObject root = object("type", "mekanism:dissolution", "per_tick_usage", true);
        JsonObject itemInput = ingredient(input);
        itemInput.addProperty("count", inputCount);
        root.add("item_input", itemInput);
        root.add("chemical_input", object("amount", 1, "chemical", "mekanism:sulfuric_acid"));
        root.add("output", object("amount", slurryAmount, "id", "basemetals:dirty_" + material));
        saveConditionalRecipe(cache, root, id, "mekanism");
    }

    private void writeThermalRecipes(CachedOutput cache) throws IOException {
        for (MaterialDefinition material : MaterialCatalogue.all()) {
            String name = material.name();
            String root = "compat/thermal/" + name + "/";
            thermalItemMachine(cache, root + "ingot_pulverizing", "thermal:pulverizer",
                    "#c:ingots/" + name, "basemetals:" + name + "_powder", 1, 4000);
            thermalItemMachine(cache, root + "dust_furnace", "thermal:furnace",
                    "#c:dusts/" + name, "basemetals:" + name + "_ingot", 1, 2000);
            thermalCrucible(cache, root + "ingot_crucible", name);
            if (material.hasOre()) {
                thermalItemMachine(cache, root + "ore_pulverizing", "thermal:pulverizer",
                        "#c:ores/" + name, "basemetals:" + name + "_powder", 2, 4000);
            }
            if (material.hasEquipment()) {
                thermalPress(cache, root + "plate_press", "#c:ingots/" + name,
                        null, 1, "basemetals:" + name + "_plate");
                thermalPress(cache, root + "gear_press", "#c:ingots/" + name,
                        "thermal:press_gear_die", 4, "basemetals:" + name + "_gear");
            }
        }
        for (Map.Entry<String, List<AlloyComponent>> alloy : ALLOYS.entrySet()) {
            JsonObject recipe = object("type", "thermal:smelter", "energy", 6400);
            JsonArray inputs = new JsonArray();
            alloy.getValue().forEach(component -> inputs.add(thermalAlloyIngredient(component)));
            recipe.add("ingredients", inputs);
            JsonArray outputs = new JsonArray();
            outputs.add(result("basemetals:" + alloy.getKey() + "_ingot", ALLOY_OUTPUTS.get(alloy.getKey())));
            recipe.add("result", outputs);
            saveConditionalRecipe(cache, recipe, "compat/thermal/alloys/" + alloy.getKey(), "thermal_expansion");
        }
    }

    private void thermalItemMachine(CachedOutput cache, String id, String type, String input,
            String output, int count, int energy) throws IOException {
        JsonObject root = object("type", type, "energy", energy);
        root.add("ingredient", ingredient(input));
        JsonArray results = new JsonArray(); results.add(result(output, count)); root.add("result", results);
        saveConditionalRecipe(cache, root, id, "thermal_expansion");
    }

    private void thermalCrucible(CachedOutput cache, String id, String material) throws IOException {
        JsonObject root = object("type", "thermal:crucible", "energy", 2000);
        root.add("ingredient", ingredient("#c:ingots/" + material));
        JsonArray results = new JsonArray();
        results.add(object("fluid", "basemetals:" + material, "amount", 90));
        root.add("result", results);
        saveConditionalRecipe(cache, root, id, "thermal_expansion");
    }

    private void thermalPress(CachedOutput cache, String id, String input, String die,
            int count, String output) throws IOException {
        JsonObject root = object("type", "thermal:press");
        if (die == null) {
            root.add("ingredient", ingredient(input));
        } else {
            JsonArray inputs = new JsonArray();
            JsonObject metal = ingredient(input); metal.addProperty("count", count); inputs.add(metal);
            inputs.add(ingredient(die)); root.add("ingredients", inputs);
        }
        JsonArray results = new JsonArray(); results.add(result(output, 1)); root.add("result", results);
        saveConditionalRecipe(cache, root, id, "thermal_expansion");
    }

    private static JsonObject thermalAlloyIngredient(AlloyComponent component) {
        JsonArray accepted = new JsonArray();
        String tag = component.ingredient().substring(1);
        accepted.add(object("tag", tag));
        if (tag.startsWith("forge:dusts/")) {
            String material = tag.substring("forge:dusts/".length());
            if (MaterialCatalogue.byName().containsKey(material)
                    || material.equals("iron") || material.equals("gold")) {
                accepted.add(object("tag", "c:ingots/" + material));
            }
        }
        JsonObject result = object("count", component.count()); result.add("value", accepted); return result;
    }

    private void saveConditionalRecipe(CachedOutput cache, JsonObject recipe, String id,
            String modId) throws IOException {
        JsonArray conditions = new JsonArray();
        conditions.add(object("type", "neoforge:mod_loaded", "modid", modId));
        java.util.Set<String> outputs = new java.util.LinkedHashSet<>();
        for (String key : List.of("result", "output", "outputs")) {
            collectItemOutputs(recipe.get(key), outputs);
        }
        outputs.stream().filter(item -> item.startsWith("basemetals:"))
                .forEach(item -> conditions.add(object("type", "basemetals:content_mode", "item", item)));
        recipe.add("neoforge:conditions", conditions);
        save(cache, recipe, "data/basemetals/recipe/" + id + ".json");
    }

    private static void collectItemOutputs(com.google.gson.JsonElement value, java.util.Set<String> outputs) {
        if (value == null) return;
        if (value.isJsonArray()) {
            value.getAsJsonArray().forEach(entry -> collectItemOutputs(entry, outputs));
        } else if (value.isJsonObject()) {
            JsonObject output = value.getAsJsonObject();
            if (output.has("item")) outputs.add(output.get("item").getAsString());
            else if (output.has("id") && !output.has("amount")) outputs.add(output.get("id").getAsString());
        }
    }

    private static AlloyComponent component(String material, int count) {
        return new AlloyComponent("#c:dusts/" + material, count);
    }

    private record AlloyComponent(String ingredient, int count) {}

    private void writeAdvancements(CachedOutput cache) throws IOException {
        for (String id : ADVANCEMENTS) {
            JsonObject root = new JsonObject();
            String parent = advancementParent(id);
            if (parent != null) {
                root.addProperty("parent", parent.contains(":") ? parent : "basemetals:" + parent);
            }
            JsonObject display = new JsonObject();
            display.add("icon", object("id", advancementIcon(id)));
            display.add("title", object("translate", "advancements.basemetals." + id + ".title"));
            display.add("description", object("translate", "advancements.basemetals." + id + ".description"));
            display.addProperty("frame", advancementFrame(id));
            display.addProperty("show_toast", true); display.addProperty("announce_to_chat", true);
            root.add("display", display);
            JsonObject criterion = new JsonObject();
            criterion.addProperty("trigger", "minecraft:impossible");
            JsonObject criteria = new JsonObject(); criteria.add("event", criterion); root.add("criteria", criteria);
            save(cache, root, "data/basemetals/advancement/" + id + ".json");
        }
    }

    private void writeLanguages(CachedOutput cache) throws IOException {
        Path legacyRoot = findLegacyLanguageRoot();
        Path translations = legacyReference.getParent().getParent().resolve("tools/translations");
        JsonObject configText = JsonParser.parseString(Files.readString(translations.resolve("config.json"))).getAsJsonObject();
        JsonObject playerText = JsonParser.parseString(Files.readString(translations.resolve("player.json"))).getAsJsonObject();
        JsonObject bucketText = JsonParser.parseString(Files.readString(translations.resolve("bucket.json"))).getAsJsonObject();
        JsonObject blockText = JsonParser.parseString(Files.readString(translations.resolve("blocks.json"))).getAsJsonObject()
                .getAsJsonObject("locales");
        Map<String, String> rawTemplates = Map.ofEntries(Map.entry("en_us", "Raw %s"), Map.entry("de_de", "%s-Erzbrocken"),
                Map.entry("es_es", "%s en bruto"), Map.entry("fr_fr", "%s brut"), Map.entry("it_it", "%s grezzo"),
                Map.entry("ja_jp", "%sの原石"), Map.entry("nl_nl", "Ruw %s"), Map.entry("pt_br", "%s bruto"),
                Map.entry("ru_ru", "Рудное сырьё: %s"), Map.entry("zh_cn", "粗%s"), Map.entry("zh_tw", "粗%s"));
        for (String locale : LOCALES) {
            JsonObject language = readLegacyLanguage(legacyRoot.resolve(locale + ".lang"));
            // Retain the original translated values under the unified chemical registry keys.
            for (String material : PROCESSING_MATERIALS) {
                String legacyName = Character.toUpperCase(material.charAt(0)) + material.substring(1);
                for (String state : List.of("clean", "dirty")) {
                    String oldKey = "gas." + (state.equals("clean") ? "cleanGas" : "gas") + legacyName;
                    if (language.has(oldKey)) {
                        language.add("chemical.basemetals." + state + "_" + material, language.remove(oldKey));
                    }
                }
            }
            ModContent.blocksById().keySet().forEach(id -> addIfAbsent(language, "block.basemetals." + id,
                    ModContent.fluids().containsKey(id) ? "Molten " + title(id) : title(id)));
            ModContent.itemsById().keySet().forEach(id -> addIfAbsent(language, "item.basemetals." + id,
                    id.endsWith("_bucket") ? title(id.substring(0, id.length() - 7)) + " Bucket" : title(id)));
            addIfAbsent(language, "itemGroup.basemetals.blocks", "Base Metals Blocks");
            addIfAbsent(language, "itemGroup.basemetals.items", "Base Metals Items");
            addIfAbsent(language, "itemGroup.basemetals.tools", "Base Metals Tools");
            addIfAbsent(language, "itemGroup.basemetals.combat", "Base Metals Combat");
            addIfAbsent(language, "itemGroup.basemetals.materials", "Base Metals Materials");
            for (MaterialDefinition material : MaterialCatalogue.all()) {
                addIfAbsent(language, "material.basemetals." + material.name(), title(material.name()));
            }
            for (String material : PROCESSING_MATERIALS) {
                addIfAbsent(language, "chemical.basemetals.dirty_" + material,
                        "Dirty " + title(material) + " Slurry");
                addIfAbsent(language, "chemical.basemetals.clean_" + material,
                        "Clean " + title(material) + " Slurry");
            }
            for (String advancement : ADVANCEMENTS) {
                addIfAbsent(language, "advancements.basemetals." + advancement + ".title", title(advancement));
                addIfAbsent(language, "advancements.basemetals." + advancement + ".description",
                        "Progress through Base Metals: " + title(advancement) + ".");
            }
            String source = configText.getAsJsonObject("aliases").has(locale)
                    ? configText.getAsJsonObject("aliases").get(locale).getAsString() : locale;
            configText.getAsJsonObject("locales").getAsJsonObject(source).entrySet()
                    .forEach(entry -> language.add(entry.getKey(), entry.getValue()));
            playerText.getAsJsonObject("locales").getAsJsonObject(source).entrySet()
                    .forEach(entry -> language.add(entry.getKey(), entry.getValue()));
            // A locale's own block names win over its alias source's (en_pt keeps its pirate names).
            JsonObject blockNames = blockText.has(locale) ? blockText.getAsJsonObject(locale)
                    : blockText.has(source) ? blockText.getAsJsonObject(source) : new JsonObject();
            blockNames.entrySet().forEach(entry -> language.add(entry.getKey(), entry.getValue()));
            ModContent.fluids().keySet().forEach(material -> {
                String name = bucketText.getAsJsonObject("locales").getAsJsonObject(source).get(material).getAsString();
                language.addProperty("item.bucket." + material, name);
                language.addProperty("item.basemetals." + material + "_bucket", name);
            });
            MaterialCatalogue.all().stream().filter(MaterialDefinition::hasOre)
                    .forEach(material -> language.addProperty("item.basemetals." + material.name() + "_raw",
                            String.format(rawTemplates.get(source), materialName(language, material.name()))));
            save(cache, language, "assets/basemetals/lang/" + locale + ".json");
        }
    }

    private static Path findLegacyLanguageRoot() throws IOException {
        Path current = Path.of("").toAbsolutePath();
        for (int depth = 0; depth < 5 && current != null; depth++, current = current.getParent()) {
            Path candidate = current.resolve("reference/1.12/lang");
            if (Files.isDirectory(candidate)) return candidate;
        }
        throw new IOException("Could not locate retained 1.12 Base Metals language files");
    }

    private static JsonObject readLegacyLanguage(Path path) throws IOException {
        JsonObject language = new JsonObject();
        for (String rawLine : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            String line = rawLine.strip();
            if (line.isEmpty() || line.startsWith("#")) continue;
            int separator = line.indexOf('=');
            if (separator < 1) continue;
            String key = modernLanguageKey(line.substring(0, separator).strip());
            String value = line.substring(separator + 1);
            language.addProperty(key, value);
        }
        return language;
    }

    private static String modernLanguageKey(String key) {
        if (key.startsWith("item.basemetals.") && key.endsWith(".name")) {
            return key.substring(0, key.length() - ".name".length());
        }
        if (key.startsWith("tile.basemetals.") && key.endsWith(".name")) {
            return "block.basemetals." + key.substring("tile.basemetals.".length(),
                    key.length() - ".name".length());
        }
        if (key.startsWith("achievement.")) {
            String suffix = key.substring("achievement.".length());
            if (suffix.endsWith(".desc")) {
                return "advancements.basemetals." + suffix.substring(0, suffix.length() - 5) + ".description";
            }
            return "advancements.basemetals." + suffix + ".title";
        }
        return key;
    }

    private static void addIfAbsent(JsonObject language, String key, String value) {
        if (!language.has(key)) language.addProperty(key, value);
    }

    private void writeChestLoot(CachedOutput cache) throws IOException {
        Map<String, String> targets = new LinkedHashMap<>();
        targets.put("abandoned_mineshaft", "minecraft:chests/abandoned_mineshaft");
        targets.put("desert_pyramid", "minecraft:chests/desert_pyramid");
        targets.put("end_city_treasure", "minecraft:chests/end_city_treasure");
        targets.put("jungle_temple", "minecraft:chests/jungle_temple");
        targets.put("nether_bridge", "minecraft:chests/nether_bridge");
        targets.put("simple_dungeon", "minecraft:chests/simple_dungeon");
        targets.put("spawn_bonus_chest", "minecraft:chests/spawn_bonus_chest");
        targets.put("stronghold_corridor", "minecraft:chests/stronghold_corridor");
        targets.put("stronghold_crossing", "minecraft:chests/stronghold_crossing");
        targets.put("village_armorer", "minecraft:chests/village/village_armorer");
        targets.put("village_toolsmith", "minecraft:chests/village/village_toolsmith");
        targets.put("village_weaponsmith", "minecraft:chests/village/village_weaponsmith");
        Map<String, String> templates = new LinkedHashMap<>();
        targets.keySet().forEach(id -> templates.put(id, id));
        templates.put("village_armorer", "village_blacksmith");
        templates.put("village_toolsmith", "village_blacksmith");
        templates.put("village_weaponsmith", "village_blacksmith");
        JsonArray global = new JsonArray();
        for (Map.Entry<String, String> target : targets.entrySet()) {
            String id = "basemetals:" + target.getKey();
            global.add(id);
            JsonObject modifier = new JsonObject();
            modifier.addProperty("type", "basemetals:add_table");
            JsonArray conditions = new JsonArray();
            conditions.add(object("condition", "neoforge:loot_table_id", "loot_table_id", target.getValue()));
            modifier.add("conditions", conditions);
            modifier.addProperty("table", "basemetals:chests/inject/" + target.getKey());
            save(cache, modifier, "data/basemetals/loot_modifiers/" + target.getKey() + ".json");
            save(cache, legacyChestTable(templates.get(target.getKey())),
                    "data/basemetals/loot_table/chests/inject/" + target.getKey() + ".json");
        }
        JsonObject root = new JsonObject();
        root.addProperty("replace", false);
        root.add("entries", global);

        save(cache, root, "data/neoforge/loot_modifiers/global_loot_modifiers.json");
    }

    private JsonObject legacyChestTable(String template) throws IOException {
        Path source = legacyReference.resolve("alt/chests/" + template + ".json");
        if (!Files.isRegularFile(source)) {
            throw new IOException("Missing retained 1.12 chest-loot reference: " + source);
        }
        JsonObject table = JsonParser.parseString(Files.readString(source, StandardCharsets.UTF_8)).getAsJsonObject();
        modernizeLootJson(table);
        remapLegacyItems(table);
        table.addProperty("type", "minecraft:chest");
        return table;
    }

    /** Points 1.12 item names at their current IDs, as the save migration does. */
    private static void remapLegacyItems(JsonElement element) {
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(GeneratedData::remapLegacyItems);
            return;
        }
        if (!element.isJsonObject()) return;
        JsonObject object = element.getAsJsonObject();
        if (object.has("name") && object.get("name").isJsonPrimitive()
                && object.get("name").getAsString().startsWith(BaseMetals.MOD_ID + ":")) {
            String path = object.get("name").getAsString().substring(BaseMetals.MOD_ID.length() + 1);
            object.addProperty("name", zone.moddev.mc.basemetals.MissingMappings.itemTargetId(path).toString());
        }
        object.entrySet().forEach(entry -> remapLegacyItems(entry.getValue()));
    }

    private static void modernizeLootJson(JsonElement element) {
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(GeneratedData::modernizeLootJson);
            return;
        }
        if (!element.isJsonObject()) return;
        JsonObject object = element.getAsJsonObject();
        new ArrayList<>(object.keySet()).stream()
                .filter(key -> key.startsWith("__comment"))
                .forEach(object::remove);
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if ((entry.getKey().equals("type") || entry.getKey().equals("function")
                    || entry.getKey().equals("condition"))
                    && entry.getValue().isJsonPrimitive()
                    && entry.getValue().getAsJsonPrimitive().isString()
                    && !entry.getValue().getAsString().contains(":")) {
                entry.setValue(new com.google.gson.JsonPrimitive("minecraft:" + entry.getValue().getAsString()));
            } else {
                modernizeLootJson(entry.getValue());
            }
        }
    }

    private void writeManifest(CachedOutput cache) throws IOException {
        List<String> blocks = ModContent.blocksById().keySet().stream().map(id -> "basemetals:" + id).toList();
        List<String> items = ModContent.itemsById().keySet().stream().map(id -> "basemetals:" + id).toList();
        List<String> buckets = ModContent.fluids().keySet().stream()
                .map(id -> "basemetals:" + id + "_bucket").toList();
        List<String> sourceFluids = ModContent.fluids().keySet().stream()
                .map(id -> "basemetals:" + id).toList();
        List<String> flowingFluids = ModContent.fluids().keySet().stream()
                .map(id -> "basemetals:flowing_" + id).toList();

        Path runtimeManifest = legacyReference.resolve("runtime/registry_manifest.json");
        JsonObject legacy = JsonParser.parseString(Files.readString(runtimeManifest, StandardCharsets.UTF_8))
                .getAsJsonObject();
        save(cache, legacy, "data/basemetals/registry_manifest_1_12.json");

        List<String> restoredBlocks = difference(blocks, strings(legacy.getAsJsonArray("blocks")), List.of());
        List<String> newItems = items.stream().filter(id -> buckets.contains(id) || id.endsWith("_raw")).toList();
        List<String> restoredItems = difference(items, strings(legacy.getAsJsonArray("items")), newItems);
        List<String> restoredFluids = difference(sourceFluids, strings(legacy.getAsJsonArray("fluids")), List.of());

        JsonObject root = new JsonObject();
        root.addProperty("format", 1);
        root.addProperty("source", "Base Metals 1.19.4 catalogue");
        root.add("blocks", array(blocks));
        root.add("items", array(items));
        root.add("fluids", array(ModContent.fluids().keySet().stream().flatMap(id ->
                java.util.stream.Stream.of("basemetals:" + id, "basemetals:flowing_" + id)).toList()));
        root.add("entities", array("basemetals:custom_arrow", "basemetals:custom_bolt"));
        root.add("recipe_serializers", array(
                "mmdlib:crushing", "basemetals:legacy_smelting", "basemetals:plate_repair", "basemetals:content_crafting"));
        root.add("loot_modifier_serializers", array("basemetals:add_table"));
        root.add("restored_blocks", array(restoredBlocks));
        root.add("restored_items", array(restoredItems));
        root.add("restored_fluids", array(restoredFluids));
        root.add("new_items", array(newItems));
        root.add("new_fluids", array(flowingFluids));
        root.add("new_entities", array("basemetals:custom_arrow", "basemetals:custom_bolt"));
        root.add("new_recipe_serializers", array(
                "mmdlib:crushing", "basemetals:legacy_smelting", "basemetals:plate_repair", "basemetals:content_crafting"));
        root.add("new_loot_modifier_serializers", array("basemetals:add_table"));
        save(cache, root, "data/basemetals/registry_manifest.json");
    }

    private static List<String> difference(List<String> current, List<String> baseline, List<String> exclusions) {
        LinkedHashSet<String> remaining = new LinkedHashSet<>(current);
        remaining.removeAll(baseline);
        remaining.removeAll(exclusions);
        return List.copyOf(remaining);
    }

    private static List<String> strings(JsonArray values) {
        List<String> result = new ArrayList<>();
        values.forEach(value -> result.add(value.getAsString()));
        return result;
    }

    private void itemTag(CachedOutput cache, String family, String material, List<String> values) throws IOException {
        tag(cache, "forge", "items/" + family + "/" + material, values);
    }
    private void blockTag(CachedOutput cache, String family, String material, List<String> values) throws IOException {
        tag(cache, "forge", "blocks/" + family + "/" + material, values);
    }
    private void tag(CachedOutput cache, String namespace, String path, List<String> values) throws IOException {
        if (namespace.equals("forge")) namespace = "c";
        if (path.startsWith("items/")) path = "item/" + path.substring("items/".length());
        else if (path.startsWith("blocks/")) path = "block/" + path.substring("blocks/".length());
        else if (path.startsWith("fluids/")) path = "fluid/" + path.substring("fluids/".length());
        JsonObject root = new JsonObject(); root.addProperty("replace", false); root.add("values", array(values));
        save(cache, root, "data/" + namespace + "/tags/" + path + ".json");
    }
    private void optionalTag(CachedOutput cache, String namespace, String path, List<String> values) throws IOException {
        JsonArray entries = new JsonArray();
        values.forEach(value -> entries.add(object("id", value, "required", false)));
        JsonObject root = new JsonObject(); root.addProperty("replace", false); root.add("values", entries);
        save(cache, root, "data/" + namespace + "/tags/" + path + ".json");
    }
    private void uncheckedTag(CachedOutput cache, String namespace, String path, List<String> values) {
        try { tag(cache, namespace, path, values); } catch (IOException e) { throw new RuntimeException(e); }
    }
    private static void addAggregate(Map<String, List<String>> aggregate, String path, String value) {
        aggregate.computeIfAbsent(path, ignored -> new ArrayList<>()).add(value);
    }

    private void shaped(CachedOutput cache, String id, List<String> pattern, Map<Character, String> keys,
            String result, int count) throws IOException {
        JsonObject root = object("type", "minecraft:crafting_shaped");
        root.add("pattern", array(pattern));
        JsonObject key = new JsonObject();
        keys.entrySet().stream().sorted(Map.Entry.comparingByKey())
                .forEach(entry -> key.add(entry.getKey().toString(), ingredient(entry.getValue())));
        root.add("key", key); root.add("result", result(result, count));
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private void shapeless(CachedOutput cache, String id, List<String> ingredients, String result, int count) throws IOException {
        JsonObject root = object("type", "minecraft:crafting_shapeless");
        JsonArray values = new JsonArray(); ingredients.forEach(value -> values.add(ingredient(value)));
        root.add("ingredients", values); root.add("result", result(result, count));
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private void smelting(CachedOutput cache, String id, String input, String result, float xp, int time,
            String type) throws IOException {
        if (result.equals("basemetals:mercury_nugget")) {
            legacySmelting(cache, id, input, result, 1, xp, time);
            return;
        }
        JsonObject root = object("type", type, "ingredient", ingredient(input), "result", object("id", result),
                "experience", xp, "cookingtime", time);
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private void legacySmelting(CachedOutput cache, String id, String input, String result, int count,
            float xp, int time) throws IOException {
        JsonObject root = object("type", "basemetals:legacy_smelting",
                "ingredient", ingredient(input), "result", result(result, count),
                "experience", xp, "cookingtime", time);
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private void crushing(CachedOutput cache, String id, String input, String result, int count) throws IOException {
        JsonObject root = object("type", "mmdlib:crushing", "ingredient", ingredient(input),
                "result", result(result, count));
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private void plateRepair(CachedOutput cache, String id, String target, String plate) throws IOException {
        JsonObject root = object("type", "basemetals:plate_repair", "target", target,
                "plate", ingredient(plate));
        save(cache, root, "data/basemetals/recipe/" + id + ".json");
    }
    private static JsonObject ingredient(String value) {
        return value.startsWith("#") ? object("tag", value.substring(1)) : object("item", value);
    }
    private static JsonObject result(String item, int count) {
        JsonObject result = object("item", item); if (count != 1) result.addProperty("count", count); return result;
    }

    private static List<String> entries(String material, String suffix, String extra) {
        List<String> result = new ArrayList<>(); result.add("basemetals:" + material + "_" + suffix);
        if (extra != null) result.add(extra); return result;
    }
    private static List<String> entries(String material, String suffix) {
        return entries(material, suffix, (String) null);
    }
    private static List<String> entries(String material, String suffix, List<String> extras) {
        List<String> result = new ArrayList<>(); result.add("basemetals:" + material + "_" + suffix);
        result.addAll(extras); return result;
    }
    private static String advancementIcon(String id) {
        return switch (id) {
            case "blocktastic" -> "minecraft:copper_block";
            case "geologist" -> "basemetals:iron_crackhammer";
            case "metallurgy" -> "basemetals:bronze_blend";
            case "angel_of_death" -> "basemetals:mithril_sword";
            case "scuba_diver" -> "basemetals:aquarium_helmet";
            case "demon_slayer" -> "basemetals:coldiron_sword";
            case "juggernaut" -> "basemetals:adamantine_chestplate";
            case "moon_boots" -> "basemetals:starsteel_boots";
            default -> id.endsWith("_maker") ? "basemetals:" + id.substring(0, id.length() - 6) + "_blend"
                    : "minecraft:copper_ingot";
        };
    }

    private static String advancementParent(String id) {
        return switch (id) {
            case "this_is_new" -> "minecraft:story/smelt_iron";
            case "blocktastic", "geologist" -> "this_is_new";
            case "metallurgy" -> "geologist";
            case "demon_slayer", "juggernaut" -> "minecraft:story/enter_the_nether";
            case "moon_boots" -> "minecraft:end/root";
            case "angel_of_death" -> "mithril_maker";
            case "scuba_diver" -> "aquarium_maker";
            default -> "metallurgy";
        };
    }

    private static String advancementFrame(String id) {
        return switch (id) {
            case "angel_of_death", "scuba_diver", "demon_slayer", "juggernaut", "moon_boots" -> "challenge";
            case "this_is_new", "blocktastic", "geologist", "metallurgy" -> "task";
            default -> "goal";
        };
    }

    private static String title(String id) {
        StringBuilder result = new StringBuilder();
        for (String word : id.replace("coldiron", "cold_iron").replace("starsteel", "starsteel").split("_")) {
            if (result.length() > 0) result.append(' ');
            result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1).toLowerCase(Locale.ROOT));
        }
        return result.toString();
    }
    private void save(CachedOutput cache, JsonObject value, String relative) throws IOException {
        if (relative.startsWith("data/basemetals/advancement/")) {
            // Use Minecraft's codec so obsolete fields cannot silently break generated advancements.
            var registries = net.minecraft.core.HolderLookup.Provider.create(java.util.stream.Stream.of(
                    net.minecraft.core.registries.BuiltInRegistries.ITEM.asTagAddingLookup()));
            var ops = registries.createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
            var advancement = net.minecraft.advancements.Advancement.CODEC.parse(ops, value)
                    .getOrThrow(error -> new IllegalStateException(relative + ": " + error));
            if (relative.startsWith("data/basemetals/advancement/recipes/")
                    && advancement.rewards().recipes().isEmpty()) {
                throw new IllegalStateException("Recipe unlock has no recipe reward: " + relative);
            }
        }
        if (relative.startsWith("data/basemetals/recipe/") && value.has("result")
                && value.get("result").isJsonObject()) {
            String type = value.get("type").getAsString();
            JsonObject result = value.getAsJsonObject("result");
            if ((type.startsWith("minecraft:") || type.startsWith("basemetals:")) && result.has("item")) {
                result.add("id", result.remove("item"));
            }
        }
        if (relative.startsWith("data/basemetals/recipe/") && value.has("type")
                && value.get("type").getAsString().startsWith("minecraft:crafting_")) {
            String id = relative.substring("data/basemetals/recipe/".length(), relative.length() - 5);
            String result = value.getAsJsonObject("result").get("id").getAsString();
            net.minecraft.world.item.Item item = net.minecraft.core.registries.BuiltInRegistries.ITEM
                    .get(ResourceLocation.parse(result));
            String category = switch (zone.moddev.mc.basemetals.ModTabs.category(item)) {
                case "blocks" -> isRedstoneComponent(item) ? "redstone" : "building";
                case "tools", "combat" -> "equipment";
                default -> "misc";
            };
            value.addProperty("category", category);
            value.addProperty("group", value.getAsJsonObject("result").get("id").getAsString());
            writeCraftingUnlock(cache, id, value);
            value = object("type", "basemetals:content_crafting", "recipe", value);
        }
        pendingWrites.add(DataProvider.saveStable(cache, value, output.resolve(relative)));
    }

    /** Blocks vanilla's recipe book files under redstone rather than building. */
    private static boolean isRedstoneComponent(Item item) {
        return item instanceof net.minecraft.world.item.BlockItem blockItem
                && (blockItem.getBlock() instanceof BasePressurePlateBlock || blockItem.getBlock() instanceof DoorBlock
                        || blockItem.getBlock() instanceof TrapDoorBlock || blockItem.getBlock() instanceof ButtonBlock
                        || blockItem.getBlock() instanceof net.minecraft.world.level.block.LeverBlock
                        || blockItem.getBlock() instanceof net.minecraft.world.level.block.TripWireHookBlock);
    }

    private static String materialName(JsonObject language, String material) {
        String legacyKey = "material." + material + ".name";
        String key = language.has(legacyKey) ? legacyKey : "material.basemetals." + material;

        return language.get(key).getAsString();
    }

    private void writeOreLoot(CachedOutput cache, String ore) throws IOException {
        String material = ore.substring(0, ore.length() - 4);
        String raw = "basemetals:" + material + "_raw";
        JsonArray functions = new JsonArray();
        functions.add(object("function", "minecraft:apply_bonus", "enchantment", "minecraft:fortune",
                "formula", "minecraft:ore_drops"));
        functions.add(object("function", "minecraft:explosion_decay"));
        JsonObject rawEntry = object("type", "minecraft:item", "name", raw, "functions", functions);
        JsonArray enchantments = new JsonArray();
        enchantments.add(object("enchantments", "minecraft:silk_touch", "levels", object("min", 1)));
        JsonArray conditions = new JsonArray();
        conditions.add(object("condition", "minecraft:match_tool", "predicate",
                object("predicates", object("minecraft:enchantments", enchantments))));
        JsonArray children = new JsonArray();
        children.add(object("type", "minecraft:item", "name", "basemetals:" + ore, "conditions", conditions));
        children.add(rawEntry);
        JsonArray entries = new JsonArray();
        entries.add(object("type", "minecraft:alternatives", "children", children));
        JsonArray pools = new JsonArray();
        pools.add(object("name", "basemetals_" + ore, "rolls", 1, "entries", entries));
        save(cache, object("type", "minecraft:block", "pools", pools),
                "data/basemetals/loot_table/blocks/" + ore + ".json");
    }

    private void writeRawProcessing(CachedOutput cache) throws IOException {
        List<String> rawMaterials = new ArrayList<>(List.of("iron", "gold", "copper"));
        MaterialCatalogue.all().stream().filter(MaterialDefinition::hasOre)
                .map(MaterialDefinition::name).forEach(rawMaterials::add);

        for (String material : rawMaterials) {
            boolean vanilla = List.of("iron", "gold", "copper").contains(material);
            String raw = vanilla ? "minecraft:raw_" + material : "basemetals:" + material + "_raw";
            tag(cache, "forge", "items/raw_materials/" + material, List.of(raw));
            crushing(cache, material + "_raw_crushing", "#c:raw_materials/" + material,
                    "basemetals:" + material + "_powder", 1);
            if (vanilla) continue;

            for (String method : List.of("smelting", "blasting")) {
                String id = material + "_raw_" + method;
                smelting(cache, id, "#c:raw_materials/" + material, "basemetals:" + material + "_ingot",
                        MaterialCatalogue.get(material).oreSmeltingExperience(), method.equals("smelting") ? 200 : 100,
                        "minecraft:" + method);
                JsonObject criteria = object("has_raw", inventoryCriterion(ingredient("#c:raw_materials/" + material)),
                        "has_recipe", object("trigger", "minecraft:recipe_unlocked",
                                "conditions", object("recipe", "basemetals:" + id)));
                writeUnlock(cache, id, criteria);
            }
        }
        tag(cache, "forge", "items/raw_materials", rawMaterials.stream().map(name -> "#c:raw_materials/" + name).toList());
    }

    private void writeCraftingUnlock(CachedOutput cache, String id, JsonObject recipe) throws IOException {
        String material = id.substring(0, id.contains("_") ? id.indexOf('_') : id.length());
        if (List.of("activator_rail", "detector_rail", "flint_and_steel", "human_detector", "minecart",
                "piston", "rail", "tripwire_hook").contains(id)) material = "steel";
        String discovery = switch (material) {
            case "diamond", "emerald", "quartz", "lapis" -> "#c:gems/" + material;
            case "coal", "charcoal" -> "minecraft:" + material;
            case "stone" -> "#c:stones";
            case "wood" -> "#minecraft:logs";
            case "redstone" -> "#c:dusts/redstone";
            default -> "#c:ingots/" + material;
        };
        JsonObject criteria = object("has_material", inventoryCriterion(ingredient(discovery)),
                "has_recipe", object("trigger", "minecraft:recipe_unlocked", "conditions", object("recipe", "basemetals:" + id)));
        List<JsonElement> inputs = new ArrayList<>();
        if (recipe.has("key")) recipe.getAsJsonObject("key").entrySet().forEach(entry -> inputs.add(entry.getValue()));
        else recipe.getAsJsonArray("ingredients").forEach(inputs::add);
        int index = 0;
        java.util.Set<String> seen = new LinkedHashSet<>();
        List<JsonElement> choices = new ArrayList<>();
        for (JsonElement input : inputs) {
            if (input.isJsonArray()) input.getAsJsonArray().forEach(choices::add);
            else choices.add(input);
        }
        List<JsonElement> materialChoices = choices.stream().filter(JsonElement::isJsonObject).filter(input -> {
            JsonObject value = input.getAsJsonObject();
            String tag = value.has("tag") ? value.get("tag").getAsString() : "";
            String item = value.has("item") ? value.get("item").getAsString() : "";
            return !tag.equals("c:rods/wooden")
                    && !List.of("minecraft:stick", "minecraft:string", "minecraft:feather").contains(item);
        }).toList();
        for (JsonElement input : materialChoices.isEmpty() ? choices : materialChoices) {
            if (!input.isJsonObject() || !seen.add(input.toString())) continue;
            criteria.add("has_ingredient_" + (++index), inventoryCriterion(input.getAsJsonObject()));
        }
        writeUnlock(cache, id, criteria);
    }

    private void writeUnlock(CachedOutput cache, String id, JsonObject criteria) throws IOException {
        JsonArray requirements = new JsonArray();
        requirements.add(array(criteria.keySet()));
        save(cache, object("parent", "minecraft:recipes/root", "criteria", criteria,
                "requirements", requirements, "rewards", object("recipes", array("basemetals:" + id))),
                "data/basemetals/advancement/recipes/" + id + ".json");
    }

    private static JsonObject inventoryCriterion(JsonObject ingredient) {
        // Item predicates use a holder set in 1.21: a tag is encoded as "#namespace:path".
        JsonObject predicate = ingredient.has("tag")
                ? object("items", "#" + ingredient.get("tag").getAsString())
                : object("items", array(ingredient.get("item").getAsString()));
        JsonArray items = new JsonArray();
        items.add(predicate);
        return object("trigger", "minecraft:inventory_changed", "conditions", object("items", items));
    }
    private static JsonArray array(String... values) { return array(List.of(values)); }
    private static JsonArray array(Iterable<String> values) {
        JsonArray array = new JsonArray(); values.forEach(array::add); return array;
    }
    private static JsonObject object(Object... values) {
        JsonObject result = new JsonObject();
        for (int i = 0; i < values.length; i += 2) {
            String key = (String) values[i]; Object value = values[i + 1];
            if (value instanceof String text) result.addProperty(key, text);
            else if (value instanceof Number number) result.addProperty(key, number);
            else if (value instanceof Boolean bool) result.addProperty(key, bool);
            else if (value instanceof com.google.gson.JsonElement json) result.add(key, json);
            else throw new IllegalArgumentException("Unsupported JSON value " + value);
        }
        return result;
    }

    @Override public String getName() { return "Base Metals catalogue data"; }
}
