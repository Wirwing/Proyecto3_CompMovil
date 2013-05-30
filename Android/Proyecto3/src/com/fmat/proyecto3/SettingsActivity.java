/**
 * 
 */
package com.fmat.proyecto3;

import com.fmat.proyecto3.todoist.ScheduledExercisesTracker;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
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

	/**
	 * @see android.preference.PreferenceActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		setContentView(R.layout.activity_preferences);

		//((Button) findViewById(R.id.bnt_clear_dropbox))
			//	.setOnClickListener(this);

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

		Preference clearTodoistPref = findPreference(getString(R.string.pref_clear_todoist));
		clearTodoistPref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						clearTodoistInfo();
						loadTodoistSettingSummary();
						ScheduledExercisesTracker tracker = new ScheduledExercisesTracker(getApplicationContext());
						tracker.clearScheduleRegistry();
						return false;
					}
				});
		
		Preference clearDropboxPref = findPreference(getString(R.string.pref_clear_dropbox));
		clearDropboxPref
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {
					@Override
					public boolean onPreferenceClick(Preference preference) {
						clearDropboxInfo();
						loadDropboxSettingSummary();
						return false;
					}
				});
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		loadDropboxSettingSummary();
		loadTodoistSettingSummary();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences,
	 *      java.lang.String)
	 */
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

	/**
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

	/**
	 * Carga la información de las preferencias de todoist Se realiza aparte
	 * porque esta preferencia se realiza mediante un intent Y
	 * PreferenceActivity no escucha ese evento
	 */
	private void loadTodoistSettingSummary() {
		String projectKey = getString(R.string.pref_todoist_project_name);
		Preference pref = findPreference(projectKey);
		String summary = getPreferenceScreen().getSharedPreferences()
				.getString(projectKey, getString(R.string.not_set));
		pref.setSummary(summary);
	}

	private void clearDropboxInfo() {
		String key = getString(R.string.dropbox_access_key);
		String secret = getString(R.string.dropbox_access_secret);

		String folder = getString(R.string.pref_dropbox_dir);

		Editor editor = getPreferenceScreen().getSharedPreferences().edit();
		editor.remove(key);
		editor.remove(secret);
		editor.remove(folder);

		editor.commit();

		Preference pref = findPreference(folder);
		String summary = getString(R.string.not_set);
		pref.setSummary(summary);
	}

	private void clearTodoistInfo() {
		String token = getString(R.string.pref_todoist_token);
		String projectId = getString(R.string.pref_todoist_project_id);
		String projectName = getString(R.string.pref_todoist_project_name);

		Editor editor = getPreferenceScreen().getSharedPreferences().edit();
		editor.remove(token);
		editor.remove(projectId);
		editor.remove(projectName);

		editor.commit();

		Preference pref = findPreference(projectName);
		String summary = getString(R.string.not_set);
		pref.setSummary(summary);

	}

	/**
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
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
				.getString(getString(R.string.not_set),
						getString(R.string.not_set));
		pref.setSummary(summary);

	}
}
