package cyano.basemetals.data;

/**
 * 
 * @author DrCyano
 *
 */
public abstract class DataConstants {

	public static final String defaultOreSpawnJSON = "{"
			+ "\n	\"dimensions\":["
			+ "\n		{"
			+ "\n			\"dimension\":\"+\","
			+ "\n			\"__comment\":\""
			+ "dimension 0 for overworld, -1 for the nether, 1 for the end, other numbers "
			+ "for dimensions added by other mods, and + for any dimension not already "
			+ "described by this file.\","
			+ "\n			\"ores\":["
			+ "\n				{"
			+ "\n					\"__comment\":\"These settings are roughly equivalent to the "
			+ "parameters of the CUSTOM world json: blockID = the unlocalized name of a "
			+ "block in the format 'ModID:blockName',blockMeta = the meta data value for "
			+ "the block (optional),size = number of blocks to spawn per ore deposit, "
			+ "variation = vary the size of deposits by +/- this amount, frequency = number "
			+ "of deposits per chunk (can be less than 1, e.g. 0.5 for 1 deposit every 2 "
			+ "chunks), minHeight = minimum spawn height, maxHeight = maximum spawn height, "
			+ "biomes = array of biomes to spawn this ore in (if empty or absent, the ore "
			+ "will spawn in all biomes). Note that the size values in the Minecraft CUSTOM "
			+ "world json do not represent the actual number of blocks spawned, where-as the "
			+ "size value here does represent actual number of blocks. \","
			+ "\n					\"blockID\":\"basemetals:copper_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":10,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":96"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:silver_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":4,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":32"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:tin_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":10,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":128"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:lead_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":5,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":64"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:zinc_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":5,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":96"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:mercury_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":3,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":32"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:nickel_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":1,"
			+ "\n					\"minHeight\":32,"
			+ "\n					\"maxHeight\":96"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:platinum_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":0.125,"
			+ "\n					\"minHeight\":1,"
			+ "\n					\"maxHeight\":32"
			+ "\n				}"
			+ "\n			]"
			+ "\n		},"
			+ "\n		{"
			+ "\n			\"dimension\":-1,"
			+ "\n			\"__comment\":\"the nether\","
			+ "\n			\"ores\":["
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:coldiron_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":5,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":128"
			+ "\n				},"
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:adamantine_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":2,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":128"
			+ "\n				}"
			+ "\n			]"
			+ "\n		},"
			+ "\n		{"
			+ "\n			\"dimension\":1,"
			+ "\n			\"__comment\":\"the end\","
			+ "\n			\"ores\":["
			+ "\n				{"
			+ "\n					\"blockID\":\"basemetals:starsteel_ore\","
			+ "\n					\"size\":8,"
			+ "\n					\"variation\":4,"
			+ "\n					\"frequency\":5,"
			+ "\n					\"minHeight\":0,"
			+ "\n					\"maxHeight\":255"
			+ "\n				}"
			+ "\n			]"
			+ "\n		}"
			+ "\n	]"
			+ "\n}";

	public static final String abandoned_mineshaft = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance per roll of common metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 4\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 63\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:lead_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:zinc_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:brass_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of a common-metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 450\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String nether_bridge = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of an uncommon metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 900\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of rare metal ingots\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 300\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_ingot\",\n" +
			"                    \"weight\": 40,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_ingot\",\n" +
			"                    \"weight\": 25,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_ingot\",\n" +
			"                    \"weight\": 20,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:platinum_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_ingot\",\n" +
			"                    \"weight\": 5,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String stronghold_crossing = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of a common-metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 450\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"10% chance per roll of uncommon metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 2\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 162\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:gold_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:silver_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:nickel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:electrum_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 3,\n" +
			"                                \"max\": 12\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String desert_pyramid = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of an uncommon metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 900\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"20% chance per roll of uncommon metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 2\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 72\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:gold_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:silver_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:nickel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:electrum_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 3,\n" +
			"                                \"max\": 12\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String simple_dungeon = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of an uncommon metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 900\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"10% chance per roll of uncommon metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 2\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 162\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:gold_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:silver_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:nickel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:electrum_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 3,\n" +
			"                                \"max\": 12\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String village_blacksmith = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of a common-metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 450\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"10% chance per roll of common metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 4\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 189\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:lead_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:zinc_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:brass_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String end_city_treasure = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of a rare metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 540\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 20,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of rare metal ingots\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 300\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_ingot\",\n" +
			"                    \"weight\": 40,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_ingot\",\n" +
			"                    \"weight\": 25,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_ingot\",\n" +
			"                    \"weight\": 20,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:platinum_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_ingot\",\n" +
			"                    \"weight\": 5,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String spawn_bonus_chest = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"100% chance of one common-metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:tin_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:copper_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String jungle_temple = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of an uncommon metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 900\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"20% chance per roll of uncommon metal ingots\",\n" +
			"            \"rolls\": {\n" +
			"                \"min\": 1,\n" +
			"                \"max\": 2\n" +
			"            },\n" +
			"            \"bonus_rolls\":1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 72\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:gold_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:silver_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:nickel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:electrum_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 3,\n" +
			"                                \"max\": 12\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_ingot\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
	public static final String stronghold_corridor = "{\n" +
			"    \"pools\": [\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of an uncommon metal tool\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 900\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"minecraft:iron_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:iron_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:bronze_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_axe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_boots\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_chestplate\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_helmet\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_hoe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_leggings\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_pickaxe\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_shovel\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_sword\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:steel_crackhammer\",\n" +
			"                    \"weight\": 3,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"minecraft:enchant_with_levels\",\n" +
			"                            \"levels\": 10,\n" +
			"                            \"treasure\": true\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:invar_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                \n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_axe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_boots\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_chestplate\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_helmet\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_hoe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_leggings\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_pickaxe\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_shovel\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_sword\",\n" +
			"                    \"weight\": 3\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_crackhammer\",\n" +
			"                    \"weight\": 3\n" +
			"                }\n" +
			"            ]\n" +
			"        },\n" +
			"        {\n" +
			"            \"__comment\":\"25% chance of rare metal ingots\",\n" +
			"            \"rolls\": 1,\n" +
			"            \"entries\": [\n" +
			"                {\n" +
			"                    \"type\": \"empty\",\n" +
			"                    \"weight\": 300\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:coldiron_ingot\",\n" +
			"                    \"weight\": 40,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:aquarium_ingot\",\n" +
			"                    \"weight\": 25,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:mithril_ingot\",\n" +
			"                    \"weight\": 20,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:platinum_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 6\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:adamantine_ingot\",\n" +
			"                    \"weight\": 10,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                },\n" +
			"                {\n" +
			"                    \"type\": \"item\",\n" +
			"                    \"name\": \"basemetals:starsteel_ingot\",\n" +
			"                    \"weight\": 5,\n" +
			"                    \"functions\": [\n" +
			"                        {\n" +
			"                            \"function\": \"set_count\",\n" +
			"                            \"count\": {\n" +
			"                                \"min\": 1,\n" +
			"                                \"max\": 4\n" +
			"                            }\n" +
			"                        }\n" +
			"                    ]\n" +
			"                }\n" +
			"            ]\n" +
			"        }\n" +
			"    ]\n" +
			"}";
}