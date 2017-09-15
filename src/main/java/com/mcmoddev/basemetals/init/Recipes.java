package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			final MMDMaterial material = Materials.getMaterialByName(MaterialNames.STEEL);
			OreDictionary.registerOre(Oredicts.SPROCKET, material.getItem(Names.GEAR));
		}

		// new recipes using rods and gears
	}
	
	@SubscribeEvent
	public static void registerRecipes( RegistryEvent.Register<IRecipe> event ) {
		register( event );
	}
}
