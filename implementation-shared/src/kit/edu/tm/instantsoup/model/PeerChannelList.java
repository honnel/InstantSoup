package kit.edu.tm.instantsoup.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Florian Schreier
 */
public class PeerChannelList {
	
	private final String peerId;
	private final List<String> channels;

	public PeerChannelList(String peerId) {
		this.peerId = peerId;
		this.channels = new ArrayList<String>();
	}
	
	/**
	 * Use this method to add one channel at a time.
	 * 
	 * @param channelName The name of the channel (including the prefix).
	 */
	public void addChannel(String channelName) {
		this.channels.add(channelName);
	}
	
	/**
	 * 
	 * @param channelNames A list with the names of all channels (including the prefix).
	 */
	public void addAllChannels(List<String> channelNames) {
		for (String channelName : channelNames) {
			this.addChannel(channelName);
		}
	}
	
	public String getPeerId() {
		return this.peerId;
	}
	
	public List<String> getChannels() {
		return this.channels;
	}
	
	@Override
	public String toString() {
		return String.format("PeerId %s (%s)", this.peerId, this.channels.toString());
	}
}
