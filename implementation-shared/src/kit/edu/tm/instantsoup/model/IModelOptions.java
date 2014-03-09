package kit.edu.tm.instantsoup.model;

/**
 * 
 * @author Pascal Becker
 *
 */
public interface IModelOptions {
	
	/**
	 * Returns the boolean if the running process is able to be a server or not.
	 * @return boolean for server
	 */
	public boolean isServer();
	
	/**
	 * Returns the boolean if the running process is able to be a client or not.
	 * @return boolean for client
	 */
	public boolean isClient();
	
	/**
	 * Peer pdus will be concatenated as defined in the rfc.
	 * 
	 *+----+-----------+-------------+-----------+-------------+-----+
     *| ID | OptionID1 | OptionData1 | OptionID2 | OptionData2 | ... |
     *+----+-----------+-------------+-----------+-------------+-----+
     *
     *Every option is terminated with <code>Constants.TERMINATION</code>
	 */
	
	/**
	 * Returns the <code>id</code> for the pdu as defined in the rfc.
	 * @return <code>id</code>
	 */
	public String getPeerIDOption();
	
	/**
	 * Returns the <code>nickname</code> for the pdu as defined in the rfc.
	 * OptionID: 0x01
	 * +----+----------+
	 * |0x01| Nickname |
	 * +----+----------+
	 * 
	 * @return <code>nickname</code>
	 */
	public String getClientNickOption();
	
	/**
	 * Returns the membership of all connected channels of the peer for the pdu as defined in the rfc.
	 * OptionID: 0x02
	 * +----+-----+-----+------+-----+-----+---+-----+------+-------+------+----+
	 * |    | Num | Ser | NumC | Cha | Cha | . | Cha | Serv | NumCh | Chan | .. |
	 * |0x02| Ser | ver | hann | nne | nne | . | nne | erId | annel | n el | .  |
	 * |    | ve  | Id  | el s | l1  | l2  | . | lN  | 2    | s2    | 1    |    |
	 * |    | rs  | 1   | 1    |     |     |   |     |      |       |      |    |
	 * +----+-----+-----+------+-----+-----+---+-----+------+-------+------+----+
	 * 
	 * @return the client member ship option
	 */
	public byte[] getClientMemberShipOption();
	
	/**
	 * Returns the server option for the pdu as defined in the rfc.	 * 
	 * OptionID: 0x10
	 * +----+---------------+
	 * |0x10| listeningPort |
	 * +----+---------------+
	 * 
	 * @return server option
	 */
	public byte[] getServerOption();
	
	/**
	 * Returns the server channel option for the pdu as defined in the rfc.
	 * OptionID: 0x11
	 * +----+-------------+------------+------------+-----+------------+
     * +0x11| NumChannels | ChannelID1 | ChannelID2 | ... | ChannelIDN |
     * +----+-------------+------------+------------+-----+------------+
     * 
	 * @return the server channel option pdu
	 */
	public byte[] getServerChannelOption();
	
	/**
	 * Returns the server invite option for the pdu as defined in the rfc.
	 * OptionID: 0x12
	 * +----+-----------+------------+-----------+-----------+-----+-----------+
     * +0x12| ChannelID | NumClients | ClientID1 | ClientID2 | ... | ClientIDN |
	 * +----+-----------+------------+-----------+-----------+-----+-----------+
	 * @return server invite option
	 */
	public String getServerInviteOption();
	
	/**
	 * Returns payload data unit as string representation. Including all relevant option-fields
	 * @return payload data unit
	 */
	public byte[] getPDU();
	
	
	/**
	 * Returns the channel option PDU as needed when a new channel is created
	 * @return payload data unit
	 */
	public String getChannelOption();

}
