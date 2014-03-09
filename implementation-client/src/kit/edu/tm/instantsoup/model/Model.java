package kit.edu.tm.instantsoup.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.Utility;
import kit.edu.tm.instantsoup.model.connections.CommandHandler;
import kit.edu.tm.instantsoup.model.connections.IClientCommandListener;
import kit.edu.tm.instantsoup.model.connections.IClientCommandSender;
import kit.edu.tm.instantsoup.model.discovery.AndroidDiscovery;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * This class is the main interface to use the model. It provides some getters and some methods to send commands to
 * external servers.
 * 
 * @author Florian Schreier
 */
public class Model implements IModel, IClientCommandListener {

	private static Model model;

	private IDiscovery discovery;
	private final IClientCommandSender commandSender;

	/**
	 * A map of all known clients. The key represents the peerId, the value is the real peer.
	 */
	private final Map<String, ClientData> clients;

	/**
	 * A map of all known servers. The key represents the peerId, the value is the real peer.
	 */
	private final Map<String, ServerData> servers;

	private String nick;
	private String id;

	private Context appContext;
	private Context serviceContext;

	/**
	 * Creates a new instance of this singleton. Use <code>getLongtermModel()</code> to get one from outside.
	 */
	private Model() {
		this.discovery = null; // initialized later on startDiscoveryManager()
		this.commandSender = new CommandHandler(this);

		this.clients = Collections.synchronizedMap(new HashMap<String, ClientData>());
		this.servers = Collections.synchronizedMap(new HashMap<String, ServerData>());
		this.nick = "Unkown stranger";
		try {
			this.id = Utility.getMacAddress();
		} catch (Exception e) {
			Log.d(Constants.LOG_TAG_DEBUG, "Could not create ID from mac address. Using random number instead.");
			this.id = String.valueOf(new Random().nextLong());
		}

		this.serviceContext = null;
	}

	/**
	 * Returns an instance of a long term model. After that <code>appStarted(Context)</code> must be called.
	 * 
	 * @return An instance of a long term model.
	 */
	public static synchronized Model getLongtermModel() {
		if (model == null) {
			model = new Model();
		}
		return model;
	}

	/**
	 * Tells the model that the application has either started or the activity and therefore the context has changed.
	 * Every activity that uses this model should call this method in its onStart(..) method.
	 * 
	 * @param appContext
	 *            The new context.
	 */
	public void appStarted(Context appContext) {
		this.appContext = appContext;
		// start service which holds a reference to this
		Intent intent = new Intent(this.appContext, ModelService.class);
		this.appContext.startService(intent);
	}

	/**
	 * Tells the model that the current activity has been stopped. Every activity that uses this model should call this
	 * method in its onStop(..) method.
	 */
	public void appStopped() {
		this.appContext = null;
	}

	/**
	 * Stops this long term model. Call this only on explicit user's desire. The next call of
	 * {@link Model#getLongtermModel()} will return a completely new model. All data will be deleted!
	 */
	public void stopLongtermModel() {
		Log.d(Constants.LOG_TAG_DEBUG, "stop longterm model");

		discovery.stopConnections();
		model = null;
		Intent intent = new Intent(this.serviceContext, ModelService.class);
		this.serviceContext.stopService(intent);
	}

	protected void setServiceContext(Context serviceContext) {
		this.serviceContext = serviceContext;
	}

	protected void startDiscoveryManager() {
		WifiManager wifi = (WifiManager) this.serviceContext.getSystemService(Context.WIFI_SERVICE);
		this.discovery = new AndroidDiscovery(wifi, this);
		this.discovery.startConnections();
	}

	protected void startStatusBarNotificationManager() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		LocalBroadcastManager.getInstance(this.serviceContext).registerReceiver(new BroadcastToNotification(), filter);
	}

	public void fillModelWidthDummyData() {
		List<String> channelsOfServer1 = new LinkedList<String>();
		channelsOfServer1.add(Constants.PREFIX_CHANNEL_PUBLIC + "Suppentreff");
		channelsOfServer1.add(Constants.PREFIX_CHANNEL_PRIVATE + "Weihnachtsmaenner");
		try {
			this.addOrUpdateServer("12345678", InetAddress.getLocalHost(), 123, channelsOfServer1);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		List<PeerChannelList> myChannels = new ArrayList<PeerChannelList>();
		PeerChannelList myServerChannelList = new PeerChannelList("12345678");
		myServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PUBLIC + "Suppentreff");
		myServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PRIVATE + "Weihnachtsmaenner");
		myChannels.add(myServerChannelList);
		this.addOrUpdateClient(this.id, null, this.nick, myChannels);

		List<PeerChannelList> channelsOfClient1 = new ArrayList<PeerChannelList>();
		PeerChannelList client1ServerChannelList = new PeerChannelList("12345678");
		client1ServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PUBLIC + "Suppentreff");
		client1ServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PRIVATE + "Weihnachtsmaenner");
		channelsOfClient1.add(client1ServerChannelList);
		this.addOrUpdateClient("ABCD", null, "Heinz die Waldfee", channelsOfClient1);

		List<PeerChannelList> channelsOfClient2 = new ArrayList<PeerChannelList>();
		PeerChannelList client2ServerChannelList = new PeerChannelList("12345678");
		client2ServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PUBLIC + "Suppentreff");
		channelsOfClient2.add(client1ServerChannelList);
		this.addOrUpdateClient("EFGH", null, "Horst", channelsOfClient2);

		List<PeerChannelList> channelsOfClient3 = new ArrayList<PeerChannelList>();
		PeerChannelList client3ServerChannelList = new PeerChannelList("12345678");
		client3ServerChannelList.addChannel(Constants.PREFIX_CHANNEL_PRIVATE + "Weihnachtsmaenner");
		channelsOfClient3.add(client1ServerChannelList);
		this.addOrUpdateClient("IJKL", null, "Hans Wurst", channelsOfClient3);

	}

	/**
	 * Returns a list of all currently known clients.
	 * 
	 * @return A list of all currently known clients.
	 */
	public List<ClientData> getAllClients() {
		return new LinkedList<ClientData>(this.clients.values());
	}

	/**
	 * Returns the client with the given id.
	 * 
	 * @param clientId
	 *            The client's id.
	 * @return The client with the given id. <code>null</code> if unknown.
	 */
	public ClientData getClient(String clientId) {
		return this.clients.get(clientId);
	}

	/**
	 * Returns a list of all currently known channels (public and private).
	 * 
	 * @return A list of all currently known channels.
	 */
	public List<ChannelData> getAllChannels() {
		List<ChannelData> channels = new LinkedList<ChannelData>();
		List<ServerData> servers = this.getAllServers();
		for (ServerData server : servers) {
			channels.addAll(server.getAllChannels());
		}
		return channels;
	}

	public List<ChannelData> getJoinedChannels() {
		List<ChannelData> joinedChannels = new LinkedList<ChannelData>();
		for (ServerData server : this.getAllServers()) {
			joinedChannels.addAll(server.getJoinedChannels());
		}
		return joinedChannels;
	}

	public ChannelData getChannel(ServerData server, String channelName, boolean privateChannel) {
		if (server == null) {
			throw new IllegalArgumentException("server must not be null");
		}

		return server.getChannel(channelName, privateChannel);
	}

	public ChannelData getChannel(String serverId, String channelName, boolean privateChannel) {
		ServerData server = this.getServer(serverId);
		if (server != null) {
			return this.getChannel(server, channelName, privateChannel);
		} else {
			return null; // peer not found or not a server
		}
	}

	/**
	 * Returns a list of all currently known servers.
	 * 
	 * @return A list of all currently known servers.
	 */
	public List<ServerData> getAllServers() {
		return new LinkedList<ServerData>(this.servers.values());
	}

	public ServerData getServer(String serverId) {
		return this.servers.get(serverId);
	}

	/**
	 * Creates a new channel on the specified server.
	 * 
	 * @param server
	 *            On this server the new channel will be created
	 * @param name
	 *            The name of the channel to be created (without prefix).
	 * @return ChannelData information set of the created public channel.
	 */
	public ChannelData createChannel(ServerData server, String name, boolean privateChannel) {
		ChannelData channel = new ChannelData(server, name, privateChannel);
		server.addChannel(channel);
		this.joinChannel(channel);
		return channel;
	}

	/**
	 * Invites users into a channel.
	 * 
	 * @param channel
	 *            is the server where the channel gets instantiated
	 * @param usersToInvite
	 *            All clients on this list will be invited by the server.
	 */
	public void invite(ChannelData channel, ClientData... usersToInvite) {
		if (channel == null) {
			throw new IllegalArgumentException("parameter must not be null");
		}

		this.commandSender.sendInvite(channel, usersToInvite);
	}

	public void joinChannel(ChannelData channel) {
		if (channel == null) {
			throw new IllegalArgumentException("parameter must not be null");
		}

		this.commandSender.sendJoin(channel);
		channel.join();
		this.discovery.sendPDU();
	}

	public void leaveChannel(ChannelData channel) {
		if (channel == null) {
			throw new IllegalArgumentException("parameter must not be null");
		}

		this.commandSender.sendExit(channel);
		channel.leave();
		this.discovery.sendPDU();
	}

	public void sendMessageToChannel(ChannelData channel, String message) {
		this.commandSender.sendSay(channel, message);
	}

	/**
	 * {@inheritDoc}
	 */
	public void addOrUpdateClient(String peerId, InetAddress ip, String nick, List<PeerChannelList> memberInChannels) {
		ClientData client = this.getClient(peerId);
		if (client == null) {
			if (nick == null) {
				client = new ClientData(peerId);
			} else {
				client = new ClientData(peerId, nick);
			}
			this.clients.put(peerId, client);
			// broadcast client joined
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_CLIENT_JOINED);
			intent.putExtra(Constants.EXTRA_CLIENT_ID, peerId);
			LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
		}

		if (nick != null) {
			client.setNick(nick);
			// broadcast client renamed
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_CLIENT_RENAMED);
			intent.putExtra(Constants.EXTRA_CLIENT_ID, peerId);
			LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
		}

		if (memberInChannels != null) {
			for (PeerChannelList serverChannels : memberInChannels) {
				ServerData server = this.getServer(serverChannels.getPeerId());
				if (server != null) {
					for (String channelNameWithPrefix : serverChannels.getChannels()) {
						String channelName = channelNameWithPrefix.substring(1);
						if (Utility.isPublicChannelName(channelNameWithPrefix)) {
							ChannelData channel = server.getChannel(channelName, false);
							if (channel != null) {
								channel.addMember(client);
							}
						}
						if (Utility.isPrivateChannelName(channelNameWithPrefix)) {
							ChannelData channel = server.getChannel(channelName, true);
							if (channel != null) {
								channel.addMember(client);
							}
						} else {
							// without any prefix?
							ChannelData channel = server.getChannel(channelNameWithPrefix, false);
							if (channel != null) {
								channel.addMember(client);
							}
						}
					}
				}
			}
		}

		client.resetLostCounter();
	}

	/**
	 * {@inheritDoc}
	 */
	public void addOrUpdateServer(String peerId, InetAddress ip, int port, List<String> hostedChannels) {
		ServerData server = this.getServer(peerId);
		if (server == null && port != Integer.MAX_VALUE) {
			server = new ServerData(peerId, new InetSocketAddress(ip, port));
			this.servers.put(peerId, server);
			// broadcast server joined
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_SERVER_JOINED);
			intent.putExtra(Constants.EXTRA_SERVER_ID, peerId);
			LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
		}

		if (server != null) {
			if (hostedChannels != null) {
				boolean changed = server.updatePublicChannels(hostedChannels);
				if (changed) {
					// broadcast channel opened or closed
					Intent intent = new Intent();
					intent.setAction(Constants.ACTION_CHANNEL_OPENED_OR_CLOSED);
					intent.putExtra(Constants.EXTRA_SERVER_ID, peerId);
					LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
				}
			}

			server.resetLostCounter();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removePeer(String peerId) {
		if (this.getClient(peerId) != null) {
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_CLIENT_LEFT);
			intent.putExtra(Constants.EXTRA_CLIENT_ID, peerId);
			LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
			this.clients.remove(peerId);
		}
		if (this.getServer(peerId) != null) {
			Intent intent = new Intent();
			intent.setAction(Constants.ACTION_SERVER_LEFT);
			intent.putExtra(Constants.EXTRA_SERVER_ID, peerId);
			LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
			this.servers.remove(peerId);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void incLostTimers() {
		List<PeerData> allPeers = new LinkedList<PeerData>();
		allPeers.addAll(this.getAllClients());
		allPeers.addAll(this.getAllServers());
		for (PeerData peer : allPeers) {
			int lostCounter = peer.incLostCounter();
			if (lostCounter >= Constants.LOST_TIMER) {
				this.removePeer(peer.getPeerId());
			}
		}
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getNick() {
		return this.nick;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * {@inheritDoc}
	 */
	public void receivedSay(ChannelData channel, Date time, String authorId, String message) {
		channel.addMessage(new Message(time, this.getClient(authorId), message));

		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_MESSAGE_RECEIVED);
		intent.putExtra(Constants.EXTRA_SERVER_ID, channel.getServer().getPeerId());
		intent.putExtra(Constants.EXTRA_CHANNEL_NAME, channel.getName());
		intent.putExtra(Constants.EXTRA_CHANNEL_PRIVATE, channel.isPrivate());
		intent.putExtra(Constants.EXTRA_MESSAGE_TIME, time.getTime());
		intent.putExtra(Constants.EXTRA_MESSAGE_AUTHOR, authorId);
		intent.putExtra(Constants.EXTRA_MESSAGE_TEXT, message);
		LocalBroadcastManager.getInstance(this.serviceContext).sendBroadcast(intent);
	}

	@Override
	public boolean isServer() {
		return false;
	}

	@Override
	public boolean isClient() {
		return true;
	}

	@Override
	public String getPeerIDOption() {
		return this.getId() + Constants.TERMINATION;
	}

	@Override
	public String getClientNickOption() {
		return Constants.CLIENT_NICK_OPTION + this.getNick() + Constants.TERMINATION;
	}

	@Override
	public byte[] getClientMemberShipOption() {
		ByteArrayOutputStream prepare = new ByteArrayOutputStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		result.write(Constants.CLIENT_MEMBERSHIP_OPTION);
		int serverCount = 0;

		List<ServerData> allServers = this.getAllServers();
		for (ServerData server : allServers) {
			List<ChannelData> joinedChannels = server.getJoinedChannels();
			if (joinedChannels.size() > 0) {
				serverCount++;
				try {
					prepare.write(server.getPeerId().getBytes());
					prepare.write(Constants.TERMINATION);
					prepare.write(joinedChannels.size());
					for (ChannelData channel : joinedChannels) {
						if (!channel.isPrivate()) {
							prepare.write(Constants.PREFIX_CHANNEL_PUBLIC);
						}
						prepare.write(channel.getName().getBytes());
						prepare.write(Constants.TERMINATION);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result.write(serverCount);
		try {
			result.write(prepare.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toByteArray();
	}

	@Override
	public byte[] getServerOption() {
		return null;
	}

	@Override
	public byte[] getServerChannelOption() {
		return null;
	}

	@Override
	public String getServerInviteOption() {
		return null;
	}

	@Override
	public byte[] getPDU() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(getPeerIDOption().getBytes());
			stream.write(getClientNickOption().getBytes());
			if (getJoinedChannels().size() > 0) {
				stream.write(getClientMemberShipOption());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] test = stream.toByteArray();
		return test;
	}

	@Override
	public String getChannelOption() {
		return null;
	}

	@Override
	public void receiveInvite(String peerId, String invitedChannel) {
		ServerData server = this.getServer(peerId);
		if (server != null) {
			server.addPrivateChannel(Constants.PREFIX_CHANNEL_PRIVATE + invitedChannel);
		}
	}
}
