package org.alvindimas05.mythickeys.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.util.MythicKeysPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class MythicKeysFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Register payloads
        PayloadTypeRegistry.playC2S().register(MythicKeysPayload.Handshake.ID, MythicKeysPayload.Handshake.CODEC);
        PayloadTypeRegistry.playC2S().register(MythicKeysPayload.Key.ID, MythicKeysPayload.Key.CODEC);

        // Configure MythicKeys on join and clean up on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> MythicKeys.handleDisconnect()));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, server) -> MythicKeys.handleConnect());

        MythicKeys.LOGGER.info("MythicKeys initialized.");

        MythicKeysReceiveMessage.init();
    }
}