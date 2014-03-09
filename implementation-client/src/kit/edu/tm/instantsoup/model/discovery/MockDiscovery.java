package kit.edu.tm.instantsoup.model.discovery;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.util.Log;
import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.IDiscovery;
import kit.edu.tm.instantsoup.model.IModelFiller;
import kit.edu.tm.instantsoup.model.PeerChannelList;

/**
 * A mock up discovery for testing purposes.
 * 
 * @author Florian Schreier
 */
public class MockDiscovery implements IDiscovery, Runnable {
	
	private static final String MOCK_SERVER_IP = "192.168.2.111";
	private static final int MOCK_SERVER_PORT = 55556;
	
	private boolean running;
	
	private final IModelFiller model;
	private final Thread thread;
	
	public MockDiscovery(IModelFiller model) {
		this.running = false;
		this.model = model;
		this.thread = new Thread(this);
	}

	@Override
	public void sendPDU() {
		Log.d(Constants.LOG_TAG_DEBUG, "Call of sendPDU()");
	}

	@Override
	public void triggerChannelOption() {
	}

	@Override
	public void startConnections() {
		this.thread.start();
	}

	@Override
	public void stopConnections() {
		this.running = false;
	}

	@Override
	public void run() {
		this.running = true;
		while(this.running) {
			try {
				Thread.sleep(Constants.SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<String> hostedChannels = new ArrayList<String>();
			hostedChannels.add(Constants.PREFIX_CHANNEL_PUBLIC + "testchannel");
			try {
				this.model.addOrUpdateServer("ABCD", InetAddress.getByName(MOCK_SERVER_IP), MOCK_SERVER_PORT, hostedChannels);
				List<PeerChannelList> memberInChannels = new LinkedList<PeerChannelList>();
				this.model.addOrUpdateClient("A6C44C67EA45", null, "testclient", memberInChannels);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

}
