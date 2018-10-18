package com.mcmoddev.basemetals.integration.plugins;

import java.util.Locale;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.data.TraitNames;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.TinkersConstruct;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersAlloyRecipeEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TinkersExtraMeltingsEvent;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Config.Options;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import static com.mcmoddev.lib.integration.plugins.TinkersConstruct.registerBasinCasting;
import static com.mcmoddev.lib.integration.plugins.TinkersConstruct.registerTableCasting;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeTinkersConstruct.PLUGIN_MODID, versions = BMeTinkersConstruct.PLUGIN_MODID
		+ "@[1.12.2-2.7.4.0,)")
public class BMeTinkersConstruct implements IIntegration {

	public static final String PLUGIN_MODID = TinkersConstruct.PLUGIN_MODID;

	public BMeTinkersConstruct() {
		// do nothing
	}

	@Override
	public void init() {
		TinkersConstruct.INSTANCE.init();
		if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void postInit(final IntegrationPostInitEvent event){
		registerPrismarineFullCasting();
	}

	public static void registerPrismarineFullCasting(){
		int gemAmount = 144;
		int blockAmount = 4 * gemAmount;

		Fluid fluid = FluidRegistry.getFluid(MaterialNames.PRISMARINE);
		registerTableCasting(new ItemStack(Items.PRISMARINE_SHARD, 1), TinkerSmeltery.castGem, fluid, gemAmount);
		registerBasinCasting(new ItemStack(Blocks.PRISMARINE, 1),fluid, blockAmount);

	}

	@SubscribeEvent
	public void materialRegistration(MaterialRegistrationEvent ev) {

		registerMaterial(Options.isMaterialEnabled(MaterialNames.ADAMANTINE),
				MaterialNames.ADAMANTINE, ev, TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.ANTIMONY), MaterialNames.ANTIMONY, ev);
		registerMaterial(Options.isMaterialEnabled(MaterialNames.AQUARIUM), MaterialNames.AQUARIUM, ev,
				TraitNames.AQUADYNAMIC, TinkerTraitLocation.HEAD,
				TraitNames.JAGGED, TinkerTraitLocation.HEAD,
				TraitNames.AQUADYNAMIC);
		registerMaterial(MaterialNames.BISMUTH, ev);
		registerMaterial(MaterialNames.BRASS, ev, TraitNames.DENSE);
		registerMaterial(MaterialNames.COLDIRON, ev, TraitNames.FREEZING);
		registerMaterial(MaterialNames.CUPRONICKEL, ev);
		registerMaterial(MaterialNames.INVAR, ev);
		registerMaterial(MaterialNames.MITHRIL, ev, TraitNames.HOLY);
		registerMaterial(MaterialNames.NICKEL, ev);
		registerMaterial(MaterialNames.PEWTER, ev, TraitNames.SOFT);
		registerMaterial(MaterialNames.PLATINUM, ev);
		registerMaterial(MaterialNames.STARSTEEL, ev, TraitNames.ENDERFERENCE,
				TinkerTraitLocation.HEAD, TraitNames.SPARKLY);
		registerMaterial(MaterialNames.TIN, ev);
		registerMaterial(MaterialNames.ZINC, ev);
	}

	protected void registerMaterial(final String name, MaterialRegistrationEvent ev, final Object... traits){
		registerMaterial(Options.isMaterialEnabled(name), name, ev, traits);
	}

	protected void registerMaterial(final boolean active, final String name,
								  MaterialRegistrationEvent ev, final Object... traits) {
		registerMaterial(active, name, true, false, ev, traits);
	}

	protected void registerMaterial(final boolean active, final String name, final boolean castable, final boolean craftable,
			MaterialRegistrationEvent ev, final Object... traits) {
		if (active) {
			TinkersMaterial mat = new TinkersMaterial(Materials.getMaterialByName(name))
					.setCastable(castable).setCraftable(craftable).setToolForge(true);
			
			int i = 0;

			while(i < traits.length) {
				TinkerTraitLocation loc;
				String trait;
				Object item = traits[i];
				if(item instanceof TinkerTraitLocation) {
					loc = (TinkerTraitLocation)item;
					trait = ((String)traits[++i]).toLowerCase(Locale.US);
					
					mat.addTrait(trait, loc);
				} else {
					trait = ((String)item).toLowerCase(Locale.US);
					mat.addTrait(trait);
				}
				i++;
			}

			ev.getRegistry().register(mat.create());
		}
	}

	private void registerMaterial(final boolean active, final String name, final boolean castable,
			final boolean craftable, MaterialRegistrationEvent ev) {
		if (active) {
			TinkersMaterial mat = new TinkersMaterial(Materials.getMaterialByName(name))
					.setCastable(castable).setCraftable(craftable).setToolForge(true).create();

			ev.getRegistry().register(mat);
		}
	}

	@SubscribeEvent
	public void registerMeltings(TinkersExtraMeltingsEvent ev) {
		if (isMaterialFluidEnabled(MaterialNames.COAL)) {
			TinkersConstruct.INSTANCE.addExtraMelting(FluidRegistry.getFluidStack(MaterialNames.COAL, 144),
					new ItemStack(net.minecraft.init.Items.COAL));
		}

		if (isMaterialFluidEnabled(MaterialNames.MERCURY)) {
			final MMDMaterial mercury = Materials.getMaterialByName(MaterialNames.MERCURY);
			TinkersConstruct.INSTANCE.addExtraMelting(FluidRegistry.getFluidStack(mercury.getName(), 144),
					mercury.getItemStack(Names.INGOT));
		}

		if (isMaterialFluidEnabled(MaterialNames.PRISMARINE)) {
			final MMDMaterial prismarine = Materials.getMaterialByName(MaterialNames.PRISMARINE);
			TinkersConstruct.INSTANCE.addExtraMelting(FluidRegistry.getFluidStack(prismarine.getName(), 144),
					new ItemStack(net.minecraft.init.Items.PRISMARINE_SHARD));
		}
	}

	private boolean isMaterialFluidEnabled(String identifier){
		return Options.isMaterialEnabled(identifier) && FluidRegistry.getFluid(identifier) != null;
	}

	private boolean isValidAlloyMaterial(String identifier){
		return Materials.hasMaterial(identifier) && FluidRegistry.getFluid(identifier) != null;
	}

	@SubscribeEvent
	public void registerMyAlloys(TinkersAlloyRecipeEvent ev) {
		if (isValidAlloyMaterial(MaterialNames.AQUARIUM)
				&& isValidAlloyMaterial(MaterialNames.COPPER)
				&& isValidAlloyMaterial(MaterialNames.ZINC)) {
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack zinc = FluidRegistry.getFluidStack(MaterialNames.ZINC, 1);
			final FluidStack prismarine = FluidRegistry.getFluidStack(MaterialNames.PRISMARINE, 3);
			ev.addAlloyRecipe(MaterialNames.AQUARIUM, 3, copper, zinc, prismarine);
		}

		if (isValidAlloyMaterial(MaterialNames.CUPRONICKEL)
				&& isValidAlloyMaterial(MaterialNames.COPPER)
				&& isValidAlloyMaterial(MaterialNames.NICKEL)) {
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 3);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			ev.addAlloyRecipe(MaterialNames.CUPRONICKEL, 4, copper, nickel);
		}

		if (isValidAlloyMaterial(MaterialNames.INVAR)
				&& isValidAlloyMaterial(MaterialNames.NICKEL)) {
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 2);
			final FluidStack nickel = FluidRegistry.getFluidStack(MaterialNames.NICKEL, 1);
			ev.addAlloyRecipe(MaterialNames.INVAR, 3, iron, nickel);
		}

		if (isValidAlloyMaterial(MaterialNames.MITHRIL)
				&& isValidAlloyMaterial(MaterialNames.COLDIRON)
				&& isValidAlloyMaterial(MaterialNames.SILVER)
				&& isValidAlloyMaterial(MaterialNames.MERCURY)) {
			final FluidStack coldiron = FluidRegistry.getFluidStack(MaterialNames.COLDIRON, 1);
			final FluidStack silver = FluidRegistry.getFluidStack(MaterialNames.SILVER, 2);
			final FluidStack mercury = FluidRegistry.getFluidStack(MaterialNames.MERCURY, 1);
			ev.addAlloyRecipe(MaterialNames.MITHRIL, 3, coldiron, silver, mercury);
		}

		if (isValidAlloyMaterial(MaterialNames.PEWTER) && Materials.hasMaterial(MaterialNames.LEAD)
				&& isValidAlloyMaterial(MaterialNames.COPPER)
				&& isValidAlloyMaterial(MaterialNames.TIN)) {
			// this makes what the "Worshipful Company of Pewterers" called "trifle"
			final FluidStack copper = FluidRegistry.getFluidStack(MaterialNames.COPPER, 2);
			final FluidStack tin = FluidRegistry.getFluidStack(MaterialNames.TIN, 137);
			final FluidStack lead = FluidRegistry.getFluidStack(MaterialNames.LEAD, 5);
			ev.addAlloyRecipe(MaterialNames.PEWTER, 144, copper, tin, lead);
		}

		if (isValidAlloyMaterial(MaterialNames.STEEL)) {
			final FluidStack iron = FluidRegistry.getFluidStack(MaterialNames.IRON, 8);
			final FluidStack coal = FluidRegistry.getFluidStack(MaterialNames.COAL, 1);
			ev.addAlloyRecipe(MaterialNames.STEEL, 8, iron, coal);
		}
	}
}
