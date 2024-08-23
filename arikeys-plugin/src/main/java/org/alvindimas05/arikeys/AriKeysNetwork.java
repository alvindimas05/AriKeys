package org.alvindimas05.arikeys;

import org.alvindimas05.arikeys.api.AriKeyPressEvent;
import org.alvindimas05.arikeys.api.AriKeyReleaseEvent;
import org.alvindimas05.arikeys.config.AriKeyInfo;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AriKeysNetwork {
	public static final short DEFAULT_MAX_STRING_LENGTH = Short.MAX_VALUE;

	public static void receiveKeyPress(Player player, byte[] message) {
        // Read the key press ID then call the AriKeyPress event.
        String[] messages = new String(message, StandardCharsets.UTF_8).split("\\|");
        Bukkit.getLogger().info("Received key event: " + Arrays.toString(messages));
        boolean firstPress = messages[1].equals("0");

        NamespacedKey id = NamespacedKey.fromString(messages[0].trim());

        if (AriKeysPlugin.get().getConf().getKeyInfoList().containsKey(id)) {
            AriKeyInfo info = AriKeysPlugin.get().getConf().getKeyInfoList().get(id);
            boolean eventCmd = AriKeysPlugin.get().getConf().isEventOnCommand();

            if (firstPress) {
                if (!info.runCommand(player) || eventCmd) Bukkit.getPluginManager().callEvent(new AriKeyPressEvent(player, id, true));
                if (info.hasMM(true)) info.mmSkill(player, true);
                return;
            }

            if (!info.hasCommand() || eventCmd) Bukkit.getPluginManager().callEvent(new AriKeyReleaseEvent(player, id, true));
            if (info.hasMM(false)) info.mmSkill(player, false);
        } else {
            Bukkit.getPluginManager()
                    .callEvent(firstPress ? new AriKeyPressEvent(player, id, false) : new AriKeyReleaseEvent(player, id, false));
        }
    }

	public static void receiveGreeting(Player player, byte[] message) {
		/* Send this server's specified keybindings to the
		 client. This is delayed to make sure the client is properly
		 connected before attempting to send any data over. */
		for (AriKeyInfo info : AriKeysPlugin.get().getConf().getKeyInfoList().values())
			sendKeyInformation(player, info.getId(), info.getDef(), info.getName(), info.getCategory(), info.getModifiers());

		player.sendMessage(AriKeysChannels.LOAD_KEYS);
	}

	// Fk you buffer, makes my life harder
	// Seperate with uncommon symbol "|"
	public static void sendKeyInformation(Player player, NamespacedKey id, int def, String name, String category, Set<ModifierKey> modifiers) {
		int[] modArray = modifiers.stream().mapToInt(ModifierKey::getId).toArray();
		String message = AriKeysChannels.ADD_KEY + "|" + id.getNamespace() + ":" + id.getKey() + "|"
				+ name + "|" + category + "|" + def + "|" + IntStream.of(modArray).mapToObj(Integer::toString)
				.collect(Collectors.joining(","));
		player.sendMessage(message);
	}
}
