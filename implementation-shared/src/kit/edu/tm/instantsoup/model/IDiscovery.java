package kit.edu.tm.instantsoup.model;

/**
 * 
 * @author Florian Schreier
 */
public interface IDiscovery {

	/**
	 * Sends a PDU.
	 */
	public void sendPDU();
	
	/**
	 * Sends PDU with ChannelOption
	 */
	public void triggerChannelOption();
	
	/**
	 * Doing all stuff needed for starting discovery connections.
	 */
	public void startConnections();
	
	/**
	 * Doing all stuff needed for closing discovery connections.
	 */
	public void stopConnections();
}
