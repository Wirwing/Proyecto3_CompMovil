package com.fmat.proyecto3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class BaseActivity extends SherlockFragmentActivity {

	private static final String TAG = BaseActivity.class.getName();

	private SharedPreferences preferences;

	protected String studentId;
	protected String studentName;
	protected String studentCareer;
	protected String dropboxFolder;
	protected String wsUrl;
	protected String wsExercisePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set Convent View
		setContentView(R.layout.activity_content);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		loadSettings();

		if (!hasAllSettings()) {
			Toast.makeText(this, "Configura la aplicación primero",
					Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(this, SettingsActivity.class),
					200);
		}

	}

	protected void loadSettings() {

		studentId = preferences.getString(getString(R.string.pref_student_id),
				null);

		studentName = preferences.getString(
				getString(R.string.pref_student_name), null);

		studentCareer = preferences.getString(
				getString(R.string.pref_student_career), null);

		dropboxFolder = preferences.getString(
				getString(R.string.pref_dropbox_dir), null);

		wsUrl = preferences
				.getString(getString(R.string.pref_server_url), null);

		wsExercisePath = preferences.getString(
				getString(R.string.pref_server_get_exercise), null);

	}

	private boolean hasAllSettings() {

		return studentId != null && studentName != null
				&& studentCareer != null && dropboxFolder != null
				&& wsUrl != null && wsExercisePath != null ? true : false;

	}

	protected void switchFragment(Fragment contentFragment) {

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		loadSettings();
		if (!hasAllSettings()) {
			Toast.makeText(this, "Configura la aplicación primero",
					Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(this, SettingsActivity.class),
					200);
		}
	}

}
