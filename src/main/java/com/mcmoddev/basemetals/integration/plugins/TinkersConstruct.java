package com.mcmoddev.basemetals.integration.plugins;

import javax.annotation.Nonnull;

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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.shared.TinkerFluids;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, 
           pluginId = TinkersConstruct.PLUGIN_MODID, 
           preInitCallback="preInit", 
           initCallback="initCallback")
public class TinkersConstruct extends com.mcmoddev.lib.integration.plugins.TinkersConstructBase implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled("tinkersconstruct")) {
			return;
		}
		
		ModifierRegistry.initModifiers();
		
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ADAMANTINE), MaterialNames.ADAMANTINE, true, false, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, true, false, TraitNames.AQUADYNAMIC, TraitLocations.HEAD, TraitNames.JAGGED, TraitLocations.HEAD, TraitNames.AQUADYNAMIC);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BISMUTH), MaterialNames.BISMUTH, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.BRASS), MaterialNames.BRASS, true, false, TraitNames.DENSE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.COLDIRON), MaterialNames.COLDIRON, true, false, TraitNames.FREEZING);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.CUPRONICKEL), MaterialNames.CUPRONICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.DIAMOND), MaterialNames.DIAMOND, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.INVAR), MaterialNames.INVAR, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.MITHRIL), MaterialNames.MITHRIL, true, false, TraitNames.HOLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.NICKEL), MaterialNames.NICKEL, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PEWTER), MaterialNames.PEWTER, true, false, TraitNames.SOFT);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.PLATINUM), MaterialNames.PLATINUM, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.STARSTEEL), MaterialNames.STARSTEEL, true, false, TraitNames.ENDERFERENCE, TraitLocations.HEAD, TraitNames.SPARKLY);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.TIN), MaterialNames.TIN, true, false);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ZINC), MaterialNames.ZINC, true, false);

		registerAlloys();
		
		if (Options.isMaterialEnabled(MaterialNames.COAL)) {
			registerMelting(MaterialNames.COAL, 144);
		}

		if ((Options.isMaterialEnabled(MaterialNames.LEAD)) && (Options.isThingEnabled("Plate"))) {
			registerModifierItem("plated", Item.getItemFromBlock(Materials.getMaterialByName(MaterialNames.LEAD).getBlock(Names.PLATE)));
		}

		if (Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			registerMelting(MaterialNames.MERCURY, 144);
			if (Options.isThingEnabled("Basics")) {
				registerModifierItem("toxic", Materials.getMaterialByName(MaterialNames.MERCURY).getItem(Names.POWDER));
			}
		}

		MinecraftForge.EVENT_BUS.register(this);
		initDone = true;
	}
	
	public void preInit() {
		registry.setupIntegrations();
		registry.addMaterialStats();
		registry.integrationPreInit();
	}
	
	public void initCallback() {
		registry.resolveTraits();
		registry.integrationsInit();
		registry.registerMeltings();
		registry.registerAlloys();
	}
	
	@SubscribeEvent
	public void registerModifiers( RegistryEvent.Register<Item> ev) {
		ModifierRegistry.registerModifiers();
	}
	
	private void registerMelting(@Nonnull final String name, @Nonnull final int amount ) {
		MMDMaterial mat = Materials.getMaterialByName(name);
		FluidStack res = new FluidStack(mat.getFluid(), amount);
		registry.registerMelting(mat.getItem(Names.INGOT), res);
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

	private static Fluid getFluid(@Nonnull final MMDMaterial mat) {
		Fluid outFluid;

		switch(mat.getName()) {
		case "iron":
			outFluid = TinkerFluids.iron;
			break;
		case "gold":
			outFluid = TinkerFluids.gold;
			break;
		case "emerald":
			outFluid = TinkerFluids.emerald;
			break;
		case "aluminumbrass":
			outFluid = TinkerFluids.alubrass;
			break;
		case "aluminum":
			outFluid = TinkerFluids.aluminum;
			break;
		case "copper":
			outFluid = TinkerFluids.copper;
			break;
		case "brass":
			outFluid = TinkerFluids.brass;
			break;
		case "tin":
			outFluid = TinkerFluids.tin;
			break;
		case "bronze":
			outFluid = TinkerFluids.bronze;
			break;
		case "zinc":
			outFluid = TinkerFluids.zinc;
			break;
		case "lead":
			outFluid = TinkerFluids.lead;
			break;
		case "nickel":
			outFluid = TinkerFluids.nickel;
			break;
		case "silver":
			outFluid = TinkerFluids.silver;
			break;
		case "electrum":
			outFluid = TinkerFluids.electrum;
			break;
		case "steel":
			outFluid = TinkerFluids.steel;
			break;
		default:
			outFluid = mat.getFluid();
		}

		return outFluid;
	}
	
	private void registerAlloys() {
		if (Options.isMaterialEnabled(MaterialNames.AQUARIUM) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && Options.isMaterialEnabled(MaterialNames.ZINC)) {
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.AQUARIUM)),3);
			FluidStack copper = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.COPPER)), 2);
			FluidStack zinc = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.ZINC)), 1);
			FluidStack prismarine = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.PRISMARINE)), 3);
			registry.registerAlloy(MaterialNames.AQUARIUM, output, copper, zinc, prismarine);
		}

		if (Options.isMaterialEnabled(MaterialNames.CUPRONICKEL) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && 
				Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.CUPRONICKEL)),4);
			FluidStack copper = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.COPPER)), 3);
			FluidStack nickel = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.NICKEL)), 1);
			registry.registerAlloy(MaterialNames.CUPRONICKEL, output, copper, nickel);
		}

		if (Options.isMaterialEnabled(MaterialNames.INVAR) && Options.isMaterialEnabled(MaterialNames.NICKEL)) {
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.INVAR)),3);
			FluidStack iron = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.IRON)), 2);
			FluidStack nickel = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.NICKEL)), 1);
			registry.registerAlloy(MaterialNames.INVAR, output, iron, nickel);
		}

		if (Options.isMaterialEnabled(MaterialNames.MITHRIL) && 
				Options.isMaterialEnabled(MaterialNames.COLDIRON) && 
				Options.isMaterialEnabled(MaterialNames.SILVER) && 
				Options.isMaterialEnabled(MaterialNames.MERCURY)) {
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.MITHRIL)),3);
			FluidStack coldiron = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.COLDIRON)), 1);
			FluidStack silver = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.SILVER)), 2);
			FluidStack mercury = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.MERCURY)), 1);
			
			registry.registerAlloy(MaterialNames.MITHRIL, output, silver, coldiron, mercury);
		}

		if (Options.isMaterialEnabled(MaterialNames.PEWTER) && 
				Options.isMaterialEnabled(MaterialNames.LEAD) && 
				Options.isMaterialEnabled(MaterialNames.COPPER) && 
				Options.isMaterialEnabled(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.PEWTER)), 144);
			FluidStack copper = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.COPPER)), 2);
			FluidStack tin = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.TIN)), 137);
			FluidStack lead = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.LEAD)), 5);
			
			registry.registerAlloy(MaterialNames.PEWTER, output, tin, copper, lead);
		}

		if (Options.isMaterialEnabled(MaterialNames.STEEL)) {
			FluidStack output = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.STEEL)), 8);
			FluidStack iron = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.IRON)), 8);
			FluidStack coal = new FluidStack(getFluid(Materials.getMaterialByName(MaterialNames.COAL)), 1);
			registry.registerAlloy(MaterialNames.STEEL, output, iron, coal);
		}
	}

}
