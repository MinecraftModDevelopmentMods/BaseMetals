package com.mcmoddev.lib.integration.plugins;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.Pair;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.basemetals.integration.plugins.TinkersModifierRegistryEvent;
import com.mcmoddev.basemetals.integration.plugins.TinkersTraitRegistryEvent;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierFakeDiamond;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierLeadPlated;
import com.mcmoddev.lib.integration.plugins.tinkers.modifiers.ModifierToxic;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitBrittle;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitHeavy;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitRadioactive;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSoft;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitSparkly;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.TraitToxic;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.ConfigBase.Options;

import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.smeltery.AlloyRecipe;
import slimeknights.tconstruct.library.traits.ITrait;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * TiC Plugin, redesigned.
 *
 * @author Daniel Hazelton &lt;dshadowwolf@gmail.com&gt;
 *
 */
public class TinkersConstructBase implements IIntegration {

	public static final String PLUGIN_MODID = "tconstruct";
	private static final IForgeRegistry<TinkerMaterial> registry = new RegistryBuilder<TinkerMaterial>()
			.disableSaving()
			.setMaxID(Integer.MAX_VALUE)
			.setName(new ResourceLocation("mmdlib", "tinker_registry"))
			.setType(TinkerMaterial.class)
			.create();
	
	private static final Map<String, AlloyRecipe> alloys = new HashMap<>();
	private static final Map<String, ITrait> traits = new HashMap<>();
	private static final Map<String, Modifier> modifiers = new HashMap<>();
	private static final List<Pair<ItemStack, FluidStack>> extraMeltings = new LinkedList<>();
	
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		registerTraits();
		MinecraftForge.EVENT_BUS.post(new TinkersTraitRegistryEvent());
		registerModifiers();
		MinecraftForge.EVENT_BUS.post(new TinkersModifierRegistryEvent());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void registerModifiers(final RegistryEvent.Register<Item> event) {
		/**
		 * actually register modifiers and traits with TiCon here
		 */
	}


	private void registerTraits() {
		registerTrait("brittle", new TraitBrittle());
		registerTrait("heavy", new TraitHeavy());
		registerTrait("radioactive", new TraitRadioactive());
		registerTrait("soft", new TraitSoft());
		registerTrait("sparkly", new TraitSparkly());
		registerTrait("toxic", new TraitToxic());
	}

	private void registerModifiers() {
		registerModifier("fake diamond", new ModifierFakeDiamond());
		registerModifier("lead plated", new ModifierLeadPlated());
		registerModifier("toxic", new ModifierToxic());
	}

	public TinkerMaterial newMaterial(@Nonnull MMDMaterial material) {
		return new TinkerMaterial(material);
	}
	
	public void registerMaterial(@Nonnull TinkerMaterial material) {
		String activeMod = Loader.instance().activeModContainer().getModId();
		ResourceLocation name = new ResourceLocation(activeMod, material.getName().toLowerCase());
		if(material.getRegistryName()==null)
			material.setRegistryName(name);
		registry.register(material);
	}
	
	public TinkerMaterial getMaterial(@Nonnull String name) {
		if (name.matches(":")) {
			return this.getMaterial(new ResourceLocation(name));
		}
		
		return this.getMaterial(Loader.instance().activeModContainer().getModId(), name);
	}
	
	public TinkerMaterial getMaterial(@Nonnull String modId, @Nonnull String name) {
		return this.getMaterial(new ResourceLocation(modId, name));
	}
	
	public TinkerMaterial getMaterial(@Nonnull ResourceLocation key) {
		return registry.getValue(key);
	}
	
	public void addExtraMelting(@Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(materialName), name, amount);
	}
	
	public void addExtraMelting(@Nonnull String modId, @Nonnull String materialName, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(modId, materialName), name, amount);
	}
	
	public void addExtraMelting(@Nonnull ResourceLocation loc, @Nonnull String name, int amount) {
		this.addExtraMelting(this.getMaterial(loc), name, amount);
	}
	
	public void addExtraMelting(TinkerMaterial material, @Nonnull String name, int amount) {
		if(material == null) return;
		
		material.addExtraMelting(name, amount);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, Object...traits) {
		registerMaterial(materialName, craftable, castable, true, traits);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable,
			boolean toolForge, Object...traits) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);
		
		int i = 0;
		while (i < traits.length) {
			if (traits[i] instanceof TinkerMaterial.TinkersTraitLocation) {
				mat.addTrait((String)traits[i+1], (TinkerMaterial.TinkersTraitLocation)traits[i]);
				i++;
			} else {
				mat.addTrait((String)traits[i], TinkerMaterial.TinkersTraitLocation.GENERAL);
			}
			i++;
		}
		
		mat.setRegistryName(materialName);
		registerMaterial(mat);
	}
	
	public void registerMaterial(@Nonnull String materialName, boolean craftable, boolean castable, boolean toolForge) {
		MMDMaterial material = Materials.getMaterialByName(materialName);
		TinkerMaterial mat = newMaterial(material);
		mat.setCastable(castable);
		mat.setCraftable(craftable);
		mat.setToolForge(toolForge);
		
		mat.setRegistryName(materialName);
		mat.settle();
		registerMaterial(mat);		
	}
	
	public void registerAlloy(@Nonnull String materialName, FluidStack result, FluidStack...recipe) {
		alloys.put(materialName, new AlloyRecipe(result, recipe));
	}
	
	public void registerTrait(String traitName, ITrait trait) {
		traits.put(traitName, trait);
	}
	
	public void registerModifier(String modifierName, Modifier modifier) {
		modifiers.put(modifierName, modifier);
	}
	
	public void registerMelting(@Nonnull ItemStack item, @Nonnull FluidStack fluid) {
		extraMeltings.add(Pair.of(item, fluid));
	}
	

	@SubscribeEvent
	public void preInit(IntegrationPreInitEvent ev) {
/*		setupIntegrations();
		addMaterialStats();
		ensureMaterialsVisible();
*/	}
	
	@SubscribeEvent
	public void init(IntegrationInitEvent ev) {
/*		registerExtraMeltings();
		addTraits();
		ensureMaterialsVisible();
*/	}

	@SubscribeEvent
	public void postInit(IntegrationPostInitEvent ev) {
/*		registerAlloyRecipes();
		ensureMaterialsVisible();
*/	}
}
