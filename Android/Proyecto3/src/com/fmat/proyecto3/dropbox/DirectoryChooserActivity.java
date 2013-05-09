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
import com.fmat.proyecto3.R;

/**
 * Permite seleccionar un directorio de dropbox
 * 
 * @author Fabián Castillo
 * 
 */
public class DirectoryChooserActivity extends SherlockListActivity {

	private final String DROPBOX_KEY = getResources().getString(
			R.string.dropbox_access_key);
	private final String DROPBOX_SECRET = getResources().getString(
			R.string.dropbox_access_secret);

	/* Objeto entry de dropbox que representa el dir actual */
	private Entry mCurrentDir;
	/* Adaptador para el listview */
	private EntryListAdapter mAdapter;
	/* API de Dropbox */
	private DropboxAPI<AndroidAuthSession> mDBApi;
	/* Objeto cargador de datos en ejecución */
	private FileListLoader mCurrentLoader = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		showProgress(true);

		if (mDBApi == null)
			initDropbox();

		// Para evitar el recargar la lista al rotar el dispositivo
		mCurrentDir = (Entry) getLastNonConfigurationInstance();
		if (mCurrentDir != null) {
			updateList(mCurrentDir);
		} else if (mDBApi.getSession().isLinked()) {
			loadAndDisplayDir("/");
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Entry selectedEntry = mAdapter.getItem(position);
		if (selectedEntry == mCurrentDir) {
			loadAndDisplayDir(selectedEntry.parentPath());
		} else if (selectedEntry.isDir) {
			loadAndDisplayDir(selectedEntry.path);
		}
	}

	@Override
	protected void onResume() {

		super.onResume();

		loadAndDisplayDir("/");

	}

	@Override
	public void onBackPressed() {
		if (mCurrentDir != null && !mCurrentDir.path.equals("/")) {
			loadAndDisplayDir(mCurrentDir.parentPath());
		} else {
			finish();
		}
	}

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

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(this);

		String key = preferences.getString(DROPBOX_KEY, null);
		String secret = preferences.getString(DROPBOX_SECRET, null);

		if (key != null && secret != null) {

			Toast.makeText(this, "Dropbox no se encuentra configurado",
					Toast.LENGTH_LONG).show();
			finish();
		}

		// Se obtiene el objeto DropboxAPI
		mDBApi = DropboxAPIFactory.getDropboxAPI(key, secret);

	}

	/**
	 * Activa el progress bar
	 * 
	 */
	private void showProgress(boolean visible) {
		setSupportProgressBarIndeterminateVisibility(visible);
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
