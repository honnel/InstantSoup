package kit.edu.tm.instantsoup.model.connections;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.utils.CommandParser;

import android.os.AsyncTask;

public class ClientCommandSenderTask extends AsyncTask<CommandPacket, Void, Boolean> {

	@Override
	protected Boolean doInBackground(CommandPacket... params) {
		boolean success = true;
		int type = params[0].getType();
		ChannelData channel = params[0].getChannel();
		Socket receiver = null;
		String command = params[0].getCommand();

		while (channel.getSocket() == null) {
			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		receiver = channel.getSocket();

		try {
			byte[] buffer = CommandParser.commandToByte(command);
			OutputStream stream = receiver.getOutputStream();
			stream.write(buffer);
			if (type == CommandPacket.SEND_AND_CLOSE) {
				receiver.close();
				channel.setSocket(null);
			}
		} catch (IOException e) {
			success = false;
		}

		return success;
	}
}
