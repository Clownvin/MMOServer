package com.clown.mmoserver.connection;

import java.util.ArrayList;

import com.clown.mmoserver.MMOServer;

public final class ConnectionManager {
	private static final ArrayList<Connection> connections = new ArrayList<Connection>();
	private static final Thread connectionLooper = new Thread() {
		@Override
		public void run() {
			while (!MMOServer.isShuttingDown()) {
				synchronized (connections) {
					for (int i = 0; i < connections.size(); i++) {
						if (connections.get(i).isDisconnected()) {
							connections.remove(i--); // Remove and decrement to account for removed index.
							continue;
						}
					}
				}
				try {
					Thread.sleep(100); // 100 MS wait.
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	static {
		connectionLooper.start();
	}
	
	public static void addConnection(final Connection connection) {
		connections.add(connection);
	}
	
	public static Connection getConnection(final String ip) {
		for (int i = 0; i < connections.size(); i++) {
			if (connections.get(i).getIp().equals(ip)) {
				return connections.get(i);
			}
		}
		return null;
	}
}
