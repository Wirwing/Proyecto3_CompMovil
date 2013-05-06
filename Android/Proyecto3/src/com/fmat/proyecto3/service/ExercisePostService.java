package com.fmat.proyecto3.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Intent;

import com.fmat.proyecto3.json.ExerciseAnswer;
import com.fmat.proyecto3.json.ExerciseFactory;

public class ExercisePostService extends ExerciseRESTService {

	private static final String TAG = ExercisePostService.class.getName();

	public static final String INTENT_RESULT_ACTION = "com.fmat.REST_POST_RESULT";
	
	public static final String EXTRA_RESULT_CODE = "EXTRA_RESULT_CODE";
	public static final String EXTRA_EXERCISE_ANSWER = "EXTRA_EXERCISE_ANSWER";

	public ExercisePostService() {
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

		ExerciseAnswer answer = (ExerciseAnswer) extras
				.get(EXTRA_EXERCISE_ANSWER);

		String message = null;
		Intent resultIntent = new Intent(INTENT_RESULT_ACTION);

		try {
			// Here we define our base request object which we will
			// send to our REST service via HttpClient.
			HttpRequestBase request = new HttpPost();
			request.setURI(new URI(url.toString()));

			HttpClient client = new DefaultHttpClient();

			// Attach form entity if necessary. Note: some REST APIs
			// require you to POST JSON. This is easy to do, simply use
			// postRequest.setHeader('Content-Type', 'application/json')
			// and StringEntity instead. Same thing for the PUT case
			// below.

			HttpPost postRequest = (HttpPost) request;
			postRequest.setHeader("Content-Type", "application/json");

			StringEntity attachedEntity = ExerciseFactory
					.marshallAnswer(answer);

			postRequest.setEntity(attachedEntity);

			// Finally, we send our request using HTTP. This is the
			// synchronous
			// long operation that we need to run on this thread.
			HttpResponse response = client.execute(request);
			// HttpEntity responseEntity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();

			resultIntent.putExtra(EXTRA_RESULT_CODE, statusCode);

		} catch (UnsupportedEncodingException e) {
			message = "A UrlEncodedFormEntity was created with an unsupported encoding.";
			// Log.e(TAG, errorMessage, e);
		} catch (URISyntaxException e) {
			message = "A URISyntaxException was created with an unsupported encoding.";
		} catch (ClientProtocolException e) {
			message = "There was a problem when sending the request.";
			// Log.e(TAG, errorMessage, e);
		} catch (ConnectTimeoutException e) {
			message = "There's no Internet Connection. Please Verify it.";
			// Log.e(TAG, errorMessage, e);
		} catch (IOException e) {
			message = "There was a problem when sending the request.";
			// Log.e(TAG, errorMessage, e);
		}

		if (message != null)
			resultIntent.putExtra(EXTRA_ERROR_MESSAGE, message);

		sendBroadcast(resultIntent);

	}
}
