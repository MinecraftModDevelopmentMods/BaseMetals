package com.mcmoddev.lib.integration.plugins;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.events.*;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierFakeDiamond;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierLeadPlated;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierToxic;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import org.apache.commons.lang3.tuple.Pair;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.Deque;
import java.util.List;
import java.util.Locale;

/**
 * TiC Plugin, redesigned.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 * @since 2018-04-26
 */

public class TinkersConstruct implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";
	
	private static boolean initDone = false;
	
	// registries
	private static final IForgeRegistry<TinkersMaterial> materialsRegistry = new RegistryBuilder<TinkersMaterial>()
			.disableSaving().setMaxID(65535)
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(TinkersMaterial.class).create();
	private static final TinkerTraitRegistry traitsRegistry = new TinkerTraitRegistry(); // technically does nothing
	private static final TinkerModifierRegistry modifiersRegistry = new TinkerModifierRegistry(); // technically does nothing

	protected static IForgeRegistry<TinkersMaterial> getMaterialsRegistry() {
		return materialsRegistry;
	}

	protected static TinkerTraitRegistry getTraitsRegistry() {
		return traitsRegistry;
	}

	protected static TinkerModifierRegistry getModifiersRegistry() {
		return modifiersRegistry;
	}

	// other storage
	private static final List<Pair<FluidStack,List<FluidStack>>> alloys = Lists.newCopyOnWriteArrayList();
	private static final List<Pair<ItemStack, FluidStack>> extraMeltings = Lists.newCopyOnWriteArrayList();
	public static final TinkersConstruct INSTANCE = new TinkersConstruct();

	private static final Deque<Material> materialsToAdd = Queues.newArrayDeque();
	
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;
		
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
		materialIntegrationPreInit();
		addMaterials();
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void materialSetup(final RegistryEvent.Register<IRecipe> ev) {
		setupMaterialItems();
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void init(final IntegrationInitEvent event) {
		addTraitsToMaterials();
		modifiersRegistry.get(new ResourceLocation("lead_plated")).addItem(Oredicts.PLATE+"Lead");
		modifiersRegistry.get(new ResourceLocation("toxic")).addItem(Oredicts.DUST+"Mercury");
		setupExtraSmeltingRecipes();
		registerAlloys();
	}
	
	protected void setupMaterialItems() {
		materialsRegistry.getEntries().stream()
		.forEach(ent -> {
			TinkersMaterial tm = ent.getValue();

			Material m = TinkerRegistry.getMaterial(tm.getName());
			MMDMaterial base = tm.getMMDMaterial();
			if (base.hasItem(Names.INGOT)) {
				m.setRepresentativeItem(base.getItem(Names.INGOT));
			} else if (base.hasItem(Names.ORE)) {
				m.setRepresentativeItem(base.getBlockItemStack(Names.ORE));
			}
			m.addCommonItems(base.getCapitalizedName());			
		});
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void postInit(final IntegrationPostInitEvent event) {
		// purposefully blank
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

	private void addMaterials() {
		while(!materialsToAdd.isEmpty()) {
			Material material = materialsToAdd.pop();
			if(TinkerRegistry.getMaterial(material.getIdentifier()) == Material.UNKNOWN){
				TinkerRegistry.addMaterial(material);
			}
			else if(Options.isForcedTrait(material.getIdentifier())){
				addExistingMaterial(material);
			}
		}
	}

	private void addExistingMaterial(Material material){
		for (TinkerTraitLocation stat:TinkerTraitLocation.values()) {
			for (ITrait trait:material.getAllTraitsForStats(stat.toString())) {
					Material existingMaterial = TinkerRegistry.getMaterial(material.identifier);
					if(!existingMaterial.hasTrait(trait.getIdentifier(), stat.toString())){
						existingMaterial.addTrait(trait, stat.toString());
					}
			}
		}
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
	
	private void materialIntegrationPreInit() {
		materialsRegistry.getEntries().stream()
		.forEach(ent -> {
			TinkersMaterial tm = ent.getValue();

			Material m = TinkerRegistry.getMaterial(tm.getName());
			MMDMaterial base = tm.getMMDMaterial();
			if(m == Material.UNKNOWN) m = tm.create().getTinkerMaterial();

			m.setCastable(tm.getCastable()).setCraftable(tm.getCraftable());
			
			m.addCommonItems(base.getCapitalizedName());
			
			m.setVisible();
			m.setFluid(base.getFluid());
			
			if(tm.getToolForge())
				TinkerRegistry.integrate(m, base.getFluid(), base.getCapitalizedName()).toolforge();
			else
				TinkerRegistry.integrate(m, base.getFluid(), base.getCapitalizedName());
				
			if(!materialsToAdd.contains(m))
				materialsToAdd.addLast(m);
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
						m.addTrait(trait);
					} else {
						loc = tp.getValue().toString().toLowerCase(Locale.US);
						m.addTrait(trait, loc);
					}
				}
			})
		);
	}
	
	private void setupExtraSmeltingRecipes() {
		// loop over our registered Extra Smelting recipes
		extraMeltings.stream().forEach( emt ->
				TinkerRegistry.registerMelting(
						emt.getLeft(), 
						emt.getRight().getFluid(), 
						emt.getRight().amount));
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

			AlloyRecipe recipe = new AlloyRecipe(output, inputs.toArray(new FluidStack[inputs.size()]));
			TinkerRegistry.registerAlloy(recipe);
		});
		
		// then we do the alloys registered separate...
		alloys.stream().forEach( p -> {
			List<FluidStack> inputs = p.getRight();
			FluidStack output = p.getLeft();

			AlloyRecipe recipe = new AlloyRecipe(output, inputs.toArray(new FluidStack[inputs.size()]));
			TinkerRegistry.registerAlloy(recipe);
		});
	}

	public static void registerTableCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount){
		TinkerRegistry.registerTableCasting(output, cast, fluid, amount);
	}

	private static void registerBasinCasting(ItemStack output, ItemStack cast, Fluid fluid, int amount){
		TinkerRegistry.registerBasinCasting(output, cast, fluid, amount);
	}

	public static void registerBasinCasting(ItemStack output, Fluid fluid, int amount){
		registerBasinCasting(output, ItemStack.EMPTY, fluid, amount);
	}
}
