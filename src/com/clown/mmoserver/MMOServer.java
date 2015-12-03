package com.clown.mmoserver;

import com.clown.mmoserver.connection.ConnectionAcceptor;

public final class MMOServer {
	private static volatile boolean shuttingDown = false;
	
	public static boolean isShuttingDown() {
		return shuttingDown;
	}
	
	public static void main(String[] args) {
		ConnectionAcceptor.start(6667);
		while (ConnectionAcceptor.isRunning()) { 
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			//TODO Add anything here?
		}
		shuttingDown = true;
	}
}
