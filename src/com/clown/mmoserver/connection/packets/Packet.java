package com.clown.mmoserver.connection.packets;

public class Packet {
	public static final int LOGIN_TYPE = 1;
	public static final int LOGOUT_TYPE = 2;
	public static final int MESSAGE_TYPE = 3;
	protected final String sourceIp;
	protected final int packetType; // 0 - default. Others are to be defined and enforced by the packet handlers.
	
	protected final byte[] data;
	
	public Packet(final String sourceIp, final int packetType, final byte[] data) {
		this.sourceIp = sourceIp;
		this.packetType = packetType;
		this.data = data;
	}
	
	public String getSourceIp() {
		return sourceIp;
	}
	
	// Try to refrain from having this used.
	public byte[] getData() {
		return data;
	}
	
	public int getPacketType() {
		return packetType;
	}
	
	public int getByte(int index) {
		if (index < 0 || index >= data.length) {
			throw new IllegalArgumentException("Index must be within the bounds of 0 to data length - 1.");
		}
		return data[index] & 0xFF;
	}
	
	public int getInteger(int index) {
		if (index < 0 || index >= data.length - 4) {
			throw new IllegalArgumentException("Index must be within the bounds of 0 to data length - 5.");
		}
		return (data[index] & 0xFF) << 24 | (data[index + 1] & 0xFF) << 16 | (data[index + 2] & 0xFF) << 8 | (data[index + 3] & 0xFF);
	}
	
	public int getLong(int index) {
		if (index < 0 || index >= data.length - 8) {
			throw new IllegalArgumentException("Index must be within the bounds of 0 to data length - 9.");
		}
		return (data[index] & 0xFF) << 56 | (data[index + 1] & 0xFF) << 48 | (data[index + 2] & 0xFF) << 40 | (data[index + 3] & 0xFF) << 32 | (data[index + 4] & 0xFF) << 24 | (data[index + 5] & 0xFF) << 16 | (data[index + 6] & 0xFF) << 8 | (data[index + 7] & 0xFF);
	}
	
	public String getString(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Index must be greater than 0.");
		}
		int len = getInteger(index); // Get the length (first 4 bytes)
		if (index + len >= data.length - len) {
			throw new IllegalArgumentException("String length is larger than data length.");
		}
		char[] chars = new char[len];
		int idx = 0;
		for (int i = index + 4; i < index + 4 + len; i++) {
			chars[idx++] = (char) data[i];
		}
		return String.valueOf(chars);
	}
}
