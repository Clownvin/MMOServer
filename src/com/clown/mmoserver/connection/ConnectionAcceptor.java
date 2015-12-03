package com.clown.mmoserver.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.clown.mmoserver.MMOServer;
import com.clown.mmoserver.connection.packets.PacketFactory;
import com.clown.mmoserver.connection.packets.PacketHandler;

public final class ConnectionAcceptor extends Thread {
	
	private static final int DEFAULT_PORT = 6667; // Doesn't really matter yet.
	private static final ConnectionAcceptor SINGLETON = new ConnectionAcceptor();
	
	private int port = DEFAULT_PORT;
	private volatile boolean running = false;
	private ServerSocket socket;
	
	private ConnectionAcceptor() {
		// To prevent instantiation.
	}
	
	public static void start(final int port) {
		// If it's already running, we shouldn't start it again.
		if (!SINGLETON.running) {
			SINGLETON.port = port;
			SINGLETON.start();
		} else {
			throw new RuntimeException("Connection acceptor already running on port "+port+".");
		}
	}
	
	@Override
	public void run() {
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			return; // Return because there's not much we can do if no server socket connection.
		}
		// Set running to true. We're cookin now.
		running = true;
		System.out.println("Connection acceptor running on port "+port);
		while (!MMOServer.isShuttingDown() && !socket.isClosed()) {
			try {
				Socket connectionSocket = socket.accept();
				if (ConnectionManager.getConnection(connectionSocket.getInetAddress().getHostAddress()) != null) {
					PacketHandler.sendPacket(PacketFactory.buildMessagePacket("That IP is already registered."), connectionSocket.getOutputStream());
					continue;
				}
				ConnectionManager.addConnection(new Connection(connectionSocket));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		running = false;
	}
	
	public static boolean isRunning() {
		return SINGLETON.running;
	}
	
	public static int getPort() {
		return SINGLETON.port;
	}
}
