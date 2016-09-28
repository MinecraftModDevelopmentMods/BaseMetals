package cyano.basemetals.material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class StarSteelMaterial extends MetalMaterial {

	/**
	 *
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 */
	public StarSteelMaterial(String name, float hardness, float strength, float magic) {
		super(name, hardness, strength, magic);
	}

	@Override
	public float getBlastResistance() {
		return 2000;
	}
}
