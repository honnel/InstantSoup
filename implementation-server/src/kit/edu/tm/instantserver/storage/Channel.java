package kit.edu.tm.instantserver.storage;

import java.util.List;

import kit.edu.tm.instantsoup.Constants;

/**
 * Channel represents a channel during runtime. It stores <code>name</code>, <code>standby peer</code> and a list of all
 * connected peers.
 * 
 * @author Pascal Becker
 * 
 */
public class Channel {

	/* Id of hosting server */
	private String serverId = null;
	private String name = null;
	private boolean privateChat = false;
	private Peer standbyPeer = null;
	private PeerList peerList = null;
	private boolean deletable = false;

	/**
	 * initializes a new channel with its name
	 * 
	 * @param name
	 */
	public Channel(String name, String serverId) {
		this.name = name;
		this.serverId = serverId;
		this.peerList = new PeerList();
		this.deletable = false;
		if(name.charAt(0) == Constants.PREFIX_CHANNEL_PRIVATE){
			this.privateChat = true;
		}else{
			this.privateChat = false;
		}		
	}

	public String getServerId() {
		return this.serverId;
	}

	/**
	 * returns the name of the channel
	 * 
	 * @return name of channel
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * returns current standby peer
	 * 
	 * @return returns the standby peer
	 */
	public Peer getStandbyPeer() {
		return this.standbyPeer;
	}

	/**
	 * returns <code>PeerList</code> of all connected peers in this channel
	 * 
	 * @return all connected peers in a PeerList
	 */
	public PeerList getPeerList() {
		return this.peerList;
	}

	/**
	 * returns the boolean if a channel is private or not.
	 * 
	 * @return if true, channel is private, if false, channel is public
	 */
	public boolean isPrivate() {
		return this.privateChat;
	}

	/**
	 * returns the boolean if the channel could be deleted because of no connected peers
	 * 
	 * @return deletable
	 */
	public boolean getDeletable() {
		return this.deletable;
	}

	/**
	 * adds a new peer to list
	 * 
	 * @param peer
	 *            the peer that should be added
	 */
	public void addPeer(Peer peer) {
		this.peerList.addPeer(peer);
		System.out.println(this.toString());
		setNewStandbyPeer(calcStandbyPeer());
	}

	/**
	 * removes a peer from list
	 * 
	 * @param peer
	 *            the peer that should be removed
	 */
	public void removePeer(Peer peer) {
		this.peerList.removePeer(peer);
		// TODO standbypeer
		// if (this.peerList.size() != 0) {
		// setNewStandbyPeer(calcStandbyPeer());
		// } else {
		// this.deletable = true;
		// }
	}

	/*
	 * Generates a new standby peer related to the possibility of a peer being a server and the amount of connect
	 * channels. The less channels a peer is connected, the better the ranking.
	 * 
	 * @return the best standby peer that could be chosen
	 */
	private Peer calcStandbyPeer() {
		Peer peer = null;
		if (this.peerList.size() != 0) {
			int channelCounter = Integer.MAX_VALUE;
			List<Peer> cur = this.peerList.getPeerList();
			for (int i = 0; i < cur.size() - 1; i++) {
				if (cur.get(i).getServer()) {
					if (cur.get(i).getConnectionList().size() < channelCounter) {
						peer = cur.get(i);
					}
				}
			}
		}
		return peer;
	}

	/*
	 * sets a new standby peer. Can only be performed from object itself.
	 */
	private void setNewStandbyPeer(Peer peer) {
		if (peer != null) {
			this.standbyPeer = peer;
		}
	}

	/**
	 * searches peerList for given peer
	 * 
	 * @param peer
	 *            peer looked for
	 * @return peer if found, else null
	 */
	public boolean findPeer(Peer peer) {
		for (int i = 0; i < this.peerList.size(); i++) {
			if (peer == this.peerList.getPeerList().get(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * sends a message to all connected members of the channel
	 */
	public void sendToMembers(String s) {
		for (int i = 0; i < this.peerList.size(); i++) {
			this.peerList.getPeerList().get(i).sendToChannel(this, s);
		}
	}

	/**
	 * returns the current instance as a String
	 */
	@Override
	public String toString() {
		String s = "Channelname: " + this.name + " serverID: " + this.serverId + " members: ";
		for (int i = 0; i < this.peerList.size(); i++) {
			s += this.peerList.getPeerList().get(i).getId() + " ";
		}
		return s;
	}
	
	public void tidyUp(){
		for (int i = 0; i < this.peerList.size(); i++) {
			Peer cur = this.peerList.getPeerList().get(i);
			for (int j = 0; j < this.peerList.size(); j++) {
				if(cur == this.peerList.getPeerList().get(j)){
					this.peerList.removePeer(this.peerList.getPeerList().get(j));
				}
			}
		}
	}
}