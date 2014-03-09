import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.utils.CommandParser;

/**
 * This server does nothing but printing incoming commands to standard output. It is used to test our android client
 * with the mock discovery.
 * 
 * @author Florian Schreier
 */
public class MockServer {

	public static void main(String[] args) {
		ServerSocket server = null;
		try {
			server = new ServerSocket(55556);
			System.out.println(server);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Socket client = null;
		try {
			client = server.accept();
			System.out.println(client);
		} catch (IOException e) {
			e.printStackTrace();
		}

		DataInputStream inputStream = null;
		try {
			inputStream = new DataInputStream(client.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		DataOutputStream outputStream = null;
		try {
			outputStream = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}

		boolean running = true;
		while (running) {
			String[] substrings = CommandParser.readCommandFromStream(inputStream);

			// handle packet
			if (substrings != null) {
				System.out.println(substrings[0]);
				if (substrings[0].equals(Constants.COMMAND_SAY)) {
					String author = "A6C44C67EA45";
					String s = Constants.COMMAND_SAY + Constants.SEPARATOR + author + Constants.SEPARATOR
							+ substrings[1];
					
					try {
						outputStream.write(CommandParser.commandToByte(s));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			} else {
				running = false;
			}
		}

		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
