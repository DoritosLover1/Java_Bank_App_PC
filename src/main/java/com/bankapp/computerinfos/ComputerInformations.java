package main.java.com.bankapp.computerinfos;

import java.net.InetAddress;
import java.net.URI;
import java.io.BufferedReader;

public final class ComputerInformations {
	
	public static String getComputerName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
			return "Unknown";
		}
	}
	
	public static String getComputerMAC() {
		try {
			InetAddress ip = InetAddress.getLocalHost();
			byte[] mac = java.net.NetworkInterface.getByInetAddress(ip).getHardwareAddress();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
			}
			return sb.toString();
		} catch (Exception e) {
			return "Unknown";
		}
	}
	
	public static String getComputerIP() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			return "Unknown";
		}
	}
	
	public static String getExternalIP() {
		try {
			URI url = new URI("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new java.io.InputStreamReader(url.toURL().openStream()));
			String ip = in.readLine();
			in.close();
			return ip;
		} catch (Exception e) {
			return "Unknown";
		}
	}
	
}
