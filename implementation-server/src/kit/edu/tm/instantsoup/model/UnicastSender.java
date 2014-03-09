package kit.edu.tm.instantsoup.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import kit.edu.tm.instantserver.storage.Peer;
import kit.edu.tm.instantsoup.Constants;

/**
 * This class UnicastSender extends Thread and establishes a unicast connection to a given peer.
 * @author Pascal Becker
 *
 */
public class UnicastSender extends Thread {
	Socket echoSocket = null;
	PrintWriter out = null;
	BufferedReader in = null;
	BufferedReader stdIn = null;
	byte[] s = null;
	boolean running = true;

	/**
	 * creates a new UnicastSender and establishes the connections
	 * @param peer
	 * @param s
	 */
	public UnicastSender(Peer peer, byte[] s) {
		this.s = s;
		try {
			echoSocket = new Socket(peer.getIp(), Constants.PORT);
			out = new PrintWriter(echoSocket.getOutputStream(), true);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: loki.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for the connection to: loki.");
			System.exit(1);
		}
	}

	/**
	 * run method for a new UnicastSender
	 */
	public void run() {
		while (running) {
			if (echoSocket != null) {
				for (int i = 0; i < 3; i++) {
					try {
						this.sendData();
						sleep(Constants.PDU_TIMER);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				this.close();
			}
		}
	}

	/**
	 *  sends data to given socket
	 */
	public void sendData() {
		if (out != null) {
			out.print(this.s);
		}
	}

	/**
	 * closes all reader, writer, sockets and terminates the thread
	 */
	public void close() {
		out.close();
		try {
			in.close();
			stdIn.close();
			echoSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.running = false;
	}
}
