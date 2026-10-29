package com.iekei;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Iekei Client - common (client + server) entrypoint.
 *
 * <p>The whole client is client-side only: see
 * {@link com.iekei.client.IekeiClientClient} for the ClickGUI bootstrap.
 */
public class IekeiClient implements ModInitializer {
	public static final String MOD_ID = "iekeiclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("[Iekei Client] common initializer ready");
	}
}
