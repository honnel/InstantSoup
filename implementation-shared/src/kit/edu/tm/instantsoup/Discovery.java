/**
 * 
 */
package kit.edu.tm.instantsoup;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;

import kit.edu.tm.instantsoup.model.IDiscovery;
import kit.edu.tm.instantsoup.model.IModel;
import kit.edu.tm.instantsoup.model.MulticastReceiver;
import kit.edu.tm.instantsoup.model.MulticastSender;

/**
 * @author Daniel Hammann, Pascal Becker
 *
 */
public class Discovery implements IDiscovery {
	
	protected kit.edu.tm.instantsoup.model.MulticastReceiver mReceiver;
	protected kit.edu.tm.instantsoup.model.MulticastSender mSender;	
	protected Inet4Address mGroupV4 = null;
	protected Inet6Address mGroupV6 = null;
	protected IModel mModel;
	protected MulticastSocket mSocket;
	
	public Discovery(IModel model) {
		this.mModel = model;
		try {
			this.mGroupV4 = (Inet4Address) InetAddress.getByName(Constants.IP4);
		} catch (IOException e) {
			System.out.println("No IPv4-Interface found!");
		}
		try {
			this.mGroupV6 = (Inet6Address) InetAddress.getByName(Constants.IP6);
		} catch (IOException e) {
			System.out.println("No IPv6-Interface found!");
		}
		try {
			this.mSocket = new MulticastSocket(Constants.PORT);
		} catch (IOException e) {
			System.out.println("Port " + Constants.PORT + "is blocked!");
		}
		try {
			if (mGroupV4 != null) {
				this.mSocket.joinGroup(mGroupV4);
			}
		} catch (IOException e) {
			System.out.println("Can't join ip v4 multicast group!");
		}
		try {
			if (mGroupV6 != null) {
				this.mSocket.joinGroup(mGroupV6);
			}
		} catch (IOException e) {
			System.out.println("Can't join ip v6 multicast group!");
		}
		this.mReceiver = new MulticastReceiver(mSocket, mModel);
		this.mSender = new MulticastSender(mSocket, mGroupV4, mGroupV6, mModel);		
	}

	/* (non-Javadoc)
	 * @see kit.edu.tm.instantsoup.model.IDiscovery#sendPDU()
	 */
	public void sendPDU() {
		mSender.sendPDU();
	}

	/* (non-Javadoc)
	 * @see kit.edu.tm.instantsoup.model.IDiscovery#startConnections()
	 */
	public void startConnections() {
		mReceiver.start();
		mSender.start();
	}

	/* (non-Javadoc)
	 * @see kit.edu.tm.instantsoup.model.IDiscovery#stopConnections()
	 */
	public void stopConnections() {
		mReceiver.cancel();
		mSender.cancel();
		try {
			mSocket.leaveGroup(mGroupV4);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mSocket.close();
		mSocket.disconnect();
	}

	@Override
	public void triggerChannelOption() {
		mSender.sendPDU(this.mModel.getChannelOption());
		
	}

}
