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

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.fmat.proyecto3.json.Exercise;

public class ExerciseRESTService extends IntentService {

	private static final String TAG = ExerciseRESTService.class.getName();

	public static final String EXTRA_HTTP_RESOURCE_ID = "EXTRA_HTTP_RESOURCE_ID";
	public static final String EXTRA_HTTP_VERB = "EXTRA_HTTP_VERB";
	public static final String EXTRA_WSRESOURCE = "EXTRA_WSRESOURCE";
	public static final String EXTRA_PARAMS = "EXTRA_PARAMS";
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";

	public ExerciseRESTService() {
		super(TAG);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "Creando servicio!");

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// When an intent is received by this Service, this method
		// is called on a new thread.

		Uri url = intent.getData();
		Bundle extras = intent.getExtras();

		if (extras == null || url == null) {
			// Extras contain our ResultReceiver and data is our REST action.
			// So, without these components we can't do anything useful.
			Log.e(TAG, "You did not pass extras or data with the Intent.");

			return;
		}

		// We default to GET if no verb was specified.
		int verb = extras.getInt(EXTRA_HTTP_VERB, HttpMethod.GET);
		String id = extras.getString(EXTRA_HTTP_RESOURCE_ID);

		String errorMessage = null;

		try {
			// Here we define our base request object which we will
			// send to our REST service via HttpClient.
			HttpRequestBase request = null;

			// Let's build our request based on the HTTP verb we were
			// given.
			switch (verb) {

			case HttpMethod.GET: {
				request = new HttpGet();
				attachUriWithQuery(request, url, id);
				break;
			}

			}// endCase

			if (request != null) {
				HttpClient client = new DefaultHttpClient();

				// Let's send some useful debug information so we can monitor
				// things
				// in LogCat.
				Log.d(TAG,
						"Executing request: " + HttpMethod.verbToString(verb)
								+ ": " + url.toString());

				// Finally, we send our request using HTTP. This is the
				// synchronous
				// long operation that we need to run on this thread.
				HttpResponse response = client.execute(request);

				HttpEntity responseEntity = response.getEntity();

				if (responseEntity != null) {

					Exercise exercise = (Exercise) ResponseHandler.getInstance()
							.handleSingleMessage(responseEntity);

					Log.i(TAG, exercise.toString());
					
				}
			}
		} catch (UnsupportedEncodingException e) {
			errorMessage = "A UrlEncodedFormEntity was created with an unsupported encoding.";
			Log.e(TAG, errorMessage, e);
		} catch (ClientProtocolException e) {
			errorMessage = "There was a problem when sending the request.";
			Log.e(TAG, errorMessage, e);
		} catch (ConnectTimeoutException e) {
			errorMessage = "There's no Internet Connection. Please Verify it.";
			Log.e(TAG, errorMessage, e);
		} catch (IOException e) {
			errorMessage = "There was a problem when sending the request.";
			Log.e(TAG, errorMessage, e);
		} finally {
			if (errorMessage != null) {
			}
		}

	}

	private static void attachUriWithQuery(HttpRequestBase request, Uri uri,
			String id) {

		try {

			String sg;

			sg = uri.getLastPathSegment();

			// Url is pointing to one client, with id.
			if (sg.equals("exercise") && id != null) {

				// Add Client ID to URL
				Uri.Builder uriBuilder = uri.buildUpon();
				uri = uriBuilder.appendPath(id).build();

			}

			request.setURI(new URI(uri.toString()));

		} catch (URISyntaxException e) {
			Log.e(TAG, "URI syntax was incorrect: " + uri.toString(), e);
		}
	}

}
