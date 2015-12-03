package com.clown.mmoserver.connection.packets;

import java.io.IOException;
import java.io.InputStream;

import com.clown.util.BinaryOperations;

public final class PacketFactory {
	
	public static Packet readPacket(final String sourceIp, final InputStream in) throws IOException {
		byte[] bytes = new byte[4];
		if (in.read(bytes) != bytes.length) {
			throw new IOException("Failed to read packet type from input stream.");
		}
		int packetType = BinaryOperations.bytesToInteger(bytes);
		if (in.read(bytes) != bytes.length) {
			throw new IOException("Failed to read length bytes from input stream.");
		}
		bytes = new byte[BinaryOperations.bytesToInteger(bytes)];
		if (in.read(bytes) != bytes.length) {
			throw new IOException("Failed to completely read data from input stream.");
		}
		switch (packetType) {
		case Packet.LOGIN_TYPE:
			return new LoginPacket(sourceIp, packetType, bytes);
		case Packet.MESSAGE_TYPE:
			return new MessagePacket(sourceIp, packetType, bytes);
		case Packet.LOGOUT_TYPE: // Probably don't need to create a special object for logout. It probably doesn't contain any more info other than that it's a logout packet.
		default:
			return new Packet(sourceIp, packetType, bytes);
		}
	}
	
	public static Packet buildLoginPacket(String username, String password) {
		byte[] bytes = new byte[username.length() + password.length() + 8];
		int idx = 0;
		for (byte b : BinaryOperations.toBytes(username.length())) {
			bytes[idx++] = b;
		}
		for (byte b : username.getBytes()) {
			bytes[idx++] = b;
		}
		for (byte b : BinaryOperations.toBytes(password.length())) {
			bytes[idx++] = b;
		}
		for (byte b : password.getBytes()) {
			bytes[idx++] = b;
		}
		return new LoginPacket("", Packet.LOGIN_TYPE, bytes);
	}
	
	public static Packet buildMessagePacket(String message) {
		byte[] bytes = new byte[message.length() + 4];
		int idx = 0;
		for (byte b : BinaryOperations.toBytes(message.length())) {
			bytes[idx++] = b;
		}
		for (byte b : message.getBytes()) {
			bytes[idx++] = b;
		}
		return new MessagePacket("", Packet.MESSAGE_TYPE, bytes);
	}
}
