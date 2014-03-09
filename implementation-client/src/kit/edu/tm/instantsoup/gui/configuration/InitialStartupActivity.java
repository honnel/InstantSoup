package kit.edu.tm.instantsoup.gui.configuration;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.model.Model;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;


/**
 * Start Activity of InstantSOUP, all internal data storage will be created here. Furthermore this class is used for singleton hook-up.
 * @author Daniel Hammann
 *
 */
public class InitialStartupActivity extends Activity {
	
	private EditText editUsername;
    private Button buttonStart;
	private CheckBox cbSaveUsername;
    
    /** Called when the activity is first created. */ 
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.start);					
		
		editUsername = (EditText) findViewById(R.id.editUsername);
		buttonStart = (Button) findViewById(R.id.buttonStart);
		cbSaveUsername = (CheckBox) findViewById(R.id.startCheckbox);	
				
		buttonStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	String username = editUsername.getText().toString();
            	if (username.equals("")) {
            		showDialog(Constants.DIALOG_ERROR_ID);
            	} else {            		
            		storePreferences();
            		Model.getLongtermModel().setNick(username);
            		InitialStartupActivity.this.finish();
            	}           	
            }
        });	
	}
	
	@Override
	public void onStop() {
		super.onStop();
		Model.getLongtermModel().appStopped();
	}
	
	/**
	 * Stores username and checkbox state for restart of instantSOUP
	 */
	private void storePreferences() {
		Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCES, 0).edit();
		if (cbSaveUsername.isChecked()) {	
			String username = editUsername.getText().toString();			
			editor.putBoolean(getString(R.string.pref_key_cb_save_username), true); //save checkbox state
			editor.putString(getString(R.string.pref_key_username), username); //save username		
		} else {
			editor.putBoolean(getString(R.string.pref_key_cb_save_username), false);
			editor.putString(getString(R.string.pref_key_username), "");   
		}
		editor.commit();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Model.getLongtermModel().appStarted(this.getApplicationContext());		
	}
	
	/**
	 * Alert Dialog for empty username-editbox
	 */
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    switch(id) {
	    case Constants.DIALOG_ERROR_ID:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("select username")
				   .setCancelable(false)
				   .setNeutralButton("ok", new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			               dialog.cancel();
			          }
			        });

			dialog = builder.create();
	        break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}
}