package kit.edu.tm.instantsoup.model;

/**
 * Stores information about any foreign peer.
 * 
 * @author Florian Schreier, Pascal Becker
 * @see ClientData
 * @see ServerData
 */
public class PeerData {

	private final String peerId;
	private int lostCounter;

	/**
	 * Creates a new plain peer.
	 * 
	 * @param peerId
	 *            The unique identifier of the new peer.
	 */
	protected PeerData(String peerId) {
		this.peerId = peerId;
		this.lostCounter = 0;
	}

	/**
	 * Returns the unique identifier of this peer.
	 * 
	 * @return The unique identifier of this peer.
	 */
	public String getPeerId() {
		return this.peerId;
	}

	protected synchronized int incLostCounter() {
		this.lostCounter++;
		return this.lostCounter;
	}

	protected synchronized void resetLostCounter() {
		this.lostCounter = 0;
	}

	public synchronized int getLostCounter() {
		return lostCounter;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "PeerId: " + this.getPeerId() + " lostCounter: " + this.getLostCounter();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PeerData) {
			PeerData peer = (PeerData) obj;
			if (this.getPeerId().equals(peer.getPeerId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
