package kit.edu.tm.instantsoup.gui.status;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.model.Model;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;


public class LobbyActivity extends ListActivity {
	
	private IntentFilter intentFilter;
	private BroadcastReceiver intentReceiver = new ModelServiceBroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {			
			 update();
		}		
	};
	
	public void onCreate(Bundle savedInstanceState) {              
        super.onCreate(savedInstanceState);
        setListAdapter(new LobbyListAdapter(this, Model.getLongtermModel().getAllClients()));        
		this.intentFilter = new IntentFilter();
		this.intentFilter.addAction(Constants.ACTION_CLIENT_JOINED);
		this.intentFilter.addAction(Constants.ACTION_CLIENT_RENAMED);
		this.intentFilter.addAction(Constants.ACTION_CLIENT_LEFT);
    }
	
	private void update() {
		setListAdapter(new LobbyListAdapter(this, Model.getLongtermModel().getAllClients()));		 
	}
	
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) 
    {    
       ((StatusListAdapter)getListAdapter()).toggle(position);
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
