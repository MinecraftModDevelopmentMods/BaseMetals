package com.mcmoddev.lib.integration.plugins;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.*;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.ModifierRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersAlloyRecipeEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersExtraMeltingsEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TraitRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.*;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.*;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * TiC Plugin, redesigned.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 * @since 2018-04-26
 */

public class TinkersConstructBase implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";

	// registries
	private static final IForgeRegistry<TinkersMaterial> materialsRegistry = new RegistryBuilder<TinkersMaterial>()
			.disableSaving().setMaxID(65535)
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(TinkersMaterial.class).create();
	private static final TinkerTraitRegistry traitsRegistry = new TinkerTraitRegistry(); // technically does nothing	
	private static final TinkerModifierRegistry modifiersRegistry = new TinkerModifierRegistry(); // technically does nothing
	
	// other storage
	private static final List<Pair<FluidStack,List<FluidStack>>> alloys = Lists.newCopyOnWriteArrayList();
	private static final List<Pair<ItemStack, FluidStack>> extraMeltings = Lists.newCopyOnWriteArrayList();
	public static final TinkersConstructBase INSTANCE = new TinkersConstructBase();
	
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
		registerInternalTraits();
		registerInternalModifiers();
		MinecraftForge.EVENT_BUS.post(new TraitRegistrationEvent(traitsRegistry));
		MinecraftForge.EVENT_BUS.post(new ModifierRegistrationEvent(modifiersRegistry));
	}
	
	/**
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void preInit(final IntegrationPreInitEvent event) {
		MinecraftForge.EVENT_BUS.post(new MaterialRegistrationEvent(materialsRegistry));
		MinecraftForge.EVENT_BUS.post(new TinkersAlloyRecipeEvent(materialsRegistry));
		MinecraftForge.EVENT_BUS.post(new TinkersExtraMeltingsEvent());
		addMaterialStats();
		addTraitsToMaterials();
		materialIntegrationPreInit();
		addMaterials();
		setupExtraSmeltingRecipes();
		registerAlloys();
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void postInit(final IntegrationPostInitEvent event) {
		// shards
		// stuff like Fortify and Extra Mod bits
		// Cating Table stuff not handled automagically
		// Smelter Fuels -- Event ?
		com.mcmoddev.basemetals.BaseMetals.logger.fatal("Tinkers Material Registry Dump Start");
		TinkerRegistry.getAllMaterials().stream()
		.forEach(material -> com.mcmoddev.basemetals.BaseMetals.logger.fatal("Material %s (%s -- %s)", material.getIdentifier(), material.getLocalizedName(), material));
		com.mcmoddev.basemetals.BaseMetals.logger.fatal("Tinkers Material Registry Dump End");
	}

	public void addAlloyRecipe(String outputName, Integer outputAmount, Object...recipe) {
		FluidStack output = FluidRegistry.getFluidStack(outputName, outputAmount);
		List<FluidStack> ingredients = Lists.newCopyOnWriteArrayList();
		
		int i = 0;
		while(i < recipe.length) {
			if(recipe[i] instanceof String) {
				ingredients.add(FluidRegistry.getFluidStack((String)recipe[i], (Integer)recipe[++i]));
			} else if(recipe[i] instanceof Fluid) {
				ingredients.add(new FluidStack((Fluid)recipe[i], (Integer)recipe[++i]));
			} else if(recipe[i] instanceof FluidStack) {
				ingredients.add((FluidStack)recipe[i]);
			} else {
				throw new IllegalArgumentException(String.format("Item of unknown type %s (%s) for an alloy recipe found in recipe for %d mB of %s, parameter %d",
						recipe[i].getClass().getName(), recipe[i].toString(), outputAmount, outputName, i+2));
			}
			i++;
		}
		alloys.add(Pair.of(output, ingredients));
	}
	
	public void addExtraMelting(String outputFluid, Integer amount, Item item) {
		addExtraMelting(outputFluid, amount, new ItemStack(item));
	}
	
	public void addExtraMelting(String outputFluid, Integer amount, Block block) {
		addExtraMelting(outputFluid, amount, Item.getItemFromBlock(block));
	}
	
	public void addExtraMelting(String outputFluid, Integer amount, ItemStack itemStack) {
		addExtraMelting(FluidRegistry.getFluidStack(outputFluid, amount), itemStack);
	}
	
	public void addExtraMelting(FluidStack output, ItemStack itemStack) {
		extraMeltings.add(Pair.of(itemStack, output));
	}
	
	// private, internal stuff from here on out

	@SuppressWarnings("deprecation")
	private void addMaterials() {
		materialsRegistry.getValues().stream()
		.forEach(tm -> TinkerRegistry.addMaterial(tm.getTinkerMaterial()));
	}
	
	private void registerInternalTraits() {
		traitsRegistry.register("brittle", MMDTraits.brittle);
		traitsRegistry.register("heavy", MMDTraits.heavy);
		traitsRegistry.register("radioactive", MMDTraits.radioactive);
		traitsRegistry.register("reactive", MMDTraits.reactive);
		traitsRegistry.register("soft", MMDTraits.soft);
		traitsRegistry.register("sparkly", MMDTraits.sparkly);
		traitsRegistry.register("toxic", MMDTraits.toxic);
	}
	
	private void registerInternalModifiers() {
		modifiersRegistry.register(new ResourceLocation("fake_diamond"), new ModifierFakeDiamond());
		modifiersRegistry.register(new ResourceLocation("lead_plated"), new ModifierLeadPlated());
		modifiersRegistry.register(new ResourceLocation("toxic"), new ModifierToxic());
	}
	
	@SuppressWarnings("deprecation")
	private void materialIntegrationPreInit() {
		materialsRegistry.getValues().stream()
		.forEach(tm -> {
			Material m = TinkerRegistry.getMaterial(tm.getName());
			MMDMaterial base = tm.getMMDMaterial();
			if(m == Material.UNKNOWN) m = tm.create().getTinkerMaterial();
			
			m.setCastable(tm.getCastable()).setCraftable(tm.getCraftable());

			m.addItemIngot(Oredicts.INGOT+base.getCapitalizedName());
			m.setVisible();
			
			if(tm.getToolForge())
				TinkerRegistry.integrate(m, base.getFluid(), base.getCapitalizedName()).toolforge();
			else
				TinkerRegistry.integrate(m, base.getFluid(), base.getCapitalizedName());
				
			com.mcmoddev.basemetals.BaseMetals.logger.fatal("Material %s - %s // %s (%s == %s  ??)", base.getName(), tm.getTinkerMaterial(), m, m.getIdentifier(), TinkerRegistry.getMaterial(base.getName()));
			
			
		});
	}
	
	private void addMaterialStats() {
		materialsRegistry.getEntries().stream()
		.map(ent -> ent.getValue())
		.forEach( mat -> TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getArrowShaftStats(),
					mat.getBowStats(), mat.getBowStringStats(), mat.getFletchingStats(),
					mat.getHeadStats(), mat.getHandleStats(), mat.getExtraStats()));
	}
	
	private void addTraitsToMaterials() {
		materialsRegistry.getEntries().stream()
		.map(ent -> ent.getValue())
		.filter(tm -> tm.hasTraits())
		.forEach( tm ->
			tm.getTraits().stream()
			.forEach( tp -> {
				ITrait trait = traitsRegistry.get(tp.getKey());
				Material m = TinkerRegistry.getMaterial(tm.getTinkerMaterial().getIdentifier());
				String loc;
				if(m == Material.UNKNOWN) m = tm.getTinkerMaterial();
				if( trait != null) {
					if(tp.getValue() == TinkerTraitLocation.GENERAL) {
						com.mcmoddev.basemetals.BaseMetals.logger.fatal("Adding trait %s to material %s", trait.getIdentifier(), m.getIdentifier());
						m.addTrait(trait);
					} else {
						loc = tp.getValue().toString().toLowerCase(Locale.US);
						com.mcmoddev.basemetals.BaseMetals.logger.fatal("Adding trait %s to material %s for trait location %s", trait.getIdentifier(), m.getIdentifier(), loc);
						m.addTrait(trait, loc);
					}
				} else {
					com.mcmoddev.basemetals.BaseMetals.logger.fatal("Trait %s does not exist ?", tp.getKey());
				}
			})
		);
	}
	
	private void setupExtraSmeltingRecipes() {
		// loop over our registered Extra Smelting recipes
		extraMeltings.stream().forEach( emt ->
			TinkerRegistry.registerMelting(emt.getLeft(), emt.getRight().getFluid(), emt.getRight().amount));
	}

	private void registerAlloys() {
		// first we start with the alloys that are materials
		materialsRegistry.getEntries().stream()
		.map(ent -> ent.getValue())
		.filter(tm -> tm.hasAlloyRecipe())
		.forEach(tm -> {
			List<FluidStack> rawRecipe = tm.getAlloyRecipe();
			FluidStack output = rawRecipe.get(0);
			List<FluidStack> inputs = Lists.newCopyOnWriteArrayList();
			
			for(int i = 1; i < rawRecipe.size(); i++) {
				inputs.add(rawRecipe.get(i));
			}

			String temp = String.join(", ", inputs.stream().map( f -> String.format("%s@%dmB", f.getLocalizedName(), f.amount)).toArray(String[]::new));
			com.mcmoddev.basemetals.BaseMetals.logger.fatal("Adding alloy recipe to make %s@%dmB from [ %s ]", output.getLocalizedName(), output.amount, temp);
			
			AlloyRecipe recipe = new AlloyRecipe(output, inputs.toArray(new FluidStack[inputs.size()]));
			TinkerRegistry.registerAlloy(recipe);
		});
		
		// then we do the alloys registered separate...
		alloys.stream().forEach( p -> {
			List<FluidStack> inputs = p.getRight();
			FluidStack output = p.getLeft();

			String temp = String.join(", ", inputs.stream().map( f -> String.format("%s@%dmB", f.getLocalizedName(), f.amount)).toArray(String[]::new));
			com.mcmoddev.basemetals.BaseMetals.logger.fatal("Adding alloy recipe to make %s@%dmB from [ %s ]", output.getLocalizedName(), output.amount, temp);

			AlloyRecipe recipe = new AlloyRecipe(output, inputs.toArray(new FluidStack[inputs.size()]));
			TinkerRegistry.registerAlloy(recipe);
		});
	}
	
	/*private void addCommonItemsToMaterials() {
		integrations.keySet().stream()
		.forEach(mat -> {
			Material rm = TinkerRegistry.getMaterial(mat.getName());
			if(rm == Material.UNKNOWN) rm = mat.getTinkerMaterial();
			MMDMaterial base = mat.getMMDMaterial();
			rm.addCommonItems(base.getCapitalizedName());
		});
	}*/
}
