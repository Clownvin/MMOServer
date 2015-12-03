package com.clown.mmoserver.connection.packets;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.clown.mmoserver.connection.Connection;
import com.clown.mmoserver.connection.ConnectionManager;
import com.clown.mmoserver.security.InvalidCredentialsException;
import com.clown.mmoserver.user.User;
import com.clown.mmoserver.user.UserDatabase;
import com.clown.threading.ThreadPool;
import com.clown.threading.ThreadTask;
import com.clown.util.BinaryOperations;

public final class PacketHandler {
	private static final ArrayList<PacketListener> listeners = new ArrayList<PacketListener>();
	
	//This is in packet handler and not its own file so that it's in the same scope as listeners.
	private static final class ListenerTask implements ThreadTask {
		private final Packet packet;
		private boolean reachedEnd = false;
		
		public ListenerTask(final Packet packet) {
			this.packet = packet;
		}
		
		@Override
		public void doTask() {
			synchronized (listeners) {
				for (int i = 0; i < listeners.size(); i++) {
					if (listeners.get(i).getLookingForType() == packet.getPacketType() && listeners.get(i).getSourceIp().equals(packet.getSourceIp())) {
						listeners.remove(i).setPacket(packet); // Remove this listener from list; it's found what it was looking for.
						reachedEnd = true;
						return;
					}
					if (listeners.get(i).isTimedOut()) {
						listeners.remove(i--);
					}
				}
			}
		}

		@Override
		public void end() {
			// Don't need anything here.
		}

		@Override
		public boolean reachedEnd() {
			return reachedEnd;
		}
		
	}
	
	public static void addPacketListener(final PacketListener listener) {
		if (listeners.contains(listener)) {
			System.err.println("Warning: Tried to add a listener to the list when there already exists one with the same search values.");
			return; // We don't need two of the same listener.
		}
		listeners.add(listener);
	}
	
	public static void handlePacket(final Packet packet) {
		ThreadPool.addTask(new ListenerTask(packet)); // Make another thread do the listener management part
		switch (packet.getPacketType()) {
		case Packet.LOGIN_TYPE:
			LoginPacket p = (LoginPacket) packet;
			User user = null;
			try {
				user = UserDatabase.loadUser(p.getUsername(), p.getPassword());
			} catch (InvalidCredentialsException e) {
				Connection connection = ConnectionManager.getConnection(packet.getSourceIp());
				if (connection != null) {
					PacketHandler.sendPacket(PacketFactory.buildMessagePacket("Invalid credentials."), connection.getOutputStream());
				}
				return;
			}
			if (user == null) {
				Connection connection = ConnectionManager.getConnection(packet.getSourceIp());
				if (connection != null) {
					PacketHandler.sendPacket(PacketFactory.buildMessagePacket("No user for username."), connection.getOutputStream());
				}
				return;
			}
			// All clear to add user to active users.
		}
	}
	
	public static void sendPacket(final Packet packet, final OutputStream out) {
		try {
			out.write(BinaryOperations.toBytes(packet.getPacketType()));
			out.write(BinaryOperations.toBytes(packet.getData().length));
			out.write(packet.getData());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
