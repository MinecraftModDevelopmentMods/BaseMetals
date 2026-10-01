package com.mcmoddev.basemetals.content;

import java.util.Locale;

/** Content modes, with names shared by the config file and network messages. */
public enum ContentMode {
	HIGH_FANTASY("high_fantasy"),
	LOW_FANTASY("low_fantasy");

	private final String serializedName;

	ContentMode(final String serializedName) {
		this.serializedName = serializedName;
	}

	public String serializedName() {
		return serializedName;
	}

	public String translationKey() {
		return "config.basemetals.content_mode." + serializedName;
	}

	public static ContentMode fromSerializedName(final String value) {
		if (value != null) {
			final String normalized = value.trim().toLowerCase(Locale.ROOT);

			for (final ContentMode mode : values()) {
				if (mode.serializedName.equals(normalized)) {
					return mode;
				}
			}
		}

		return HIGH_FANTASY;
	}

	public static boolean isValidSerializedName(final String value) {
		if (value == null) {
			return false;
		}

		for (final ContentMode mode : values()) {
			if (mode.serializedName.equals(value.trim().toLowerCase(Locale.ROOT))) {
				return true;
			}
		}

		return false;
	}

	public static String[] serializedNames() {
		final ContentMode[] modes = values();
		final String[] names = new String[modes.length];

		for (int i = 0; i < modes.length; i++) {
			names[i] = modes[i].serializedName;
		}

		return names;
	}

	public static String[] translationKeys() {
		final ContentMode[] modes = values();
		final String[] names = new String[modes.length];

		for (int i = 0; i < modes.length; i++) {
			names[i] = modes[i].translationKey();
		}

		return names;
	}
}
