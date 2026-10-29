package com.iekei.client.gui;

import com.iekei.client.gui.Icons.Icon;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The full Iekei Client module list: 8 categories, 78 modules, each with the
 * settings documented in the design spec.
 */
public final class Registry {
	private Registry() {
	}

	private static final List<Category> CATEGORIES = new ArrayList<>();
	private static int activeCategory;

	static {
		/* ------------------------------------------------ Movement */
		Category movement = new Category("movement", "Movement", "移動系",
				"Travel faster, higher and further than vanilla allows.", Icon.FOOTPRINTS);
		movement.add(mod("fly", "Fly", "Allows sustained flight in survival.", true,
				Setting.slider("Speed", 1.6, 0.1, 5.0, 0.1, "x"),
				Setting.choice("Mode", "Vanilla", "Creative", "Glide"),
				Setting.toggle("Anti-Kick", true)));
		movement.add(mod("speed", "Speed", "Multiplies your ground movement speed.", true,
				Setting.slider("Multiplier", 2.4, 1.0, 10.0, 0.1, "x"),
				Setting.choice("Mode", "Strafe", "Omni", "Legacy"),
				Setting.toggle("Auto Jump", true)));
		movement.add(mod("noslowdown", "NoSlowdown", "Removes the slowdown from using items.", true,
				Setting.toggle("Items Only", false),
				Setting.toggle("Ignore Webs", true)));
		movement.add(mod("jesus", "Jesus", "Walk on water, lava and other liquids.", false,
				Setting.choice("Liquid", "Water", "Lava", "Both"),
				Setting.toggle("Sneak to Sink", true)));
		movement.add(mod("step", "Step", "Instantly climbs up blocks.", true,
				Setting.slider("Height", 1.0, 0.5, 2.5, 0.1, "b"),
				Setting.toggle("Instant", true)));
		movement.add(mod("highjump", "HighJump", "Jump several blocks into the air.", false,
				Setting.slider("Multiplier", 2.0, 1.0, 8.0, 0.1, "x"),
				Setting.toggle("Glide Down", false)));
		movement.add(mod("safewalk", "SafeWalk", "Stops you from walking off edges.", true,
				Setting.toggle("Force Sneak", true),
				Setting.toggle("Include Air Blocks", false)));
		movement.add(mod("spider", "Spider", "Climb walls like a spider.", false,
				Setting.slider("Climb Speed", 1.0, 0.2, 3.0, 0.1, "x"),
				Setting.toggle("Only When Looking Up", true)));
		movement.add(mod("inventorymove", "InventoryMove", "Move while an inventory screen is open.", false,
				Setting.toggle("Allow in GUIs", true)));
		movement.add(mod("airjump", "AirJump", "Jump again while already airborne.", false,
				Setting.slider("Jump Height", 0.9, 0.5, 2.0, 0.1, "b"),
				Setting.slider("Max Jumps", 3, 1, 10, 1, "x")));
		movement.add(mod("entityspeed", "EntitySpeed", "Speeds up boats, horses and minecarts.", false,
				Setting.slider("Speed", 2.5, 1.0, 8.0, 0.1, "x")));
		movement.add(mod("blink", "Blink", "Suspends outgoing packets, then teleports.", false,
				Setting.slider("Hold Duration", 800, 100, 3000, 50, "ms"),
				Setting.toggle("Auto Send", true)));
		movement.add(mod("fastladder", "FastLadder", "Climb ladders at high speed.", false,
				Setting.slider("Speed", 2.0, 1.0, 5.0, 0.1, "x")));
		movement.add(mod("parkour", "Parkour", "Automatically jumps at ledge edges.", false,
				Setting.toggle("Auto Jump", true),
				Setting.toggle("Require Sprint", false)));
		CATEGORIES.add(movement);

		/* ------------------------------------------------ Combat */
		Category combat = new Category("combat", "Combat", "戦闘系",
				"Combat assistance with humanised timing and rotation.", Icon.SWORDS);
		combat.add(mod("killaura", "Killaura", "Attacks nearby entities automatically.", true,
				Setting.slider("Range", 4.2, 2.5, 7.0, 0.1, "b"),
				Setting.slider("APS", 9.5, 1.0, 20.0, 0.5, "/s"),
				Setting.choice("Priority", "Closest", "Lowest HP", "Angle"),
				Setting.toggle("Target Players", true)));
		combat.add(mod("triggerbot", "Triggerbot", "Attacks whatever is under your crosshair.", false,
				Setting.slider("Delay", 60, 0, 500, 10, "ms")));
		combat.add(mod("aimassist", "AimAssist", "Gently nudges your aim towards targets.", true,
				Setting.slider("Smoothness", 4.5, 1.0, 10.0, 0.1, ""),
				Setting.slider("FOV", 90, 30, 180, 5, "°"),
				Setting.toggle("Vertical Assist", false)));
		combat.add(mod("autoclicker", "AutoClicker", "Clicks for you at a set CPS.", false,
				Setting.slider("CPS", 12, 1, 20, 0.5, "/s"),
				Setting.choice("Button", "Left", "Right", "Both"),
				Setting.toggle("Jitter", true)));
		combat.add(mod("reach", "Reach", "Extends your attack range.", false,
				Setting.slider("Distance", 3.6, 3.0, 6.0, 0.1, "b")));
		combat.add(mod("velocity", "Velocity", "Reduces knockback taken from hits.", true,
				Setting.slider("Horizontal", 35, 0, 100, 1, "%"),
				Setting.slider("Vertical", 60, 0, 100, 1, "%")));
		combat.add(mod("criticals", "Criticals", "Forces every hit to be a critical hit.", false,
				Setting.choice("Mode", "Packet", "Jump", "MiniJump")));
		combat.add(mod("bowaimbot", "BowAimbot", "Predicts and aims bow shots for you.", false,
				Setting.slider("Prediction", 0.8, 0.0, 2.0, 0.05, "x")));
		combat.add(mod("fastbow", "FastBow", "Shoots arrows with almost no cooldown.", false,
				Setting.toggle("Instant Release", true)));
		combat.add(mod("targetstrafe", "TargetStrafe", "Circles around your current target.", false,
				Setting.slider("Radius", 3.0, 1.0, 6.0, 0.1, "b"),
				Setting.choice("Direction", "Clockwise", "Counter", "Random")));
		combat.add(mod("hitbox", "HitBox", "Enlarges entity hitboxes.", false,
				Setting.slider("Size", 0.6, 0.1, 2.0, 0.05, "b")));
		combat.add(mod("fastuse", "FastUse", "Uses items such as food far faster.", false,
				Setting.slider("Speed", 2.5, 1.0, 5.0, 0.1, "x")));
		CATEGORIES.add(combat);

		/* ------------------------------------------------ Utility */
		Category utility = new Category("utility", "Player Utility", "ユーティリティ",
				"Quality of life automation for grinding and survival.", Icon.WRENCH);
		utility.add(mod("fastbreak", "FastBreak", "Mines blocks faster.", true,
				Setting.slider("Speed", 2.0, 1.0, 5.0, 0.1, "x"),
				Setting.toggle("Packet Mode", true)));
		utility.add(mod("fastplace", "FastPlace", "Removes the block placement delay.", false,
				Setting.slider("Delay", 0, 0, 4, 1, "t")));
		utility.add(mod("autoarmor", "AutoArmor", "Equips the best armour automatically.", true,
				Setting.toggle("Allow Hotbar", false)));
		utility.add(mod("autoeat", "AutoEat", "Eats when your hunger drops.", false,
				Setting.slider("Hunger Threshold", 14, 2, 19, 1, "/20")));
		utility.add(mod("autototem", "AutoTotem", "Keeps a totem in your offhand.", true,
				Setting.slider("Swap Below", 10, 1, 20, 1, "hp")));
		utility.add(mod("autotool", "AutoTool", "Selects the fastest tool when mining.", false,
				Setting.toggle("Respect Silk Touch", false),
				Setting.toggle("Stop Low Durability", true)));
		utility.add(mod("cheststealer", "ChestStealer", "Empties chests into your inventory.", false,
				Setting.slider("Delay", 60, 0, 500, 10, "ms")));
		utility.add(mod("inventorycleaner", "InventoryCleaner", "Drops junk items automatically.", false,
				Setting.toggle("Auto Drop", true),
				Setting.toggle("Keep Gapples", true)));
		utility.add(mod("nofall", "NoFall", "Cancels fall damage.", true,
				Setting.choice("Mode", "Packet", "NoGround", "Spoof")));
		utility.add(mod("scaffold", "Scaffold", "Places blocks beneath you as you walk.", false,
				Setting.choice("Mode", "Normal", "Tower", "Legit"),
				Setting.toggle("Silent Rotate", true)));
		utility.add(mod("fastdrop", "FastDrop", "Drops items without the delay.", false,
				Setting.slider("Speed", 3, 1, 5, 1, "x")));
		utility.add(mod("autorespawn", "AutoRespawn", "Respawns instantly after dying.", false,
				Setting.toggle("Enabled", true)));
		utility.add(mod("autodisconnect", "AutoDisconnect", "Leaves the server at low health.", false,
				Setting.slider("Health", 6, 1, 20, 1, "hp")));
		utility.add(mod("antiafk", "AntiAFK", "Avoids AFK kicks with small moves.", false,
				Setting.choice("Mode", "Swing", "Walk", "Rotate"),
				Setting.slider("Interval", 30, 5, 120, 5, "s")));
		CATEGORIES.add(utility);

		/* ------------------------------------------------ Render */
		Category render = new Category("render", "Render Visual", "視覚・描画系",
				"Client-side visualisation of the world around you.", Icon.EYE);
		render.add(mod("xray", "Xray", "Reveals ores through solid blocks.", false,
				Setting.slider("Transparency", 70, 0, 100, 1, "%"),
				Setting.toggle("Ores Only", true)));
		render.add(mod("esp", "ESP", "Highlights entities through walls.", true,
				Setting.choice("Style", "Outline", "Skeleton", "Box"),
				Setting.choice("Color", "Accent", "Red", "Cyan", "Gold")));
		render.add(mod("tracers", "Tracers", "Draws lines from you to targets.", false,
				Setting.choice("Color", "Accent", "Blue", "Lime", "White"),
				Setting.slider("Thickness", 2, 1, 5, 0.5, "px")));
		render.add(mod("nametags", "Nametags", "Enlarges and restyles nametags.", false,
				Setting.slider("Size", 1.4, 0.5, 3.0, 0.1, "x"),
				Setting.toggle("Show Health", true)));
		render.add(mod("fullbright", "Fullbright", "Removes darkness client-side.", true,
				Setting.slider("Brightness", 12, 1, 15, 1, "lvl")));
		render.add(mod("freecam", "Freecam", "Detaches the camera from your body.", false,
				Setting.slider("Fly Speed", 1.5, 0.5, 5.0, 0.1, "x")));
		render.add(mod("storageesp", "StorageESP", "Highlights chests and shulkers.", false,
				Setting.choice("Color", "Amber", "Accent", "White")));
		render.add(mod("itemesp", "ItemESP", "Highlights dropped items on the ground.", false,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(render);

		/* ------------------------------------------------ Cosmetic */
		Category cosmetic = new Category("cosmetic", "Cosmetic", "見た目系",
				"Purely visual tweaks — sky, animations and effects.", Icon.PALETTE);
		cosmetic.add(mod("customsky", "CustomSky", "Replaces the vanilla skybox texture.", false,
				Setting.choice("Skybox", "Galaxy", "Sunset", "Nebula")));
		cosmetic.add(mod("custommodel", "CustomModel", "Swaps the model used by held tools.", false,
				Setting.choice("Model", "Vanilla", "Slim 1.7", "Glow")));
		cosmetic.add(mod("customanimations", "CustomAnimations", "Changes the first-person hand motion.", true,
				Setting.choice("Animation", "1.7", "Smooth", "Modern")));
		cosmetic.add(mod("cape", "Cape / Cosmetics", "Shows capes and cosmetic accessories.", false,
				Setting.toggle("Show Cape", true),
				Setting.toggle("Show Wings", false)));
		cosmetic.add(mod("timechanger", "TimeChanger", "Pins the world time client-side.", false,
				Setting.slider("Time", 13000, 0, 24000, 100, "t")));
		cosmetic.add(mod("weatherchanger", "WeatherChanger", "Forces a weather state client-side.", false,
				Setting.choice("Weather", "Clear", "Rain", "Thunder")));
		cosmetic.add(mod("lowfire", "LowFire", "Lowers the fire overlay while burning.", true,
				Setting.slider("Overlay Height", 30, 0, 100, 1, "%")));
		cosmetic.add(mod("hitcolor", "HitColor", "Tints the screen when you take a hit.", false,
				Setting.choice("Tint", "Red", "Accent", "Orange")));
		cosmetic.add(mod("particles", "ParticleCustomizer", "Amplifies hit and critical particles.", false,
				Setting.slider("Amount", 120, 0, 300, 10, "%")));
		cosmetic.add(mod("motionblur", "MotionBlur", "Adds a subtle trailing screen blur.", false,
				Setting.slider("Strength", 3.5, 0.0, 10.0, 0.1, "")));
		cosmetic.add(mod("crosshair", "CustomCrosshair", "Reshapes the crosshair to your taste.", false,
				Setting.slider("Size", 9, 2, 20, 1, "px"),
				Setting.slider("Thickness", 2, 1, 4, 0.5, "px")));
		CATEGORIES.add(cosmetic);

		/* ------------------------------------------------ Base find */
		Category basefind = new Category("basefind", "Base Find", "拠点捜索系",
				"Anubis-style structure and stash scanning for anarchy servers.", Icon.HOME);
		basefind.add(mod("basefinder", "BaseFinder", "Detects man-made block clusters.", true,
				Setting.slider("Sensitivity", 6, 1, 10, 1, ""),
				Setting.slider("Scan Range", 128, 32, 512, 32, "b")));
		basefind.add(mod("chestfinder", "ChestFinder", "Scans for containers and logs coordinates.", false,
				Setting.slider("Range", 96, 16, 256, 16, "b"),
				Setting.toggle("Log to Chat", true)));
		basefind.add(mod("portalfinder", "PortalFinder", "Detects nether portals nearby.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("bedfinder", "BedFinder", "Locates beds for respawn anchors.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("spawnerfinder", "SpawnerFinder", "Finds mob spawners underground.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("illegalblockfinder", "IllegalBlockFinder", "Flags blocks that should not exist.", false,
				Setting.toggle("Enabled", true)));
		basefind.add(mod("stashfinder", "StashFinder", "Identifies high-volume storage stashes.", false,
				Setting.slider("Min Containers", 12, 2, 64, 1, "")));
		basefind.add(mod("newchunks", "NewChunks", "Marks chunks generated by other players.", false,
				Setting.choice("Highlight", "Emerald", "Accent", "Red")));
		basefind.add(mod("trailfinder", "TrailFinder", "Traces movement paths of players.", false,
				Setting.toggle("Show Lines", true)));
		basefind.add(mod("worldguardfinder", "WorldGuardFinder", "Detects claims and protected regions.", false,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(basefind);

		/* ------------------------------------------------ Notification */
		Category notify = new Category("notify", "Notification", "通知・警報系",
				"Alerts that reach you outside the game window.", Icon.BELL);
		notify.add(mod("basealert", "BaseAlert", "Notifies when a base is detected.", true,
				Setting.toggle("System Sound", true)));
		notify.add(mod("playeralert", "PlayerAlert", "Warns when players come close.", false,
				Setting.slider("Trigger Distance", 64, 8, 256, 8, "b")));
		notify.add(mod("visualalert", "VisualAlert", "Shows on-screen popup warnings.", false,
				Setting.toggle("Enabled", true)));
		notify.add(mod("soundalert", "SoundAlert", "Plays a detection sound alert.", false,
				Setting.slider("Volume", 60, 0, 100, 5, "%")));
		notify.add(mod("webhook", "WebhookNotifier", "Sends detections to a Discord webhook.", false,
				Setting.choice("Target", "Discord", "Custom URL")));
		notify.add(mod("autologalert", "AutoLogOnAlert", "Disconnects when an alert fires.", false,
				Setting.slider("Health Threshold", 8, 1, 20, 1, "hp"),
				Setting.slider("Distance Threshold", 32, 8, 128, 8, "b")));
		CATEGORIES.add(notify);

		/* ------------------------------------------------ Settings */
		Category settings = new Category("settings", "Client Settings", "設定",
				"Keybinds, accent theme and client-wide preferences.", Icon.GEAR);
		settings.add(mod("keybind", "Keybind Config", "The key that opens and closes this menu.", true,
				Setting.choice("Menu Toggle Key", "Right Shift", "Right Ctrl", "Grave")));
		settings.add(mod("theme", "Theme Selector", "Accent colour used across the whole client.", true,
				Setting.slider("Glow Intensity", 70, 0, 100, 5, "%")));
		settings.add(mod("sound", "Sound Effects", "Subtle UI clicks and toggle blips.", true,
				Setting.toggle("Enabled", true)));
		CATEGORIES.add(settings);
	}

	private static Module mod(String id, String name, String description, boolean enabled, Setting... settings) {
		return new Module(id, name, description, enabled, settings);
	}

	public static List<Category> categories() {
		return Collections.unmodifiableList(CATEGORIES);
	}

	public static Category active() {
		return CATEGORIES.get(activeCategory);
	}

	public static int activeIndex() {
		return activeCategory;
	}

	public static void setActive(int index) {
		activeCategory = Math.floorMod(index, CATEGORIES.size());
	}

	public static int totalModules() {
		int n = 0;

		for (Category c : CATEGORIES) {
			n += c.modules().size();
		}

		return n;
	}

	public static int totalEnabled() {
		int n = 0;

		for (Category c : CATEGORIES) {
			n += c.enabledCount();
		}

		return n;
	}

	/** Every module that matches the query (name, description or category). */
	public static List<Module> search(String query) {
		String q = query.toLowerCase().trim();
		List<Module> out = new ArrayList<>();

		if (q.isEmpty()) {
			return out;
		}

		for (Category c : CATEGORIES) {
			for (Module m : c.modules()) {
				String haystack = (m.name() + " " + m.description() + " " + c.name() + " " + c.subtitle())
						.toLowerCase();

				if (haystack.contains(q)) {
					out.add(m);
				}
			}
		}

		return out;
	}
}
