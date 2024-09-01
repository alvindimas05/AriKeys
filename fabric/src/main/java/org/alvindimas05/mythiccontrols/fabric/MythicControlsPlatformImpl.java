package org.alvindimas05.mythiccontrols.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.alvindimas05.mythiccontrols.MythicControls;
import org.alvindimas05.mythiccontrols.fabric.mixin.MCKeyboardFabricMixin;
import org.alvindimas05.mythiccontrols.util.MythicControlsPayload;
import org.alvindimas05.mythiccontrols.util.network.KeyPressData;

public class MythicControlsPlatformImpl {
    public static void sendHandshake() {
        MythicControls.LOGGER.info("Sending handshake to the server...");
        ClientPlayNetworking.send(new MythicControlsPayload.Handshake("Hello"));
    }

    public static KeyBinding getKeyBinding(InputUtil.Key code) {
        return MCKeyboardFabricMixin.getKeyBindings().get(code);
    }

    public static void sendKey(KeyPressData data) {
        ClientPlayNetworking.send(new MythicControlsPayload.Key(
            data.id.toString() + "|" + (data.release ? "1" : "0")
        ));
    }
}