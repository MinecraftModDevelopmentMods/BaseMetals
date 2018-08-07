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

/**
 * <p>
 * Core handling of the loading of {@link MMDPlugin} based plugins and the firing of
 * {@link IIntegrationEvent} based events.
 * <p>
 * This code can be very complex and hard to understand. Don't worry, it won't be on any tests!
 *
 * @author D. Hazelton
 * @author J. Iwanek
 *
 */
public enum IntegrationManager {
	/**
	 * As an enum this code is functionally static, but this small "hack" gives us a handle for
	 * accessing everything and allows it to not, actually, be fully static.
	 */
	INSTANCE;

	/**
	 * Stores a list of found and valid-to-load integrations.
	 */
	private final List<IIntegration> integrations = Lists.newArrayList();

	/**
	 * Maps the plugin names to the versions of the mods they are for. Uses the ForgeModLoader
	 * VersionMatch system.
	 */
	private final Map<String, Map<String, VersionMatch>> plugins = Maps.newConcurrentMap();

	/**
	 * Find and load a single item from the
	 * {@link net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData ASMData} provided by FML.
	 * 
	 * @param item
	 *            The item of data to search for.
	 * @param asmData
	 *            The ASMData to search through
	 * @return The value of the data, as a String, or the Empty String ("")
	 */
	private String getAnnotationItem(@Nonnull final String item, @Nonnull final ASMData asmData) {
		if (asmData.getAnnotationInfo().get(item) != null) {
			return asmData.getAnnotationInfo().get(item).toString();
		} else {
			return "";
		}
	}

	/**
	 * Private interface to help with integrating with the FML version matching system.
	 * 
	 * @author D. Hazelton
	 *
	 */
	private interface VersionMatch {

		boolean matches(String otherVersion);

		default String asString() {
			return "any";
		}
	}

	/**
	 * <p>
	 * Harvest the version requirements during the FMLConstructionEvent phase and set them up for
	 * use.
	 * <p>
	 * This starts by making sure that the version+modid matches a loaded mod or, if a version spec
	 * is not provided, that the modid matches a loaded mod.
	 * <p>
	 * If everything matches up, the plugin is added to the plugins list for a later run through the
	 * event system.
	 * 
	 * @param event
	 *            FMLConstructionEvent that is where this routine was actually called from and
	 *            provides the ASMData needed for all the work being done.
	 * @throws InvalidVersionSpecificationException
	 *             Thrown if the version specification is not a valid Maven Artifact Version.
	 */
	public void setup(@Nonnull final FMLConstructionEvent event)
			throws InvalidVersionSpecificationException {
		for (final ASMData asmDataItem : event.getASMHarvestedData()
				.getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = this.getAnnotationItem("addonId", asmDataItem);
			final String modId = this.getAnnotationItem("pluginId", asmDataItem);
			final String versions = this.getAnnotationItem("versions", asmDataItem);
			if (!versions.equals("")) {
				this.plugins.computeIfAbsent(addonId, val -> Maps.newConcurrentMap());
				Map<String, VersionMatch> rv = this.plugins.get(addonId);
				for (String entry : versions.split(";")) {
					String[] bits = entry.split("@");
					String targetModId = bits[0];
					if (bits[1].matches("[\\[\\(]?[\\w\\d\\.\\+,]+[\\]\\)]?")) {
						rv.put(targetModId, new VersionMatch() {

							private final VersionRange myRange = VersionRange
									.createFromVersionSpec(bits[1]);

							public boolean matches(final String otherVersion) {
								return myRange
										.containsVersion(new DefaultArtifactVersion(otherVersion));
							}

							@Override
							public String asString() {
								return myRange.toStringFriendly();
							}
						});
						BaseMetals.logger.fatal("versions: %s - %s!!%s - %s", entry, bits[0],
								bits[1], rv);
					} else {
						rv.put(targetModId, match -> true);
					}
					this.plugins.put(addonId, rv);
				}
			} else {
				this.plugins.computeIfAbsent(addonId, val -> Maps.newConcurrentMap());
				Map<String, VersionMatch> rv = this.plugins.get(addonId);
				rv.put(modId, match -> true);
				this.plugins.put(addonId, rv);
			}
		}
	}

	/**
	 * <p>
	 * During this event the valid plugins, listed in the plugins map, are initialized and have
	 * references to them stored in the integrations list.
	 *
	 * @param event
	 *            This is the FMLPreInitializationEvent that was happening when this method was
	 *            called.
	 */
	public void preInit(@Nonnull final FMLPreInitializationEvent event) {
		for (final ASMData asmDataItem : event.getAsmData()
				.getAll(MMDPlugin.class.getCanonicalName())) {
			final String addonId = this.getAnnotationItem("addonId", asmDataItem);
			final String pluginId = this.getAnnotationItem("pluginId", asmDataItem);
			final String clazz = asmDataItem.getClassName();

			if (Loader.isModLoaded(pluginId)) {
				String pluginVersion = FMLCommonHandler.instance().findContainerFor(pluginId)
						.getVersion();
				VersionMatch matcher = this.plugins.get(addonId).getOrDefault(pluginId,
						match -> true);

				if (!matcher.matches(pluginVersion)) {
					BaseMetals.logger.error(
							"Version %s of mod %s is not valid for this mods (%s) integration with it - %s required",
							pluginVersion, pluginId, addonId, matcher.asString());
					continue;
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

	/**
	 * Set off the {@link IntegrationPreInitEvent}.
	 */
	public void preInitPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationPreInitEvent());
	}

	/**
	 * Set off the {@link IntegrationInitEvent}.
	 */
	public void initPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationInitEvent());
	}

	/**
	 * Set off the {@link IntegrationPostInitEvent}.
	 */
	public void postInitPhase() {
		MinecraftForge.EVENT_BUS.post(new IntegrationPostInitEvent());
	}
}
