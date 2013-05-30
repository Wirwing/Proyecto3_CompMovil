package com.fmat.proyecto3.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

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

import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseFactory;

/**
 * Servicio para la obtención de ejercicios del servidor
 * @author Fabián Castillo
 *
 */
public class ExerciseGetService extends ExerciseRESTService {

	/**
	 * Tag del servicio
	 */
	private static final String TAG = ExerciseGetService.class.getName();

	/**
	 * Acción del servicio
	 */
	public static final String INTENT_RESULT_ACTION = "com.fmat.REST_GET_RESULT";
	/**
	 * Código de acción
	 */
	public static final String EXTRA_HTTP_RESOURCE_ID = "EXTRA_HTTP_RESOURCE_ID";

	/**
	 * Constructor
	 */
	public ExerciseGetService() {
		super(TAG);
	}

	/**
	 * 
	 * @see com.fmat.proyecto3.service.ExerciseRESTService#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
	}

	/**
	 * @see com.fmat.proyecto3.service.ExerciseRESTService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		super.onHandleIntent(intent);
		
		if (extras != null && extras.containsKey(EXTRA_HTTP_RESOURCE_ID)) {
			getSingleExercise(extras.getString(EXTRA_HTTP_RESOURCE_ID));
		} else {
			getExerciseList();
		}

	}

	private void getSingleExercise(String id) {

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

	private void getExerciseList() {

		String errorMessage = null;
		Intent resultIntent = new Intent(INTENT_RESULT_ACTION);

		try {

			// Here we define our base request object which we will
			// send to our REST service via HttpClient.
			HttpRequestBase request = new HttpGet();
			request.setURI(new URI(url.toString()));

			HttpClient client = new DefaultHttpClient();

			// Finally, we send our request using HTTP. This is the
			// synchronous
			// long operation that we need to run on this thread.
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().getStatusCode() == 200) {

				HttpEntity responseEntity = response.getEntity();

				if (responseEntity != null) {

					Exercise[] exercises = ExerciseFactory
							.unmarshallExercises(responseEntity);

					ArrayList<Exercise> exerciseList = new ArrayList<Exercise>(
							Arrays.asList(exercises));

					resultIntent.putParcelableArrayListExtra(
							Exercise.EXTRA_EXERCISES, exerciseList);

				}

			} else {
				errorMessage = "No se pudo recuperar lista de ejercicios";
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
