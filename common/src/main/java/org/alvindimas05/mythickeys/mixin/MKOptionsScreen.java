package org.alvindimas05.mythickeys.mixin;

import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.screen.MythicKeysButton;
import org.alvindimas05.mythickeys.screen.MythicKeysOptions;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class MKOptionsScreen extends Screen {
	@Shadow
	@Final
	private GameOptions settings;

	protected MKOptionsScreen(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	protected void initMythicKeysButton(CallbackInfo ci) {
		if (client == null || client.isInSingleplayer() || MythicKeys.getKeybinds().isEmpty()) return;
		// Add the mythickeys button widget
		this.addDrawableChild(new MythicKeysButton(this.width / 2 + 158, this.height / 6 + 72 - 6,
				(button) -> this.client.setScreen(new MythicKeysOptions(this, this.settings))));
	}

}
