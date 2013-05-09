package com.fmat.proyecto3.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseFactory;

public class ExerciseGetService extends ExerciseRESTService {

	private static final String TAG = ExerciseGetService.class.getName();

	public static final String INTENT_RESULT_ACTION = "com.fmat.REST_GET_RESULT";
	public static final String EXTRA_HTTP_RESOURCE_ID = "EXTRA_HTTP_RESOURCE_ID";

	public ExerciseGetService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// When an intent is received by this Service, this method
		// is called on a new thread.

		super.onHandleIntent(intent);

		if (!super.hasExtras())
			return;

		// We default to GET if no verb was specified.
		String id = extras.getString(EXTRA_HTTP_RESOURCE_ID);

		String errorMessage = null;
		Intent resultIntent = new Intent(INTENT_RESULT_ACTION);

		try {
			// Here we define our base request object which we will
			// send to our REST service via HttpClient.
			HttpRequestBase request = new HttpGet();
			attachUriWithQuery(request, url, id);

			HttpClient client = new DefaultHttpClient();

			// Finally, we send our request using HTTP. This is the
			// synchronous
			// long operation that we need to run on this thread.
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {

				HttpEntity responseEntity = response.getEntity();

				if (responseEntity != null) {

					Exercise exercise = ExerciseFactory
							.unmarshallExercise(responseEntity);

					Log.i(TAG, exercise.toString());
					resultIntent.putExtra(Exercise.EXTRA_EXERCISE, exercise);
				}

			} else {
				errorMessage = "No existe ejercicio con ID " + id;
			}
			
		} catch (UnsupportedEncodingException e) {
			errorMessage = "La direccion del servicio es invalida.";
			// Log.e(TAG, errorMessage, e);
		} catch (URISyntaxException e) {
			errorMessage = "La direccion del servicio es invalida.";
		} catch (ClientProtocolException e) {
			errorMessage = "Hubo un problema al contactar al servidor.";
			// Log.e(TAG, errorMessage, e);
		} catch (ConnectTimeoutException e) {
			errorMessage = "No hay conexion a Internet.";
			// Log.e(TAG, errorMessage, e);
		} catch (IOException e) {
			errorMessage = "Hubo un problema al contactar al servidor.";
			// Log.e(TAG, errorMessage, e);
		}

		if (errorMessage != null)
			resultIntent.putExtra(EXTRA_ERROR_MESSAGE, errorMessage);

		sendBroadcast(resultIntent);

	}

	private static void attachUriWithQuery(HttpRequestBase request, Uri uri,
			String id) throws URISyntaxException {

		// Add ID to URL
		Uri.Builder uriBuilder = uri.buildUpon();
		uri = uriBuilder.appendPath(id).build();

		request.setURI(new URI(uri.toString()));

	}

}
