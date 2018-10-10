package com.mcmoddev.lib.integration.plugins;

import com.google.common.collect.Queues;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.IntegrationPostInitEvent;
import com.mcmoddev.lib.integration.IntegrationPreInitEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerModifierRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitLocation;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.util.Config.Options;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

import java.util.Deque;
import java.util.Locale;

/**
 * CA Plugin, redesigned.
 *
 * @author Bruno Gaspar &lt;kurokyoudai@live.com&gt;
 * @since 2018-10-10
 */

public class ConstructsArmory extends TinkersConstruct {

	public static final String PLUGIN_MODID = "conarm";
	
	private static boolean initDone = false;

	private static final IForgeRegistry<TinkersMaterial> materialsRegistry = TinkersConstruct.getMaterialsRegistry();
	private static final TinkerTraitRegistry traitsRegistry = TinkersConstruct.getTraitsRegistry(); // technically does nothing
	private static final TinkerModifierRegistry modifiersRegistry = TinkersConstruct.getModifiersRegistry(); // technically does nothing
	
	// other storage
	public static final ConstructsArmory INSTANCE = new ConstructsArmory();

	private static final Deque<Material> materialsToAdd = Queues.newArrayDeque();
	
	@Override
	public void init() {
		if (!Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	/**
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void preInit(final IntegrationPreInitEvent event) {
		MinecraftForge.EVENT_BUS.post(new MaterialRegistrationEvent(materialsRegistry));
		addMaterialStats();
		addMaterials();
	}

	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void init(final IntegrationInitEvent event) {
		addTraitsToMaterials();
	}
	
	/**
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void postInit(final IntegrationPostInitEvent event) {
		// purposefully blank
	}

	private void addMaterials() {
		while(!materialsToAdd.isEmpty()) {
			TinkerRegistry.addMaterial(materialsToAdd.pop());
		}
	}
	
	private void addMaterialStats() {
		materialsRegistry.getEntries().stream()
		.map(ent -> ent.getValue())
		.forEach( mat -> TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getCoreStats(), mat.getPlatesStats(), mat.getTrimStats()));
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
}
