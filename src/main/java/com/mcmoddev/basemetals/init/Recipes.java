package com.mcmoddev.basemetals.init;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

/**
 *
 * @author Jasmine Iwanek
 *
 */
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
		// all recipes for BaseMetals are currently in JSON - this exists in case there are some we have to add
		// programmatically
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
