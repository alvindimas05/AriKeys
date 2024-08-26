package org.alvindimas05.arikeys.fabric;

import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.util.Identifier;
import org.alvindimas05.arikeys.AriKeys;
import org.alvindimas05.arikeys.util.AriKeysIO;
import org.alvindimas05.arikeys.util.AriKeysPayload;
import org.alvindimas05.arikeys.util.network.KeyAddData;

import java.util.Arrays;
import java.util.stream.Stream;

public class AriKeysReceiveMessage {
    public static void init(){
        ClientReceiveMessageEvents.ALLOW_GAME.register((message, overlay) -> {
            if(!message.getString().startsWith(AriKeysPayload.AddKey.ID.id().toString()) &&
                !message.getString().startsWith(AriKeysPayload.Load.ID.id().toString())
            ) return true;

            try {
                String[] messages = message.getString().split("\\|");
                if(messages[0].equals(AriKeysPayload.AddKey.ID.id().toString())) {
                    AriKeys.add(new KeyAddData(
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
                 the client will load the keybinding data from the AriKeysIO */
                if(messages[0].equals(AriKeysPayload.Load.ID.id().toString())){
                    AriKeysIO.load();
                }
            } catch (Exception e) {
                AriKeys.LOGGER.error("Something is wrong when receiving message from the server:");
                e.printStackTrace();
            }

            return false;
        });
    }
}
