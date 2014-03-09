package kit.edu.tm.instantsoup;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kit.edu.tm.instantsoup.model.IModelFiller;
import kit.edu.tm.instantsoup.model.PeerChannelList;

/**
 * used for utility tasks
 * 
 * @author Pascal Becker
 * 
 */
public class Utility {
	private static int count;

	/**
	 * returns the MAC address of the current network interface
	 * 
	 * @return MAC address as String in one series of digits/letters
	 * @throws Exception
	 *             something went wrong
	 */
	public static String getMacAddress() throws Exception {
		String result = "";
		try {
			for (NetworkInterface ni : Collections.list(NetworkInterface.getNetworkInterfaces())) {
				byte[] hardwareAddress = ni.getHardwareAddress();

				if (hardwareAddress != null) {
					for (int i = 0; i < hardwareAddress.length; i++) {
						result += String.format((i == 0 ? "" : "") + "%02X", hardwareAddress[i]);
					}
					if (result.length() > 0 && !ni.isLoopback()) {
						return result;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * this method parses a given string for information defined in the RFC
	 * 
	 * @param s
	 *            string to parse
	 */
	public static void parseStringArray(DatagramPacket packet, IModelFiller model) {
		count = 0;
		String peerId = "";
		int port = Integer.MAX_VALUE;
		String nick = null;
		boolean server = false;
		boolean client = false;
		List<PeerChannelList> memberInChannels = null;
		ArrayList<String> channelList = null;
		byte[] bytePDU = null;
		bytePDU = packet.getData();
		InetAddress ip = packet.getAddress();

		while (bytePDU[count] != '\0') {
			peerId += (char) bytePDU[count++];
		}
		count++;

		while (count < bytePDU.length) {
			byte option = bytePDU[count++];
			if (isServerOption(option)) {
				server = true;
				port = parseServerOption(bytePDU);
			} else if (isServerChannelsOption(option)) {
				server = true;
				channelList = parseServerChannelOption(bytePDU);
			} else if (isServerInviteOption(option)) {
				// TODO
			} else if (isClientNickOption(option)) {
				client = true;
				nick = parseClientNickOption(bytePDU);
			} else if (isClientMembershipOption(option)) {
				client = true;
				memberInChannels = parseClientMembershipOption(bytePDU);
			} else {
				// ?
			}
			count++;
		}
		if (server) {
			model.addOrUpdateServer(peerId, ip, port, channelList);
		}
		if (client) {
			model.addOrUpdateClient(peerId, ip, nick, memberInChannels);
		}
	}

	private static List<PeerChannelList> parseClientMembershipOption(byte[] bytePDU) {
		List<PeerChannelList> result = new ArrayList<PeerChannelList>();
		byte numServers = bytePDU[count++];
		for (int i = 0; i < numServers; i++) {
			String serverId = "";
			while (bytePDU[count] != Constants.TERMINATION) {
				serverId += (char) bytePDU[count++];
			}
			count++;
			PeerChannelList serverChannelList = new PeerChannelList(serverId);
			byte numChannels = bytePDU[count++];
			for (int j = 0; j < numChannels; j++) {
				String channelName = "";
				while (bytePDU[count] != Constants.TERMINATION) {
					channelName += (char) bytePDU[count++];
				}
				count++;
				if (channelName.length() > 0) {
					if (!isPublicChannelName(channelName)) {
						channelName = Constants.PREFIX_CHANNEL_PUBLIC + channelName;
					}
					serverChannelList.addChannel(channelName);
				}
			}
			result.add(serverChannelList);
		}
		count--;
		return result;
	}

	private static String parseClientNickOption(byte[] bytePDU) {
		String nick = "";
		while (bytePDU[count] != Constants.TERMINATION) {
			nick += (char) bytePDU[count++];
		}
		return nick;
	}

	private static ArrayList<String> parseServerChannelOption(byte[] bytePDU) {
		ArrayList<String> parsedChannels = new ArrayList<String>();
		byte numChannels = bytePDU[count++];
		for (int i = 0; i < numChannels; i++) {
			String channelName = "";
			while (bytePDU[count] != Constants.TERMINATION) {
				channelName += (char) bytePDU[count++];
			}
			count++;
			if (channelName.length() > 0) {
				if (isPublicChannelName(channelName)) {
					channelName = channelName.substring(1);
				}
				parsedChannels.add(channelName);
			}
		}
		count--;
		return parsedChannels;
	}

	private static int parseServerOption(byte[] bytePDU) {
		int port = 0;
		port |= bytePDU[count++] & 0xFF;
		port <<= 8;
		port |= bytePDU[count] & 0xFF;
		assert port < Constants.MIN_PORT || port > Constants.MAX_PORT;
		return port;
	}

	/**
	 * gets the first char of the string and returns a boolean whether this char is an option or not
	 * 
	 * @param s
	 *            string to check
	 * @return true if it is an option, false if not
	 */
	public static boolean isAnyServerOption(byte c) {
		return isServerOption(c) || isServerChannelsOption(c) || isServerInviteOption(c);
	}

	public static boolean isServerOption(byte c) {
		return c == Constants.SERVER_OPTION;
	}

	public static boolean isServerChannelsOption(byte c) {
		return c == Constants.SERVER_CHANNELS_OPTION;
	}

	public static boolean isServerInviteOption(byte c) {
		return c == Constants.SERVER_INVITE_OPTION;
	}

	public static boolean isAnyClientOption(byte c) {
		return isClientNickOption(c) || isClientMembershipOption(c);
	}

	public static boolean isClientNickOption(byte c) {
		return c == Constants.CLIENT_NICK_OPTION;
	}

	public static boolean isClientMembershipOption(byte c) {
		return c == Constants.CLIENT_MEMBERSHIP_OPTION;
	}

	public static boolean isPrivateChannelName(String channelNameWithPrefix) {
		char prefix = channelNameWithPrefix.charAt(0);
		return prefix == Constants.PREFIX_CHANNEL_PRIVATE;
	}

	public static boolean isPublicChannelName(String channelNameWithPrefix) {
		char prefix = channelNameWithPrefix.charAt(0);
		return prefix == Constants.PREFIX_CHANNEL_PUBLIC;
	}
}
