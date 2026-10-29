package com.iekei.client;

import com.iekei.IekeiClient;
import com.iekei.client.gui.ClickGuiScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.sdl.SDLScancode;

/**
 * Client entrypoint.
 *
 * <p>Registers the ClickGUI keybind ([Right Shift], rebindable in
 * Options > Controls) and opens the ClickGUI while no other screen is shown.
 * Closing is handled inside {@link ClickGuiScreen} itself, which keeps the
 * toggle reliable even while a screen is focused.
 */
public final class IekeiClientClient implements ClientModInitializer {
	/** Custom keybind category shown in Options > Controls. */
	public static final KeyMapping.Category CATEGORY =
			KeyMapping.Category.register(Identifier.fromNamespaceAndPath(IekeiClient.MOD_ID, "main"));

	/** [Right Shift] - opens the ClickGUI. */
	public static KeyMapping openGuiKey;

	/**
	 * When the GUI closes itself because [Right Shift] was pressed again, the
	 * very same key press must not re-open it on the next client tick.
	 */
	private static long suppressOpenUntil;

	@Override
	public void onInitializeClient() {
		openGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
				"key.iekeiclient.open_gui",
				InputConstants.Type.KEYBOARD,
				SDLScancode.SDL_SCANCODE_RSHIFT,
				CATEGORY));

		ClientTickEvents.END_CLIENT_TICK.register(IekeiClientClient::onEndTick);

		IekeiClient.LOGGER.info("[Iekei Client] ClickGUI bound to [Right Shift]");
	}

	private static void onEndTick(Minecraft client) {
		if (openGuiKey == null) {
			return;
		}

		// drain every queued press so holding the key never spams the GUI
		while (openGuiKey.consumeClick()) {
			if (client.gui.screen() == null && System.currentTimeMillis() >= suppressOpenUntil) {
				client.gui.setScreen(new ClickGuiScreen());
			}
		}
	}

	/** Called by the GUI when it closes itself via the toggle key. */
	public static void suppressOpenFor(long millis) {
		suppressOpenUntil = System.currentTimeMillis() + millis;
	}
}
