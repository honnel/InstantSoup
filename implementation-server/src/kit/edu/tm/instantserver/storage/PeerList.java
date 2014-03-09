package kit.edu.tm.instantserver.storage;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kit.edu.tm.instantsoup.Constants;

/**
 * this class represents a list of peers
 * 
 * @author Pascal Becker
 * 
 */
public class PeerList {

	private List<Peer> peerList;

	/**
	 * initializes a new list
	 */
	public PeerList() {
		this.peerList = Collections.synchronizedList(new ArrayList<Peer>());
	}

	/**
	 * adds a new peer
	 * 
	 * @param peer
	 *            new peer
	 */
	public void addPeer(Peer peer) {
		this.peerList.add(peer);
	}

	/**
	 * removes a peer
	 * 
	 * @param peer
	 *            that should be removed
	 */
	public void removePeer(Peer peer) {
		this.peerList.remove(peer);
	}

	/**
	 * returns complete list
	 * 
	 * @return peer list
	 */
	public List<Peer> getPeerList() {
		return this.peerList;
	}

	/**
	 * searches the peerlist if there is a peer with that name in it.
	 * If there is a peer with the same name, the lost counter of that peer is reseted to 0. 
	 * If there is no peer with the name, he is added to list.
	 * 
	 * @param s
	 *            name of peer
	 * @return peer if found, else null
	 */
	public Peer findPeer(String id, InetAddress ip) {
		Peer found = null;
		for (int i = 0; i < this.peerList.size(); i++) {
			if (id.equals(this.peerList.get(i).getId())) {
				found = this.peerList.get(i);
				found.resetLostCounter();
			}
		}
		if(found == null){
			found = new Peer(id, ip, null);
			this.addPeer(found);
		}	
		return found;
	}
	
	/**
	 * takes a peer id and searches the peerList for it. If not found returns null
	 * @param id id of searched peer
	 * @return peer or null
	 */
	public Peer findPeer(String id){
		Peer found = null;
		for (int i = 0; i < this.peerList.size(); i++) {
			if (id.equals(this.peerList.get(i).getId())) {
				found = this.peerList.get(i);
				found.resetLostCounter();
			}
		}
		return found;
	}
	
	/**
	 * finds a peer with his InetAddress object in the peer list.
	 * @param ip InetAddress from peer
	 * @return searched peer
	 */
	public Peer findPeer(InetAddress ip){
		Peer found = null;
		for (int i = 0; i < this.peerList.size(); i++) {
			if(this.peerList.get(i).getIp() != null){
				if (ip.getHostAddress().equals(this.peerList.get(i).getIp().getHostAddress())) {
					found = this.peerList.get(i);
				}
			}			
		}
		return found;
	}
	
	/**
	 * after waiting 
	 */
	public void incLostCounterForAllPeers(){
		List<Peer> cur = this.peerList;
		for(int i = 0; i < cur.size(); i++){
			cur.get(i).incLostCounter();
			if(cur.get(i).getLostCounter() == Constants.LOST_TIMER){
				System.out.println("Peer timed out: " + cur.get(i).getId());
				cur.remove(i);
			}
		}
	}
	
	/**
	 * returns the size/the amount of peers contained in list
	 * @return
	 */
	public int size(){
		return this.peerList.size();
	}
	
	/**
	 * returns the current instance as a String
	 */
	@Override
	public String toString(){
		String s = "";
		for(int i = 0; i < this.peerList.size(); i++){
			System.out.println(i);
			s += "ID: " + this.peerList.get(i).getId() + " [" + this.peerList.get(i).getIp().getHostAddress() + "]\n";
		}
		return s;
	}

}
