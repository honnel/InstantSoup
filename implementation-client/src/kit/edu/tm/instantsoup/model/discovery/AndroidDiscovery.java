package kit.edu.tm.instantsoup.model.discovery;

import kit.edu.tm.instantsoup.Discovery;
import kit.edu.tm.instantsoup.model.IModel;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.MulticastLock;

/**
 * This class adapts the normal discovery to the android system.
 * 
 * @author Daniel Hammann
 * @author Florian Schreier
 */
public class AndroidDiscovery extends Discovery {
	
	private MulticastLock mWifiMulticastLock;

	public AndroidDiscovery(WifiManager wifi, IModel model) {
		super(model);
		this.mWifiMulticastLock = wifi.createMulticastLock("InstantSOUP");
	}
	
	@Override
	public synchronized void sendPDU() {
		// use a thread different from the main thread to send a pdu (otherwise NetworkOnMainThread will be thrown)
		new Thread(new Runnable() {
			@Override
			public void run() {
				mSender.sendPDU();
			}
		}).start();
	}

	@Override
	public void startConnections() {		
		mWifiMulticastLock.acquire();
		super.startConnections();
	}

	@Override
	public void stopConnections() {
		super.stopConnections();
		if (mWifiMulticastLock != null && mWifiMulticastLock.isHeld()) {
			mWifiMulticastLock.release();
		}
	}

}
