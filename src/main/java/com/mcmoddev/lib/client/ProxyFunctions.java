package com.mcmoddev.lib.client;

import com.mcmoddev.lib.data.SharedStrings;

import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class ProxyFunctions {

	private ProxyFunctions() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	/**
	 *
	 * @return IRenderFactory
	 */
	@SuppressWarnings("rawtypes")
	public static net.minecraftforge.fml.client.registry.IRenderFactory<?> entityVillagerRenderer() {
		return (IRenderFactory) RenderVillager::new;
	}
}
