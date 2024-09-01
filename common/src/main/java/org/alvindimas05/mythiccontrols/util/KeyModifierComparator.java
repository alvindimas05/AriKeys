package org.alvindimas05.mythiccontrols.util;

import org.alvindimas05.mythiccontrols.MythicControl;

import java.util.Comparator;

public class KeyModifierComparator implements Comparator<MythicControl> {
	/* Compare Keybinds based on their modifier count */
	public int compare(MythicControl key1, MythicControl key2) {
		return Integer.compare(key1.getBoundModifiers().size(), key2.getBoundModifiers().size());
	}
}
