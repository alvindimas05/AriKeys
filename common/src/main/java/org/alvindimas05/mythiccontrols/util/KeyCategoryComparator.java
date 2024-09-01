package org.alvindimas05.mythiccontrols.util;

import org.alvindimas05.mythiccontrols.MythicControl;

import java.util.Comparator;

public class KeyCategoryComparator implements Comparator<MythicControl> {
	/* Compare Keybinds based on their category */
	public int compare(MythicControl key1, MythicControl key2) {
		int id = key1.getId().compareTo(key2.getId());
		int category = key1.getCategory().compareTo(key2.getCategory());
		return Integer.compare(category, id);
	}
}
