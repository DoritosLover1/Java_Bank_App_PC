package main.java.com.bankapp.encryptionfunc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordEncyrption {
	public static String hashFunction (String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashValue = md.digest(password.getBytes());
			
			StringBuilder hashString = new StringBuilder();
			for (byte b : hashValue) {
				String hashPart = Integer.toHexString(0xff & b);
				if (hashPart.length() == 1) hashString.append('0');
				hashString.append(hashPart);
			}
			return hashString.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
}
