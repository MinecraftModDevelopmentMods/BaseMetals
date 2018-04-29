package com.mcmoddev.lib.events;

import com.mcmoddev.lib.data.ActiveModData;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MMDLibRegisterMaterials extends Event {
	public void setActive(String name) {
		ActiveModData.instance.setActive(name);
	}
}
