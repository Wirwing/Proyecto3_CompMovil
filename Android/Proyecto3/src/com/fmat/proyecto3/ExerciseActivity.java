package com.fmat.proyecto3;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.fmat.proyecto3.fragment.ExerciseDescriptionFragment;
import com.fmat.proyecto3.fragment.ExerciseFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;

/**
 * Actividad encarga del ejercicio que el usuario resuelve.
 */
public class ExerciseActivity extends BaseActivity implements
		ExerciseDescriptionFragment.OnDescriptionListener,
		ExerciseFragment.OnExerciseListener {

	// Ejercicio pasado por parametro
	private Exercise exercise;

	/**
	 * Llamado al crear la actividad.
	 * 
	 * @return nothing
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Recupera el Bundle pasado por argumento, junto a el ejercicio
		 * contenido dentro de este.
		 */
		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		// Recupera el ejercicio
		exercise = (Exercise) savedInstanceState.get(Exercise.EXTRA_EXERCISE);

		// Crea un nuevo fragmento que muestra la descripcion
		Fragment descriptionFragment = ExerciseDescriptionFragment.newInstance(
				exercise.getId(), exercise.getDescription());

		// Lo muestra dentro de la actividad
		switchFragment(descriptionFragment);

	}

	/**
	 * @see com.fmat.proyecto3.fragment.ExerciseFragment.OnExerciseListener#onFinishExcercise(int[],
	 *      long)
	 */
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

	/**
	 * 
	 * @see
	 * com.fmat.proyecto3.fragment.ExerciseDescriptionFragment.OnDescriptionListener
	 * #onStartExcercise()
	 */
	@Override
	public void onStartExcercise() {

		Fragment content = ExerciseFragment.newInstance(exercise
				.getStatements());

		switchFragment(content);

	}

}
