package kit.edu.tm.instantsoup.gui.status;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.gui.configuration.CreateChannelActivity;
import kit.edu.tm.instantsoup.model.ChannelData;
import kit.edu.tm.instantsoup.model.Model;
import kit.edu.tm.instantsoup.model.ServerData;
import android.app.ExpandableListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.ExpandableListContextMenuInfo;

public class ServerListActivity extends ExpandableListActivity {

	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver = new ModelServiceBroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auf Actions unterschiedlich reagieren!
			update();
		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ServerExpandableListAdapter(this, Model.getLongtermModel().getAllServers()));
		registerForContextMenu(getExpandableListView());
		//set intent filter
		this.intentFilter = new IntentFilter();
		this.intentFilter.addAction(Constants.ACTION_CHANNEL_OPENED_OR_CLOSED);
		this.intentFilter.addAction(Constants.ACTION_SERVER_JOINED);
		this.intentFilter.addAction(Constants.ACTION_SERVER_LEFT);
	}
	
    private void update() {
    	setListAdapter(new ServerExpandableListAdapter(this, Model.getLongtermModel().getAllServers()));		
	}

	@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {    	
    	ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) menuInfo;
    	int type = ExpandableListView.getPackedPositionType(info.packedPosition);
        
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
        	 menu.setHeaderTitle(getString(R.string.context_channel_menu));
             menu.add(0, 0, 0, getString(R.string.context_join_channel));
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
        	 menu.setHeaderTitle(getString(R.string.context_server_menu));
             menu.add(0, 0, 0, getString(R.string.context_create_channel));
        }        
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ExpandableListContextMenuInfo info = (ExpandableListContextMenuInfo) item.getMenuInfo();
        int type = ExpandableListView.getPackedPositionType(info.packedPosition);   
        int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition); 
        int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition); 
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {        	
        	ChannelData channelData = (ChannelData) this.getExpandableListAdapter().getChild(groupPos, childPos);
        	GuiUtils.joinChannel(this, channelData.getName(), channelData.getServerId(), channelData.isPrivate());            
            return true;
        } else if (type == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {
        	ServerData serverData = (ServerData) this.getExpandableListAdapter().getGroup(item.getGroupId());
            Intent createChannelIntent = new Intent(ServerListActivity.this, CreateChannelActivity.class);  
            createChannelIntent.putExtra(Constants.EXTRA_SERVER_ID, serverData.getPeerId());
            startActivity(createChannelIntent); 
            return true;
        }
        
        return false;
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lobby_list_menu, menu);
        return true;
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
    
    @Override
    protected void onResume() {
    	LocalBroadcastManager.getInstance(this).registerReceiver(intentReceiver, intentFilter);
    	update();
    	super.onResume();
    }

    @Override
    protected void onPause() {
    	LocalBroadcastManager.getInstance(this).unregisterReceiver(intentReceiver);
    	super.onPause();
    }
}
