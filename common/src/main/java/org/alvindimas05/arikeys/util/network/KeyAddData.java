package org.alvindimas05.arikeys.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

@Getter
@RequiredArgsConstructor
public class KeyAddData {
	private final Identifier id;
	private final String name, category;
	private final int defKey;
	private final int[] modifiers;
}
