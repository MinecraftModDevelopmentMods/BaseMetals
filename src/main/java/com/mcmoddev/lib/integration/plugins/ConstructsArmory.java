package com.mcmoddev.lib.integration.plugins;

import c4.conarm.lib.materials.ArmorMaterials;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * CA Plugin, redesigned.
 *
 * @author Bruno Gaspar &lt;kurokyoudai@live.com&gt;
 * @since 2018-10-10
 */
public class ConstructsArmory extends TinkersConstruct {

	public static final String PLUGIN_MODID = "conarm";
	
	private static boolean initDone = false;

//	private static final IForgeRegistry<TinkersMaterial> materialsRegistry = TinkersConstruct.getMaterialsRegistry();
//	private static final TinkerTraitRegistry traitsRegistry = TinkersConstruct.getTraitsRegistry(); // technically does nothing
//	private static final TinkerModifierRegistry modifiersRegistry = TinkersConstruct.getModifiersRegistry(); // technically does nothing

	// other storage
	public static final ConstructsArmory INSTANCE = new ConstructsArmory();

	protected static void addArmorTrait(Material material, ITrait trait1, ITrait trait2){
		ArmorMaterials.addArmorTrait(material, trait1, trait2);
	}

//	private static final Deque<Material> materialsToAdd = Queues.newArrayDeque();

//	@Override
//	public void init() {
//		if (!Options.isModEnabled(PLUGIN_MODID) || initDone) {
//			return;
//		}
//		initDone = true;
//
//		MinecraftForge.EVENT_BUS.register(this);
//	}

//	/**
//	 *
//	 * @param event
//	 */
//	@SubscribeEvent
//	public void preInit(final IntegrationPreInitEvent event) {
//		// purposefully blank
//	}

//	/**
//	 *
//	 * @param event
//	 */
//	@SubscribeEvent
//	public void init(final IntegrationInitEvent event) {
//		// purposefully blank
//	}
//
//	/**
//	 *
//	 * @param event
//	 */
//	@SubscribeEvent
//	public void postInit(final IntegrationPostInitEvent event) {
//		// purposefully blank
//	}
}
