package com.fmat.proyecto3.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public abstract class ExerciseRESTService extends IntentService {

	public static final String EXTRA_WSRESOURCE = "EXTRA_WSRESOURCE";
	public static final String EXTRA_ERROR_MESSAGE = "EXTRA_ERROR_MESSAGE";

	public ExerciseRESTService(String TAG) {
		super(TAG);
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	protected Uri url;
	protected Bundle extras;

	@Override
	protected void onHandleIntent(Intent intent) {
		// When an intent is received by this Service, this method
		// is called on a new thread.

		url = intent.getData();
		extras = intent.getExtras();

	}

	protected boolean hasExtras() {
		return (extras == null || url == null)? false : true;
	}
}
