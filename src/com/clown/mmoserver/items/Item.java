package com.clown.mmoserver.items;

import com.clown.util.ByteFormatted;

public abstract class Item implements ByteFormatted<Item> {
	protected final int itemId;
	protected int itemAmount = 0;
	protected int itemType = 0; // 0 = null
	
	public Item(final int itemId) {
		this.itemId = itemId;
	}
	
	public int getItemType() {
		return itemType;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int getItemAmount() {
		return itemAmount;
	}
}
