package com.fmat.proyecto3.todoist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.dropbox.client2.android.AndroidAuthSession;
import com.fmat.proyecto3.R;

/**
 * Permite seleccionar un proyecto de todoist
 * 
 * @author Fabián Castillo
 * 
 */
public class ProjectChooserActivity extends SherlockListActivity {

	/* Adaptador para el listview */
	private ArrayAdapter<Project> mAdapter;
	/* Acceso a Todoist */
	private Todoist mTodoist;

	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		showProgress(true);

		mTodoist = new Todoist();
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String token = preferences.getString(getString(R.string.pref_todoist_token), null);
		if(token == null){
			Intent intent = new Intent(this, LoginActivity.class);
			startActivityForResult(intent, 200);
		} else {
			mTodoist.setToken(token);
			loadProjects();
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
	 * @see android.app.Activity#onActivityResult(android.os.Bundle)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			String token = preferences.getString(getString(R.string.pref_todoist_token), null);
			mTodoist.setToken(token);
			loadProjects();
		} else {
			finish();
		}
	}

	/**
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Project selectedProject = mAdapter.getItem(position);
		
		storeProjectInfo(selectedProject.getId(), selectedProject.getName());
		
		finish();
	}
	
	/**
	 * Inicia la carga de proyectos de todoist
	 */
	protected void loadProjects(){
		ProjectListLoader loader = new ProjectListLoader();
		loader.execute();
		
		setProgressBarIndeterminateVisibility(true);
	}


	private void updateList(Project[] projects) {
		
		ArrayAdapter<Project> adapter = new ArrayAdapter<Project>(this, android.R.layout.simple_list_item_1, projects);
		setListAdapter(adapter);
		mAdapter = adapter;

		showProgress(false);
	}

	/**
	 * Persiste la información sobre un proyecto
	 * 
	 * @param projectId el id del proyecto
	 * @param projectName el nombre del projecto
	 */
	private void storeProjectInfo(int projectId, String projectName) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(getString(R.string.pref_todoist_project_id), projectId);
		editor.putString(getString(R.string.pref_todoist_project_name), projectName);
		editor.commit();
	}


	/**
	 * Activa el progress bar
	 * 
	 */
	private void showProgress(boolean visibility) {
		setSupportProgressBarIndeterminateVisibility(visibility);
	}

	/**
	 * Realiza carga de los datos de los proyectos de todoist
	 * @author Fabián Castillo
	 * 
	 */
	private class ProjectListLoader extends AsyncTask<Void, Integer, Project[]> {

		@Override
		protected Project[] doInBackground(Void... params) {
			showProgress(true);
			Project[] projects = mTodoist.getProjects();
			return projects;
		}

		@Override
		protected void onPostExecute(Project[] result) {
			if (!isCancelled()) {
				updateList(result);
			}
		}

	}

}