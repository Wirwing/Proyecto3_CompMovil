package com.fmat.proyecto3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

	private static final String TAG = MainActivity.class.getName();

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

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	// Exercise ex = new Exercise();
	//
	// ex.setId("1");
	// ex.setTitle("Ejercicio 1");
	// ex.setDescription("Este es el primer ejercicio. Ejercicio de prueba.");
	// String[] statements = { "	System.out.println(\"Hello world\");", "}",
	// "public static void main(String[] args){" };
	// ex.setStatements(statements);
	//
	// String jsonObject = new Gson().toJson(ex).toString();
	//
	// Log.i(TAG, jsonObject);

	// Intent intent = new Intent(this, RESTService.class);
	// String url = Constants.WS_URL + Constants.EXERCISE_PATH;
	//
	// intent.setData(Uri.parse(url));
	//
	// intent.putExtra(RESTService.EXTRA_HTTP_VERB, HttpMethod.GET);
	// intent.putExtra(RESTService.EXTRA_HTTP_RESOURCE_ID, "1");
	//
	// startService(intent);

}
