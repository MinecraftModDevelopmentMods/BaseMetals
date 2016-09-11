package cyano.basemetals.material;

/**
 * 
 * @author Jasmine Iwanek
 *
 */
public class AdamantineMaterial extends MetalMaterial {

	public AdamantineMaterial(String name, float hardness, float strength,
			float magic) {
		super(name, hardness, strength, magic);
	}

	@Override
	public float getBlastResistance() {
		return 2000;
	}
}
