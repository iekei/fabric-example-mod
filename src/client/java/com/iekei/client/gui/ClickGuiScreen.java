package com.iekei.client.gui;

import com.iekei.client.IekeiClientClient;
import com.iekei.client.gui.Icons.Icon;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.lwjgl.sdl.SDLScancode;

/**
 * Iekei Client - ClickGUI.
 *
 * <p>An in-game rendition of the web mockup: frosted dark glass panels, accent
 * themed line-art icons, a category rail, live module search, accordion module
 * cards and a theme customiser. The whole UI is drawn immediately each frame
 * with {@link GuiGraphicsExtractor}; the world stays visible behind it so the
 * translucent panels read as glass.
 *
 * <p>Toggle with [Right Shift] (see {@code IekeiClientClient}).
 */
public class ClickGuiScreen extends Screen {
	/* ---------------- layout constants ---------------- */
	private static final int SIDEBAR_W = 108;
	private static final int HEADER_H = 34;
	private static final int FOOTER_H = 16;
	private static final int CARD_HEADER_H = 26;
	private static final int SETTING_ROW_H = 18;
	private static final int GAP = 4;
	private static final int PAD = 6;
	private static final int CARD_RADIUS = 4;
	private static final int PANEL_RADIUS = 7;

	/* ---------------- colours ---------------- */
	private static final int TEXT = Theme.TEXT;
	private static final int MUTED = Theme.MUTED;
	private static final int DIM = Theme.DIM;
	private static final int CARD_BG = 0x26FFFFFF;
	private static final int CARD_BG_HOVER = 0x33FFFFFF;
	private static final int STROKE = 0x1FFFFFFF;
	private static final int STROKE_STRONG = 0x3AFFFFFF;

	/* ---------------- state ---------------- */
	private String search = "";
	private boolean searchFocused;
	private int scroll;
	private int contentHeight;
	private boolean themeOpen;
	private Setting dragging;
	private int dragTrackX;
	private int dragTrackW;
	private String toast = "";
	private long toastTime;
	private long lastFrameTime = System.currentTimeMillis();
	private float timeSeconds;

	/* rebuilt every frame so input handling always matches what is on screen */
	private final List<CardHit> cards = new ArrayList<>();
	private final List<Rect> categoryRects = new ArrayList<>();
	private final List<Rect> swatchRects = new ArrayList<>();
	private Rect searchRect = Rect.EMPTY;
	private Rect themeBtnRect = Rect.EMPTY;
	private Rect closeBtnRect = Rect.EMPTY;
	private Rect toggleAllRect = Rect.EMPTY;
	private Rect contentRect = Rect.EMPTY;
	private Rect panelRect = Rect.EMPTY;
	private Rect themePanelRect = Rect.EMPTY;
	private CardHit currentCard;
	private final List<Rect> presetRects = new ArrayList<>();

	public ClickGuiScreen() {
		super(Component.literal("Iekei Client"));
	}

	/* ================================================================
	   Screen hooks
	   ================================================================ */

	@Override
	public boolean isPauseScreen() {
		// keep the world running behind the GUI, exactly like a client overlay
		return false;
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float deltaTicks) {
		// no vanilla panorama / blur - we draw our own dimming layer
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor g, int mouseX, int mouseY, float deltaTicks) {
		long now = System.currentTimeMillis();
		float dt = Math.min(0.1F, (now - lastFrameTime) / 1000.0F);
		lastFrameTime = now;
		timeSeconds += dt;

		for (Category c : Registry.categories()) {
			for (Module m : c.modules()) {
				m.updateAnimation(dt);
			}
		}

		layout();

		// subtle dim so the glass panels separate from the world
		g.fill(0, 0, this.width, this.height, 0x44000000);

		drawPanel(g);
		drawSidebar(g, mouseX, mouseY);
		drawHeader(g, mouseX, mouseY);
		drawContent(g, mouseX, mouseY);
		drawFooter(g);

		if (themeOpen) {
			drawThemePopover(g, mouseX, mouseY);
		}

		drawToast(g);
	}

	/* ================================================================
	   Layout
	   ================================================================ */

	private void layout() {
		int pw = Math.min(600, this.width - 12);
		int ph = Math.min(392, this.height - 12);
		int px = (this.width - pw) / 2;
		int py = (this.height - ph) / 2;
		panelRect = new Rect(px, py, pw, ph);

		int mainX = px + SIDEBAR_W;
		int mainW = pw - SIDEBAR_W;

		searchRect = new Rect(px + pw - 172, py + 9, 118, 16);
		themeBtnRect = new Rect(px + pw - 50, py + 9, 32, 16);
		closeBtnRect = new Rect(px + pw - 16, py + 9, 10, 16);

		contentRect = new Rect(mainX + PAD, py + HEADER_H + 22, mainW - PAD * 2,
				ph - HEADER_H - 22 - FOOTER_H - 2);

		categoryRects.clear();

		int navX = px + 5;
		int navY = py + 40;
		int navW = SIDEBAR_W - 10;

		for (int i = 0; i < Registry.categories().size(); i++) {
			categoryRects.add(new Rect(navX, navY + i * 20, navW, 19));
		}

		swatchRects.clear();

		int swY = py + ph - 24;
		int swX = px + 8;

		for (int i = 0; i < Theme.PRESETS.length; i++) {
			swatchRects.add(new Rect(swX + i * 16, swY, 11, 11));
		}

		presetRects.clear();
		int tpW = 128;
		int tpH = 34;
		themePanelRect = new Rect(px + pw - 16 - tpW, py + 28, tpW, tpH);

		for (int i = 0; i < Theme.PRESETS.length; i++) {
			presetRects.add(new Rect(themePanelRect.x + 8 + i * 23, themePanelRect.y + 11, 15, 15));
		}
	}

	/* ================================================================
	   Panel / chrome
	   ================================================================ */

	private void drawPanel(GuiGraphicsExtractor g) {
		int x = panelRect.x;
		int y = panelRect.y;
		int w = panelRect.w;
		int h = panelRect.h;

		Draw.shadow(g, x, y, w, h, PANEL_RADIUS, 10, 0x8C000000);

		// frosted body + 1px accent border ring
		Draw.roundOutline(g, x, y, w, h, PANEL_RADIUS, 0xE80A0A0F, Theme.alpha(70));

		// accent bloom at the top edge and a soft top highlight
		Draw.gradient(g, x + PANEL_RADIUS, y + 1, w - PANEL_RADIUS * 2, 30, Theme.alpha(26), 0x00000000, 8);
		Draw.gradient(g, x + 2, y + 2, w - 4, 40, 0x12FFFFFF, 0x00000000, 5);
	}

	private void drawSidebar(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelRect.x + 1;
		int y = panelRect.y + 1;
		int w = SIDEBAR_W - 1;
		int h = panelRect.h - 2;

		Draw.roundRect(g, x + 1, y + 1, w - 1, h - 2, PANEL_RADIUS - 1, 0x14FFFFFF);
		Draw.vLine(g, x + w, y + 6, h - 12, STROKE);

		// brand
		int bx = x + 8;
		int by = y + 9;

		Draw.roundRect(g, bx, by, 18, 18, 5, Theme.alpha(235));
		Draw.icon(g, Icon.SPARKLES, bx + 3, by + 3, 12, 0xFFFFFFFF);
		Draw.text(g, "IEKEI", bx + 24, by + 1, TEXT);
		Draw.text(g, ".", bx + 24 + Draw.width("IEKEI"), by + 1, Theme.accent());
		Draw.text(g, "CLIENT · FABRIC", bx + 24, by + 11, DIM);

		// category rail
		for (int i = 0; i < Registry.categories().size(); i++) {
			Category cat = Registry.categories().get(i);
			Rect r = categoryRects.get(i);
			boolean active = i == Registry.activeIndex() && search.isEmpty() && !searchFocused;
			boolean hover = r.contains(mouseX, mouseY);

			if (active) {
				Draw.roundRect(g, r.x, r.y, r.w, r.h, 4, Theme.alpha(38));
				Draw.roundRect(g, r.x, r.y, r.w, r.h, 4, Theme.alpha(64));
				Draw.roundRect(g, r.x + 1, r.y + 1, r.w - 2, r.h - 2, 3, Theme.alpha(30));
				Draw.rect(g, r.x, r.y + 5, 2, 9, Theme.accent());
			} else if (hover) {
				Draw.roundRect(g, r.x, r.y, r.w, r.h, 4, 0x14FFFFFF);
			}

			int color = active ? Theme.accent() : (hover ? TEXT : MUTED);
			Draw.icon(g, cat.icon(), r.x + 7, r.y + 5, 10, color);
			Draw.text(g, cat.name(), r.x + 22, r.y + 6, active ? TEXT : MUTED);

			String count = cat.enabledCount() + "";
			Draw.textRight(g, count, r.x + r.w - 5, r.y + 6, active ? Theme.accent() : DIM);
		}

		// keybind card + accent swatches
		int kx = x + 7;
		int ky = y + h - 66;
		int kw = w - 14;

		Draw.roundRect(g, kx, ky, kw, 26, 4, 0x14FFFFFF);
		Draw.text(g, "TOGGLE GUI", kx + 5, ky + 4, DIM);
		Draw.text(g, "[ Right Shift ] to", kx + 5, ky + 14, Theme.accent());

		Draw.text(g, "ACCENT", kx, ky + 33, DIM);

		for (int i = 0; i < swatchRects.size(); i++) {
			Rect r = swatchRects.get(i);
			boolean selected = (Theme.PRESETS[i] & 0xFFFFFF) == (Theme.accent() & 0xFFFFFF);
			boolean hover = r.contains(mouseX, mouseY);

			if (selected) {
				Draw.roundRect(g, r.x - 2, r.y - 2, r.w + 4, r.h + 4, 5, 0x33FFFFFF);
			}

			Draw.roundRect(g, r.x, r.y, r.w, r.h, 4, Theme.PRESETS[i]);

			if (hover) {
				Draw.roundRect(g, r.x, r.y, r.w, r.h, 4, 0x33FFFFFF);
			}
		}
	}

	private void drawHeader(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		int x = panelRect.x + SIDEBAR_W;
		int y = panelRect.y;

		Draw.filledCircle(g, x + 11, y + 14, 2, Theme.accent());
		Draw.text(g, "ClickGUI", x + 18, y + 9, TEXT);
		Draw.text(g, "[ Right Shift ] to Toggle Menu", x + 18, y + 20, DIM);

		// search field
		Rect s = searchRect;
		boolean hover = s.contains(mouseX, mouseY);
		Draw.roundRect(g, s.x, s.y, s.w, s.h, 4, searchFocused ? 0x33FFFFFF : (hover ? 0x22FFFFFF : 0x18FFFFFF));
		Draw.roundRect(g, s.x, s.y, s.w, s.h, 4, searchFocused ? Theme.alpha(160) : STROKE);

		Draw.icon(g, Icon.SEARCH, s.x + 4, s.y + 3, 10, searchFocused ? Theme.accent() : DIM);

		String shown = search.isEmpty() && !searchFocused ? "Search modules..." : search;
		int textColor = search.isEmpty() && !searchFocused ? DIM : TEXT;
		Draw.textTruncated(g, shown, s.x + 17, s.y + 4, s.w - 22, textColor);

		if (searchFocused && (timeSeconds * 2) % 2 < 1) {
			int caret = s.x + 17 + Math.min(Draw.width(search), s.w - 24);
			Draw.rect(g, caret, s.y + 4, 1, 9, Theme.accent());
		}

		// theme button
		Rect t = themeBtnRect;
		boolean themeHover = t.contains(mouseX, mouseY) || themeOpen;
		Draw.roundRect(g, t.x, t.y, t.w, t.h, 4, themeHover ? Theme.alpha(46) : 0x18FFFFFF);
		Draw.roundRect(g, t.x, t.y, t.w, t.h, 4, themeHover ? Theme.alpha(120) : STROKE);
		Draw.icon(g, Icon.PALETTE, t.x + 4, t.y + 3, 10, themeHover ? Theme.accent() : MUTED);
		Draw.text(g, "Theme", t.x + 16, t.y + 4, themeHover ? TEXT : MUTED);

		// close
		Rect c = closeBtnRect;
		boolean closeHover = c.contains(mouseX, mouseY);
		Draw.icon(g, Icon.CLOSE, c.x + 1, c.y + 3, 10, closeHover ? Theme.accent() : DIM);

		Draw.hLine(g, x, y + HEADER_H - 2, panelRect.w - SIDEBAR_W - 2, STROKE);
	}

	private void drawContent(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		cards.clear();
		toggleAllRect = Rect.EMPTY;

		Rect area = contentRect;
		boolean searching = !search.isEmpty();
		Category cat = Registry.active();
		List<Module> modules = searching ? Registry.search(search) : cat.modules();

		// category header / search banner
		int headY = area.y - 20;

		if (searching) {
			Draw.icon(g, Icon.SEARCH, area.x, headY - 1, 10, Theme.accent());
			Draw.text(g, modules.size() + " module" + (modules.size() == 1 ? "" : "s") + " found for \"" + search + "\"",
					area.x + 14, headY, MUTED);
		} else {
			Draw.icon(g, cat.icon(), area.x, headY - 2, 12, Theme.accent());
			Draw.text(g, cat.name(), area.x + 17, headY, TEXT);
			Draw.text(g, cat.subtitle(), area.x + 20 + Draw.width(cat.name()), headY, DIM);
			Draw.textTruncated(g, cat.description(), area.x + 17, headY + 10, area.w - 150, DIM);

			String count = cat.enabledCount() + " / " + cat.modules().size();
			Draw.roundRect(g, area.x + area.w - 116, headY - 2, 44, 13, 3, Theme.alpha(28));
			Draw.text(g, count, area.x + area.w - 112, headY + 1, Theme.accent());

			Rect ta = new Rect(area.x + area.w - 68, headY - 2, 68, 13);
			toggleAllRect = ta;
			boolean hover = ta.contains(mouseX, mouseY);
			Draw.roundRect(g, ta.x, ta.y, ta.w, ta.h, 3, hover ? Theme.alpha(46) : 0x14FFFFFF);
			Draw.textCenter(g, "Toggle all", ta.x + ta.w / 2, ta.y + 2, hover ? TEXT : MUTED);
		}

		/* ---- card grid ---- */
		int cardW = (area.w - GAP) / 2;
		int[] columnY = {0, 0};
		List<Module> visible = new ArrayList<>();
		List<Integer> offsets = new ArrayList<>();

		for (Module m : modules) {
			int col = visible.size() % 2;
			visible.add(m);
			offsets.add(columnY[col]);
			columnY[col] += cardHeight(m) + GAP;
		}

		contentHeight = Math.max(columnY[0], columnY[1]);
		int maxScroll = Math.max(0, contentHeight - area.h + 4);
		scroll = Math.max(0, Math.min(scroll, maxScroll));

		Draw.pushClip(g, area.x, area.y - 22, area.x + area.w, area.y + area.h);

		for (int i = 0; i < visible.size(); i++) {
			Module m = visible.get(i);
			int col = i % 2;
			int x = area.x + col * (cardW + GAP);
			int y = area.y + offsets.get(i) - scroll;
			drawCard(g, m, x, y, cardW, mouseX, mouseY);
		}

		if (visible.isEmpty()) {
			Draw.icon(g, Icon.SEARCH, area.x + area.w / 2 - 6, area.y + 40, 12, DIM);
			Draw.textCenter(g, "No modules match your search", area.x + area.w / 2, area.y + 58, MUTED);
			Draw.textCenter(g, "Try \"fly\", \"esp\", \"finder\" or \"speed\"", area.x + area.w / 2, area.y + 70, DIM);
		}

		Draw.popClip(g);

		// scrollbar
		if (maxScroll > 0) {
			int trackX = area.x + area.w + 2;
			int trackH = area.h;
			Draw.roundRect(g, trackX, area.y, 2, trackH, 1, 0x18FFFFFF);
			int knobH = Math.max(16, trackH * area.h / Math.max(1, contentHeight));
			int knobY = area.y + (trackH - knobH) * scroll / Math.max(1, maxScroll);
			Draw.roundRect(g, trackX, knobY, 2, knobH, 1, Theme.alpha(150));
		}
	}

	private void drawCard(GuiGraphicsExtractor g, Module m, int x, int y, int w, int mouseX, int mouseY) {
		int h = cardHeight(m);
		boolean hover = mouseX >= x && mouseX < x + w && mouseY >= y && mouseY < y + CARD_HEADER_H;
		boolean on = m.enabled();

		int bg = on ? Theme.alpha(26) : (hover ? CARD_BG_HOVER : CARD_BG);
		Draw.roundRect(g, x, y, w, h, CARD_RADIUS, bg);
		Draw.roundRect(g, x, y, w, h, CARD_RADIUS, on ? Theme.alpha(90) : (hover ? STROKE_STRONG : STROKE));
		Draw.roundRect(g, x + 1, y + 1, w - 2, h - 2, CARD_RADIUS - 1, bg);

		// icon chip
		int iconColor = on ? Theme.accent() : MUTED;
		Draw.roundRect(g, x + 5, y + 5, 15, 15, 4, on ? Theme.alpha(40) : 0x1AFFFFFF);
		Draw.icon(g, m.category().icon(), x + 7, y + 7, 11, iconColor);

		// name + description
		int tagW = 0;

		if (!search.isEmpty()) {
			String tag = m.category().name().toUpperCase();
			tagW = Draw.width(tag) + 6;
			Draw.roundRect(g, x + 25 + Draw.width(m.name()) + 4, y + 5, tagW, 9, 2, 0x1AFFFFFF);
			Draw.text(g, tag, x + 25 + Draw.width(m.name()) + 7, y + 6, DIM);
		}

		int nameMax = w - 80 - tagW;
		Draw.textTruncated(g, m.name(), x + 25, y + 5, nameMax, on ? TEXT : 0xFFD6DAE6);
		Draw.textTruncated(g, m.description(), x + 25, y + 15, w - 34, on ? MUTED : DIM);

		// switch
		int swX = x + w - 30;
		int swY = y + 9;
		drawSwitch(g, swX, swY, 20, 9, on);

		// chevron
		Draw.icon(g, Icon.CHEVRON, x + w - 12, y + 8, 10, m.expanded() ? Theme.accent() : DIM);

		// expandable settings
		float p = m.expandProgress();
		CardHit hit = new CardHit(m, x, y, w, h, swX - 2, swY - 3, 22, 15);
		cards.add(hit);
		currentCard = hit;

		if (p > 0.01F) {
			int viewH = Math.round((h - CARD_HEADER_H) * p);
			Draw.pushClip(g, x, y + CARD_HEADER_H, x + w, y + CARD_HEADER_H + viewH);
			Draw.hLine(g, x + 5, y + CARD_HEADER_H, w - 10, STROKE);

			int sy = y + CARD_HEADER_H + 3;

			for (Setting setting : m.settings()) {
				drawSetting(g, m, setting, x + 5, sy, w - 10, mouseX, mouseY);
				sy += SETTING_ROW_H;
			}

			Draw.popClip(g);
		}

		currentCard = null;
	}

	private void drawSetting(GuiGraphicsExtractor g, Module m, Setting s, int x, int y, int w, int mouseX,
			int mouseY) {
		switch (s.type()) {
			case TOGGLE -> {
				Draw.text(g, s.label(), x, y + 4, MUTED);
				int swX = x + w - 18;
				addHit(new SettingHit(s, swX - 3, y, 24, SETTING_ROW_H, 0, 0));
				drawSwitch(g, swX, y + 5, 16, 8, s.getBool());
			}
			case CHOICE -> {
				Draw.text(g, s.label(), x, y + 4, MUTED);
				int bw = Math.min(58, Math.max(34, Draw.width(s.choiceLabel()) + 10));
				int bx = x + w - bw;
				boolean hover = mouseX >= bx && mouseX < bx + bw && mouseY >= y + 2 && mouseY < y + 15;
				Draw.roundRect(g, bx, y + 2, bw, 13, 3, hover ? Theme.alpha(40) : 0x1AFFFFFF);
				Draw.roundRect(g, bx, y + 2, bw, 13, 3, hover ? Theme.alpha(120) : STROKE);
				Draw.textTruncated(g, s.choiceLabel(), bx + 4, y + 5, bw - 12, TEXT);
				Draw.icon(g, Icon.CHEVRON, bx + bw - 9, y + 6, 8, DIM);
				addHit(new SettingHit(s, bx, y, bw, 16, 0, 0));
			}
			case SLIDER -> {
				Draw.text(g, s.label(), x, y + 1, MUTED);
				Draw.textRight(g, s.displayValue(), x + w, y + 1, Theme.accent());

				int trackY = y + 12;
				int trackW = w;
				Draw.roundRect(g, x, trackY, trackW, 3, 1, 0x24FFFFFF);
				int fillW = Math.round(trackW * s.progress());
				Draw.roundRect(g, x, trackY, Math.max(2, fillW), 3, 1, Theme.accent());
				int knobX = x + Math.max(0, Math.min(trackW - 3, fillW - 2));
				Draw.roundRect(g, knobX, trackY - 2, 4, 7, 2, 0xFFFFFFFF);

				addHit(new SettingHit(s, x, y, w, SETTING_ROW_H, x, trackW));
			}
		}
	}

	private void drawSwitch(GuiGraphicsExtractor g, int x, int y, int w, int h, boolean on) {
		Draw.roundRect(g, x, y, w, h, h / 2, on ? Theme.accent() : 0x33FFFFFF);

		if (on) {
			Draw.roundRect(g, x + 1, y + 1, w - 2, h - 3, h / 2, Theme.alpha(255));
		}

		int knob = h - 2;
		int knobX = on ? x + w - knob - 1 : x + 1;
		Draw.filledCircle(g, knobX + knob / 2, y + h / 2, knob / 2, on ? 0xFFFFFFFF : 0xFFB9BEC9);
	}

	private void drawThemePopover(GuiGraphicsExtractor g, int mouseX, int mouseY) {
		Rect p = themePanelRect;
		Draw.shadow(g, p.x, p.y, p.w, p.h, 5, 6, 0x8C000000);
		Draw.roundRect(g, p.x, p.y, p.w, p.h, 5, 0xF7101017);
		Draw.roundRect(g, p.x, p.y, p.w, p.h, 5, STROKE_STRONG);

		Draw.text(g, "Theme Customizer", p.x + 7, p.y + 4, TEXT);

		for (int i = 0; i < presetRects.size(); i++) {
			Rect r = presetRects.get(i);
			boolean selected = (Theme.PRESETS[i] & 0xFFFFFF) == (Theme.accent() & 0xFFFFFF);
			boolean hover = r.contains(mouseX, mouseY);

			if (selected || hover) {
				Draw.roundRect(g, r.x - 1, r.y - 1, r.w + 2, r.h + 2, 4, selected ? 0x44FFFFFF : 0x22FFFFFF);
			}

			Draw.roundRect(g, r.x, r.y, r.w, r.h, 3, Theme.PRESETS[i]);
		}

		Draw.text(g, Theme.PRESET_NAMES[Math.max(0, Theme.presetIndex())], p.x + 7, p.y + 20, DIM);
	}

	private void drawFooter(GuiGraphicsExtractor g) {
		int x = panelRect.x + SIDEBAR_W + PAD;
		int y = panelRect.y + panelRect.h - FOOTER_H + 1;

		Draw.hLine(g, panelRect.x + SIDEBAR_W + 2, y - 3, panelRect.w - SIDEBAR_W - 4, STROKE);

		Draw.filledCircle(g, x + 2, y + 4, 1, Theme.ON);
		Draw.text(g, "CONNECTED", x + 7, y, MUTED);

		String stats = "ACTIVE " + Registry.totalEnabled() + " / " + Registry.totalModules();
		Draw.textRight(g, stats, panelRect.x + panelRect.w - PAD, y, Theme.accent());
		Draw.textRight(g, "TOGGLE [SHIFTRIGHT]", panelRect.x + panelRect.w - PAD - Draw.width(stats) - 12, y, DIM);
	}

	private void drawToast(GuiGraphicsExtractor g) {
		if (toast.isEmpty()) {
			return;
		}

		long age = System.currentTimeMillis() - toastTime;

		if (age > 2200) {
			toast = "";
			return;
		}

		float fade = age < 160 ? age / 160.0F : (age > 1900 ? 1.0F - (age - 1900) / 300.0F : 1.0F);
		int alpha = (int) (255 * Math.max(0.0F, Math.min(1.0F, fade)));
		int w = Draw.width(toast) + 24;
		int x = panelRect.x + panelRect.w - w - 6;
		int y = panelRect.y + panelRect.h - FOOTER_H - 22 + (int) ((1 - fade) * 6);

		Draw.roundRect(g, x, y, w, 16, 4, Theme.alpha(Math.min(alpha, 220)));
		Draw.roundRect(g, x + 1, y + 1, w - 2, 14, 3, (Math.min(alpha, 235) << 24) | 0x0E0E14);
		Draw.icon(g, Icon.CHECK, x + 5, y + 4, 9, Theme.alpha(alpha));
		Draw.text(g, toast, x + 17, y + 4, (alpha << 24) | (TEXT & 0xFFFFFF));
	}

	private void addHit(SettingHit hit) {
		if (currentCard != null) {
			currentCard.settings.add(hit);
		}
	}

	private int cardHeight(Module m) {
		int settings = m.settings().size();
		return CARD_HEADER_H + Math.round(settings * SETTING_ROW_H * m.expandProgress()) + 4;
	}

	/* ================================================================
	   Input
	   ================================================================ */

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		double mx = event.x();
		double my = event.y();

		if (event.button() != InputConstants.MOUSE_BUTTON_LEFT) {
			return super.mouseClicked(event, doubleClick);
		}

		// clicks outside the panel fall through to the game
		if (!panelRect.contains(mx, my)) {
			searchFocused = false;
			themeOpen = false;
			return super.mouseClicked(event, doubleClick);
		}

		// theme popover owns clicks while open
		if (themeOpen) {
			for (int i = 0; i < presetRects.size(); i++) {
				if (presetRects.get(i).contains(mx, my)) {
					Theme.setPreset(i);
					notifyToast("Accent · " + Theme.PRESET_NAMES[i]);
					return true;
				}
			}

			if (!themePanelRect.contains(mx, my) && !themeBtnRect.contains(mx, my)) {
				themeOpen = false;
			}
		}

		if (closeBtnRect.contains(mx, my)) {
			onClose();
			return true;
		}

		if (themeBtnRect.contains(mx, my)) {
			themeOpen = !themeOpen;
			return true;
		}

		searchFocused = searchRect.contains(mx, my);

		if (searchFocused) {
			return true;
		}

		// sidebar swatches
		for (int i = 0; i < swatchRects.size(); i++) {
			if (swatchRects.get(i).contains(mx, my)) {
				Theme.setPreset(i);
				notifyToast("Accent · " + Theme.PRESET_NAMES[i]);
				return true;
			}
		}

		// categories
		for (int i = 0; i < categoryRects.size(); i++) {
			if (categoryRects.get(i).contains(mx, my)) {
				Registry.setActive(i);
				scroll = 0;
				search = "";
				return true;
			}
		}

		// toggle all
		if (toggleAllRect.contains(mx, my) && search.isEmpty()) {
			Category cat = Registry.active();
			boolean allOn = cat.enabledCount() == cat.modules().size();

			for (Module m : cat.modules()) {
				m.setEnabled(!allOn);
			}

			notifyToast(cat.name() + (allOn ? " disabled" : " enabled"));
			return true;
		}

		// module cards + their settings
		if (!contentRect.contains(mx, my)) {
			return super.mouseClicked(event, doubleClick);
		}

		for (CardHit hit : cards) {
			if (!hit.contains(mx, my)) {
				continue;
			}

			if (hit.switchHit(mx, my)) {
				hit.module.toggleEnabled();
				notifyToast(hit.module.name() + (hit.module.enabled() ? " enabled" : " disabled"));
				return true;
			}

			if (my < hit.y + CARD_HEADER_H) {
				hit.module.setExpanded(!hit.module.expanded());
				return true;
			}

			SettingHit sh = hit.settingAt(mx, my);

			if (sh != null) {
				switch (sh.setting.type()) {
					case TOGGLE -> {
						sh.setting.toggleBool();
						return true;
					}
					case CHOICE -> {
						sh.setting.cycleChoice();
						return true;
					}
					case SLIDER -> {
						dragging = sh.setting;
						dragTrackX = sh.trackX;
						dragTrackW = sh.trackW;
						updateSlider(mx);
						return true;
					}
				}
			}

			return true;
		}

		return super.mouseClicked(event, doubleClick);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		dragging = null;
		return super.mouseReleased(event);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dragX, double dragY) {
		if (dragging != null) {
			updateSlider(event.x());
			return true;
		}

		return super.mouseDragged(event, dragX, dragY);
	}

	private void updateSlider(double mouseX) {
		if (dragging == null || dragTrackW <= 0) {
			return;
		}

		float t = (float) ((mouseX - dragTrackX) / dragTrackW);
		t = Math.max(0.0F, Math.min(1.0F, t));
		dragging.setNumber(dragging.min() + t * (dragging.max() - dragging.min()));
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (contentRect.contains(mouseX, mouseY)) {
			scroll -= (int) Math.round(verticalAmount * 18);
			int maxScroll = Math.max(0, contentHeight - contentRect.h + 4);
			scroll = Math.max(0, Math.min(scroll, maxScroll));
			return true;
		}

		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		// [Right Shift] closes the GUI (opening is handled by the keybind)
		if (event.scancode() == SDLScancode.SDL_SCANCODE_RSHIFT) {
			IekeiClientClient.suppressOpenFor(300L);
			onClose();
			return true;
		}

		if (searchFocused) {
			if (event.scancode() == SDLScancode.SDL_SCANCODE_BACKSPACE) {
				if (!search.isEmpty()) {
					search = search.substring(0, search.length() - 1);
				} else {
					searchFocused = false;
				}

				return true;
			}

			if (event.scancode() == SDLScancode.SDL_SCANCODE_ESCAPE) {
				searchFocused = false;
				return true;
			}
		}

		return super.keyPressed(event);
	}

	@Override
	public boolean charTyped(CharacterEvent event) {
		if (searchFocused) {
			int codepoint = event.codepoint();

			if (codepoint >= 32 && codepoint != 127) {
				search = search + event.codepointAsString();
				scroll = 0;
			}

			return true;
		}

		return super.charTyped(event);
	}

	private void notifyToast(String message) {
		toast = message;
		toastTime = System.currentTimeMillis();
	}

	/* ================================================================
	   Hit testing helpers
	   ================================================================ */

	/** Simple mutable rectangle. */
	private static final class Rect {
		static final Rect EMPTY = new Rect(0, 0, 0, 0);

		final int x;
		final int y;
		final int w;
		final int h;

		Rect(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}

		boolean contains(double px, double py) {
			return px >= x && px < x + w && py >= y && py < y + h;
		}
	}

	private static final class SettingHit {
		final Setting setting;
		final int x;
		final int y;
		final int w;
		final int h;
		final int trackX;
		final int trackW;

		SettingHit(Setting setting, int x, int y, int w, int h, int trackX, int trackW) {
			this.setting = setting;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.trackX = trackX;
			this.trackW = trackW;
		}

		boolean contains(double px, double py) {
			return px >= x && px < x + w && py >= y && py < y + h;
		}
	}

	private static final class CardHit {
		final Module module;
		final int x;
		final int y;
		final int w;
		final int h;
		final int swX;
		final int swY;
		final int swW;
		final int swH;
		final List<SettingHit> settings = new ArrayList<>();

		CardHit(Module module, int x, int y, int w, int h, int swX, int swY, int swW, int swH) {
			this.module = module;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.swX = swX;
			this.swY = swY;
			this.swW = swW;
			this.swH = swH;
		}

		boolean contains(double px, double py) {
			return px >= x && px < x + w && py >= y && py < y + h;
		}

		boolean switchHit(double px, double py) {
			return px >= swX && px < swX + swW && py >= swY && py < swY + swH;
		}

		SettingHit settingAt(double px, double py) {
			for (SettingHit sh : settings) {
				if (sh.contains(px, py)) {
					return sh;
				}
			}

			return null;
		}
	}
}
