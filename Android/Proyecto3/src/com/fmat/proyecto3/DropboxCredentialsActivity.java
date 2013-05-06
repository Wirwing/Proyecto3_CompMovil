package com.fmat.proyecto3;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.fmat.proyecto3.dropbox.DropboxAPIFactory;

public class DropboxCredentialsActivity extends Activity {

	private DropboxAPI<AndroidAuthSession> dropboxApi;

	private SharedPreferences preferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
		dropboxApi = DropboxAPIFactory.getDropboxAPI();
		dropboxApi.getSession().startAuthentication(this);

	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		//Si hay sesion y el usuario logro autentificar correctamente
		if (dropboxApi != null && dropboxApi.getSession().authenticationSuccessful()) {

			// terminar la autentificación
			dropboxApi.getSession().finishAuthentication();
			persistAuth();

		}

	}
	
	/*
	 * Grabar la sesion a las preferencias compartidas
	 */
	private void persistAuth() {

		AccessTokenPair tokens = dropboxApi.getSession().getAccessTokenPair();

		String key = getResources().getString(R.string.dropbox_access_key);
		String secret = getResources()
				.getString(R.string.dropbox_access_secret);

		Editor edit = preferences.edit();
		edit.putString(key, tokens.key);
		edit.putString(secret, tokens.secret);
		edit.commit();
		
		finish();
		
	}

}
