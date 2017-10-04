package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.tconstruct.library.MaterialIntegration;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.ArrowShaftMaterialStats;
import slimeknights.tconstruct.library.materials.BowMaterialStats;
import slimeknights.tconstruct.library.materials.ExtraMaterialStats;
import slimeknights.tconstruct.library.materials.FletchingMaterialStats;
import slimeknights.tconstruct.library.materials.HandleMaterialStats;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.traits.ITrait;

public class TinkersConstructRegistry {
	public static final TinkersConstructRegistry instance = new TinkersConstructRegistry();
	private static final Map<String, Map<String, TCMaterial>> registry = new HashMap<>();
	private static final Map<String, List<MaterialIntegration>> integrations = new HashMap<>();
	private static final Map<String, Map<String, AlloyRecipe>> alloys = new HashMap<>();
	private static final Map<String, Map<Item, FluidStack>> meltings = new HashMap<>();
	
	private TinkersConstructRegistry() {
		// do nothing
	}
	
	public TCMaterial newMaterial(@Nonnull final String name, @Nonnull final int color) {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<String, TCMaterial> work = registry.getOrDefault(curMod, new HashMap<>());
		TCMaterial temp;
		if( work.isEmpty() || !work.containsKey(name) ) {
			temp = TCMaterial.get(name,color);
			work.put(name, temp);
			registry.put(curMod,work);
		} else {
			temp = work.get(name);
		}

		return temp;
	}

	public TCMaterial newMaterial(@Nonnull final MMDMaterial mat) {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<String, TCMaterial> work = registry.getOrDefault(curMod, new HashMap<>());
		if( work.isEmpty() || !work.containsKey(mat.getName()) ) {
			work.put(mat.getName(), TCMaterial.get(mat));
			registry.put(curMod, work);
		}
		
		return work.get(mat.getName());
	}
	
	public TCMaterial getMaterial(@Nonnull final String name) {
		for( final Entry<String, Map<String,TCMaterial>> ent : registry.entrySet()) {
			if(ent.getValue().containsKey(name))
				return ent.getValue().get(name);
		}
		
		return newMaterial(name, 0xFFFFFFFF);
	}
	
	public void addMaterialStats() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			entry.getValue().settle(); // be double sure!
			final HeadMaterialStats headStats = entry.getValue().getHeadStats();
			final HandleMaterialStats handleStats = entry.getValue().getHandleStats();
			final ExtraMaterialStats extraStats = entry.getValue().getExtraStats();
			final BowMaterialStats bowStats = entry.getValue().getBowStats();
			final ArrowShaftMaterialStats arrowStats = entry.getValue().getArrowStats();
			final FletchingMaterialStats fletchingStats = entry.getValue().getFletchingStats();
			
			if( TinkerRegistry.getMaterial(entry.getKey()) == entry.getValue().getMaterial() ) {
				// the material was properly registered
				Material work = entry.getValue().getMaterial();
				TinkerRegistry.addMaterialStats( work, headStats, handleStats, extraStats );
				TinkerRegistry.addMaterialStats( work, bowStats );
				TinkerRegistry.addMaterialStats( work, arrowStats );
				TinkerRegistry.addMaterialStats( work, fletchingStats );
			}
		}
	}
	
	public void registerAlloy(@Nonnull final String name, @Nonnull final FluidStack result, FluidStack... input ) {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<String,AlloyRecipe> curRecipes = alloys.getOrDefault(curMod, new HashMap<>());
		if( curRecipes.containsKey(name) )
			return;
		
		curRecipes.put(name, new AlloyRecipe(result, input));
		alloys.put(curMod, curRecipes);
	}
	
	public void registerMelting(@Nonnull final Item item, @Nonnull final FluidStack result ) {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<Item, FluidStack> modMelts = meltings.getOrDefault(curMod, new HashMap<>());
		
		if( modMelts.containsKey(item) )
			return;
		
		modMelts.put(item, result);
		meltings.put(curMod, modMelts);
	}
	
	public void setRepresentativeItem() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			entry.getValue().setRepresentativeItem(Names.INGOT.toString());
		}
	}

	public void setupIntegrations() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		BaseMetals.logger.debug("setupIntegrations() for mod %s", curMod);
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			BaseMetals.logger.debug("processing material %s from mod %s", entry.getKey(), curMod);
			TCMaterial material = entry.getValue();
			
			material.settle();
			
			material.setFluid(material.getMMDMaterial().getFluid());

			if( material.getCraftable() )
				material.getMaterial().setCraftable(true);
			else
				material.getMaterial().setCastable(true);
			
			MaterialIntegration integration = new MaterialIntegration(material.getMaterial(), material.getFluid(), material.getMMDMaterial().getCapitalizedName());
			
			if( material.getRepresentativeItemName() != null ) 				
				integration.setRepresentativeItem(material.getRepresentativeItemName());
			
			if( material.toolForge() )
				integration.toolforge();

			List<MaterialIntegration> cmi = integrations.getOrDefault(curMod, new ArrayList<>());
			cmi.add(integration);
			integrations.put(curMod, cmi);
			TinkerRegistry.integrate(integration);
			integration.preInit();
		}
	}
	
	private void addTraits(TCMaterial mat) {
		if( !mat.getTraits().isEmpty() ) {
			for( final Entry<String, List<ITrait>> ent : mat.getTraits().entrySet() ) {
				String traitLoc = ent.getKey();
				for( ITrait trait : ent.getValue() ) {
					if( traitLoc.equals("general") ) {
						mat.getMaterial().addTrait(trait);
					} else {
						mat.getMaterial().addTrait(trait,traitLoc);
					}
				}
			}			
		}
	}
	
	public void integrationsInit() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			final TCMaterial work = entry.getValue();
			final MMDMaterial curMMDMat = work.getMMDMaterial();
			final Material curMat = work.getMaterial();

			Item rI = curMMDMat.getItem(Names.INGOT);
			curMat.addItem(rI, 1, 144);
			curMat.setRepresentativeItem(rI);
			curMat.addCommonItems(curMMDMat.getCapitalizedName());
			addTraits(work);
		}
	}
	
	
	public void registerMeltings() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<Item, FluidStack> myMelts = meltings.getOrDefault(curMod, Collections.emptyMap());
		
		if( myMelts.isEmpty())
			return;
		
		for( final Entry<Item, FluidStack> ent : myMelts.entrySet() ) {
			Item item = ent.getKey();
			FluidStack fluidStack = ent.getValue();
			Fluid fluid = fluidStack.getFluid();
			int amount = fluidStack.amount;
			
			TinkerRegistry.registerMelting(item, fluid, amount);
		}		
	}
	
	public void registerAlloys() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		Map<String,AlloyRecipe> curRecipes;
		if( alloys.containsKey(curMod) ) {
			curRecipes = alloys.get(curMod);
			for( final Entry<String, AlloyRecipe> ent : curRecipes.entrySet()) {
				TinkerRegistry.registerAlloy(ent.getValue());
			}
		}
	}
	
	public void resolveTraits() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			entry.getValue().resolveTraits();
		}
	}

	public void setMaterialsVisible() {
		final String curMod = Loader.instance().activeModContainer().getModId();
		for( final Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			if(entry.getValue().getMaterial().isHidden()) {
				BaseMetals.logger.fatal("Setting material %s to visible", entry.getValue().getMMDMaterial().getCapitalizedName());
				entry.getValue().getMaterial().setVisible();
			}
		}
	}
}
