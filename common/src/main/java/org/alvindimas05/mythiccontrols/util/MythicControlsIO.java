package org.alvindimas05.mythiccontrols.util;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Splitter;
import com.google.common.io.Files;
import org.alvindimas05.mythiccontrols.MythicControl;
import org.alvindimas05.mythiccontrols.MythicControls;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.nbt.NbtCompound;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/* Minecrafts way of storing keybinds. Not the cleanest, but it works. */
public class MythicControlsIO {
	private static final Logger LOGGER = LogManager.getLogger();
	private static final Splitter COLON_SPLITTER = Splitter.on(':').limit(2);
	private static final File KEYBIND_FILE = new File(MinecraftClient.getInstance().runDirectory, "mythiccontrols.txt");

	public static void load() {
		try {
			if (!KEYBIND_FILE.exists()) return;

			NbtCompound nbtCompound = new NbtCompound();
			BufferedReader bufferedReader = Files.newReader(KEYBIND_FILE, Charsets.UTF_8);

			try {
				bufferedReader.lines().forEach((line) -> {
					try {
						Iterator<String> iterator = COLON_SPLITTER.split(line).iterator();
						nbtCompound.putString(iterator.next(), iterator.next());
					} catch (Exception exception) {
						LOGGER.warn("Skipping bad option: {}", line);
					}

				});
			} catch (Throwable throwable) {
				try {
					bufferedReader.close();
				} catch (Throwable throwable2) {
					throwable.addSuppressed(throwable2);
				}

				throw throwable;
			}
			bufferedReader.close();

			for (MythicControl mythicControl : MythicControls.getKeybinds()) {
				if(Objects.equals(mythicControl.getType().getPath(), "voice")) continue;

				String key = "mythiccontrol_" + mythicControl.getId().toString().replace(":", "+");

                assert mythicControl.getBoundKeyCode() != null;
                String defKey = mythicControl.getBoundKeyCode().getTranslationKey();
				String keybind = MoreObjects.firstNonNull(nbtCompound.contains(key) ? nbtCompound.getString(key) : null, defKey);

				Set<ModifierKey> modifiers = new HashSet<>(mythicControl.getModifiers());
				for (ModifierKey modifier : ModifierKey.ALL) {
					String modKey = key + "_" + modifier.getId();
					if (nbtCompound.contains(modKey)) modifiers.add(modifier);
					else modifiers.remove(modifier);
				}

				if (!defKey.equals(keybind) || !modifiers.containsAll(mythicControl.getModifiers())) {
					mythicControl.setBoundKey(InputUtil.fromTranslationKey(keybind), false);
					mythicControl.setBoundModifiers(modifiers);
				}
			}

			KeyBinding.updateKeysByCode();
		} catch (Exception exception) {
			LOGGER.error("Failed to load mythiccontrols bindings", exception);
		}
	}

	public static void save() {
		try {
			final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(KEYBIND_FILE), StandardCharsets.UTF_8));

			try {
				for (MythicControl mythicControl : MythicControls.getKeybinds()) {
					if(mythicControl.getType().getPath().equals("voice")) continue;

					printWriter.print("mythiccontrol_" + mythicControl.getId().toString().replace(":", "+"));
					printWriter.print(':');
					printWriter.println(mythicControl.getBoundKeyCode().getTranslationKey());

					for (ModifierKey modifier : mythicControl.getBoundModifiers()) {
						printWriter.print("mythiccontrol_" + mythicControl.getId().toString().replace(":", "+") + "_" + modifier.getId());
						printWriter.print(':');
						printWriter.println("true");
					}
				}
			} catch (Throwable throwable) {
				try {
					printWriter.close();
				} catch (Throwable throwable2) {
					throwable.addSuppressed(throwable2);
				}

				throw throwable;
			}

			printWriter.close();
		} catch (Exception exception) {
			LOGGER.error("Failed to save mythiccontrols bindings", exception);
		}
	}
}
