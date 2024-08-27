package org.alvindimas05.mythickeys.fabric;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.util.Identifier;
import org.alvindimas05.mythickeys.MythicKeys;
import org.alvindimas05.mythickeys.util.MythicKeysIO;
import org.alvindimas05.mythickeys.util.MythicKeysPayload;
import org.alvindimas05.mythickeys.util.network.KeyAddData;

import java.util.Arrays;
import java.util.stream.Stream;

public class MythicKeysReceiveMessage {
    public static void init(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if(!message.getString().startsWith(MythicKeysPayload.AddKey.ID.id().toString()) &&
                !message.getString().startsWith(MythicKeysPayload.Load.ID.id().toString())
            ) return true;

            try {
                String[] messages = message.getString().split("\\|");
                if(messages[0].equals(MythicKeysPayload.AddKey.ID.id().toString())) {
                    MythicKeys.add(new KeyAddData(
                            Identifier.of(messages[1]),
                            messages[2],
                            messages[3],
                            Integer.parseInt(messages[4]),
                            messages.length > 5 ? Stream.of(messages[5].split(","))
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
