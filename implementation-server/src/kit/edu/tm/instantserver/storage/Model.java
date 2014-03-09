package kit.edu.tm.instantserver.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.Utility;
import kit.edu.tm.instantsoup.model.IModel;
import kit.edu.tm.instantsoup.model.PeerChannelList;

/**
 * The class <code>Storage</code> represents all stored data during runtime. It keeps a list of all
 * <code>Channels</code> and a list of all <code>Peer</code> The ID of the server is stored in the field <code>ID</code>
 * . The ID is generated out of the MAC address
 * 
 * @author Pascal Becker
 * 
 */
public class Model implements IModel {

	private ChannelList channelList = null;
	private PeerList peerList = null;
	private String ID = null;
	private boolean server = true;
	private boolean client = false;
	private int mServerChannelOptionCounter = 0;

	/**
	 * initializes a new storage for runtime
	 */
	public Model() {
		try {
			this.ID = Utility.getMacAddress();
		} catch (Exception e) {
			System.out.println("Could not create ID from mac address. Check interfaces!");
		}
		this.channelList = new ChannelList();
		this.peerList = new PeerList();
	}

	/**
	 * returns list of all peers that are currently online. No difference between joined on this server or not.
	 * 
	 * @return list of online peers
	 */
	public PeerList getPeerList() {
		return peerList;
	}

	/**
	 * returns a list of all channels that are currently in use
	 * 
	 * @return list of all channels
	 */
	public ChannelList getChannelList() {
		return channelList;
	}

	/**
	 * returns ID of the server generated via MAC address
	 * 
	 * @return ID of server
	 */
	public String getID() {
		return this.ID;
	}

	@Override
	public void addOrUpdateClient(String peerId, InetAddress ip, String nick, List<PeerChannelList> memberInChannels) {
		this.peerList.findPeer(peerId, ip);
	}

	@Override
	public void addOrUpdateServer(String peerId, InetAddress ip, int port, List<String> hostedChannels) {
		Peer peer = this.peerList.findPeer(peerId, ip);
		peer.setIp(ip);
		// ACHTUNG hostedChannels == null wenn kein Channel bisher offen!
		if (hostedChannels != null) {
			
		}
	}

	@Override
	public void removePeer(String peerId) {
		System.out.println("removePeer");
	}

	@Override
	public void incLostTimers() {
		this.peerList.incLostCounterForAllPeers();

	}

	@Override
	public String getPeerIDOption() {
		return this.ID + Constants.TERMINATION;
	}

	@Override
	public String getClientNickOption() {
		return null;
	}

	@Override
	public byte[] getClientMemberShipOption() {
		return null;
	}

	@Override
	public byte[] getServerOption() {		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte byte1 = (byte) ((Constants.LISTENING_PORT & 0xFF00) >> 8);
        byte byte2 = (byte) (Constants.LISTENING_PORT & 0xFF);
        stream.write(Constants.SERVER_OPTION);
        stream.write(byte1);
        stream.write(byte2);
        byte[] test = stream.toByteArray();
		return test;
	}

	@Override
	public byte[] getServerChannelOption() {
		return this.channelList.generateChannelList();
	}

	@Override
	public String getServerInviteOption() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isServer() {
		return this.server;
	}

	@Override
	public boolean isClient() {
		return this.client;
	}

	@Override
	public byte[] getPDU() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(getPeerIDOption().getBytes());		
			stream.write(getServerOption());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (mServerChannelOptionCounter % 4 == 0) {
			mServerChannelOptionCounter = 0;
			try {
				stream.write(getServerChannelOption());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mServerChannelOptionCounter++;
		return stream.toByteArray();
	}

	public String getChannelOption() {
		return getPeerIDOption() + getServerOption() + getServerChannelOption();
	}

	@Override
	public void receiveInvite(String peerId, String invitedChannel) {
		// TODO Auto-generated method stub
		
	}

}
