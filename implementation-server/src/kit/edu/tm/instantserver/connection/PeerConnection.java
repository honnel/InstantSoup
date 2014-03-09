package kit.edu.tm.instantserver.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import kit.edu.tm.instantserver.functionality.Server;
import kit.edu.tm.instantserver.storage.Channel;
import kit.edu.tm.instantserver.storage.Connection;
import kit.edu.tm.instantserver.storage.Peer;
import kit.edu.tm.instantserver.storage.PeerList;
import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.UnicastSender;
import kit.edu.tm.instantsoup.utils.CommandParser;

/**
 * The class PeerConnection represents a TCP connection between server and client. It stores information about the peer,
 * the socket and the channel. If a new PeerConnection is created a new Thread is created to separate and to avoid
 * blocking.
 * 
 * @author Pascal Becker
 * 
 */
public class PeerConnection extends Thread {

	private boolean running = true;

	private Peer peer;
	private Channel channel;
	private Socket socket;

	private PrintWriter out;

	/**
	 * constructor for PeerConnection
	 * 
	 * @param socket
	 *            the specific socket
	 * @param peer
	 *            the peer
	 */
	public PeerConnection(Socket socket, Peer peer) {
		this.peer = peer;
		this.socket = socket;
	}

	/**
	 * runs a new PeerConnection. It parses incoming commands like JOIN, SAY, INVITE and EXIT and coordinates the
	 * associated actions.
	 */
	public void run() {
		try {
			this.out = new PrintWriter(this.socket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.out.println("Connection could not be established!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Connection could not be established!");
			e.printStackTrace();
		}

		while (running) {
			try {
				String[] sArray = CommandParser
						.readCommandFromStream(new DataInputStream(this.socket.getInputStream()));
				System.out.println(sArray[0]);
				if (sArray[0].equals(Constants.COMMAND_JOIN)) {
					parseJoin(sArray);
				} else if (sArray[0].equals(Constants.COMMAND_SAY)) {
					parseSay(sArray);
				} else if (sArray[0].equals(Constants.COMMAND_EXIT)) {
					parseExit();
				} else if (sArray[0].equals(Constants.COMMAND_INVITE)) {
					parseInvite(sArray);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			out.close();
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * executes a join command
	 */
	private void parseJoin(String[] sArray) {
		Channel curChannel = Server.server.getModel().getChannelList().findChannel(sArray[1]);
		if (curChannel == null) {
			// if private true, else false
			curChannel = new Channel(sArray[1], Server.server.getModel().getID());
			Server.server.getModel().getChannelList().addChannel(curChannel);
			System.out.println("new channel created: " + curChannel.getName());
			Server.server.getDiscovery().triggerChannelOption();
		}
		if (curChannel.findPeer(this.peer)) {
			System.out.println("Peer already connected. Connection closes....");
			// curChannel.tidyUp();
		} else {
			this.setChannel(curChannel);
		}
	}
	
	/*
	 * parses a say and send message to all members
	 */
	private void parseSay(String[] sArray) {
		String s = Constants.COMMAND_SAY + Constants.TERMINATION + this.peer.getId() + Constants.TERMINATION
				+ sArray[1];
		this.channel.sendToMembers(s);
	}

	/*
	 * executes an exit command
	 */
	private void parseExit() throws IOException {
		this.channel.removePeer(this.peer);
		Server.server.getModel().getChannelList().checkChannelMember();
		this.cancel();
	}
	
	/*
	 * parses a invite and sends defined SERVER-INVITE-OPTION to all invited peers
	 */
	private void parseInvite(String[] sArray) {
		PeerList inviteList = new PeerList();
		for (int i = 1; i < sArray.length - 1; i++) {
			Peer cur = Server.server.getModel().getPeerList().findPeer(sArray[i]);
			if (cur != null) {
				inviteList.addPeer(cur);
			}
		}
		String pdu = Constants.SERVER_INVITE_OPTION + Constants.TERMINATION + this.channel.getName()
				+ Constants.TERMINATION + inviteList.size() + Constants.TERMINATION;
		for (int i = 0; i < inviteList.size(); i++) {
			pdu += inviteList.getPeerList().get(i).getId() + Constants.TERMINATION;
		}
		for (int i = 0; i < inviteList.size(); i++) {
			ArrayList<UnicastSender> uniList = new ArrayList<UnicastSender>();
			uniList.add(new UnicastSender(inviteList.getPeerList().get(i), CommandParser.commandToByte(pdu)));
			uniList.get(i).start();
			System.out.println("SERVER-INVITE-OPTION send to " + inviteList.getPeerList().get(i).getId());
		}
	}

	/**
	 * sendData is able to send data over the stored socket to the connected peer.
	 * 
	 * @param bs
	 *            the network message exactly described in the RFC
	 */
	public void sendData(String s) {
		DataOutputStream outputStream = null;
		try {
			outputStream = new DataOutputStream(this.socket.getOutputStream());
			outputStream.write(CommandParser.commandToByte(s));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * cancels the running thread
	 */
	private void cancel() {
		this.running = false;
	}

	/*
	 * does everything needed for the set of a channel like assign all objects, adds peer to channel and channel to peer
	 */
	private void setChannel(Channel channel) {
		this.channel = channel;
		this.peer.addConnection(new Connection(this, channel));
		this.channel.addPeer(this.peer);
	}

	/**
	 * returns the current instance as a String
	 */
	@Override
	public String toString() {
		return "Peer: " + this.peer.toString() + "\n" + "Channel: " + this.channel.toString();
	}
}
