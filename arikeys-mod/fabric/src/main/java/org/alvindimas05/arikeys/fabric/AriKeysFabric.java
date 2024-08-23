package org.alvindimas05.arikeys.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.alvindimas05.arikeys.AriKeys;
import org.alvindimas05.arikeys.util.AriKeysPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class AriKeysFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register payloads
        PayloadTypeRegistry.playC2S().register(AriKeysPayload.Handshake.ID, AriKeysPayload.Handshake.CODEC);
        PayloadTypeRegistry.playC2S().register(AriKeysPayload.Key.ID, AriKeysPayload.Key.CODEC);

        // Configure AriKeys on join and clean up on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> AriKeys.handleDisconnect()));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, server) -> AriKeys.handleConnect());

        AriKeys.LOGGER.info("AriKeys initialized.");

        AriKeysReceiveMessage.init();
    }
}