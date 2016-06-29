package cyano.basemetals.material;

public class StarSteelMaterial extends MetalMaterial {

	public StarSteelMaterial(String name, float hardness, float strength,
			float magic) {
		super(name, hardness, strength, magic);
	}

	public float getBlastResistance(){
		return 2000;
	}
}
