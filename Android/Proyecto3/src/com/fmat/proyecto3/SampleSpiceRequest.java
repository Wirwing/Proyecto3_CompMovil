package com.fmat.proyecto3;

import java.io.IOException;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.gson.GsonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;

public class SampleSpiceRequest extends
		GoogleHttpClientSpiceRequest<Message> {

	private String baseUrl;

	public SampleSpiceRequest(String zipCode) {
		super(Message.class);

		this.baseUrl = "https://graph.facebook.com/me";

		// this.baseUrl = String
		// .format("http://www.myweather2.com/developer/forecast.ashx?uac=AQmS68n6Ku&query=%s&output=json",
		// zipCode);
	}
	
	@Override
	public Message loadDataFromNetwork() throws IOException {
		// Ln.d("Call web service " + baseUrl);
		HttpRequest request = getHttpRequestFactory()//
				.buildGetRequest(new GenericUrl(baseUrl));
		request.setParser(new GsonFactory().createJsonObjectParser());
		return request.execute().parseAs(getResultType());
	}

}
