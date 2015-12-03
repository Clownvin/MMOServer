package com.clown.mmoserver.user;

import com.clown.util.BinaryOperations;

public class User {
	private String username = null;
	private String password = null;
	
	public User(final String username, final String password) {
		this.username = username;
		this.password = password;
	}
	
	public static User fromBytes(byte[] bytes) {
		char[] cbuff = new char[BinaryOperations.bytesToInteger(bytes)];
		for (int i = 0; i < cbuff.length; i++) {
			cbuff[i] = (char) bytes[i + 4];
		}
		String username = String.valueOf(cbuff);
		int idx = cbuff.length + 4;
		cbuff = new char[BinaryOperations.bytesToInteger(bytes, idx)];
		idx += 4;
		for (int i = 0; i < cbuff.length; i++) {
			cbuff[i] = (char) bytes[idx++];
		}
		String password = String.valueOf(cbuff);
		return new User(username, password);
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

	public byte[] toBytes() {
		byte[] bytes = new byte[username.length() + password.length() + 8]; // 8 for lengths
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
		return bytes;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User)) {
			return false;
		}
		return ((User)obj).getUsername().equalsIgnoreCase(username);
	}
}
