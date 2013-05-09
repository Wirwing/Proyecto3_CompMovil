/**
 * 
 */
package com.fmat.proyecto3;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Actividad para la configuración de las preferencias del usuario.
 * 
 * @author Fabián Castillo
 * 
 */
public class SettingsActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener, OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_preferences);

		((Button) findViewById(R.id.bnt_clear_dropbox))
				.setOnClickListener(this);

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
			String summary = TextUtils.isEmpty(value) ? getString(R.string.not_set)
					: value;
			pref.setSummary(summary);
		}
	}

	/*
	 * Carga la información de las preferencias del dir de dropbox Se realiza
	 * aparte porque esta preferencia se realiza mediante un intent Y
	 * PreferenceActivity no escucha ese evento
	 */
	private void loadDropboxSettingSummary() {
		String dbKey = getString(R.string.pref_dropbox_dir);
		Preference pref = findPreference(dbKey);
		String summary = getPreferenceScreen().getSharedPreferences()
				.getString(dbKey, getString(R.string.not_set));
		pref.setSummary(summary);
	}

	@Override
	public void onClick(View v) {

		String key = getString(R.string.dropbox_access_key);
		String secret = getString(R.string.dropbox_access_secret);

		String folder = getString(R.string.pref_dropbox_dir);

		Editor editor = getPreferenceScreen().getSharedPreferences().edit();
		editor.remove(key);
		editor.remove(secret);
		editor.remove(folder);

		editor.commit();

		Preference pref = findPreference(folder);
		String summary = getPreferenceScreen().getSharedPreferences()
				.getString(getString(R.string.not_set), getString(R.string.not_set));
		pref.setSummary(summary);
		
	}
}
