package kit.edu.tm.instantsoup.model;

import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.ClientData;
import kit.edu.tm.instantsoup.model.Message;
import kit.edu.tm.instantsoup.model.ServerData;

/**
 * Stores information about any channel.
 * 
 * @author Florian Schreier
 */
public class ChannelData {

	/** The server that hosts this channel. */
	private final ServerData server;
	/** How to reach the channel. Not the same as server socket because there is one connection per channel. */
	private Socket socket;
	/** The name of the channel without prefix. */
	private final String name;
	/** <code>true</code> if this channel is private, <code>false</code> if not. */
	private final boolean privateChannel;
	/** <code>true</code> if we have joined this channel, <code>false</code> if not. */
	private boolean joined;
	/** All members of this channel. */
	private final List<ClientData> members;
	/** The messages written in this channel. */
	private final List<Message> messages;
	/** This server has to be contacted if the current channel hoster is unavailable. */
	private ServerData failOver;

	/**
	 * Creates a new channel data object.
	 * 
	 * @param server
	 *            The server which hosts this channel
	 * @param name
	 *            The name of this channel (without prefix).
	 * @param privateChannel
	 *            <code>true</code> if this channel is private, <code>false</code> if not.
	 */
	protected ChannelData(ServerData server, String name, boolean privateChannel) {
		this.server = server;
		this.socket = null;
		this.name = name;
		this.privateChannel = privateChannel;
		this.joined = false;
		this.members = Collections.synchronizedList(new LinkedList<ClientData>());
		this.messages = Collections.synchronizedList(new LinkedList<Message>());
		this.failOver = null;
	}

	public ServerData getServer() {
		return this.server;
	}

	public String getServerId() {
		return server.getPeerId();
	}

	public synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}

	public synchronized Socket getSocket() {
		return this.socket;
	}

	public String getName() {
		return this.name;
	}

	public boolean isPrivate() {
		return this.privateChannel;
	}

	/**
	 * Joins a channel so that the next calls of <code>havaJoined()</code> return <code>true</code> till further
	 * modifications. Informs no other peer about that. Use <code>Model.joinChannel(..)</code> to do so.
	 */
	protected void join() {
		this.joined = true;
	}

	/**
	 * Leaves a channel so that the next calls of <code>havaJoined()</code> return <code>false</code> till further
	 * modifications. Informs no other peer about that. Use <code>Model.leaveChannel(..)</code> to do so.
	 */
	public void leave() {
		this.joined = false;
	}

	/**
	 * Returns <code>true</code> if we have joined this channel, <code>false</code> if not.
	 * 
	 * @return <code>true</code> if we have joined this channel, <code>false</code> if not.
	 */
	public boolean haveJoined() {
		return this.joined;
	}

	/**
	 * Adds a member to the channel list. When trying to add users already in the list nothing happens (i.e. nobody is
	 * added twice).
	 * 
	 * @param member
	 *            The member to add.
	 * @return <code>true</code> if member has been added. <code>false</code> if member was already in the list.
	 */
	protected synchronized boolean addMember(ClientData member) {
		if (!this.members.contains(member)) {
			this.members.add(member);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Removes a member from the list. Nothing happens when trying to remove a user not in the list.
	 * 
	 * @param member
	 *            The member to remove.
	 * @return <code>true</code> if member has been removed. <code>false</code> if member has not been in the list.
	 */
	protected synchronized boolean removeMember(ClientData member) {
		return this.members.remove(member);
	}

	public synchronized List<ClientData> getMembers() {
		return this.members;
	}

	/**
	 * Adds a message just to the internal list. Use <code>Model.sendMessageToChannel(..)</code> to send messages to an
	 * external server.
	 * 
	 * @param message
	 *            The message to be added.
	 */
	protected synchronized void addMessage(Message message) {
		this.messages.add(message);
	}

	public synchronized List<Message> getMessages() {
		return this.messages;
	}

	public ServerData getFailOver() {
		return failOver;
	}

	protected void setFailOver(ServerData failOver) {
		this.failOver = failOver;
	}

	@Override
	public String toString() {
		if (this.isPrivate()) {
			return Constants.PREFIX_CHANNEL_PRIVATE + this.getName();
		} else {
			return Constants.PREFIX_CHANNEL_PUBLIC + this.getName();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ChannelData) {
			ChannelData channel = (ChannelData) obj;
			if (this.getServer().equals(channel.getServer()) && this.isPrivate() == channel.isPrivate()
					&& this.getName().equals(channel.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
