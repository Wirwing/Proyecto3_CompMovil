package com.fmat.proyecto3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.fragment.LoadingFragment;
import com.fmat.proyecto3.fragment.MainFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.service.ExerciseRESTService;

public class MainActivity extends SherlockFragmentActivity implements
		MainFragment.OnExerciseSelectedListener {

	private static final String TAG = MainActivity.class.getName();

	private Fragment contentFragment;

	private ExerciseReceiver receiver;
	private IntentFilter filter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contentFragment = MainFragment.newInstance("10002900",
				"Azaneth Aguilar", "Nutricion");

		// Set Convent View
		setContentView(R.layout.activity_content);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (receiver != null)
			return;

		receiver = new ExerciseReceiver();
		filter = new IntentFilter(ExerciseRESTService.INTENT_RESULT_ACTION);
		super.registerReceiver(receiver, filter);

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

		Intent intent = new Intent(this, ExerciseRESTService.class);
		String url = Constants.WS_URL + Constants.EXERCISE_PATH;

		intent.setData(Uri.parse(url));

		// intent.putExtra(ExerciseRESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
		intent.putExtra(ExerciseRESTService.EXTRA_HTTP_RESOURCE_ID, number);

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
						.get(ExerciseRESTService.EXTRA_EXERCISE);

				if (exercise != null) {

					Intent exerciseIntent = new Intent(MainActivity.this,
							ExerciseActivity.class);
					
					exerciseIntent.putExtra(ExerciseActivity.EXTRA_EXERCISE, exercise);
					
					startActivity(exerciseIntent);
					
				}

			} else {
				Log.e(TAG, errorMessage);
			}

		}
	}

}
