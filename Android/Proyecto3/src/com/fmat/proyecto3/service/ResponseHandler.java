package com.fmat.proyecto3.service;

import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;

import android.util.Log;

import com.fmat.proyecto3.json.Exercise;
import com.google.gson.Gson;

public class ResponseHandler {

	private static final String TAG = ResponseHandler.class.getName();
	private static final Gson gson = new Gson();

	private static class Holder {
		private static final ResponseHandler INSTANCE = new ResponseHandler();
	}

	public static ResponseHandler getInstance() {
		return Holder.INSTANCE;
	}

	public Object handleSingleMessage(HttpEntity responseEntity)
			throws IllegalStateException, IOException {

		InputStreamReader contentReader = new InputStreamReader(
				responseEntity.getContent());

		Exercise exercise = gson.fromJson(contentReader, Exercise.class);

		Log.i(TAG, "Exercise received: " + exercise.toString());

		return exercise;

	}

}
