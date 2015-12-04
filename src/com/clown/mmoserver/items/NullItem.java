package com.clown.mmoserver.items;

import com.clown.util.BinaryOperations;

public class NullItem extends Item {
	public static final int NULL_ITEM_ID = -1;
	
	public NullItem() {
		super(NULL_ITEM_ID);
	}
	
	@Override
	public int getItemAmount() {
		return 0;
	}

	@Override
	public Item fromBytes(byte[] bytes) {
		return new NullItem();
	}

	@Override
	public byte[] toBytes() {
		byte[] bytes = new byte[8]; // item id, item type ALWAYS, followed by rest of data.
		int idx = 0;
		for (byte b : BinaryOperations.toBytes(NULL_ITEM_ID)) {
			bytes[idx++] = b;
		}
		for (byte b : BinaryOperations.toBytes(itemType)) {
			bytes[idx++] = b;
		}
		return bytes;
	}

	@Override
	public int sizeOf() {
		return 8;
	}

}
