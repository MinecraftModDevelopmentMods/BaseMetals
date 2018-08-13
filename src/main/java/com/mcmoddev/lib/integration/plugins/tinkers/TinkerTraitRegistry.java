package com.mcmoddev.lib.integration.plugins.tinkers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nullable;

import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.traits.ITrait;

public class TinkerTraitRegistry {
	private static final Map<String, ITrait> traitsRegistry = new ConcurrentHashMap<>(256);

	public TinkerTraitRegistry() {
		// do nothing
	}
	
	public void register(String traitName, ITrait trait) {
		traitsRegistry.put(traitName, trait);
	}
	
	@Nullable
	public ITrait get(String traitName) {
		ITrait pos = TinkerRegistry.getTrait(traitName);
		if(traitsRegistry.containsKey(traitName)) {
			return traitsRegistry.get(traitName);
		} else if(pos != null) {
			return pos;
		} else {
			Field f;
			try {
				f = slimeknights.tconstruct.tools.TinkerTraits.class.getDeclaredField(traitName);
				if(f != null) {
					return (ITrait) f.get(null);
				}
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				return null;
			}
		}
		return null;
	}

	public List<ITrait> getTraits() {
		return Collections.unmodifiableList(new ArrayList<>(traitsRegistry.values()));
	}
}
