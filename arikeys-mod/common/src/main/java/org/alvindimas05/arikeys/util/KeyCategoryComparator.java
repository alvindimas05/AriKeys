package org.alvindimas05.arikeys.util;

import org.alvindimas05.arikeys.AriKey;

import java.util.Comparator;

public class KeyCategoryComparator implements Comparator<AriKey> {
	/* Compare Keybinds based on their category */
	public int compare(AriKey key1, AriKey key2) {
		int id = key1.getId().compareTo(key2.getId());
		int category = key1.getCategory().compareTo(key2.getCategory());
		return Integer.compare(category, id);
	}
}
