package org.alvindimas05.mythiccontrols;

import net.minecraft.client.MinecraftClient;
import org.alvindimas05.mythiccontrols.util.network.KeyPressData;
import org.modogthedev.api.VoiceLibApi;

import java.util.Objects;

public class MythicControlsVoice {
    public static void registerEvent(){
        try {
            VoiceLibApi.registerClientSpeechListener((consumer) -> {
                // Only check for voices while outside a GUI
                if (MinecraftClient.getInstance().currentScreen != null) return;

                for (MythicControl mythicControl : MythicControls.getModifierSortedKeybinds())
                    if (Objects.equals(mythicControl.getType().getPath(), "voice"))
                        MythicControlsPlatform.sendKey(new KeyPressData(mythicControl.getId(), true));
            });
        } catch (NoClassDefFoundError e){
            MythicControls.LOGGER.warn("Can't find VoiwceLibApi! Disabling voice control.");
        }
    }
}
