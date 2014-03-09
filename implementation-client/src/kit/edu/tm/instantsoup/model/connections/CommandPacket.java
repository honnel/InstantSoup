package kit.edu.tm.instantsoup.model.connections;

import kit.edu.tm.instantsoup.model.ChannelData;

/**
 * This class is used to communicate with a {@link ClientCommandSenderTask}.
 * 
 * @author Florian Schreier
 */
public class CommandPacket {

	public static final int SEND_AND_KEEP = 0;
	public static final int SEND_AND_CLOSE = 1;

	private final int type;
	private final ChannelData channel;
	private final String command;
	
	public CommandPacket(int type, ChannelData channel, String command) {
		this.type = type;
		this.channel = channel;
		this.command = command;
	}
	
	public int getType() {
		return this.type;
	}
	
	public ChannelData getChannel() {
		return this.channel;
	}
	
	public String getCommand() {
		return this.command;
	}
}
