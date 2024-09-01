package org.alvindimas05.mythiccontrols.mixin;

import org.alvindimas05.mythiccontrols.MythicControl;
import org.alvindimas05.mythiccontrols.MythicControls;
import org.alvindimas05.mythiccontrols.MythicControlsPlatform;
import org.alvindimas05.mythiccontrols.util.network.KeyPressData;
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
import java.util.Objects;

@Mixin(KeyBinding.class)
public class MCKeyboardMixin {
	@Unique
	private static final List<InputUtil.Key> mythiccontrols$pressedKeys = new ArrayList<>();

	@Inject(method = "setKeyPressed", at = @At("HEAD"))
	private static void input(InputUtil.Key key, boolean pressed, CallbackInfo ci) {
		// Only check for keybinds while outside a GUI
		if (MinecraftClient.getInstance().currentScreen != null) return;

		KeyBinding keyBinding = MythicControlsPlatform.getKeyBinding(key);
		if (keyBinding != null) {
			Identifier id = MythicControls.cleanIdentifier(keyBinding.getTranslationKey());
			if (MythicControls.getVanillaKeys().contains(id)) mythiccontrols$registerPress(id, key, pressed);
		}

		for (MythicControl mythicControl : MythicControls.getModifierSortedKeybinds())
			if (!Objects.equals(mythicControl.getType().getPath(), "voice") && key.equals(mythicControl.getBoundKeyCode())
					&& mythicControl.testModifiers()) mythiccontrols$registerPress(mythicControl.getId(), key, pressed);
	}

	@Unique
	private static void mythiccontrols$registerPress(Identifier id, InputUtil.Key key, boolean pressed) {
		// Check if the button was pressed or released
		if (pressed) {
			boolean held = mythiccontrols$pressedKeys.contains(key);
			// Check if it is already being pressed
			if (!held) {
				// Add it to the list of currently pressed keys
				mythiccontrols$pressedKeys.add(key);
				mythiccontrols$sendPacket(id, false);
			}
		} else {
			// Remove it from the list of currently pressed keys
			mythiccontrols$pressedKeys.remove(key);
			mythiccontrols$sendPacket(id, true);
		}
	}

	@Unique
	private static void mythiccontrols$sendPacket(Identifier id, boolean release) {
		// Call the platform specific packet sending code
		MythicControlsPlatform.sendKey(new KeyPressData(id, release));
	}
}
