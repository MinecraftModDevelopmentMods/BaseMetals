package com.mcmoddev.basemetals.util;

import java.io.File;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.ConfigKeys;
import com.mcmoddev.lib.integration.plugins.DenseOres;
import com.mcmoddev.lib.integration.plugins.EnderIO;
import com.mcmoddev.lib.integration.plugins.IC2;
import com.mcmoddev.lib.integration.plugins.Mekanism;
import com.mcmoddev.lib.integration.plugins.Thaumcraft;
import com.mcmoddev.lib.integration.plugins.ThermalExpansion;
import com.mcmoddev.lib.integration.plugins.TinkersConstruct;
import com.mcmoddev.lib.integration.plugins.VeinMiner;
import com.mcmoddev.lib.util.Config;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Jasmine Iwanek
 *
 */
public final class BMeConfig extends Config {

	private static Configuration configuration;
	private static final String CONFIG_FILE = "config/BaseMetals.cfg";
	private static final String GENERAL_CAT = "General";
	private static final String INTEGRATION_CAT = "Mod Integration";
	private static final String MATERIALS_CAT = "Metals";
	private static final String VANILLA_CAT = "Vanilla";
	private static final String HAMMER_RECIPES_CAT = "Crack Hammer Recipes";
	private static final String TOOLS_CAT = "Tools and Items";
	private static final String FLUIDS_CAT = "Fluids";

	/**
	 * Fired when the configuration changes.
	 *
	 * @param event The Event.
	 */
	@SubscribeEvent
	public void onConfigChange(final ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(BaseMetals.MODID)) {
			init();
		}
	}

	/**
	 *
	 */
	public static void init() {
		if (configuration == null) {
			configuration = new Configuration(new File(CONFIG_FILE));
			MinecraftForge.EVENT_BUS.register(new BMeConfig());
		}

		// GENERAL
		Options.setDisableAllHammerRecipes(configuration.getBoolean("disable_crack_hammer",
				GENERAL_CAT, false, "If true, then the crack hammer cannot be crafted."));
		Options.setEnforceHardness(configuration.getBoolean("enforce_hardness", GENERAL_CAT, true,
				"If true, then the crack hammer cannot crush ingots into powders if that \n"
						+ "crackhammer is not hard enough to crush the ingot's ore."));
		Options.setStrongHammers(configuration.getBoolean("strong_hammers", GENERAL_CAT, true,
				"If true, then the crack hammer can crush ingots/ores that a pickaxe of the same \n"
						+ "material can harvest. If false, then your crack hammer must be made of a harder \n"
						+ "material than the ore you are crushing."));
		Options.setAutoDetectRecipes(configuration.getBoolean("automatic_recipes", GENERAL_CAT,
				true,
				"If true, then Base Metals will scan the Ore Dictionary to automatically add a \n"
						+ "Crack Hammer recipe for every material that has an ore, dust, and ingot."));
		Options.setRequireMMDOreSpawn(configuration.getBoolean("using_orespawn", GENERAL_CAT, true,
				"If false, then Base Metals will not require MMD Ore Spawn mod. \n"
						+ "Set to false if using another mod to manually handle ore generation."));
		Options.setEnableAchievements(configuration.getBoolean("achievements", GENERAL_CAT, true,
				"If false, then Base Metals Achievements will be disabled (This is currently required if you disable any metals"));
		Options.setCrackHammerFullStack(
				configuration.getBoolean("crackhammer_full_stacks", GENERAL_CAT, false,
						"If true then you can crackhammer full stacks of dropped items."));
		Options.setEnablePlateRepairs(configuration.getBoolean("repair_using_plates", GENERAL_CAT,
				true, "Repair shields and armor with metal plates of the same type"));
		Options.setEnableShieldUpgrades(configuration.getBoolean("upgrade_shields", GENERAL_CAT,
				true,
				"Upgrade a fully repaired shield to a material at least as hard as the shields current one using a plate of that material in the Anvil"));
		Options.setTwoDustDrop(configuration.getBoolean("hammer_produces_two_dust", GENERAL_CAT,
				true,
				"If true, then the crack hammer when crushing ingots/ores two dust will drop"));
		// fallback orespawn can live as a 'thingEnabled' for a bit...
		Options.setFallbackOreSpawn(configuration.getBoolean("fallback_orespawn", GENERAL_CAT, true,
				"disable this and using_orespawn to make MMDLib based mods not spawn any ores"));

		// INTEGRATION
		Options.modEnabled(EnderIO.PLUGIN_MODID,
				configuration.getBoolean("ender_io_integration", INTEGRATION_CAT, true,
						"If false, then Base Metals will not try and integrate with Ender IO"));
		Options.modEnabled(IC2.PLUGIN_MODID,
				configuration.getBoolean("ic2_integration", INTEGRATION_CAT, true,
						"If false, then Base Metals will not try and integrate with IC2"));
		Options.modEnabled(Mekanism.PLUGIN_MODID,
				configuration.getBoolean("mekanism_integration", INTEGRATION_CAT, true,
						"If false, then Base Metals will not try and integrate with Mekanism"));
		Options.modEnabled(Thaumcraft.PLUGIN_MODID,
				configuration.getBoolean("thaumcraft_integration", INTEGRATION_CAT, true,
						"If false, then Base Metals will not try and integrate with Thaumcraft"));
		Options.modEnabled(TinkersConstruct.PLUGIN_MODID, configuration.getBoolean(
				"tinkers_construct_integration", INTEGRATION_CAT, true,
				"If false, then Base Metals will not try and integrate with Tinkers Construct"));
		Options.modEnabled(VeinMiner.PLUGIN_MODID,
				configuration.getBoolean("veinminer_integration", INTEGRATION_CAT, true,
						"If false, then Base Metals will not try and integrate with VeinMiner"));
		Options.modEnabled("taiga", configuration.getBoolean("taiga_integration",
				INTEGRATION_CAT, true,
				"Requires that Tinkers' Construct integration also be on. If false, TAIGA provided materials and traits will not be available in Base Metals"));
		Options.modEnabled(DenseOres.PLUGIN_MODID,
				configuration.getBoolean("denseores", INTEGRATION_CAT, true,
						"If DenseOres is available, this will allow automatic integration"));
		Options.modEnabled(ThermalExpansion.PLUGIN_MODID, configuration.getBoolean(
				"thermal_expansion", INTEGRATION_CAT, true,
				"If Thermal Expansion is available, this wil automatically integrate materials with the various machines"));

		// METALS
		Options.materialEnabled(MaterialNames.ADAMANTINE, configuration.getBoolean(
				"EnableAdamantine", MATERIALS_CAT, true, "Enable Adamantine Items and Materials"));
		Options.materialEnabled(MaterialNames.ANTIMONY, configuration.getBoolean("EnableAntimony",
				MATERIALS_CAT, true, "Enable Antimony Items and Materials"));
		Options.materialEnabled(MaterialNames.AQUARIUM, configuration.getBoolean("EnableAquarium",
				MATERIALS_CAT, true, "Enable Aquarium Items and Materials"));
		Options.materialEnabled(MaterialNames.BISMUTH, configuration.getBoolean("EnableBismuth",
				MATERIALS_CAT, true, "Enable Bismuth Items and Materials"));
		Options.materialEnabled(MaterialNames.BRASS, configuration.getBoolean("EnableBrass",
				MATERIALS_CAT, true, "Enable Brass Items and Materials"));
		Options.materialEnabled(MaterialNames.BRONZE, configuration.getBoolean("EnableBronze",
				MATERIALS_CAT, true, "Enable Bronze Items and Materials"));
		Options.materialEnabled(MaterialNames.CHARCOAL, configuration.getBoolean("EnableCharcoal",
				MATERIALS_CAT, true, "Enable Charcoal Items and Materials"));
		Options.materialEnabled(MaterialNames.COAL, configuration.getBoolean("EnableCoal",
				MATERIALS_CAT, true, "Enable Coal Items and Materials"));
		Options.materialEnabled(MaterialNames.COLDIRON, configuration.getBoolean("EnableColdIron",
				MATERIALS_CAT, true, "Enable ColdIron Items and Materials"));
		Options.materialEnabled(MaterialNames.COPPER, configuration.getBoolean("EnableCopper",
				MATERIALS_CAT, true, "Enable Copper Items and Materials"));
		Options.materialEnabled(MaterialNames.CUPRONICKEL,
				configuration.getBoolean("EnableCupronickel", MATERIALS_CAT, true,
						"Enable Cupronickel Items and Materials"));
		Options.materialEnabled(MaterialNames.ELECTRUM, configuration.getBoolean("EnableElectrum",
				MATERIALS_CAT, true, "Enable Electrum Items and Materials"));
		Options.materialEnabled(MaterialNames.INVAR, configuration.getBoolean("EnableInvar",
				MATERIALS_CAT, true, "Enable Invar Items and Materials"));
		Options.materialEnabled(MaterialNames.LEAD, configuration.getBoolean("EnableLead",
				MATERIALS_CAT, true, "Enable Lead Items and Materials"));
		Options.materialEnabled(MaterialNames.MERCURY, configuration.getBoolean("EnableMercury",
				MATERIALS_CAT, true, "Enable Mercury Items and Materials"));
		Options.materialEnabled(MaterialNames.MITHRIL, configuration.getBoolean("EnableMithril",
				MATERIALS_CAT, true, "Enable Mithril Items and Materials"));
		Options.materialEnabled(MaterialNames.NICKEL, configuration.getBoolean("EnableNickel",
				MATERIALS_CAT, true, "Enable Nickel Items and Materials"));
		Options.materialEnabled(MaterialNames.PEWTER, configuration.getBoolean("EnablePewter",
				MATERIALS_CAT, true, "Enable Pewter Items and Materials"));
		Options.materialEnabled(MaterialNames.PLATINUM, configuration.getBoolean("EnablePlatinum",
				MATERIALS_CAT, true, "Enable Platinum Items and Materials"));
		Options.materialEnabled(MaterialNames.SILVER, configuration.getBoolean("EnableSilver",
				MATERIALS_CAT, true, "Enable Silver Items and Materials"));
		Options.materialEnabled(MaterialNames.STARSTEEL, configuration.getBoolean("EnableStarSteel",
				MATERIALS_CAT, true, "Enable StarSteel Items and Materials"));
		Options.materialEnabled(MaterialNames.STEEL, configuration.getBoolean("EnableSteel",
				MATERIALS_CAT, true, "Enable Steel Items and Materials"));
		Options.materialEnabled(MaterialNames.TIN, configuration.getBoolean("EnableTin",
				MATERIALS_CAT, true, "Enable Tin Items and Materials"));
		Options.materialEnabled(MaterialNames.ZINC, configuration.getBoolean("EnableZinc",
				MATERIALS_CAT, true, "Enable Zinc Items and Materials"));

		// VANILLA
		Options.materialEnabled(MaterialNames.DIAMOND, configuration.getBoolean("EnableDiamond",
				VANILLA_CAT, true, "Enable Diamond Items and Materials"));
		Options.materialEnabled(MaterialNames.EMERALD, configuration.getBoolean("EnableEmerald",
				VANILLA_CAT, true, "Enable Emerald Items and Materials"));
		Options.materialEnabled(MaterialNames.GOLD, configuration.getBoolean("EnableGold",
				VANILLA_CAT, true, "Enable Gold Items and Materials"));
		Options.materialEnabled(MaterialNames.IRON, configuration.getBoolean("EnableIron",
				VANILLA_CAT, true, "Enable Iron Items and Materials"));
		Options.materialEnabled(MaterialNames.STONE, configuration.getBoolean("EnableStone",
				VANILLA_CAT, true, "Enable Stone Items and Materials"));
		Options.materialEnabled(MaterialNames.WOOD, configuration.getBoolean("EnableWood",
				VANILLA_CAT, true, "Enable Wood Items and Materials"));
		Options.materialEnabled(MaterialNames.ENDER, configuration.getBoolean("EnableEnder",
				VANILLA_CAT, true, "Enable Ender Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.QUARTZ,
				configuration.getBoolean("EnableQuartz", VANILLA_CAT, true,
						"Enable Nether Quartz Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.OBSIDIAN, configuration.getBoolean("EnableObsidian",
				VANILLA_CAT, true, "Enable Obsidian Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.LAPIS,
				configuration.getBoolean("EnableLapis", VANILLA_CAT, true,
						"Enable Lapis Lazuli Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.PRISMARINE,
				configuration.getBoolean("EnablePrismarine", VANILLA_CAT, true,
						"Enable Prismarine Items and Materials (not currently in use)"));
		Options.materialEnabled(MaterialNames.REDSTONE, configuration.getBoolean("EnableRedstone",
				VANILLA_CAT, true, "Enable Redstone Items and Materials (not currently in use)"));

		// RECIPE AMOUNTS/TOOL&ITEM DISABLING
		Options.setGearQuantity(configuration.getInt("Gear Quantity", TOOLS_CAT, 4, 1, 64,
				"Number of Gears per recipe"));
		Options.setPlateQuantity(configuration.getInt("Plate Quantity", TOOLS_CAT, 3, 1, 64,
				"Number of Plates per recipe"));
		Options.setRodQuantity(configuration.getInt("Rod Quantity", TOOLS_CAT, 4, 1, 64,
				"Number of Rods per recipe"));
		Options.setFurnaceCheese(configuration.getBoolean("Furnace Cheese", TOOLS_CAT, true,
				"Melt down armor and tools for full value"));
		Options.setFurnace1112(configuration.getBoolean("Furnace1112", TOOLS_CAT, true,
				"Mimic 1.11.2 armor and tool melting.\n Overridden by Furnace Cheese"));

		Options.thingEnabled(ConfigKeys.BASICS, configuration.getBoolean("Enable Basics", TOOLS_CAT,
				true,
				"Set to false to disable: Nuggets, Ingots, Powders, Blends, Blocks and Ores"));
		Options.thingEnabled(ConfigKeys.BASIC_TOOLS, configuration.getBoolean("Enable Basic Tools",
				TOOLS_CAT, true, "Set to false to disable: Axe, Hoe, Pickaxe, Shovel and Sword"));
		Options.thingEnabled(ConfigKeys.BOW_AND_ARROW,
				configuration.getBoolean("Enable Bow and Arrow", TOOLS_CAT, true,
						"Set to false to turn off custom bows and arrows"));
		Options.thingEnabled(ConfigKeys.CROSSBOW_AND_BOLT,
				configuration.getBoolean("Enable Crossbow and Bolts", TOOLS_CAT, true,
						"Set to false to disable custom crossbows and bolts"));
		Options.thingEnabled(ConfigKeys.ARMOR, configuration.getBoolean("Enable Armor", TOOLS_CAT,
				true, "Set to false to disable: Helmet, Chestplate, Leggings and Boots"));
		Options.thingEnabled(ConfigKeys.CRACKHAMMER, configuration.getBoolean("Enable Crackhammer",
				TOOLS_CAT, true,
				"Why would you want to disable a valuable early-game tool ?\nThe question we're all asking though is will it blend?"));
		Options.thingEnabled(ConfigKeys.FISHING_ROD, configuration.getBoolean("Enable Fishing Rod",
				TOOLS_CAT, true, "Set to false to turn off added fishing rods"));
		Options.thingEnabled(ConfigKeys.HORSE_ARMOR, configuration.getBoolean("Enable Horse Armor",
				TOOLS_CAT, true, "Set to false to disable extra Horse Armor"));
		Options.thingEnabled(ConfigKeys.SHEARS, configuration.getBoolean("Enable Shears", TOOLS_CAT,
				true, "I love Shears, do you? If you're a Shears hater, set this to false"));
		Options.thingEnabled(ConfigKeys.SMALL_DUST, configuration.getBoolean("Enable Small Dust",
				TOOLS_CAT, true,
				"Turn this off to disable nugget-sized piles of dust.\n(Dust is a drug and drugs are bad. Don't do drugs, mmm-kay ?"));
		Options.thingEnabled(ConfigKeys.ROD, configuration.getBoolean("Enable Rod", TOOLS_CAT, true,
				"Spare the Rod... Wait, no, that's Biblical...\nThis controls whether or not rods of various materials (similar to Tinkers' Construct Tool Rod) sare available"));
		Options.thingEnabled(ConfigKeys.GEAR, configuration.getBoolean("Enable Gear", TOOLS_CAT,
				true,
				"A lot of mods have Gears, we can provide them. Turn this off if you think you don't need them."));
		Options.thingEnabled(ConfigKeys.SHIELD, configuration.getBoolean("Enable Shield", TOOLS_CAT,
				true,
				"The Combat Update brought Shields to Vanilla Minecraft. Turn this off if you don't want them to multiply"));
		Options.thingEnabled(ConfigKeys.BARS, configuration.getBoolean("Enable Bars", TOOLS_CAT,
				true, "No, not the kind you drink at. The kind you find on jail-cells."));
		Options.thingEnabled(ConfigKeys.PLATE, configuration.getBoolean("Enable Plates", TOOLS_CAT,
				true,
				"IC2, Tech Reborn and a number of other mods require plates of material. This provides recipes for them."));
		Options.thingEnabled(ConfigKeys.DOOR, configuration.getBoolean("Enable Door", TOOLS_CAT,
				true, "Doors of many wondrous materials"));
		Options.thingEnabled(ConfigKeys.TRAPDOOR, configuration.getBoolean("Enable Trapdoors",
				TOOLS_CAT, true, "Do you want trapdoors? Because that's how you get trapdoors!"));
		Options.thingEnabled(ConfigKeys.BUTTON, configuration.getBoolean("Enable Buttons",
				TOOLS_CAT, true, "Ooooh, what does this button do?"));
		Options.thingEnabled(ConfigKeys.SLAB, configuration.getBoolean("Enable Slab", TOOLS_CAT,
				true, "Slabs of all the materials that get added"));
		Options.thingEnabled(ConfigKeys.LEVER, configuration.getBoolean("Enable Lever", TOOLS_CAT,
				true, "Levers of all different materials - they even have different hardness's"));
		Options.thingEnabled(ConfigKeys.PRESSURE_PLATE,
				configuration.getBoolean("Enable Pressure-plates", TOOLS_CAT, true,
						"Now your traps can be hidden in even more places!"));
		Options.thingEnabled(ConfigKeys.STAIRS, configuration.getBoolean("Enable Stairs", TOOLS_CAT,
				true,
				"Stairs of our wonderful metals! Come and get your own - or don't. It's up to you."));
		Options.thingEnabled(ConfigKeys.WALL, configuration.getBoolean("Enable Wall", TOOLS_CAT,
				true, "Hey, Teachers! Leave those kids alone!"));
		Options.thingEnabled(ConfigKeys.EXPERIMENTAL, configuration.getBoolean(
				"Enable Experimental", TOOLS_CAT, false,
				"Enable experimental features (Don't blame us if they burn down your cat or kill your house)"));
		// Add some utility bits that are referenced
		Options.thingEnabled(ConfigKeys.ANVIL, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.BOOKSHELF, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.FLOWERPOT, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.LADDER, Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.TRIPWIRE_HOOK,
				Options.isThingEnabled(ConfigKeys.EXPERIMENTAL));
		Options.thingEnabled(ConfigKeys.SCYTHE,
				configuration.getBoolean("Enable Scythe", TOOLS_CAT, false, "Enable Scythe"));

		// Fluid options
		Options.fluidEnabled("Charcoal", configuration.getBoolean("Enabled Charcoal", FLUIDS_CAT,
				true, "Enable the molten fluid of Charcoal"));
		Options.fluidEnabled("Coal", configuration.getBoolean("Enabled Coal", FLUIDS_CAT, true,
				"Enable the molten fluid of Coal"));
		Options.fluidEnabled("Diamond", configuration.getBoolean("Enabled Diamond", FLUIDS_CAT,
				false, "Enable the molten fluid of Diamond"));
		Options.fluidEnabled("Emerald", configuration.getBoolean("Enabled Emerald", FLUIDS_CAT,
				false, "Enable the molten fluid of Emerald"));
		Options.fluidEnabled("Gold", configuration.getBoolean("Enabled Gold", FLUIDS_CAT, false,
				"Enable the molten fluid of Gold"));
		Options.fluidEnabled("Iron", configuration.getBoolean("Enabled Iron", FLUIDS_CAT, false,
				"Enable the molten fluid of Iron"));
		Options.fluidEnabled("Obsidian", configuration.getBoolean("Enabled Obsidian", FLUIDS_CAT,
				false, "Enable the molten fluid of Obsidian"));
		Options.fluidEnabled("Prismarine", configuration.getBoolean("Enabled Prismarine",
				FLUIDS_CAT, true, "Enable the molten fluid of Prismarine"));
		Options.fluidEnabled("Redstone", configuration.getBoolean("Enabled Redstone", FLUIDS_CAT,
				false, "Enable the molten fluid of Redstone"));
		Options.fluidEnabled("Adamantine", configuration.getBoolean("Enabled Adamantine",
				FLUIDS_CAT, true, "Enable the molten fluid of Adamantine"));
		Options.fluidEnabled("Antimony", configuration.getBoolean("Enabled Antimony", FLUIDS_CAT,
				true, "Enable the molten fluid of Antimony"));
		Options.fluidEnabled("Aquarium", configuration.getBoolean("Enabled Aquarium", FLUIDS_CAT,
				true, "Enable the molten fluid of Aquarium"));
		Options.fluidEnabled("Bismuth", configuration.getBoolean("Enabled Bismuth", FLUIDS_CAT,
				true, "Enable the molten fluid of Bismuth"));
		Options.fluidEnabled("Brass", configuration.getBoolean("Enabled Brass", FLUIDS_CAT, true,
				"Enable the molten fluid of Brass"));
		Options.fluidEnabled("Bronze", configuration.getBoolean("Enabled Bronze", FLUIDS_CAT, true,
				"Enable the molten fluid of Bronze"));
		Options.fluidEnabled("ColdIron", configuration.getBoolean("Enabled ColdIron", FLUIDS_CAT,
				true, "Enable the molten fluid of ColdIron"));
		Options.fluidEnabled("Copper", configuration.getBoolean("Enabled Copper", FLUIDS_CAT, true,
				"Enable the molten fluid of Copper"));
		Options.fluidEnabled("Cupronickel", configuration.getBoolean("Enabled Cupronickel",
				FLUIDS_CAT, true, "Enable the molten fluid of Cupronickel"));
		Options.fluidEnabled("Electrum", configuration.getBoolean("Enabled Electrum", FLUIDS_CAT,
				true, "Enable the molten fluid of Electrum"));
		Options.fluidEnabled("Invar", configuration.getBoolean("Enabled Invar", FLUIDS_CAT, true,
				"Enable the molten fluid of Invar"));
		Options.fluidEnabled("Lead", configuration.getBoolean("Enabled Lead", FLUIDS_CAT, true,
				"Enable the molten fluid of Lead"));
		Options.fluidEnabled("Mithril", configuration.getBoolean("Enabled Mithril", FLUIDS_CAT,
				true, "Enable the molten fluid of Mithril"));
		Options.fluidEnabled("Nickel", configuration.getBoolean("Enabled Nickel", FLUIDS_CAT, true,
				"Enable the molten fluid of Nickel"));
		Options.fluidEnabled("Pewter", configuration.getBoolean("Enabled Pewter", FLUIDS_CAT, true,
				"Enable the molten fluid of Pewter"));
		Options.fluidEnabled("Platinum", configuration.getBoolean("Enabled Platinum", FLUIDS_CAT,
				true, "Enable the molten fluid of Platinum"));
		Options.fluidEnabled("Silver", configuration.getBoolean("Enabled Silver", FLUIDS_CAT, true,
				"Enable the molten fluid of Silver"));
		Options.fluidEnabled("StarSteel", configuration.getBoolean("Enabled StarSteel", FLUIDS_CAT,
				true, "Enable the molten fluid of StarSteel"));
		Options.fluidEnabled("Steel", configuration.getBoolean("Enabled Steel", FLUIDS_CAT, true,
				"Enable the molten fluid of Steel"));
		Options.fluidEnabled("Tin", configuration.getBoolean("Enabled Tin", FLUIDS_CAT, true,
				"Enable the molten fluid of Tin"));
		Options.fluidEnabled("Zinc", configuration.getBoolean("Enabled Zinc", FLUIDS_CAT, true,
				"Enable the molten fluid of Zinc"));

		// DISABLE CRACK HAMMER RECIPES
		Options.setDisabledRecipes(parseDisabledRecipes(configuration.getString(
				"DisabledCrackhammerRecipes", GENERAL_CAT, "",
				"Disable the recipes by putting the input materials ore dictionary name ore registry name in this key.\nThe format is a semicolon (;) separate list of ore dictionary names (ie:  oreGold;oreIron;oreCopper - this would blacklist Gold, Iron and Copper ores from working")));

		// CRACK HAMMER RECIPES
		final ConfigCategory userRecipeCat = configuration.getCategory(HAMMER_RECIPES_CAT);

		userRecipeCat.setComment(
				"This section allows you to add your own recipes for the Crack Hammer (and other rock \n"
						+ "crushers). Recipes are specified in semicolon (;) delimited lists of formulas in the \n"
						+ "format modid:name#y->x*modid:name#y, where x is the number of items in a stack and y \n"
						+ "is the metadata value. Note that both x and y are optional, so you can use the \n"
						+ "formula modid:name->modid:name for most items/blocks. \n\n"
						+ "All properties in this section will be parsed for formulas, regardless their name. \n"
						+ "This lets you organize your recipe lists for easier reading.");
		if (userRecipeCat.keySet().isEmpty()) {
			final Property prop = new Property("custom", "", Property.Type.STRING);
			prop.setComment(
					"Example: minecraft:stained_glass#11->minecraft:dye#4; minecraft:wool->4*minecraft:string");
			userRecipeCat.put("custom", prop);
		}

		manageUserHammerRecipes(userRecipeCat.values());

		if (configuration.hasChanged()) {
			configuration.save();
		}
	}
}
