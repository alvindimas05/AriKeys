package org.alvindimas05.mythiccontrols.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import org.alvindimas05.mythiccontrols.MythicControls;
import org.alvindimas05.mythiccontrols.MythicControlsVoice;
import org.alvindimas05.mythiccontrols.util.MythicControlsPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class MythicControlsFabric implements ClientModInitializer {
    private boolean isVoiceControlEventRegistered = false;
    @Override
    public void onInitializeClient() {
        // Register payloads
        PayloadTypeRegistry.playC2S().register(MythicControlsPayload.Handshake.ID, MythicControlsPayload.Handshake.CODEC);
        PayloadTypeRegistry.playC2S().register(MythicControlsPayload.Key.ID, MythicControlsPayload.Key.CODEC);

        // Configure MythicControls on join and clean up on disconnect
        ClientPlayConnectionEvents.DISCONNECT.register(((handler, client) -> MythicControls.handleDisconnect()));
        ClientPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            MythicControls.handleConnect();

            if(isVoiceControlEventRegistered) return;
            MythicControlsVoice.registerEvent();
            isVoiceControlEventRegistered = true;
        });

        MythicControlsReceiveMessage.init();

        MythicControls.LOGGER.info("MythicControls initialized.");
    }
}