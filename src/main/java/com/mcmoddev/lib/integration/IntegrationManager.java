package com.mcmoddev.lib.integration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public enum IntegrationManager {
	INSTANCE;

	private List<IIntegration> integrations = Lists.newArrayList();
	private Map<String,List<Map<IIntegration,Method>>> callbacks = new HashMap<>();

	private String getAnnotationItem( String item, final ASMData asmData ) {
		if( asmData.getAnnotationInfo().get(item) != null ) {
			return asmData.getAnnotationInfo().get(item).toString();
		} else {
			return "";
		}
	}
	
	public void preInit(FMLPreInitializationEvent event) {
		for (final ASMData asmDataItem : event.getAsmData().getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = getAnnotationItem("addonId", asmDataItem);
			final String pluginId = getAnnotationItem("pluginId", asmDataItem);
			final String preInitCallback = getAnnotationItem("preInitCallback", asmDataItem);
			final String initCallback = getAnnotationItem("initCallback", asmDataItem);
			final String postInitCallback = getAnnotationItem("postInitCallback", asmDataItem);
			final String clazz = asmDataItem.getClassName();
			
			if ((event.getModMetadata().modId.equals(addonId)) && (Loader.isModLoaded(pluginId))) {
				IIntegration integration;
				try {
					integration = Class.forName(clazz).asSubclass(IIntegration.class).newInstance();
					integrations.add(integration);
					integration.init();
					
					BaseMetals.logger.debug("Loaded " + pluginId + " for " + addonId);
					setCallback(integration,preInitCallback,"preInit");
					setCallback(integration,initCallback,"init");
					setCallback(integration,postInitCallback, "postInit");
				} catch (final Exception ex) {
					BaseMetals.logger.error("Couldn't load "+ pluginId + " for " + addonId, ex);
				}
			}
		}
	}

	private void setCallback(IIntegration i, String name, String phase) {
		if( "".equals(name) ) 
			return;
		
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
			BaseMetals.logger.debug("Exception adding callback %s for loading phase %s\nException: %s", name, phase, e.getMessage());
		}
	}

	private void runCallback(Map<IIntegration,Method> cbs) {
		for( Entry<IIntegration,Method> ent : cbs.entrySet() ) {
			try {
				ent.getValue().invoke(ent.getKey());
			} catch( final Exception e) {
				BaseMetals.logger.debug("Exception running callback: %s", e.getMessage());
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
