package cyano.basemetals.init;

import cyano.basemetals.material.MetalMaterial;
import net.minecraftforge.fluids.Fluid;
import slimeknights.tconstruct.library.materials.Material;

class MaterialCorrelation {
	private Material material;
	private MetalMaterial metal;
	private Fluid meltFluid;
	
	public MaterialCorrelation(Material material, MetalMaterial metalByName, Fluid meltFluid) {
		this.setMaterial(material);
		this.setMetal(metalByName);
		this.setMeltFluid(meltFluid);
	}

	/**
	 * @return the material
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * @param material the material to set
	 */
	public void setMaterial(Material material) {
		this.material = material;
	}

	/**
	 * @return the metal
	 */
	public MetalMaterial getMetal() {
		return metal;
	}

	/**
	 * @param metal the metal to set
	 */
	public void setMetal(MetalMaterial metal) {
		this.metal = metal;
	}

	/**
	 * @return the meltFluid
	 */
	public Fluid getMeltFluid() {
		return meltFluid;
	}

	/**
	 * @param meltFluid the meltFluid to set
	 */
	public void setMeltFluid(Fluid meltFluid) {
		this.meltFluid = meltFluid;
	}
	
}
