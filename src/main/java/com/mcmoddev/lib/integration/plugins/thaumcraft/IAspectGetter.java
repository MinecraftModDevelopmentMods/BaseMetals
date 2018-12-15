package com.mcmoddev.lib.integration.plugins.thaumcraft;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.mcmoddev.lib.data.NameToken;

import thaumcraft.api.aspects.Aspect;

public interface IAspectGetter {
	public static final NameToken MATERIAL_WIDE = new NameToken("whole-material");

	IAspectCalculation getCalcFor(Aspect aspect);
	List<Pair<Aspect, IAspectCalculation>> getAspectForPart(NameToken part);
}
