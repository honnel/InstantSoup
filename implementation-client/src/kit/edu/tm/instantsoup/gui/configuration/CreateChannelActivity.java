package kit.edu.tm.instantsoup.gui.configuration;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.gui.status.GuiUtils;
import kit.edu.tm.instantsoup.gui.status.LobbyListAdapter;
import kit.edu.tm.instantsoup.model.Model;
import kit.edu.tm.instantsoup.model.ServerData;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;

public class CreateChannelActivity extends Activity{	
	private static final int DIALOG_SELECT_MEMBERS = 0;
	private EditText editChannelName;
	private Spinner spinnerServerList;
	private CheckBox cbPrivateChannel;
	private Button btnCreate;
	private EditText editPrivateChannelMembers;
	private Button btnSelectMembers;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_channel);	
		editChannelName = (EditText) findViewById(R.id.channelName);
		spinnerServerList = (Spinner) findViewById(R.id.serverListSpinner);
		cbPrivateChannel = (CheckBox) findViewById(R.id.cbPrivateChannel);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		editPrivateChannelMembers = (EditText) findViewById(R.id.privateChannelMembers);
		btnSelectMembers = (Button) findViewById(R.id.btnSelectMembers);
		spinnerServerList.setAdapter(new ArrayAdapter<ServerData>(this, R.layout.message, Model.getLongtermModel().getAllServers()));
		
		cbPrivateChannel.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,	boolean isChecked) {
					editPrivateChannelMembers.setVisibility(isChecked ? View.VISIBLE : View.GONE);		
					btnSelectMembers.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				}				
		});
		
		btnSelectMembers.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_SELECT_MEMBERS);
			}
		});		
		
		btnCreate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String channelName = editChannelName.getText().toString();
            	if (channelName.equals("")) {
            		showDialog(Constants.DIALOG_ERROR_ID);
            	} else { 
            		ServerData server = (ServerData) spinnerServerList.getSelectedItem();
            		if (server.getAllChannels().contains(channelName)) {
            			showDialog(Constants.DIALOG_ERROR_ID);
            		} else {
            			if (cbPrivateChannel.isChecked()) {
            				Model.getLongtermModel().createChannel(server, channelName, true);
            				GuiUtils.joinChannel(CreateChannelActivity.this, channelName, server.getPeerId(), true);
            			} else {
            				Model.getLongtermModel().createChannel(server, channelName, false);
            				GuiUtils.joinChannel(CreateChannelActivity.this, channelName, server.getPeerId(), false);
            			}
            		}
            	}
			}			
		});
	}
	
	public Dialog onCreateDialog(int id, Bundle bundle) {
	    Dialog dialog;
	    switch(id) {
	    case DIALOG_SELECT_MEMBERS:
	    	Builder builder = new AlertDialog.Builder(this);
	    	
	    	builder.setIcon(R.drawable.ic_menu_create_channel);
            builder.setTitle("Select Members");
            DialogInterface.OnClickListener listener =  new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Yes so do some stuff */
                }
            };
            builder.setAdapter(new LobbyListAdapter(this, Model.getLongtermModel().getAllClients()), listener);
            
//            builder.setMultiChoiceItems(R.array.select_dialog_items3,
//                    new boolean[]{false, true, false, true, false, false, false},
//                    new DialogInterface.OnMultiChoiceClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton,
//                                boolean isChecked) {
//
//                            /* User clicked on a check box do some stuff */
//                        }
//                    })
//            .setPositiveButton(R.string.alert_dialog_ok,
//                    new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    /* User clicked Yes so do some stuff */
//                }
//            })
//            .setNegativeButton(R.string.alert_dialog_cancel,
//                    new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int whichButton) {
//
//                    /* User clicked No so do some stuff */
//                }
//            })
//           .create();
	    	
	    	dialog = builder.create();
	    	
	        break;
	    default:
	        dialog = null;
	    }
		return dialog;		
	}
	
    @Override
	public void onStart() {
		super.onStart();
		Model.getLongtermModel().appStarted(this.getApplicationContext());
	}
	
    @Override
	public void onStop() {    	
		Model.getLongtermModel().appStopped();
		super.onStop();
	}
	
	
}
