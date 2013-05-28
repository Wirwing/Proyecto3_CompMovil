package com.fmat.proyecto3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

/**
 * Clase principal que administra las opciones principales de la aplicacion: Iniciar un ejercicio y 
 * establecer configuracion.
 * @author Irving
 *
 */
public class MainActivity extends BaseActivity implements
		MainFragment.OnExerciseSelectedListener {

	private static final String TAG = MainActivity.class.getName();

	private Fragment contentFragment;

	private ExerciseReceiver receiver;
	private IntentFilter filter;

	/**
	 * @see com.fmat.proyecto3.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		if (contentFragment == null)
			contentFragment = MainFragment.newInstance(studentId, studentName,
					studentCareer);

		switchFragment(contentFragment);

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
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onMenuItemSelected(int, android.view.MenuItem)
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
	 * @see com.fmat.proyecto3.BaseActivity#onActivityResult(int, int, android.content.Intent)
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
	 * @see com.fmat.proyecto3.fragment.MainFragment.OnExerciseSelectedListener#onExerciseSelected(java.lang.String)
	 */
	@Override
	public void onExerciseSelected(String number) {

		String message = "Cargando ejercicio " + number;

		Log.i(TAG, "Exercise number: " + number);

		Fragment messageFragment = LoadingFragment.newInstance(message);

		switchFragment(messageFragment);

		Intent intent = new Intent(this, ExerciseGetService.class);
		String url = wsUrl + wsExercisePath;

		intent.setData(Uri.parse(url));

		// intent.putExtra(ExerciseRESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
		intent.putExtra(ExerciseGetService.EXTRA_HTTP_RESOURCE_ID, number);

		startService(intent);

	}

	/**
	 * BroadcastReceiver que escucha al servicio que solicita el ejercicio.
	 * @author Irving
	 *
	 */
	class ExerciseReceiver extends BroadcastReceiver {

		/**
		 * Notifica al usuario si el ejercicio no se pudo encontrar, de otra forma inicia una actividad con el mismo.
		 */
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

					exerciseIntent.putExtra(Exercise.EXTRA_EXERCISE, exercise);

					startActivity(exerciseIntent);

				}

			} else {
				Toast.makeText(MainActivity.this, errorMessage,
						Toast.LENGTH_SHORT).show();
				Log.e(TAG, errorMessage);

				switchFragment(contentFragment);

			}

		}
	}

}
