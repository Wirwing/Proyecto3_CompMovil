package com.fmat.proyecto3;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.fmat.proyecto3.fragment.ExerciseDescriptionFragment;
import com.fmat.proyecto3.fragment.ExerciseFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;

public class ExerciseActivity extends BaseActivity implements
		ExerciseDescriptionFragment.OnDescriptionListener,
		ExerciseFragment.OnExerciseListener {

	private static final String TAG = ExerciseActivity.class.getName();

	private Exercise exercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		exercise = (Exercise) savedInstanceState.get(Exercise.EXTRA_EXERCISE);

		Fragment descriptionFragment = ExerciseDescriptionFragment.newInstance(
				exercise.getId(), exercise.getDescription());

		switchFragment(descriptionFragment);

	}

	@Override
	public void onFinishExcercise(int[] keys, long millis) {

		int durationInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(millis);

		ExerciseAnswer answer = new ExerciseAnswer();
		answer.setId(exercise.getId());
		answer.setAnswerKeys(keys);
		answer.setDurationInSeconds(durationInSeconds);

		Intent intent = new Intent(this, ExerciseAnswerActivity.class);
		intent.putExtra(ExerciseAnswer.EXTRA_EXERCISE_ANSWER, answer);
		intent.putExtra(Exercise.EXTRA_EXERCISE, exercise);
		startActivity(intent);

	}

	@Override
	public void onStartExcercise() {

		Fragment content = ExerciseFragment.newInstance(exercise
				.getStatements());

		switchFragment(content);

	}

}
