package kit.edu.tm.instantsoup.gui.chat;

import java.util.Date;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.ClientData;
import kit.edu.tm.instantsoup.model.Message;
import kit.edu.tm.instantsoup.model.Model;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ChatActivity extends Activity {

	private ArrayAdapter<String> mConversationArrayAdapter;
	private ListView mConversationView;
	private EditText mOutEditText;
	private OnEditorActionListener mWriteListener;
	private Button mSendButton;
	private TextView titleBarLeft;
	private TextView titleBarRight;
	private IntentFilter intentFilter;	
	private String channelName;
	private String serverId;
	private boolean privateChannel;

	private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	if (channelName.equals(intent.getExtras().getString(Constants.EXTRA_CHANNEL_NAME))) {
		    	if (intent.getAction().equals(Constants.ACTION_MEMBER_JOINED)) {	    		
		    		memberJoined(getClientDataFromIntent(intent));
		    	}
		    	if (intent.getAction().equals(Constants.ACTION_MEMBER_LEFT)) {
		    		memberLeft(getClientDataFromIntent(intent));
		    	}
		    	if (intent.getAction().equals(Constants.ACTION_MESSAGE_RECEIVED)) {
		    		ClientData messageAuthor = getClientDataFromIntent(intent);
		    		String text = intent.getStringExtra(Constants.EXTRA_MESSAGE_TEXT);
		    		Date time = new Date(intent.getLongExtra(Constants.EXTRA_MESSAGE_TIME, System.currentTimeMillis()));
		    		Message message = new Message(time, messageAuthor, text);
					messageReceived(message);
		    	}
	    	}
	    }

		private ClientData getClientDataFromIntent(Intent intent) {
			String clientId = intent.getStringExtra(Constants.EXTRA_MESSAGE_AUTHOR);
    		ClientData member = Model.getLongtermModel().getClient(clientId);
			return member;
		}
	};

	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.chat);	
		
		intentFilter = new IntentFilter();
		intentFilter.addAction(Constants.ACTION_MESSAGE_RECEIVED);
		
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
		titleBarLeft = (TextView) findViewById(R.id.title_left_text); 
		titleBarLeft.setText(R.string.app_name);
        titleBarRight = (TextView) findViewById(R.id.title_right_text);
		
		channelName = getIntent().getExtras().getString(Constants.EXTRA_CHANNEL_NAME);
		serverId = getIntent().getExtras().getString(Constants.EXTRA_SERVER_ID);
		privateChannel = getIntent().getExtras().getBoolean(Constants.EXTRA_CHANNEL_PRIVATE, false);
		titleBarRight.setText(channelName);		
	}
	
    @Override
	public void onStart() {
		super.onStart();
		ChannelData channel = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
		setupChat(channel);
		Model.getLongtermModel().appStarted(this.getApplicationContext());
	}
	
    @Override
	public void onStop() {    	
		Model.getLongtermModel().appStopped();
		super.onStop();
	}
    
    @Override
    protected void onResume() {
    	mConversationArrayAdapter.clear();
    	ChannelData channel = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
    	for (Message message : channel.getMessages()) {
    		mConversationArrayAdapter.add(messageBuilder(message.getText(), message.getAuthor().getNick()));
		}    	
    	LocalBroadcastManager.getInstance(this).registerReceiver(intentReceiver, intentFilter);
    	super.onResume();
    }

    @Override
    protected void onPause() {
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(intentReceiver);
    	super.onPause();
    }

	private void setupChat(ChannelData channel) {
		mConversationArrayAdapter = new ArrayAdapter<String>(this, R.layout.message);
        mConversationView = (ListView) findViewById(R.id.in);
        mConversationView.setAdapter(mConversationArrayAdapter);
        
        // Initialize the compose field with a listener for the return key
        mOutEditText = (EditText) findViewById(R.id.edit_text_out);
        mOutEditText.setOnEditorActionListener(mWriteListener);
        
        // Initialize the send button with a listener that for click events
        mSendButton = (Button) findViewById(R.id.button_send);
        mSendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	Model m = Model.getLongtermModel();
            	// Send a message using content of the edit text widget
                TextView view = (TextView) findViewById(R.id.edit_text_out);
                String message = view.getText().toString();
                if (!message.equals("")) {
                	//m.receivedSay(channel, new java.util.Date(), new ClientData(m.getId(),m.getNick()), message);
                	ChannelData channel = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
                	m.sendMessageToChannel(channel, message);
	                mOutEditText.setText("");
                }
            }
        });
	}
	
	private String messageBuilder(String message, String nick) {	
		String time = android.text.format.DateFormat.format("kk:mm", new java.util.Date()).toString();
		String wellFormatedMessage = String.format("%s (%s): %s", nick, time, message);
		return wellFormatedMessage;
	}

	public void memberJoined(ClientData member) {
		ChannelData channel = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
		Toast.makeText(ChatActivity.this, member.getNick() + " joins " + channel.getName() + "..." , Toast.LENGTH_SHORT).show();	
	}

	public void memberLeft(ClientData member) {
		ChannelData channel = Model.getLongtermModel().getChannel(serverId, channelName, privateChannel);
		Toast.makeText(ChatActivity.this, member.getNick() + " lefts " + channel.getName() + "..." , Toast.LENGTH_SHORT).show();		
	}

	public void messageReceived(Message message) {
		String nick = "?";
		if (message.getAuthor() != null && message.getAuthor().getNick() != null) {
			nick = message.getAuthor().getNick();
		}
		mConversationArrayAdapter.add(messageBuilder(message.getText(), nick));		
	}
	
}
