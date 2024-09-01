package org.alvindimas05.mythiccontrols;

import dev.architectury.injectables.annotations.ExpectPlatform;
import org.alvindimas05.mythiccontrols.util.network.KeyPressData;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class MythicControlsPlatform {
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
