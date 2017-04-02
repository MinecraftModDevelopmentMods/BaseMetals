package com.mcmoddev.lib.material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public interface IMMDObject {

	/**
	 *
	 * @return The Material
	 */
	public MMDMaterial getMaterial();

	/**
	 * @deprecated
	 * @return The Material
	 */
	@Deprecated
	public MMDMaterial getMetalMaterial();
}
