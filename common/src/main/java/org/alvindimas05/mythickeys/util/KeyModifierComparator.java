package org.alvindimas05.mythickeys.util;

import org.alvindimas05.mythickeys.MythicKey;

import java.util.Comparator;

public class KeyModifierComparator implements Comparator<MythicKey> {
	/* Compare Keybinds based on their modifier count */
	public int compare(MythicKey key1, MythicKey key2) {
		return Integer.compare(key1.getBoundModifiers().size(), key2.getBoundModifiers().size());
	}
}
