package com.fmat.proyecto3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.fmat.proyecto3.fragment.ExerciseResultFragment;
import com.fmat.proyecto3.fragment.LoadingFragment;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;
import com.fmat.proyecto3.service.ExercisePostService;
import com.fmat.proyecto3.service.ExerciseRESTService;

public class ExerciseAnswerActivity extends BaseActivity implements
		ExerciseResultFragment.OnResultListener {

	private static final String TAG = ExerciseAnswerActivity.class.getName();

	private AnswerReceiver receiver;
	private IntentFilter filter;

	private ExerciseAnswer answer;
	private Exercise exercise;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		answer = (ExerciseAnswer) savedInstanceState
				.get(ExerciseAnswer.EXTRA_EXERCISE_ANSWER);

		exercise = (Exercise) savedInstanceState.get(Exercise.EXTRA_EXERCISE);

		loadSettings();

		String[] statements = rearrangeStatementsByKeys(
				exercise.getStatements(), answer.getAnswerKeys());

		Fragment resultFragment = ExerciseResultFragment.newInstance(
				answer.getId(), answer.getDurationInSeconds(), statements);

		setContentView(R.layout.activity_content);

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, resultFragment).commit();

		if (!super.hastAllSettings()) {
			Toast.makeText(this, "Llena primero la configuracion",
					Toast.LENGTH_SHORT).show();
			startActivity(new Intent(this, MainActivity.class));
		}

	}

	private String[] rearrangeStatementsByKeys(String[] statements,
			int[] sortKeys) {

		if (statements == null || sortKeys == null
				|| statements.length - sortKeys.length != 0) {
			throw new IllegalArgumentException(
					"Arrays must have the same length");
		}

		String[] arrangedStaments = new String[statements.length];

		for (int i = 0; i < arrangedStaments.length; i++)
			arrangedStaments[i] = statements[sortKeys[i]];

		return arrangedStaments;

	}

	@Override
	public void onSendAnswer(String comments) {

		answer.setComments(comments);

		answer.setStudentId(studentId);
		answer.setStudentName(studentName);
		answer.setCareer(studentCareer);

		String message = "Enviando respuesta..";

		Fragment messageFragment = LoadingFragment.newInstance(message);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, messageFragment).commit();

		Intent intent = new Intent(this, ExercisePostService.class);

		String url = wsUrl + wsExercisePath;

		intent.setData(Uri.parse(url));

		intent.putExtra(ExercisePostService.EXTRA_EXERCISE_ANSWER, answer);

		startService(intent);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (receiver != null)
			return;

		receiver = new AnswerReceiver();
		filter = new IntentFilter(ExercisePostService.INTENT_RESULT_ACTION);
		super.registerReceiver(receiver, filter);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

	}

	class AnswerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();

			if (!intent.hasExtra(ExerciseRESTService.EXTRA_ERROR_MESSAGE)) {

				Intent mainIntent = new Intent(ExerciseAnswerActivity.this,
						MainActivity.class);

				startActivity(mainIntent);

			} else {

				String errorMessage = extras
						.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

				Log.e(TAG, errorMessage);
			}

		}
	}

}
