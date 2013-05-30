package com.fmat.proyecto3;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fmat.proyecto3.fragment.ExerciseDescriptionFragment;
import com.fmat.proyecto3.fragment.ExerciseFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;
import com.fmat.proyecto3.service.LocationTrackingHandler;
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

	// Solicitante de cambios de ubicacion
	private LocationRequest locationRequest;

	// Ubicacion obtenida del GPS
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
		Fragment descriptionFragment = ExerciseDescriptionFragment
				.newInstance(exercise.getId(), exercise.getDescription(),
						exercise.getDate());

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
		super.onStart();

		// conectar cliente
		client.connect();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

		// quitar notificaciones y desconectar cliente
		client.removeLocationUpdates(this);
		client.disconnect();

	}

	/**
	 * @see com.fmat.proyecto3.fragment.ExerciseFragment.OnExerciseListener#onFinishExcercise(int[],
	 *      long)
	 */
	@Override
	public void onFinishExcercise(int[] keys, long millis) {

		//Obtener duracion de la resolucion del ejercicio 
		int durationInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);

		//Establecer los valores de respuesta
		ExerciseAnswer answer = new ExerciseAnswer();
		answer.setId(exercise.getId());
		answer.setAnswerKeys(keys);
		answer.setDurationInSeconds(durationInSeconds);

		answer.setDate(new Date().getTime());

		location = client.getLastLocation();
		
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

	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {

		client.requestLocationUpdates(locationRequest, this);

	}

	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener#onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks#onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.google.android.gms.location.LocationListener#onLocationChanged(android.location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {

		this.location = location;

	}
	
}
