package com.clown.mmoserver.connection.packets;

public final class LoginPacket extends Packet {
	private final String username;
	private final String password;
	
	public LoginPacket(String sourceIp, int packetType, byte[] data) {
		super(sourceIp, packetType, data);
		if (packetType != Packet.LOGIN_TYPE) {
			throw new RuntimeException("Invalid packet type for login packet.");
		}
		int indexOfPassword = getInteger(0); // Length of first string.
		username = getString(0);
		password = getString(indexOfPassword);
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
