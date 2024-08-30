package org.alvindimas05.mythickeys;

import org.alvindimas05.mythickeys.util.ModifierKey;
import org.alvindimas05.mythickeys.util.network.KeyAddData;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

@Getter
public class MythicKey {
	private final Identifier type;
	private final Identifier id;
	private final String name, category;
	@Nullable
	private final InputUtil.Key keyCode;
	@Nullable
	private InputUtil.Key boundKeyCode;
	private final String voice;
	private final Set<ModifierKey> modifiers;
	private Set<ModifierKey> boundModifiers;

	public MythicKey(KeyAddData data) {
		this(data.getType(), data.getId(), data.getName(), data.getCategory(),
				data.getDefKey() != null ? InputUtil.Type.KEYSYM.createFromCode(data.getDefKey()) : null,
				data.getVoice(), data.getModifiers());
	}

	public MythicKey(Identifier type, Identifier id, String name, String category,
					 @Nullable InputUtil.Key keyCode, String voice, int[] modifiers) {
		this.type = type;
		this.id = id;
		this.name = name;
		this.category = category;
		this.keyCode = keyCode;
		this.boundKeyCode = keyCode;
		this.voice = voice;
		this.modifiers = ModifierKey.getFromArray(modifiers);
		this.boundModifiers = new HashSet<>(this.modifiers);
	}

	public void setBoundKey(InputUtil.Key key, boolean handleModifiers) {
		if (handleModifiers) {
			Set<ModifierKey> mods = new HashSet<>();
			for (ModifierKey modifier : ModifierKey.ALL)
				if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), modifier.getCode())) mods.add(modifier);
			setBoundModifiers(mods);
		}
		this.boundKeyCode = key;
	}

	public void setBoundModifiers(Set<ModifierKey> modifiers) {
		this.boundModifiers = modifiers;
	}

	public void resetBoundModifiers() {
		setBoundModifiers(new HashSet<>(this.modifiers));
	}

	public boolean hasChanged() {
		return keyCode != null && (!keyCode.equals(boundKeyCode) || !testModifiers(this.modifiers));
	}

	public boolean isUnbound() {
		return boundKeyCode != null && boundKeyCode.equals(InputUtil.UNKNOWN_KEY);
	}

	public boolean testModifiers() {
		for (ModifierKey key : boundModifiers)
			if (!InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), key.getCode())) return false;
		return true;
	}

	public boolean testModifiers(Set<ModifierKey> otherKeys) {
		return boundModifiers.containsAll(otherKeys) && otherKeys.containsAll(boundModifiers);
	}
}
