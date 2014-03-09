package kit.edu.tm.instantsoup.model;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kit.edu.tm.instantsoup.model.PeerData;

/**
 * Stores information about every foreign server.
 * 
 * @author Florian Schreier
 */
public class ServerData extends PeerData {

	private final InetSocketAddress socketAddress;

	private final Map<String, ChannelData> publicChannels;
	private final Map<String, ChannelData> privateChannels;

	protected ServerData(String peerId, InetSocketAddress socket) {
		super(peerId);
		this.socketAddress = socket;
		this.publicChannels = Collections.synchronizedMap(new HashMap<String, ChannelData>());
		this.privateChannels = Collections.synchronizedMap(new HashMap<String, ChannelData>());
	}

	public InetSocketAddress getSocketAddress() {
		return this.socketAddress;
	}
	
	protected synchronized void addChannel(ChannelData channel) {
		if (channel.isPrivate()) {
			this.privateChannels.put(channel.getName(), channel);
		} else {
			this.publicChannels.put(channel.getName(), channel);
		}
	}
	
	/**
	 * Compares the given <code>names</code> list with an internal channel list. It detects differences between both and
	 * adds or removes the specific channel.
	 * <p>
	 * After performing this method this object knows only channels with the given names.
	 * 
	 * @param channelNames
	 *            A list of channel names without prefixes.
	 * @return <code>true</code> if something changed, <code>false</code> if not.
	 */
	protected synchronized boolean updatePublicChannels(List<String> channelNames) {
		boolean changes = false;
		// remove no longer existing public channels
		Iterator<String> iterator = this.publicChannels.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			if (!channelNames.contains(name)) {
				iterator.remove();
				changes = true;
			}
		}
		// add new ones
		for (String name : channelNames) {
			if (!this.publicChannels.containsKey(name)) {
				this.publicChannels.put(name, new ChannelData(this, name, false));
				changes = true;
			}
		}
		// return true if something changed
		return changes;
		
	}
	
	protected synchronized void addPrivateChannel(String channelName) {
		if (!this.privateChannels.containsKey(channelName)) {
			this.privateChannels.put(channelName, new ChannelData(this, channelName, true));
		}
	}

	/**
	 * Compares the given <code>names</code> list with an internal channel list. It detects differences between both and
	 * adds or removes the specific channel.
	 * <p>
	 * After performing this method this object knows only channels with the given names.
	 * 
	 * @param channelNames
	 *            A list of channel names without prefixes.
	 * @return <code>true</code> if something changed, <code>false</code> if not.
	 */
	protected synchronized boolean updatePrivateChannels(List<String> channelNames) {
		boolean changes = false;
		// remove no longer existing private channels
		Iterator<String> iterator = this.privateChannels.keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			if (!channelNames.contains(name)) {
				iterator.remove();
				changes = true;
			}
		}
		// add new ones
		for (String name : channelNames) {
			if (!this.privateChannels.containsKey(name)) {
				this.privateChannels.put(name, new ChannelData(this, name, true));
				changes = true;
			}
		}
		// return true if something changed
		return changes;
	}

	/**
	 * Returns the channel with the given name.
	 * 
	 * @param channelName
	 *            The channel's name without prefix.
	 * @param privateChannel
	 *            <code>true</code> if channel is private. <code>false</code> if not.
	 * @return The channel data. <code>null</code> if no channel with this name exists on this server.
	 */
	public synchronized ChannelData getChannel(String channelName, boolean privateChannel) {
		if (privateChannel) {
			return this.privateChannels.get(channelName);
		} else {
			return this.publicChannels.get(channelName);
		}
	}

	public synchronized List<ChannelData> getPublicChannels() {
		return new ArrayList<ChannelData>(this.publicChannels.values());
	}

	public synchronized List<ChannelData> getPrivateChannels() {
		return new ArrayList<ChannelData>(this.privateChannels.values());
	}

	public synchronized List<ChannelData> getAllChannels() {
		List<ChannelData> channels = new ArrayList<ChannelData>();
		channels.addAll(this.getPublicChannels());
		channels.addAll(this.getPrivateChannels());
		return channels;
	}

	public synchronized List<ChannelData> getJoinedChannels() {
		List<ChannelData> allChannels = this.getAllChannels();
		List<ChannelData> joinedChannels = new LinkedList<ChannelData>();
		for (ChannelData channel : allChannels) {
			if (channel.haveJoined()) {
				joinedChannels.add(channel);
			}
		}
		return joinedChannels;
	}

}
