package kit.edu.tm.instantsoup.model.connections;

import java.util.Date;

import kit.edu.tm.instantsoup.model.ChannelData;

/**
 * Methods of this interface are called when the corresponding command handler receives commands from server.
 * 
 * @author Florian Schreier
 */
public interface IClientCommandListener {

	public void receivedSay(ChannelData channel, Date time, String authorId, String message);

}
