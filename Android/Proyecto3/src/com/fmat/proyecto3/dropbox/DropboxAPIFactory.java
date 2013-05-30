package com.fmat.proyecto3.dropbox;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;

/**
 * Creación del objeto DropboxAPI
 * 
 * @author Fabián Castillo
 * 
 */
public class DropboxAPIFactory {

	private final static String APP_KEY = "51n0mgo2szoy31k";
	private final static String APP_SECRET = "jr18rre2loe9zhe";
	private final static AccessType ACCESS_TYPE = AccessType.DROPBOX;

	/**
	 * Obtiene un objeto DropboxAPI
	 * 
	 * @return objeto DropboxAPI
	 */
	public static DropboxAPI<AndroidAuthSession> getDropboxAPI() {

		AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
		AndroidAuthSession session = new AndroidAuthSession(appKeys,
				ACCESS_TYPE);

		return new DropboxAPI<AndroidAuthSession>(session);

	}

	/**
	 * Obtiene un objeto DropboxAPI con una sesión con los tokens proporcionados
	 * como parámetro
	 * 
	 * @param key
	 *            token key
	 * @param secret
	 *            token secret
	 * @return
	 */
	public static DropboxAPI<AndroidAuthSession> getDropboxAPI(String key,
			String secret) {
		
		DropboxAPI<AndroidAuthSession> dropBoxAPI = getDropboxAPI();
		AccessTokenPair accessTokenPair = new AccessTokenPair(key, secret);
		dropBoxAPI.getSession().setAccessTokenPair(accessTokenPair);
		
		return dropBoxAPI;
		
	}

}
