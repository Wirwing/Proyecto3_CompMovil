package com.fmat.proyecto3.dropbox;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.fmat.proyecto3.R;

/**
 * Permite seleccionar un directorio de dropbox
 * 
 * @author Fabián Castillo
 * 
 */
public class DirectoryChooserActivity extends SherlockListActivity {

	/* Objeto entry de dropbox que representa el dir actual */
	private Entry mCurrentDir;
	/* Adaptador para el listview */
	private EntryListAdapter mAdapter;
	/* API de Dropbox */
	private DropboxAPI<AndroidAuthSession> mDBApi;
	/* Objeto cargador de datos en ejecución */
	private FileListLoader mCurrentLoader = null;

	private String KEY_PREF;
	private String SECRET_PREF;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (KEY_PREF == null && SECRET_PREF == null) {
			KEY_PREF = getString(R.string.dropbox_access_key);
			SECRET_PREF = getString(R.string.dropbox_access_secret);
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		showProgress(true);

		initDropbox();

		// Para evitar el recargar la lista al rotar el dispositivo
		mCurrentDir = (Entry) getLastNonConfigurationInstance();
		if (mCurrentDir != null) {
			updateList(mCurrentDir);
		} else if (mDBApi.getSession().isLinked()) {
			loadAndDisplayDir("/");
		}

	}

	/**
	 * @see com.actionbarsherlock.app.SherlockListActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Entry selectedEntry = mAdapter.getItem(position);
		if (selectedEntry == mCurrentDir) {
			loadAndDisplayDir(selectedEntry.parentPath());
		} else if (selectedEntry.isDir) {
			loadAndDisplayDir(selectedEntry.path);
		}
	}

	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		AndroidAuthSession session = mDBApi.getSession();
		if (session.authenticationSuccessful()) {
			try {
				// terminar la autentificación
				session.finishAuthentication();
				AccessTokenPair tokens = session.getAccessTokenPair();

				storeKeys(tokens.key, tokens.secret);
				loadAndDisplayDir("/");
			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
				Toast.makeText(this, "Couldn't authenticate to dropbox",
						Toast.LENGTH_LONG).show();
				finish();
			}
		}
		super.onResume();
	}

	/**
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		if (mCurrentDir != null && !mCurrentDir.path.equals("/")) {
			loadAndDisplayDir(mCurrentDir.parentPath());
		} else {
			finish();
		}
	}

	/**
	 * @see android.app.Activity#onRetainNonConfigurationInstance()
	 */
	@Override
	public Object onRetainNonConfigurationInstance() {
		return mCurrentDir;
	}

	private void loadAndDisplayDir(String path) {
		if (mCurrentLoader != null && !mCurrentLoader.isCancelled()) {
			mCurrentLoader.cancel(true);
		}
		setProgressBarIndeterminateVisibility(true);
		FileListLoader loader = new FileListLoader();

		loader.execute(path);
		mCurrentLoader = loader;
	}

	private void updateList(Entry entry) {
		List<Entry> directories = new ArrayList<Entry>();
		for (Entry childEntry : entry.contents) {
			if (childEntry.isDir) {
				directories.add(childEntry);
			}
		}

		Entry parent = entry;
		if (entry.path.equals("/")) {
			// Ponemos al root como primer elemento de la lista
			parent = null;
			directories.add(0, entry);
		}

		EntryListAdapter adapter = new EntryListAdapter(this, directories,
				parent);
		setListAdapter(adapter);
		mAdapter = adapter;

		showProgress(false);
	}

	private Entry getEntry(String path) {
		try {
			return mDBApi.metadata(path, 0, null, true, null);
		} catch (DropboxException e) {
			Log.e("DropboxFileChooser", "Something went wrong", e);
		}
		return null;
	}

	private void initDropbox() {
		// Se obtiene el objeto DropboxAPI
		mDBApi = DropboxAPIFactory.getDropboxAPI();

		// Se obtienen los tokens almacenados
		AccessTokenPair accessTokens = getStoredKeys();
		if (accessTokens != null) {
			mDBApi.getSession().setAccessTokenPair(accessTokens);
		} else {
			// Si no hay tokens almacenados se inicia el proceso
			// de petición de datos para autenticación
			mDBApi.getSession().startAuthentication(this);
		}
	}

	/**
	 * Guarda las claves proporcionadas para poder establecer automáticamente
	 * sesiones con dropbox en el futuro
	 * 
	 * @param key
	 *            token key
	 * @param secret
	 *            token secret
	 */
	private void storeKeys(String key, String secret) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(KEY_PREF, key);
		editor.putString(SECRET_PREF, secret);
		editor.commit();
	}

	/**
	 * Obtiene las claves almacenadas para establecer la sesión de dropbox
	 * 
	 * @return objeto AccessTokenPair con la información almacenada de las
	 *         claves key y secret de dropbox, devolverá un objeto null si no se
	 *         encuentran almacenadas dichas claves
	 */
	private AccessTokenPair getStoredKeys() {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String key = preferences.getString(KEY_PREF, null);
		String secret = preferences.getString(SECRET_PREF, null);
		if (key != null && secret != null) {
			return new AccessTokenPair(key, secret);
		}
		return null;
	}

	/**
	 * Activa el progress bar
	 * 
	 */
	private void showProgress(boolean visibility) {
		setSupportProgressBarIndeterminateVisibility(visibility);
	}

	/**
	 * Realiza carga de los datos de un directorio de dropbox
	 * 
	 * @author Fabián Castillo
	 * 
	 */
	private class FileListLoader extends AsyncTask<String, Integer, Entry> {

		@Override
		protected Entry doInBackground(String... params) {
			showProgress(true);
			String path = params[0];
			Entry entry = getEntry(path);
			return entry;
		}

		@Override
		protected void onPostExecute(Entry result) {
			if (!isCancelled()) {
				mCurrentDir = result;
				updateList(result);
			}
		}

	}

}