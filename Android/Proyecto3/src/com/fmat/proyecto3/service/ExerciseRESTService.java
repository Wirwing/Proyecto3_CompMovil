package com.fmat.proyecto3.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

/**
 * Clase base para los servicios REST
 * @author Irving Caro
 *
 */
public abstract class ExerciseRESTService extends IntentService {

	/**
	 * Código de la dirección del web service
	 */
	public static final String EXTRA_WSRESOURCE = "EXTRA_WSRESOURCE";
	/**
	 * Código para el mensaje de error
	 */
	public static final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";

	/**
	 * Constructor
	 * @param TAG tag del servicio
	 */
	public ExerciseRESTService(String TAG) {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * Url del servicio
	 */
	protected Uri url;
	/**
	 * Extras del servicio
	 */
	protected Bundle extras;

	@Override
	protected void onHandleIntent(Intent intent) {
		// When an intent is received by this Service, this method
		// is called on a new thread.

		url = intent.getData();
		extras = intent.getExtras();

	}

	/**
	 * Verifica que si se tienen extras
	 * @return true si tiene extras, false en caso contrario
	 */
	protected boolean hasExtras() {
		return (url == null)? false : true;
	}
}
