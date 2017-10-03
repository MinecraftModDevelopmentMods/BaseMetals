package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.tinkers.ModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TCMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.TraitLocations;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, 
           pluginId = TinkersConstruct.PLUGIN_MODID, 
           preInitCallback="preInit", 
           initCallback="initCallback",
           postInitCallback="postInit")
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled("tinkersconstruct")) {
			return;
		}
		
		
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, TraitNames.AQUADYNAMIC, TraitLocations.HEAD, TraitNames.JAGGED, TraitLocations.HEAD, TraitNames.AQUADYNAMIC);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, true, false, TraitNames.DENSE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, TraitNames.FREEZING);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, TraitNames.HOLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, TraitNames.SOFT);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, TraitNames.ENDERFERENCE, TraitLocations.HEAD, TraitNames.SPARKLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, true, false);
	
		MinecraftForge.EVENT_BUS.register(this);
		initDone = true;
	}
	
	public void preInit() {
		registry.setupIntegrations();
		registry.addMaterialStats();
		registerMelting();
		registry.setMaterialsVisible();
	}
	
	public void initCallback() {
		registerAlloys();
		registry.resolveTraits();
		registry.integrationsInit();
		registry.setMaterialsVisible();
		registry.registerMeltings();
	}

	public void postInit() {
		registry.setMaterialsVisible();
		registry.registerAlloys();
	}
	
	@SubscribeEvent
	public void registerModifiers( RegistryEvent.Register<Item> ev) {
		ModifierRegistry.initModifiers();
		registerModifiers();
		ModifierRegistry.registerModifiers();
	}
	
	private void registerModifiers() {
		if ((Options.isMaterialEnabled(MaterialNames.LEAD)) && (Options.isThingEnabled("Plate"))) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}
		
		if (Options.isMaterialEnabled(MaterialNames.MERCURY) && Options.isThingEnabled("Basics")) {
			registerModifierItem("toxic", Materials.getMaterialByName(MaterialNames.MERCURY).getItem(Names.POWDER));
		}

	}
	
	private void registerMelting() {
		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
			registry.registerMelting(net.minecraft.init.Items.COAL, FluidRegistry.getFluidStack(MaterialNames.COAL, 144));
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			MMDMaterial merc = Materials.getMaterialByName(MaterialNames.MERCURY);
			registry.registerMelting(merc.getItem(Names.INGOT), FluidRegistry.getFluidStack(merc.getName(), 144));
		}

		if (Options.isMaterialEnabled(MaterialNames.PRISMARINE)) {
			MMDMaterial pris = Materials.getMaterialByName(MaterialNames.PRISMARINE);
			registry.registerMelting(net.minecraft.init.Items.PRISMARINE_SHARD, FluidRegistry.getFluidStack(pris.getName(), 666));			
		}		
	}

	private boolean isTraitLoc(String loc) {
		switch (loc) {
		case TraitLocations.BOW:
		case TraitLocations.BOWSTRING:
		case TraitLocations.EXTRA:
		case TraitLocations.FLETCHING:
		case TraitLocations.HANDLE:
		case TraitLocations.HEAD:
		case TraitLocations.PROJECTILE:
		case TraitLocations.SHAFT:
			return true;
		default:
			return false;
		}
	}

	private void addTraits(TCMaterial mat, String[] traits) {
		int i = 0;
		while (i < traits.length) {
			if( i == (traits.length - 1) ) {
				// can only be a "general" trait
				mat.addTrait("general", traits[i]);
			} else {
				String item = traits[i];
				if( isTraitLoc(item) ) {
					if( i+1 >= traits.length ) {
						return;
					}
					i++;
					mat.addTrait(item, traits[i]);
				} else {
					mat.addTrait("general", traits[i]);
				}
			}
			i++;
		}
	}

	private void registerMaterial(boolean enabled, String name, boolean castable, boolean craftable, String... traits) {
		if (enabled) {
				registerMaterial(name, castable, craftable, traits);
		}
	}

	private void registerMaterial(String name, boolean castable, boolean craftable, String... traits) {
		MMDMaterial mmdMat = Materials.getMaterialByName(name);
		TCMaterial mat = registry.newMaterial(name, mmdMat.getTintColor());
		
		if( castable )
			mat.setCastable();
		if( craftable )
			mat.setCraftable();
		
		mat.setSourceMaterial(mmdMat);
		mat.genStatsFromSource();
		
		if (traits.length > 0) {
			addTraits(mat, traits);
		}
		
		mat.settle();
	}
	
	private void registerAlloys() {
		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && Options.isMaterialEnabled(MaterialNames.ZINC)) {
			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.AQUARIUM, 3);
			FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			FluidStack zinc = FluidRegistry.getFluidStack(MaterialNames.ZINC, 1);
			FluidStack prismarine = FluidRegistry.getFluidStack(MaterialNames.PRISMARINE, 3);
			registry.registerAlloy(MaterialNames.AQUARIUM, output, copper, zinc, prismarine);
		}
		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && 
				Options.isMaterialEnabled(MaterialNames.NICKEL)) {

			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.CUPRONICKEL,4);
			FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 3);
			FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			registry.registerAlloy(MaterialNames.CUPRONICKEL, output, copper, nickel);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR) && Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.INVAR,3);
			FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 2);
			FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
		    registry.registerAlloy(MaterialNames.INVAR, output, iron, nickel);				
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL) && 
				Options.isMaterialEnabled(MaterialNames.COLDIRON) && 
				Options.isMaterialEnabled(MaterialNames.SILVER) && 
				Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.MITHRIL,3);
			FluidStack coldiron = FluidRegistry.getFluidStack(MaterialNames.COLDIRON, 1);
			FluidStack silver = FluidRegistry.getFluidStack(MaterialNames.SILVER, 2);
			FluidStack mercury = FluidRegistry.getFluidStack(MaterialNames.MERCURY, 1);
			registry.registerAlloy(MaterialNames.MITHRIL, output, coldiron, silver, mercury);
		}

		if (Options.isMaterialEnabled(MaterialNames.PEWTER) && 
				Options.isMaterialEnabled(MaterialNames.LEAD) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && 
				Options.isMaterialEnabled(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.PEWTER, 144);
			FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			FluidStack tin = FluidRegistry.getFluidStack(MaterialNames.TIN, 137);
			FluidStack lead = FluidRegistry.getFluidStack(MaterialNames.LEAD, 5);
			registry.registerAlloy(MaterialNames.PEWTER, output, copper, tin, lead);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			FluidStack output = FluidRegistry.getFluidStack(MaterialNames.STEEL, 8);
			FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 8);
			FluidStack coal = FluidRegistry.getFluidStack(MaterialNames.COAL, 1);
			registry.registerAlloy(MaterialNames.STEEL, output, iron, coal);				
		}
	}

}
