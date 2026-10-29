package com.iekei.client.gui;

/**
 * Accent theming. Every colour used by the ClickGUI is derived from
 * {@link #accent()}, exactly like the {@code --accent} CSS variable in the
 * web mockup.
 */
public final class Theme {
	private Theme() {
	}

	/** Preset accents (ARGB). */
	public static final int[] PRESETS = {
			0xFFA855F7, // Neon Purple
			0xFFFF3B5C, // Cyber Red
			0xFF3B82F6, // Electric Blue
			0xFF10D98A, // Emerald
			0xFFF5A524 // Amber Gold
	};

	public static final String[] PRESET_NAMES = {
			"Purple", "Red", "Blue", "Emerald", "Amber"
	};

	private static int accent = PRESETS[0];
	private static float glow = 0.7F;

	public static int accent() {
		return accent;
	}

	public static void setAccent(int argb) {
		accent = 0xFF000000 | (argb & 0x00FFFFFF);
	}

	public static void setPreset(int index) {
		if (index >= 0 && index < PRESETS.length) {
			setAccent(PRESETS[index]);
		}
	}

	public static int presetIndex() {
		for (int i = 0; i < PRESETS.length; i++) {
			if ((PRESETS[i] & 0x00FFFFFF) == (accent & 0x00FFFFFF)) {
				return i;
			}
		}
		return -1;
	}

	public static float glow() {
		return glow;
	}

	public static void setGlow(float value) {
		glow = Math.max(0.0F, Math.min(1.0F, value));
	}

	/** Accent with a custom alpha (0-255). */
	public static int alpha(int alpha) {
		return (alpha & 0xFF) << 24 | (accent & 0x00FFFFFF);
	}

	/** Blend the accent towards black (used for panel tints). */
	public static int shade(float factor, int alpha) {
		int r = (int) (((accent >> 16) & 0xFF) * factor);
		int g = (int) (((accent >> 8) & 0xFF) * factor);
		int b = (int) ((accent & 0xFF) * factor);
		return (alpha & 0xFF) << 24 | r << 16 | g << 8 | b;
	}

	/** Blend two ARGB colours. */
	public static int mix(int from, int to, float t) {
		t = Math.max(0.0F, Math.min(1.0F, t));
		int a = (int) (((from >>> 24) & 0xFF) * (1 - t) + ((to >>> 24) & 0xFF) * t);
		int r = (int) (((from >> 16) & 0xFF) * (1 - t) + ((to >> 16) & 0xFF) * t);
		int g = (int) (((from >> 8) & 0xFF) * (1 - t) + ((to >> 8) & 0xFF) * t);
		int b = (int) ((from & 0xFF) * (1 - t) + (to & 0xFF) * t);
		return a << 24 | r << 16 | g << 8 | b;
	}

	/* ---------- neutral palette (matches the web mockup) ---------- */
	public static final int TEXT = 0xFFEEF0F7;
	public static final int MUTED = 0xFF9BA1B6;
	public static final int DIM = 0xFF6C7288;
	public static final int PANEL = 0xE00A0A0F;
	public static final int CARD = 0x24FFFFFF;
	public static final int CARD_HOVER = 0x38FFFFFF;
	public static final int STROKE = 0x22FFFFFF;
	public static final int STROKE_STRONG = 0x3DFFFFFF;
	public static final int SHADOW = 0x99000000;
	public static final int ON = 0xFF3DDC97;
}
