package kit.edu.tm.instantsoup.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.SocketException;

import kit.edu.tm.instantsoup.Utility;

/**
 * MulticastReceiver receives all incoming peer pdu and updates the model via
 * IModelFiller interface methods
 * 
 * @see IModelFiller
 * @author Daniel Hammann
 * 
 */
public class MulticastReceiver extends Thread {

	private boolean mRunning = true;
	private MulticastSocket mSocket;
	private IModelFiller mModel;

	public MulticastReceiver(MulticastSocket socket, IModelFiller model) {
		this.mSocket = socket;
		this.mModel = model;
	}

	@Override
	public void run() {		
		while (mRunning) {
			try {
				DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
				mSocket.receive(packet);
				System.out.println("<<< Getting data from: " + packet.getAddress() + ": "
						+ new String(packet.getData(), 0, packet.getLength()));
				Utility.parseStringArray(packet, mModel);
			} catch (SocketException e) {
				// no error handling needed
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Cancels MulticastReceiver thread
	 */
	public synchronized void cancel() {
		this.mRunning = false;
	}
}
