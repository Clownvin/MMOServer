package com.clown.mmoserver.items;

import com.clown.util.BinaryOperations;

public final class ItemDefinition {
	public final int itemId;
	public final int maxStack;
	//Other flags?
	
	public ItemDefinition(final int itemId, final int maxStack) {
		this.itemId = itemId;
		this.maxStack = maxStack;
	}
	
	public static ItemDefinition fromBytes(byte[] bytes) {
		int itemId = BinaryOperations.bytesToInteger(bytes);
		int maxStack = BinaryOperations.bytesToInteger(bytes, 4);
		return new ItemDefinition(itemId, maxStack);
	}
	
	public byte[] toBytes() {
		byte[] bytes = new byte[8]; // TODO Increase as more properties come.
		int idx = 0;
		for (byte b : BinaryOperations.toBytes(itemId)) {
			bytes[idx++] = b;
		}
		for (byte b : BinaryOperations.toBytes(maxStack)) {
			bytes[idx++] = b;
		}
		return bytes;
	}
}
