package com.fmat.proyecto3;

import java.util.concurrent.TimeUnit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.fragment.ExerciseDescriptionFragment;
import com.fmat.proyecto3.fragment.ExerciseFragment;
import com.fmat.proyecto3.json.Exercise;

public class ExerciseActivity extends SherlockFragmentActivity implements
		ExerciseDescriptionFragment.OnDescriptionListener,
		ExerciseFragment.OnExerciseListener {

	private static final String TAG = ExerciseActivity.class.getName();
	
	public static final String EXTRA_EXERCISE = "EXTRA_EXERCISE";
	public static final String EXTRA_NUMBER = "EXTRA_EXERCISE";
	public static final String EXTRA_DEGREE = "EXTRA_EXERCISE";

	private String studentNumber;
	private String name;
	private String degree;
	
	private Exercise exercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		exercise = (Exercise) savedInstanceState.get(EXTRA_EXERCISE);

		Fragment descriptionFragment = ExerciseDescriptionFragment.newInstance(
				exercise.getId(), exercise.getDescription());

		setContentView(R.layout.activity_content);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, descriptionFragment).commit();

	}

	@Override
	public void onFinishExcercise(int[] keys, long millis) {

		String elapsedTime = String.format(
				"%d min, %d sec",
				TimeUnit.MILLISECONDS.toMinutes(millis),
				TimeUnit.MILLISECONDS.toSeconds(millis)
						- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
								.toMinutes(millis)));

		Log.i(TAG, elapsedTime);
		
	}

	@Override
	public void onStartExcercise() {

		Fragment content = ExerciseFragment.newInstance(exercise
				.getStatements());
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, content).commit();
	}

}
