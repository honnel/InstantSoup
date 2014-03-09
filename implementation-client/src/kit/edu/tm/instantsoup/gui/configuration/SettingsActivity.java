package kit.edu.tm.instantsoup.gui.configuration;

import kit.edu.tm.instantsoup.Constants;
import kit.edu.tm.instantsoup.R;
import kit.edu.tm.instantsoup.model.Model;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/* Some initializations */
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		ListView listView = new ListView(this);
		listView.setId(android.R.id.list);
		listView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		layout.addView(listView);

		this.setContentView(layout);
		/* Preferences time! (we build the preferences) */
		DialogPreference nickDialog = new EditNickPreference(this);
		Preference version = getPreference("InstantSOUP", "Version 0.0.0.1", null);
		Preference author = getPreference("Authors", "Pascal Becker, Daniel Hammann, Florian Schreier", null);

		DialogPreference deleteModel = new ModelDeletePreference(this, "Delete internal data model",
				"Are you really shure?");

		/* Now we add the preferences to the preference screen */
		PreferenceScreen preferenceScreen = this.getPreferenceManager().createPreferenceScreen(this);
		addPreferenceCategory(preferenceScreen, "Settings", nickDialog, deleteModel, version, author);
		this.setPreferenceScreen(preferenceScreen);
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

	private boolean addPreferenceCategory(PreferenceScreen preferenceScreen, String titleCategory,
			Preference... preferences) {
		boolean addPreference = false;
		for (Preference preference : preferences) {
			if (preference != null)
				addPreference = true;
		}
		if (addPreference) {
			PreferenceCategory preferenceCategory = new PreferenceCategory(this);
			preferenceCategory.setTitle(titleCategory);
			preferenceScreen.addPreference(preferenceCategory);
			for (Preference preference : preferences) {
				if (preference != null)
					preferenceCategory.addPreference(preference);
			}
			return true;
		} else
			return false;
	}

	private Preference getPreference(String title, String summary, Intent intent) {
		Preference pref = new Preference(this);
		pref.setTitle(title);
		pref.setSummary(summary);
		if (intent != null)
			pref.setIntent(intent);
		return pref;
	}

	private class EditNickPreference extends EditTextPreference {

		public EditNickPreference(Context context) {
			super(context);
			String nick = Model.getLongtermModel().getNick();
			this.setTitle("Nickname");
			this.setSummary(nick);
			this.setDialogMessage("Change nickname:");
			this.setText(nick);
		}

		/**
		 * {@inheritDoc}
		 */
		protected void onDialogClosed(boolean positiveResult) {
			// Deletes model after the ok button is clicked.
			if (positiveResult) {
				String newNick = getEditText().getText().toString();
				Model.getLongtermModel().setNick(newNick);
				/* store nickname persistent */
				boolean isNickStoredPersitent = SettingsActivity.this.getSharedPreferences(Constants.SHARED_PREFERENCES, 0).getBoolean(getString(R.string.pref_key_cb_save_username), false);
				if (isNickStoredPersitent) {
					Editor editor =  SettingsActivity.this.getSharedPreferences(Constants.SHARED_PREFERENCES, 0).edit();
					editor.putString(getString(R.string.pref_key_username), newNick); //save username persistent
					editor.commit();
				}
				this.setSummary(newNick);
			}

			super.onDialogClosed(positiveResult);
		}

	}

	private class ModelDeletePreference extends DialogPreference {
		public ModelDeletePreference(Context context, String title, String text) {
			super(context, null);
			this.setTitle(title);
			this.setSummary("This can't be reverted");
			this.setDialogMessage(text);
		}

		/**
		 * {@inheritDoc}
		 */
		protected void onDialogClosed(boolean positiveResult) {
			// Deletes model after the ok button is clicked.
			if (positiveResult) {
				Model.getLongtermModel().appStarted(super.getContext());
				Model.getLongtermModel().stopLongtermModel();
				Model.getLongtermModel().appStopped();
			}

			super.onDialogClosed(positiveResult);
		}
	}
}

/*
 * public class SettingsActivity extends Activity {
 * 
 * @Override public void onCreate(Bundle savedInstanceState) {
 * super.onCreate(savedInstanceState); setContentView(R.layout.settings); }
 * 
 * @Override public void onStart() { super.onStart();
 * Model.getLongtermModel().appStarted(this.getApplicationContext()); }
 * 
 * @Override public void onStop() { Model.getLongtermModel().appStopped();
 * super.onStop(); }
 */

