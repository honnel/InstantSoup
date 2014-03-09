package kit.edu.tm.instantsoup.gui.status;

import kit.edu.tm.instantsoup.Constants;
import android.content.IntentFilter;

public class ModelServiceIntentFilter extends IntentFilter {
	public ModelServiceIntentFilter() {
		super();
		this.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		this.addAction(Constants.ACTION_CHANNEL_OPENED_OR_CLOSED);
		this.addAction(Constants.ACTION_CLIENT_JOINED);
		this.addAction(Constants.ACTION_CLIENT_LEFT);
		this.addAction(Constants.ACTION_MEMBER_JOINED);
		this.addAction(Constants.ACTION_MEMBER_LEFT);
		this.addAction(Constants.ACTION_SERVER_JOINED);
		this.addAction(Constants.ACTION_SERVER_LEFT);
	}
}
