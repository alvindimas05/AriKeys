package org.alvindimas05.mythiccontrols.util;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public interface MythicControlsPayload {
	record Handshake(String value) implements CustomPayload {
		public static final Id<Handshake> ID = new Id<>(Identifier.of("mythiccontrol:greeting"));
		public static final PacketCodec<RegistryByteBuf, Handshake> CODEC = PacketCodecs.STRING
				.xmap(Handshake::new, Handshake::value).cast();

		@Override
		public Id<Handshake> getId() {
			return ID;
		}
	}
	record AddKey(String value) implements CustomPayload {
		public static final Id<AddKey> ID = new Id<>(Identifier.of("mythiccontrol:addkey"));
		public static final PacketCodec<RegistryByteBuf, AddKey> CODEC = PacketCodecs.STRING
				.xmap(AddKey::new, AddKey::value).cast();

		@Override
		public Id<AddKey> getId() {
			return ID;
		}
	}
	record Load(byte[] value) implements CustomPayload {
		public static final Id<Load> ID = new Id<>(Identifier.of("mythiccontrol:load"));
		public static final PacketCodec<RegistryByteBuf, Load> CODEC = PacketCodecs.BYTE_ARRAY
				.xmap(Load::new, Load::value).cast();

		@Override
		public Id<Load> getId() {
			return ID;
		}
	}
	record Key(String value) implements CustomPayload {
		public static final Id<Key> ID = new Id<>(Identifier.of("mythiccontrol:keybind"));
		public static final PacketCodec<RegistryByteBuf, Key> CODEC = PacketCodecs.STRING
				.xmap(Key::new, Key::value).cast();

		@Override
		public Id<Key> getId() {
			return ID;
		}
	}
}
