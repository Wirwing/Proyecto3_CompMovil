package com.fmat.proyecto3;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class BaseActivity extends SherlockFragmentActivity {

	private static final String TAG = BaseActivity.class.getName();

	protected Fragment contentFragment;

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

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

	}

	protected void loadSettings() {

		if (preferences.contains(getString(R.string.pref_student_id)))
			studentId = preferences.getString(
					getString(R.string.pref_student_id), null);

		if (preferences.contains(getString(R.string.pref_student_name)))
			studentName = preferences.getString(
					getString(R.string.pref_student_name), null);

		if (preferences.contains(getString(R.string.pref_student_career)))
			studentCareer = preferences.getString(
					getString(R.string.pref_student_career), null);

		if (preferences.contains(getString(R.string.pref_dropbox_dir)))
			dropboxFolder = preferences.getString(
					getString(R.string.pref_dropbox_dir), null);

		if (preferences.contains(getString(R.string.pref_server_url)))
			wsUrl = preferences.getString(getString(R.string.pref_server_url),
					null);

		if (preferences.contains(getString(R.string.pref_server_get_exercise)))
			wsExercisePath = preferences.getString(
					getString(R.string.pref_server_get_exercise), null);

	}

	protected boolean hastAllSettings() {

		return studentId != null && studentName != null
				&& studentCareer != null && dropboxFolder != null
				&& wsUrl != null && wsExercisePath != null ? true : false;

	}
}
