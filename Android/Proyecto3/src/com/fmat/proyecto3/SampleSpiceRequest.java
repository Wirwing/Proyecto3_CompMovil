package com.fmat.proyecto3;

import java.io.IOException;

import com.fmat.proyecto3.json.Exercise;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

public class SampleSpiceRequest extends GoogleHttpClientSpiceRequest<Exercise> {

	private static final String API = "http://mobileservice.site40.net/api/exercise/";

	private String baseUrl;

	public SampleSpiceRequest(String id) {
		super(Exercise.class);

		this.baseUrl = String.format(API + id);

	}

	@Override
	public Exercise loadDataFromNetwork() throws IOException {

		// Ln.d("Call web service " + baseUrl);
		HttpRequest request = getHttpRequestFactory().buildGetRequest(
				new GenericUrl(baseUrl));

		// request.getHeaders().setContentType("application/json");

		request.setParser(new GsonFactory().createJsonObjectParser());

		HttpResponse response = request.execute();

		Exercise result = response.parseAs(getResultType());

		return result;
	}

}
