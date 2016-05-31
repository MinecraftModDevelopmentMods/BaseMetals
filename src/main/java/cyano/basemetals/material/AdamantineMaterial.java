package cyano.basemetals.material;

public class AdamantineMaterial extends MetalMaterial {

	public AdamantineMaterial(String name, float hardness, float strength,
			float magic, float rarity) {
		super(name, hardness, strength, magic,(int)Math.max(100*rarity, 1));
	}

	public float getBlastResistance() {
		return 2000;
	}
}
