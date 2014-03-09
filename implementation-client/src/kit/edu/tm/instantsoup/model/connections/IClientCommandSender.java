package kit.edu.tm.instantsoup.model.connections;

import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;

/**
 * This interface provides all commands a client is able to send to servers.
 * 
 * @author Florian Schreier
 */
public interface IClientCommandSender {

	/**
	 * Sends a join command.
	 * 
	 * @param channel
	 */
	public void sendJoin(ChannelData channel);

	/**
	 * Sends a say command.
	 * 
	 * @param channel
	 * @param message
	 */
	public void sendSay(ChannelData channel, String message);

	/**
	 * Sends an exit command.
	 * 
	 * @param channel
	 */
	public void sendExit(ChannelData channel);

	/**
	 * Sends an invite command.
	 * 
	 * @param channel
	 * @param usersToInvite
	 */
	public void sendInvite(ChannelData channel, ClientData... usersToInvite);
}
