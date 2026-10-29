package com.iekei.client.gui;

import com.iekei.client.gui.Icons.Icon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * Tiny immediate-mode drawing helpers on top of {@link GuiGraphicsExtractor}:
 * rounded rectangles, circles, translucent gradients, clipped regions,
 * strings and line-art icons.
 */
public final class Draw {
	private Draw() {
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	/* ---------------- primitives ---------------- */

	public static void fill(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int color) {
		if (x2 <= x1 || y2 <= y1) {
			return;
		}

		g.fill(x1, y1, x2, y2, color);
	}

	public static void rect(GuiGraphicsExtractor g, int x, int y, int w, int h, int color) {
		fill(g, x, y, x + w, y + h, color);
	}

	public static void hLine(GuiGraphicsExtractor g, int x, int y, int w, int color) {
		fill(g, x, y, x + w, y + 1, color);
	}

	public static void vLine(GuiGraphicsExtractor g, int x, int y, int h, int color) {
		fill(g, x, y, x + 1, y + h, color);
	}

	/** Filled rectangle with rounded corners. */
	public static void roundRect(GuiGraphicsExtractor g, int x, int y, int w, int h, int radius, int color) {
		if (w <= 0 || h <= 0) {
			return;
		}

		int r = Math.min(radius, Math.min(w, h) / 2);

		if (r <= 0) {
			rect(g, x, y, w, h, color);
			return;
		}

		// middle block + top/bottom caps
		rect(g, x, y + r, w, h - 2 * r, color);

		for (int i = 0; i < r; i++) {
			int dy = r - i - 1;
			int inset = r - (int) Math.round(Math.sqrt((double) (r * r - dy * dy)));
			rect(g, x + inset, y + i, w - inset * 2, 1, color);
			rect(g, x + inset, y + h - i - 1, w - inset * 2, 1, color);
		}
	}

	/** Rounded rectangle outline (drawn as a slightly smaller inner cut-out). */
	public static void roundOutline(GuiGraphicsExtractor g, int x, int y, int w, int h, int radius,
			int fillColor, int borderColor) {
		roundRect(g, x, y, w, h, radius, borderColor);
		roundRect(g, x + 1, y + 1, w - 2, h - 2, Math.max(0, radius - 1), fillColor);
	}

	/** Approximate vertical gradient (a handful of bands is plenty at GUI scale). */
	public static void gradient(GuiGraphicsExtractor g, int x, int y, int w, int h, int top, int bottom, int bands) {
		int n = Math.max(1, bands);

		for (int i = 0; i < n; i++) {
			float t = (i + 0.5F) / n;
			int color = Theme.mix(top, bottom, t);
			int y1 = y + h * i / n;
			int y2 = y + h * (i + 1) / n;
			rect(g, x, y1, w, Math.max(1, y2 - y1), color);
		}
	}

	/** Soft drop shadow / glow behind a panel. */
	public static void shadow(GuiGraphicsExtractor g, int x, int y, int w, int h, int radius, int spread, int color) {
		for (int i = spread; i > 0; i--) {
			int a = (color >>> 24) & 0xFF;
			int faded = (color & 0x00FFFFFF) | ((a * i / (spread * 2)) << 24);
			roundRect(g, x - i, y - i, w + i * 2, h + i * 2, radius + i, faded);
		}
	}

	public static void circle(GuiGraphicsExtractor g, int cx, int cy, int r, int color) {
		if (r <= 0) {
			return;
		}

		int x = r;
		int y = 0;
		int err = 1 - x;

		while (x >= y) {
			rect(g, cx - x, cy + y, 2 * x + 1, 1, color);
			rect(g, cx - x, cy - y, 2 * x + 1, 1, color);
			rect(g, cx - y, cy + x, 2 * y + 1, 1, color);
			rect(g, cx - y, cy - x, 2 * y + 1, 1, color);
			y++;

			if (err < 0) {
				err += 2 * y + 1;
			} else {
				x--;
				err += 2 * (y - x) + 1;
			}
		}
	}

	public static void filledCircle(GuiGraphicsExtractor g, int cx, int cy, int r, int color) {
		for (int dy = -r; dy <= r; dy++) {
			int span = (int) Math.sqrt(Math.max(0, r * r - dy * dy));
			rect(g, cx - span, cy + dy, span * 2 + 1, 1, color);
		}
	}

	/** 1px line via Bresenham. */
	public static void line(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2, int color) {
		int dx = Math.abs(x2 - x1);
		int dy = -Math.abs(y2 - y1);
		int sx = x1 < x2 ? 1 : -1;
		int sy = y1 < y2 ? 1 : -1;
		int err = dx + dy;

		while (true) {
			fill(g, x1, y1, x1 + 1, y1 + 1, color);

			if (x1 == x2 && y1 == y2) {
				return;
			}

			int e2 = 2 * err;

			if (e2 >= dy) {
				err += dy;
				x1 += sx;
			}

			if (e2 <= dx) {
				err += dx;
				y1 += sy;
			}
		}
	}

	/* ---------------- text ---------------- */

	public static int width(String text) {
		return font().width(Component.literal(text));
	}

	public static void text(GuiGraphicsExtractor g, String text, int x, int y, int color) {
		text(g, text, x, y, color, true);
	}

	public static void text(GuiGraphicsExtractor g, String text, int x, int y, int color, boolean shadow) {
		if (text == null || text.isEmpty()) {
			return;
		}

		g.text(font(), text, x, y, color, shadow);
	}

	public static void textRight(GuiGraphicsExtractor g, String text, int rightX, int y, int color) {
		text(g, text, rightX - width(text), y, color);
	}

	public static void textCenter(GuiGraphicsExtractor g, String text, int centerX, int y, int color) {
		text(g, text, centerX - width(text) / 2, y, color);
	}

	/** Text vertically centred in a box. */
	public static void textCenterY(GuiGraphicsExtractor g, String text, int x, int y, int h, int color) {
		text(g, text, x, y + (h - font().lineHeight) / 2 + 1, color);
	}

	public static void textTruncated(GuiGraphicsExtractor g, String text, int x, int y, int maxWidth, int color) {
		if (width(text) <= maxWidth) {
			text(g, text, x, y, color);
			return;
		}

		String ellipsis = "…";
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < text.length(); i++) {
			if (width(sb.toString() + text.charAt(i) + ellipsis) > maxWidth) {
				break;
			}

			sb.append(text.charAt(i));
		}

		text(g, sb + ellipsis, x, y, color);
	}

	/* ---------------- clipping ---------------- */

	public static void pushClip(GuiGraphicsExtractor g, int x1, int y1, int x2, int y2) {
		g.enableScissor(x1, y1, x2, y2);
	}

	public static void popClip(GuiGraphicsExtractor g) {
		g.disableScissor();
	}

	/* ---------------- icons ---------------- */

	public static void icon(GuiGraphicsExtractor g, Icon icon, int x, int y, int size, int color) {
		Icons.draw(g, icon, x, y, size, color);
	}
}
