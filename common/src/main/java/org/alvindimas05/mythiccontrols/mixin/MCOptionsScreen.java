package org.alvindimas05.mythiccontrols.mixin;

import org.alvindimas05.mythiccontrols.MythicControls;
import org.alvindimas05.mythiccontrols.screen.MythicControlsButton;
import org.alvindimas05.mythiccontrols.screen.MythicControlsOptions;
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
public class MCOptionsScreen extends Screen {
	@Shadow
	@Final
	private GameOptions settings;

	protected MCOptionsScreen(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"))
	protected void initMythicControlsButton(CallbackInfo ci) {
		if (client == null || client.isInSingleplayer() || MythicControls.getKeybinds().isEmpty()) return;
		// Add the mythiccontrols button widget
		this.addDrawableChild(new MythicControlsButton(this.width / 2 + 158, this.height / 6 + 72 - 6,
				(button) -> this.client.setScreen(new MythicControlsOptions(this, this.settings))));
	}

}
