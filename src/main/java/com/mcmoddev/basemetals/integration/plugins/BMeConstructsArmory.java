package com.mcmoddev.basemetals.integration.plugins;


import c4.conarm.common.armor.traits.ArmorTraits;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.content.ContentPolicy;
import com.mcmoddev.basemetals.content.MaterialForm;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.integration.IntegrationInitEvent;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.ConstructsArmory;
import com.mcmoddev.lib.integration.plugins.armory.traits.MMDTraitsCA;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.integration.plugins.tinkers.traits.MMDTraits;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.tools.TinkerTraits;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.mcmoddev.lib.integration.plugins.ConstructsArmory.*;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeConstructsArmory.PLUGIN_MODID, versions = BMeConstructsArmory.PLUGIN_MODID
        + "@[1.12.2-2.10.1.87,)")
public final class BMeConstructsArmory implements IIntegration {

    public static final String PLUGIN_MODID = ConstructsArmory.PLUGIN_MODID;
	private final List<com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial> highFantasyMaterials =
			new ArrayList<>();

    public BMeConstructsArmory() {
    }

    @Override
    public void init() {
        INSTANCE.init();
        if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);    
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void materialRegistration(MaterialRegistrationEvent ev) {
        if(Config.Options.isModEnabled(PLUGIN_MODID)){
			if (ContentPolicy.active().mode() != ContentMode.HIGH_FANTASY) {
				return;
			}
			highFantasyMaterials.clear();
			ev.getRegistry().getEntries().stream()
					.map(ent -> ent.getValue())
					.forEach(highFantasyMaterials::add);
		}
	}

	/** Adds armor stats after ConArm has installed its default stat types. */
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void armorRegistration(final IntegrationInitEvent event) {
		final Collection<com.mcmoddev.lib.integration.plugins.tinkers.TinkersMaterial> materials =
				ContentPolicy.active().mode() == ContentMode.HIGH_FANTASY
						? highFantasyMaterials : BMeTinkersConstruct.restrictedMaterials();
		materials.stream()
				.filter(mat -> ContentPolicy.active().allows(mat.getName(), MaterialForm.CHESTPLATE))
				.forEach(mat -> {
					TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getCoreStats(),
							mat.getPlatesStats(), mat.getTrimStats());
					switch (mat.getName()){
                            case MaterialNames.ADAMANTINE:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.vengeful, ArmorTraits.prideful);
                                break;
                            case MaterialNames.ANTIMONY:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.brittle);
                                break;
                            case MaterialNames.AQUARIUM:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.rough, ArmorTraits.aquaspeed);
                                break;
                            case MaterialNames.BRASS:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.dense);
                                break;
                            case MaterialNames.COLDIRON:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.icy);
                                break;
                            case MaterialNames.LEAD:
								// Keep the legacy trait request even though some ConArm versions ignore it.
								addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.malleable);
                                break;
                            case MaterialNames.MITHRIL:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.blessed);
                                break;
                            case MaterialNames.NICKEL:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.magnetic2, ArmorTraits.magnetic);
                                addArmorTrait(mat.getTinkerMaterial(), TinkerTraits.shocking);
                                break;
                            case MaterialNames.PEWTER:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.malleable);
                                break;
                            case MaterialNames.STARSTEEL:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraits.sparkly, ArmorTraits.enderport);
                                break;
                            case MaterialNames.ZINC:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.reactive);
                        }
                    });
	}
}
