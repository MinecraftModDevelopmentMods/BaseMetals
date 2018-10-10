package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.lib.integration.plugins.ConstructsArmory;
import com.mcmoddev.lib.integration.plugins.TinkersConstruct;
import com.mcmoddev.lib.util.Config;
import net.minecraftforge.common.MinecraftForge;

public class BMeConstructsArmory extends BMeTinkersConstruct {

    public static final String PLUGIN_MODID = TinkersConstruct.PLUGIN_MODID;

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
}
