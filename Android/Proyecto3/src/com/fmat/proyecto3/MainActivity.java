package com.fmat.proyecto3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.fragment.LoadingFragment;
import com.fmat.proyecto3.fragment.MainFragment;

public class MainActivity extends SherlockFragmentActivity implements
		MainFragment.OnExerciseSelectedListener {

	private static final String TAG = MainActivity.class.getName();

	private Fragment contentFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		contentFragment = MainFragment.newInstance("10002900",
				"Azaneth Aguilar", "Nutricion");

		// Set Convent View
		setContentView(R.layout.activity_content);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

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

	@Override
	public void onExerciseSelected(String number) {

		String message = "Loading exercise " + number;

		Log.i(TAG, "Exercise number: " + number);

		Fragment messageFragment = LoadingFragment.newInstance(message);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, messageFragment).commit();

	}

}
