package kit.edu.tm.instantserver.storage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import kit.edu.tm.instantserver.functionality.Server;
import kit.edu.tm.instantsoup.Constants;

/**
 * The class ChannelList stores a list of <code>Channels</code>.
 * 
 * @author Pascal Becker
 * 
 */
public class ChannelList {

	private ArrayList<Channel> channelList = null;

	/**
	 * initializes a new list.
	 */
	public ChannelList() {
		this.channelList = new ArrayList<Channel>();
	}

	public ChannelList(ArrayList<Channel> aList) {
		this.channelList = aList;
	}

	/**
	 * adds a new channel to the list
	 * 
	 * @param channel
	 *            new channel that should be added
	 */
	public void addChannel(Channel channel) {
		this.channelList.add(channel);
	}

	/**
	 * removes channels from list
	 * 
	 * @param channel
	 *            channel that should be deleted
	 */
	public void removeChannel(Channel channel) {
		this.channelList.remove(channel);
	}

	/**
	 * returns a list of all channels
	 * 
	 * @return list of all channels
	 */
	public ArrayList<Channel> getChannelList() {
		return this.channelList;
	}

	/**
	 * generates the whole PDU information for the server
	 * 
	 * @return
	 */
	public byte[] generateChannelList() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();		
		int chanCounter = 0;
		String listChan = "";
		for (int i = 0; i < this.channelList.size(); i++) {
			if (!this.channelList.get(i).isPrivate()) {
				listChan += this.channelList.get(i).getName() + Constants.TERMINATION;
				++chanCounter;
			}
		}
		stream.write(Constants.SERVER_CHANNELS_OPTION);
		stream.write(chanCounter);
		try {
			stream.write(listChan.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stream.toByteArray();
	}

	/**
	 * returns size of channelList
	 * 
	 * @return size of channelList
	 */
	public int getSize() {
		return this.channelList.size();
	}

	/**
	 * searches channelList if channel with name s exists in list
	 * 
	 * @param s
	 *            name of searched channel
	 * @return channel if found, else null
	 */
	public Channel findChannel(String s) {
		for (int i = 0; i < this.channelList.size(); i++) {
			if (s.equals(this.channelList.get(i).getName())) {
				return this.channelList.get(i);
			}
		}
		return null;
	}
	
	/**
	 * checks every channel if it contains member. If no member is connected the channel is deleted.
	 */
	public void checkChannelMember(){
		boolean deletedAChannel = false;
		for (int i = 0; i < channelList.size(); i++) {
			if(this.channelList.get(i).getPeerList().size() == 0){
				System.out.println("Channel " + this.channelList.get(i).getName() + " removed due to emptiness");
				this.channelList.remove(i);
				deletedAChannel = true;
			}
		}
		if(deletedAChannel){
			Server.server.getDiscovery().triggerChannelOption();
		}
	}

	/**
	 * returns the current instance as a String
	 */
	@Override
	public String toString() {
		String s = "Following channels are available: ";
		for (int i = 0; i < this.channelList.size(); i++) {
			s += this.channelList.get(i).getName() + " ";
		}
		return s;
	}

}
