/**
 * 
 */
package com.fmat.proyecto3;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.fmat.proyecto3.R;

/**
 * @author Fabián Castillo
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getFragmentManager().beginTransaction()
		// .replace(android.R.id.content, new SettingsFragment()).commit();
		addPreferencesFromResource(R.xml.preferences);
		SharedPreferences preferences = getPreferenceScreen()
				.getSharedPreferences();

		int[] settingsResNames = { R.string.pref_server_url,
				R.string.pref_student_id, R.string.pref_student_name,
				R.string.pref_student_career,
				R.string.pref_server_get_exercise,
				R.string.pref_server_send_exercise, R.string.pref_dropbox_dir };

		String notSet = getString(R.string.not_set);
		for (int settingResId : settingsResNames) {
			String settingKey = getString(settingResId);
			Preference preference = findPreference(settingKey);
			preference.setSummary(preferences.getString(settingKey, notSet));
		}
	}

	@Override
	protected void onResume() {
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

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Preference pref = findPreference(key);
		if (pref instanceof EditTextPreference) {
			EditTextPreference etp = (EditTextPreference) pref;
			String value = etp.getText();
			String summary = TextUtils.isEmpty(value) ? getString(R.string.not_set) : value;
			pref.setSummary(summary);
		}
	}

	private void loadDropboxSettingSummary() {
		String dbKey = getString(R.string.pref_dropbox_dir);
		Preference pref = findPreference(dbKey);
		String summary = getPreferenceScreen().getSharedPreferences()
				.getString(dbKey, getString(R.string.not_set));
		pref.setSummary(summary);
	}

}
