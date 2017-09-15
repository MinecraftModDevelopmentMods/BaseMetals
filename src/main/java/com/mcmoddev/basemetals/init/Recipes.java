package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@SuppressWarnings("deprecation")
public class Recipes extends com.mcmoddev.lib.init.Recipes {

	private static boolean initDone = false;

	private Recipes() {
		throw new IllegalAccessError("Not a instantiable class");
	}

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		Materials.init();
		Blocks.init();
		Items.init();

		initPureVanillaOredicts();
		initPureVanillaCrusherRecipes();
		initVanillaRecipes();
		initGeneralRecipes();
		initModSpecificRecipes();

		initDone = true;
	}

	protected static void initVanillaRecipes() {
	}

	private static void initModSpecificRecipes() {
		if (Options.isMaterialEnabled(MaterialNames.ADAMANTINE)) {
			// Alt oreDict Adamantite
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamantite");

			// Alt oreDict Adamantium
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamantium");

			// Alt oreDict Adamant
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.ADAMANTINE), "Adamant");
		}

		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.AQUARIUM), 3, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.ZINC, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE, MaterialNames.PRISMARINE);
		}

		if (Options.isMaterialEnabled(MaterialNames.BRASS)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRASS), 3, MaterialNames.COPPER, MaterialNames.COPPER, "Zinc");
		}

		if (Options.isMaterialEnabled(MaterialNames.BRONZE)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.BRONZE), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.TIN);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.CUPRONICKEL), 4, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.COPPER, MaterialNames.NICKEL);
		}

		if (Options.isMaterialEnabled(MaterialNames.ELECTRUM)) {
			addSimpleAlloyRecipe(Materials.getMaterialByName(MaterialNames.ELECTRUM), 2, MaterialNames.GOLD, MaterialNames.SILVER);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.INVAR), 3, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.NICKEL);
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			addAdditionalOredicts(Materials.getMaterialByName(MaterialNames.MERCURY), "Quicksilver");

			if (FluidRegistry.isUniversalBucketEnabled()) {
				final ItemStack bucketMercury = UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, Materials.getMaterialByName(MaterialNames.MERCURY).getFluid());
				GameRegistry.addRecipe(new ResourceLocation("bucket"), new ShapelessOreRecipe(new ResourceLocation("bucket"), bucketMercury, net.minecraft.init.Items.BUCKET, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY, Oredicts.INGOT_MERCURY));
			}
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL)) {
			addAlloyRecipe(Materials.getMaterialByName(MaterialNames.MITHRIL), 3, MaterialNames.SILVER, MaterialNames.SILVER, MaterialNames.COLDIRON, MaterialNames.MERCURY);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STEEL);
			OreDictionary.registerOre(Oredicts.SPROCKET, material.getItem(Names.GEAR));

			// addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, "Carbon");

			addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.CHARCOAL);

			addAlloyRecipe(material, 8, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.IRON, MaterialNames.COAL);

			GameRegistry.addRecipe(new ResourceLocation("activator_rail"), new ShapedOreRecipe(new ResourceLocation("activator_rail"), new ItemStack(net.minecraft.init.Blocks.ACTIVATOR_RAIL, 6), "x/x", "x*x", "x/x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, '*', net.minecraft.init.Blocks.REDSTONE_TORCH));
			GameRegistry.addRecipe(new ResourceLocation("detector)rail"), new ShapedOreRecipe(new ResourceLocation("detector_rail"), new ItemStack(net.minecraft.init.Blocks.DETECTOR_RAIL, 6), "x x", "x-x", "x*x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, '-', net.minecraft.init.Blocks.STONE_PRESSURE_PLATE, '*', Oredicts.DUST_REDSTONE));
			GameRegistry.addRecipe(new ResourceLocation("rail"), new ShapedOreRecipe(new ResourceLocation("rail"), new ItemStack(net.minecraft.init.Blocks.RAIL, 16), "x x", "x/x", "x x", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD));
			GameRegistry.addRecipe(new ResourceLocation("tripwire_hook"), new ShapedOreRecipe(new ResourceLocation("tripwire_hook"), new ItemStack(net.minecraft.init.Blocks.TRIPWIRE_HOOK), "x  ", "/  ", "w  ", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.STICK_WOOD, 'w', Oredicts.PLANK_WOOD));
			GameRegistry.addRecipe(new ResourceLocation("flint_and_steel"), new ShapelessOreRecipe(new ResourceLocation("flint_and_steel"), new ItemStack(net.minecraft.init.Items.FLINT_AND_STEEL), Oredicts.INGOT_STEEL, net.minecraft.init.Items.FLINT));
			GameRegistry.addRecipe(new ResourceLocation("hopper"), new ShapedOreRecipe(new ResourceLocation("hopper"), new ItemStack(net.minecraft.init.Blocks.HOPPER), "x x", "x/x", " x ", 'x', Oredicts.INGOT_STEEL, '/', Oredicts.CHEST_WOOD));
			GameRegistry.addRecipe(new ResourceLocation("minecart"), new ShapedOreRecipe(new ResourceLocation("minecart"), new ItemStack(net.minecraft.init.Items.MINECART), "x x", "xxx", 'x', Oredicts.INGOT_STEEL));
			GameRegistry.addRecipe(new ResourceLocation("piston"), new ShapedOreRecipe(new ResourceLocation("piston"), new ItemStack(net.minecraft.init.Blocks.PISTON), "www", "sxs", "s*s", 'x', Oredicts.INGOT_STEEL, 'w', Oredicts.PLANK_WOOD, 's', Oredicts.COBBLESTONE, '*', Oredicts.DUST_REDSTONE));
		}

		// new recipes using rods and gears
		GameRegistry.addRecipe(new ResourceLocation("lever"), new ShapedOreRecipe(new ResourceLocation("lever"), new ItemStack(net.minecraft.init.Blocks.LEVER, 1), "x", "y", 'x', Oredicts.ROD, 'y', Oredicts.COBBLESTONE));
		GameRegistry.addRecipe(new ResourceLocation("human_detector"), new ShapedOreRecipe(new ResourceLocation("human_detector"), new ItemStack(Blocks.humanDetector, 1), "xx", "yy", 'x', Oredicts.INGOT_BRICK, 'y', Oredicts.GEAR));
	}
}
