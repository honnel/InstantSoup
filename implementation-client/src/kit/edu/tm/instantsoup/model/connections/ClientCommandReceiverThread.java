package kit.edu.tm.instantsoup.model.connections;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Date;

import android.util.Log;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.utils.CommandParser;

/**
 * This main task of this thread is to handle incoming commands. But it also controls the life circle of the channel
 * socket.
 * 
 * @author Florian Schreier
 */
public class ClientCommandReceiverThread implements Runnable {

	private boolean running;

	private final ChannelData channel;
	private final IClientCommandListener commandListener;

	private DataInputStream inputStream;

	public ClientCommandReceiverThread(ChannelData channel, IClientCommandListener commandListener) {
		this.running = false;
		this.channel = channel;
		this.commandListener = commandListener;
	}

	public void stop() {
		this.running = false;
	}

	@Override
	public void run() {
		this.running = true;

		this.establishChannelSocket();

		Socket socket = this.channel.getSocket();
		if (socket == null) {
			this.running = false;
		} else {
			try {
				this.inputStream = new DataInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				this.running = false;
			}
		}

		while (this.running) {
			String[] substrings = CommandParser.readCommandFromStream(this.inputStream);

			// handle packet
			if (substrings != null) {
				if (substrings.length == 3 && substrings[0].equals(Constants.COMMAND_SAY)) {
					this.commandListener.receivedSay(this.channel, new Date(), substrings[1], substrings[2]);
				}
			} else {
				this.running = false;
			}
		}
	}

	private void establishChannelSocket() {
		InetSocketAddress serverAddress = channel.getServer().getSocketAddress();
		try {
			Log.d(Constants.LOG_TAG_DEBUG, "trying to contact " + serverAddress);
			Socket socket = new Socket(serverAddress.getAddress(), serverAddress.getPort());
//			Socket socket = new Socket();
//			socket.connect(serverAddress, 10000);
			Log.d(Constants.LOG_TAG_DEBUG, "connection to " + serverAddress + " established");
			this.channel.setSocket(socket);
		} catch (SocketTimeoutException e) {
			Log.d(Constants.LOG_TAG_DEBUG, "connection to " + serverAddress + " timed out");
			this.running = false;
		} catch (IOException e) {
			e.printStackTrace();
			this.running = false;
		}
	}
}
