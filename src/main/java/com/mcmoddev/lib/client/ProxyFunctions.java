package com.mcmoddev.lib.client;

import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ProxyFunctions {

	/**
	 *
	 * @return IRenderFactory
	 */
	@SuppressWarnings("rawtypes")
	public static net.minecraftforge.fml.client.registry.IRenderFactory<?> entityVillagerRenderer() {
		return (IRenderFactory) RenderVillager::new;
	}
}
