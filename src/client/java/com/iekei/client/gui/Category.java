package com.iekei.client.gui;

import com.iekei.client.gui.Icons.Icon;
import java.util.ArrayList;
import java.util.List;

/** A ClickGUI category (sidebar entry) with its modules. */
public final class Category {
	private final String id;
	private final String name;
	private final String subtitle;
	private final String description;
	private final Icon icon;
	private final List<Module> modules = new ArrayList<>();

	public Category(String id, String name, String subtitle, String description, Icon icon) {
		this.id = id;
		this.name = name;
		this.subtitle = subtitle;
		this.description = description;
		this.icon = icon;
	}

	public Category add(Module module) {
		module.setCategory(this);
		modules.add(module);
		return this;
	}

	public String id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String subtitle() {
		return subtitle;
	}

	public String description() {
		return description;
	}

	public Icon icon() {
		return icon;
	}

	public List<Module> modules() {
		return modules;
	}

	public int enabledCount() {
		int n = 0;

		for (Module m : modules) {
			if (m.enabled()) {
				n++;
			}
		}

		return n;
	}
}
