package kit.edu.tm.instantsoup.model;

import java.util.Date;


/**
 * These data objects handle messages.
 * 
 * @author Florian Schreier
 */
public class Message {
	
	private final Date time;
	private final ClientData author;
	private final String text;
	
	public Message(Date time, ClientData author, String text) {
		this.time = time;
		this.author = author;
		this.text = text;
	}

	public Date getTime() {
		return this.time;
	}

	public ClientData getAuthor() {
		return this.author;
	}

	public String getText() {
		return this.text;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return "(" + this.time + ") " + this.author + ": " + this.text;
	}
}
