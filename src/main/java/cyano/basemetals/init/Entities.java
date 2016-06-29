package cyano.basemetals.init;

public abstract class Entities {

	private static boolean initDone = false;
	public static void init(){
		if(initDone) return;

		initDone = true;
	}
}
