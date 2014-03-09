package kit.edu.tm.instantsoup.gui.status;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.gui.chat.ChatActivity;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.Model;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public final class GuiUtils {
	private GuiUtils() {

	}

	public static boolean isChannelJoined(String channelName, String serverId, boolean isPrivate) {
		boolean result = false;
		for (ChannelData channel : Model.getLongtermModel().getJoinedChannels()) {
			if (!result) {
				result = channel.getName().equals(channelName)
						&& channel.getServerId().equals(serverId)
						&& channel.isPrivate() == isPrivate;
			}
		}
		return result;
	}

	public static void joinChannel(Context context, String channelName, String serverId, boolean isPrivate) {
		if (!isChannelJoined(channelName, serverId, isPrivate)) {
			Toast.makeText(context, Model.getLongtermModel().getNick() + " joins " + channelName + "...",
					Toast.LENGTH_SHORT).show();
		}
		Intent chatIntent = new Intent(context, ChatActivity.class);
		chatIntent.putExtra(Constants.EXTRA_CHANNEL_NAME, channelName);
		chatIntent.putExtra(Constants.EXTRA_SERVER_ID, serverId);
		chatIntent.putExtra(Constants.EXTRA_CHANNEL_PRIVATE, isPrivate);
		context.startActivity(chatIntent);
	}

	public static void leaveChannel(Context context, ChannelData channelData) {
		Toast.makeText(context, Model.getLongtermModel().getNick() + " leaves " + channelData.getName() + "...",
				Toast.LENGTH_SHORT).show();
		Model.getLongtermModel().leaveChannel(channelData);
	}

	public static void joinSoupTextHint(Context context) {
		Toast.makeText(context, Model.getLongtermModel().getNick() + " joins the SOUP...", Toast.LENGTH_SHORT).show();
	}
}
