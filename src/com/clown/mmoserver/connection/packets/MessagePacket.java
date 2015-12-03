package com.clown.mmoserver.connection.packets;

public final class MessagePacket extends Packet {
	private final String message;
	public MessagePacket(String sourceIp, int packetType, byte[] data) {
		super(sourceIp, packetType, data);
		if (packetType != Packet.MESSAGE_TYPE) {
			throw new RuntimeException("Invalid type for message packet.");
		}
		message = getString(0); // Should be the only data type.
	}
	
	public String getMessage() {
		return message;
	}
}
