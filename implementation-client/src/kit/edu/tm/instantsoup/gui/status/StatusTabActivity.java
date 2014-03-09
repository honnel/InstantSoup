package kit.edu.tm.instantsoup.gui.status;



import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.gui.configuration.CreateChannelActivity;
import kit.edu.tm.instantsoup.gui.configuration.InitialStartupActivity;
import kit.edu.tm.instantsoup.gui.configuration.SettingsActivity;
import kit.edu.tm.instantsoup.model.Model;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

public class StatusTabActivity extends TabActivity /*implements OnGesturePerformedListener*/ {
	/*private GestureLibrary gestureLibrary;*/	
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.status_tab_view);	    
	    	    
	    boolean checked = getSharedPreferences(Constants.SHARED_PREFERENCES, 0).getBoolean(getString(R.string.pref_key_cb_save_username), false);
	    if (!checked) {
	    	startActivityForResult(new Intent(this, InitialStartupActivity.class), 0);
	    } else {
	    	Model.getLongtermModel().setNick(getSharedPreferences(Constants.SHARED_PREFERENCES, 0).getString(getString(R.string.pref_key_username), "Unknown stranger"));	    	
	    	GuiUtils.joinSoupTextHint(this); 
	    }
	    initTabs();
	    
	    /*gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.spells);
        if (!gestureLibrary.load()) {
        	finish();
        }

        GestureOverlayView gestures = (GestureOverlayView) findViewById(R.id.gestures);
        gestures.addOnGesturePerformedListener(this);*/
	    
	   
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 GuiUtils.joinSoupTextHint(this);
	 }

	@Override
	public void onStart() {
		super.onStart();
		Model.getLongtermModel().appStarted(this.getApplicationContext());	
		WifiManager wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		if (!wifi.isWifiEnabled()) {
			Toast.makeText(this, "Wifi connection isn't enabled!" , Toast.LENGTH_SHORT).show();
		}
	}
	
    @Override
	public void onStop() {    	
		Model.getLongtermModel().appStopped();
		super.onStop();
	}
    
    private void initTabs() {
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, LobbyActivity.class);
	    spec = tabHost.newTabSpec(Constants.LOBBY_SMALL);
	    spec.setIndicator(Constants.LOBBY_BIG, res.getDrawable(R.drawable.ic_tab_lobby));
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, ChannelListActivity.class);
	    spec = tabHost.newTabSpec(Constants.CHANNEL_LIST);
	    spec.setIndicator(Constants.CHANNEL, res.getDrawable(R.drawable.ic_tab_channel_list));
	    spec.setContent(intent);
	    tabHost.addTab(spec);

	    intent = new Intent().setClass(this, ServerListActivity.class);
	    spec = tabHost.newTabSpec(Constants.SERVER_LIST);
	    spec.setIndicator(Constants.SERVERS, res.getDrawable(R.drawable.ic_tab_server_list));
	    spec.setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_channel: Intent createChannelIntent = new Intent(this, CreateChannelActivity.class);            		
            						  this.startActivity(createChannelIntent); 
                                break;
            case R.id.settings: Intent createSettingsIntent = new Intent(this, SettingsActivity.class);            		
								this.startActivity(createSettingsIntent); 
                                break;
            case R.id.close:    Model.getLongtermModel().stopLongtermModel();
            					finish();
								break;
        }
        return true;
    }

	/*@Override
	public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
		ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);

		// We want at least one prediction
		if (predictions.size() > 0) {
			Prediction prediction = predictions.get(0);
			// We want at least some confidence in the result
			if (prediction.score > 1.0) {
				// Show the spell
				Toast.makeText(this, prediction.name, Toast.LENGTH_SHORT).show();
			}
		}		
	}*/
}
