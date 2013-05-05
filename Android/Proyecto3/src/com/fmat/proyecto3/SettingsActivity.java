/**
 * 
 */
package com.fmat.proyecto3;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

/**
 * @author Fabián Castillo
 * 
 */
@SuppressLint("NewApi")
public class SettingsActivity extends PreferenceActivity {

	static final String PREF_SERVER = "pref_server";
	static final String PREF_STUDENT_ID = "pref_student_id";
	static final String PREF_STUDENT_NAME = "pref_student_name";
	static final String PREF_STUDENT_CAREER = "pref_student_career";
	static final String PREF_API_GET_EXERCISE = "pref_get_exercise";
	static final String PREF_API_SEND_EXERCISE = "pref_send_exercise";
	static final String PREF_DROPBOX_DIR = "pref_dropbox_dir";
	static final String NOT_SET = "Not set";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

	public static class SettingsFragment extends PreferenceFragment implements
			OnSharedPreferenceChangeListener {

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.preferences);
			SharedPreferences preferences = getPreferenceScreen()
					.getSharedPreferences();

			String[] settingsNames = { PREF_SERVER, PREF_STUDENT_ID,
					PREF_STUDENT_NAME, PREF_STUDENT_CAREER,
					PREF_API_GET_EXERCISE, PREF_API_SEND_EXERCISE };

			for (String setting : settingsNames) {
				EditTextPreference editTextPref = (EditTextPreference) findPreference(setting);
				editTextPref
						.setSummary(preferences.getString(setting, NOT_SET));
			}
			Preference dbPreference = findPreference(PREF_DROPBOX_DIR);
			dbPreference.setSummary(preferences.getString(PREF_DROPBOX_DIR, NOT_SET));
		}

		@Override
		public void onResume() {
			super.onResume();
			getPreferenceScreen().getSharedPreferences()
					.registerOnSharedPreferenceChangeListener(this);
			loadDropboxSettingSummary();
		}

		public void onPause() {
			super.onPause();
			getPreferenceScreen().getSharedPreferences()
					.unregisterOnSharedPreferenceChangeListener(this);
		}

		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			Preference pref = findPreference(key);
			if (pref instanceof EditTextPreference) {
				EditTextPreference etp = (EditTextPreference) pref;
				String value = etp.getText();
				String summary = TextUtils.isEmpty(value) ? NOT_SET : value;
				pref.setSummary(summary);
			} else {
				String summary = sharedPreferences.getString(PREF_DROPBOX_DIR, NOT_SET);
				pref.setSummary(summary);
			}
		}
		
		private void loadDropboxSettingSummary() {
			Preference pref = findPreference(PREF_DROPBOX_DIR);
			String summary = getPreferenceScreen().getSharedPreferences().getString(PREF_DROPBOX_DIR, NOT_SET);
			pref.setSummary(summary);
		}

	}

}
