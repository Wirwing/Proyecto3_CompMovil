package com.fmat.proyecto3;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

import com.octo.android.robospice.GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class MainActivity extends Activity {

	private static final String JSON_CACHE_KEY = "tweets_json";
	private SpiceManager spiceManager = new SpiceManager(
			SampleSpiceService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);

		refreshTweets();
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}

	public void refreshTweets() {

		spiceManager.execute(new SampleSpiceRequest("75000"), JSON_CACHE_KEY,
				DurationInMillis.ONE_MINUTE, new WeatherRequestListener());

	}

	public final class WeatherRequestListener implements
			RequestListener<Message> {

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			Toast.makeText(MainActivity.this, "failure",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onRequestSuccess(final Message result) {
			Toast.makeText(MainActivity.this, "success",
					Toast.LENGTH_SHORT).show();
//			String originalText = getString(R.string.textview_text);
//			mLoremTextView.setText(originalText
//					+ result.getWeather().getCurren_weather().get(0).getTemp());
		}
	}

}
