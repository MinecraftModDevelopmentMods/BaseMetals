package com.mcmoddev.lib.integration;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum IntegrationManager {
	INSTANCE;

	private List<IIntegration> integrations = Lists.newArrayList();
	private Map<String,List<Map<IIntegration,Method>>> callbacks = new HashMap<>();
	private static final String preInit = "preInitCallback";
	private static final String init = "initCallback";
	private static final String postInit = "postInitCallback";
	
	public void preInit(FMLPreInitializationEvent event) {
		for (final ASMData asmDataItem : event.getAsmData().getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = asmDataItem.getAnnotationInfo().get("addonId").toString();
			final String pluginId = asmDataItem.getAnnotationInfo().get("pluginId").toString();
			
			String[] cbs = new String[3];
			if( asmDataItem.getAnnotationInfo().get(preInit) != null ) {
				cbs[0] =  asmDataItem.getAnnotationInfo().get(preInit).toString();
			} else {
				cbs[0] =  "init";
			}
			if( asmDataItem.getAnnotationInfo().get(init) != null ) {
				cbs[1] =  asmDataItem.getAnnotationInfo().get(init).toString();
			} else {
				cbs[1] =  "";
			}
			if( asmDataItem.getAnnotationInfo().get(postInit) != null ) {
				cbs[2] =  asmDataItem.getAnnotationInfo().get(postInit).toString();
			} else {
				cbs[2] =  "";
			}
			
			final String preInitCallback = cbs[0];
			final String initCallback = cbs[1];
			final String postInitCallback = cbs[2];
			final String clazz = asmDataItem.getClassName();
			
			if ((event.getModMetadata().modId.equals(addonId)) && (Loader.isModLoaded(pluginId))) {
//			if ((Loader.instance().activeModContainer().getModId().equals(addonId)) && (Loader.isModLoaded(pluginId))) {
				IIntegration integration;
				try {
					integration = Class.forName(clazz).asSubclass(IIntegration.class).newInstance();
					integrations.add(integration);
					integration.init();
					
					BaseMetals.logger.debug("Loaded " + pluginId + " for " + addonId);
					if( !("".equals(preInitCallback)) ) {
						setCallback(integration,preInitCallback,"preInit");
					}
					if( !("".equals(initCallback)) ) {
						setCallback(integration,initCallback,"init");
					}
					if( !("".equals(postInitCallback)) ) {
						setCallback(integration,postInitCallback, "postInit");
					}
				} catch (final Exception ex) {
					BaseMetals.logger.error("Couldn't load "+ pluginId + " for " + addonId, ex);
				}
			}
		}
	}

	private void setCallback(IIntegration i, String name, String phase) {
		List<Map<IIntegration,Method>> k;
		if( callbacks.containsKey(phase) ) {
			k = callbacks.get(phase);
		} else {
			k = Lists.newArrayList();
		}
		Map<IIntegration,Method> mmm = new HashMap<>();
		try {
			mmm.put(i,i.getClass().getMethod(name));
			k.add(mmm);
			callbacks.put(phase, k);
		} catch( final Exception e ) {
			// do nothing
		}
	}

	private void runCallback(Map<IIntegration,Method> cbs) {
		for( Entry<IIntegration,Method> ent : cbs.entrySet() ) {
			try {
				ent.getValue().invoke(ent.getKey());
			} catch( final Exception e) {
				// do nothing here
			}
		}
	}
	
	public void runCallbacks(String phase) {
		if( callbacks.containsKey(phase) ) {
			List<Map<IIntegration,Method>> cbs = callbacks.get(phase);
			for( Map<IIntegration,Method> map : cbs ) {
				runCallback(map);
			}
		}
	}
}
