package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.MMDPlugin;
import com.mcmoddev.lib.integration.plugins.ConstructsArmory;
import com.mcmoddev.lib.integration.plugins.tinkers.events.MaterialRegistrationEvent;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;

@MMDPlugin(addonId = BaseMetals.MODID, pluginId = BMeConstructsArmory.PLUGIN_MODID, versions = BMeConstructsArmory.PLUGIN_MODID
        + "@[1.12.2-2.10.1.87,)")
public final class BMeConstructsArmory extends BMeTinkersConstruct {

    public static final String PLUGIN_MODID = ConstructsArmory.PLUGIN_MODID;

    public BMeConstructsArmory() {
        // do nothing
    }

    @Override
    public void init() {
        ConstructsArmory.INSTANCE.init();
        if (!Config.Options.isModEnabled(PLUGIN_MODID)) {
            return;
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void materialRegistration(MaterialRegistrationEvent ev) {
        ev.getRegistry().getEntries().stream()
                .map(ent -> ent.getValue())
                .forEach(mat -> TinkerRegistry.addMaterialStats(mat.getTinkerMaterial(), mat.getCoreStats(), mat.getPlatesStats(), mat.getTrimStats()));
    }
}
