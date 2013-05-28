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

/**
 * Actividad cuando el ejercicio ha sido resuelto. Maneja los comentarios del
 * mismo, e intenta enviar los resultados via Servicio, a los cuales escucha
 * cuando envian un Broadcast indicando el estado de las peticiones e informa al
 * usuario de las mismas.
 * 
 * @author Irving
 * 
 */
public class ExerciseAnswerActivity extends BaseActivity implements
		ExerciseResultFragment.OnResultListener {

	private static final String TAG = ExerciseAnswerActivity.class.getName();

	private SendAnswerReceiver receiver;
	private UploadToDropBoxReceiver dropboxReceiver;

	private IntentFilter filter;

	private ExerciseAnswer answer;
	private Exercise exercise;

	private Fragment contentFragment;

	private boolean answerAlreadyOnWS;
	private boolean sendToDropbox;

	/**
	 * @see com.fmat.proyecto3.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null)
			savedInstanceState = getIntent().getExtras();

		answer = (ExerciseAnswer) savedInstanceState
				.get(ExerciseAnswer.EXTRA_EXERCISE_ANSWER);

		exercise = (Exercise) savedInstanceState.get(Exercise.EXTRA_EXERCISE);

		String[] statements = StatementSorter.rearrangeStatementsByKeys(
				exercise.getStatements(), answer.getAnswerKeys());

		contentFragment = ExerciseResultFragment.newInstance(answer.getId(),
				answer.getDurationInSeconds(), statements);

		switchFragment(contentFragment);

		answerAlreadyOnWS = false;

	}

	/**
	 * @see com.fmat.proyecto3.fragment.ExerciseResultFragment.OnResultListener#onSendAnswer(java.lang.String,
	 *      boolean)
	 */
	@Override
	public void onSendAnswer(String comments, boolean sendToDropbox) {

		this.sendToDropbox = sendToDropbox;

		answer.setComments(comments);

		answer.setStudentId(studentId);
		answer.setStudentName(studentName);
		answer.setCareer(studentCareer);

		String message = "Enviando respuesta..";

		Fragment messageFragment = LoadingFragment.newInstance(message);
		switchFragment(messageFragment);

		// Start WS Service

		if (answerAlreadyOnWS && sendToDropbox) {

			startUpload();

		} else {

			Intent intent = new Intent(this, ExercisePostService.class);
			String url = wsUrl + wsExercisePath;
			intent.setData(Uri.parse(url));
			intent.putExtra(ExercisePostService.EXTRA_EXERCISE_ANSWER, answer);
			startService(intent);

		}

	}

	/**
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (receiver != null)
			return;

		if (dropboxReceiver != null)
			return;

		receiver = new SendAnswerReceiver();
		filter = new IntentFilter(ExercisePostService.INTENT_RESULT_ACTION);
		super.registerReceiver(receiver, filter);

		dropboxReceiver = new UploadToDropBoxReceiver();
		filter = new IntentFilter(DropboxUploadService.INTENT_RESULT_ACTION);
		super.registerReceiver(dropboxReceiver, filter);

	}

	/**
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}

		if (dropboxReceiver != null) {
			unregisterReceiver(dropboxReceiver);
			dropboxReceiver = null;
		}

	}

	/**
	 * Subclase de BroadcastReceiver que escucha las notificaciones de los
	 * servicios.
	 * 
	 * @author Irving
	 * 
	 */
	class SendAnswerReceiver extends BroadcastReceiver {

		/**
		 * Al recibir respuesta del servicio, le indica al usuario si hubo algun error, de otro modo inicia 
		 * la subida del ejercicio a Dropbox
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();

			if (!intent.hasExtra(ExerciseRESTService.EXTRA_ERROR_MESSAGE)) {

				if (extras.getInt(ExercisePostService.EXTRA_RESULT_CODE) == ExercisePostService.EXERCISE_ALREADY_SOLVED_BY_USER_SERVER_RESPONSE_CODE) {
					answerAlreadyOnWS = true;
				}

				if (sendToDropbox) {
					startUpload();
				} else {

					if (answerAlreadyOnWS)
						Toast.makeText(
								ExerciseAnswerActivity.this,
								"Ya has enviado una respuesta para este ejercicio!",
								Toast.LENGTH_SHORT).show();

					Intent mainIntent = new Intent(ExerciseAnswerActivity.this,
							MainActivity.class);

					startActivity(mainIntent);
				}

			} else {

				String errorMessage = extras
						.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

				Log.e(TAG, errorMessage);

				Toast.makeText(ExerciseAnswerActivity.this, errorMessage,
						Toast.LENGTH_SHORT).show();
				switchFragment(contentFragment);

			}

		}

	}

	private void startUpload() {

		Intent dropboxIntent = new Intent(ExerciseAnswerActivity.this,
				DropboxUploadService.class);

		dropboxIntent.putExtra(ExerciseAnswer.EXTRA_EXERCISE_ANSWER, answer);
		dropboxIntent.putExtra(Exercise.EXTRA_EXERCISE, exercise);

		startService(dropboxIntent);

	}

	/**
	 * Subclase de BroadcastReceiver que escucha las notificaciones de los
	 * servicios.
	 * 
	 * @author Irving
	 * 
	 */
	class UploadToDropBoxReceiver extends BroadcastReceiver {

		/**
		 * Al finalizar el servicio de subida de ejercicio a Dropbox, indica al
		 * usuario si fue correcto mandandolo a la pantalla principal; de otro
		 * modo, le despliega el error en pantalla.
		 */
		@Override
		public void onReceive(Context context, Intent intent) {

			Bundle extras = intent.getExtras();

			if (!intent.hasExtra(DropboxUploadService.EXTRA_ERROR_MESSAGE)) {

				Intent mainIntent = new Intent(ExerciseAnswerActivity.this,
						MainActivity.class);

				startActivity(mainIntent);

			} else {

				String errorMessage = extras
						.getString(ExerciseRESTService.EXTRA_ERROR_MESSAGE);

				Log.e(TAG, errorMessage);

				Toast.makeText(ExerciseAnswerActivity.this, errorMessage,
						Toast.LENGTH_SHORT).show();
				switchFragment(contentFragment);

			}

		}

	}

}
