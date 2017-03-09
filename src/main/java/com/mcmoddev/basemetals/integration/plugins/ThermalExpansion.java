package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.util.Config.Options;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.material.MetalMaterial;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.util.ThermalExpansionHelper;

@MMDPlugin( addonId = BaseMetals.MODID, pluginId = ThermalExpansion.PLUGIN_MODID )
public class ThermalExpansion extends com.mcmoddev.lib.integration.plugins.ThermalExpansion implements IIntegration {

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableTinkersConstruct) {
			return;
		}
		
		addFurnace(Options.enableAdamantine, "Adamantine");
		addFurnace(Options.enableAntimony, "Antimony");
		addFurnace(Options.enableAquarium, "Aquarium");
		addFurnace(Options.enableBismuth, "Bismuth");
		addFurnace(Options.enableBrass, "Brass");
		addFurnace(Options.enableBronze, "Bronze");
		addFurnace(Options.enableColdIron, "ColdIron");
		addFurnace(Options.enableCupronickel, "Cupronickel");
		addFurnace(Options.enableInvar, "Invar");
		addFurnace(Options.enableMithril, "Mithril");
		addFurnace(Options.enableNickel, "Nickel");
		addFurnace(Options.enablePewter, "Pewter");
		addFurnace(Options.enablePlatinum, "Platinum");
		addFurnace(Options.enableStarSteel, "StarSteel");
		addFurnace(Options.enableTin, "Tin");
		addFurnace(Options.enableZinc, "Zinc");
		addFurnace(Options.enableLead, "Lead");
		addFurnace(Options.enableMercury, "Mercury");
		addFurnace(Options.enableSilver, "Silver");

		addCrucible(Options.enableAdamantine, "Adamantine");
		addCrucible(Options.enableAntimony, "Antimony");
		addCrucible(Options.enableAquarium, "Aquarium");
		addCrucible(Options.enableBismuth, "Bismuth");
		addCrucible(Options.enableBrass, "Brass");
		addCrucible(Options.enableBronze, "Bronze");
		addCrucible(Options.enableColdIron, "ColdIron");
		addCrucible(Options.enableCupronickel, "Cupronickel");
		addCrucible(Options.enableInvar, "Invar");
		addCrucible(Options.enableMithril, "Mithril");
		addCrucible(Options.enableNickel, "Nickel");
		addCrucible(Options.enablePewter, "Pewter");
		addCrucible(Options.enablePlatinum, "Platinum");
		addCrucible(Options.enableStarSteel, "StarSteel");
		addCrucible(Options.enableTin, "Tin");
		addCrucible(Options.enableZinc, "Zinc");
		addCrucible(Options.enableLead, "Lead");
		addCrucible(Options.enableMercury, "Mercury");
		addCrucible(Options.enableSilver, "Silver");

		addPlatePress(Options.enableAdamantine, "Adamantine");
		addPlatePress(Options.enableAntimony, "Antimony");
		addPlatePress(Options.enableAquarium, "Aquarium");
		addPlatePress(Options.enableBismuth, "Bismuth");
		addPlatePress(Options.enableBrass, "Brass");
		addPlatePress(Options.enableBronze, "Bronze");
		addPlatePress(Options.enableColdIron, "ColdIron");
		addPlatePress(Options.enableCupronickel, "Cupronickel");
		addPlatePress(Options.enableInvar, "Invar");
		addPlatePress(Options.enableMithril, "Mithril");
		addPlatePress(Options.enableNickel, "Nickel");
		addPlatePress(Options.enablePewter, "Pewter");
		addPlatePress(Options.enablePlatinum, "Platinum");
		addPlatePress(Options.enableStarSteel, "StarSteel");
		addPlatePress(Options.enableTin, "Tin");
		addPlatePress(Options.enableZinc, "Zinc");
		addPlatePress(Options.enableLead, "Lead");
		addPlatePress(Options.enableMercury, "Mercury");
		addPlatePress(Options.enableSilver, "Silver");

		addPressStorage(Options.enableAdamantine, "Adamantine");
		addPressStorage(Options.enableAntimony, "Antimony");
		addPressStorage(Options.enableAquarium, "Aquarium");
		addPressStorage(Options.enableBismuth, "Bismuth");
		addPressStorage(Options.enableBrass, "Brass");
		addPressStorage(Options.enableBronze, "Bronze");
		addPressStorage(Options.enableColdIron, "ColdIron");
		addPressStorage(Options.enableCupronickel, "Cupronickel");
		addPressStorage(Options.enableInvar, "Invar");
		addPressStorage(Options.enableMithril, "Mithril");
		addPressStorage(Options.enableNickel, "Nickel");
		addPressStorage(Options.enablePewter, "Pewter");
		addPressStorage(Options.enablePlatinum, "Platinum");
		addPressStorage(Options.enableStarSteel, "StarSteel");
		addPressStorage(Options.enableTin, "Tin");
		addPressStorage(Options.enableZinc, "Zinc");
		addPressStorage(Options.enableLead, "Lead");
		addPressStorage(Options.enableMercury, "Mercury");
		addPressStorage(Options.enableSilver, "Silver");

		initDone = true;
	}
	
	protected void addFurnace(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Ore -> Ingot default, according to TE source, is 2000
			 * dust -> Ingot default, according to same, is DEFAULT * 14 / 20 - at the 2000RF default, this is 1400
			 */
			int ENERGY_ORE = 2000;
			int ENERGY_DUST = 1400;
			ItemStack ore = new ItemStack( mat.ore, 1 );
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			ThermalExpansionHelper.addFurnaceRecipe( ENERGY_ORE, ore, ingot );
			if( Options.enableBasics && mat.powder != null ) {
				ItemStack dust = new ItemStack( mat.powder, 1 );
				ThermalExpansionHelper.addFurnaceRecipe( ENERGY_DUST, dust, ingot );
			}
		}
	}
	
	protected void addCrucible(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			/*
			 * Default power, according to TE source, is 8000
			 * This is used for Pyrotheum, Cryotheum, Aerotheum, Petrotheum and Redstone.
			 * Redstone Block is 72000
			 * Glowstone and Ender Pearl are 20000
			 * Glowstone block is 80000
			 * Cobblestone/Stone/Obsidian is 320000
			 */
			int ENERGY = 8000;
			
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			FluidStack oreFluid = FluidRegistry.getFluidStack( mat.getName(), 288 );
			FluidStack baseFluid = FluidRegistry.getFluidStack( mat.getName(), 144 );
			
			if( mat.ore != null ) {
				ItemStack ore = new ItemStack( mat.ore, 1 );
				ThermalExpansionHelper.addCrucibleRecipe( ENERGY, ore, oreFluid );
			}
			
			ThermalExpansionHelper.addCrucibleRecipe( ENERGY, ingot, baseFluid );
			
			if( Options.enableBasics && mat.powder != null ) {
				ItemStack dust = new ItemStack( mat.powder, 1 );
				ThermalExpansionHelper.addCrucibleRecipe( ENERGY, dust, baseFluid );
			}
			addCrucibleExtra( Options.enablePlate, Item.getItemFromBlock(mat.plate), FluidRegistry.getFluidStack(mat.getName(), 144), ENERGY );
			addCrucibleExtra( Options.enableBasics, mat.nugget, FluidRegistry.getFluidStack(mat.getName(), 16), ENERGY );
		}
	}

	private void addCrucibleExtra(boolean enabled, Item input, FluidStack output, int energy ) {
		if( enabled && input != null && output != null ) {
			ItemStack inItems = new ItemStack( input, 1 );
			ThermalExpansionHelper.addCrucibleRecipe( energy, inItems, output );
		}
	}

	private void addPlatePress(boolean enabled, String materialName) {
		if( enabled && Options.enablePlate ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			
			/*
			 * Compactors default is 4000RF per operation
			 */
			int ENERGY = 4000;
			addCompactorPressRecipe( ENERGY, new ItemStack( mat.ingot, 1), new ItemStack( mat.plate, 1) );
		}
	}

	private void addPressStorage(boolean enabled, String materialName) {
		if( enabled ) {
			MetalMaterial mat = Materials.getMaterialByName(materialName.toLowerCase());
			
			/*
			 * Compactors default is 4000RF per operation
			 */
			int ENERGY = 4000;
			ItemStack ingots = new ItemStack( mat.ingot, 9 );
			ItemStack nuggets = new ItemStack( mat.nugget, 9 );
			ItemStack block = new ItemStack( mat.block, 1 );
			ItemStack ingot = new ItemStack( mat.ingot, 1 );
			
			addCompactorStorageRecipe( ENERGY, ingots, block );
			addCompactorStorageRecipe( ENERGY, nuggets, ingot );
		}
	}
	
}
