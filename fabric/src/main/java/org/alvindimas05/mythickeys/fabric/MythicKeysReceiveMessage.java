package org.alvindimas05.mythickeys.fabric;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.util.Identifier;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.util.MythicKeysIO;
import org.alvindimas05.mythickeys.util.MythicKeysPayload;
import org.alvindimas05.mythickeys.util.network.KeyAddData;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public class MythicKeysReceiveMessage {
    public static void init(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if(!message.getString().startsWith(MythicKeysPayload.AddKey.ID.id().toString()) &&
                !message.getString().startsWith(MythicKeysPayload.Load.ID.id().toString())
            ) return true;

            MythicKeys.LOGGER.info(message.getString());

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
                if(messages[0].equals(MythicKeysPayload.AddKey.ID.id().toString())) {
                    MythicKeys.add(new KeyAddData(
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
                 the client will load the keybinding data from the MythicKeysIO */
                if(messages[0].equals(MythicKeysPayload.Load.ID.id().toString())){
                    MythicKeysIO.load();
                }
            } catch (Exception e) {
                MythicKeys.LOGGER.error("Something is wrong when receiving message from the server:");
                e.printStackTrace();
            }

            return false;
        });
    }
}
