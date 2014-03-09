package kit.edu.tm.instantsoup.model;

import java.net.InetAddress;
import java.util.List;

/**
 * This is the model interface for the data filler perspective.
 * 
 * @author Florian Schreier, Pascal Becker
 */
public interface IModelFiller {

	/**
	 * Adds a client to the model if this is the first time it is seen. If not its information will be updated.
	 * 
	 * @param peerId
	 *            If a client with this unique identifier is already known, its information will be updated. If not a
	 *            new client with this id will be added.
	 * @param nick
	 *            The nickname of the client. If <code>null</code>, the current nickname is not changed. Use the empty
	 *            String to delete the nickname.
	 * @param memberInChannels
	 *            The client is a member of all channels in this list. They are identified by its names (with prefix).
	 *            If <code>null</code>, the current list is not changed. Use an empty list to remove all memberships.
	 */
	public void addOrUpdateClient(String peerId, InetAddress ip, String nick, List<PeerChannelList> memberInChannels);

	/**
	 * Adds a server to the model if this is the first time it is seen. If not its information will be updated.
	 * 
	 * @param peerId
	 *            If a server with this unique identifier is already known, its information will be updated. If not a
	 *            new server with this id will be added.
	 * @param ip
	 *            Eventual JOIN commands will be sent to this address. Must not be <code>null</code>.
	 * @param port
	 *            The corresponding port.
	 * @param hostedChannels
	 *            The server hosts all channels in this list. They are identified by its names (without prefix). If
	 *            <code>null</code>, the current list is not changed. Use an empty list to remove all hosted.
	 */
	public void addOrUpdateServer(String peerId, InetAddress ip, int port, List<String> hostedChannels);
	
	public void receiveInvite(String peerId, String invitedChannel);

	/**
	 * Removes the peer with the given <code>id</code> from the model (e.g. not recently seen).
	 * 
	 * @param peerId
	 *            If a peer with this unique identifier is known, it will be removed. If not, nothing happens.
	 */
	public void removePeer(String peerId);

	/**
	 * Increments the lost timer of all peers in database.
	 */
	public void incLostTimers();

}
