package kit.edu.tm.instantsoup.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

import kit.edu.tm.instantsoup.Constants;

/**
 * MulticastSender send in periodic time intervals pdu to the multicast group of InstantSOUP.
 */
public class MulticastSender extends Thread {

	private boolean mRunning = true;
	private MulticastSocket mSocket;
	private InetAddress mGroupV4;
	private InetAddress mGroupV6;
	private IModel mModel;

	public MulticastSender(MulticastSocket socket, Inet4Address groupV4, Inet6Address groupV6, IModel model) {
		this.mSocket = socket;
		this.mGroupV4 = groupV4;
		this.mGroupV6 = groupV6;
		this.mModel = model;
	}

	@Override
	public void run() {
		while (this.mRunning) {
			try {
				Thread.sleep(Constants.PDU_TIMER);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sendPDU();
			mModel.incLostTimers();
		}
	}

	/**
	 * Method sends a periodic PDU to InstantSOUP multicast group
	 */
	public synchronized void sendPDU() {
		try {
			sendPacket(mModel.getPDU());
		} catch (SocketException e) {
			// no error handling needed
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(">>> Sending PDU...");
	}
	
	/**
	 * Method sends a periodic PDU to InstantSOUP multicast group.
	 * Especially for triggered PDUs this method is needed.
	 */
	public synchronized void sendPDU(String s){
		try {
			sendPacket(s.getBytes());
		} catch (SocketException e) {
			// no error handling needed
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(">>> Sending PDU...");
	}

	private void sendPacket(byte[] packet) throws IOException {
		// Send IPv4 packet
		if (mGroupV4 != null) {
			DatagramPacket datagram = new DatagramPacket(packet, packet.length, mGroupV4, Constants.PORT);
			mSocket.send(datagram);
		}
		// Send IPv6 packet
		if (mGroupV6 != null) {
			DatagramPacket datagram = new DatagramPacket(packet, packet.length, mGroupV6, Constants.PORT);
			mSocket.send(datagram);
		}		
	}

	public synchronized void cancel() {
		this.mRunning = false;
	}
}
