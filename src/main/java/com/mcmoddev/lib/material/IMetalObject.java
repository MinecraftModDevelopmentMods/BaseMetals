package com.mcmoddev.lib.material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IMetalObject {

	/**
	 *
	 * @return The Material
	 */
	public MetalMaterial getMaterial();

	/**
	 * @deprecated
	 * @return The Material
	 */
	@Deprecated
	public MetalMaterial getMetalMaterial();
}
