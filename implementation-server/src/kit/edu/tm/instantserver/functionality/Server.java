package kit.edu.tm.instantserver.functionality;

import kit.edu.tm.instantserver.connection.ServerListener;
import kit.edu.tm.instantserver.storage.Channel;
import kit.edu.tm.instantserver.storage.Model;
import kit.edu.tm.instantsoup.Discovery;

/**
 * the <code>Server</code> represents the whole server.
 * 
 * @author Pascal Becker
 * 
 */
public class Server extends Thread {

	private Model model = null;
	private Discovery discovery = null;
	private ServerListener serverListener = null;
	public static Server server = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		server = new Server();
		server.getModel().getChannelList().addChannel(new Channel("#Gruppe5", "Server1"));
//		server.getModel().getChannelList().addChannel(new Channel("#Test2", "Server2"));
//		server.getModel().getChannelList().addChannel(new Channel("@Test3", "Server2"));
		server.start();
		//new ClientArray().start();

	}

	/**
	 * initializes a new server with storage. The server connects to the
	 * multicast group specified in <code>Utility.java</code>. It can be chosen
	 * between an IPv4 or and IPv6 server by setting the boolean.
	 * 
	 * @param ip
	 *            if false ipV4 is initialized, else ipV6
	 */
	public Server() {
		this.model = new Model();
		this.discovery = new Discovery(this.model);
		this.serverListener  = new ServerListener();
	}

	public void run() {
		discovery.startConnections();		
		this.serverListener.start();
	}

	// TODO
	/**
	 * creates a new channel
	 * 
	 * @param name
	 *            the name of the channel
	 */
	public void newChannel(String name) {

	}

	/**
	 * returns the runtime storage
	 * 
	 * @return storage
	 */
	public Model getModel() {
		return this.model;
	}
	
	public Discovery getDiscovery(){
		return this.discovery;
	}

}
