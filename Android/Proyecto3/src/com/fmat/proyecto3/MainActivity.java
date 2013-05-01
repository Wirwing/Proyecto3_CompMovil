package com.fmat.proyecto3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.service.ExerciseJsonRequest;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getName();

	private static final String JSON_CACHE_KEY = "tweets_json";

	private SpiceManager spiceManager = new SpiceManager(
			JacksonSpringAndroidSpiceService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);

		refreshTweets();
	}

	@Override
	protected void onStop() {
		super.onStop();
		spiceManager.shouldStop();

	}

	public void refreshTweets() {

		spiceManager.execute(new ExerciseJsonRequest(), JSON_CACHE_KEY,
				DurationInMillis.ONE_SECOND, new TweetRequestListener());

//		Exercise ex = new Exercise();
//
//		ex.setId("1");
//		ex.setTitle("Ejercicio 1");
//		ex.setDescription("Este es el primer ejercicio. Ejercicio de prueba.");
//		String[] statements = { "	System.out.println(\"Hello world\");", "}",
//				"public static void main(String[] args){" };
//		ex.setStatements(statements);
//
//		String jsonObject = new Gson().toJson(ex).toString();
//
//		Log.i(TAG, jsonObject);

		
//		 Intent intent = new Intent(this, RESTService.class);
//		 String url = Constants.WS_URL + Constants.EXERCISE_PATH;
//		
//		 intent.setData(Uri.parse(url));
//		
//		 intent.putExtra(RESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
//		 intent.putExtra(RESTService.EXTRA_HTTP_RESOURCE_ID, "1");
//
//		 startService(intent);
		 
	}
	
	//inner class of your spiced Activity
	private class TweetRequestListener implements RequestListener< Exercise > {

	        @Override
	        public void onRequestFailure( SpiceException spiceException ) {
	          //update your UI
	        	
	        	Log.i(TAG, spiceException.getMessage());
	        	
	        }

	        @Override
	        public void onRequestSuccess( Exercise listTweets ) {
	          //update your UI
	        	
	        	Log.i(TAG, String.valueOf(listTweets == null));
	        	
	        }
	    }
	
}
