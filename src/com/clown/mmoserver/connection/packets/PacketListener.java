package com.clown.mmoserver.connection.packets;

import java.util.concurrent.TimeoutException;

public class PacketListener {
	public static final long DEFAULT_TIMEOUT = 150; // 150 ms
	protected volatile Packet packet = null;
	protected volatile boolean timedOut = false;
	protected final int lookingForType;
	protected final String sourceIp;
	
	public PacketListener(final String sourceIp, final int lookingForType) {
		this.sourceIp = sourceIp;
		this.lookingForType = lookingForType;
	}
	
	public String getSourceIp() {
		return sourceIp;
	}
	
	public void setPacket(final Packet packet) {
		this.packet = packet;
		this.notifyAll(); // Notify packet arrival.
	}
	
	public Packet getPacket(final long timeout) throws TimeoutException {
		if (packet == null) {
			try {
				this.wait(timeout);
			} catch (InterruptedException e) {}
			if (packet == null) { // Still null.
				timedOut = true;
				throw new TimeoutException("Listener timed out while waiting for packet.");
			}
		}
		return packet;
	}
	
	public void setTimedOut(boolean value) {
		this.timedOut = value;
	}
	
	public boolean isTimedOut() {
		return timedOut;
	}
	
	public int getLookingForType() {
		return lookingForType;
	}
	
	public Packet getPacket() throws TimeoutException {
		return getPacket(DEFAULT_TIMEOUT);
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PacketListener)) {
			return false;
		}
		PacketListener other = (PacketListener) o;
		if (other.lookingForType == lookingForType && other.sourceIp.equals(sourceIp)) {
			return true;
		}
		return false;
	}
}
