package cyano.basemetals.material;

public class LeadMaterial extends MetalMaterial {

	public LeadMaterial(String name, float hardness, float strength, float magic) {
		super(name, hardness, strength, magic);
	}

	@Override
	public float getBaseAttackDamage() {
		return 4;
	}
}
