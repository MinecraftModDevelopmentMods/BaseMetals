package review;

/** Compiled against the old jar, then loaded unchanged by the new candidate. */
public final class LegacyMmdConsumer extends com.mcmoddev.basemetals.proxy.CommonProxy {

    public static Object material() {
        return com.mcmoddev.basemetals.init.Materials.getMaterialByName("copper");
    }

    public static Object instance() {
        return com.mcmoddev.basemetals.BaseMetals.instance;
    }

    public static Class<?> literal() {
        return com.mcmoddev.basemetals.BaseMetals.class;
    }

    public static Class<?> reflected() throws ClassNotFoundException {
        return Class.forName("com.mcmoddev.basemetals.BaseMetals");
    }
}
