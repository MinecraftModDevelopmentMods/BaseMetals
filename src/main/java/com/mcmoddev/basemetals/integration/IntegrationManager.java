package com.mcmoddev.basemetals.integration;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.util.AnnotationChecker;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

public enum IntegrationManager {
    INSTANCE;

    private List<IIntegration> integrations = Lists.newArrayList();

    public void preInit(FMLPreInitializationEvent event) {
    	for( final ASMData asmDataItem : event.getAsmData().getAll(BaseMetalsPlugin.class.getCanonicalName()) ) {
    		String modId = asmDataItem.getAnnotationInfo().get("value").toString();
    		String className = asmDataItem.getClassName();
    		if (Loader.isModLoaded(modId)) {
    			IIntegration integration;
				try {
					integration = Class.forName(className).asSubclass(IIntegration.class).newInstance();
					integrations.add(integration);
					integration.init();
				} catch (final Exception e) {
					// do nothing 
				}
    		}
    	}
//    	throw new Error("Take off and nuke it from orbit, it's the only way to be sure");
/*        AnnotationChecker.getInstances(event.getAsmData(), BaseMetalsPlugin.class, IIntegration.class).stream()
                .forEachOrdered(integration -> {
                    Class<? extends IIntegration> integrationClass = integration.getClass();
                    BaseMetalsPlugin plugin = integrationClass.getAnnotation(BaseMetalsPlugin.class);
                    if (Loader.isModLoaded(plugin.value())) {
                    	BaseMetals.logger.debug("Loaded " + plugin.value());
//                    	FMLLog.severe("BASEMETALS: Loaded " + plugin.value());
                        integrations.add(integration);
                        integration.init();
                    }
                });*/
    }
}