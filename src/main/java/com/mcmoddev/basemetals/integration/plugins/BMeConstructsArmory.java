package com.mcmoddev.basemetals.integration.plugins;


import c4.conarm.common.armor.traits.ArmorTraits;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.lib.integration.IIntegration;
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

import static com.mcmoddev.lib.integration.plugins.ConstructsArmory.*;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeConstructsArmory.PLUGIN_MODID, versions = BMeConstructsArmory.PLUGIN_MODID
        + "@[1.12.2-2.10.1.87,)")
public final class BMeConstructsArmory implements IIntegration {

    public static final String PLUGIN_MODID = ConstructsArmory.PLUGIN_MODID;

    public BMeConstructsArmory() {
        // do nothing
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
            ev.getRegistry().getEntries().stream()
                    .map(ent -> ent.getValue())
                    .forEach(mat -> {
                        TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getCoreStats(), mat.getPlatesStats(), mat.getTrimStats());
                        switch (mat.getName()){
                            case MaterialNames.ADAMANTINE:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.vengeful, ArmorTraits.prideful);
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
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.malleable);
                                break;
                            case MaterialNames.MITHRIL:
                                addArmorTrait(mat.getTinkerMaterial(), ArmorTraits.blessed);
                                break;
                            case MaterialNames.PEWTER:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraitsCA.malleable);
                                break;
                            case MaterialNames.STARSTEEL:
                                addArmorTrait(mat.getTinkerMaterial(), MMDTraits.sparkly, ArmorTraits.enderport);
                                break;
                        }
                    });
        }
    }
}
