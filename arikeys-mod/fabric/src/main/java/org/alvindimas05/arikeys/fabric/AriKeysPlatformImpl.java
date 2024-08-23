package org.alvindimas05.arikeys.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.alvindimas05.arikeys.AriKeys;
import org.alvindimas05.arikeys.fabric.mixin.AKKeyboardFabricMixin;
import org.alvindimas05.arikeys.util.AriKeysPayload;
import org.alvindimas05.arikeys.util.network.KeyPressData;

public class AriKeysPlatformImpl {
    public static void sendHandshake() {
        AriKeys.LOGGER.info("Sending handshake to the server...");
        ClientPlayNetworking.send(new AriKeysPayload.Handshake("Hello"));
    }

    public static KeyBinding getKeyBinding(InputUtil.Key code) {
        return AKKeyboardFabricMixin.getKeyBindings().get(code);
    }

    public static void sendKey(KeyPressData data) {
        ClientPlayNetworking.send(new AriKeysPayload.Key(
            data.id.toString() + "|" + (data.release ? "1" : "0")
        ));
    }
}