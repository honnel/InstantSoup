package kit.edu.tm.instantsoup.utils;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import kit.edu.tm.instantsoup.Constants;

public final class CommandParser {

	private CommandParser() {
	}

	/**
	 * Translates a string into a byte array that is leaded by its length.
	 * 
	 * @param command
	 * @return
	 */
	public static byte[] commandToByte(String command) {
		byte[] commandBytes = command.getBytes(Constants.CHAR_SET);
		int commandLength = commandBytes.length;
		ByteBuffer buf = ByteBuffer.allocate(commandLength + 4);
		buf.putInt(command.length());
		buf.put(commandBytes);
		return buf.array();
	}

	public static String[] readCommandFromStream(DataInputStream in) {
		String[] substrings = null;

		while (substrings == null) {
			boolean noFail = true;
			// read length of following command
			int length = 0;
			try {
				length = in.readInt();
			} catch (EOFException e) {
				noFail = false;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}

			// read command
			byte[] buffer = new byte[length];
			if (noFail) {
				try {
					in.read(buffer, 0, length);
				} catch (EOFException e) {
					noFail = false;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			// split packet
			if (noFail) {
				String p = new String(buffer, 0, length, Constants.CHAR_SET);
				substrings = p.split(String.valueOf(Constants.SEPARATOR));
			}
		}

		return substrings;
	}
}
