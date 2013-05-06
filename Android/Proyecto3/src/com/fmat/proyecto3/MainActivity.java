package com.fmat.proyecto3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fmat.proyecto3.fragment.LoadingFragment;
import com.fmat.proyecto3.fragment.MainFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.service.ExerciseGetService;
import com.fmat.proyecto3.service.ExerciseRESTService;

public class MainActivity extends BaseActivity  implements
		MainFragment.OnExerciseSelectedListener {

	private static final String TAG = MainActivity.class.getName();

	private Fragment contentFragment;

	private ExerciseReceiver receiver;
	private IntentFilter filter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		loadSettings();
		
		if(!super.hastAllSettings())
			startActivity(new Intent(this, SettingsActivity.class));
	
		if(!super.hasDropboxCredentials())
			startActivity(new Intent(this, DropboxCredentialsActivity.class));
		
		contentFragment = MainFragment.newInstance(studentId,
				studentName, studentCareer);

		// Set Convent View
		setContentView(R.layout.activity_content);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

		
		if (receiver != null)
			return;
		
		receiver = new ExerciseReceiver();
		filter = new IntentFilter(ExerciseGetService.INTENT_RESULT_ACTION);
		super.registerReceiver(receiver, filter);

	}

	/**
	 * Infla el menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Para fines de prueba se configuración
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

	}

	@Override
	public void onExerciseSelected(String number) {

		number = "1";

		String message = "Loading exercise " + number;

		Log.i(TAG, "Exercise number: " + number);

		Fragment messageFragment = LoadingFragment.newInstance(message);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, messageFragment).commit();

		Intent intent = new Intent(this, ExerciseGetService.class);
		String url = wsUrl + wsExercisePath;

		intent.setData(Uri.parse(url));

		// intent.putExtra(ExerciseRESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
		intent.putExtra(ExerciseGetService.EXTRA_HTTP_RESOURCE_ID, number);

		startService(intent);

	}

	class ExerciseReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();

			String errorMessage = extras
					.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

			if (errorMessage == null) {

				Exercise exercise = (Exercise) extras
						.get(Exercise.EXTRA_EXERCISE);

				if (exercise != null) {

					Intent exerciseIntent = new Intent(MainActivity.this,
							ExerciseActivity.class);

					exerciseIntent.putExtra(Exercise.EXTRA_EXERCISE,
							exercise);

					startActivity(exerciseIntent);

				}

			} else {
				Log.e(TAG, errorMessage);
			}

		}
	}

}
