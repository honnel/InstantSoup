package kit.edu.tm.instantsoup.model.connections;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;
import kit.edu.tm.instantsoup.model.Model;
import kit.edu.tm.instantsoup.model.PeerChannelList;

public class MockHandler implements IClientCommandSender, Runnable {

	private static final int SLEEP_TIME = 2000;

	private final IClientCommandListener commandListener;

	/**
	 * Creates a new command handler mock up.
	 */
	public MockHandler(IClientCommandListener listener) {
		this.commandListener = listener;
		new Thread(this).start();
	}

	@Override
	public void sendJoin(ChannelData channel) {
	}

	@Override
	public void sendSay(ChannelData channel, String message) {
	}

	@Override
	public void sendExit(ChannelData channel) {
	}

	@Override
	public void sendInvite(ChannelData channel, ClientData... usersToInvite) {
	}

	/**
	 * {@inheritDoc}
	 */
	public void run() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		IClientCommandListener listener = this.commandListener;
		if (!Model.getLongtermModel().getAllChannels().isEmpty()) {
			ChannelData channel = Model.getLongtermModel().getAllChannels().get(0);
			ClientData author = Model.getLongtermModel().getAllClients().get(0);
			String message = "test 123 test";
			listener.receivedSay(channel, new Date(), author.getPeerId(), message);
			List<PeerChannelList> list = new LinkedList<PeerChannelList>();
			String channelName = Model.getLongtermModel().getServer("12345678").getAllChannels().get(0).toString();
			PeerChannelList serverChannelList = new PeerChannelList("12345678");
			serverChannelList.addChannel(channelName);
			list.add(serverChannelList);
			Model.getLongtermModel().addOrUpdateClient("123", null, "Bockwurst", list);
			try {
				List<String> channels = new ArrayList<String>();
				channels.add("#Apfeltalk");
				Model.getLongtermModel().addOrUpdateServer("456", InetAddress.getLocalHost(), 1337, channels);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
	}

}
