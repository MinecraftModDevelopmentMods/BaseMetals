package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nonnull;

import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

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
	private static final List<MaterialIntegration> integrations = new ArrayList<>();
	private static final Map<String, Map<String, AlloyRecipe>> alloys = new HashMap<>();
	private static final Map<Item, FluidStack> meltings = new HashMap<>();
	
	private TinkersConstructRegistry() {
		// do nothing
	}
	
	public TCMaterial newMaterial(@Nonnull final String name, @Nonnull final int color) {
		String curMod = Loader.instance().activeModContainer().getModId();
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
	
	public TCMaterial getMaterial(@Nonnull final String name) {
		for( Entry<String, Map<String,TCMaterial>> ent : registry.entrySet()) {
			if(ent.getValue().containsKey(name))
				return ent.getValue().get(name);
		}
		
		return newMaterial(name, 0xFFFFFFFF);
	}
	
	public void addMaterialStats() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			HeadMaterialStats headStats = entry.getValue().getHeadStats();
			HandleMaterialStats handleStats = entry.getValue().getHandleStats();
			ExtraMaterialStats extraStats = entry.getValue().getExtraStats();
			BowMaterialStats bowStats = entry.getValue().getBowStats();
			ArrowShaftMaterialStats arrowStats = entry.getValue().getArrowStats();
			FletchingMaterialStats fletchingStats = entry.getValue().getFletchingStats();
			
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
		String curMod = Loader.instance().activeModContainer().getModId();
		Map<String,AlloyRecipe> curRecipes = alloys.getOrDefault(curMod, new HashMap<>());
		if( curRecipes.containsKey(name) )
			return;
		
		curRecipes.put(name, new AlloyRecipe(result, input));
	}
	
	public void registerMelting(@Nonnull final Item item, @Nonnull final FluidStack result ) {
		if( meltings.containsKey(item) )
			return;
		
		meltings.put(item, result);
	}
	
	public void setRepresentativeItem() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			entry.getValue().setRepresentativeItem(Names.INGOT.toString());
		}
	}

	public void setupIntegrations() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			TCMaterial material = entry.getValue();
			
			material.getMaterial().setFluid(material.getFluid());

			if( material.getCraftable() )
				material.getMaterial().setCraftable(true);
			else
				material.getMaterial().setCastable(true);
			

			MaterialIntegration materialIntegration = new MaterialIntegration( material.getMaterial(), material.getFluid(), material.getMMDMaterial().getCapitalizedName());

			if( material.getRepresentativeItemName() != null ) 				
				materialIntegration.setRepresentativeItem(material.getRepresentativeItemName());
			
			if( material.toolForge() )
				materialIntegration.toolforge();

			integrations.add(materialIntegration);
			TinkerRegistry.integrate(materialIntegration);
		}
	}
	
	public void integrationPreInit() {
		for(MaterialIntegration integration : integrations) {
			integration.preInit();
		}
	}
	
	private void addTraits(TCMaterial mat) {
		if( !mat.getTraits().isEmpty() ) {
			for( Entry<String, List<ITrait>> ent : mat.getTraits().entrySet() ) {
				String traitLoc = ent.getKey();
				for( ITrait trait : ent.getValue() ) {
					if( traitLoc.equals("general") ) {
						mat.getMaterial().addTrait(trait);
					} else {
						mat.getMaterial().addTrait(trait,traitLoc);
					}
					TinkerRegistry.addMaterialTrait(mat.getMaterial().identifier, trait, null);
				}
			}			
		}
	}
	
	public void integrationsInit() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			TCMaterial work = entry.getValue();
			MMDMaterial curMMDMat = work.getMMDMaterial();
			Material curMat = work.getMaterial();

			Item rI = curMMDMat.getItem(Names.INGOT);
			curMat.addItem(rI, 1, 144);
			curMat.setRepresentativeItem(rI);
			curMat.addCommonItems(curMMDMat.getCapitalizedName());
			addTraits(work);
		}
	}
	
	
	public void registerMeltings() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			if( entry.getValue().getExtraMelting().isEmpty() )
				return;
			
			MMDMaterial mat = entry.getValue().getMMDMaterial();
			Fluid outFluid = entry.getValue().getFluid();
			
			for( Entry<String, Integer> ent : entry.getValue().getExtraMelting().entrySet() ) {
				String type = ent.getKey().split(":")[0].toLowerCase();
				String name = ent.getKey().split(":")[1];
				int fluidAmount = ent.getValue().intValue();
				
				if( type.equals("block") )
					TinkerRegistry.registerMelting(mat.getBlock(name), outFluid, fluidAmount);
				else
					TinkerRegistry.registerMelting(mat.getItem(name), outFluid, fluidAmount);
			}
		}
		
		for( Entry<Item, FluidStack> ent : meltings.entrySet() ) {
			TinkerRegistry.registerMelting(ent.getKey(), ent.getValue().getFluid(), ent.getValue().amount);
		}
		
		registerStandardMeltings();
	}
	
	public void registerStandardMeltings() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			TCMaterial tcm = entry.getValue();
			MMDMaterial mat = tcm.getMMDMaterial();
			Fluid output = tcm.getFluid();
			int outputAmount = tcm.getIngotAmount();
			
			if( MaterialNames.COAL.equals(mat.getName()) ) {
				TinkerRegistry.registerMelting("itemCoal", output, outputAmount);
			}
			
			TinkerRegistry.registerMelting(Oredicts.DUST+mat.getCapitalizedName(), output, outputAmount);
			TinkerRegistry.registerMelting(Oredicts.DUST_SMALL, output, outputAmount/9);
		}
	}
		
	public void registerAlloys() {
		String curMod = Loader.instance().activeModContainer().getModId();
		Map<String,AlloyRecipe> curRecipes;
		if( alloys.containsKey(curMod) ) {
			curRecipes = alloys.get(curMod);
			for( Entry<String, AlloyRecipe> ent : curRecipes.entrySet()) {
				TinkerRegistry.registerAlloy(ent.getValue());
			}
		}
	}

	public void resolveTraits() {
		String curMod = Loader.instance().activeModContainer().getModId();
		for( Entry<String, TCMaterial> entry : registry.get(curMod).entrySet()) {
			entry.getValue().resolveTraits();
		}
	}
}
