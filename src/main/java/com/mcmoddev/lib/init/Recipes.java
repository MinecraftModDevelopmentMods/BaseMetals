package com.mcmoddev.lib.init;

import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.integration.plugins.IC2Base;
import com.mcmoddev.lib.material.IMMDObject;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
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

/**
 *
 * @author Jasmine Iwanek
 *
 */
public abstract class Recipes {

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
				new ItemStack(net.minecraft.init.Blocks.COBBLESTONE, 1)); // Stone Bricks to Cobblestone
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Slab to Cobblestone Slab
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 5),
				new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 3)); // Stone Bricks Slab to Cobblestone Slab

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.COBBLESTONE,
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.MOSSY_COBBLESTONE,
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Cobblestone Wall to Gravel
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.COBBLESTONE_WALL, 1, 1),
				new ItemStack(net.minecraft.init.Blocks.GRAVEL, 1)); // Mossy Cobblestone Wall to Gravel

		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.GRAVEL, new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Gravel
																														// to
																														// Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SANDSTONE,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4)); // Sandstone to Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 1),
				new ItemStack(net.minecraft.init.Blocks.SAND, 2)); // Sandstone Slab to 2 Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLASS,
				new ItemStack(net.minecraft.init.Blocks.SAND, 1)); // Glass to Sand

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.RED_SANDSTONE,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Sandstone to Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB2, 1, 0),
				new ItemStack(net.minecraft.init.Blocks.SAND, 2, 1)); // Red Sandstone Slab to 2 Red Sand
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.STAINED_GLASS,
				new ItemStack(net.minecraft.init.Blocks.SAND, 4, 1)); // Red Stained Glass to Red Sand

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.GLOWSTONE,
				new ItemStack(net.minecraft.init.Items.GLOWSTONE_DUST, 4)); // Glowstone to 4 Glowstone Dust

		final MMDMaterial lapis = Materials.getMaterialByName(MaterialNames.LAPIS); 
		// Lapis Ore to 8 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_LAPIS,
				lapis.getItemStack(Names.INGOT, 8));
		 // Lapis Block to 9 Lapis
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK_LAPIS,
				lapis.getItemStack(Names.INGOT, 9));

		final MMDMaterial redstone = Materials.getMaterialByName(MaterialNames.LAPIS);
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
				new ItemStack(net.minecraft.init.Items.BLAZE_POWDER, 2)); // Blaze Rod to 2 Blaze Powder

		final MMDMaterial quartz = Materials.getMaterialByName(MaterialNames.QUARTZ);
		CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE_QUARTZ,
				quartz.getItemStack(Names.INGOT, 2)); // Nether Quartz Ore to 2 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.QUARTZ_BLOCK,
				quartz.getItemStack(Names.INGOT, 4)); // Quartz Block to 4 Quartz
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.STONE_SLAB, 1, 7),
				quartz.getItemStack(Names.INGOT, 2)); // Quartz Slab to 2 Quartz

		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 0),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 4)); // Prismarine to Prismarine Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 1),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 9)); // Brismarine Bricks to Prismarine Shard
		CrusherRecipeRegistry.addNewCrusherRecipe(new ItemStack(net.minecraft.init.Blocks.PRISMARINE, 1, 2),
				new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD, 8)); // Dark Prismarine to Prismarine Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SEA_LANTERN,
				new ItemStack(net.minecraft.init.Items.PRISMARINE_CRYSTALS, 5)); // Sea Lantern to 5 Prismarine Crystals
																					// to Prismarine Shard

		CrusherRecipeRegistry.addNewCrusherRecipe(net.minecraft.init.Blocks.SLIME_BLOCK,
				new ItemStack(net.minecraft.init.Items.SLIME_BALL, 9)); // Slime Block to 9 Slime Balls

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
			makeSimpleRecipes(material); // slab, wall, small-powder, rod, lever, crackhammer, trapdoor, etc...
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
		if (isMMDItem(material, Names.ARROW)) {
			GameRegistry.addSmelting(material.getItemStack(Names.ARROW),
					material.getItemStack(Names.NUGGET, 1), 0); // 0.25 nugget loss
		}

		if (isMMDItem(material, Names.BOW)) {
			GameRegistry.addSmelting(material.getItemStack(Names.BOW),
					material.getItemStack(Names.INGOT, 1), 0); // 4.5 nugget loss
		}
	}

	private static void makeCrossbowRecipes(@Nonnull final MMDMaterial material) {
		if ((material.hasItem(Names.GEAR)) && (material.hasItem(Names.CROSSBOW))) {
			GameRegistry.addSmelting(material.getItemStack(Names.CROSSBOW),
					material.getItemStack(Names.INGOT, 2 + Options.gearQuantity()), 0);
		}

		if (material.hasItem(Names.BOLT)) {
			GameRegistry.addSmelting(material.getItemStack(Names.BOLT),
					material.getItemStack(Names.NUGGET, 2), 0); // 0.25 nugget loss
		}
	}

	private static void makeNuggetRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.hasItem(Names.NUGGET)) {
			if (isMMDBlock(material, Names.BUTTON)) {
				GameRegistry.addSmelting(material.getBlockItemStack(Names.BUTTON),
						material.getItemStack(Names.NUGGET, 2), 0);
			}
			if (material.hasItem(Names.ROD)) {
				GameRegistry.addSmelting(material.getItemStack(Names.ROD),
						material.getItemStack(Names.NUGGET, 4), 0); // Roughly half a nugget loss
			}

			if ((material.hasItem(Names.POWDER)) && (material.hasItem(Names.SMALLPOWDER))) {
				GameRegistry.addSmelting(material.getItemStack(Names.SMALLPOWDER),
						material.getItemStack(Names.NUGGET, 1), 0);

				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName,
						material.getItemStack(Names.SMALLPOWDER, 1));
			}
		}
		if (material.hasItem(Names.SMALLBLEND)) {
			GameRegistry.addSmelting(material.getItemStack(Names.SMALLBLEND),
					material.getItemStack(Names.NUGGET, 1), 0);
		}
	}

	private static void makeModRecipes(@Nonnull final MMDMaterial material) {
		if (material.hasItem(Names.GEAR) && (material.hasItem(Names.INGOT))) {
			// if there is no ingot, no cheese
			GameRegistry.addSmelting(new ItemStack(material.getItem(Names.GEAR)),
					material.getItemStack(Names.INGOT, Options.gearQuantity()), 0); // you lose the rod
		}
	}

	private static void makeSimpleRecipes(@Nonnull final MMDMaterial material) {
		if ((material.hasBlock(Names.BLOCK)) && (material.hasItem(Names.SLAB))) {
			GameRegistry.addSmelting(material.getItemStack(Names.SLAB),
					material.getItemStack(Names.NUGGET, 4), 0); // you lose roughly half a nugget
		}

		if ((material.hasItem(Names.ROD)) && (material.hasBlock(Names.LEVER))) {
			GameRegistry.addSmelting(material.getBlockItemStack(Names.LEVER),
					material.getItemStack(Names.INGOT, 1), 0); // you lose the rod
		}

		if (material.hasBlock(Names.WALL)) {
			GameRegistry.addSmelting(material.getBlockItemStack(Names.WALL),
					material.getBlockItemStack(Names.BLOCK, 1), 0);
		}

		Map<Names, Integer> stuff = new TreeMap<>();
		stuff.put(Names.PLATE, 1);
		stuff.put(Names.PRESSURE_PLATE, 2);
		stuff.put(Names.DOOR, 2);
		stuff.put(Names.TRAPDOOR, 4);
		stuff.put(Names.HORSE_ARMOR, 6);
		stuff.put(Names.FISHING_ROD, 1);

		stuff.entrySet().stream()
		.filter(ent -> isMMDBlock(material, ent.getKey()))
		.forEach(ent -> GameRegistry.addSmelting(material.getBlockItemStack(ent.getKey()),
				material.getItemStack(Names.INGOT, ent.getValue().intValue()), 0));

		if (isMMDBlock(material, Names.BARS)) {
			GameRegistry.addSmelting(material.getBlockItemStack(Names.BARS),
					material.getItemStack(Names.NUGGET, 3), 0); // roughly half a nugget loss
		}
	}

	private static void makeIngotRecipes(@Nonnull final MMDMaterial material) {
		final float oreSmeltXP = material.getOreSmeltXP();

		if (material.hasItem(Names.INGOT)) {
			if (material.hasOre()) {
				GameRegistry.addSmelting(material.getBlockItemStack(Names.ORE),
						material.getItemStack(Names.INGOT, 1), oreSmeltXP);
			} else if (material.hasBlend()) {
				GameRegistry.addSmelting(material.getItemStack(Names.BLEND),
						material.getItemStack(Names.INGOT, 1), oreSmeltXP);
			}

			if (material.hasItem(Names.POWDER)) {
				if (material.getName().equals(MaterialNames.CHARCOAL) || 
						material.getName().equals(MaterialNames.COAL)) {
					return;
				}
				
				ItemStack ingot = material.getItemStack(Names.INGOT);

				GameRegistry.addSmelting(material.getItemStack(Names.POWDER),
						ingot, oreSmeltXP);
			}
		}
	}

	private static void makePowderRecipes(@Nonnull final MMDMaterial material) {
		final String oreDictName = material.getCapitalizedName();

		if (material.hasItem(Names.POWDER)) {
			if (material.hasOre() || material.hasBlock(Names.ORE)) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.ORE + oreDictName,
						material.getItemStack(Names.POWDER, Options.twoDustDrop() ? 2 : 1));
			}
			if (material.hasItem(Names.INGOT)) {
				if (material.getName().equals(MaterialNames.CHARCOAL) || 
					 	material.getName().equals(MaterialNames.COAL)) {
					CrusherRecipeRegistry.addNewCrusherRecipe(material.getItemStack(Names.INGOT), 
							material.getItemStack(Names.POWDER, 1));
				} else {
					CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.INGOT + oreDictName,
							material.getItemStack(Names.POWDER, 1));
				}
			}
			if (material.hasBlock(Names.BLOCK)) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.BLOCK + oreDictName,
						material.getItemStack(Names.POWDER, 9));
			}
			if ((material.hasItem(Names.SMALLPOWDER)) && (material.hasItem(Names.NUGGET))) {
				CrusherRecipeRegistry.addNewCrusherRecipe(Oredicts.NUGGET + oreDictName,
						material.getItemStack(Names.SMALLPOWDER, 1));
			}
		}
	}

	private static void generateBaseTools(@Nonnull final MMDMaterial material) {
		if (isMMDItem(material, Names.SHEARS)) {
			GameRegistry.addSmelting(material.getItemStack(Names.SHEARS),
					material.getItemStack(Names.INGOT, 2), 0);
		}
	}

	protected static void furnaceSpecial(@Nonnull final MMDMaterial material) {
		if ((material.hasItem(Names.INGOT)) && (Options.furnaceCheese())) {
			Map<Names, Integer> stuff = new TreeMap<>();
			stuff.put(Names.BOOTS, 4);
			stuff.put(Names.HELMET, 5);
			stuff.put(Names.SWORD, 2);
			stuff.put(Names.SHOVEL, 1);
			stuff.put(Names.PICKAXE, 3);
			stuff.put(Names.HOE, 2);
			stuff.put(Names.AXE, 3);
			stuff.put(Names.LEGGINGS, 7);
			stuff.put(Names.CHESTPLATE, 8);

			stuff.entrySet().stream().filter(ent -> material.hasItem(ent.getKey()))
			.forEach(ent -> GameRegistry.addSmelting(material.getItemStack(ent.getKey()),
					material.getItemStack(Names.INGOT, ent.getValue().intValue()), 0));

			if (material.hasItem(Names.CRACKHAMMER)) {
				GameRegistry.addSmelting(material.getItemStack(Names.CRACKHAMMER),
						material.getBlockItemStack(Names.BLOCK, 1), 0);
			}
		}
	}

	private static void initModSpecificRecipes() {
		for (MMDMaterial material : Materials.getAllMaterials()) {
			if (material.isEmpty()) {
				continue;
			}

			if (Options.isModEnabled(IC2Base.PLUGIN_MODID)) {
				if ((isMMDItem(material, Names.CRUSHED)) && material.hasItem(Names.INGOT)) {
					GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED), material.getItemStack(Names.INGOT),
							material.getOreSmeltXP());
				}
				if ((isMMDItem(material, Names.CRUSHED_PURIFIED)) && material.hasItem(Names.INGOT)) {
					GameRegistry.addSmelting(material.getItemStack(Names.CRUSHED_PURIFIED),
							material.getItemStack(Names.INGOT), material.getOreSmeltXP());
				}
			}
		}
	}

	protected static void addAdditionalOredicts(@Nonnull final String materialName, final String oreDictName) {
		addAdditionalOredicts(Materials.getMaterialByName(materialName), oreDictName);
	}

	protected static void addAdditionalOredicts(@Nonnull final MMDMaterial material,
			@Nonnull final String oreDictNameIn) {
		if (material.isEmpty()) {
			return;
		}

		final String oreDictName = StringUtils.capitalize(oreDictNameIn);

		if ((material.hasOre()) && (material.hasBlock(Names.ORE))) {
			OreDictionary.registerOre(Oredicts.ORE + oreDictName, material.getBlockItemStack(Names.ORE));
		}
		if (material.hasBlock(Names.BLOCK)) {
			OreDictionary.registerOre(Oredicts.BLOCK + oreDictName, material.getBlockItemStack(Names.BLOCK));
		}

		Map<Names, String> stuff = new TreeMap<>();
		stuff.put(Names.PLATE, Oredicts.PLATE);
		stuff.put(Names.DOOR, Oredicts.DOOR);
		stuff.put(Names.NUGGET, Oredicts.NUGGET);
		stuff.put(Names.TRAPDOOR, Oredicts.TRAPDOOR);
		stuff.put(Names.NUGGET, Oredicts.NUGGET);
		stuff.put(Names.POWDER, Oredicts.DUST);
		stuff.put(Names.GEAR, Oredicts.GEAR);
		stuff.put(Names.ROD, Oredicts.ROD);

		stuff.entrySet().stream()
		.filter(ent -> material.hasItem(ent.getKey()))
		.forEach(ent -> OreDictionary.registerOre(ent.getValue() + oreDictName, material.getItemStack(ent.getKey())));

		if (material.hasBlock(Names.BARS)) {
			OreDictionary.registerOre(Oredicts.BARS + oreDictName, material.getBlockItemStack(Names.BARS));
		}
		if (material.hasBlend()) {
			if (material.hasItem(Names.BLEND)) {
				OreDictionary.registerOre(Oredicts.DUST + oreDictName, material.getItemStack(Names.BLEND));
			}
			if (material.hasItem(Names.SMALLBLEND)) {
				OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName, material.getItemStack(Names.SMALLBLEND));
				OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName, material.getItemStack(Names.SMALLBLEND));
			}
		}
		if (material.hasItem(Names.INGOT)) {
			if (material.getType() != MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.INGOT + oreDictName, material.getItemStack(Names.INGOT)); // For non-gem
																										// based
			} else if (material.getType() == MaterialType.GEM) {
				OreDictionary.registerOre(Oredicts.GEM + oreDictName, material.getItemStack(Names.INGOT)); // For Gem based
			}
		}

		if (material.hasItem(Names.SMALLPOWDER)) {
			OreDictionary.registerOre(Oredicts.DUST_TINY + oreDictName, material.getItemStack(Names.SMALLPOWDER));
			OreDictionary.registerOre(Oredicts.DUST_SMALL + oreDictName, material.getItemStack(Names.SMALLPOWDER));
		}
	}

	/**
	 *
	 * @param event
	 */
	public static void register(final RegistryEvent.Register<IRecipe> event) {
		String mod = Loader.instance().activeModContainer().getModId();
		if (!Materials.hasMaterialFromMod(mod)) {
			return;
		}

		// the following is broken, as the event *DOES* *NOT* *WORK* right and the call
		// asks the
		if (mod.equals(BaseMetals.MODID) && Materials.hasMaterial(MaterialNames.MERCURY)) {
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.MERCURY), "Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled()) {
				final ItemStack bucketMercury = FluidUtil.getFilledBucket(
						new FluidStack(Materials.getMaterialByName(MaterialNames.MERCURY).getFluid(), 1000));
				ShapelessOreRecipe buckMerc = new ShapelessOreRecipe(new ResourceLocation("basemetals", "bucket"),
						bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY,
						Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY,
						Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY);
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
	 * @param material The Material
	 * @param name Name of the Block to check
	 * @return boolean Is it IMMDObject?
	 */
	protected static boolean isMMDBlock(final MMDMaterial material, final Names name) {
		return ((material.hasBlock(name)) && (material.getBlock(name) instanceof IMMDObject));
	}

	/**
	 * @param material The Material
	 * @param name Name of the item to check
	 * @return boolean Is it IMMDObject?
	 */
	protected static boolean isMMDItem(final MMDMaterial material, final Names name) {
		return ((material.hasItem(name)) && (material.getItem(name) instanceof IMMDObject));
	}
}
