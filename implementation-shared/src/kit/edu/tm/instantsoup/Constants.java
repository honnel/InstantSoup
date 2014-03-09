package kit.edu.tm.instantsoup;

import java.nio.charset.Charset;

/**
 * This class contains only constants that are used all over InstantSOUP.
 * 
 * @author Pascal Becker
 * @author Florian Schreier
 */
public class Constants {
	
	// DEBUG TAG
	public static final String LOG_TAG_DEBUG = "soup";
	
	/*InstantSOUPActivity.java*/
	public static final int DIALOG_ERROR_ID = 0;
	public static final String USERNAME = "username";
	
	/*StatusTabView.java*/
	public static final String LOBBY_SMALL = "lobby";
	public static final String LOBBY_BIG = "Lobby";
	public static final String CHANNEL_LIST = "channelList";
	public static final String CHANNEL = "Channels";
	public static final String SERVER_LIST = "serverList";
	public static final String SERVERS = "Servers";
	
	/*CommandSender.java*/
	public static final String COMMAND_JOIN = "JOIN";
	public static final String COMMAND_SAY = "SAY";
	public static final String COMMAND_EXIT = "EXIT";
	public static final String COMMAND_INVITE = "INVITE";
	public static final char SEPARATOR = '\0';
	public static final Charset CHAR_SET = Charset.forName("UTF8");

	/*Discovery.java*/
	public static final String IP4 = "239.255.99.63";
	public static final String IP6 = "ffx2::4C:48:43";
	public static final int PORT = 55555;
	public static final int SLEEP_TIME = 5000;
	public static final int MIN_PORT = 49152;
	public static final int MAX_PORT = 65535;
	// listening port
	public static final int LISTENING_PORT = 8000;
	// termination symbol of peerPDU
	public static final char TERMINATION = '\0';
	// timer for sending a new PDU in ms
	public static final int PDU_TIMER = 15000;
	public static final int LOST_TIMER = 5;
	
	//PDU options
	public static final char CLIENT_NICK_OPTION = 0x01;
	public static final char CLIENT_MEMBERSHIP_OPTION = 0x02;
	public static final char SERVER_OPTION = 0x10;
	public static final char SERVER_CHANNELS_OPTION = 0x11;
	public static final char SERVER_INVITE_OPTION = 0x12;

	/*Channeldata.java*/
	public static final char PREFIX_CHANNEL_PRIVATE = '@';
	public static final char PREFIX_CHANNEL_PUBLIC = '#';
	
	/*ClientData.java*/
	public static final String EMPTY = "";
	
	/* Broadcasts and Notifications */
	public static final int HELLO_ID = 0;
	private static final String PROJECT_PREFIX = "edu.kit.tm.instantsoup.";
	// Channel Listener Actions
	public static final String ACTION_MEMBER_JOINED = PROJECT_PREFIX + "MEMBER_JOINED";
	public static final String ACTION_MEMBER_LEFT = PROJECT_PREFIX + "MEMBER_LEFT";
	public static final String ACTION_MESSAGE_RECEIVED = PROJECT_PREFIX + "MESSAGE_RECEIVED";
	// Status Listener Actions
	/** This action will be broadcasted when a client joins. Comes with {@link EXTRA_CLIENT_ID}. */
	public static final String ACTION_CLIENT_JOINED = PROJECT_PREFIX + "CLIENT_JOINED";
	/** This action will be broadcasted when a client joins. Comes with {@link EXTRA_CLIENT_ID}. */
	public static final String ACTION_CLIENT_RENAMED = PROJECT_PREFIX + "ACTION_CLIENT_RENAMED";
	/** This action will be broadcasted when a client leaves. Comes with {@link EXTRA_CLIENT_ID}. */
	public static final String ACTION_CLIENT_LEFT = PROJECT_PREFIX + "CLIENT_LEFT";
	/** This action will be broadcasted when a server joins. Comes with {@link EXTRA_SERVER_ID}. */
	public static final String ACTION_SERVER_JOINED = PROJECT_PREFIX + "SERVER_JOINED";
	/** This action will be broadcasted when a server leaves. Comes with {@link EXTRA_SERVER_ID}. */
	public static final String ACTION_SERVER_LEFT = PROJECT_PREFIX + "SERVER_LEFT";
	/** This action will be broadcasted when a channel gets opened or closed. Comes with {@link EXTRA_SERVER_ID}. */
	public static final String ACTION_CHANNEL_OPENED_OR_CLOSED = PROJECT_PREFIX + "CHANNEL_OPENED_OR_CLOSED";
	// Extras
	public static final String EXTRA_SERVER_ID = PROJECT_PREFIX + "EXTRA_SERVER_ID";
	public static final String EXTRA_CLIENT_ID = PROJECT_PREFIX + "EXTRA_CLIENT_ID";
	public static final String EXTRA_CHANNEL_NAME = PROJECT_PREFIX + "EXTRA_CHANNEL_NAME";
	public static final String EXTRA_CHANNEL_PRIVATE = PROJECT_PREFIX + "EXTRA_CHANNEL_PRIVATE";
	public static final String EXTRA_MESSAGE_TIME = PROJECT_PREFIX + "EXTRA_MESSAGE_TIME";
	public static final String EXTRA_MESSAGE_AUTHOR = PROJECT_PREFIX + "EXTRA_MESSAGE_AUTHOR";
	public static final String EXTRA_MESSAGE_TEXT = PROJECT_PREFIX + "EXTRA_MESSAGE_TEXT";
	
	/* SharedPreferences File*/
	public static final String SHARED_PREFERENCES = "INSTANT_SOUP_SHARED_PREFERENCES";
	
}
