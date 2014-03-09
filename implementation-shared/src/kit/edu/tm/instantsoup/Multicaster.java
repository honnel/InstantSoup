package kit.edu.tm.instantsoup;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import kit.edu.tm.instantsoup.model.IModel;

/**
 * This class creates a new Multicaster thread that reads and writes from/to a
 * multicast group defined in <code>Constants.java</>
 * 
 * @author Pascal Becker
 * 
 */
@Deprecated
public class Multicaster extends Thread {

	private MulticastSocket socket = null;
	private InetAddress groupv4 = null;
	private InetAddress groupv6 = null;
	public MulticastWriter mWriter = null;
	public MulticastListener mListener = null;
	private IModel model = null;
	
	public Multicaster(IModel model) {
		this.model = model;
	}

	/**
	 * runs new multicaster thread
	 */
	public void run() {
		// binding IPv4
		try {
			groupv4 = InetAddress.getByName(Constants.IP4);
			socket = new MulticastSocket(Constants.PORT);
			socket.joinGroup(groupv4);
		} catch (Exception e) {
			System.out.println("No IPv4-Interface found!");
		}
		// binding IPv6
		try {
			groupv6 = InetAddress.getByName(Constants.IP6);
			socket.joinGroup(groupv6);
		} catch (Exception e) {
			System.out.println("No IPv6-Interface found!");
		}

		// starting threads
		mWriter = new MulticastWriter(this.model);
		mWriter.start();
		mListener = new MulticastListener(this.model);
		mListener.start();

	}

	/**
	 * sends a given packet to the multicast group
	 * 
	 * @param packet
	 *            given packet e.g. event PDU
	 */
	public void sendPacket(byte[] packet) {
		this.mWriter.sendPacket(packet);
	}

	/**
	 * cancels the reader and writer
	 */
	public synchronized void cancel() {
		mWriter.cancel();
		mListener.cancel();
	}

	/**
	 * MulticastWriter writes to the multicast group
	 */
	private class MulticastWriter extends Thread {

		private boolean running = true;
		private IModel model = null;

		public MulticastWriter(IModel model) {
			this.model = model;
		}

		/**
		 * runs MulticastWriter
		 */
		@Override
		public void run() {
			int mCounter = 0;
			while (this.running) {
				try {
					Thread.sleep(Constants.PDU_TIMER);
					String message = this.model.getPeerIDOption();

					if (this.model.isServer()) {
						// concatenating pdu as server
						message += this.model.getServerOption();

						if (mCounter % 4 == 0) {
							message += this.model.getServerChannelOption();
						}
					} else if (this.model.isClient()) {
						// concatenating pdu as client
						message += this.model.getClientNickOption();
					} else {
						System.out
								.println("What are you doing? I am no server and no client? Do I exist? Common! I am something! Tell me! Pleeeease!");
					}

					byte[] packet = message.getBytes();
					this.sendPacket(packet);
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.model.incLostTimers();
				mCounter++;
			}
		}

		/**
		 * s
		 * 
		 * @param packet
		 */
		public void sendPacket(byte[] packet) {
			try {
				// Send IPv4 packet
				DatagramPacket discovery = new DatagramPacket(packet, packet.length, groupv4, Constants.PORT);
				socket.send(discovery);
				System.out.println("Sending PDU...");

				// Send IPv6 packet
				if (groupv6 != null) {
					discovery = new DatagramPacket(packet, packet.length, groupv6, Constants.PORT);
					socket.send(discovery);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		/**
		 * cancels writer
		 */
		public synchronized void cancel() {
			this.running = false;
		}
	}

	/**
	 * Listens to multicast group
	 */
	public class MulticastListener extends Thread {

		private boolean running = true;

		private IModel model = null;

		public MulticastListener(IModel model) {
			this.model = model;
		}

		/**
		 * runs MulticastListener
		 */
		@Override
		public void run() {
			DatagramPacket packet = new DatagramPacket(new byte[1000], 1000);
			while (this.running) {
				try {
					socket.receive(packet);
					System.out.println("Getting data from: " + packet.getAddress() + ": "
							+ new String(packet.getData(), 0, packet.getLength()));
					Utility.parseStringArray(packet, this.model);
				} catch (Exception e) {
					System.out.println("Error on receive: " + e.getMessage());
				}
			}
		}

		/**
		 * cancels listener
		 */
		public synchronized void cancel() {
			this.running = false;
		}
	}
}
