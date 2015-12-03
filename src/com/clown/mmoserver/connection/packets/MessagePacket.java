package com.clown.mmoserver.connection.packets;

public class MessagePacket extends Packet {

	public MessagePacket(String sourceIp, int packetType, byte[] data) {
		super(sourceIp, packetType, data);
		if (packetType != Packet.MESSAGE_TYPE) {
			throw new RuntimeException("Invalid type for message packet.");
		}
	}
}
