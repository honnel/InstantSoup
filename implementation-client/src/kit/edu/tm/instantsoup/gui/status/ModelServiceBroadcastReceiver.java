package kit.edu.tm.instantsoup.gui.status;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;
import kit.edu.tm.instantsoup.model.Model;
import kit.edu.tm.instantsoup.model.ServerData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class ModelServiceBroadcastReceiver extends BroadcastReceiver {
	@Override
    public abstract void onReceive(Context context, Intent intent);

	protected ClientData getClientDataFromIntent(Intent intent) {
		String clientId = intent.getStringExtra(Constants.EXTRA_CLIENT_ID);
		ClientData member = Model.getLongtermModel().getClient(clientId);
		return member;
	}

	protected ServerData getServerDataFromIntent(Intent intent) {
		String serverId = intent.getStringExtra(Constants.EXTRA_SERVER_ID);
		ServerData server = Model.getLongtermModel().getServer(serverId);
		return server;
	}

	protected ChannelData getChannelDataFromIntent(Intent intent) {
		String serverId = intent.getStringExtra(Constants.EXTRA_SERVER_ID);
		String channelName = intent.getExtras().getString(Constants.EXTRA_CHANNEL_NAME);
		Boolean privateChannel = intent.getExtras().getBoolean(Constants.EXTRA_CHANNEL_PRIVATE);
		ChannelData channelData = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
		return channelData;
	}
}
