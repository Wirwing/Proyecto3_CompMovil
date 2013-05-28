package com.fmat.proyecto3;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.fmat.proyecto3.fragment.LoadingFragment;
import com.fmat.proyecto3.fragment.MainFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.service.ExerciseGetService;
import com.fmat.proyecto3.service.ExerciseRESTService;
import com.fmat.proyecto3.service.LocationTrackingHandler;

/**
 * Clase principal que administra las opciones principales de la aplicacion:
 * Iniciar un ejercicio y establecer configuracion.
 * 
 * @author Irving
 * 
 */
public class MainActivity extends BaseActivity implements
		MainFragment.OnExerciseSelectedListener,
		LocationTrackingHandler.OnLocationTrackingLocationChanged {

	private static final String TAG = MainActivity.class.getName();

	private Fragment contentFragment;

	private ExerciseReceiver receiver;
	private IntentFilter filter;

	private ArrayList<Exercise> exercises;

	private boolean alreadyFetching = false;

	private LocationTrackingHandler locationHandler;

	/**
	 * @see com.fmat.proyecto3.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		locationHandler = new LocationTrackingHandler(this);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		  /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
		locationHandler.onConnect();

		
	}
	
	
	 /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

    	locationHandler.onDisconnect();

        super.onStop();
    }
	
	
	/**
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (exercises == null && !alreadyFetching) {
			initFetchExercises();
		}

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

	/**
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onMenuItemSelected(int,
	 *      android.view.MenuItem)
	 */
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, SettingsActivity.class);
			startActivityForResult(intent, 200);
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * @see com.fmat.proyecto3.BaseActivity#onActivityResult(int, int,
	 *      android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();

		overridePendingTransition(0, 0);
		startActivity(intent);

	}

	/**
	 * @see com.fmat.proyecto3.fragment.MainFragment.OnExerciseSelectedListener#onExerciseSelected(java.lang.S)tring)
	 */
	@Override
	public void onExerciseSelected(Exercise exercise) {

		if (exercise != null) {
			contentFragment = LoadingFragment
					.newInstance("Cargando ejercicio " + exercise.getId());

			switchFragment(contentFragment);

			alreadyFetching = true;

			Intent intent = new Intent(this, ExerciseGetService.class);
			String url = wsUrl + wsExercisePath;

			intent.setData(Uri.parse(url));

			// intent.putExtra(ExerciseRESTService.EXTRA_HTTP_VERB,
			// HttpMethod.GET);
			intent.putExtra(ExerciseGetService.EXTRA_HTTP_RESOURCE_ID,
					exercise.getId());

			startService(intent);
		}

	}

	private void initFetchExercises() {

		contentFragment = LoadingFragment
				.newInstance("Cargando ejercicios");

		switchFragment(contentFragment);

		alreadyFetching = true;

		Intent intent = new Intent(this, ExerciseGetService.class);
		String url = wsUrl + wsExercisePath;

		intent.setData(Uri.parse(url));

		// intent.putExtra(ExerciseRESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
		// intent.putExtra(ExerciseGetService.EXTRA_HTTP_RESOURCE_ID, number);

		startService(intent);

	}

	/**
	 * BroadcastReceiver que escucha al servicio que solicita el ejercicio.
	 * 
	 * @author Irving
	 * 
	 */
	class ExerciseReceiver extends BroadcastReceiver {

		/**
		 * Notifica al usuario si el ejercicio no se pudo encontrar, de otra
		 * forma inicia una actividad con el mismo.
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			alreadyFetching = false;

			Bundle extras = intent.getExtras();

			String errorMessage = extras
					.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

			if (errorMessage == null) {

				handleExtras(extras);

			} else {

				Toast.makeText(MainActivity.this, errorMessage,
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, errorMessage);

			}

		}

		private void handleExtras(Bundle extras) {

			// Single exercise
			if (extras.containsKey(Exercise.EXTRA_EXERCISE)) {

				Exercise exercise = extras
						.getParcelable(Exercise.EXTRA_EXERCISE);

				Intent exerciseIntent = new Intent(MainActivity.this,
						ExerciseActivity.class);

				exerciseIntent.putExtra(Exercise.EXTRA_EXERCISE, exercise);

				startActivity(exerciseIntent);

				return;

			}

			if (extras.containsKey(Exercise.EXTRA_EXERCISES)) {

				exercises = extras
						.getParcelableArrayList(Exercise.EXTRA_EXERCISES);

				contentFragment = MainFragment.newInstance(studentId,
						studentName, studentCareer, exercises);

				switchFragment(contentFragment);

			}

		}

	}

	@Override
	public void onLocationChanged(Location location) {

		if (contentFragment instanceof MainFragment)
			((MainFragment) contentFragment).onLocationChanged(location);

	}

}
