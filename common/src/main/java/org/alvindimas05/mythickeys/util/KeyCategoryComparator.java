package org.alvindimas05.mythickeys.util;

import org.alvindimas05.mythickeys.MythicKey;

import java.util.Comparator;

public class KeyCategoryComparator implements Comparator<MythicKey> {
	/* Compare Keybinds based on their category */
	public int compare(MythicKey key1, MythicKey key2) {
		int id = key1.getId().compareTo(key2.getId());
		int category = key1.getCategory().compareTo(key2.getCategory());
		return Integer.compare(category, id);
	}
}
