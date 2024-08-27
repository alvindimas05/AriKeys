package org.alvindimas05.mythickeys;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.alvindimas05.mythickeys.util.network.KeyPressData;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class MythicKeysPlatform {
	@ExpectPlatform
	public static void sendHandshake() {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static void sendKey(KeyPressData data) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static KeyBinding getKeyBinding(InputUtil.Key code) {
		throw new AssertionError();
	}
}
