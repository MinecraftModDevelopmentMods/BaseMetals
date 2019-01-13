package com.mcmoddev.lib.integration.plugins;

import c4.conarm.lib.materials.ArmorMaterials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.plugins.tinkers.TinkerTraitRegistry;
import com.mcmoddev.lib.integration.plugins.tinkers.events.TraitRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.armory.traits.MMDTraitsCA;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.traits.ITrait;

/**
 * CA Plugin, redesigned.
 *
 * @author Bruno Gaspar &lt;kurokyoudai@live.com&gt;
 * @since 2018-10-10
 */
public class ConstructsArmory implements IIntegration {

	public static final String PLUGIN_MODID = "conarm";

	private static boolean initDone = false;

	private static final TinkerTraitRegistry traitsRegistry = TinkersConstruct.getTraitsRegistry(); // technically does nothing

	public static final ConstructsArmory INSTANCE = new ConstructsArmory();

	public static void addArmorTrait(Material material, ITrait trait1, ITrait trait2){
		ArmorMaterials.addArmorTrait(material, trait1, trait2);
	}

	public static void addArmorTrait(Material material, ITrait trait1){
		ArmorMaterials.addArmorTrait(material, trait1);
	}

	@Override
	public void init() {
		if (!Config.Options.isModEnabled(PLUGIN_MODID) || initDone) {
			return;
		}
		initDone = true;

		MinecraftForge.EVENT_BUS.register(this);
		registerInternalTraits();
		MinecraftForge.EVENT_BUS.post(new TraitRegistrationEvent(traitsRegistry));
	}

	private void registerInternalTraits() {
		traitsRegistry.register("mmd-icy-ca", MMDTraitsCA.icy);
		traitsRegistry.register("mmd-malleable-ca", MMDTraitsCA.malleable);
		traitsRegistry.register("mmd-toxic-ca", MMDTraitsCA.toxic);
		traitsRegistry.register("mmd-reactive-ca", MMDTraitsCA.reactive);
		traitsRegistry.register("mmd-brittle-ca", MMDTraitsCA.brittle);
		traitsRegistry.register("mmd-poisonous-ca", MMDTraitsCA.poisonous);
	}
}
