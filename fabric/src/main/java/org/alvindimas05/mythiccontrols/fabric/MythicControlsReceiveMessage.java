package org.alvindimas05.mythiccontrols.fabric;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.util.Identifier;
import org.alvindimas05.mythiccontrols.MythicControls;
import org.alvindimas05.mythiccontrols.util.MythicControlsIO;
import org.alvindimas05.mythiccontrols.util.MythicControlsPayload;
import org.alvindimas05.mythiccontrols.util.network.KeyAddData;

import java.util.Objects;
import java.util.stream.Stream;

public class MythicControlsReceiveMessage {
    public static void init(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if(!message.getString().startsWith(MythicControlsPayload.AddKey.ID.id().toString()) &&
                !message.getString().startsWith(MythicControlsPayload.Load.ID.id().toString())
            ) return true;

            MythicControls.LOGGER.info(message.getString());

            // 0 = channel name (ex. mythiccontrol:addkey)
            // 1 = type name (ex. type:key or type:voice)
            // 2 = control id (ex. mythiccontrol:examplekey)
            // 3 = control name (ex. Example Key)
            // 4 = category name (ex. Example Category)
            // 5 = key code (nullable if type:voice) (ex. 74 means J key)
            // 6 = voice word (nullable if type:key) (ex. "example")
            // 7 = key modifiers (ex. left alt or left shift in key code)

            try {
                String[] messages = message.getString().split("\\|");
                if(messages[0].equals(MythicControlsPayload.AddKey.ID.id().toString())) {
                    MythicControls.add(new KeyAddData(
                            Identifier.of(messages[1]),
                            Identifier.of(messages[2]),
                            messages[3],
                            messages[4],
                            !Objects.equals(messages[5], "null") ? Integer.parseInt(messages[5]) : null,
                            !Objects.equals(messages[6], "null") ? messages[6] : null,
                            messages.length > 7 ? Stream.of(messages[7].split(","))
                                    .mapToInt(Integer::parseInt)
                                    .toArray() : new int[0]
                    ));
                }


                /* When the server finish sending keybinds it will send the load message and
                 the client will load the keybinding data from the MythicControlsIO */
                if(messages[0].equals(MythicControlsPayload.Load.ID.id().toString())){
                    MythicControlsIO.load();
                }
            } catch (Exception e) {
                MythicControls.LOGGER.error("Something is wrong when receiving message from the server:");
                e.printStackTrace();
            }

            return false;
        });
    }
}
