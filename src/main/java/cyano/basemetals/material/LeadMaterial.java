package cyano.basemetals.material;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class LeadMaterial extends MetalMaterial {

	/**
	 *
	 * @param name
	 * @param hardness
	 * @param strength
	 * @param magic
	 */
	public LeadMaterial(String name, float hardness, float strength, float magic) {
		super(name, hardness, strength, magic);
	}

	@Override
	public float getBaseAttackDamage() {
		return 4;
	}
}
