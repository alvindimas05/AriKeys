package org.alvindimas05.mythickeys.screen;

import org.alvindimas05.mythickeys.MythicKey;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.util.MythicKeysIO;
import org.alvindimas05.mythickeys.util.ModifierKey;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.HashSet;

public class MythicKeysOptions extends GameOptionsScreen {
	public MythicKey focusedMKey;
	private MythicKeyControlsListWidget keyBindingListWidget;
	private ButtonWidget resetButton;

	public MythicKeysOptions(Screen parent, GameOptions options) {
		super(parent, options, Text.translatable("mythickeys.controls.title"));
	}

	protected void addOptions(){

	}

	protected void init() {
		if (client == null) return;
		this.keyBindingListWidget = new MythicKeyControlsListWidget(this, this.client);
		this.addSelectableChild(this.keyBindingListWidget);
		this.resetButton = this.addDrawableChild(ButtonWidget.builder(Text.translatable("controls.resetAll"), (button) -> {
			for (MythicKey keyBinding : MythicKeys.getKeybinds()) {
				keyBinding.setBoundKey(keyBinding.getKeyCode(), false);
				keyBinding.resetBoundModifiers();
			}
			KeyBinding.updateKeysByCode();
		}).dimensions(this.width / 2 - 155, this.height - 29, 150, 20).build());


		this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> this.client.setScreen(this.parent))
				.dimensions(this.width / 2 - 155 + 160, this.height - 29, 150, 20).build());
	}

	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (this.focusedMKey != null) {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				focusedMKey.setBoundKey(InputUtil.UNKNOWN_KEY, false);
				focusedMKey.setBoundModifiers(new HashSet<>());
			} else if (isModifier(keyCode)) return super.keyPressed(keyCode, scanCode, modifiers);
			else focusedMKey.setBoundKey(InputUtil.fromKeyCode(keyCode, scanCode), true);
			MythicKeysIO.save();

			this.focusedMKey = null;
			KeyBinding.updateKeysByCode();
			return true;
		} else {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	private boolean isModifier(int code) {
		for (ModifierKey modifier : ModifierKey.ALL)
			if (modifier.getCode() == code) return true;
		return false;
	}

	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.focusedMKey != null) {
			focusedMKey.setBoundKey(InputUtil.Type.MOUSE.createFromCode(button), true);
			MythicKeysIO.save();

			this.focusedMKey = null;
			KeyBinding.updateKeysByCode();
			return true;
		} else {
			return super.mouseClicked(mouseX, mouseY, button);
		}
	}

	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

//		this.renderBackground(context, mouseX, mouseY, delta);
		this.keyBindingListWidget.render(context, mouseX, mouseY, delta);
		context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 8, 0xFFFFFF);
		boolean canReset = false;

		for (MythicKey mythicKey : MythicKeys.getKeybinds()) {
			if (mythicKey.hasChanged()) {
				canReset = true;
				break;
			}
		}

		this.resetButton.active = canReset;
	}
}
