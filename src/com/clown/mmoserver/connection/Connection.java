package com.clown.mmoserver.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.clown.mmoserver.connection.packets.PacketFactory;
import com.clown.mmoserver.connection.packets.PacketHandler;

public class Connection extends Thread {
	protected final Socket socket;
	protected final String ip;
	protected volatile OutputStream out;
	protected volatile InputStream in;
	protected volatile boolean disconnect = false;
	
	public Connection(final Socket socket) throws IOException {
		this.socket = socket;
		this.ip = socket.getInetAddress().getHostAddress();
		this.out = socket.getOutputStream();
		this.in = socket.getInputStream();
		this.start();
	}
	
	public OutputStream getOutputStream() {
		return out;
	}
	
	public InputStream getInputStream() {
		return in;
	}
	
	public String getIp() {
		return ip;
	}
	
	public synchronized void closeConnection() {
		disconnect = true;
		if (socket.isConnected()) {
			try {
				out.close();
				in.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void run() {
		while (!isDisconnected()) {
			try {
				PacketHandler.handlePacket(PacketFactory.readPacket(ip, in));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		disconnect = true;
	}
	
	public synchronized boolean isDisconnected() {
		return disconnect || socket.isClosed();
	}
}
