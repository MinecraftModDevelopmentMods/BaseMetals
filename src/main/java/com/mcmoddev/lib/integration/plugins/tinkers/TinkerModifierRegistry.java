package com.mcmoddev.lib.integration.plugins.tinkers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import net.minecraft.util.ResourceLocation;
import slimeknights.tconstruct.library.modifiers.Modifier;

public class TinkerModifierRegistry {
	private static final Map<ResourceLocation, Modifier> modifiersRegistry = new ConcurrentHashMap<>(256);

	public TinkerModifierRegistry() {
		// do nothing
	}
	
	public void register(ResourceLocation resourceLocation, Modifier modifier) {
		modifiersRegistry.put(resourceLocation, modifier);
	}
	
	@Nullable
	public Modifier get(ResourceLocation resourceLocation) {
		return modifiersRegistry.getOrDefault(resourceLocation, null);
	}

	public List<Modifier> getModifiers() {
		return Collections.unmodifiableList(new ArrayList<>(modifiersRegistry.values()));
	}

}
