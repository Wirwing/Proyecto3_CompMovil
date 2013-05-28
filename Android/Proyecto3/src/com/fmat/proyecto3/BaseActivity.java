package com.fmat.proyecto3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

/**
 * Actividad base de la aplicacion. Las demas actividades heredan de esta.
 * Provee utilidades necesarias para las clases hijas.
 * 
 * @author Irving
 * 
 */
public class BaseActivity extends SherlockFragmentActivity {

	private SharedPreferences preferences;

	/**
	 * Preferencias almacenadas para id de estudiante
	 */
	protected String studentId;

	/**
	 * Preferencias almacenadas para nombre de estudiante
	 */
	protected String studentName;

	/**
	 * Preferencias almacenadas para licenciatura de estudiante
	 */
	protected String studentCareer;

	/**
	 * Preferencias almacenadas para carpeta de dropbox seleccionada
	 */
	protected String dropboxFolder;

	/**
	 * Preferencias almacenadas para direccion del servicio web
	 */
	protected String wsUrl;

	/**
	 * Preferencias almacenadas para el recurso de web que expone ejercicios
	 */
	protected String wsExercisePath;

	/**
	 * Llamado al crear la actividad, crea una referencia a las preferencias.
	 * 
	 * @return nothing
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set Convent View
		setContentView(R.layout.activity_content);

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		loadSettings();

		/*
		 * Si no hay preferencias almacenadas, dirige el usuario a la actividad
		 * de ingreso de ellas
		 */
		if (!hasAllSettings()) {
			Toast.makeText(this, "Configura la aplicación primero",
					Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(this, SettingsActivity.class),
					200);
		}

	}

	/**
	 * Intenta obtener los valores almacenados en preferncias. De otro modo,
	 * inicializa estos valores a nulo.
	 * 
	 * @return nothing
	 */
	protected void loadSettings() {

		studentId = preferences.getString(getString(R.string.pref_student_id),
				null);

		studentName = preferences.getString(
				getString(R.string.pref_student_name), null);

		studentCareer = preferences.getString(
				getString(R.string.pref_student_career), null);

		dropboxFolder = preferences.getString(
				getString(R.string.pref_dropbox_dir), null);

		wsUrl = preferences
				.getString(getString(R.string.pref_server_url), null);

		wsExercisePath = preferences.getString(
				getString(R.string.pref_server_get_exercise), null);

	}

	private boolean hasAllSettings() {

		return studentId != null && studentName != null
				&& studentCareer != null && dropboxFolder != null
				&& wsUrl != null && wsExercisePath != null ? true : false;

	}

	/**
	 * Reemplaza la vista actual de la actividad por el fragmento recibido.
	 * 
	 * @param contentFragment            Fragmento a mostrar
	 * 
	 * @return nothing
	 */
	protected void switchFragment(Fragment contentFragment) {

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, contentFragment).commit();

	}

	/**
	 * Carga las referencias una vez que se retorna de la actividad de preferencias,
	 * para corroborar que realmente han sido ingresadas.
	 * 
	 * @return nothing
	 */
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);

		loadSettings();
		if (!hasAllSettings()) {
			Toast.makeText(this, "Configura la aplicación primero",
					Toast.LENGTH_SHORT).show();
			startActivityForResult(new Intent(this, SettingsActivity.class),
					200);
		}
	}

}
