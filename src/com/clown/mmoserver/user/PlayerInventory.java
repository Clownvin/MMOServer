package com.clown.mmoserver.user;

import com.clown.mmoserver.items.Item;
import com.clown.mmoserver.items.NullItem;
import com.clown.util.BinaryOperations;

public class PlayerInventory {
	private Item[] inventoryItems = new Item[64];
	
	public PlayerInventory() {
		initNulls();
	}
	
	public void initNulls() {
		for (int i = 0; i < inventoryItems.length; i++) {
			inventoryItems[i] = new NullItem();
		}
	}
	
	public PlayerInventory fromBytes() {
		
	}
	
	public byte[] toBytes() {
		int totalSize = 256;
		for (int i = 0; i < inventoryItems.length; i++) {
			totalSize += inventoryItems[i].sizeOf(); // Size of should be size without including the sizeof.
		}
		byte[] bytes = new byte[totalSize];
		int idx = 0;
		for (int i = 0; i < inventoryItems.length; i++) {
			for (byte b : BinaryOperations.toBytes(inventoryItems[i].sizeOf())) {
				bytes[idx++] = b;
			}
			for (byte b : inventoryItems[i].toBytes()) {
				bytes[idx++] = b;
			}
		}
		return bytes;
	}
}
