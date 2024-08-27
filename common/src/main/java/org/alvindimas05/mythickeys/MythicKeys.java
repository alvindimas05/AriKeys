package org.alvindimas05.mythickeys;

import org.alvindimas05.mythickeys.util.MythicKeysIO;
import org.alvindimas05.mythickeys.util.KeyCategoryComparator;
import org.alvindimas05.mythickeys.util.KeyModifierComparator;
import org.alvindimas05.mythickeys.util.network.KeyAddData;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MythicKeys {
	public static final String MOD_ID = "mythickeys";
	public static final Logger LOGGER = LoggerFactory.getLogger(MythicKeys.MOD_ID);

	private static final Map<Identifier, MythicKey> CUSTOM_KEYS = new HashMap<>();
	private static final Set<Identifier> VANILLA_KEYS = new HashSet<>();

	private static final Comparator<MythicKey> CATEGORY_COMPARATOR = new KeyCategoryComparator(),
			MODIFIER_COMPARATOR = new KeyModifierComparator().reversed();

	public static Collection<MythicKey> getKeybinds() {
		return CUSTOM_KEYS.values();
	}

	public static void handleConnect() {
		// Clean up, then perform handshake protocol
		MythicKeys.clear();
		MythicKeysPlatform.sendHandshake();
	}

	public static void handleDisconnect() {
		// Clean up after disconnection
		MythicKeysIO.save();
		MythicKeys.clear();
	}

	/* Custom sorting rules as running Collections.sort()
	 will cause a crash, since these keybinds aren't
	 registered the usual way. */
	public static List<MythicKey> getCategorySortedKeybinds() {
		List<MythicKey> set = new ArrayList<>(CUSTOM_KEYS.values());
		set.sort(CATEGORY_COMPARATOR);
		return set;
	}

	public static List<MythicKey> getModifierSortedKeybinds() {
		List<MythicKey> set = new ArrayList<>(CUSTOM_KEYS.values());
		set.sort(MODIFIER_COMPARATOR);
		return set;
	}

	public static Set<Identifier> getVanillaKeys() {
		return VANILLA_KEYS;
	}

	public static Identifier cleanIdentifier(String key) {
		return Identifier.of(Identifier.DEFAULT_NAMESPACE, key.replace("key.", "").replace(".", "").toLowerCase());
	}

	public static void clear() {
		VANILLA_KEYS.clear();
		CUSTOM_KEYS.clear();
	}

	public static void add(KeyAddData key) {
		Identifier id = key.getId();
		if (id.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) VANILLA_KEYS.add(id);
		else CUSTOM_KEYS.put(id, new MythicKey(key));
	}
}
