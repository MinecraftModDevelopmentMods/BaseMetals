package com.mcmoddev.lib.integration;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface that all events of the MMDLib Mod-Integration plugin system present.
 * 
 * @author D. Hazelton
 *
 */
public interface IIntegrationEvent {

	default List<MMDMaterial> materials() {
		return Collections.<MMDMaterial>unmodifiableList(
				Materials.getAllMaterials().stream().collect(Collectors.toList()));
	}
}
