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
import com.fmat.proyecto3.service.DropboxUploadService;
import com.fmat.proyecto3.service.ExercisePostService;
import com.fmat.proyecto3.service.ExerciseRESTService;
import com.fmat.proyecto3.utils.StatementSorter;

public class ExerciseAnswerActivity extends BaseActivity implements
		ExerciseResultFragment.OnResultListener {

	private static final String TAG = ExerciseAnswerActivity.class.getName();

	private AnswerReceiver receiver;
	private DropBoxReceiver dropReceiver;

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

		String[] statements = StatementSorter.rearrangeStatementsByKeys(
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

		// Start WS Service
		Intent intent = new Intent(this, ExercisePostService.class);
		String url = wsUrl + wsExercisePath;
		intent.setData(Uri.parse(url));
		intent.putExtra(ExercisePostService.EXTRA_EXERCISE_ANSWER, answer);
		startService(intent);

		receiver.startUpload();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (receiver != null)
			return;

		if (dropReceiver != null)
			return;

		receiver = new AnswerReceiver();
		filter = new IntentFilter(ExercisePostService.INTENT_RESULT_ACTION);
		super.registerReceiver(receiver, filter);

		dropReceiver = new DropBoxReceiver();
		filter = new IntentFilter(DropboxUploadService.INTENT_RESULT_ACTION);
		super.registerReceiver(dropReceiver, filter);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

		if (dropReceiver != null) {
			unregisterReceiver(dropReceiver);
			dropReceiver = null;
		}

	}

	class AnswerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();

			if (!intent.hasExtra(ExerciseRESTService.EXTRA_ERROR_MESSAGE)) {

				startUpload();

			} else {

				String errorMessage = extras
						.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

				Log.e(TAG, errorMessage);
			}

		}

		protected void startUpload() {

			Intent dropboxIntent = new Intent(ExerciseAnswerActivity.this,
					DropboxUploadService.class);

			dropboxIntent
					.putExtra(ExerciseAnswer.EXTRA_EXERCISE_ANSWER, answer);
			dropboxIntent.putExtra(Exercise.EXTRA_EXERCISE, exercise);

			startService(dropboxIntent);

		}

	}

	class DropBoxReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			Intent mainIntent = new Intent(ExerciseAnswerActivity.this,
					MainActivity.class);

			startActivity(mainIntent);

		}

	}

}
