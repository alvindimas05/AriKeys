package org.alvindimas05.mythiccontrols.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

@Getter
@RequiredArgsConstructor
public class KeyAddData {
	private final Identifier type;
	private final Identifier id;
	private final String name, category;
	@Nullable
	private final Integer defKey;
	@Nullable
	private final String voice;
	private final int[] modifiers;
}
