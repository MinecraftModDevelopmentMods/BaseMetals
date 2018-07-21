package com.mcmoddev.lib.events;

import com.mcmoddev.lib.data.ActiveModData;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MMDLibRegisterItems extends Event {
	public void setActive(final String name) {
		ActiveModData.instance.setActive(name);
	}
}
