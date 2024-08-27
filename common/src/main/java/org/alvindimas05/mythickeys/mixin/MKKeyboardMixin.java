package org.alvindimas05.mythickeys.mixin;

import org.alvindimas05.mythickeys.MythicKey;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.MythicKeysPlatform;
import org.alvindimas05.mythickeys.util.network.KeyPressData;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(KeyBinding.class)
public class MKKeyboardMixin {
	@Unique
	private static final List<InputUtil.Key> mythickeys$pressedKeys = new ArrayList<>();

	@Inject(method = "setKeyPressed", at = @At("HEAD"))
	private static void input(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
		// Only check for keybinds while outside a GUI
		if (MinecraftClient.getInstance().currentScreen != null) return;

		KeyBinding keyBinding = MythicKeysPlatform.getKeyBinding(key);
		if (keyBinding != null) {
			Identifier id = MythicKeys.cleanIdentifier(keyBinding.getTranslationKey());
			if (MythicKeys.getVanillaKeys().contains(id)) mythickeys$registerPress(id, key, pressed);
		}

		for (MythicKey mythicKey : MythicKeys.getModifierSortedKeybinds())
			if (key.equals(mythicKey.getBoundKeyCode()) && mythicKey.testModifiers()) mythickeys$registerPress(mythicKey.getId(), key, pressed);
	}

	@Unique
	private static void mythickeys$registerPress(Identifier id, InputUtil.Key key, boolean pressed) {
		// Check if the button was pressed or released
		if (pressed) {
			boolean held = mythickeys$pressedKeys.contains(key);
			// Check if it is already being pressed
			if (!held) {
				// Add it to the list of currently pressed keys
				mythickeys$pressedKeys.add(key);
				mythickeys$sendPacket(id, false);
			}
		} else {
			// Remove it from the list of currently pressed keys
			mythickeys$pressedKeys.remove(key);
			mythickeys$sendPacket(id, true);
		}
	}

	@Unique
	private static void mythickeys$sendPacket(Identifier id, boolean release) {
		// Call the platform specific packet sending code
		MythicKeysPlatform.sendKey(new KeyPressData(id, release));
	}
}
