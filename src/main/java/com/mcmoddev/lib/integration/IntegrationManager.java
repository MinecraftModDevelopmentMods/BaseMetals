package com.mcmoddev.lib.integration;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.List;

public enum IntegrationManager {
	INSTANCE;

	private List<IIntegration> integrations = Lists.newArrayList();

	public void preInit(FMLPreInitializationEvent event) {
		for (final ASMData asmDataItem : event.getAsmData().getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = asmDataItem.getAnnotationInfo().get("addonId").toString();
			final String pluginId = asmDataItem.getAnnotationInfo().get("pluginId").toString();
			final String clazz = asmDataItem.getClassName();

			if ((event.getModMetadata().modId.equals(addonId)) && (Loader.isModLoaded(pluginId))) {
//			if ((Loader.instance().activeModContainer().getModId().equals(addonId)) && (Loader.isModLoaded(pluginId))) {
				IIntegration integration;
				try {
					integration = Class.forName(clazz).asSubclass(IIntegration.class).newInstance();
					integrations.add(integration);
					integration.init();
					BaseMetals.logger.debug("Loaded " + pluginId + " for " + addonId);
				} catch (final Exception ex) {
					BaseMetals.logger.error("Couldn't load "+ pluginId + " for " + addonId, ex);
				}
			}
		}
	}
}
