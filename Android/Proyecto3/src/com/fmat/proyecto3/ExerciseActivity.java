package com.fmat.proyecto3;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.fmat.proyecto3.fragment.ExerciseDescriptionFragment;
import com.fmat.proyecto3.fragment.ExerciseFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;
import com.fmat.proyecto3.service.LocationTrackingHandler;
import com.fmat.proyecto3.todoist.Item;
import com.fmat.proyecto3.todoist.Todoist;
import com.fmat.proyecto3.todoist.TodoistException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Actividad encarga del ejercicio que el usuario resuelve.
 */
public class ExerciseActivity extends BaseActivity implements
		ExerciseDescriptionFragment.OnDescriptionListener,
		ExerciseFragment.OnExerciseListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	// Ejercicio pasado por parametro
	private Exercise exercise;
	private LocationClient client;

	private LocationRequest locationRequest;
	private Location location;

	/**
	 * Llamado al crear la actividad.
	 * 
	 * @return nothing
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		client = new LocationClient(this, this, this);

		/*
		 * Recupera el Bundle pasado por argumento, junto a el ejercicio
		 * contenido dentro de este.
		 */
		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		// Recupera el ejercicio
		exercise = (Exercise) savedInstanceState.get(Exercise.EXTRA_EXERCISE);

		// Crea un nuevo fragmento que muestra la descripcion
		Fragment descriptionFragment = ExerciseDescriptionFragment.newInstance(
				exercise.getId(), exercise.getDescription(), exercise.getDate());

		// Lo muestra dentro de la actividad
		switchFragment(descriptionFragment);

		// Create a new global location parameters object
		locationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		locationRequest
				.setInterval(LocationTrackingHandler.UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		locationRequest
				.setFastestInterval(LocationTrackingHandler.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		client.connect();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		client.removeLocationUpdates(this);
		client.disconnect();

	}

	/**
	 * @see com.fmat.proyecto3.fragment.ExerciseFragment.OnExerciseListener#onFinishExcercise(int[],
	 *      long)
	 */
	@Override
	public void onFinishExcercise(int[] keys, long millis) {

		int durationInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);

		ExerciseAnswer answer = new ExerciseAnswer();
		answer.setId(exercise.getId());
		answer.setAnswerKeys(keys);
		answer.setDurationInSeconds(durationInSeconds);

		answer.setDate(new Date().getTime());

		while (location == null) {

		}

		double[] rawLoc = new double[] { location.getLatitude(),
				location.getLongitude() };

		answer.setPlace(rawLoc);

		Intent intent = new Intent(this, ExerciseAnswerActivity.class);
		intent.putExtra(ExerciseAnswer.EXTRA_EXERCISE_ANSWER, answer);
		intent.putExtra(Exercise.EXTRA_EXERCISE, exercise);
		startActivity(intent);

	}

	/**
	 * 
	 * @see com.fmat.proyecto3.fragment.ExerciseDescriptionFragment.OnDescriptionListener
	 *      #onStartExcercise()
	 */
	@Override
	public void onStartExcercise() {

		Fragment content = ExerciseFragment.newInstance(exercise
				.getStatements());

		switchFragment(content);

	}

	@Override
	public void onConnected(Bundle arg0) {

		client.requestLocationUpdates(locationRequest, this);

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {

		this.location = location;

	}

	@Override
	public void onScheduleExercise() {
		
		final ProgressDialog dialog = ProgressDialog.show(this, "Calendarizando",
				"Enviando ejercicio a Todoist", true, false);
		
		AsyncTask<Exercise, Void, Boolean> task = new AsyncTask<Exercise, Void, Boolean>(){

			@Override
			protected Boolean doInBackground(Exercise... exercises) {
				Exercise exercise = exercises[0];
				
				SharedPreferences prefs =  PreferenceManager.getDefaultSharedPreferences(ExerciseActivity.this);
				String token = prefs.getString(getString(R.string.pref_todoist_token), null);
				int projectId = prefs.getInt(getString(R.string.pref_todoist_project_id), -1);
				String content = exercise.getTitle() +  ": " + exercise.getDescription();
				SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				String dateString = dateFormat.format(new Date(exercise.getDate() * 1000));
				
				try{
					Todoist todoist = new Todoist(token);
					Item item = todoist.addItem(projectId, content, dateString);
					if(item != null) return
							true;
				} catch (TodoistException te){
					Log.e(this.toString(), "Todoist: " + te.getMessage());
				}
				return false;
			}
			
		@Override
			protected void onPostExecute(Boolean result) {
				dialog.dismiss();
				super.onPostExecute(result);
			}	
			
		};
		
		task.execute(exercise);

	} // fin onScheduleExercise()
}
