package com.mcmoddev.lib.integration;

import com.google.common.collect.Lists;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.material.MMDMaterial;

import java.util.List;

public interface IIntegrationEvent {
	default List<MMDMaterial> materials() {
		return Lists.newLinkedList(Materials.getAllMaterials());
	}
}
