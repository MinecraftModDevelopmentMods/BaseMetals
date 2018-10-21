package com.mcmoddev.lib.init;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.plugins.IC2;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.CheeseMath;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.util.Oredicts;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Recipes {

	protected static final String DEFAULT_ORESMELT_XP = "%01.2f";

	protected Recipes() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 */
	public static void init() {
		initPureVanillaOredicts();
		initPureVanillaCrusherRecipes();

		initGeneralRecipes();
		initModSpecificRecipes();
	}

	protected static void initPureVanillaOredicts() {
		OreDictionary.registerOre(Oredicts.BARS_IRON, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.BARS, net.minecraft.init.Blocks.IRON_BARS);
		OreDictionary.registerOre(Oredicts.DOOR_IRON, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.IRON_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR_WOOD, net.minecraft.init.Items.ACACIA_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.JUNGLE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.SPRUCE_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.DARK_OAK_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.BIRCH_DOOR);
		OreDictionary.registerOre(Oredicts.DOOR, net.minecraft.init.Items.ACACIA_DOOR);
	}

	protected static void initPureVanillaCrusherRecipes() {
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.STONE,
				new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone to Cobblestone
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STONEBRICK,
				new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone Bricks to
																			// Cobblestone
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Slab to
																			// Cobblestone Slab
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 5),
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Bricks Slab to
																			// Cobblestone Slab

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.COBBLESTONE,
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.MOSSY_COBBLESTONE,
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone Wall to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 1),
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone Wall to
																		// Gravel

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.GRAVEL,
				new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Gravel
																	// to
																	// Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SANDSTONE,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4)); // Sandstone to Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 1),
				new ItemStack(net.minecraft.init.Blocks.SAND, 2)); // Sandstone Slab to 2 Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLASS,
				new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Glass to Sand

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.RED_SANDSTONE,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Sandstone to Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB2, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.SAND, 2, 1)); // Red Sandstone Slab to 2 Red
																		// Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STAINED_GLASS,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Stained Glass to Red
																		// Sand

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLOWSTONE,
				new ItemStack(net.minecraft.init.Items.GLOWSTONE_DUST, 4)); // Glowstone to 4
																			// Glowstone Dust

		final MMDMaterial lapis = Materials.getMaterialByName(MaterialNames.LAPIS);
		// Lapis Ore to 8 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_LAPIS,
				lapis.getItemStack(Names.INGOT, 8));
		// Lapis Block to 9 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK_LAPIS,
				lapis.getItemStack(Names.INGOT, 9));

		final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.REDSTONE);
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_REDSTONE,
				redstone.getItemStack(Names.POWDER, 8)); // Redstone Ore to 8 Redstone
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK_REDSTONE,
				redstone.getItemStack(Names.POWDER, 9)); // Redstone Block to 9 Redstone

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.REEDS,
				new ItemStack(net.minecraft.init.Items.SUGAR, 2)); // Sugar Cane to 2 Sugar

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.BONE_BLOCK,
				new ItemStack(net.minecraft.init.Items.DYE, 9, 15)); // Bone Block to 9 Bonemeal
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BONE,
				new ItemStack(net.minecraft.init.Items.DYE, 3, 15)); // Bone to 3 Bonemeal

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Items.BLAZE_ROD,
				new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2)); // Blaze Rod to 2 Blaze
																			// Powder

		final MMDMaterial quartz = Materials.getMaterialByName(MaterialNames.QUARTZ);
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_QUARTZ,
				quartz.getItemStack(Names.INGOT, 2)); // Nether Quartz Ore to 2 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.QUARTZ_BLOCK,
				quartz.getItemStack(Names.INGOT, 4)); // Quartz Block to 4 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 7),
				quartz.getItemStack(Names.INGOT, 2)); // Quartz Slab to 2 Quartz

		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 0),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 4)); // Prismarine to
																				// Prismarine Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 1),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 9)); // Brismarine Bricks
																				// to Prismarine
																				// Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(
				new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 2),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 8)); // Dark Prismarine to
																				// Prismarine Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SEA_LANTERN,
				new ItemStack(net.minecraft.init.Items.PRISMARINE_CRYSTALS, 5)); // Sea Lantern to 5
																					// Prismarine
																					// Crystals
																					// to Prismarine
																					// Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SLIME_BLOCK,
				new ItemStack(net.minecraft.init.Items.SLIME_BALL, 9)); // Slime Block to 9 Slime
																		// Balls

		// Thermal Expansion and others have decided that Obsidian crushes to 4 obsidian dust
		// Match them instead of being a special snowflake
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.OBSIDIAN,
				Materials.getMaterialByName(MaterialNames.OBSIDIAN).getItemStack(Names.POWDER, 4));
	}

	protected static void initVanillaRecipes() {
		// stub
	}

	protected static void initGeneralRecipes() {
		for (final MMDMaterial material : Materials.getAllMaterials()) {

			if (material.isEmpty()) {
				continue;
			}

			// first we handle any recipes possibly involving transformations of an
			// ore-block
			makePowderRecipes(material); // this also handles ingot->powder, block->powder, etc..
			makeIngotRecipes(material); // nugget->ingot, ore->ingot, powder->ingot

			// nugget based recipes
			makeNuggetRecipes(material);

			// then the rest of the basic stuff
			makeSimpleRecipes(material); // slab, wall, small-powder, rod, lever, crackhammer,
											// trapdoor, etc...
			makeModRecipes(material); // rod, gear, etc...

			// ranged weapons all use the 'ROD' of a material
			if (material.hasItem(Names.ROD)) {
				makeCrossbowRecipes(material);
				makeBowRecipes(material);
			}

			// for now this can live here
			if (material.hasItem(Names.INGOT)) {
				// Vanilla has all the base tools & armors where they currently matter.
				generateBaseTools(material);
			}

			furnaceSpecial(material);
		}
	}

	private static void makeBowRecipes(@Nonnull final MMDMaterial material) {
		final float baseXP = Float
				.parseFloat(String.format(DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));
		final float nuggetXP = Float.parseFloat(String.format(DEFAULT_ORESMELT_XP, baseXP / 9.0f));

		if (isMMDItem(material, Names.ARROW)) {
			addFurnaceRecipe(material.getItemStack(Names.ARROW),
					material.getItemStack(Names.NUGGET, 1), nuggetXP); // 0.25 nugget loss
		}

		if (isMMDItem(material, Names.BOW)) {
			addFurnaceRecipe(material.getItemStack(Names.BOW),
					material.getItemStack(Names.INGOT, 1), baseXP); // 4.5 nugget loss
		}
	}

	private static void makeCrossbowRecipes(@Nonnull final MMDMaterial material) {
		final float baseXP = Float
				.parseFloat(String.format(DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));
		final float nuggetXP = Float.parseFloat(String.format(DEFAULT_ORESMELT_XP, baseXP / 9.0f));

		if ((material.hasItem(Names.GEAR)) && (material.hasItem(Names.CROSSBOW))) {
			final int count = CheeseMath.getIngotCount(material,
					material.getItemStack(Names.CROSSBOW));
			addFurnaceRecipe(material.getItemStack(Names.CROSSBOW),
					material.getItemStack(Names.INGOT, count), baseXP);
		}

		if (material.hasItem(Names.BOLT)) {
			addFurnaceRecipe(material.getItemStack(Names.BOLT),
					material.getItemStack(Names.NUGGET, 2), nuggetXP); // 0.25 Nugget loss
		}
	}

	private static void makeNuggetRecipes(@Nonnull final MMDMaterial material) {
		final float baseXP = Float
				.parseFloat(String.format(Locale.ENGLISH, DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));
		final float nuggetXP = Float.parseFloat(String.format(Locale.ENGLISH, DEFAULT_ORESMELT_XP, baseXP / 9.0f));
		final String oreDictName = material.getCapitalizedName();

		if (material.hasItem(Names.NUGGET)) {
			if (isMMDBlock(material, Names.BUTTON)) {
				addFurnaceRecipe(material.getBlockItemStack(Names.BUTTON),
						material.getItemStack(Names.NUGGET, 2), nuggetXP);
			}
			if (material.hasItem(Names.ROD)) {
				int cheeseQty = CheeseMath.getNuggetCount(material,
						material.getItemStack(Names.ROD));
				final ItemStack out = cheeseQty >= 9 ? material.getItemStack(Names.INGOT)
						: cheeseQty > 0 ? material.getItemStack(Names.NUGGET) : ItemStack.EMPTY;
				float xp = nuggetXP;

				if (!out.isEmpty()) {
					if (cheeseQty >= 9) {
						cheeseQty /= 9;
						xp = baseXP;
					}
					out.setCount(cheeseQty);
					addFurnaceRecipe(material.getItemStack(Names.ROD), out, xp);
				} else {
					BaseMetals.logger.fatal(
							"Rods per craft set to %d - got %d nuggets when trying to generate a cheese recipe, stopping it from happening (material %s)",
							Options.rodQuantity(), cheeseQty, material.getName());
				}
			}

			if ((material.hasItem(Names.POWDER)) && (material.hasItem(Names.SMALLPOWDER))
					&& (!material.getName().equals(MaterialNames.COAL))
					&& (!material.getName().equals(MaterialNames.CHARCOAL))) {
				addFurnaceRecipe(material.getItemStack(Names.SMALLPOWDER),
						material.getItemStack(Names.NUGGET, 1), nuggetXP);

				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName,
						material.getItemStack(Names.SMALLPOWDER, 1));
			}
		}
		if (material.hasItem(Names.SMALLBLEND)) {
			addFurnaceRecipe(material.getItemStack(Names.SMALLBLEND),
					material.getItemStack(Names.NUGGET, 1), nuggetXP);
		}
	}

	private static void makeModRecipes(@Nonnull final MMDMaterial material) {
		if (material.hasItem(Names.GEAR) && (material.hasItem(Names.INGOT))) {
			final int resCount = CheeseMath.getNuggetCount(material,
					material.getItemStack(Names.GEAR));
			final float ingotXP = Float
					.parseFloat(String.format(DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));
			final float nuggetXP = Float
					.parseFloat(String.format(DEFAULT_ORESMELT_XP, ingotXP / 9.0f));

			if (resCount < 9 && resCount > 0) {
				addFurnaceRecipe(new ItemStack(material.getItem(Names.GEAR)),
						material.getItemStack(Names.NUGGET, resCount), nuggetXP);
			} else if (resCount >= 9) {
				addFurnaceRecipe(new ItemStack(material.getItem(Names.GEAR)),
						material.getItemStack(Names.INGOT, resCount / 9), ingotXP);
			} else {
				BaseMetals.logger.warn(
						"Gears Per Craft set to %d - got a result nugget count of %d when trying to make the cheese recipe for Gears (material %s)",
						Options.gearQuantity(), resCount, material.getName());
			}
		}
	}

	private static void makeSimpleRecipes(@Nonnull final MMDMaterial material) {
		final float baseXP = Float
				.parseFloat(String.format(DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));
		final float nuggetXP = Float.parseFloat(String.format(DEFAULT_ORESMELT_XP, baseXP / 9.0f));

		if ((material.hasBlock(Names.BLOCK)) && (material.hasItem(Names.SLAB))) {
			addFurnaceRecipe(material.getItemStack(Names.SLAB),
					material.getItemStack(Names.NUGGET, 4), nuggetXP); // you lose roughly half a
																		// nugget
		}

		if ((material.hasItem(Names.ROD)) && (material.hasBlock(Names.LEVER))) {
			addFurnaceRecipe(material.getBlockItemStack(Names.LEVER),
					material.getItemStack(Names.INGOT, 1), baseXP); // you lose the rod
		}

		if (material.hasBlock(Names.WALL)) {
			addFurnaceRecipe(material.getBlockItemStack(Names.WALL),
					material.getBlockItemStack(Names.BLOCK, 1), 0);
		}

		final Map<Names, Integer> stuff = new TreeMap<>();
		stuff.put(Names.PRESSURE_PLATE, 2);
		stuff.put(Names.DOOR, 2);
		stuff.put(Names.TRAPDOOR, 4);
		stuff.put(Names.HORSE_ARMOR, 6);
		stuff.put(Names.FISHING_ROD, 1);

		stuff.entrySet().stream().filter(ent -> isMMDBlock(material, ent.getKey()))
				.forEach(ent -> addFurnaceRecipe(material.getBlockItemStack(ent.getKey()),
						material.getItemStack(Names.INGOT, ent.getValue().intValue()), baseXP));

		if (material.hasBlock(Names.PLATE) && isMMDBlock(material, Names.PLATE)) {
			int nuggetCount = CheeseMath.getNuggetCount(material,
					material.getBlockItemStack(Names.PLATE));
			if (nuggetCount > 9) {
				addFurnaceRecipe(material.getBlockItemStack(Names.PLATE), material.getItemStack(
						Names.INGOT,
						CheeseMath.getIngotCount(material, material.getItemStack(Names.PLATE))),
						nuggetXP);
			} else if (nuggetCount > 0) {
				addFurnaceRecipe(material.getBlockItemStack(Names.PLATE),
						material.getItemStack(Names.NUGGET, nuggetCount), baseXP);
			} else {
				BaseMetals.logger.warn(
						"Plates Per Craft set to %d - got a result nugget count of %d when trying to make the cheese recipe for plates (material %s)",
						Options.plateQuantity(), nuggetCount, material.getName());
			}
		}

		if (isMMDBlock(material, Names.BARS)) {
			addFurnaceRecipe(material.getBlockItemStack(Names.BARS),
					material.getItemStack(Names.NUGGET, 3), nuggetXP); // roughly half a nugget loss
		}
	}

	private static void makeIngotRecipes(@Nonnull final MMDMaterial material) {
		final float oreSmeltXP = material.getOreSmeltXP();

		if (material.hasItem(Names.INGOT)) {
			if (material.hasOre()) {
				addFurnaceRecipe(material.getBlockItemStack(Names.ORE),
						material.getItemStack(Names.INGOT, 1), oreSmeltXP);
			} else if (material.hasBlend()) {
				addFurnaceRecipe(material.getItemStack(Names.BLEND),
						material.getItemStack(Names.INGOT, 1), oreSmeltXP);
			}

			if (material.hasItem(Names.POWDER)) {
				if (material.getName().equals(MaterialNames.CHARCOAL)
						|| material.getName().equals(MaterialNames.COAL)) {
					return;
				}

				final ItemStack ingot = material.getItemStack(Names.INGOT);
				final ItemStack powder = material.getItemStack(Names.POWDER);

				addFurnaceRecipe(powder, ingot, oreSmeltXP);
			}
		}
	}

	private static void makePowderRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.hasItem(Names.POWDER)) {
			crushOre(material, oreDictName);
			crushIngot(material, oreDictName);
			crushBlock(material, oreDictName);
			doSmallPowder(material, oreDictName);
		}
	}

	private static void crushOre(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictName) {
		if (material.hasOre() || material.hasBlock(Names.ORE)) {
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName,
					material.getItemStack(Names.POWDER, Options.twoDustDrop() ? 2 : 1));
		}
	}

	private static void crushIngot(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictName) {
		if (material.hasItem(Names.INGOT)) {
			if (material.getName().equals(MaterialNames.CHARCOAL)
					|| material.getName().equals(MaterialNames.COAL)) {
				CrusherRecipeRegistry.addNewCrusherRecipe(material.getItemStack(Names.INGOT),
						material.getItemStack(Names.POWDER, 1));
			} else {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName,
						material.getItemStack(Names.POWDER, 1));
			}
		}
	}

	private static void crushBlock(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictName) {
		if (material.hasBlock(Names.BLOCK)) {
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName,
					material.getItemStack(Names.POWDER, 9));
		}
	}

	private static void doSmallPowder(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictName) {
		if ((material.hasItem(Names.SMALLPOWDER)) && (material.hasItem(Names.NUGGET))) {
			CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName,
					material.getItemStack(Names.SMALLPOWDER, 1));
		}

	}

	private static void generateBaseTools(@Nonnull final MMDMaterial material) {
		if (isMMDItem(material, Names.SHEARS)) {
			addFurnaceRecipe(material.getItemStack(Names.SHEARS),
					material.getItemStack(Names.INGOT, 2), material.getOreSmeltXP());
		}
	}

	private static void addFurnaceRecipe(@Nonnull final ItemStack input, @Nonnull final ItemStack output,
			final float experience) {
		maybeRemoveRecipe(input);
		if (input.isEmpty()) {
			return;
		}
		final float actualXP = Math.max(FurnaceRecipes.instance().getSmeltingExperience(output),
				experience);
		GameRegistry.addSmelting(input, output, actualXP);
	}

	/**
	 *
	 */
	public static void dumpFurnaceRecipes() {
		final Map<ItemStack, ItemStack> smeltingMap = FurnaceRecipes.instance().getSmeltingList();

		smeltingMap.entrySet().forEach(ent -> {
			final ItemStack res = FurnaceRecipes.instance().getSmeltingResult(ent.getKey());
			final float exp = FurnaceRecipes.instance().getSmeltingExperience(res);
			BaseMetals.logger.fatal("Furnace Recipe, %s -> %s with %01.2f exp", ent.getKey(),
					ent.getValue(), exp);
		});
	}

	private static ItemStack findMatchingItemStack(final List<ItemStack> list, final ItemStack match) {
		for (final ItemStack item : list) {
			if (item.getCount() == match.getCount()
					&& (item.getItemDamage() == OreDictionary.WILDCARD_VALUE
							|| item.getItemDamage() == match.getItemDamage()
							|| match.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					&& item.getItem().equals(match.getItem())) {
				return item;
			}
		}

		return ItemStack.EMPTY;
	}

	private static void maybeRemoveRecipe(@Nonnull final ItemStack forItem) {
		final Map<ItemStack, ItemStack> smeltingMap = FurnaceRecipes.instance().getSmeltingList();
		final ItemStack matcher = findMatchingItemStack(
				smeltingMap.keySet().stream().collect(Collectors.toList()), forItem);

		if (matcher.isEmpty()) {
			return;
		}

		final ItemStack output = smeltingMap.getOrDefault(matcher, ItemStack.EMPTY);

		if (output.isEmpty()) {
			return;
		}

		FurnaceRecipes.instance().getSmeltingList().remove(matcher, output);
	}

	protected static void furnaceSpecial(@Nonnull final MMDMaterial material) {
		final float baseXP = Float
				.parseFloat(String.format(Locale.ENGLISH, DEFAULT_ORESMELT_XP, material.getOreSmeltXP()));

		if (Options.furnaceCheese()) {
			if (material.hasItem(Names.INGOT)) {
				final List<Names> itemNames = Arrays.asList(
				Names.BOOTS, Names.HELMET, Names.SWORD,    Names.SHOVEL, Names.PICKAXE,
				Names.HOE,   Names.AXE,    Names.LEGGINGS, Names.CHESTPLATE);

				itemNames.stream()
				.filter(name -> material.hasItem(name))
				.forEach(name -> {
					ItemStack target = material.getItemStack(name);
					target.setItemDamage(OreDictionary.WILDCARD_VALUE);
					maybeRemoveRecipe(target);
					int outCount = CheeseMath.getIngotCount(material, target);
					addFurnaceRecipe(material.getItemStack(name), material.getItemStack(Names.INGOT, outCount), baseXP);
				});

				if (material.hasItem(Names.CRACKHAMMER)) {
					addFurnaceRecipe(material.getItemStack(Names.CRACKHAMMER),
							material.getBlockItemStack(Names.BLOCK, 1), 0);
				}
			}
		}
	}

	private static void initModSpecificRecipes() {
		for (final MMDMaterial material : Materials.getAllMaterials()) {
			if (material.isEmpty()) {
				continue;
			}

			if (Options.isModEnabled(IC2.PLUGIN_MODID)) {
				if ((isMMDItem(material, Names.CRUSHED)) && material.hasItem(Names.INGOT)) {
					addFurnaceRecipe(material.getItemStack(Names.CRUSHED),
							material.getItemStack(Names.INGOT), material.getOreSmeltXP());
				}
				if ((isMMDItem(material, Names.CRUSHED_PURIFIED))
						&& material.hasItem(Names.INGOT)) {
					addFurnaceRecipe(material.getItemStack(Names.CRUSHED_PURIFIED),
							material.getItemStack(Names.INGOT), material.getOreSmeltXP());
				}
			}
		}
	}

	protected static void addAdditionalOredicts(@Nonnull final String materialName,
			final String oreDictName) {
		addAdditionalOredicts(Materials.getMaterialByName(materialName), oreDictName);
	}

	protected static void addAdditionalOredicts(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictNameIn) {
		if (material.isEmpty()) {
			return;
		}

		final String oreDictName = StringUtils.capitalize(oreDictNameIn);

		if ((material.hasOre()) && (material.hasBlock(Names.ORE))) {
			OreDictionary.registerOre(Oredicts.ORE + oreDictName,
					material.getBlockItemStack(Names.ORE));
		}
		if (material.hasBlock(Names.BLOCK)) {
			OreDictionary.registerOre(Oredicts.BLOCK + oreDictName,
					material.getBlockItemStack(Names.BLOCK));
		}

		final Map<Names, String> stuff = new TreeMap<>();
		stuff.put(Names.PLATE, Oredicts.PLATE);
		stuff.put(Names.DOOR, Oredicts.DOOR);
		stuff.put(Names.NUGGET, Oredicts.NUGGET);
		stuff.put(Names.TRAPDOOR, Oredicts.TRAPDOOR);
		stuff.put(Names.NUGGET, Oredicts.NUGGET);
		stuff.put(Names.POWDER, Oredicts.DUST);
		stuff.put(Names.GEAR, Oredicts.GEAR);
		stuff.put(Names.ROD, Oredicts.ROD);

		stuff.entrySet().stream().filter(ent -> material.hasItem(ent.getKey()))
				.forEach(ent -> OreDictionary.registerOre(ent.getValue() + oreDictName,
						material.getItemStack(ent.getKey())));

		if (material.hasBlock(Names.BARS)) {
			OreDictionary.registerOre(Oredicts.BARS + oreDictName,
					material.getBlockItemStack(Names.BARS));
		}
		if (material.hasBlend()) {
			if (material.hasItem(Names.BLEND)) {
				OreDictionary.registerOre(Oredicts.DUST + oreDictName,
						material.getItemStack(Names.BLEND));
			}
			if (material.hasItem(Names.SMALLBLEND)) {
				OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName,
						material.getItemStack(Names.SMALLBLEND));
				OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName,
						material.getItemStack(Names.SMALLBLEND));
			}
		}
		if (material.hasItem(Names.INGOT)) {
			if (material.getType() != MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.INGOT + oreDictName,
						material.getItemStack(Names.INGOT)); // For non-gem
				// based
			} else if (material.getType() == MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.GEM + oreDictName,
						material.getItemStack(Names.INGOT)); // For Gem based
			}
		}

		if (material.hasItem(Names.SMALLPOWDER)) {
			OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName,
					material.getItemStack(Names.SMALLPOWDER));
			OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName,
					material.getItemStack(Names.SMALLPOWDER));
		}
	}

	/**
	 *
	 * @param event
	 *            The Event.
	 */
	public static void register(final RegistryEvent.Register<IRecipe> event) {
		final String mod = Loader.instance().activeModContainer().getModId();
		if (!Materials.hasMaterialFromMod(mod)) {
			return;
		}

		// the following is broken, as the event *DOES* *NOT* *WORK* right and the call
		// asks the
		if (mod.equals(BaseMetals.MODID) && Materials.hasMaterial(MaterialNames.MERCURY)) {
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.MERCURY),
					"Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled() && FluidRegistry.getFluid(MaterialNames.MERCURY) != null) {
				final ItemStack bucketMercury = FluidUtil.getFilledBucket(new FluidStack(
						Materials.getMaterialByName(MaterialNames.MERCURY).getFluid(), 1000));
				final ShapelessOreRecipe buckMerc = new ShapelessOreRecipe(
						new ResourceLocation("basemetals", "bucket"), bucketMercury,
						net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY,
						Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY,
						Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY,
						Oredicts.INGOT_MERCURY);
				buckMerc.setRegistryName("basemetals", "mercury_bucket");
				event.getRegistry().register(buckMerc);
			}
		}

		initPureVanillaOredicts();
		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initGeneralRecipes();
		initModSpecificRecipes();

	}

	/**
	 * Checks whether the named Block is an IMMDObject.
	 *
	 * @param material
	 *            The Material
	 * @param name
	 *            Name of the Block to check
	 * @return boolean Is it IMMDObject?
	 */
	protected static boolean isMMDBlock(final MMDMaterial material, final Names name) {
		return ((material.hasBlock(name)) && (material.getBlock(name) instanceof IMMDObject));
	}

	/**
	 * Checks whether the named Item is an IMMDObject.
	 *
	 * @param material
	 *            The Material
	 * @param name
	 *            Name of the item to check
	 * @return boolean Is it IMMDObject?
	 */
	protected static boolean isMMDItem(final MMDMaterial material, final Names name) {
		return ((material.hasItem(name)) && (material.getItem(name) instanceof IMMDObject));
	}
}
