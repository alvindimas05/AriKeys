package org.alvindimas05.arikeys.util.network;

import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@RequiredArgsConstructor
public class KeyPressData {
	public final Identifier id;
	public final boolean release;
}
