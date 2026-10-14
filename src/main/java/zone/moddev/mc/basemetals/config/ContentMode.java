package zone.moddev.mc.basemetals.config;

import java.util.Locale;

/** Names shared by the config file, config screen and login check. */
public enum ContentMode {
    HIGH_FANTASY("high_fantasy"),
    LOW_FANTASY("low_fantasy");

    private final String name;

    ContentMode(String name) {
        this.name = name;
    }

    public String serializedName() {
        return name;
    }

    public String translationKey() {
        return "config.basemetals.content_mode." + name;
    }

    public static ContentMode parse(String value) {
        if (value != null) {
            String normalized = value.trim().toLowerCase(Locale.ROOT);
            for (ContentMode mode : values()) {
                if (mode.name.equals(normalized)) return mode;
            }
        }

        return HIGH_FANTASY;
    }

    public static boolean isValid(String value) {
        return value != null && parse(value).name.equals(value.trim().toLowerCase(Locale.ROOT));
    }
}
