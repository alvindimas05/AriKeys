package org.alvindimas05.mythickeys.fabric;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.fabric.mixin.MKKeyboardFabricMixin;
import org.alvindimas05.mythickeys.util.MythicKeysPayload;
import org.alvindimas05.mythickeys.util.network.KeyPressData;

public class MythicKeysPlatformImpl {
    public static void sendHandshake() {
        MythicKeys.LOGGER.info("Sending handshake to the server...");
        ClientPlayNetworking.send(new MythicKeysPayload.Handshake("Hello"));
    }

    public static KeyBinding getKeyBinding(InputUtil.Key code) {
        return MKKeyboardFabricMixin.getKeyBindings().get(code);
    }

    public static void sendKey(KeyPressData data) {
        ClientPlayNetworking.send(new MythicKeysPayload.Key(
            data.id.toString() + "|" + (data.release ? "1" : "0")
        ));
    }
}