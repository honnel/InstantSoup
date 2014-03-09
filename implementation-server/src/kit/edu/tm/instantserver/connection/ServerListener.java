package kit.edu.tm.instantserver.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import kit.edu.tm.instantserver.functionality.Server;
import kit.edu.tm.instantsoup.Constants;

/**
 * ServerListener waits for incoming connections on the port specified in
 * Constants.LISTENING_PORT. If a new connections is established it creates a
 * new thread of PeerConnection.
 * 
 * @author Pascal Becker
 * 
 */
public class ServerListener extends Thread {

	private boolean running = true;

	/**
	 * creates a new ServerListener
	 */
	public ServerListener() {

	}

	/**
	 * runs the ServerListener and waits for incoming connections. If a peer
	 * connects a new thread of PeerConnection is started and it returns to
	 * listening mode
	 */
	public synchronized void run() {
		ServerSocket srvr = null;
		try {
			srvr = new ServerSocket(Constants.LISTENING_PORT);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		while (running) {
			try {
				Socket socket = srvr.accept();
				System.out.println("~~~>New TCP connection established! IP: " + socket.getInetAddress().getHostAddress());
				PeerConnection newCon = new PeerConnection(socket, Server.server.getModel().getPeerList()
						.findPeer(socket.getInetAddress()));
				newCon.start();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.print("Connection lost!");
			}
		}

		try {
			srvr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * cancels ServerListener
	 */
	public void cancel() {
		this.running = false;
	}

}
