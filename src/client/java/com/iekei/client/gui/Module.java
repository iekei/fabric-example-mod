package com.iekei.client.gui;

import java.util.ArrayList;
import java.util.List;

/** A toggleable client module together with its settings. */
public final class Module {
	private final String id;
	private final String name;
	private final String description;
	private final List<Setting> settings = new ArrayList<>();
	private Category category;
	private boolean enabled;
	private boolean expanded;
	private float expandProgress;

	public Module(String id, String name, String description, boolean enabled, Setting... settings) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.enabled = enabled;
		this.settings.addAll(List.of(settings));
	}

	public String id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public List<Setting> settings() {
		return settings;
	}

	public Category category() {
		return category;
	}

	void setCategory(Category category) {
		this.category = category;
	}

	public boolean enabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public void toggleEnabled() {
		enabled = !enabled;
	}

	public boolean expanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public float expandProgress() {
		return expandProgress;
	}

	/** Advances the accordion animation (delta in seconds). */
	public void updateAnimation(float deltaSeconds) {
		float target = expanded ? 1.0F : 0.0F;
		float speed = Math.min(1.0F, deltaSeconds * 7.0F);

		if (expandProgress < target) {
			expandProgress = Math.min(target, expandProgress + speed);
		} else if (expandProgress > target) {
			expandProgress = Math.max(target, expandProgress - speed);
		}
	}
}
