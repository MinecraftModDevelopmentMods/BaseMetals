package cyano.basemetals.stabiliser;

/**
 * Used for compat with stabiliser
 *
 * @author TheCodedOne
 */
public interface IStableInfo {

	String getModID();
	String getModVersion();
	String getModName();
	String getModClass();
	String getUpdateJSONURL();
	ColorScheme getColorScheme();

}
