package com.iekei.client.gui;

import java.util.Arrays;
import java.util.List;

/**
 * A single configurable value of a module. The in-game ClickGUI renders these
 * as toggles, sliders and cycling dropdowns.
 */
public final class Setting {
	public enum Type {
		TOGGLE,
		SLIDER,
		CHOICE
	}

	private final String label;
	private final Type type;
	private final List<String> options;
	private final double min;
	private final double max;
	private final double step;
	private final String unit;

	private boolean boolValue;
	private double numberValue;
	private int choiceIndex;

	private Setting(String label, Type type, List<String> options, double min, double max, double step, String unit) {
		this.label = label;
		this.type = type;
		this.options = options;
		this.min = min;
		this.max = max;
		this.step = step;
		this.unit = unit;
	}

	public static Setting toggle(String label, boolean value) {
		Setting s = new Setting(label, Type.TOGGLE, List.of(), 0, 0, 0, "");
		s.boolValue = value;
		return s;
	}

	public static Setting slider(String label, double value, double min, double max, double step, String unit) {
		Setting s = new Setting(label, Type.SLIDER, List.of(), min, max, step, unit);
		s.numberValue = value;
		return s;
	}

	public static Setting choice(String label, String... options) {
		Setting s = new Setting(label, Type.CHOICE, Arrays.asList(options), 0, 0, 0, "");
		s.choiceIndex = 0;
		return s;
	}

	public String label() {
		return label;
	}

	public Type type() {
		return type;
	}

	public List<String> options() {
		return options;
	}

	public String unit() {
		return unit;
	}

	public boolean getBool() {
		return boolValue;
	}

	public void setBool(boolean value) {
		boolValue = value;
	}

	public void toggleBool() {
		boolValue = !boolValue;
	}

	public double getNumber() {
		return numberValue;
	}

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	/** Sets the value, clamped and snapped to the configured step. */
	public void setNumber(double value) {
		double v = Math.max(min, Math.min(max, value));

		if (step > 0) {
			v = min + Math.round((v - min) / step) * step;
		}

		numberValue = v;
	}

	/** 0..1 position of the slider knob. */
	public float progress() {
		return max > min ? (float) ((numberValue - min) / (max - min)) : 0.0F;
	}

	public int getChoice() {
		return choiceIndex;
	}

	public void setChoice(int index) {
		if (options.isEmpty()) {
			return;
		}

		choiceIndex = Math.floorMod(index, options.size());
	}

	public void cycleChoice() {
		setChoice(choiceIndex + 1);
	}

	public String choiceLabel() {
		return options.isEmpty() ? "-" : options.get(choiceIndex);
	}

	/** Formatted value shown on the right of a setting row. */
	public String displayValue() {
		return switch (type) {
			case TOGGLE -> boolValue ? "ON" : "OFF";
			case CHOICE -> choiceLabel();
			case SLIDER -> {
				double v = numberValue;
				String number = step >= 1 ? String.valueOf(Math.round(v)) : String.format("%.1f", v);
				yield number + unit;
			}
		};
	}
}
