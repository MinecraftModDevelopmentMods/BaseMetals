package com.mcmoddev.lib.integration;

import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mcmoddev.basemetals.BaseMetals;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion;
import net.minecraftforge.fml.common.versioning.InvalidVersionSpecificationException;
import net.minecraftforge.fml.common.versioning.VersionRange;

public enum IntegrationManager {

	INSTANCE;

	private final List<IIntegration> integrations = Lists.newArrayList();
	private final Map<String,Map<String, VersionMatch>> plugins = Maps.newConcurrentMap();
	
	private String getAnnotationItem(@Nonnull final String item, @Nonnull final ASMData asmData) {
		if (asmData.getAnnotationInfo().get(item) != null) {
			return asmData.getAnnotationInfo().get(item).toString();
		} else {
			return "";
		}
	}

	private interface VersionMatch {
		boolean matches(String otherVersion);
		default String asString() {
			return "any";
		};
	}
	
	/**
	 * Harvest the version requirements during the FMLConstructionEvent phase and set them up for use
	 * @param event FMLConstructionEvent
	 * @throws InvalidVersionSpecificationException
	 */
	public void setup(@Nonnull final FMLConstructionEvent event) throws InvalidVersionSpecificationException {
		for (final ASMData asmDataItem : event.getASMHarvestedData()
				.getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = this.getAnnotationItem("addonId", asmDataItem);
			final String modId = this.getAnnotationItem("pluginId", asmDataItem);
			final String versions = this.getAnnotationItem("versions", asmDataItem);
			if (!versions.equals("")) {
				this.plugins.computeIfAbsent(addonId, (val) -> Maps.newConcurrentMap());
				Map<String,VersionMatch> rv = this.plugins.get(addonId);
				for (String entry : versions.split(";")) {
					String[] bits = entry.split("@");
					String targetModId = bits[0];
					if (bits[1].matches("\\[[\\w\\d\\.\\+]+[\\]\\)]")) {
						rv.put(targetModId, new VersionMatch() {
							private final VersionRange myRange = VersionRange.createFromVersionSpec(bits[1]);
							public boolean matches(String otherVersion) {
								return myRange.containsVersion(new DefaultArtifactVersion(otherVersion));
							}
							public String asString() {
								return myRange.toStringFriendly();
							}
						});
					} else {
						rv.put(targetModId, (match) -> true);
					}
				}
			} else {
				this.plugins.computeIfAbsent(addonId, (val) -> Maps.newConcurrentMap());
				Map<String,VersionMatch> rv = this.plugins.get(addonId);
				rv.put(modId, (match) -> true);
			}
		}
	}
	
	/**
	 *
	 * @param event
	 */
	public void preInit(@Nonnull final FMLPreInitializationEvent event) {
		for (final ASMData asmDataItem : event.getAsmData().getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = this.getAnnotationItem("addonId", asmDataItem);
			final String pluginId = this.getAnnotationItem("pluginId", asmDataItem);
			final String clazz = asmDataItem.getClassName();

			if ((event.getModMetadata().modId.equals(addonId)) && (Loader.isModLoaded(pluginId))) {
				String pluginVersion = FMLCommonHandler.instance().findContainerFor(pluginId).getVersion();
				VersionMatch matcher = this.plugins.get(addonId).getOrDefault(pluginId, (match) -> true);
				if (!matcher.matches(pluginVersion)) {
					BaseMetals.logger.error("Version %s of mod %s is not valid for this mods (%s) integration with it - %s required", pluginVersion, pluginId, addonId, matcher.asString());
					break;
				}

				IIntegration integration;
				try {
					integration = Class.forName(clazz).asSubclass(IIntegration.class).newInstance();
					this.integrations.add(integration);

					integration.init();

					BaseMetals.logger.debug("Loaded " + pluginId + " for " + addonId);
				} catch (final Exception ex) {
					BaseMetals.logger.error("Couldn't load " + pluginId + " for " + addonId, ex);
				}
			}
		}
	}
	
	public void preInitPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationPreInitEvent());
	}
	
	public void initPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationInitEvent());
	}
	
	public void postInitPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationPostInitEvent());
	}

}
