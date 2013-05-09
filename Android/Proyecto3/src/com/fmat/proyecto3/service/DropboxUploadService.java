package com.fmat.proyecto3.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.fmat.proyecto3.R;
import com.fmat.proyecto3.dropbox.DropboxAPIFactory;
import com.fmat.proyecto3.json.Exercise;
import com.fmat.proyecto3.json.ExerciseAnswer;
import com.fmat.proyecto3.utils.TempFileHandler;

public class DropboxUploadService extends IntentService {

	public static final String INTENT_RESULT_ACTION = "com.fmat.DROPBOX_RESULT";
	public static final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";
	
	private static final String TAG = DropboxUploadService.class.getName();

	private static final String FOLDER_SEPARATION = "/";
	
	private SharedPreferences preferences;

	private String key;
	private String secret;
	private String filePath;

	private DropboxAPI<AndroidAuthSession> dropboxApi;

	public DropboxUploadService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		key = getResources().getString(R.string.dropbox_access_key);
		secret = getResources().getString(R.string.dropbox_access_secret);
		filePath = getResources().getString(R.string.pref_dropbox_dir);

		key = preferences.getString(key, null);
		secret = preferences.getString(secret, null);
		filePath = preferences.getString(filePath, null);

		if (key == null || secret == null || filePath == null || extras == null)
			return;

		Intent resultIntent = new Intent(INTENT_RESULT_ACTION);
		String errorMessage = null;
		
		try {

			dropboxApi = DropboxAPIFactory.getDropboxAPI(key, secret);

			Exercise exercise = (Exercise) extras.get(Exercise.EXTRA_EXERCISE);
			ExerciseAnswer answer = (ExerciseAnswer) extras
					.get(ExerciseAnswer.EXTRA_EXERCISE_ANSWER);

			Calendar calendar = Calendar.getInstance();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
					Locale.getDefault());

			String fileName = String.format("%s_%s_%s.txt", exercise.getId(),
					answer.getStudentId(), sdf.format(calendar.getTime()));

			File file = new File(getExternalFilesDir(null), fileName);

			BufferedWriter bwriter = new BufferedWriter(new FileWriter(file));
			TempFileHandler.createTempFile(bwriter, exercise, answer);

			FileInputStream inputStream = new FileInputStream(file);
			
			if(!filePath.equalsIgnoreCase("/")){
				filePath += FOLDER_SEPARATION;
			}
			
			dropboxApi.putFileOverwrite(filePath + fileName, inputStream,
					file.length(), null);

			file.delete();

		} catch (ClassCastException ce) {
			errorMessage = "La direccion del servicio es invalida.";
			
		} catch (IOException ioe) {
			errorMessage = "Hubo un problema al generar archivo.";
		} catch (DropboxUnlinkedException e) {
			errorMessage = "No tienes una cuenta de Dropbox configurada.";
		} catch (DropboxException e) {
			errorMessage = "Hubo un problema al subir el archivo a tu cuenta de Dropbox.";
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		if(errorMessage != null){
			Log.e(TAG, errorMessage);
			resultIntent.putExtra(EXTRA_ERROR_MESSAGE, errorMessage);
		}
		
		sendBroadcast(resultIntent);

	}
	
	
}
