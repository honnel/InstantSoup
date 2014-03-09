package kit.edu.tm.instantserver.storage;

import kit.edu.tm.instantserver.connection.PeerConnection;

public class Connection {
	private PeerConnection pCon;
	private Channel channel;
	
	public Connection(PeerConnection pCon, Channel channel){
		this.pCon = pCon;
		this.channel = channel;
	}
	
	public PeerConnection getPeerConnection(){
		return this.pCon;
	}
	
	public Channel getChannel(){
		return this.channel;
	}
	
	public void setPeerConnection(PeerConnection pCon){
		this.pCon = pCon;
	}
	
	public void setChannel(Channel channel){
		this.channel = channel;
	}
	
	public void sendData(String s){
		this.pCon.sendData(s);
	}
}
