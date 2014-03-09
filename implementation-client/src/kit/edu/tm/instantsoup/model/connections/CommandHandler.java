package kit.edu.tm.instantsoup.model.connections;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;

/**
 * 
 * @author Florian Schreier
 */
public class CommandHandler implements IClientCommandSender {

	private IClientCommandListener commandListener;

	/**
	 * Creates a new command handler.
	 */
	public CommandHandler(IClientCommandListener commandListener) {
		this.commandListener = commandListener;
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendJoin(ChannelData channel) {
		StringBuilder command = new StringBuilder();
		command.append(Constants.COMMAND_JOIN);
		command.append(Constants.SEPARATOR);
		command.append(channel);
		command.append(Constants.SEPARATOR);

		new Thread(new ClientCommandReceiverThread(channel, this.commandListener)).start();

		CommandPacket packet = new CommandPacket(CommandPacket.SEND_AND_KEEP, channel, command.toString());
		new ClientCommandSenderTask().execute(packet);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendSay(ChannelData channel, String message) {
		if (channel == null) {
			return;
		}
		if (channel.getSocket() == null || channel.getSocket().isClosed()) {
			channel.leave();
			channel.setSocket(null);
		}

		StringBuilder command = new StringBuilder();
		command.append(Constants.COMMAND_SAY);
		command.append(Constants.SEPARATOR);
		command.append(message);
		command.append(Constants.SEPARATOR);

		CommandPacket packet = new CommandPacket(CommandPacket.SEND_AND_KEEP, channel, command.toString());
		new ClientCommandSenderTask().execute(packet);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendExit(ChannelData channel) {
		if (channel == null) {
			return;
		}
		if (channel.getSocket() == null || channel.getSocket().isClosed()) {
			channel.leave();
			channel.setSocket(null);
		}

		StringBuilder command = new StringBuilder();
		command.append(Constants.COMMAND_EXIT);
		command.append(Constants.SEPARATOR);

		CommandPacket packet = new CommandPacket(CommandPacket.SEND_AND_CLOSE, channel, command.toString());
		new ClientCommandSenderTask().execute(packet);
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendInvite(ChannelData channel, ClientData... usersToInvite) {
		if (channel == null) {
			return;
		}
		if (channel.getSocket() == null || channel.getSocket().isClosed()) {
			channel.leave();
			channel.setSocket(null);
		}

		StringBuilder command = new StringBuilder();
		command.append(Constants.COMMAND_INVITE);
		command.append(Constants.SEPARATOR);
		for (ClientData client : usersToInvite) {
			command.append(client.getPeerId());
			command.append(Constants.SEPARATOR);
		}

		CommandPacket packet = new CommandPacket(CommandPacket.SEND_AND_KEEP, channel, command.toString());
		new ClientCommandSenderTask().execute(packet);
	}
}
