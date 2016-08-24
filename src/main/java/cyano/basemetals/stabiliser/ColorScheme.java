package cyano.basemetals.stabiliser;

/**
 * Used for compat with stabiliser
 *
 * @author TheCodedOne
 */
public class ColorScheme {

	private final int primary;
	private final int secondary;

	private ColorScheme(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}

	public static ColorScheme create(int primary, int secondary) {
		return new ColorScheme(primary, secondary);
	}

	public int getPrimary() {
		return primary;
	}

	public int getSecondary() {
		return secondary;
	}
}
