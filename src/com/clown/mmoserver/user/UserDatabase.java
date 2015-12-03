package com.clown.mmoserver.user;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.clown.mmoserver.security.InvalidCredentialsException;
import com.clown.util.BinaryOperations;

public final class UserDatabase {
	public static final String USER_PATH = "./data/users/";
	private static final ArrayList<User> users = new ArrayList<User>();
	
	public static void addUser(final User user) {
		users.add(user);
	}
	
	public static void removeUser(final User user) {
		users.remove(user);
	}
	
	public static boolean userLoggedIn(final User user) {
		for (User u: users) {
			if (u.getUsername().equalsIgnoreCase(user.getUsername())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean userExists(final String username) {
		File userFile = new File(USER_PATH+username+".user");
		return userFile.exists() && !userFile.isDirectory();
	}
	
	public static User loadUser(final String username, final String password) throws InvalidCredentialsException {
		try {
			FileInputStream inputStream = new FileInputStream(USER_PATH + username + ".user");
			byte[] bytes = new byte[4];
			if (inputStream.read(bytes) != bytes.length) {
				System.err.println("Failed to read all length bytes.");
				inputStream.close();
				return null;
			}
			bytes = new byte[BinaryOperations.bytesToInteger(bytes)];
			if (inputStream.read(bytes) != bytes.length) {
				System.err.println("Failed to read all data bytes.");
				inputStream.close();
				return null;
			}
			inputStream.close();
			User user = User.fromBytes(bytes);
			if (!user.getPassword().equals(password)) {
				throw new InvalidCredentialsException("User password and given password don't match.");
			}
			return user;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void saveUser(final User user) {
		byte[] bytes = user.toBytes();
		try {
			FileOutputStream writer = new FileOutputStream(USER_PATH + user.getUsername() + ".user");
			writer.write(BinaryOperations.toBytes(bytes.length));
			writer.write(bytes);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
