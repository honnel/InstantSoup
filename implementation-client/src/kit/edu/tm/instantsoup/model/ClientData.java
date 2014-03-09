package kit.edu.tm.instantsoup.model;

/**
 * Stores information about any foreign client.
 * 
 * @author Florian Schreier
 */
public class ClientData extends PeerData {

	/** The nickname of this client. If there is no name, this string is empty. */
	private String nick;

	/**
	 * Creates a new client without a name.
	 * 
	 * @param peerId
	 *            The unique identifier.
	 */
	protected ClientData(String peerId) {
		this(peerId, "");
	}

	/**
	 * Creates a new client with the given name.
	 * 
	 * @param uuid
	 *            The unique identifier.
	 * @param nick
	 *            The nickname of the peer.
	 */
	public ClientData(String peerId, String nick) {
		super(peerId);
		this.setNick(nick);
	}

	/**
	 * Returns the nickname of this client.
	 * 
	 * @return The nickname of this client. If there is no name, this string is empty. It is never <code>null</code>.
	 */
	public String getNick() {
		return nick;
	}

	/**
	 * Sets the nickname to the given String.
	 * 
	 * @param nick The new nickname.
	 */
	protected void setNick(String nick) {
		assert nick != null : "Nick must not be null!";
		this.nick = nick;
	}
}
