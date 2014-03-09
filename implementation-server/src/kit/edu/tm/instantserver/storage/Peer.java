package kit.edu.tm.instantserver.storage;

import java.net.InetAddress;
import java.util.ArrayList;

import kit.edu.tm.instantsoup.Constants;

/**
 * This class represents a peer using the InstantSOUP chat protocol. A peer
 * stores the <code>name</code>, {@code ip} and a list of all <code>channels</
 * 
 * @author Pascal Becker
 * 
 */

public class Peer {

	private String id = null;
	private InetAddress ip = null;
	private int port = Integer.MAX_VALUE;
	private boolean server = false;
	private int lostCounter = 0;
	private ArrayList<Connection> connectionList = null;

	/**
	 * initiates a new peer
	 */
	public Peer(String id, InetAddress ip, ChannelList channelList) {
		this.id = id;
		this.ip = ip;
		this.connectionList = new ArrayList<Connection>();
		this.server = false;
		this.lostCounter = 0;
	}

	/**
	 * returns id of peer
	 * 
	 * @return id of peer
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * sets id
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * returns ip of peer
	 * 
	 * @return ip of peer
	 */
	public InetAddress getIp() {
		return this.ip;
	}

	/**
	 * sets ip
	 * 
	 * @param ip
	 */
	public void setIp(InetAddress ip) {
		this.ip = ip;
	}

	/**
	 * returns server
	 */
	public boolean getServer() {
		return this.server;
	}

	/**
	 * sets server
	 * 
	 * @param server
	 */
	public void setServer(boolean server) {
		this.server = server;
	}

	/**
	 * returns port
	 * 
	 * @return port
	 */
	public int getPort() {
		return this.port;
	}

	/**
	 * sets port
	 * 
	 * @param port
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * returns lostCounter
	 * 
	 * @return lostcounter
	 */
	public int getLostCounter() {
		return lostCounter;
	}

	/**
	 * sets lostCounter to 0 (zero)
	 * 
	 * @param lostCounter
	 */
	public void resetLostCounter() {
		this.lostCounter = 0;
	}

	/**
	 * increases lostCounter by 1 (one)
	 */
	public void incLostCounter() {
		this.lostCounter++;
	}

	/**
	 * returns the current instance as a String
	 */
	@Override
	public String toString() {
		String s = "Peer " + this.id + " isServer? " + this.server;
		for (int i = 0; i < connectionList.size(); i++) {
			s += " " + connectionList.get(i).getChannel().getName();
		}
		return s;
	}

	/**
	 * returns connectionList
	 * 
	 * @return connectionList
	 */
	public ArrayList<Connection> getConnectionList() {
		return this.connectionList;
	}

	/**
	 * sets connectionList
	 * 
	 * @param connectionList
	 */
	public void setConnectionList(ArrayList<Connection> connectionList) {
		this.connectionList = connectionList;
	}

	/**
	 * adds a new Connection
	 * 
	 * @param c
	 *            Connection
	 */
	public void addConnection(Connection c) {
		this.connectionList.add(c);
	}

	/**
	 * removes a Connection
	 * 
	 * @param c
	 *            Connection that should be removed
	 */
	public void removeConnection(Connection c) {
		this.connectionList.remove(c);
	}
	
	/**
	 * uses the connectionList and searches for given channel and sends the given string
	 * @param channel channel that recieves message
	 * @param bs the message
	 */
	public void sendToChannel(Channel channel, String s){
		for (int i = 0; i < this.connectionList.size(); i++) {
			if(this.connectionList.get(i).getChannel() == channel){
				this.connectionList.get(i).sendData(s);
				System.out.println(">>> sending \"" + s.split(""+Constants.TERMINATION)[2] + "\" to " + this.id + " in channel " + channel.getName());
			}
		}
	}

}
