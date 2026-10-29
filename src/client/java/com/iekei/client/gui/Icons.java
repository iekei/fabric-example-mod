package com.iekei.client.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Minimal 12x12 line-art icon set, drawn with 1px strokes so that the accent
 * colour only ever touches the outline (the in-game counterpart of the SVG
 * icon set used by the web mockup).
 */
public final class Icons {
	private Icons() {
	}

	public enum Icon {
		FOOTPRINTS,
		SWORDS,
		WRENCH,
		EYE,
		PALETTE,
		HOME,
		BELL,
		GEAR,
		SEARCH,
		SPARKLES,
		CLOSE,
		CHECK,
		CHEVRON,
		POWER
	}

	/**
	 * Draws an icon inside a {@code size x size} box.
	 *
	 * @param x left edge
	 * @param y top edge
	 * @param size box size in GUI pixels (12 works best)
	 */
	public static void draw(GuiGraphicsExtractor g, Icon icon, int x, int y, int size, int color) {
		float s = size / 12.0F;

		switch (icon) {
			case FOOTPRINTS -> footprints(g, x, y, s, color);
			case SWORDS -> swords(g, x, y, s, color);
			case WRENCH -> wrench(g, x, y, s, color);
			case EYE -> eye(g, x, y, s, color);
			case PALETTE -> palette(g, x, y, s, color);
			case HOME -> home(g, x, y, s, color);
			case BELL -> bell(g, x, y, s, color);
			case GEAR -> gear(g, x, y, s, color);
			case SEARCH -> search(g, x, y, s, color);
			case SPARKLES -> sparkles(g, x, y, s, color);
			case CLOSE -> close(g, x, y, s, color);
			case CHECK -> check(g, x, y, s, color);
			case CHEVRON -> chevron(g, x, y, s, color);
			case POWER -> power(g, x, y, s, color);
		}
	}

	/** Convert a logical 0-12 coordinate into a screen pixel. */
	private static int p(int v, float s) {
		return Math.round(v * s);
	}

	private static void pt(GuiGraphicsExtractor g, int x, int y, float s, int lx, int ly, int color) {
		g.fill(x + p(lx, s), y + p(ly, s), x + p(lx, s) + 1, y + p(ly, s) + 1, color);
	}

	private static void ln(GuiGraphicsExtractor g, int x, int y, float s, int x1, int y1, int x2, int y2, int color) {
		Draw.line(g, x + p(x1, s), y + p(y1, s), x + p(x2, s), y + p(y2, s), color);
	}

	private static void circle(GuiGraphicsExtractor g, int x, int y, float s, int cx, int cy, int r, int color) {
		Draw.circle(g, x + p(cx, s), y + p(cy, s), Math.max(1, p(r, s)), color);
	}

	/* ---------------- glyphs (12x12 logical grid) ---------------- */

	private static void footprints(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		// left foot
		ln(g, x, y, s, 2, 10, 2, 7, color);
		ln(g, x, y, s, 2, 7, 5, 7, color);
		ln(g, x, y, s, 5, 10, 5, 7, color);
		ln(g, x, y, s, 2, 10, 5, 10, color);
		pt(g, x, y, s, 3, 4, color);
		// right foot
		ln(g, x, y, s, 7, 5, 7, 2, color);
		ln(g, x, y, s, 7, 2, 10, 2, color);
		ln(g, x, y, s, 10, 5, 10, 2, color);
		ln(g, x, y, s, 7, 5, 10, 5, color);
		pt(g, x, y, s, 8, 9, color);
	}

	private static void swords(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 2, 10, 9, 3, color);
		ln(g, x, y, s, 9, 3, 10, 2, color);
		ln(g, x, y, s, 2, 10, 3, 9, color);
		ln(g, x, y, s, 1, 8, 4, 11, color);
		ln(g, x, y, s, 10, 10, 3, 3, color);
		ln(g, x, y, s, 8, 11, 11, 8, color);
		pt(g, x, y, s, 2, 3, color);
	}

	private static void wrench(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 2, 10, 7, 5, color);
		circle(g, x, y, s, 8, 4, 3, color);
		ln(g, x, y, s, 7, 4, 9, 4, color);
		pt(g, x, y, s, 2, 10, color);
	}

	private static void eye(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 1, 6, 3, 3, color);
		ln(g, x, y, s, 3, 3, 6, 2, color);
		ln(g, x, y, s, 6, 2, 9, 3, color);
		ln(g, x, y, s, 9, 3, 11, 6, color);
		ln(g, x, y, s, 11, 6, 9, 9, color);
		ln(g, x, y, s, 9, 9, 6, 10, color);
		ln(g, x, y, s, 6, 10, 3, 9, color);
		ln(g, x, y, s, 3, 9, 1, 6, color);
		Draw.filledCircle(g, x + p(6, s), y + p(6, s), Math.max(1, p(2, s)), color);
	}

	private static void palette(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		circle(g, x, y, s, 6, 6, 5, color);
		pt(g, x, y, s, 4, 4, color);
		pt(g, x, y, s, 8, 5, color);
		pt(g, x, y, s, 5, 8, color);
		pt(g, x, y, s, 9, 9, color);
	}

	private static void home(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 1, 6, 6, 1, color);
		ln(g, x, y, s, 6, 1, 11, 6, color);
		ln(g, x, y, s, 3, 6, 3, 11, color);
		ln(g, x, y, s, 9, 6, 9, 11, color);
		ln(g, x, y, s, 3, 11, 9, 11, color);
		ln(g, x, y, s, 5, 11, 5, 8, color);
		ln(g, x, y, s, 7, 11, 7, 8, color);
		ln(g, x, y, s, 5, 8, 7, 8, color);
	}

	private static void bell(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 4, 9, 4, 5, color);
		ln(g, x, y, s, 4, 5, 6, 2, color);
		ln(g, x, y, s, 6, 2, 8, 5, color);
		ln(g, x, y, s, 8, 5, 8, 9, color);
		ln(g, x, y, s, 2, 9, 10, 9, color);
		pt(g, x, y, s, 6, 11, color);
	}

	private static void gear(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		circle(g, x, y, s, 6, 6, 3, color);
		ln(g, x, y, s, 6, 1, 6, 3, color);
		ln(g, x, y, s, 6, 9, 6, 11, color);
		ln(g, x, y, s, 1, 6, 3, 6, color);
		ln(g, x, y, s, 9, 6, 11, 6, color);
		ln(g, x, y, s, 3, 3, 4, 4, color);
		ln(g, x, y, s, 8, 8, 9, 9, color);
		ln(g, x, y, s, 3, 9, 4, 8, color);
		ln(g, x, y, s, 8, 4, 9, 3, color);
	}

	private static void search(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		circle(g, x, y, s, 5, 5, 3, color);
		ln(g, x, y, s, 8, 8, 11, 11, color);
	}

	private static void sparkles(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 5, 1, 5, 6, color);
		ln(g, x, y, s, 3, 3, 7, 3, color);
		pt(g, x, y, s, 9, 2, color);
		pt(g, x, y, s, 9, 5, color);
		pt(g, x, y, s, 2, 9, color);
		pt(g, x, y, s, 5, 9, color);
		ln(g, x, y, s, 7, 7, 11, 11, color);
	}

	private static void close(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 3, 3, 9, 9, color);
		ln(g, x, y, s, 9, 3, 3, 9, color);
	}

	private static void check(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 2, 6, 5, 9, color);
		ln(g, x, y, s, 5, 9, 10, 3, color);
	}

	private static void chevron(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		ln(g, x, y, s, 3, 5, 6, 8, color);
		ln(g, x, y, s, 6, 8, 9, 5, color);
	}

	private static void power(GuiGraphicsExtractor g, int x, int y, float s, int color) {
		circle(g, x, y, s, 6, 7, 4, color);
		ln(g, x, y, s, 6, 1, 6, 5, color);
	}
}
