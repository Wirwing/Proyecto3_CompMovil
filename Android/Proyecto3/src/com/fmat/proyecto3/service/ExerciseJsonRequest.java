package com.fmat.proyecto3.service;

import com.fmat.proyecto3.json.Exercise;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public class ExerciseJsonRequest extends SpringAndroidSpiceRequest<Exercise> {

	public ExerciseJsonRequest() {
		super(Exercise.class);
	}

	@Override
	public Exercise loadDataFromNetwork() throws Exception {
		return getRestTemplate().getForObject(
				"http://mobileservice.site40.net/api/1", Exercise.class);
	}

}
