package com.fmat.proyecto3.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.utils.ErrorDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Servicio de resolución de localización
 * 
 * @author Irving Caro
 * 
 */
public class LocationTrackingHandler implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	/*
	 * Constants for location update parameters
	 */
	/**
	 * Milisegundos por segundo
	 */
	public static final int MILLISECONDS_PER_SECOND = 1000;

	/**
	 * Intervalo de actualización
	 */
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	/**
	 * Intervalo de límite rápido
	 */
	public static final int FAST_CEILING_IN_SECONDS = 1;

	/**
	 * Intervalo de actualización en milisegundos
	 */
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;

	/**
	 * Límite de intervalos de actualización, usado cuando la aplicación es
	 * visible
	 */
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;

	private static final String TAG = LocationTrackingHandler.class.getName();

	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	private LocationClient locationClient;
	private LocationRequest locationRequest;

	private OnLocationTrackingLocationChanged listener;

	/**
	 * Constructor
	 * @param listener Escuchador de cambios de localización
	 */
	public LocationTrackingHandler(OnLocationTrackingLocationChanged listener) {

		this.listener = listener;

		locationClient = new LocationClient((Context) listener, this, this);

		// Create a new global location parameters object
		locationRequest = LocationRequest.create();

		/*
		 * Set the update interval
		 */
		locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

		// Use high accuracy
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

		// Set the interval ceiling to one minute
		locationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

	}

	/**
	 * Evento de conexión
	 */
	public void onConnect() {

		locationClient.connect();

	}

	/**
	 * Event de desconexión
	 */
	public void onDisconnect() {

		// If the client is connected
		if (locationClient.isConnected()) {
			stopPeriodicUpdates();
		}
		/*
		 * After disconnect() is called, the client is considered "dead".
		 */
		locationClient.disconnect();

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		startPeriodicUpdates();
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	/**
	 * In response to a request to start updates, send a request to Location
	 * Services
	 */
	private void startPeriodicUpdates() {

		locationClient.requestLocationUpdates(locationRequest, this);
	}

	/**
	 * In response to a request to stop updates, send a request to Location
	 * Services
	 */
	private void stopPeriodicUpdates() {
		locationClient.removeLocationUpdates(this);
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub

		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult((Activity) listener,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			/*
			 * If no resolution is available, display a dialog to the user with
			 * the error.
			 */
			showErrorDialog(connectionResult.getErrorCode());
		}

	}

	/**
	 * Show a dialog returned by Google Play services for the connection error
	 * code
	 * 
	 * @param errorCode
	 *            An error code returned from onConnectionFailed
	 */
	private void showErrorDialog(int errorCode) {

		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				(Activity) listener, CONNECTION_FAILURE_RESOLUTION_REQUEST);

		// If Google Play services can provide an error dialog
		if (errorDialog != null) {

			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();

			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);

			// Show the error dialog in the DialogFragment
			errorFragment.show(((SherlockFragmentActivity) listener)
					.getSupportFragmentManager(), TAG);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		listener.onLocationChanged(location);
	}

	/**
	 * Esta interfaz debe ser implementada por las actividades que llaman a este
	 * fragmento, para poder comunicarse con la actividad.
	 */
	public interface OnLocationTrackingLocationChanged {
		/**
		 * Callback cuando se elige un ejercicio
		 * 
		 * @param number
		 *            Id del ejercicio
		 * 
		 * @return nothing
		 */
		public void onLocationChanged(Location location);
	}

}
