package kit.edu.tm.instantsoup.model;

import java.util.List;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.gui.chat.ChatActivity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * This receiver listens for MESSAGE_RECEIVED actions and shows a status bar notification if the application is
 * currently not visible for the user. It should be registered with a filter only accepting the MESSAGE_RECEIVED action.
 * 
 * @author Florian Schreier
 */
public class BroadcastToNotification extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		boolean isRunning = true; // show no notifications if unsure
		try {
			isRunning = new ForegroundCheckTask().execute(context).get();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!isRunning && intent.getAction().equals(Constants.ACTION_MESSAGE_RECEIVED)) {
			NotificationManager notificationManager;
			notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

			long time = intent.getLongExtra(Constants.EXTRA_MESSAGE_TIME, System.currentTimeMillis());

			CharSequence contentTitle = context.getString(R.string.notification_title);
			CharSequence contentText = this.getText(context, intent);
			Intent notificationIntent = new Intent(context, ChatActivity.class);
			notificationIntent.putExtra(Constants.EXTRA_SERVER_ID, intent.getStringExtra(Constants.EXTRA_SERVER_ID));
			notificationIntent.putExtra(Constants.EXTRA_CHANNEL_NAME,
					intent.getStringExtra(Constants.EXTRA_CHANNEL_NAME));
			notificationIntent.putExtra(Constants.EXTRA_CHANNEL_PRIVATE,
					intent.getBooleanExtra(Constants.EXTRA_CHANNEL_PRIVATE, false));

			PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
			
			Notification notification = new Notification(R.drawable.soup, this.getText(context, intent), time);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			notification.flags |= Notification.FLAG_AUTO_CANCEL;

			notificationManager.notify(Constants.HELLO_ID, notification);
		}
	}
	
	private String getText(Context context, Intent intent) {
		String extraMessageAuthor = intent.getStringExtra(Constants.EXTRA_MESSAGE_AUTHOR);
		String extraMessageText = intent.getStringExtra(Constants.EXTRA_MESSAGE_TEXT);
		
		ClientData authorClientData = Model.getLongtermModel().getClient(extraMessageAuthor);
		String author;
		if (authorClientData != null) {
			author = authorClientData.getNick();
		} else {
			author = context.getString(R.string.unknown);
		}
		
		return author + ": " + extraMessageText;
	}

	/**
	 * This checks if app is in the foreground by using a background task.
	 * 
	 * @author Idolon
	 */
	private class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}
			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}

}
