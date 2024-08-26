package org.alvindimas05.arikeys;

import org.alvindimas05.arikeys.util.AriKeysIO;
import org.alvindimas05.arikeys.util.KeyCategoryComparator;
import org.alvindimas05.arikeys.util.KeyModifierComparator;
import org.alvindimas05.arikeys.util.network.KeyAddData;
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

public final class AriKeys {
	public static final String MOD_ID = "arikeys";
	public static final Logger LOGGER = LoggerFactory.getLogger(AriKeys.MOD_ID);

	private static final Map<Identifier, AriKey> CUSTOM_KEYS = new HashMap<>();
	private static final Set<Identifier> VANILLA_KEYS = new HashSet<>();

	private static final Comparator<AriKey> CATEGORY_COMPARATOR = new KeyCategoryComparator(),
			MODIFIER_COMPARATOR = new KeyModifierComparator().reversed();

	public static Collection<AriKey> getKeybinds() {
		return CUSTOM_KEYS.values();
	}

	public static void handleConnect() {
		// Clean up, then perform handshake protocol
		AriKeys.clear();
		AriKeysPlatform.sendHandshake();
	}

	public static void handleDisconnect() {
		// Clean up after disconnection
		AriKeysIO.save();
		AriKeys.clear();
	}

	/* Custom sorting rules as running Collections.sort()
	 will cause a crash, since these keybinds aren't
	 registered the usual way. */
	public static List<AriKey> getCategorySortedKeybinds() {
		List<AriKey> set = new ArrayList<>(CUSTOM_KEYS.values());
		set.sort(CATEGORY_COMPARATOR);
		return set;
	}

	public static List<AriKey> getModifierSortedKeybinds() {
		List<AriKey> set = new ArrayList<>(CUSTOM_KEYS.values());
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
		else CUSTOM_KEYS.put(id, new AriKey(key));
	}
}
