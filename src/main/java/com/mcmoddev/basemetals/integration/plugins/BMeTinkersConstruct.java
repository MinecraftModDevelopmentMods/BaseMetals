package com.mcmoddev.basemetals.integration.plugins;

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
import com.mcmoddev.lib.util.CheeseMath;
import com.mcmoddev.lib.util.Config.Options;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.tuple.Pair;

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
        if(!slimeknights.tconstruct.TConstruct.pulseManager.isPulseLoaded("TinkerSmeltery")) {
            return;
        }
        if (!Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent(priority=EventPriority.LOWEST)
	public void registerMiscShit(RegistryEvent.Register<IRecipe> ev) {
		List<Names> items = Arrays.asList( Names.CHESTPLATE, Names.LEGGINGS, Names.HELMET, Names.BOOTS);
		List<MMDMaterial> materials = (List<MMDMaterial>)Materials.getMaterialsByMod(BaseMetals.MODID);
		materials.stream()
		.filter( b -> b.getFluid() != null)
		.forEach( base ->
			items.stream()
			.filter( n -> base.hasItem(n))
			.filter( nv -> base.getItem(nv).getRegistryName().getNamespace().equalsIgnoreCase(BaseMetals.MODID))
			.map( name -> Pair.<String, Integer>of(name.toString(), Integer.valueOf(CheeseMath.getIngotCount(base, base.getItemStack(name)))))
			.filter( evx -> evx.getValue() > 1)
			.forEach( evga -> TinkersConstruct.INSTANCE.addExtraMelting(base.getName(), evga.getRight() * Material.VALUE_Ingot, base.getItemStack(evga.getLeft()))));
	}

	@SubscribeEvent
	public void postInit(final IntegrationPostInitEvent event){
		if(Options.isMaterialEnabled(MaterialNames.PRISMARINE) && Options.isFluidEnabled(MaterialNames.PRISMARINE)) {
			registerPrismarineFullCasting();
		}
		// TODO Add a config option to enable/disable this
		if(Options.isMaterialEnabled(MaterialNames.ENDER) && Options.isFluidEnabled(MaterialNames.ENDER)) {
			registerEnderFullCasting();
		}
	}

	private static void registerPrismarineFullCasting(){
		int gemAmount = 144;
		int blockAmount = 4 * gemAmount;

		Fluid fluid = FluidRegistry.getFluid(MaterialNames.PRISMARINE);
		registerTableCasting(new ItemStack(Items.PRISMARINE_SHARD, 1), TinkerSmeltery.castGem, fluid, gemAmount);
		registerBasinCasting(new ItemStack(Blocks.PRISMARINE, 1),fluid, blockAmount);

	}

	private static void registerEnderFullCasting(){
		int gemAmount = 144;

		Fluid fluid = FluidRegistry.getFluid(MaterialNames.ENDER);
		registerTableCasting(new ItemStack(Items.ENDER_PEARL, 1), TinkerSmeltery.castGem, fluid, gemAmount);

	}

	@SubscribeEvent
	public void materialRegistration(MaterialRegistrationEvent ev) {

		registerMaterial(MaterialNames.ADAMANTINE, ev,
				TraitNames.COLDBLOODED, TraitNames.INSATIABLE);
		registerMaterial(MaterialNames.ANTIMONY, ev,
				TraitNames.BRITTLE);
		registerMaterial(MaterialNames.AQUARIUM, ev,
				TraitNames.AQUADYNAMIC, TinkerTraitLocation.HEAD,
				TraitNames.JAGGED, TinkerTraitLocation.HEAD,
				TraitNames.AQUADYNAMIC);
		registerMaterial(MaterialNames.BISMUTH, ev);
		registerMaterial(MaterialNames.BRASS, ev,
				TraitNames.DENSE);
		registerMaterial(MaterialNames.COLDIRON, ev,
				TraitNames.FREEZING);
		registerMaterial(MaterialNames.CUPRONICKEL, ev);
		registerMaterial(MaterialNames.INVAR, ev);
		registerMaterial(MaterialNames.LEAD, ev,
				TraitNames.SOFT);
		registerMaterial(MaterialNames.MITHRIL, ev,
				TraitNames.HOLY);
		registerMaterial(MaterialNames.NICKEL, ev,
				TraitNames.MAGNETIC, TinkerTraitLocation.HEAD,
				TraitNames.SHOCKING, TinkerTraitLocation.HEAD,
				TraitNames.MAGNETIC2,
				TraitNames.SHOCKING);
		registerMaterial(MaterialNames.PEWTER, ev,
				TraitNames.SOFT);
		registerMaterial(MaterialNames.PLATINUM, ev);
		registerMaterial(MaterialNames.STARSTEEL, ev,
				TraitNames.ENDERFERENCE, TinkerTraitLocation.HEAD, TraitNames.SPARKLY);
		registerMaterial(MaterialNames.TIN, ev);
		registerMaterial(MaterialNames.ZINC, ev,
				TraitNames.REACTIVE);
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

	@SubscribeEvent
	public void registerMeltings(TinkersExtraMeltingsEvent ev) {
		if (isMaterialFluidEnabled(MaterialNames.COAL)) {
			TinkersConstruct.INSTANCE.addExtraMelting(FluidRegistry.getFluidStack(MaterialNames.COAL, 144),
					new ItemStack(net.minecraft.init.Items.COAL));
		}

		if (isMaterialFluidEnabled(MaterialNames.ENDER)) {
			final MMDMaterial ender = Materials.getMaterialByName(MaterialNames.ENDER);
			TinkersConstruct.INSTANCE.addExtraMelting(FluidRegistry.getFluidStack(ender.getName(), 144),
				new ItemStack(Items.ENDER_PEARL));
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
